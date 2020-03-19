package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import com.nsj.gfos.award.gUtils.Utils;

/**
 *
 * @author Schnuels
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
			sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Personalnummer = \"" + personalnummer + "\";";
			try {
				ResultSet rSet = QueryHandler.query(sqlStmt);
				if(rSet == null)
					return JsonHandler.fehler("Fehler bei Zugriff auf die Datenbank");
				if(!rSet.next())
					return JsonHandler.fehler("Fehler bei Zugriff auf die Datenbank");
				Mitarbeiter obj = Utils.createMitarbeiterFromQuery(rSet, new String[] {"Personalnummer", "Name", "Vorname", "erreichbar", "Arbeitskonto", "EMail", "Passwort", "Status", "Rechteklasse", "Abteilung", "Vertreter", "gda"});
				return JsonHandler.embedMitarbeiterInErfolg(obj, "Session wurde erfolgreich erstellt.");
				} catch (Exception e) {
				return JsonHandler.fehler(e.toString());
			}
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}				
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
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	public static boolean checkSessionID(String sessionID) {
		String sqlStmt = "SELECT SessionID FROM gfos.active_sessions WHERE SessionID = \"" + sessionID + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			return false;
		}
		return false;
	}
	
}
