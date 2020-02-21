package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class RightHandler {
	/**
	*String Arrays, in denen alle möglichen Rechteklassen und auszuführende Aktionen abgespeichert sind.
	*/
	private static final String[] allRightclasses = { "root", "admin", "personnelDepartment", "headOfDepartment", "user" };
	private static final String[] allActions = {"getAllMitarbeiter", "addMitarbeiter", "addAdmin", "removeAdmin", "removeMitarbeiter"};

	
	/**
	 * Die Methode gibt die Rechteklasse eines Mitarbeiters zurück, der durch seine Session ID in der Datenbank identifiziert wird.
	 * In der Datenbank werden dazu die Tabellen "active_sessions" und "mitarbeiter" mit Hilfe der Personalnummer zusammengefügt.
	 */
	public static String getRightclassFromSessionID(String auth) {
		String sqlStmt = "SELECT gfos.mitarbeiter.Rechteklasse FROM gfos.mitarbeiter INNER JOIN gfos.active_sessions ON gfos.mitarbeiter.Personalnummer = gfos.active_sessions.Mitarbeiter WHERE gfos.active_sessions.SessionID = \""
				+ auth + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return "";
			return rs.getString("Rechteklasse");
		} catch (SQLException e) {
			return "";
		}
	}
	
	
	/**
	 *Die Methode gibt die Rechteklasse eines Mitarbeiters zurück, der durch seine Personalnummer in der Datenbank identifiziert wird.
	 * 
	 */
	public static String getRightClassFromPersonalnummer(String pn) {
		String sqlStmt = "SELECT gfos.mitarbeiter.Rechteklasse FROM gfos.mitarbeiter WHERE gfos.mitarbeiter.Personalnummer = \""
				+ pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return "";
			return rs.getString("Rechteklasse");
		} catch (SQLException e) {
			return "";
		}
	}

	
	/**
	 * Die Methode prüft anhand der Rechteklasse und der auszuführenden Aktion des Mitarbeiters, ob dies für seine Rechteklasse zulässig ist.
	 */
	public static boolean checkPermission(String auth, String action) {
		String rightclass = getRightclassFromSessionID(auth);
		if (!Arrays.asList(allRightclasses).contains(rightclass))
			return false;
		if (!Arrays.asList(allActions).contains(action))
			return false;

		switch (rightclass) {
		case "root":
			return checkActionRoot(action);
		case "admin":
			return checkActionAdmin(action);
		case "personnelDepartment":
			return checkActionPersonnelDepartment(action);
		case "headOfDepartment":
			return checkActionHeadOfDepartment(action);
		case "user":			
			return checkActionUser(action);
		}
		return false;
	}
	/**
	 * Diese Methoden legen fest, welche Aktionen mit welcher Rechteklasse ausgeführt werden dürfen.
	 */
	private static boolean checkActionRoot(String action) {
		switch (action) {		
		default:
			return true;
		}
	}

	private static boolean checkActionAdmin(String action) {
		switch (action) {		
		default:
			return true;
		}
	}

	private static boolean checkActionPersonnelDepartment(String action) {
		switch (action) {		
		case "addAdmin":
			return false;
		case "removeAdmin":
			return false;
		default:
			return true;
		}
	}

	private static boolean checkActionHeadOfDepartment(String action) {
		switch (action) {		
		case "getAllMitarbeiter":
			return false;
		case "addAdmin":
			return false;
		case "addMitarbeiter":
			return false;
		case "removeAdmin":
			return false;
		case "removeMitarbeiter":
			return false;
		default:
			return true;
		}
		
	}

	private static boolean checkActionUser(String action) {
		switch (action) {		
		case "getAllMitarbeiter":
			return false;
		case "addAdmin":
			return false;
		case "addMitarbeiter":
			return false;
		case "removeAdmin":
			return false;
		case "removeMitarbeiter":
			return false;
		default:
			return true;
		}
	}
}
