package com.nsj.gfos.award.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Die Klasse <i>QueryHandler</i> stellt Methoden bereit, um SQL-Statements auf der Datenbank auszuführen 
 * author Schnuels
 */
public class QueryHandler {
    
    /**
     * Die Methode <i>query</i> führt ein SQL-Statement durch und gibt die Ergebnisse des SELECT Befehls zurück.
     * @param stmt - SQL-Statement der Query
     * @return ResultSet - das von der Query zurückgegebene Objekt vom Typ ResultSet
     * @throws SQLException
     */
    public static ResultSet query(String stmt) throws SQLException{
        
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            
            myConn = DriverManager.getConnection("jdbc:mysql://192.168.178.45/gfos?useSSL=false", "desktop", "gfos2020");            
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(stmt);               
        } catch(Exception e) {
            e.printStackTrace();
        } 
        return myRs;
    }
    
    /**
     * Die Methode <i>update</i> führt ein SQL-Statement mit einem UPDATE, DELETE oder INSERT Befehl durch.
     * @param stmt - SQL-Statement des Updates
     * @return int - -1, wenn das Statement nicht ausführbar ist, und 0, wenn bei der Durchführung des Updates
     *               ein Fehler auftritt und die Aktion nicht durchgeführt werden kann
     * @throws SQLException
     */
    public static int update(String stmt) throws SQLException{
        
        Connection myConn = null;
        Statement myStmt = null;
        int myRs = -1;
        try {
            
            myConn = DriverManager.getConnection("jdbc:mysql://192.168.178.45/gfos?useSSL=false", "desktop", "gfos2020");           
            myStmt = myConn.createStatement();
            myRs = myStmt.executeUpdate(stmt); 
            
        } catch(Exception e) {
            e.printStackTrace();
        }        
        return myRs;
    }
    
}
