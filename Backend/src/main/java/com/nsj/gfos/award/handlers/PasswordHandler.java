package com.nsj.gfos.award.handlers;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.DatatypeConverter;

/**
 * Die Klasse <i>PasswordHandler</i> sorgt f�r die Verhashung der Passw�rter.
 * 
 * @author Sophief
 *
 */
public class PasswordHandler {

	/**
	 * Die Methode <i>getHash</i> h�ngt zun�chst einen Salt an das Passwort und
	 * verhasht dieses dann mit Hilfe der importierten Klasse
	 * javax.xml.bind.DatatypeConverter. Als Hashalgorithmus wird SHA-512 benutzt,
	 * der das Passwort in eine Zeichenkette mit 128 Zeichen verhasht.
	 * 
	 * @param password - das zu verhashende Passwort
	 * @return String - das verhashte Passwort
	 */
	public static String getHash(String password) {
		String hashValue = "";
		if (password.equals(""))
			return hashValue;

		password += "GF05@!*6R73L";
		byte[] inputBytes = password.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(inputBytes);
			byte[] digestedBytes = messageDigest.digest();
			hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		} catch (Exception ex) {
		}
		return hashValue;
	}

	/**
	 * Die Methode <i>checkPassword</i> prüft, ob ein gegebenes Passwort mit dem des
	 * gegebenen Mitarbeiters übereinstimmt.
	 * 
	 * @param password - eingegebenes Passwort
	 * @param pn       - Personalnummer des zu überprüfenden Mitarbeiters
	 * @return boolean - true, wenn die Passwörter übereinstimmen, false, wenn nicht
	 */
	public static boolean checkPassword(String password, String pn) {
		String hashed = PasswordHandler.getHash(password);
		String sqlStmt = "SELECT Passwort FROM gfos.mitarbeiter WHERE Personalnummer = \"" + pn + "\";";
		try {
			ResultSet rs = QueryHandler.query(sqlStmt);
			if (!rs.next())
				return false;
			return rs.getString("Passwort").equals(hashed);
		} catch (SQLException e) {
			return false;
		}
	}

}
