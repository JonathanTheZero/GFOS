package com.nsj.gfos.award.handlers;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

/**
 * Die Klasse <i>PasswordHandler</i> sorgt für die Verhashung der Passwörter.
 * 
 * @author Sophief
 *
 */
public class PasswordHandler {

	/**
	 * Die Methode <i>getHash</i> hängt zunächst einen Salt an das Passwort und verhasht dieses dann mit Hilfe der importierten Klasse javax.xml.bind.DatatypeConverter.
	 * Als Hashalgorithmus wird SHA-512 benutzt, der das Passwort in eine Zeichenkette mit 128 Zeichen verhasht.
	 * 
	 * @param password - das zu verhashende Passwort
	 * @return String - das verhashte Passwort
	 */
	public static String getHash(String password) { // Passwort als Parameter eiongeben
		String hashValue = "";
		if (password.equals(""))
			return hashValue; // Falls das Passwort leer ist, gibt es einen leeren Hash-Code

		password += "GF05@!*6R73L"; // Salt dranhÃ¤ngen wegen WÃ¶rterbuch-Attacken
		byte[] inputBytes = password.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(inputBytes);
			byte[] digestedBytes = messageDigest.digest();
			hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		} catch (Exception ex) {
		}
		return hashValue; // falls ein Fehler auftritt, wird ein leerer String zurÃ¼ckgegeben
	}

}
