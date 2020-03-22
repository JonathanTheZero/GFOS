package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.RightHandler;
import com.nsj.gfos.award.handlers.SessionHandler;
import com.nsj.gfos.award.gUtils.Utils;

import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Die Klasse <i>ArbeitsgruppenResource</i> ist eine Resource der Api und wird
 * über den Pfad /api/arbeitsgruppen angesprochen. Sie ist dafür da, die
 * Anfragen des Clients bezüglich der Arbeitsgruppen zu verwalten.
 * 
 * @author Artemis
 */
@Path("arbeitsgruppen")
public class ArbeitsgruppenResource {

	/**
	 * Die Methode <i>getAllArbeitsgruppen</i> gibt alle bestehenden Arbeitsgruppen
	 * zurück.
	 * 
	 * @param auth - SessionID des anfordernden Mitarbeiters
	 * @return String - alle Arbeitsgruppen als Json
	 */
	@GET
	@Path("getAll{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllArbeitsgruppen(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 1)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!RightHandler.checkPermission(auth, "getAllArbeitsgruppen"))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, gfos.arbeitsgruppe.Bezeichnung, gfos.arbeitsgruppe.Leiter FROM gfos.arbeitsgruppe;";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			ArrayList<Arbeitsgruppe> arbeitsgruppen = new ArrayList<Arbeitsgruppe>();
			ObjectMapper om = new ObjectMapper();
			do {
				Arbeitsgruppe a = new Arbeitsgruppe();
				a.setBezeichnung(rs.getString("Bezeichnung"));
				a.setLeiter(rs.getString("Leiter"));
				a.setArbeitsgruppenID(rs.getString("ArbeitsgruppenID"));
				String sql = "SELECT gfos.arbeitsgruppenteilnahme.Mitarbeiter FROM gfos.arbeitsgruppenteilnahme WHERE \""
						+ a.getArbeitsgruppenID() + "\" = gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID;";
				try {
					ResultSet mitarbeiter = QueryHandler.query(sql);
					while (mitarbeiter.next()) {
						a.addMitglied(mitarbeiter.getString("Mitarbeiter"));
					}
				} catch (SQLException e) {
					return JsonHandler.fehler(e.toString());
				}
				arbeitsgruppen.add(a);
			} while (rs.next());
			try {
				return om.writeValueAsString(arbeitsgruppen);
			} catch (Exception e) {
				return JsonHandler.fehler(e.toString());
			}
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>getArbeitsgruppenFromMitarbeiter</i> gibt die Arbeitsgruppen
	 * des Mitarbeiters, dem die Personalnummer gehört, aus der Datenbank zurück,
	 * falls diese existieren.
	 * 
	 * @param auth - SessionID des anfordernden Mitarbeiters
	 * @param pn   - Personalnummer des Mitarbeiters
	 * @return String - Arbeitsgruppen des Mitarbeiters als Json
	 */
	@GET
	@Path("getAllFromMitarbeiter{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArbeitsgruppenFromMitarbeiter(@PathParam("attributes") String query) {
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
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!RightHandler.checkPermission(auth, "getArbeitsgruppe") && Utils.checkSelfOperation(auth, pn))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, gfos.arbeitsgruppe.Bezeichnung, gfos.arbeitsgruppe.Leiter FROM gfos.arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID IN (SELECT gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.Mitarbeiter = \""
				+ pn + "\") ORDER BY gfos.arbeitsgruppe.ArbeitsgruppenID ASC;";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			ArrayList<Arbeitsgruppe> arbeitsgruppen = new ArrayList<Arbeitsgruppe>();
			ObjectMapper om = new ObjectMapper();
			do {
				Arbeitsgruppe a = new Arbeitsgruppe();
				a.setBezeichnung(rs.getString("Bezeichnung"));
				a.setLeiter(rs.getString("Leiter"));
				a.setArbeitsgruppenID(rs.getString("ArbeitsgruppenID"));
				String sql = "SELECT gfos.arbeitsgruppenteilnahme.Mitarbeiter FROM gfos.arbeitsgruppenteilnahme WHERE \""
						+ a.getArbeitsgruppenID() + "\" = gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID;";
				try {
					ResultSet mitarbeiter = QueryHandler.query(sql);
					while (mitarbeiter.next()) {
						a.addMitglied(mitarbeiter.getString("Mitarbeiter"));
					}
				} catch (SQLException e) {
					return JsonHandler.fehler(e.toString());
				}
				arbeitsgruppen.add(a);
			} while (rs.next());
			try {
				return om.writeValueAsString(arbeitsgruppen);
			} catch (Exception e) {
				return JsonHandler.fehler(e.toString());
			}
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>alterLeiter</i> verändert den Leiter der Arbeitsgruppe.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @param pn   - Personalnummer des Mitarbeiters, der Leiter der Arbeitsgruppe
	 *             werden soll
	 * @param id   - ArbeitsgruppenID von der Abreitsgruppe, die den Mitarbeiter als
	 *             Leiter haben soll
	 * @return String - Erfolg oder Fehler als Rückgabe
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
		if (id.length() != 12)
			return JsonHandler.fehler("Ungültige ArbeitsgruppenID.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!Utils.isInArbeitsgruppe(id, pn))
			return JsonHandler.fehler("Der Leiter muss bereits in der Arbeitsgruppe sein.");
		if (Utils.getLeiter(id).equals(pn))
			return JsonHandler.fehler("Der Mitarbeiter ist bereits der Leiter der Arbeitsgruppe.");
		if (!RightHandler.checkPermissionFromPn(pn, "becomeLeiter"))
			return JsonHandler.fehler("Der angegebene Mitarbeiter hat keine Berechtigung Leiter zu werden.");
		if (!((Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				&& Utils.getLeiter(id).equals(Utils.getPersonalnummerFromSessionID(auth)))
				|| RightHandler.checkPermission(auth, "alterLeiter")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "UPDATE gfos.arbeitsgruppe SET gfos.arbeitsgruppe.Leiter = \"" + pn
				+ "\" WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \"" + id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Die Veränderung konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Der Leiter wurde erfolgreich geändert.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>addArbeitsgruppe</i> erstellt eine neue Arbeitsgruppe anhand
	 * der Bezeichnung und des Leiters und weist ihr eine ArbeitsgruppenID zu.
	 * 
	 * @param auth        - SessionID des ausführenden Mitarbeiters
	 * @param bezeichnung - Bezeichnung für neue Arbeitsgruppe
	 * @param pn          - Personalnummer des Mitarbeiters, der Leiter werden soll
	 * @return String - neu erstellte Arbeitsgruppe oder Fehler als Rückgabe
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
		if (!Utils.checkIfMitarbeiterExists(leiter))
			return JsonHandler.fehler("Der Mitarbeiter, der Leiter werden soll, existiert nicht.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!RightHandler.checkPermission(auth, "addArbeitsgruppe"))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		if (!RightHandler.checkPermissionFromPn(leiter, "becomeLeiter"))
			return JsonHandler.fehler("Der angegebene Leiter hat keine Berechtigung Leiter zu werden.");
		String arbeitsgruppenID = Utils.createArbeitsgruppenID();
		String sqlStmt = "INSERT INTO gfos.arbeitsgruppe (ArbeitsgruppenID, Bezeichnung, Leiter) Values ('"
				+ arbeitsgruppenID + "', '" + bezeichnung + "', '" + leiter + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Das Einfügen der Arbeitsgruppe konnte nicht durchgeführt werden.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
		sqlStmt = "INSERT INTO gfos.arbeitsgruppenteilnahme (ArbeitsgruppenID, Mitarbeiter) Values ('"
				+ arbeitsgruppenID + "', '" + leiter + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Das Einfügen des Leiters konnte nicht durchgeführt werden.");
			Arbeitsgruppe a = new Arbeitsgruppe();
			a.setBezeichnung(bezeichnung);
			a.setArbeitsgruppenID(arbeitsgruppenID);
			a.setLeiter(leiter);
			a.addMitglied(leiter);
			return JsonHandler.createJsonFromArbeitsgruppe(a);
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>removeArbeitsgruppe</i> löscht eine Arbeitsgruppe anhand ihrer
	 * ArbeitsgruppenID.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @param id   - ArbeitsgruppenID von der Arbeitsgruppe, die gelöscht werden
	 *             soll
	 * @return String - Erfolg oder Fehler als Rückgabe
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
		if (!Utils.checkIfArbeitsgruppeExistsFromID(id))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		if (!((Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				&& Utils.getLeiter(id).equals(Utils.getPersonalnummerFromSessionID(auth)))
				|| RightHandler.checkPermission(auth, "removeArbeitsgruppe")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "DELETE FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \""
				+ id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Das Löschen konnte nicht durchgeführt werden.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
		sqlStmt = "DELETE FROM gfos.arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \"" + id + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Die Gruppe konnte nicht gelöscht werden.");
			return JsonHandler.erfolg("Arbeitsgruppe wurde erfolgreich gelöscht.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>removeMitarbeiter</i> löscht einen Mitarbeiter aus einer
	 * Arbeitsgruppe.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @param pn   - Personalnummer des Mitarbeiters, der aus der Arbeitsgruppe
	 *             gelöscht werden soll
	 * @param id   - ArbeitsgruppenID der Arbeitsgruppe, aus der der Mitarbeiter
	 *             gelöscht werden soll
	 * @return String - Erfolg oder Fehler als Rückgabe
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
		if (id.length() != 12)
			return JsonHandler.fehler("Ungültige ArbeitsgruppenID.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!Utils.isInArbeitsgruppe(id, pn))
			return JsonHandler.fehler("Der Mitarbeiter befindet sich nicht in der Arbeitsgruppe.");
		if (Utils.getLeiter(id).equals(pn))
			return JsonHandler.fehler("Der Leiter der Arbeitsgruppe kann nicht entfernt werden.");
		if (!((Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				&& Utils.getLeiter(id).equals(Utils.getPersonalnummerFromSessionID(auth)))
				|| RightHandler.checkPermission(auth, "removeMitarbeiterFromArbeitsgruppe")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "DELETE FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \""
				+ id + "\" AND gfos.arbeitsgruppenteilnahme.Mitarbeiter = \"" + pn + "\";";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Das Löschen konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Mitarbeiter wurde erfolgreich aus der Arbeitsgruppe gelöscht.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>addMitarbeiter</i> fügt einen Mitarbeiter in eine
	 * Arbeitsgruppe ein.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @param pn   - Personalnummer des Mitarbeiters, der in die Arbeitsgruppe
	 *             eingefügt werden soll
	 * @param id   - ArbeitsgruppenID der Arbeitsgruppe, in die der Mitarbeiter
	 *             eingefügt werden soll
	 * @return String - Erfolg oder Fehler als Rückgabe
	 */
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
		String id = attributes[2].split("=")[1];
		if (pn.length() != 12)
			return JsonHandler.fehler("Ungültige Personalnummer.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!Utils.checkIfArbeitsgruppeExistsFromID(id))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		if (!Utils.checkIfMitarbeiterExists(pn))
			return JsonHandler.fehler("Der Mitarbeiter existiert nicht.");
		if (Utils.isInArbeitsgruppe(id, pn))
			return JsonHandler.fehler("Der Mitarbeiter ist bereits in der Arbeitsgruppe vorhanden.");
		if (!((Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				&& Utils.getLeiter(id).equals(Utils.getPersonalnummerFromSessionID(auth)))
				|| RightHandler.checkPermission(auth, "addMitarbeiterToArbeitsgruppe")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		if (!RightHandler.checkPermissionFromPn(pn, "getAddedToArbeitsgruppe"))
			return JsonHandler.fehler("Der Mitarbeiter darf nicht in die Arbeitsgruppe hinzugefügt werden.");
		String sqlStmt = "INSERT INTO gfos.arbeitsgruppenteilnahme (ArbeitsgruppenID, Mitarbeiter) Values ('" + id
				+ "', '" + pn + "');";
		try {
			int rs = QueryHandler.update(sqlStmt);
			if (rs == 0)
				return JsonHandler.fehler("Das Einfügen des Mitarbeitsers konnte nicht durchgeführt werden.");
			return JsonHandler.erfolg("Der Mitarbeiter wurde in die Arbeitsgruppe eingefügt.");
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>getArbeitsgruppeFromID</i> gibt die Arbeitsgruppe mit Hilfe
	 * ihrer ID zurück.
	 * 
	 * @param auth - SessionID des ausführenden Mitarbeiters
	 * @param id   - ArbeitsgruppenID von der Abreitsgruppe, die man haben möchte
	 * @return String - Arbeitsgruppe als Json
	 */
	@GET
	@Path("getFromID{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArbeitsgruppeFromID(@PathParam("attributes") String query) {
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
		if (!Utils.checkIfArbeitsgruppeExistsFromID(id))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		if (!(Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				|| RightHandler.checkPermission(auth, "getArbeitsgruppe")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, gfos.arbeitsgruppe.Bezeichnung, gfos.arbeitsgruppe.Leiter FROM gfos.arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \""
				+ id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Arbeitsgruppe a = QueryHandler.createArbeitsgruppeFromQuery(rs);
			sqlStmt = "SELECT mitarbeiter FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \""
					+ id + "\";";
			rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			do {
				a.addMitglied(rs.getString("Mitarbeiter"));
			} while (rs.next());
			return JsonHandler.createJsonFromArbeitsgruppe(a);
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>getArbeitsgruppeFromBezeichnung</i> gibt die Arbeitsgruppe mit
	 * Hilfe ihrer Bezeichnung zurück.
	 * 
	 * @param auth        - SessionID des ausführenden Mitarbeiters
	 * @param bezeichnung - Bezeichnung von der Abreitsgruppe, die man haben möchte
	 * @return String - Arbeitsgruppe als Json
	 */
	@GET
	@Path("getFromBezeichnung{attributes}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArbeitsgruppeFromBezeichnung(@PathParam("attributes") String query) {
		query = query.substring(1);
		String[] attributes = query.split("&");
		if (attributes.length != 2)
			return JsonHandler.fehler("Falsche Anzahl an Parametern.");
		for (String attribute : attributes) {
			if (attribute.split("=").length != 2)
				return JsonHandler.fehler("Parameter sind falsch formatiert.");
		}
		String auth = attributes[0].split("=")[1];
		String bezeichnung = attributes[1].split("=")[1];
		if (bezeichnung.equals(""))
			return JsonHandler.fehler("Bezeichnung ist leer.");
		if (!SessionHandler.checkSessionID(auth))
			return JsonHandler.fehler("SessionID ist ungültig.");
		if (!Utils.checkIfArbeitsgruppeExistsFromBezeichnung(bezeichnung))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		if (Utils.getArbeitsgruppenIDFromBezeichnung(bezeichnung).equals(""))
			return JsonHandler.fehler("Arbeitsgruppe existiert nicht.");
		String id = Utils.getArbeitsgruppenIDFromBezeichnung(bezeichnung);
		if (!(Utils.isInArbeitsgruppe(id, Utils.getPersonalnummerFromSessionID(auth))
				|| RightHandler.checkPermission(auth, "getArbeitsgruppe")))
			return JsonHandler.fehler("Der Mitarbeiter hat keine Berechtigung.");
		String sqlStmt = "SELECT gfos.arbeitsgruppe.ArbeitsgruppenID, gfos.arbeitsgruppe.Bezeichnung, gfos.arbeitsgruppe.Leiter FROM gfos.arbeitsgruppe WHERE gfos.arbeitsgruppe.ArbeitsgruppenID = \""
				+ id + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			Arbeitsgruppe a = QueryHandler.createArbeitsgruppeFromQuery(rs);
			sqlStmt = "SELECT mitarbeiter FROM gfos.arbeitsgruppenteilnahme WHERE gfos.arbeitsgruppenteilnahme.ArbeitsgruppenID = \""
					+ id + "\";";
			rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return JsonHandler.fehler("Leere Rückgabe der Datenbank.");
			do {
				a.addMitglied(rs.getString("Mitarbeiter"));
			} while (rs.next());
			return JsonHandler.createJsonFromArbeitsgruppe(a);
		} catch (SQLException e) {
			return JsonHandler.fehler(e.toString());
		}
	}

}
