/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.dataWrappers.Mitarbeiter;
import org.codehaus.jackson.map.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("mitarbeiter")
public class MitarbeiterResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMitarbeiter() {
        ResultSet rs = null;
        String res = "";
        try {
            rs = QueryHandler.query("SELECT * FROM mitarbeiter;", "desktop", "gfos2020");
            ArrayList<Mitarbeiter> allEmpl = new ArrayList<Mitarbeiter>();
            ObjectMapper om = new ObjectMapper();
            while(rs.next()) {
                try {
                    Mitarbeiter m  = createMitarbeiterFromQuery(rs);
                    allEmpl.add(m);
                } catch (Exception e) {res = e.toString();}       
            }
            try {
                res = om.writeValueAsString(allEmpl);
            } catch (Exception e) {res = e.toString();}          
        } catch (Exception e) {res = e.toString();}      
        return res;
    }

    private Mitarbeiter createMitarbeiterFromQuery(ResultSet rs) throws SQLException{
        Mitarbeiter m = new Mitarbeiter();
        m.setPersonalnummer(rs.getString("Personalnummer"));
        m.setName(rs.getString("Name"));
        m.setVorname(rs.getString("Vorname"));        
        m.setErreichbar((rs.getString("erreichbar").equals("1")) ? true : false);
        m.setArbeitskonto(Integer.parseInt(rs.getString("Arbeitskonto")));
        m.setEmail(rs.getString("E-Mail"));
        m.setStatus(rs.getString("Status"));
        m.setRechteklasse(rs.getString("Rechteklasse"));
        m.setAbteilung(rs.getString("Abteilung"));
        m.setVertreter(rs.getString("Vertreter"));
        return m;
    }
    
    private String createJsonFromMitarbeiter(Mitarbeiter m) {
        ObjectMapper om = new ObjectMapper();
        String s;
        try {
            s = om.writeValueAsString(m);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }

}
