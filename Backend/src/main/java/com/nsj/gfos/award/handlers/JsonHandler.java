package com.nsj.gfos.award.handlers;

import org.codehaus.jackson.map.ObjectMapper;

import com.nsj.gfos.award.dataWrappers.Arbeitsgruppe;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;

/**
 * Die Klasse <i>JsonHandler</i> stellt einige Hilfsmethoden bereit, um
 * Nachrichten oder Objekte in Form von Json-Objekten zu verpacken, welche von
 * der Api zurückgegeben werden können.
 * 
 * @author Schnuels
 * @author Sophief
 */
public class JsonHandler {

	/**
	 * Die Methode <i>fehler</i> gibt eine Nachricht als Json-Objekt formatiert
	 * zurück
	 * 
	 * @param msg - Die zu formatierende Nachricht
	 * @return String - {"fehler": "{msg}"}
	 */
	public static String fehler(String msg) {
		return "{\"fehler\": \"" + msg + "\"}";
	}

	/**
	 * Die Methode <i>erfolg</i> gibt eine Nachricht als Json-Objekt formatiert
	 * zurück
	 * 
	 * @param msg - Die zu formatierende Nachricht
	 * @return String - {"erfolg": "{msg}"}
	 */
	public static String erfolg(String msg) {
		return "{\"erfolg\": \"" + msg + "\"}";
	}

	/**
	 * Die Methode <i>embedMitarbeiterInErfolg</i> hängt ein Mitarbeiter Objekt an
	 * eine Erfolgsnachricht an.
	 * 
	 * @param obj - Das Mitarbeiter Objekt
	 * @param msg - Die Nachricht
	 * @return String - {"erfolg": "{msg}", "data": {formatiertes Mitarbeiter
	 *         Objekt}}
	 */
	public static String embedMitarbeiterInErfolg(Mitarbeiter obj, String msg) {
		String erfolg = erfolg(msg);
		erfolg = erfolg.substring(0, erfolg.length() - 1);
		String objJson = createJsonFromMitarbeiter(obj);
		return erfolg + ", \"data\": " + objJson + "}";
	}

	/**
	 * Die Methode <i>createJsonFromMitarbeiter</i> formatiert ein Mitarbeiter
	 * Objekt als ein Json-Objekt und gibt dieses zurück.
	 * 
	 * @param m - Das Mitarbeiter Objekt
	 * @return String - das formatierte Objekt
	 */
	public static String createJsonFromMitarbeiter(Mitarbeiter m) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(m);
		} catch (Exception e) {
			return fehler(e.toString());
		}
	}

	/**
	 * Die Methode <i>createJsonFromArbeitsgruppe</i> formatiert ein Arbeitsgruppen
	 * Objekt als ein Json-Objekt und gibt dieses zurück.
	 * 
	 * @param m - Das Arbeitsgruppen Objekt
	 * @return String - das formatierte Objekt
	 */
	public static String createJsonFromArbeitsgruppe(Arbeitsgruppe a) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(a);
		} catch (Exception e) {
			return fehler(e.toString());
		}
	}

}
