package com.nsj.gfos.award.gUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.RightHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

/**
 * Die Klasse Utils enthält eine Ansammlung von Hilfsmethoden, welche in den Resources oder in anderen Hilfmethoden benutzt werden.
 * @author Schnuels
 * @author Artemis
 */
public class Utils {

	/**
	 * Die Methode <i>createMitarbeiterFromQuery</i> bekommt ein ResultSet und einen Array von Spalten übergeben, und speichert die Werte aus den angegebenen Spalten
	 * der Datenbank in einem Mitarbeiter Objekt, welches schlussendlich zurückgegeben wird.
	 * @param rs - ResultSet aus einer Abfrage auf der Mitarbeiter Tabelle der Datenbank
	 * @param columns - Ein Array aller Spalten, dessen Werte übernommen werden.
	 * @return Mitarbeiter - Objekt vom Typ Mitarbeiter != null.
	 * @throws SQLException - falls das ResultSet eine Spalte nicht finden kann.
	 */
	public static Mitarbeiter createMitarbeiterFromQuery(ResultSet rs, String[] columns) throws SQLException{
        Mitarbeiter m = new Mitarbeiter();
        for(String column : columns) {
        	switch (column) {
        	case "Personalnummer":
        		m.setPersonalnummer(rs.getString("Personalnummer"));
        		break;
        	case "Name":
        		m.setName(rs.getString("Name"));
            	break;
        	case "Vorname":
        		 m.setVorname(rs.getString("Vorname"));     
        		break;
        	case "erreichbar":
        		m.setErreichbar((rs.getString("erreichbar").equals("1") ? true : false));
                break;
        	case "Arbeitskonto":
        		m.setArbeitskonto(Integer.parseInt(rs.getString("Arbeitskonto")));
                break;
        	case "EMail":
        		m.setEmail(rs.getString("EMail"));
                break;
        	case "Status":
        		m.setStatus(rs.getString("Status"));
                break;
        	case "Rechteklasse":
        		 m.setRechteklasse(rs.getString("Rechteklasse"));
                break;
        	case "Passwort":
       		 m.setPasswort(rs.getString("Passwort"));
               break;
        	case "Abteilung":
        		 m.setAbteilung(rs.getString("Abteilung"));
                break;
        	case "Vertreter":
        		m.setVertreter(rs.getString("Vertreter"));
                break;
        	case "gda":
        		m.setGrundDAbw(rs.getString("gda"));
                break;
        	}
        }      
        return m;
    }
	
	/**
	 * Die Methode <i>createMitarbeiterID</i> gibt die niedrigste Personalnummer als 12-stelligen String aus, wobei
	 * die nicht benutzten Stellen vorne mit Nullen aufgefüllt werden: 1234 --> "000000001234".
	 * @return String - Die niedrigste, nicht benutzte Personalnummer. 
	 */
	public String createMitarbeiterID() {
		long id = 1;
		String idAsString = "";
		do {
			idAsString = "";
			for(int i = 0; i < 12 - Long.toString(id).length(); i++) {
				idAsString += "0";		
			}
			idAsString += Long.toString(id);
			String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE \""+ idAsString + "\" = gfos.mitarbeiter.Personalnummer;";
			try {
				ResultSet rs = QueryHandler.query(sqlStmt);
				if (!rs.next()) {
					return idAsString;
				}else {
					id++;
				}
			} catch (SQLException e) {
				return "";
			}
		}while(id <= 999999999999l);
		return "";
	}
	
