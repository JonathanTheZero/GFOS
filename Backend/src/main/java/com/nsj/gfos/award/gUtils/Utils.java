package com.nsj.gfos.award.gUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.RightHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

public class Utils {

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
	
	public static boolean checkVertreter(String auth, String pn) {
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
			if(rs == null) return false;			
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean checkAbteilung(String auth, String pn) {
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
	
	public static boolean checkArbeitsgruppe(String auth, String pn) {
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
	
	public static boolean isInArbeitsgruppe(String pn) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppenteilnahme WHERE Personalnummer = " + pn + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}
	
	public static String getFormattedValue(String[] param) {
		String[] strings = {"n", "vn", "em", "s", "pw", "rk", "gda", "ab", "ve"};
		if(Arrays.asList(strings).contains(param[0]))
			return "\"" + param[1] + "\"";
		return param[1];
	}
	
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

	public static boolean isVertreter(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Vertreter = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

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

	public static boolean checkIfMitarbeiterExists(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Personalnummer = " + pn + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

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
}