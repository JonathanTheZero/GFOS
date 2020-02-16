/*
d * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nsj.gfos.award.handlers.JsonHandler;
import com.nsj.gfos.award.handlers.PasswordHandler;
import com.nsj.gfos.award.handlers.QueryHandler;
import com.nsj.gfos.award.handlers.SessionHandler;
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
            rs = QueryHandler.query("SELECT * FROM mitarbeiter;");
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add{attributes}")
    public String addMitarbeiter(@PathParam("attributes") String query) {
    	query = query.substring(1);
    	String[] attributes = query.split("&");
    	if(attributes.length != 13)
    		return JsonHandler.fehler("Falsche Anzahl an Parametern.");
    	for(String attribute: attributes) {
    		if(attribute.split("=").length != 2)
    			return JsonHandler.fehler("Parameter sind falsch formatiert.");
    	}
    	String auth = attributes[0].split("=")[1];
    	if(!SessionHandler.checkSessionID(auth))
    		return JsonHandler.fehler("SessionID ist ungültig.");
    	String pn = "\"" + attributes[1].split("=")[1] + "\"";
    	String name = "\"" + attributes[2].split("=")[1] + "\"";
    	String vorname = "\"" + attributes[3].split("=")[1] + "\"";
    	String erreichbar = attributes[4].split("=")[1];
    	String arbeitskonto = attributes[5].split("=")[1];
    	String email = "\"" + attributes[6].split("=")[1] + "\"";
    	String passwort = "\"" + PasswordHandler.getHash(attributes[7].split("=")[1]) + "\"";
    	String status = "\"" + attributes[8].split("=")[1] + "\"";
    	String gda = "\"" + attributes[9].split("=")[1] + "\"";
    	String rechteklasse = "\"" + attributes[10].split("=")[1] + "\"";
    	String abteilung = "\"" + attributes[11].split("=")[1] + "\"";
    	String vertreter = "\"" + attributes[12].split("=")[1] + "\"";
    	String sqlStmt = "INSERT INTO gfos.mitarbeiter VALUES("+pn+","+name+","+vorname+","+erreichbar+","+arbeitskonto+","+email+","+passwort+","+status+","+gda+","+rechteklasse+","+abteilung+","+vertreter+");";
    	try {
    		int rs = QueryHandler.update(sqlStmt);
			if(rs == 0)
				return JsonHandler.fehler("Fehler!");
			return JsonHandler.erfolg("Mitarbeiter wurde erfolgreich hinzugefügt.");
		} catch (SQLException e) {return JsonHandler.fehler(e.toString());}
    }
    //http://localhost:8080/award/api/mitarbeiter/add:auth=123456789012&pn=000000000001&n=Sommerfeld&vn=Nils&er=0&ak=20&em=n.s@e.de&pw=1234&s=Abwesend&gda=Feierabend&rk=Admin&ab=IT-Sicherheit&ve=000000000000
    
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
        m.setPasswort(rs.getString("Passwort"));
        m.setGrundDAbw(rs.getString("gda"));
        return m;
    }
       
}
