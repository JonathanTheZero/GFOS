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

	private static Arbeitsgruppe createArbeitsgruppeFromQuery(ResultSet rs) throws SQLException {
		Arbeitsgruppe a = new Arbeitsgruppe();
		a.setBezeichnung(rs.getString("Bezeichnung"));
		a.setLeiter(rs.getString("Leiter"));
		do {
			a.addMitglied(rs.getString("Personalnummer"));
		} while (rs.next());
		return a;
	}

}