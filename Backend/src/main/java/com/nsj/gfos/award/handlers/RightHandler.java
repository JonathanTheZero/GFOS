package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Verwaltung der Rechteklassen der Mitarbeiter. Gibt Bestätigung, wenn eine
 * Aktion ausgeführt werden darf.
 * 
 * @author sophi
 *
 */
public class RightHandler {
	/**
	 * String Arrays, in denen alle möglichen Rechteklassen und auszuführende
	 * Aktionen abgespeichert sind.
	 */
	private static final String[] allRightclasses = { "root", "admin", "personnelDepartment", "headOfDepartment",
			"user" };
	private static final String[] allActions = { "getAllMitarbeiter", "addMitarbeiter", "addAdmin", "removeAdmin",
			"removeMitarbeiter", "test", "selfGet", "unrestrictedGet", "restrictedGet", "userGet", "selfAlter",
			"unrestrictedAlter", "restrictedAlter", "getArbeitsgruppe", "alterLeiter", "addArbeitsgruppe",
			"removeArbeitsgruppe", "removeMitarbeiterFromArbeitsgruppe", "addMitarbeiterToArbeitsgruppe", "becomeLeiter"};

	/**
	 * Die Methode <i>getRightclassFromSessionID</i> liefert die Rechteklasse zum
	 * Mitarbeiter.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @return String - Rechteklasse des Mitarbeiters
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
	 * Die Methode <i>getRightclassFromSessionID</i> liefert die Rechteklasse zum
	 * Mitarbeiter.
	 * 
	 * @param pn - Personalnummer des Mitarbeiters
	 * @return String - Rechteklasse des Mitarbeiters
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
	 * TODO
	 * 
	 * @param action
	 * @param attribute
	 * @return
	 */
	public static boolean permittedAttribute(String action, String attribute) {
		if (!action.equals("selfAlter") && attribute.equals("pw"))
			return false;
		return true;
	}

	/**
	 * Die Methode <i>checkPermission</i> prüft anhand der Rechteklasse und der
	 * auszuführenden Aktion des Mitarbeiters, ob dies für seine Rechteklasse
	 * zulässig ist.
	 * 
	 * @param auth   - SessionID des ausführenden Mitarbeiters
	 * @param action - Aktion, die ausgeführt werden soll
	 * @return boolean - true, wenn der Mitarbeiter die Aktion ausführen darf,
	 *         false, wenn nicht
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
	 * Die Methode <i>checkPermission</i> prüft anhand der Rechteklasse und der
	 * auszuführenden Aktion des Mitarbeiters, ob dies für seine Rechteklasse
	 * zulässig ist.
	 * 
	 * @param auth   - SessionID des ausführenden Mitarbeiters
	 * @param action - Aktion, die ausgeführt werden soll
	 * @return boolean - true, wenn der Mitarbeiter die Aktion ausführen darf,
	 *         false, wenn nicht
	 */
	public static boolean checkPermissionFromPn(String pn, String action) {
		String rightclass = getRightClassFromPersonalnummer(pn);
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
	 * Diese Methoden <i>checkAction + Rechteklasse</i> legen fest, welche Aktionen
	 * mit welcher Rechteklasse ausgeführt werden dürfen.
	 * 
	 * @param action - Aktion, die ausgeführt werden soll
	 * @return boolean - true, wenn die Rechteklasse die Aktion ausführen darf,
	 *         false, wenn nicht
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
		case "unrestrictedGet":
			return false;
		case "restrictedAlter":
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
		case "userGet":
			return false;
		case "unrestrictedGet":
			return false;
		case "restrictedAlter":
			return false;
		case "getArbeitsgruppe":
			return false;
		case "alterLeiter":
			return false;
		case "addArbeitsgruppe":
			return false;
		case "removeArbeitsgruppe":
			return false;
		case "removeMitarbeiterFromArbeitsgruppe":
			return false;
		case "addMitarbeiterToArbeitsgruppe":
			return false;
		case "becomeLeiter":
			return false;
		default:
			return true;
		}
	}
}
