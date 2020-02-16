/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LoL
 */
public class SessionHandler {

	public static String createSession(String[] args) {		
		String sessionID = args[0];
		String personalnummer = args[1];
		String sqlStmt = "INSERT INTO gfos.active_sessions VALUES (\"" + sessionID + "\",\"" + personalnummer + "\");";
		if(checkSessionID(sessionID))
			return JsonHandler.fehler("ID existiert bereits.");
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0) 
				return JsonHandler.fehler("Fehler bei der Erstellung der Session!");
			return JsonHandler.erfolg("Session wurde erfolgreich erstellt.");
		} catch (SQLException e) {return e.toString();}				
	}
	
	public static String closeSession(String sessionID) {
		String sqlStmt = "DELETE FROM gfos.active_sessions WHERE SessionID = \"" + sessionID + "\";";
		if(!checkSessionID(sessionID))
			return JsonHandler.fehler("ID existiert nicht.");
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0) 
				return JsonHandler.fehler("Fehler beim Löschen der Session!");
			return JsonHandler.erfolg("Session wurde erfolgreich geschlossen.");
		} catch (SQLException e) {return e.toString();}
	}
	
	public static boolean checkSessionID(String sessionID) {
		String sqlStmt = "SELECT SessionID FROM gfos.active_sessions WHERE SessionID = \"" + sessionID + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs.next())
				return true;
		} catch (SQLException e) {return false;}
		return false;
	}
	
}
