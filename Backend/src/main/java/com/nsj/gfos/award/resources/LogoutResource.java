package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.gUtils.Utils;
import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.SessionHandler;

/**
 * Die Klasse LogoutResource ist eine Resource der Api und wird über den Pfad
 * /api/logout angesprochen. Sie ist dafür da, einen Client von dem Webservice
 * abzumelden.
 * 
 * @author Schnuels
 */
@Path("logout{sessionID}")
public class LogoutResource {

	/**
	 * Die Methode <i>logout</i> wird bei einer Anfrage an diese Resource aufgerufen
	 * und verarbeitet die als Parameter gegebene SessionID und gibt diese an den
	 * <i>SessionHandler</i> weiter.
	 * 
	 * @param sessionID - Teil der URL nach 'logout' also zum Beispiel
	 *                  ':auth=123456789012'
	 * @return String - Fehler bei falscher Formatierung des Parameters oder die
	 *         Rückgabe von <i>SessionHandler.closeSession(...)</i>
	 */
	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(@PathParam("sessionID") String sessionID) {
		sessionID = sessionID.substring(1);
		if (sessionID.split("=").length != 2)
			return JsonHandler.fehler("Falsche Formatierung des Parameters.");
		String auth = sessionID.split("=")[1];
		if (!SessionHandler.changeStatus(Utils.getPersonalnummerFromSessionID(auth), "Offline"))
			return JsonHandler.fehler("Status konnte aufgrund eines Fehlers nicht geändert werden.");
		if (!SessionHandler.changeErreichbar(Utils.getPersonalnummerFromSessionID(auth), 0))
			return JsonHandler.fehler("Erreichbarkeit konnte aufgrund eines Fehlers nicht geändert werden.");
		return SessionHandler.closeSession(auth);
	}

	/**
	 * Die Methode <i>logoutPost</i> wird bei einer POST Anfrage an diese Resource aufgerufen
	 * und verarbeitet die als Parameter gegebene SessionID und gibt diese an den
	 * <i>SessionHandler</i> weiter.
	 * 
	 * @param sessionID - Teil der URL nach 'logout' also zum Beispiel
	 *                  ':auth=123456789012'
	 * @return String - Fehler bei falscher Formatierung des Parameters oder die
	 *         Rückgabe von <i>SessionHandler.closeSession(...)</i>
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String logoutPost(@PathParam("sessionID") String sessionID) {
		sessionID = sessionID.substring(1);
		if (sessionID.split("=").length != 2)
			return JsonHandler.fehler("Falsche Formatierung des Parameters.");
		String auth = sessionID.split("=")[1];
		if (!SessionHandler.changeStatus(Utils.getPersonalnummerFromSessionID(auth), "Offline"))
			return JsonHandler.fehler("Status konnte aufgrund eines Fehlers nicht geändert werden.");
		if (!SessionHandler.changeErreichbar(Utils.getPersonalnummerFromSessionID(auth), 0))
			return JsonHandler.fehler("Erreichbarkeit konnte aufgrund eines Fehlers nicht geändert werden.");
		return SessionHandler.closeSession(auth);
	}

}
