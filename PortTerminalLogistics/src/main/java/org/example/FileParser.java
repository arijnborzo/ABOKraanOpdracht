package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileParser {

    ArrayList<Slot> slots = new ArrayList<>();
    ArrayList<Cranes> cranes = new ArrayList<>();

    ArrayList <Container> containers = new ArrayList<>();
    HashMap<String,Integer> assignments = new HashMap<>();
    int hMax;
    int targetHeight;

    int length;
    int width;
    public void parseFile(String filename) throws IOException {
        String resourceName = filename;
        String is = new String(Files.readAllBytes(Paths.get(resourceName)));


        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray jsonslots = object.getJSONArray("slots");
        JSONArray jsoncontainers = object.getJSONArray("containers");
        JSONArray jsonassignments= object.getJSONArray("assignments");
        JSONArray jsoncranes= object.getJSONArray("cranes");
        hMax = object.getInt("maxheight");
        length = object.getInt("length");
        width = object.getInt("width");


        if(!object.isNull("targetheight")){
            targetHeight = object.getInt("targetheight");
        }
        else {
            targetHeight = hMax;
        }
        for (int i=0; i<jsonslots.length();i++){
            JSONObject id = jsonslots.getJSONObject(i);

            slots.add(new Slot((int) id.get("id"),new Coordinate((int) id.get("x"),(int) id.get("y"))));
        }
        for (int i=0; i<jsoncontainers.length();i++){
            JSONObject id = jsoncontainers.getJSONObject(i);
            containers.add(new Container((int) id.get("id"),(int) id.get("length"),-1, -1));
        }
        for (int i=0; i<jsoncranes.length();i++){
            JSONObject id = jsoncranes.getJSONObject(i);
            cranes.add(new Cranes((int) id.get("id"),new Coordinate((int) id.get("x"), id.getDouble("y")), (int) id.get("ymin"),
                    (int) id.get("ymax"), (int) id.get("xmin"),(int)id.get("xmax"),
                    (int) id.get("xspeed"), (int)id.get("yspeed")));
        }
        for (int i=0; i<jsonassignments.length();i++){
            JSONObject data = jsonassignments.getJSONObject(i);
            int slot_id = data.getInt("slot_id");
            String position;
            int cId = (int)data.get("container_id");
            int h=1;

            position = slot_id + "," + h;
            while(assignments.containsKey(position)){
                h++;
                position = slot_id + "," + h;
            }
            assignments.put(position,(int)data.get("container_id"));
            if(containers.get(cId).getSlotId() == -1) containers.get(cId).setSlotId(slot_id);
            containers.get(cId).setSlotH(h);

            for (int j = 0; j<containers.get(cId).getLc()-1; j++){
                containers.get(cId).getLc();
                position = slot_id+1 + "," + h;
                assignments.put(position,(int)data.get("container_id"));
            }

        }

    }
}
