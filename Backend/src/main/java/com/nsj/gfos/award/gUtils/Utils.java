package com.nsj.gfos.award.gUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

/**
 * Die Klasse Utils enthält eine Ansammlung von Hilfsmethoden, welche in den
 * Resources oder in anderen Hilfmethoden benutzt werden.
 * 
 * @author Schnuels
 * @author Artemis
 */
public class Utils {

	/**
	 * Die Methode <i>createMitarbeiterID</i> gibt die niedrigste Personalnummer als
	 * 12-stelligen String aus, wobei die nicht benutzten Stellen vorne mit Nullen
	 * aufgefüllt werden: 1234 --> "000000001234".
	 * 
	 * @return String - Die niedrigste, nicht benutzte Personalnummer.
	 */
	public static String createMitarbeiterID() {
		long id = 1;
		String idAsString = "";
		do {
			idAsString = "";
			for (int i = 0; i < 12 - Long.toString(id).length(); i++) {
				idAsString += "0";
			}
			idAsString += Long.toString(id);
			String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE \"" + idAsString
					+ "\" = gfos.mitarbeiter.Personalnummer;";
			try {
				ResultSet rs = QueryHandler.query(sqlStmt);
				if (!rs.next()) {
					return idAsString;
				} else {
					id++;
				}
			} catch (SQLException e) {
				return "";
			}
		} while (id <= 999999999999l);
		return "";
	}

	/**
	 * Die Methode <i>checkIfMitarbeiterExists</i> überprüft anhand einer gegebenen
	 * Personalnummer, ob ein Mitarbeiter mit dieser Nummer bereits in der Datenbank
	 * existiert.
	 * 
	 * @param pn - Personalnummer des zu überprüfenden Mitarbeiters
	 * @return boolean - true wenn der Mtiarbeiter in der Datenbank existiert,
	 *         false, wenn nicht.
	 */
	public static boolean checkIfMitarbeiterExists(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>createArbeitsgruppenID</i> gibt die niedrigste
	 * Arbeitsgruppennummer als 12-stelligen String aus, wobei die nicht benutzten
	 * Stellen vorne mit Nullen aufgefüllt werden: 1234 --> "000000001234".
	 * 
	 * @return String - Die niedrigste, nicht benutzte ArbeitsgruppenID.
	 */
	public static String createArbeitsgruppenID() {
		long id = 1;
		String idAsString = "";
		do {
			idAsString = "";
			for (int i = 0; i < 12 - Long.toString(id).length(); i++) {
				idAsString += "0";
			}
			idAsString += Long.toString(id);
			String sqlStmt = "SELECT * From gfos.arbeitsgruppe WHERE \"" + idAsString
					+ "\" = gfos.arbeitsgruppe.ArbeitsgruppenID;";
			try {
				ResultSet rs = QueryHandler.query(sqlStmt);
				if (!rs.next()) {
					return idAsString;
				} else {
					id++;
				}
			} catch (SQLException e) {
				return "";
			}
		} while (id <= 999999999999l);
		return "";

	}

	/**
	 * Die Methode <i>checkIfArbeitsgruppeExistsFromID</i> prüft, ob eine
	 * Arbeitsgruppe mit der ArbeitsgruppenID schon existiert.
	 * 
	 * @param id - ArbeitsgruppenID
	 * @return boolean - Ob schon eine Arbeitsgruppe mit der ArbeitsgruppenID
	 *         existiert.
	 */
	public static boolean checkIfArbeitsgruppeExistsFromID(String id) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppe WHERE ArbeitsgruppenID = \"" + id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>checkIfArbeitsgruppeExistsFromBezeichnung</i> prüft, ob eine
	 * Arbeitsgruppe mit der Bezeichnung schon existiert.
	 * 
	 * @param bezeichnung - Bezeichnung der Arbeitsgruppe
	 * @return boolean - Ob schon eine Arbeitsgruppe mit der Bezeichnung existiert.
	 */
	public static boolean checkIfArbeitsgruppeExistsFromBezeichnung(String bezeichnung) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppe WHERE Bezeichnung = \"" + bezeichnung + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>getArbeitsgruppenIDFromBezeichnung</i> gibt die Bezeichnung
	 * der Arbeitsgruppe zurück, die die ArbeitsgruppenID hat.
	 * 
	 * @param bezeichnung - Bezeichnung der Arbeitsgruppe, dessen ID man haben
	 *                    möchte
	 * @return String - ArbeitsgruppenID
	 */
	public static String getArbeitsgruppenIDFromBezeichnung(String bezeichnung) {
		String sqlStmt = "SELECT ArbeitsgruppenID FROM gfos.arbeitsgruppe WHERE Bezeichnung = \"" + bezeichnung + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return "";
			return rs.getString("ArbeitsgruppenID");
		} catch (SQLException e) {
			return "";
		}

	}

