package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

@Path("logout{sessionID}")
public class LogoutResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(@PathParam("sessionID") String sessionID) {
		sessionID = sessionID.substring(1);
		if(sessionID.split("=").length != 2)
			return JsonHandler.fehler("Falsche Formatierung des Parameters.");
		String auth = sessionID.split("=")[1];
		return SessionHandler.closeSession(auth);		
	}
	
}