	/**
	 * Die Methode <i>checkIfMitarbeiterExists</i> überprüft anhand einer gegebenen Personalnummer, ob ein Mitarbeiter mit dieser Nummer bereits in der Datenbank existiert.
	 * @param pn - Personalnummer des zu überprüfenden Mitarbeiters
	 * @return boolean - true wenn der Mtiarbeiter in der Datenbank existiert, false, wenn nicht.
	 */
	public static boolean checkIfMitarbeiterExists(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Personalnummer = " + pn + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}
		
	/**
	 * Aus dem ResultSet wird eine Arbeitsgruppe erstellt, die zurückgegeben wird.
	 * 
	 */
	public static Arbeitsgruppe createArbeitsgruppeFromQuery(ResultSet rs) throws SQLException {
		Arbeitsgruppe a = new Arbeitsgruppe();
		a.setBezeichnung(rs.getString("Bezeichnung"));
		a.setLeiter(rs.getString("Leiter"));
		a.setArbeitsgruppenID(rs.getString("ArbeitsgruppenID"));
		do {
			a.addMitglied(rs.getString("Mitarbeiter"));
		} while (rs.next());
		return a;
	}
	
	/**
	 * Es wird von der niedrigsten ArbeitsnummerID ausgegangen geguckt, ob diese belegt ist.
	 * Falls ja, wird die ID um 1 erhöht, wenn nein, wird die ID zurückgegeben.
	 */
	public static String createArbeitsgruppenID() {
		boolean available = false;
		int id = 1;
		String idInString;
		do {
			idInString = "";
			for(int i = 0; i < 12 - Integer.toString(id).length(); i++) {
				idInString += "0";		
			}
			idInString += Integer.toString(id);
			String sqlStmt = "SELECT * From gfos.arbeitsgruppe WHERE \""+ idInString + "\" = gfos.arbeitsgruppe.ArbeitsgruppenID;";
			try {
				ResultSet rs = QueryHandler.query(sqlStmt);
				if (!rs.next()) {
					available = true;
					return idInString;
				}else {
					id++;
				}
			} catch (SQLException e) {
				return "";
			}
		}while(!available);
		return "";
	}
		
	public static boolean checkIfArbeitsgruppeExistsFromID(String id) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppe WHERE ArbeitsgruppenID = \"" + id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}
	
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
	 * Die Methode <i>getGetAction</i> wird aufgerufen, wenn der Client eine Anfrage an die /mitarbeiter/get Resource stellt und bestimmt abhängig von den  
	 * Rechteklassen des Clients und des Mitarbeiters, welcher zurückgegeben werden soll, eine im RightHandler registrierte Aktion, welche als String zurückgegeben wird. 
	 * @param auth - SessionID der Anfrage
	 * @param pn - Personalnummer des angefragten Mitarbeiters
	 * @return String - Eine Aktion aus: selfGet, unrestrictedGet, restrictedGet, userGet.
	 */
	public static String getGetAction(String auth, String pn) {
		if(checkSelfOperation(auth, pn))
			return "selfGet";
		switch(RightHandler.getRightclassFromSessionID(auth)) {
		case "root":
			return "unrestrictedGet";
		case "admin":
			return "unrestrictedGet";
		case "personnelDepartment":
			return "unrestrictedGet";
		case "headOfDepartment":
			return "restrictedGet";
		case "user":
			if(checkAbteilung(auth, pn) || checkArbeitsgruppe(auth, pn) || checkVertreter(auth, pn))
				return "restrictedGet";
			return "userGet";
		}
		return "";
	}
	
