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

        Movement move = new Movement(1,1,new Coordinate(1,1), new Coordinate(5,5),10.0,4.0);
        System.out.println(move.getTravelTime());
        Movement move2 = new Movement(2,1,new Coordinate(6,6), new Coordinate(3,4),10.0,3.0);


        System.out.println(move.collision(move,move2));
        for(int i = 0; i < move.getOverlapArea(move,move2).length; i++) {
            System.out.print(move.getOverlapArea(move,move2)[i]);
        }

        FileParser test = new FileParser();
        test.parseFile("/Users/arijnborzo/ABOKraanOpdracht/PortTerminalLogistics/src/main/input.json");
    }
}
