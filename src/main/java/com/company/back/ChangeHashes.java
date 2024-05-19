/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Create browser popups....
 *https://teamdev.com/jxbrowser/docs/guides/popups.html
 * @author david
 */

public class ChangeHashes {
    
 
    
    // mysql connection information
    
    String url;
    String mysql_username;
    String mysql_password;
    
    
    public String HashString(String input, String mode){
        String result;
        try{
            
            MessageDigest md = MessageDigest.getInstance(mode);
            
            result = ByteToString(md.digest(input.getBytes(StandardCharsets.UTF_8)));
            
        }catch(NoSuchAlgorithmException e){
           result = "F"; // Thats an acai refrence "Big F"
           System.out.println(e.getMessage());
        }

        
        return result;
    }
    
    public String ByteToString(byte[] in){
        
        BigInteger number = new BigInteger(1, in);
            
            
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return (hexString.toString().toLowerCase());
    }
    
    
    
    public Connection GetConnection() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // låt oss test denna istället
        Connection connection = DriverManager.getConnection(this.url, this.mysql_username, this.mysql_password);
        return connection;
    }catch (ClassNotFoundException e) {
        System.out.println(e.getMessage());
        return null;
    }catch(SQLException e){
        System.out.println(e.getMessage());
        return null;
    }
    }
    public String ChangeHashes_f(String NewPassword){
        
        // Get DB connection
        String hashed_password = HashString(NewPassword, "SHA-256");
        System.out.println(hashed_password);
        // Create sql statement

        try{
            Connection connection = GetConnection();
            // skaffa data som kan updateras (jag hade ingen aning om att man skulle spsificera detta...)
            // where är där för att inte göra sql gudarna arga
            Statement statement = connection.prepareStatement("UPDATE employees SET password='" + hashed_password+"' WHERE employeeNumber > 0;");
            statement.execute("UPDATE employees SET password='" + hashed_password+"' WHERE employeeNumber > 0;");
            
            

            
            connection.close();
            statement.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return "SQLException";
        }catch (Exception e) { // generic case
            System.out.println(e.getMessage());
            return "Exception";
        }
        System.out.println("OK:" + hashed_password);
        return hashed_password;
    }
        public ChangeHashes(){
            url = "jdbc:mysql://localhost:3306/Company?zeroDateTimeBehavior=CONVERT_TO_NULL";
            mysql_username = "admin";
            mysql_password = "password";
        }
        // if username does not exist or password is incorrect
        
}
// https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html