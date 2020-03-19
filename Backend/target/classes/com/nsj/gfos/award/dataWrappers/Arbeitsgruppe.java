package com.nsj.gfos.award.dataWrappers;

import java.util.ArrayList;

/**
 * Die Klasse <i>Arbeitsgruppe</i> ist lediglich dazu da, die Tabelle 'arbeitsgruppe' der Datenbank in einem 
 * Java Objekt zusammenzufassen.
 * @author Artemis
 */
public class Arbeitsgruppe {
    
    private ArrayList<String> mitglieder;
    private String leiter;
    private String bezeichnung;
    private String arbeitsgruppenID;
    
    public Arbeitsgruppe() {
    	mitglieder = new ArrayList<String>();
    	leiter = "";
    	bezeichnung = "";
    	arbeitsgruppenID = "";
    }

    public ArrayList<String> getMitglieder() {
        return mitglieder;
    }

    public void setMitglieder(ArrayList<String> mitglieder) {
        this.mitglieder = mitglieder;
    }

    public String getLeiter() {
        return leiter;
    }

    public void setLeiter(String leiter) {
        this.leiter = leiter;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    
    public void addMitglied(String mitglied) {
        if(mitglied != null)
            mitglieder.add(mitglied);
    }

	public String getArbeitsgruppenID() {
		return arbeitsgruppenID;
	}

	public void setArbeitsgruppenID(String arbeitsgruppenID) {
		this.arbeitsgruppenID = arbeitsgruppenID;
	}

    
    
}