	/**
	 * Die Methode <i>getColumns</i> wird während einer Anfrage an die /mitarbeiter/get Resource aufgerufen und bekommt eine Aktion als Parameter,
	 * von welcher abhängig sie die Spalten zurück gibt, welche der Client sehen darf zurück.
	 * @param action - Auf der Rechteklasse des Clients und des angefragten Mitarbeiters basierende Aktion 
	 * @return String - Die Methode gibt die erlaubten Spalten durch ", " getrennt als String zurück. Im Fall eines Fehlers würde ein leerer String zurückgegeben werden.
	 */
	public static String getColumns(String action) {
		switch(action) {
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
	 * Die Methode <i>checkVertreter</i> wird in der Methode <i>getGetAction</i> aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer
	 * als Vertreter des Clients eingetragen ist. Das Ergebnis dieser Abfrage wird als Boolean zurückgegeben.
	 * @param auth - SessionID des Clients
	 * @param pn - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean -  Die Methode gibt zurück, ob der Mitarbeiter zu pn Vertreter des Clients ist.
	 */
	private static boolean checkVertreter(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = " + auth + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT Personalnummer FROM gfos.mitarbeiter WHERE Vertreter = \"" + pn + "\" AND Personalnummer = \"" + sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);			
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Die Methode <i>checkAbteilung</i> wird in der Methode <i>getGetAction</i> aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer
	 * in der gleichen Abteilung wie der Client ist.
	 * @param auth - SessionID des Clients
	 * @param pn - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter und der Client in der gleichen Abteilung sind.
	 */
	private static boolean checkAbteilung(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = " + auth + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT Abteilung FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\" OR Personalnummer = \"" + sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			rs.next();
			if(rs.getString("Abteilung").equals(""))
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Die Methode <i>checkArbeitsgruppe</i> wird in der Methode <i>getGetAction</i> aufgerufen und prüft, ob der Mitarbeiter mit der angegebenen Personalnummer 
	 * und der Client in mindestens einer gemeinsamen Arbeitsgruppe sind.
	 * @param auth - SessionID des Clients
	 * @param pn - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter und der Client in mindestens einer gemeinsamen Arbeitsgruppe sind.
	 */
	private static boolean checkArbeitsgruppe(String auth, String pn) {
		String sessionPn = "";
		String sqlStmt = "SELECT Mitarbeiter FROM gfos.active_sessions WHERE SessionID = " + auth + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs.next())
				sessionPn = rs.getString("Mitarbeiter");
		} catch (SQLException e) {
			return false;
		}
		sqlStmt = "SELECT ArbeitsgruppenID FROM gfos.arbeitsgruppenteilnahme WHERE Personalnummer = \"" + pn + "\" OR Personalnummer = \"" + sessionPn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			rs.next();
			if(rs.getString("ArbeitsgruppenID").equals(""))
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Die Methode <i>checkReferencesInDatabase</i> wird aufgerufen, wenn bei einer Anfrage an die /mitarbeiter/remove Resource der Mitarbeiter trotz einwandfreiem Verlauf
	 * der Anfrage an die Datenbank der Mitarbeiter nicht gelöscht wurde, und gibt dem Client die Gründe für das Fehlschlagen zurück.
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return String - Der Rückgabewert setzt sich aus 3 möglichen Fehlern zusammen: Mitarbeiter ist noch angemeldet,
	 *  Mitarbeiter ist noch als Vertreter eingetragen, Mitarbeiter ist noch Teil einer Arbeitsgruppe
	 */
	public static String checkReferencesInDatabase(String pn) {
		String error = "";
		if (SessionHandler.doSessionsExistForPersonalnummer(pn))
			error += " Mitarbeiter ist noch angemeldet.";
		if (isVertreter(pn))
			error += " Mitarbeiter ist noch als Vertreter eingetragen.";
		if(isInArbeitsgruppe(pn))
			error += " Mitarbeiter ist noch Teil einer Arbeitsgruppe.";
		return error;
	}
	
	/**
	 * Die Methode<i>isInArbeitsgruppe</i> mit einem Parameter wird in der Methode <i>checkReferencesInDatabase</i> aufgerufen. Sie prüft, ob der zu löschende Mitarbeiter
	 * noch Mitglied in einer Arbeitsgruppe ist und gibt das Ergebnis dieser Abfrage als boolean zurück. 
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter noch Mitglied in einer Arbeitsgruppe ist.
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
	 * Die Methode<i>isVertreter</i> wird in der Methode <i>checkReferencesInDatabase</i> aufgerufen. Sie prüft, ob der zu löschende Mitarbeiter
	 * noch als Vertreter von irgendeinem anderen Mitarbeiter eingetragen ist.
	 * @param pn - Personalnummer des zu löschenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter noch als Vertreter eingetragen ist.
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
	 * Die Methode <i>getFormattedValue</i> wird bei einer Anfrage an die /mitarbeiter/alter Resource aufgerufen und hängt, falls der als Parameter übergebene Parameter aus
	 * der URL in der Datenbank als String gespeichert wird, an beide Enden des Strings Anführungszeichen, damit der Wert auch in der SQL-Abfrage als String erkannt wird.
	 * @param param - Der an "=" gesplitette Parameter also beispielsweise: {"pw","sicheresPasswort123"}
	 * @return String - Entweder wird der Formatierte Wert zurückgegeben: "\"sicheresPasswort123\"", oder der unveränderte Wert: "sicheresPasswort123".
	 */
	public static String getFormattedValue(String[] param) {
		String[] strings = {"n", "vn", "em", "s", "pw", "rk", "gda", "ab", "ve"};
		if(Arrays.asList(strings).contains(param[0]))
			return "\"" + param[1] + "\"";
		return param[1];
	}
	
	/**
	 * Die Methode <i>getFormattedValue</i> wird bei einer Anfrage an die /mitarbeiter/alter Resource aufgerufen und gibt den vollen Namen einer Spalte,
	 * abhängig von dem übergebeben Kürzel zurück. 
	 * @param param - Kürzel des Parameters
	 * @return String - Gibt den vollen Namen des übergebenen Kürzels zurück.
	 */
	public static String getColumnName(String param) {
		switch(param) {
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
	 * Die Methode<i>isInArbeitsgruppe</i> mit zwei Parametern wird bei mehreren Anfragen an die /arbeitsgruppen Resources aufgerufen und prüft ob ein 
	 * Mitarbeiter in einer bestimmten Arbeitsgruppe ist.
	 * @param id - ID der zu überprüfenden Arbeitsgruppe
	 * @param pn - Personalnummer des zu überprüfenden Mitarbeiters
	 * @return boolean - Die Methode gibt zurück, ob der Mitarbeiter Mitglied in der angegebenen Arbeitsgruppe ist.
	 */
	public static boolean isInArbeitsgruppe(String id, String pn) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppenteilnahme WHERE Mitarbeiter = \"" + pn + "\" AND ArbeitsgruppenID = \"" + id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}
	
	/**
	 * Die Methode <i>checkSelfOperation</i> prüft, ob der Client mit dem angegebenen Mitarbeiter übereinstimmt, und überprüft so, ob der Client eine Aktion wie z.B 
	 * sich selbst löschen, oder Daten von sich selbst anfordern ausführen möchte.
	 * @param auth - SessionID des Clients
	 * @param pn - Personalnummer des abgefragten Mitarbeiters
	 * @return boolean - Gibt true zurück, wenn der Mitarbeiter mit dem Client übereinstimmt und false, wenn nicht.
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
	
}