	/**
	 * Die Methode <i>checkVertreter</i> wird in der Methode <i>getGetAction</i>
	 * aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer
	 * als Vertreter des Clients eingetragen ist. Das Ergebnis dieser Abfrage wird
	 * als Boolean zurückgegeben.
	 * 
	 * @param auth - SessionID des Clients
	 * @param pn   - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter zu pn Vertreter
	 *         des Clients ist.
	 */
	public static boolean checkVertreter(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = \"" + auth + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT Personalnummer FROM gfos.mitarbeiter WHERE Vertreter = \"" + pn + "\" AND Personalnummer = \""
				+ sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Die Methode <i>checkAbteilung</i> wird in der Methode <i>getGetAction</i>
	 * aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer
	 * in der gleichen Abteilung wie der Client ist.
	 * 
	 * @param auth - SessionID des Clients
	 * @param pn   - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter und der Client
	 *         in der gleichen Abteilung sind.
	 */
	public static boolean checkAbteilung(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = \"" + auth + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT Abteilung FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\" OR Personalnummer = \""
				+ sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			rs.next();
			if (rs.getString("Abteilung").equals(""))
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Die Methode <i>checkArbeitsgruppe</i> wird in der Methode <i>getGetAction</i>
	 * aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer
	 * und der Client in mindestens einer gemeinsamen Arbeitsgruppe sind.
	 * 
	 * @param auth - SessionID des Clients
	 * @param pn   - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter und der Client
	 *         in mindestens einer gemeinsamen Arbeitsgruppe sind.
	 */
	public static boolean checkArbeitsgruppe(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = " + auth + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT ArbeitsgruppenID FROM gfos.arbeitsgruppenteilnahme WHERE Personalnummer = \"" + pn
				+ "\" OR Personalnummer = \"" + sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			rs.next();
			if (rs.getString("ArbeitsgruppenID").equals(""))
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Die Methode <i>checkReferencesInDatabase</i> wird aufgerufen, wenn bei einer
	 * Anfrage an die /mitarbeiter/remove Resource der Mitarbeiter trotz
	 * einwandfreiem Verlauf der Anfrage an die Datenbank der Mitarbeiter nicht
	 * gelöscht wurde, und gibt dem Client die Gründe für das Fehlschlagen zurück.
	 * 
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return String - Der Rückgabewert setzt sich aus 3 möglichen Fehlern
	 *         zusammen: Mitarbeiter ist noch angemeldet, Mitarbeiter ist noch als
	 *         Vertreter eingetragen, Mitarbeiter ist noch Teil einer Arbeitsgruppe
	 */
	public static String checkReferencesInDatabase(String pn) {
		String error = "";
		if (SessionHandler.checkIfUserIsConnected(pn))
			error += " Mitarbeiter ist noch angemeldet.";
		if (isVertreter(pn))
			error += " Mitarbeiter ist noch als Vertreter eingetragen.";
		if (isInArbeitsgruppe(pn))
			error += " Mitarbeiter ist noch Teil einer Arbeitsgruppe.";
		return error;
	}

	/**
	 * Die Methode<i>isInArbeitsgruppe</i> mit einem Parameter wird in der Methode
	 * <i>checkReferencesInDatabase</i> aufgerufen. Sie prüft, ob der zu löschende
	 * Mitarbeiter noch Mitglied in einer Arbeitsgruppe ist und gibt das Ergebnis
	 * dieser Abfrage als boolean zurück.
	 * 
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter noch Mitglied
	 *         in einer Arbeitsgruppe ist.
	 */
	private static boolean isInArbeitsgruppe(String pn) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppenteilnahme WHERE Mitarbeiter = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode<i>isVertreter</i> wird in der Methode
	 * <i>checkReferencesInDatabase</i> aufgerufen. Sie prüft, ob der zu löschende
	 * Mitarbeiter noch als Vertreter von irgendeinem anderen Mitarbeiter
	 * eingetragen ist.
	 * 
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter noch als
	 *         Vertreter eingetragen ist.
	 */
	private static boolean isVertreter(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Vertreter = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>getFormattedValue</i> wird bei einer Anfrage an die
	 * /mitarbeiter/alter Resource aufgerufen und hängt, falls der als Parameter
	 * übergebene Parameter aus der URL in der Datenbank als String gespeichert
	 * wird, an beide Enden des Strings Anführungszeichen, damit der Wert auch in
	 * der SQL-Abfrage als String erkannt wird.
	 * 
	 * @param param - Der an "=" gesplitette Parameter also beispielsweise:
	 *              {"pw","sicheresPasswort123"}
	 * @return String - Entweder wird der Formatierte Wert zurückgegeben:
	 *         "\"sicheresPasswort123\"", oder der unveränderte Wert:
	 *         "sicheresPasswort123".
	 */
	public static String getFormattedValue(String[] param) {
		String[] strings = { "n", "vn", "em", "s", "rk", "gda", "ab", "ve" };
		if (Arrays.asList(strings).contains(param[0]))
			return "\"" + param[1] + "\"";
		return param[1];
	}

	/**
	 * Die Methode <i>getColumnName</i> wird bei einer Anfrage an die
	 * /mitarbeiter/alter Resource aufgerufen und gibt den vollen Namen einer
	 * Spalte, abhängig von dem übergebeben Kürzel zurück.
	 * 
	 * @param param - Kürzel des Parameters
	 * @return String - Gibt den vollen Namen des übergebenen Kürzels zurück.
	 */
	public static String getColumnName(String param) {
		switch (param) {
			case "n":
				return "Name";
			case "vn":
				return "Vorname";
			case "er":
				return "erreichbar";
			case "em":
				return "EMail";
			case "ak":
				return "Arbeitskonto";
			case "pw":
				return "Passwort";
			case "s":
				return "Status";
			case "rk":
				return "Rechteklasse";
			case "ab":
				return "Abteilung";
			case "gda":
				return "gda";
			case "ve":
				return "Vertreter";
		}
		return "";
	}

	/**
	 * Die Methode<i>isInArbeitsgruppe</i> mit zwei Parametern wird bei mehreren
	 * Anfragen an die /arbeitsgruppen Resources aufgerufen und prüft ob ein
	 * Mitarbeiter in einer bestimmten Arbeitsgruppe ist.
	 * 
	 * @param id - ID der zu überprüfenden Arbeitsgruppe
	 * @param pn - Personalnummer des zu überprüfenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter Mitglied in der
	 *         angegebenen Arbeitsgruppe ist.
	 */
	public static boolean isInArbeitsgruppe(String id, String pn) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppenteilnahme WHERE Mitarbeiter = \"" + pn
				+ "\" AND ArbeitsgruppenID = \"" + id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>checkSelfOperation</i> prüft, ob der Client mit dem
	 * angegebenen Mitarbeiter übereinstimmt, und überprüft so, ob der Client eine
	 * Aktion wie z.B sich selbst löschen, oder Daten von sich selbst anfordern
	 * ausführen möchte.
	 * 
	 * @param auth - SessionID des Clients
	 * @param pn   - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Gibt true zurück, wenn der Mitarbeiter mit dem Client
	 *         übereinstimmt und false, wenn nicht.
	 */
	public static boolean checkSelfOperation(String auth, String pn) {
		String sqlStmt = "SELECT * FROM gfos.active_sessions WHERE SessionID = \"" + auth + "\" AND Mitarbeiter = \""
				+ pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * Die Methode <i>getPersonalnummerFromSessionID</i> gibt die Personalnummer des
	 * Mitarbeiter mit Hilfe der SessionID zurück.
	 * 
	 * @param auth - SessionID des Clients
	 * @return String - Personalnummer des Mitarbeiters
	 */
	public static String getPersonalnummerFromSessionID(String auth) {
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = \"" + auth + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				return rs.getString("Mitarbeiter");
			return "";
		} catch (SQLException e) {
			return "";
		}
	}

	/**
	 * Die Methode <i>getPersonalnummerFromEmail</i> gibt die Personalnummer eines
	 * Mitarbeiters mit Hilfe der Email zurück.
	 * 
	 * @param email - Email des Mitarbeiters
	 * @return String - Personalnummer des Mitarbeiters
	 */
	public static String getPersonalnummerFromEmail(String email) {
		String sqlStmt = "SELECT Personalnummer FROM gfos.mitarbeiter WHERE EMail = \"" + email + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				return rs.getString("Personalnummer");
			return "";
		} catch (SQLException e) {
			return "";
		}
	}

	/**
	 * Die Methode <i>getLeiter</i> gibt den Leiter der Arbeitsgruppe mit Hilde der
	 * ArbeitsgruppenID zurück.
	 * 
	 * @param id - ArbeitsgruppenID
	 * @return String - Personalnummer des Leiter der Arbeitsgruppe
	 */
	public static String getLeiter(String id) {
		String sqlStmt = "SELECT Leiter FROM gfos.arbeitsgruppe WHERE ArbeitsgruppenID = " + id + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (rs.next())
				return rs.getString("Leiter");
			return "";
		} catch (SQLException e) {
			return "";
		}
	}
}