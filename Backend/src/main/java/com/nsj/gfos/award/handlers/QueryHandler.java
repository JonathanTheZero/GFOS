package com.nsj.gfos.award.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHandler {
    
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
