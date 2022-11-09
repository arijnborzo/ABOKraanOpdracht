package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileParser {
    public void parseFile(String filename) throws IOException {
        ArrayList<Slot> slots = new ArrayList<>();
        ArrayList <Container> containers = new ArrayList<>();
        HashMap<String,Integer> assignments = new HashMap<>();
        String resourceName = filename;
        String is = new String(Files.readAllBytes(Paths.get(resourceName)));


        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray jsonslots = object.getJSONArray("slots");
        JSONArray jsoncontainers = object.getJSONArray("containers");
        JSONArray jsonassignments= object.getJSONArray("assignments");

        for (int i=0; i<jsonslots.length();i++){
            JSONObject id = jsonslots.getJSONObject(i);
            slots.add(new Slot((int) id.get("id"),new Coordinate((int) id.get("x"),(int) id.get("y"))));
        }
        for (int i=0; i<jsoncontainers.length();i++){
            JSONObject id = jsoncontainers.getJSONObject(i);
            containers.add(new Container((int) id.get("id"),(int) id.get("length")));
        }
        for (int i=0; i<jsonassignments.length();i++){
            JSONObject id = jsonassignments.getJSONObject(i);
            JSONArray slot_ids = id.getJSONArray("slot_id");
            String position;
            int h=0;
            if(slot_ids.length()==1){
                position = slot_ids.getInt(0) + "," + h;
                while(assignments.containsKey(position)){
                    h++;
                    position = slot_ids.getInt(0) + "," + h;
                }
                assignments.put(position,(int)id.get("container_id"));
            }
            else{
                //positie 1
                position = slot_ids.getInt(0) + "," + h;
                while(assignments.containsKey(position)){
                    h++;
                    position = slot_ids.getInt(0) + "," + h;
                }
                assignments.put(position,(int)id.get("container_id"));

                //positie 2
                position = slot_ids.getInt(1) + "," + h;
                while(assignments.containsKey(position)){
                    h++;
                    position = slot_ids.getInt(1) + "," + h;
                }
                assignments.put(position,(int)id.get("container_id"));
            }
        }

    }
}
