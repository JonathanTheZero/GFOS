package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;

@Path("stats")
public class StatsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMitarbeiterCount() {
		String stmt = "SELECT COUNT(*) AS Anzahl_Mitarbeiter FROM gfos.mitarbeiter";
		try {
			ResultSet rs = QueryHandler.query(stmt);
			if(!rs.next())
				return JsonHandler.fehler("Es ist ein Fehler in der Datenbank aufgetreten");
		} catch (SQLException e) {
			JsonHandler.fehler("Es ist ein Fehler in der Datenbank aufgetreten");
		}
		return JsonHandler.erfolg("Es wurden " + rs.getString("Anzahl_Mitarbeiter") + " Mitarbeiter gefunden.");
	}
	
}
