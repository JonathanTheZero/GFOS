package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class RightHandler {

	private static final String[] allRightclasses = { "root", "admin", "personnelDepartment", "headOfDepartment", "user" };
	private static final String[] allActions = { "DeleteRoot", "test", "getAllMitarbeiter", "addMitarbeiter", "addAdmin"};

	public static String getRightclassFromSessionID(String si) {
		String sqlStmt = "SELECT gfos.mitarbeiter.Rechteklasse FROM gfos.mitarbeiter INNER JOIN gfos.active_sessions ON gfos.mitarbeiter.Personalnummer = gfos.active_sessions.Mitarbeiter WHERE gfos.active_sessions.SessionID = \""
				+ si + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return "";
			return rs.getString("Rechteklasse");
		} catch (SQLException e) {
			return "";
		}
	}

	public static boolean checkPermission(String si, String action) {
		String rightclass = getRightclassFromSessionID(si);
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

	private static boolean checkActionRoot(String action) {
		switch (action) {
		case "DeleteRoot":
			return false;
		case "addMitarbeiter":
			return false;
		default:
			return true;
		}
	}

	private static boolean checkActionAdmin(String action) {
		switch (action) {
		case "DeleteRoot":
			return false;
		default:
			return true;
		}
	}

	private static boolean checkActionPersonnelDepartment(String action) {
		switch (action) {
		case "DeleteRoot":
			return false;
		case "addAdmin":
			return false;
		default:
			return true;
		}
	}

	private static boolean checkActionHeadOfDepartment(String action) {
		switch (action) {
		case "DeleteRoot":
			return false;
		case "getAllMitarbeiter":
			return false;
		case "addAdmin":
			return false;
		case "addMitarbeiter":
			return false;
		default:
			return true;
		}
		
	}

	private static boolean checkActionUser(String action) {
		switch (action) {
		case "DeleteRoot":
			return false;
		case "getAllMitarbeiter":
			return false;
		case "addAdmin":
			return false;
		case "addMitarbeiter":
			return false;
		default:
			return true;
		}
	}
}
