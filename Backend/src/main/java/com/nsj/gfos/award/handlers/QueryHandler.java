package com.nsj.gfos.award.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;

/**
 * Die Klasse <i>QueryHandler</i> stellt Methoden bereit, um SQL-Statements auf
 * der Datenbank auszuführen author Schnuels
 */
public class QueryHandler {

	/**
	 * Die Methode <i>query</i> führt ein SQL-Statement durch und gibt die
	 * Ergebnisse des SELECT Befehls zurück.
	 * 
	 * @param stmt - SQL-Statement der Query
	 * @return ResultSet - das von der Query zurückgegebene Objekt vom Typ ResultSet
	 * @throws SQLException
	 */
	public static ResultSet query(String stmt) throws SQLException {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {

			myConn = DriverManager.getConnection("jdbc:mysql://192.168.178.45/gfos?useSSL=false", "desktop",
					"gfos2020");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myRs;
	}

	/**
	 * Die Methode <i>update</i> führt ein SQL-Statement mit einem UPDATE, DELETE
	 * oder INSERT Befehl durch.
	 * 
	 * @param stmt - SQL-Statement des Updates
	 * @return int - -1, wenn das Statement nicht ausführbar ist, und 0, wenn bei
	 *         der Durchführung des Updates ein Fehler auftritt und die Aktion nicht
	 *         durchgeführt werden kann
	 * @throws SQLException
	 */
	public static int update(String stmt) throws SQLException {

		Connection myConn = null;
		Statement myStmt = null;
		int myRs = -1;
		try {

			myConn = DriverManager.getConnection("jdbc:mysql://192.168.178.45/gfos?useSSL=false", "desktop",
					"gfos2020");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeUpdate(stmt);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myRs;
	}

	/**
	 * Die Methode <i>createMitarbeiterFromQuery</i> bekommt ein ResultSet und einen
	 * Array von Spalten übergeben, und speichert die Werte aus den angegebenen
	 * Spalten der Datenbank in einem Mitarbeiter Objekt, welches schlussendlich
	 * zurückgegeben wird.
	 * 
	 * @param rs      - ResultSet aus einer Abfrage auf der Mitarbeiter Tabelle der
	 *                Datenbank
	 * @param columns - Ein Array aller Spalten, dessen Werte übernommen werden.
	 * @return Mitarbeiter - Objekt vom Typ Mitarbeiter != null.
	 * @throws SQLException - falls das ResultSet eine Spalte nicht finden kann.
	 */
	public static Mitarbeiter createMitarbeiterFromQuery(ResultSet rs, String[] columns) throws SQLException {
		Mitarbeiter m = new Mitarbeiter();
		for (String column : columns) {
			switch (column) {
				case "Personalnummer":
					m.setPersonalnummer(rs.getString("Personalnummer"));
					break;
				case "Name":
					m.setName(rs.getString("Name"));
					break;
				case "Vorname":
					m.setVorname(rs.getString("Vorname"));
					break;
				case "erreichbar":
					m.setErreichbar((rs.getString("erreichbar").equals("1") ? true : false));
					break;
				case "Arbeitskonto":
					m.setArbeitskonto(Integer.parseInt(rs.getString("Arbeitskonto")));
					break;
				case "EMail":
					m.setEmail(rs.getString("EMail"));
					break;
				case "Status":
					m.setStatus(rs.getString("Status"));
					break;
				case "Rechteklasse":
					m.setRechteklasse(rs.getString("Rechteklasse"));
					break;
				case "Passwort":
					m.setPasswort(rs.getString("Passwort"));
					break;
				case "Abteilung":
					m.setAbteilung(rs.getString("Abteilung"));
					break;
				case "Vertreter":
					m.setVertreter(rs.getString("Vertreter"));
					break;
				case "gda":
					m.setGrundDAbw(rs.getString("gda"));
					break;
			}
		}
		return m;
	}

	/**
	 * Die Methode <i>createArbeitsgruppeFromQuer</i> bekommt ein ResultSet
	 * übergeben und speichert die Werte aus den angegebenen Spalten der Datenbank
	 * in einem Arbeitsgruppen Objekt, welches schlussendlich zurückgegeben wird.
	 * 
	 * @param rs - ResultSet aus einer Abfrage auf der Arbeitsgruppen und
	 *           Arbeitsgruppenteilnahme Tabelle der Datenbank
	 * @return Arbeitsgruppe - Objekt vom Typ Arbeitsgruppe != null.
	 * @throws SQLException - falls das ResultSet eine Spalte nicht finden kann.
	 */
	public static Arbeitsgruppe createArbeitsgruppeFromQuery(ResultSet rs) throws SQLException {
		Arbeitsgruppe a = new Arbeitsgruppe();
		a.setBezeichnung(rs.getString("Bezeichnung"));
		a.setLeiter(rs.getString("Leiter"));
		a.setArbeitsgruppenID(rs.getString("ArbeitsgruppenID"));
		return a;
	}

}
