package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.gUtils.Utils;
import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

/**
 * Die Klasse LoginResource ist eine Resource der Api und wird über den Pfad /api/login angesprochen. Sie ist dafür da, einen
 * Client an den Webservice anzumelden.
 * @author Schnuels
 */
@Path("login{params}")
public class LoginResource {

	/**
	 * Die Methode <i>login</i> wird bei einer Anfrage an die Resource aufgerufen und prüft verschiedenste Fehler und die
	 * Richtigkeit der Anmeldedaten, bevor sie über den <i>SessionHandler</i> eine neue Session für den Mitarbeiter erstellt.
	 * @param param - Teil der URL nach 'login' also zum Beispiel ':auth=123456789012&em=e@e.de&pw=1234'
	 * @return String - über den <i>JsonHandler</i> formatierter Fehler oder die Rückgabe von <i>SessionHandler.createSession(...)</i>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@PathParam("params") String param) {
		param = param.substring(1);
		String[] params = param.split("&");	
		if(params.length != 3) 
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");			
		String[] auth = params[0].split("=");
		String[] em = params[1].split("=");
		String[] pw = params[2].split("=");		
		if(auth.length != 2 || em.length != 2 || pw.length != 2) 
			return JsonHandler.fehler("Falsche Parameter Syntax.");		
		if(auth[1].length() != 12) 
			return JsonHandler.fehler("Parameter sind falsch formatiert.");
		String pn = Utils.getPersonalnummerFromEmail(em[1]);
		if(!Utils.checkPassword(pw[1], pn)) 
			return JsonHandler.fehler("Passwort oder Email falsch oder Benutzer existiert nicht.");
		if(Utils.checkIfUserIsConnected(pn))
			return JsonHandler.fehler("Dieser Benutzer ist bereits angemeldet.");
		if(!SessionHandler.changeStatus(pn, "Online"))
			return JsonHandler.fehler("Status konnte aufgrund eines Fehlers nicht geändert werden.");
		if(!SessionHandler.changeErreichbar(pn, 1))
			return JsonHandler.fehler("Erreichbarkeit konnte aufgrund eines Fehlers nicht geändert werden.");
		return SessionHandler.createSession(new String[]{auth[1], pn});
	}	
				
}
