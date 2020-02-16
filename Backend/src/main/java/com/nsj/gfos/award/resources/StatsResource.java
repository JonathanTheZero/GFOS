package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;

@Path("stats")
public class StatsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMitarbeiterCount() {
		String stmt = "SELECT COUNT(*) AS Anzahl_Mitarbeiter FROM gfos.mitarbeiter";
		ResultSet rs;
		String count = "";
		try {
			 rs = QueryHandler.query(stmt);
			if(!rs.next())
				return JsonHandler.fehler("Es ist ein Fehler in der Datenbank aufgetreten");
			count = rs.getString("Anzahl_Mitarbeiter");
		} catch (SQLException e) {
			JsonHandler.fehler("Es ist ein Fehler in der Datenbank aufgetreten");
		}
		return JsonHandler.erfolg("Es wurden " + count + " Mitarbeiter gefunden.");
	}
	
}
