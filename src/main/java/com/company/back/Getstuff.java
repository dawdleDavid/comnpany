/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author david
 */

/*
_____________________________________________________

Denna klass användes då man skall hämta saker, därav namnet.
Till en början var det tanken att allt som involverade att hämta
saker från databasen skulle ske här, men detta visade sig vara opraktiskt,
vid senare versioner så andvändes endast de enklaste metoderna.
_____________________________________________________
*/

public class Getstuff {
      
    String url;
    String mysql_username;
    String mysql_password;

    
    
    
    
    /*
        Metod för att hämta en connection, sättet är något föråldrat, men fungerar.
        Om connection inte kan hämtas så skapas ett 'exception'.
    */
    public Connection GetConnection() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // låt oss test denna istället
        Connection connection = DriverManager.getConnection(this.url, this.mysql_username, this.mysql_password);
        return connection;
    }catch (ClassNotFoundException e) {
        System.out.println("ClassNotFoundException(GetConnection): " + e.getMessage());
        return null;
    }catch(SQLException e){
        System.out.println("SQLException(GetConnection): " + e.getMessage());
        return null;
    }
    }
    /*
        Metod för att hämta information från databasen i form av en ArrayList<String>.
        Denna metod användes bara på ett fåtal stållen. Den skrevs innan ResultSet började
        användas, då detta var ett bättre sätt att hämta saker från databasen.
    */
    public ArrayList<String> GetFromEmployeeNumber(int employeeNumber, String column, String table, String cond){
        ArrayList<String> retval = new ArrayList(); 

        try{
            Connection connection = GetConnection();
    
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + column + " FROM "+ table +" WHERE "+cond+" =" + employeeNumber + ";");

            while(result.next()){
		   retval.add(result.getString(column));
            }
            connection.close();
        }catch(SQLException e){
            // some black magic going on with this SQL exception.... 
            System.out.println("SQLException(GetFromEmployeeNumber): " + e);
        }
        if(retval.isEmpty()){
            retval.add("<p style=\"color: red;\">no data</p>");
        }
        
        System.out.println("retval: " + retval.toString());
        
        
        return retval;
    }
    /*
        Den metod som kom att ersätta GetFromEmployeeNumber. Använder sig
        istället av ett ResultSet, som är en cachad version av query informationen.
    */
    public ResultSet GetResultSetFromQuery(String query, Connection connection){
        try{
            Statement statement = connection.createStatement();  
            ResultSet result = statement.executeQuery(query);  
            return result;
        }catch(SQLException e){
            System.out.println("SQLException(GetResultSetFromQuery): "  + e);
        }
        return null;   
    }
    /*
        GetStringListFromQuery är samma som GetResultSetFromQuery, men den
        retunerar en arraylist istället, även denna kom att falla ur användning
        med tiden.
    */
    public ArrayList<String> GetStringListFromQuery(String query, String thing){
        ArrayList<String> ret = new ArrayList();

        try{
            Connection connection = GetConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            int i = 0;
            while(result.next()){
                ret.add(i, result.getString(thing));
                i++;
            }
        
            return ret;
        }catch(SQLException e){
            System.out.println("SQLException(GetStringListFromQuery): "  + e);
        }
        return null;
    }
    
    /*
        Constructor för klassen, eftersom det antas att det inte komer att ske något byta av databas så
        är denna information helt statisk. Skulle lika gärna kunna ges vid initisiering. 
    */
    public Getstuff(){
        this.url = "jdbc:mysql://localhost:3306/company?zeroDateTimeBehavior=CONVERT_TO_NULL&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.mysql_username = "david";
        this.mysql_password = "password";
    }    
}


