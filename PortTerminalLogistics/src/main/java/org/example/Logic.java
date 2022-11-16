package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Logic
{
    ArrayList<Slot> slots ;
    ArrayList <Container> containers ;
    HashMap<String,Integer> assignments ;
    int hMax = 2;


    public void readFile() throws IOException {
        FileParser test = new FileParser();
        test.parseFile("src/main/input.json");

        slots = test.slots;
        containers = test.containers;
        assignments = test.assignments;
    }

    public Movement move(Container c, int sId)
    {
        // kijken als a kan opgepakt worden
        Slot initSlotC = slots.get(c.getSlotId());
        if(assignments.containsKey(c.getSlotId()+","+c.getSlotH()+1))
        {
            move(containers.get(assignments.get(c.getSlotId()+","+c.getSlotH()+1)),getFreeSpot(c));
        }
        // verplaats container

    }

    private int getFreeSpot(Container c)
    {
        Coordinate co = slots.get(c.getSlotId()).getCoordinate();
        while(true)
        {
            co.setX(co.getX()+1);
            for (int h=0 ;h < hMax ; h++ )
            {
                int ok = 0;
                for(int l = 0 ; l< c.getLc(); l++)
                {
                    Coordinate cc = new Coordinate(co.getX()+l,co.getY());
                    if(assignments.containsKey(coordinateToSlotID(co)+","+h)) continue;
                    ok++;
                }
                if(ok == c.getLc() && h==0) return  coordinateToSlotID(co);
                else if(ok == c.getLc())
                {
                    if(checkstackingConstrains(co,c)) return coordinateToSlotID(co);
                }


            }

        }

    }

    private boolean checkstackingConstrains(Coordinate co, Container c)
    {

    }

    private int coordinateToSlotID(Coordinate co)
    {
        return slots.get(slots.indexOf(new Slot(-1,co))).getId();
    }
}
