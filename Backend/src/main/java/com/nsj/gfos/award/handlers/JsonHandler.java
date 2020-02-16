package com.nsj.gfos.award.handlers;

import org.codehaus.jackson.map.ObjectMapper;

import com.nsj.gfos.award.dataWrappers.Mitarbeiter;

public class JsonHandler {

	public static String fehler(String msg) {
		return "{\"fehler\": \"" + msg + "\"}";
	}
	
	public static String erfolg(String msg) {
		return "{\"erfolg\": \"" + msg + "\"}";
	}
	
	 public static String createJsonFromMitarbeiter(Mitarbeiter m) {
	        ObjectMapper om = new ObjectMapper();
	        String s;
	        try {
	            s = om.writeValueAsString(m);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	        return s;
	 }
	
}
