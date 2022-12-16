package org.example;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        boolean changeHeight = true;

        Logic logic = new Logic();
        logic.readFile(changeHeight);
        //ArrayList <int[]> schedule = logic.GenerateSchedule();

        if(changeHeight){
            ArrayList <int[]> schedule = logic.GenerateScheduleHeight();
        }
        else{
            ArrayList <int[]> schedule = logic.GenerateSchedule();
        }

        logic.startOrdening(changeHeight);
    }
}
