package com.nsj.gfos.award.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.nsj.gfos.award.gUtils.Utils;

/**
 * Verwaltung der Rechteklassen der Mitarbeiter. Gibt Bestätigung, wenn eine
 * Aktion ausgeführt werden darf.
 * 
 * @author Sophief
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
			"removeMitarbeiter", "selfGet", "unrestrictedGet", "restrictedGet", "userGet", "selfAlter",
			"unrestrictedAlter", "restrictedAlter", "getArbeitsgruppe", "alterLeiter", "addArbeitsgruppe",
			"removeArbeitsgruppe", "removeMitarbeiterFromArbeitsgruppe", "addMitarbeiterToArbeitsgruppe",
			"becomeLeiter", "getArbeitsgruppenFromMitarbeiter", "getAllArbeitsgruppen", "getAddedToArbeitsgruppe" };

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
			case "getAddedToArbeitsgruppe":
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
			case "removeArbeitsgruppe":
				return false;
			case "removeMitarbeiterFromArbeitsgruppe":
				return false;
			case "addMitarbeiterToArbeitsgruppe":
				return false;
			case "getArbeitsgruppe":
				return false;
			case "getArbeitsgruppenFromMitarbeiter":
				return false;
			case "getAllArbeitsgruppen":
				return false;
			case "alterLeiter":
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
			case "getAllArbeitsgruppen":
				return false;
			case "getArbeitsgruppenFromMitarbeiter":
				return false;
			default:
				return true;
		}
	}

	/**
	 * Die Methode <i>getGetAction</i> wird aufgerufen, wenn der Client eine Anfrage
	 * an die /mitarbeiter/get Resource stellt und bestimmt abhängig von den
	 * Rechteklassen des Clients und des Mitarbeiters, welcher zurückgegeben werden
	 * soll, eine im RightHandler registrierte Aktion, welche als String
	 * zurückgegeben wird.
	 * 
	 * @param auth - SessionID der Anfrage
	 * @param pn   - Personalnummer des angefragten Mitarbeiters
	 * @return String - Eine Aktion aus: selfGet, unrestrictedGet, restrictedGet,
	 *         userGet.
	 */
	public static String getGetAction(String auth, String pn) {
		if (Utils.checkSelfOperation(auth, pn))
			return "selfGet";
		switch (RightHandler.getRightclassFromSessionID(auth)) {
			case "root":
				return "unrestrictedGet";
			case "admin":
				return "unrestrictedGet";
			case "personnelDepartment":
				return "unrestrictedGet";
			case "headOfDepartment":
				return "restrictedGet";
			case "user":
				if (Utils.checkAbteilung(auth, pn) || Utils.checkArbeitsgruppe(auth, pn)
						|| Utils.checkVertreter(auth, pn))
					return "restrictedGet";
				return "userGet";
		}
		return "";
	}

	/**
	 * Die Methode <i>getColumns</i> wird während einer Anfrage an die
	 * /mitarbeiter/get Resource aufgerufen und bekommt eine Aktion als Parameter,
	 * von welcher abhängig sie die Spalten zurück gibt, welche der Client sehen
	 * darf zurück.
	 * 
	 * @param action - Auf der Rechteklasse des Clients und des angefragten
	 *               Mitarbeiters basierende Aktion
	 * @return String - Die Methode gibt die erlaubten Spalten durch ", " getrennt
	 *         als String zurück. Im Fall eines Fehlers würde ein leerer String
	 *         zurückgegeben werden.
	 */
	public static String getColumns(String action) {
		switch (action) {
			case "selfGet":
				return "Personalnummer, Name, Vorname, erreichbar, Arbeitskonto, EMail, Status, Rechteklasse, Abteilung, Passwort, Vertreter, gda";
			case "unrestrictedGet":
				return "Personalnummer, Name, Vorname, erreichbar, Arbeitskonto, EMail, Status, Rechteklasse, Abteilung, Vertreter, gda";
			case "restrictedGet":
				return "Personalnummer, Name, Vorname, erreichbar, EMail, Status, Rechteklasse, Abteilung, Vertreter";
		}
		return "";
	}

	/**
	 * Die Methode <i>getAlterAction</i> wird aufgerufen, wenn der Client eine
	 * Anfrage an die /mitarbeiter/alter Resource stellt und bestimmt abhängig von
	 * den Rechteklassen des Clients und des Mitarbeiters, wessen Attribut verändert
	 * werden soll, eine im RightHandler registrierte Aktion, welche als String
	 * zurückgegeben wird.
	 * 
	 * @param auth      - SessionID der Anfrage
	 * @param attribute - das angefragte Attribut
	 * @return String - eine Aktion aus: SelfAlter, unrestrictedAlter,
	 *         restrictedAlter.
	 */
	public static String getAlterAction(String auth, String pn) {
		if (Utils.checkSelfOperation(auth, pn))
			return "selfAlter";
		switch (RightHandler.getRightclassFromSessionID(auth)) {
			case "root":
				return "unrestrictedAlter";
			case "admin":
				return "unrestrictedAlter";
			case "personnelDepartment":
				return "unrestrictedAlter";
			case "headOfDepartment":
				return "restrictedAlter";
			case "user":
				return "restrictedAlter";
		}
		return "";
	}
}
