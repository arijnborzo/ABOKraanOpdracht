package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileparserEnd {
    ArrayList<int[]> assignments = new ArrayList<>();
    public void parseFile(String filename) throws IOException {
        String resourceName = filename;
        String is = new String(Files.readAllBytes(Paths.get(resourceName)));

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray jsonassignments = object.getJSONArray("assignments");

        for (int i=0; i<jsonassignments.length();i++){
            JSONObject id = jsonassignments.getJSONObject(i);
            assignments.add(new int[] {id.getInt("slot_id"),id.getInt("container_id")});
        }
    }

}
