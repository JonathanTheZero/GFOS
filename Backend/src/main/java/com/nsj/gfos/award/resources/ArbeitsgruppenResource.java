package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.SessionHandler;
import com.nsj.gfos.award.gUtils.Utils;



import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("arbeitsgruppen")
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
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, gfos.arbeitsgruppe.Bezeichnung, gfos.arbeitsgruppe.Leiter, gfos.arbeitsgruppenteilnahme.Mitarbeiter "
				+ "FROM gfos.arbeitsgruppenteilnahme JOIN gfos.arbeitsgruppe"
				+ "         ON gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = gfos.arbeitsgruppe.ArbeitsgruppenID "
				+ "WHERE (SELECT gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.Mitarbeiter = \"" + pn + "\")"
						+ "= gfos.arbeitsgruppe.ArbeitsgruppenID;";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Arbeitsgruppe a = Utils.createArbeitsgruppeFromQuery(rs);
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
			return JsonHandler.erfolg("Arbeitsgruppe wurde erfolgreich eingefügt.");
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
		if (Utils.checkIfArbeitsgruppeExistsFromBezeichnung(bezeichnung))
			return JsonHandler.fehler("Die Bezeichnung der Arbeitsgruppe ist bereits vergeben.");
		if(!Utils.checkIfMitarbeiterExists(leiter))
			return JsonHandler.fehler("Der Mitarbeiter, der Leiter werden soll, existiert nicht.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		// TODO checkRights + die des Leiters
		String arbeitsgruppenID = Utils.createArbeitsgruppenID();
		String sqlStmt = "INSERT INTO gfos.arbeitsgruppe (ArbeitsgruppenID, Bezeichnung, Leiter) Values ('"+ arbeitsgruppenID +"', '" + bezeichnung + "', '" + leiter + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Einfügen der Arbeitsgruppe konnte nicht durchgeführt werden.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
		sqlStmt = "INSERT INTO gfos.arbeitsgruppenteilnahme (ArbeitsgruppenID, Mitarbeiter) Values ('"+ arbeitsgruppenID +"', '" + leiter + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Einfügen des Leiters konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Arbeitsgruppe wurde erfolgreich erstellt.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	/**
	 * Als Parameter werden die SessionID und die ArbeitsgruppenID übergeben.
	 * Wenn die SessionID gültig ist und der Mitarbeiter die Berechtigung hat, werden erst die Mitarbeiter aus der Arbeitsgruppe, dann die Arbeitsgruppe selber gelöscht.
	 */
	@GET
	@Path("remove{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String removeArbeitsgruppe(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 2)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		String id = attributes[1].split("=")[1];
		if (id.length() != 12)
			return JsonHandler.fehler("ID ist ungültig");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if(!Utils.checkIfArbeitsgruppeExistsFromID(id))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		// TODO checkRights
		String sqlStmt = "DELETE FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \"" + id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Löschen konnte nicht durchgeführt werden.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
		sqlStmt = "DELETE FROM gfos.arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \"" + id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Die Gruppe konnte nicht gelöscht werden.");
			return JsonHandler.erfolg("Arbeitsgruppe wurde erfolgreich gelöscht.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	
	/**
	 * Übergeben werden die SessionID, die Personalnummer des zu löschenden Mitarbeiters und die Arbeitsgruppe, aus der der Mitarbeiter gelöscht werden soll.
	 * Die Parameter werden auf ihre Richtigkeit geprüft.
	 * Es wird geprüft, ob der Mitarbeiter, der gelöscht werden soll, der Leiter der Arbeitsgruppe ist.
	 * Wenn ja, wird das Löschen abgebrochen.
	 * Wenn nein, wird der Mitarbeiter aus der Arbeitsgruppe gelöscht.
	 */
	@GET
	@Path("removeMitarbeiter{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String removeMitarbeiter(@PathParam("attributes") String query) {
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
		if(!Utils.isInArbeitsgruppe(id, pn))
			return JsonHandler.fehler("Der Mitarbeiter befindet sich nicht in der Arbeitsgruppe.");
		// TODO checkRights
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.Leiter FROM gfos.Arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \"" + id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(rs == null)
				return JsonHandler.fehler("Der Leiter der Arbeitsgruppe konnte nicht gefunden werden.");			
			rs.next();
			if(id.equals(rs.getString("Leiter")))
				return JsonHandler.fehler("Der Leiter einer Arbeitsgruppe kann nicht gelöscht werden.");	
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
		sqlStmt = "DELETE FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \"" + id + "\" AND gfos.arbeitsgruppenteilnahme.Mitarbeiter = \"" + pn + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Löschen konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Mitarbeiter wurde erfolgreich aus der Arbeitsgruppe gelöscht.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}
	
	@GET
	@Path("addMitarbeiter{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String addMitarbeiter(@PathParam("attributes") String query) {
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
		String arbeitsgruppenID = attributes[2].split("=")[1];
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if (!Utils.checkIfArbeitsgruppeExistsFromID(arbeitsgruppenID))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		if(!Utils.checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Der Mitarbeiter existiert nicht.");
		if(Utils.isInArbeitsgruppe(arbeitsgruppenID, pn))
			return JsonHandler.fehler("Der Mitarbeiter ist bereits in der Arbeitsgruppe vorhanden.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		// TODO checkRights
		String sqlStmt = "INSERT INTO gfos.arbeitsgruppenteilnahme (ArbeitsgruppenID, Mitarbeiter) Values ('"+ arbeitsgruppenID +"', '" + pn + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Das Einfügen des Mitarbeitsers konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Der Mitarbeiter wurde in die Arbeitsgruppe eingefügt.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

}