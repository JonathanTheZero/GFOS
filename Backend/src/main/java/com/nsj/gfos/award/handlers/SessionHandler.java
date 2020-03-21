package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import com.nsj.gfos.award.gUtils.Utils;

/**
 * Die Klasse <i>SessionHandler</i> stellt Hilfsmethoden bereit, welche sich um das Management
 * der Sessions und anderer Funktionen bei An- und Abmeldung kümmern.
 * @author Schnuels
 */
public class SessionHandler {

	/**
	 * Die Methode <i>createSession</i> erstellt mit Hilfe einer SessionID und einer Personalnummer eine neue Session
	 * auf der Datenbank und gibt nach erfogreichem Prozess eine Erfolgsmeldung zurück, in welcher das Objekt des 
	 * angemeldeten Mitarbeiters enthalten ist.
	 * @param args - Ein Array mit der SessionID und der Personalnummer als Inhalt
	 * @return String - Eine Fehlermeldung bei einem Fehler und eine Erfolgsmeldung mit Mitarbeiter Objekt bei einem Erfolg
	 */
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
			
	/**
	 * Die Methode <i>closeSession</i> schließt eine vorhandene Session mit Hilfe der SessionID.
	 * @param sessionID - ID der zu schließenden Session
	 * @return String - Je nach Ausgang des Vorgangs eine Fehler- oder Erfolgsmeldung
	 */
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
	
	/**
	 * DIe Methode <i>checkSessionID</i> prüft, ob zu einer gegebenen SessionID eine aktive Session
	 * auf der Datenbank hinterlegt ist.
	 * @param sessionID - zu prüfende SessionID
	 * @return boolean - true, wenn eine Session für die ID existiert, false, wenn nicht
	 */
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

	/**
	 * Die Methode <i>changeStatus</i> ändert den Status eines Mitarbeiters auf der Datenbank.
	 * @param pn - Personalnummer des Mitarbeiters
	 * @param status - neu einzutragender Status
	 * @return boolean - true, wenn der Status geändert werden konnte, false, wenn nicht
	 */
	public static boolean changeStatus(String pn, String status) {
		String sqlStmt = "UPDATE gfos.Mitarbeiter SET Status = \"" + status + "\" WHERE Personalnummer = \"" + pn + "\"";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Die Methode <i>changeErreichbar</i> ändert den Wert für 'erreichbar' eines Mitarbeiters auf der Datenbank.
	 * @param pn - Personalnummer des Mitarbeiters
	 * @param status - neu einzutragender Wert für 'erreichbar'
	 * @return boolean - true, wenn 'erreichbar' geändert werden konnte, false, wenn nicht
	 */
	public static boolean changeErreichbar(String pn, int erreichbar) {
		String sqlStmt = "UPDATE gfos.Mitarbeiter SET erreichbar = " + erreichbar + " WHERE Personalnummer = \"" + pn + "\"";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
}
