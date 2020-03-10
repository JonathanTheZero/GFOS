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

import com.nsj.gfos.award.gUtils.Utils;

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
					Mitarbeiter m = Utils.createMitarbeiterFromQuery(rs, new String[] {"Personalnummer", "Name", "Vorname", "erreichbar", "Arbeitskonto", "EMail", "Status", "Rechteklasse", "Abteilung", "Vertreter", "gda"});
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
		if (attributes.length != 8)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String name = "\"" + attributes[1].split("=")[1] + "\"";
		String vorname = "\"" + attributes[2].split("=")[1] + "\"";
		String email = "\"" + attributes[3].split("=")[1] + "\"";
		String passwort = "\"" + PasswordHandler.getHash(attributes[4].split("=")[1]) + "\"";
		String rechteklasse = attributes[5].split("=")[1];
		String abteilung = "\"" + attributes[6].split("=")[1] + "\"";
		String vertreter = "\"" + attributes[7].split("=")[1] + "\"";
		String pn = Utils.createMitarbeiterID();
		if (rechteklasse.equals("root"))
			return JsonHandler.fehler("Root Account kann nicht erstellt werden.");
		if (!RightHandler.checkPermission(auth, (rechteklasse.equals("admin")) ? "addAdmin" : "addMitarbeiter"))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		String sqlStmt = "INSERT INTO gfos.mitarbeiter VALUES(\"" + pn + "\"," + name + "," + vorname + ", false, 0," 
			+ email + "," + passwort + ", Abwesend, - ,\"" + rechteklasse
				+ "\"," + abteilung + "," + vertreter + ");";
		if (Utils.checkIfMitarbeiterExists(pn))
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
		String action = Utils.getGetAction(auth, pn);
		if (!RightHandler.checkPermission(auth, action))
			return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String columns = Utils.getColumns(action);
		String sqlStmt = "SELECT " + columns + " FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Mitarbeiter m = Utils.createMitarbeiterFromQuery(rs, columns.split(", "));
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
		if (pn.length() != 12 || !Utils.checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if (RightHandler.getRightClassFromPersonalnummer(pn).equals("root"))
			return JsonHandler.fehler("Root Account kann nicht entfernt werden.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (Utils.checkSelfOperation(auth, pn))
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
			if (Utils.checkIfMitarbeiterExists("\"" + pn + "\""))
				return JsonHandler.fehler(Utils.checkReferencesInDatabase(pn));
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
		if(params[1].split("=")[1].length() != 12 || !Utils.checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Ungültige Personalnummer angegeben.");
		String values = "";
		for(int i = 2; i < params.length; i++) {
			if(params[i].split("=").length != 2)
				return JsonHandler.fehler("Parameter falsch formatiert.");
			if(!Arrays.asList(validParams).contains(params[i].split("=")[0]))
				return JsonHandler.fehler("Kein valider Parameter.");
			String action = Utils.getAlterAction(params[0].split("=")[1], pn);
			if (!RightHandler.checkPermission(params[0].split("=")[1], action) || !RightHandler.permittedAttribute(action, params[i].split("=")[0]))
				return JsonHandler.fehler("Keine Genehmigung für diese Aktion erhalten.");
			values += Utils.getColumnName(params[i].split("=")[0]) + " = " + Utils.getFormattedValue(params[i].split("=")) + ", ";
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
	
}
