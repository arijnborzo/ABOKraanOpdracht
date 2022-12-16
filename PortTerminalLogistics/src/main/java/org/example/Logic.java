package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Logic
{
    ArrayList<Slot> slots ;
    ArrayList <Container> containers ;
    ArrayList <Cranes> cranes ;
    ArrayList <int[]> EndPosition ;
    ArrayList <int[]> schedule;
    ArrayList <Movement> movements;
    HashMap<String,Integer> assignments ;
    int hMax ;
    int hTarget;
    int lenghtRaster;
    int widthRaster;
    int tijd = 0;

    public void readFile(boolean changeHeight) throws IOException {

        FileParser fileParser = new FileParser();
        fileParser.parseFile("src/main/MH2Terminal_20_10_3_2_100.json");
        if(!changeHeight){
            FileparserEnd fileparser2 = new FileparserEnd();
            fileparser2.parseFile("src/main/input2einde.json");
            EndPosition = fileparser2.assignments;
        }
        slots = fileParser.slots;
        containers = fileParser.containers;
        assignments = fileParser.assignments;
        cranes = fileParser.cranes;
        hMax = fileParser.hMax;
        hTarget = fileParser.targetHeight;
        lenghtRaster = fileParser.length;
        widthRaster = fileParser.width;;

    }

    public Movement move(Container c, int sId)
    {
        // kijken als a kan opgepakt worden
        Slot initSlotC = slots.get(c.getSlotId());
        if(assignments.containsKey(c.getSlotId()+","+c.getSlotH()+1))
        {
            move(containers.get(assignments.get(c.getSlotId()+","+c.getSlotH()+1)),getFreeSpot(c,hMax));
        }
        if(getSlotHeight(sId)>=hMax)
        {
            move(containers.get(assignments.get(sId+","+getSlotHeight(sId))),getFreeSpot(c,hMax));
        }
        // verplaats container
        Cranes crane = cranes.get(0);

        Movement van = new Movement(crane.getCraneId(),-1,-1,-1,tijd,crane.getPositie(),slots.get(c.getSlotId()).getCoordinate(),crane.getxSpeed(),crane.getySpeed());
        tijd += van.getTravelTime();
        crane.setPositie(slots.get(c.getSlotId()).getCoordinate());
        Movement naar = new Movement(crane.getCraneId(),c.getId(),c.getSlotId(),sId,tijd,crane.getPositie(),slots.get(sId).getCoordinate(),crane.getxSpeed(),crane.getySpeed());
        tijd += naar.getTravelTime();

        crane.setPositie(slots.get(sId).getCoordinate());
        for(int i=0;i<c.getLc();i++){
            assignments.put(sId+i +","+getSlotHeight(sId+i),c.getId());
            assignments.remove(c.getSlotId()+ "," + c.getSlotH());
            c.setId(sId);
            c.setSlotH(getSlotHeight(sId));
        }
        return naar;
    }

    public Movement move(Container c){
        int free = getFreeSpot(c,hTarget);
        Cranes crane = cranes.get(0);

        Movement van = new Movement(crane.getCraneId(),-1,-1,-1,tijd,crane.getPositie(),slots.get(c.getSlotId()).getCoordinate(),crane.getxSpeed(),crane.getySpeed());
        tijd += van.getTravelTime();

        crane.setPositie(slots.get(c.getSlotId()).getCoordinate());
        Movement naar = new Movement(crane.getCraneId(),c.getId(),c.getSlotId(),free,tijd,crane.getPositie(),slots.get(free).getCoordinate(),crane.getxSpeed(),crane.getySpeed());
        tijd += naar.getTravelTime();

        crane.setPositie(slots.get(free).getCoordinate());
        for(int i=0;i<c.getLc();i++){
            assignments.put(free+i +","+getSlotHeight(free+i),c.getId());
            assignments.remove(c.getSlotId()+i+ "," + c.getSlotH());

        }
        c.setSlotId(free);
        c.setSlotH(getSlotHeight(free));
        return naar;
    }
    public void startOrdening(Boolean changeHeigt){
        movements = new ArrayList<>();
        if(!changeHeigt){
            for (int[] scheduleItem: schedule){
                movements.add(move(containers.get(scheduleItem[0]),scheduleItem[2]));
            }
        }
        else{
            for (int[] scheduleItem: schedule){
                movements.add(move(containers.get(scheduleItem[0])));
            }
        }
    }
    //returns id, from, to
    public ArrayList <int[]> GenerateSchedule(){
        schedule =new ArrayList<>();
        for(int i=0; i<containers.size();i++){
            int currentPos = containers.get(i).getSlotId();
            int containerId = containers.get(i).getId();
            for(int j =0;j<EndPosition.size();j++){
                int endPos = EndPosition.get(j)[0];
                int container = EndPosition.get(j)[1];
                if(containerId == container && currentPos != endPos){
                    schedule.add(new int[]{containerId,currentPos,endPos});
                }
            }
        }
        return schedule;
    }
    public ArrayList <int[]> GenerateScheduleHeight(){
        schedule =new ArrayList<>();
        for(HashMap.Entry<String, Integer> entry: assignments.entrySet()){
            if(containers.get(entry.getValue()).getSlotH()>hTarget){
               schedule.add(new int[]{entry.getValue(),Integer.parseInt(entry.getKey().split(",")[0])});
            }
        }
        Collections.reverse(schedule);
        return schedule;
    }
    private int getFreeSpot(Container c, int hMax)
    {
        Coordinate co = new Coordinate(slots.get(c.getSlotId()).getCoordinate().getX(),slots.get(c.getSlotId()).getCoordinate().getY());
        while(true)
        {

            if(co.getY()==widthRaster-1 && co.getX()==lenghtRaster-c.getLc()){
                co.setY(0);
                co.setX(0);
            }
            else if(co.getX()==lenghtRaster-c.getLc()){
                co.setX(0);
                co.setY(co.getY()+1);
            }
            else{co.setX(co.getX()+1);}
            for (int h=1 ;h <= hMax ; h++ )
            {
                int ok = 0;
                for(int l = 0 ; l< c.getLc(); l++)
                {
                    Coordinate cc = new Coordinate(co.getX()+l,co.getY());
                    if(assignments.containsKey(coordinateToSlotID(cc)+","+h)) continue;
                    ok++;
                }
                if(ok == c.getLc() && h==1) return  coordinateToSlotID(co);
                else if(ok == c.getLc())
                {
                    if(checkstackingConstrains(co,c)) return coordinateToSlotID(co);
                }
            }
        }
    }

    private boolean checkstackingConstrains(Coordinate co, Container c)
    {
        return getSlotHeight(coordinateToSlotID(co))<hTarget;
    }

    private int coordinateToSlotID(Coordinate co)
    {
        if(slots.indexOf(new Slot(-1,co))==-1){
            System.out.println("int 123412");
    }
        return slots.get(slots.indexOf(new Slot(-1,co))).getId();
    }
    private int getSlotHeight(int slotId){
        int i = 1;
        while(assignments.containsKey(slotId+ "," +i)) {
            i++;
        }
        return i;
    }
}
