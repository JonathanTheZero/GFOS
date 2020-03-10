package com.nsj.gfos.award.handlers;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class PasswordHandler {

	public static String getHash(String password){    //Passwort als Parameter eiongeben
        String hashValue = "";
        if(password.equals("")) return hashValue;    //Falls das Passwort leer ist, gibt es einen leeren Hash-Code 

        password += "GF05@!*6R73L";    //Salt dranhängen wegen Wörterbuch-Attacken
        byte[] inputBytes = password.getBytes();
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        }catch(Exception ex){}
        return hashValue;    //falls ein Fehler auftritt, wird ein leerer String zurückgegeben
    }
	
}
