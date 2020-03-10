package com.nsj.gfos.award.resources;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;

@Path("dummy")
public class DummyResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String testPassword() {
		String sqlStmt = "DELETE FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \"000000000002\";";
		int rs = -1;
		try {
			rs = QueryHandler.update(sqlStmt);
		} catch (SQLException e) {			
			return JsonHandler.fehler(e.toString());
		}
		return JsonHandler.erfolg(Integer.toString(rs));
	}
	
}
