/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.dataWrappers;

import java.util.ArrayList;

public class Arbeitsgruppe {
    
    private ArrayList<Mitarbeiter> mitglieder;
    private Mitarbeiter leiter;
    private String bezeichnung;
    
    public Arbeitsgruppe() {
        
    }

    public ArrayList<Mitarbeiter> getMitglieder() {
        return mitglieder;
    }

    public void setMitglieder(ArrayList<Mitarbeiter> mitglieder) {
        this.mitglieder = mitglieder;
    }

    public Mitarbeiter getLeiter() {
        return leiter;
    }

    public void setLeiter(Mitarbeiter leiter) {
        this.leiter = leiter;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    
    public void addMitglied(Mitarbeiter mitglied) {
        if(mitglied != null)
            mitglieder.add(mitglied);
    }
    
    
}