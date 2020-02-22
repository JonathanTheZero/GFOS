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
import org.glassfish.jersey.server.JSONP;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
			rs = QueryHandler.query("SELECT * FROM mitarbeiter;");
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
	// http://localhost:8080/award/api/mitarbeiter/add:auth=123456789012&pn=000000000001&n=Sommerfeld&vn=Nils&er=0&ak=20&em=n.s@e.de&pw=1234&s=Abwesend&gda=Feierabend&rk=Admin&ab=IT-Sicherheit&ve=000000000000

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
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if (RightHandler.getRightClassFromPersonalnummer(pn).equals("root"))
			return JsonHandler.fehler("Root Account kann nicht entfernt werden.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (checkSelfRemoval(auth, pn))
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
		if(params[0].split("=").length != 2 || !params[0].split("=")[0].equals("auth"))
			return JsonHandler.fehler("SessionID benötigt.");
		if(!SessionHandler.checkSessionID(params[0].split("=")[1]))
			return JsonHandler.fehler("Ungültige SessionID.");
		for(String param : params) {
			if(param.split("=").length != 2)
				return JsonHandler.fehler("Parameter falsch formatiert.");
			switch(param.split("=")[0]) {
			case "pn":
        		
        		break;
        	case "n":
        		
            	break;
        	case "vn":
        	
        		break;
        	case "er":
        	
                break;
        	case "ak":
        	
                break;
        	case "em":
        	
                break;
        	case "pw":
        	
                break;
        	case "s":
        		
                break;
        	case "rk":
        		 
                break;
        	case "gda":
        		
                break;
        	case "ab":
        		
                break;
        	case "ve":
        		
                break;
			}
		}
		return "";
	}

	private String getColumns(String action) {
		// TODO Methode schreiben
		return "Personalnummer, Vorname, Status";
	}

	private String getGetAction(String auth, String pn) {
		// TODO Methode schreiben
		return "test";
	}

	private String checkReferencesInDatabase(String pn) {
		String error = "";
		if (SessionHandler.doSessionsExistForPersonalnummer(pn))
			error += " Mitarbeiter ist noch angemeldet.";
		if (isVertreter(pn))
			error += " Mitarbeiter ist noch als Vertreter eingetragen.";
		// TODO Arbeitsgruppe prüfen
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

	private boolean checkSelfRemoval(String auth, String pn) {
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
        		m.setErreichbar((rs.getString("erreichbar").equals("1")) ? "true" : "false");
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
