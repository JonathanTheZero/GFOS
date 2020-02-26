/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.dataWrappers;

import java.util.ArrayList;

public class Arbeitsgruppe {
    
    private ArrayList<String> mitglieder;
    private String leiter;
    private String bezeichnung;
    
    public Arbeitsgruppe() {
        
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
    
    
}