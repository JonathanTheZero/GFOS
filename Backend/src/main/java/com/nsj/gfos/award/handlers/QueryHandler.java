/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nsj.gfos.award.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHandler {
    
    public static ResultSet query(String stmt, String user, String password) throws SQLException{
        
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            
            myConn = DriverManager.getConnection("jdbc:mysql://192.168.178.45/gfos?useSSL=false", user, password);
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(stmt);         
            
        } catch(Exception e) {
            e.printStackTrace();
        } 
        
        return myRs;
    }
    
}