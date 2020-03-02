package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.SessionHandler;
import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("arbeitsgruppe")
public class ArbeitsgruppenResource {

	/**
	 * Als Parameter sind die Session ID des bearbeitenden Mitarbeiters und die Personalnummer des Mitarbeiters, von der man die Arbeitsgruppe haben möchte, anzugeben.
	 * Die Parameter werden auf ihre richtiogkeit überprüft.
	 * Dann wird aus der Datenbank die passende Arbeitsgruppe ausgegeben, falls diese existiert.
	 * Die Methode gibt die Arbeitsgruppe als Objekt in Json zurück.
	 */
	@GET
	@Path("get{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArbeitsgruppeFromMitarbeiter(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 2)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		String pn = attributes[1].split("=")[1];
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		// TODO checkRights
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, \r\n"
				+ "       gfos.arbeitsgruppe.Bezeichnung, \r\n" + "       gfos.arbeitsgruppe.Leiter, \r\n"
				+ "       gfos.arbeitsgruppenteilnahme.Personalnummer \r\n" + "FROM   gfos.arbeitsgruppenteilnahme \r\n"
				+ "       JOIN gfos.arbeitsgruppe \r\n"
				+ "         ON gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \r\n"
				+ "            gfos.arbeitsgruppe.ArbeitsgruppenID \r\n" + "WHERE \r\n"
				+ "(SELECT gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.Personalnummer = \""
				+ pn + "\")\r\n" + "= gfos.arbeitsgruppe.ArbeitsgruppenID;";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Arbeitsgruppe a = createArbeitsgruppeFromQuery(rs);
			return JsonHandler.createJsonFromArbeitsgruppe(a);
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	
	/**
	 * Als Parameter sind die Session ID des bearbeitenden Mitarbeitsers, die Personalnummer des Mitarbeiters, der Leiter werden soll, und die Arbeitsgruppen ID der zu bearbeitenden Arbeitsgruppe anzugeben.
	 * Die Attribute werden auf ihre Richtigkeit geprüft.
	 * Dann werden die Rechteklassen des bearbeitenden Mitarbeiters geprüft und dann die des zu werdenden Leiters.
	 * Der Leiter wird in der Datenbank aktualisiert.
	 */
	@GET
	@Path("alterLeiter{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String alterLeiter(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 3)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		String pn = attributes[1].split("=")[1];
		String id = attributes[2].split("=")[1];
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if(id.length() != 12)
			return JsonHandler.fehler("Ungültige ArbeitsgruppenID.");
		// TODO checkRights + die des Leiters
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String sqlStmt = "UPDATE gfos.arbeitsgruppe SET gfos.arbeitsgruppe.Leiter = \"" + pn + "\" WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \"" + id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Die Veränderung konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Leiter wurde erfolgreich verändert.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	
	/**
	 * Als Parameter werden die SessionID, die Bezeichnung der Arbeitsgruppe und der Leiter übergeben.
	 * Dann wird eine neue Arbeitsgruppe erstellt, die die nächst freie ID von der Methode createArbeitsgruppenID() erhält.
	 * Außerdem werden noch die Bezeichnung und der Leiter eingefügt.
	 */
	@GET
	@Path("add{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String addArbeitsgruppe(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 3)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		String bezeichnung = attributes[1].split("=")[1];
		String leiter = attributes[2].split("=")[1];
		if (bezeichnung == "")
			return JsonHandler.fehler("Die Bezeichnung der Arbeitsgruppe ist leer.");
		if(!checkIfMitarbeiterExists(leiter))
			return JsonHandler.fehler("Der Mitarbeiter, der Leiter werden soll, existiert nicht.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		// TODO checkRights + die des Leiters
		String sqlStmt = "INSERT INTO gfos.arbeitsgruppe (ArbeitsgruppenID, Bezeichnung, Leiter) Values ('"+ createArbeitsgruppenID() +"', '" + bezeichnung + "', '" + leiter + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Einfügen konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Arbeitsgruppe wurde erfolgreich verändert.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	/**
	 * Aus dem ResultSet wird eine Arbeitsgruppe erstellt, die zurückgegeben wird.
	 * 
	 */
	private static Arbeitsgruppe createArbeitsgruppeFromQuery(ResultSet rs) throws SQLException {
		Arbeitsgruppe a = new Arbeitsgruppe();
		a.setBezeichnung(rs.getString("Bezeichnung"));
		a.setLeiter(rs.getString("Leiter"));
		a.setArbeitsgruppenID(rs.getString("ArbeitsgruppenID"));
		do {
			a.addMitglied(rs.getString("Personalnummer"));

		} while (rs.next());
		return a;
	}
	
	/**
	 * Es wird von der niedrigsten ArbeitsnummerID ausgegangen geguckt, ob diese belegt ist.
	 * Falls ja, wird die ID um 1 erhöht, wenn nein, wird die ID zurückgegeben.
	 */
	private static String createArbeitsgruppenID() {
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

}
