package com.nsj.gfos.award.handlers;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigHandler {

    private static final JSONObject config = getConfig();

    private static JSONObject getConfig() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try(FileReader reader = new FileReader("C:/Users/LoL/Documents/GFOS/Backend/config/database.config.json")) {
            Object obj = parser.parse(reader);
            jsonObject = (JSONObject) obj;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getHost() {
        return (String) config.get("host");
    }

    public static String getUser() {
        return (String) config.get("user");
    }

    public static String getPassword() {
        return (String) config.get("password");
    }

}