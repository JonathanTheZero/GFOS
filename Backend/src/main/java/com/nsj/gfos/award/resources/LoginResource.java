/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.resources;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.PasswordHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

/**
 *
 * @author LoL
 */
@Path("login{params}")
public class LoginResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@PathParam("params") String param) {
		param = param.substring(1);
		String[] params = param.split("&");	
		if(params.length != 3) 
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");			
		String[] auth = params[0].split("=");
		String[] pn = params[1].split("=");
		String[] pw = params[2].split("=");		
		if(auth.length != 2 || pn.length != 2 || pw.length != 2) 
			return JsonHandler.fehler("Falsche Parameter Syntax.");		
		if(auth[1].length() != 12 || pn[1].length() != 12) 
			return JsonHandler.fehler("Parameter sind falsch formatiert.");
		if(checkIfUserIsConnected(pn[1]))
			return JsonHandler.fehler("Dieser Benutzer ist bereits angemeldet.");
		if(!checkPassword(pw[1], pn[1])) 
			return JsonHandler.fehler("Passwort oder Benutzername falsch oder Benutzer existiert nicht.");
		return SessionHandler.createSession(new String[]{auth[1], pn[1]});
	}	
	
	private boolean checkPassword(String password, String pn) {
		String hashed = PasswordHandler.getHash(password);
		String sqlStmt = "SELECT Passwort FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if(!rs.next())
				return false;
			return rs.getString("Passwort").equals(hashed);
		} catch (SQLException e) {
			return false;
		}
	}
	
	private boolean checkIfUserIsConnected(String pn) {
		String sqlStmt = "SELECT * FROM gfos.active_sessions WHERE Mitarbeiter = \"" + pn + "\";";	
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}
	
}
