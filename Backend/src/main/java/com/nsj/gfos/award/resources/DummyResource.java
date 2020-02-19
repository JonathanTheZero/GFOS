package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.PasswordHandler;

@Path("dummy")
public class DummyResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String testPassword() {
		return JsonHandler.erfolg(PasswordHandler.getHash("root"));
	}
	
}
