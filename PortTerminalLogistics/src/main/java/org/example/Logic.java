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
        fileParser.parseFile("src/main/TerminalA_20_10_3_2_160.json");
        if(!changeHeight){
            FileparserEnd fileparser2 = new FileparserEnd();
            fileparser2.parseFile("src/main/targetTerminalA_20_10_3_2_160.json");
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

    public ArrayList<Movement> move(Container c, int sId, int nextCraneId)
    {
        Cranes[] cranesToUse = getPossibleCrane(slots.get(c.getSlotId()).getCoordinate(), slots.get(sId).getCoordinate());
        ArrayList<Movement> movements1= new ArrayList<>();

        if(cranesToUse[0] == cranesToUse[1]){
            Slot initSlotC = slots.get(c.getSlotId());
            if(assignments.containsKey(c.getSlotId()+","+c.getSlotH()+1))
            {
                move(containers.get(assignments.get(c.getSlotId()+","+c.getSlotH()+1)),getFreeSpot(c,hMax,cranesToUse[0].getxMin(),cranesToUse[0].getxMax()-c.getLc()),-1);
            }
            if(getSlotHeight(sId)>=hMax)
            {
                move(containers.get(assignments.get(sId+","+getSlotHeight(sId))),getFreeSpot(c,hMax,cranesToUse[0].getxMin(),cranesToUse[0].getxMax()-c.getLc()),-1);
            }
            movements1.add(moveContainerWithOneCrane(cranesToUse[0],c,sId));
            if(cranesToUse[0].getCraneId() != nextCraneId){
                Movement backToStartPosition = new Movement(cranesToUse[0].getCraneId(),-1,-1,-1,tijd,cranesToUse[0].getPositie(),slots.get(coordinateToSlotID(cranesToUse[0].getStartPositie())).getCoordinate(),cranesToUse[0].getxSpeed(),cranesToUse[0].getySpeed());
                tijd+=backToStartPosition.getTravelTime();
            }
            return movements1;
        }
        else{
            Slot initSlotC = slots.get(c.getSlotId());
            if(assignments.containsKey(c.getSlotId()+","+c.getSlotH()+1))
            {
                move(containers.get(assignments.get(c.getSlotId()+","+c.getSlotH()+1)),getFreeSpot(c,hMax,cranesToUse[1].getxMin(),cranesToUse[1].getxMax()-c.getLc()),-1);
            }
            if(getSlotHeight(sId)>=hMax)
            {
                move(containers.get(assignments.get(sId+","+getSlotHeight(sId))),getFreeSpot(c,hMax,cranesToUse[1].getxMin(),cranesToUse[1].getxMax()-c.getLc()),-1);
            }
            int[]overlappingArea = cranesToUse[0].getOverlapArea(cranesToUse[1]);
            int freeSpot = getFreeSpot(c,hMax,overlappingArea[0],overlappingArea[1]);

            movements1.add(moveContainerWithOneCrane(cranesToUse[0],c,freeSpot));
            Movement backToStartPosition = new Movement(cranesToUse[0].getCraneId(),-1,-1,-1,tijd,cranesToUse[0].getPositie(),slots.get(coordinateToSlotID(cranesToUse[0].getStartPositie())).getCoordinate(),cranesToUse[0].getxSpeed(),cranesToUse[0].getySpeed());
            tijd+=backToStartPosition.getTravelTime();

            movements1.add(moveContainerWithOneCrane(cranesToUse[1],c,sId));
            backToStartPosition = new Movement(cranesToUse[1].getCraneId(),-1,-1,-1,tijd,cranesToUse[1].getPositie(),slots.get(coordinateToSlotID(cranesToUse[1].getStartPositie())).getCoordinate(),cranesToUse[1].getxSpeed(),cranesToUse[1].getySpeed());
            tijd+=backToStartPosition.getTravelTime();

            return movements1;
        }

    }
    public Movement moveContainerWithOneCrane(Cranes crane,Container c, int sId ){
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
    public Cranes[] getPossibleCrane(Coordinate start, Coordinate end){
        Cranes [] usableCranes = new Cranes[2];
        for(Cranes crane : cranes){
            if(crane.canReachCoordinate(start) && usableCranes[0]==null){
                usableCranes[0] = crane;
            }
            if(crane.canReachCoordinate(end) && usableCranes[1]==null){
                usableCranes[1] = crane;
            }
            if( (usableCranes[0] != null) && (usableCranes[1] != null)){
                continue;
            }
        }
        return usableCranes;
    }
    public ArrayList<Movement> move(Container c){
        int finalFreeSpot = getFreeSpot(c,hTarget,0,lenghtRaster-c.getLc());

        Cranes[] cranesToUse = getPossibleCrane(slots.get(c.getSlotId()).getCoordinate(), slots.get(finalFreeSpot).getCoordinate());
        ArrayList<Movement> movements1= new ArrayList<>();

        if(cranesToUse[0] == cranesToUse[1]) {
            Slot initSlotC = slots.get(c.getSlotId());
            movements1.add(moveContainerWithOneCrane(cranesToUse[0],c,finalFreeSpot));
            return movements1;
        }
        else{
            Slot initSlotC = slots.get(c.getSlotId());

            int[]overlappingArea = cranesToUse[0].getOverlapArea(cranesToUse[1]);
            int intermediateFreeSpot = getFreeSpot(c,hMax,overlappingArea[0],overlappingArea[1]);
            movements1.add(moveContainerWithOneCrane(cranesToUse[0],c,intermediateFreeSpot));
            movements1.add(moveContainerWithOneCrane(cranesToUse[1],c,finalFreeSpot));

            return movements1;
        }


    }
    public void startOrdening(Boolean changeHeigt){
        movements = new ArrayList<>();
        if(!changeHeigt){
            for(int i = 0; i<schedule.size();i++){
                if(i == schedule.size()-1){
                    movements.addAll(move(containers.get(schedule.get(i)[0]),schedule.get(i)[2],-1));
                }
                else{
                    Cranes [] possibleCranes = getPossibleCrane(slots.get(schedule.get(i+1)[1]).getCoordinate(), slots.get(schedule.get(i+1)[2]).getCoordinate());
                    movements.addAll(move(containers.get(schedule.get(i)[0]),schedule.get(i)[2],possibleCranes[0].getCraneId()));
                }
            }
        }
        else{
            for (int[] scheduleItem: schedule){
                movements.addAll(move(containers.get(scheduleItem[0])));
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
    private int getFreeSpot(Container c, int hMax, int xMin, int xMax)
    {
        Coordinate co = new Coordinate(slots.get(c.getSlotId()).getCoordinate().getX(),slots.get(c.getSlotId()).getCoordinate().getY());
        while(true)
        {
            if(co.getY()==widthRaster-1 && co.getX()==xMax){
                co.setY(0);
                co.setX(xMin);
            }
            else if(co.getX()==xMax){
                co.setY(co.getY()+1);
                co.setX(xMin);
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
        int slotID = coordinateToSlotID(co);
        int heightSlot = getSlotHeight(slotID);
        int totalLength = 0;
        int previousContainer = -1;

        for(int i = 0; i<c.getLc(); i++){

            if(assignments.get((slotID+i) + "," + (heightSlot-1)) == null){
                return false;
            }
            int containerID = assignments.get((slotID+i) + "," + (heightSlot-1));

            if(containerID != previousContainer){
                totalLength = containers.get(containerID).getLc();
                previousContainer = containerID;
            }
        }
        return (getSlotHeight(coordinateToSlotID(co))<hTarget) && (c.getLc() == totalLength);
    }

    private int coordinateToSlotID(Coordinate co)
    {
        if(slots.indexOf(new Slot(-1,co))==-1){
            System.out.println("int 123412");
    }
        return slots.get(slots.indexOf(new Slot(-1,co))).getId();
    }
    private int getSlotHeight(int slotId){
        int i = 0;
        while(assignments.containsKey(slotId+ "," +(i+1))) {
            i++;
        }
        return i;
    }
    public void PrintResult(){
        System.out.println("%CraneId;ContainerId;PickupTime;EndTime;PickupPosX;PickupPosY;EndPosX;EndPosY;");
        for(Movement move : movements){
            System.out.println(move.getId() + ";" + move.getContainerId()+ ';'+ move.getStartTime()+ ";" + move.getEndTime() + ";" + move.getStartLocation().getX() + ";" + move.getStartLocation().getY()+ ";" + move.getEndLocation().getX() + ";" + move.getEndLocation().getY());
        }
    }
}
