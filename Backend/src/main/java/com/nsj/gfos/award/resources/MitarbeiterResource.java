/*
d * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.PasswordHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.RightHandler;
import com.nsj.gfos.award.handlers.SessionHandler;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import org.codehaus.jackson.map.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Path("mitarbeiter")
public class MitarbeiterResource {
	
	@GET
	@Path("getAll{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllMitarbeiter(@PathParam("param") String param) {
		if (param.split("=").length != 2)
			return JsonHandler.fehler("Parameter ist falsch formatiert.");
		String auth = param.split("=")[1];
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!RightHandler.checkPermission(auth, "getAllMitarbeiter"))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		ResultSet rs = null;
		try {
			rs = QueryHandler.query("SELECT * FROM gfos.mitarbeiter;");
			ArrayList<Mitarbeiter> allEmpl = new ArrayList<Mitarbeiter>();
			ObjectMapper om = new ObjectMapper();
			while (rs.next()) {
				try {
					Mitarbeiter m = createMitarbeiterFromQuery(rs, new String[] {"Personalnummer", "Name", "Vorname", "erreichbar", "Arbeitskonto", "EMail", "Status", "Rechteklasse", "Abteilung", "Vertreter", "gda"});
					allEmpl.add(m);
				} catch (Exception e) {
					return JsonHandler.fehler(e.toString());
				}
			}
			try {
				return om.writeValueAsString(allEmpl);
			} catch (Exception e) {
				return JsonHandler.fehler(e.toString());
			}
		} catch (Exception e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add{attributes}")
	public String addMitarbeiter(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 13)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String pn = "\"" + attributes[1].split("=")[1] + "\"";
		String name = "\"" + attributes[2].split("=")[1] + "\"";
		String vorname = "\"" + attributes[3].split("=")[1] + "\"";
		String erreichbar = attributes[4].split("=")[1];
		String arbeitskonto = attributes[5].split("=")[1];
		String email = "\"" + attributes[6].split("=")[1] + "\"";
		String passwort = "\"" + PasswordHandler.getHash(attributes[7].split("=")[1]) + "\"";
		String status = "\"" + attributes[8].split("=")[1] + "\"";
		String gda = "\"" + attributes[9].split("=")[1] + "\"";
		String rechteklasse = attributes[10].split("=")[1];
		String abteilung = "\"" + attributes[11].split("=")[1] + "\"";
		String vertreter = "\"" + attributes[12].split("=")[1] + "\"";
		if (rechteklasse.equals("root"))
			return JsonHandler.fehler("Root Account kann nicht erstellt werden.");
		if (!RightHandler.checkPermission(auth, (rechteklasse.equals("admin")) ? "addAdmin" : "addMitarbeiter"))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		String sqlStmt = "INSERT INTO gfos.mitarbeiter VALUES(" + pn + "," + name + "," + vorname + "," + erreichbar
				+ "," + arbeitskonto + "," + email + "," + passwort + "," + status + "," + gda + ",\"" + rechteklasse
				+ "\"," + abteilung + "," + vertreter + ");";
		if (checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Personalnummer wurde bereits verwendet.");
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Fehler!");
			return JsonHandler.erfolg("Mitarbeiter wurde erfolgreich hinzugefügt.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get{params}")
	public String getMitarbeiter(@PathParam("params") String query) {
		query = query.substring(1);
		String[] params = query.split("&");
		if (params.length != 2)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		if (params[0].split("=").length != 2 || params[1].split("=").length != 2)
			return JsonHandler.fehler("Falsche Formatierung der Parameter.");
		String auth = params[0].split("=")[1];
		String pn = params[1].split("=")[1];
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		String action = getGetAction(auth, pn);
		if (!RightHandler.checkPermission(auth, action))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String columns = getColumns(action);
		String sqlStmt = "SELECT " + columns + " FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Mitarbeiter m = createMitarbeiterFromQuery(rs, columns.split(", "));
			return JsonHandler.createJsonFromMitarbeiter(m);
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("remove{params}")
	public String removeMitarbeiter(@PathParam("params") String query) {
		query = query.substring(1);
		String[] params = query.split("&");
		if (params.length != 2)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		if (params[0].split("=").length != 2 || params[1].split("=").length != 2)
			return JsonHandler.fehler("Falsche Formatierung der Parameter.");
		String auth = params[0].split("=")[1];
		String pn = params[1].split("=")[1];
		if (pn.length() != 12 || !checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if (RightHandler.getRightClassFromPersonalnummer(pn).equals("root"))
			return JsonHandler.fehler("Root Account kann nicht entfernt werden.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (checkSelfOperation(auth, pn))
			return JsonHandler.fehler("Ein Account kann nicht sich selbst löschen.");
		if (!RightHandler.checkPermission(auth,
				(RightHandler.getRightClassFromPersonalnummer(pn).equals("admin") ? "removeAdmin"
						: "removeMitarbeiter")))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		String sqlStmt = "DELETE FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Personalnummer existiert nicht.");
			if (checkIfMitarbeiterExists("\"" + pn + "\""))
				return JsonHandler.fehler(checkReferencesInDatabase(pn));
			return JsonHandler.erfolg("Mitarbeiter wurde erfolgreich gelöscht.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	@GET
	@Path("alter{params}")
	@Produces(MediaType.APPLICATION_JSON)
	public String alterMitarbeiter(@PathParam("params") String query) {
		query = query.substring(1);
		String[] params = query.split("&");
		String[] validParams = {"n", "vn", "er", "em", "ak", "s", "pw", "rk", "gda", "ab", "ve"};
		if(params.length < 3)
			return JsonHandler.fehler("Zu wenige Parameter.");
		if(params[0].split("=").length != 2 || !params[0].split("=")[0].equals("auth"))
			return JsonHandler.fehler("SessionID benötigt.");
		if(!SessionHandler.checkSessionID(params[0].split("=")[1]))
			return JsonHandler.fehler("Ungültige SessionID.");
		if(params[1].split("=").length != 2 || !params[1].split("=")[0].equals("pn"))
			return JsonHandler.fehler("Keine Personalnummer angegeben.");
		String pn = params[1].split("=")[1];
		if(params[1].split("=")[1].length() != 12 || !checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Ungültige Personalnummer angegeben.");
		String values = "";
		for(int i = 2; i < params.length; i++) {
			if(params[i].split("=").length != 2)
				return JsonHandler.fehler("Parameter falsch formatiert.");
			if(!Arrays.asList(validParams).contains(params[i].split("=")[0]))
				return JsonHandler.fehler("Kein valider Parameter.");
			//TODO Rechte
			values += getColumnName(params[i].split("=")[0]) + " = " + getFormattedValue(params[i].split("=")) + ", ";
		}	
		String sqlStmt = "UPDATE gfos.mitarbeiter SET " + values.substring(0, values.length() - 2) + "WHERE Personalnummer = " + pn + ";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Die Veränderungen konnten nicht durchgeführt werden.");
			return JsonHandler.erfolg("Werte wurden erfolgreich verändert.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	private String getColumns(String action) {
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
	
	private String getGetAction(String auth, String pn) {
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
	
	private boolean checkVertreter(String auth, String pn) {
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
	
	private boolean checkAbteilung(String auth, String pn) {
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
	
	private boolean checkArbeitsgruppe(String auth, String pn) {
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
	
	private boolean isInArbeitsgruppe(String pn) {
		String sqlStmt = "SELECT * FROM gfos.arbeitsgruppenteilnahme WHERE Personalnummer = " + pn + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}
	
	private String getFormattedValue(String[] param) {
		String[] strings = {"n", "vn", "em", "s", "pw", "rk", "gda", "ab", "ve"};
		if(Arrays.asList(strings).contains(param[0]))
			return "\"" + param[1] + "\"";
		return param[1];
	}
	
	private String getColumnName(String param) {
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

	private String checkReferencesInDatabase(String pn) {
		String error = "";
		if (SessionHandler.doSessionsExistForPersonalnummer(pn))
			error += " Mitarbeiter ist noch angemeldet.";
		if (isVertreter(pn))
			error += " Mitarbeiter ist noch als Vertreter eingetragen.";
		if(isInArbeitsgruppe(pn))
			error += " Mitarbeiter ist noch Teil einer Arbeitsgruppe.";
		return error;
	}

	private boolean isVertreter(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Vertreter = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	private boolean checkSelfOperation(String auth, String pn) {
		String sqlStmt = "SELECT * FROM gfos.active_sessions WHERE SessionID = \"" + auth + "\" AND Mitarbeiter = \""
				+ pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	private boolean checkIfMitarbeiterExists(String pn) {
		String sqlStmt = "SELECT * FROM gfos.mitarbeiter WHERE Personalnummer = " + pn + ";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return true;
		}
	}

	private Mitarbeiter createMitarbeiterFromQuery(ResultSet rs, String[] columns) throws SQLException{
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
