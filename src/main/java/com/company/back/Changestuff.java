/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author david
 */
public class Changestuff {
    Connection connection;

    
    public int AddCustomer(ArrayList<String> customer_data){
            
        
        
            // if you have the same address line
            String query = "SELECT customerName FROM customers WHERE addressLine1='"+ customer_data.get(4) +"';"; // test if this exists
            
            
            try{
                System.out.println("control: " + query);
                Statement statement = this.connection.createStatement();
                ResultSet result = statement.executeQuery(query);
                while(result.next()){
                    if(result.getString("customerName").equals(customer_data.get(0))){
                        return 1; // user already exists
                    }
            }/**/
	}catch(SQLException e){
	     System.out.println("AddCustomer(SQLException1): " + e);
	    }
         // get contact last and first name       


         

        
	query = "INSERT INTO customers"
            + "(customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, "
            + "city, state, postalCode, country, salesRepEmployeeNumber, creditLimit) "
            + "VALUES('"+ customer_data.get(0) +"','"+customer_data.get(1)  
            + "','"+ customer_data.get(2) +"','"+customer_data.get(3) +"','"+customer_data.get(4) 
            + "','"+customer_data.get(5) +"','"+customer_data.get(6) +"','"+customer_data.get(7) +"', '" + customer_data.get(8)+"', '" + customer_data.get(9)
            + "', '" + customer_data.get(10) +"', '" + customer_data.get(11)+"');";		    
            System.out.println("control: " + query);
            try{
                // 		Statement statement = this.connection.createStatement(); // not needed, idk
                Statement statement = this.connection.prepareStatement(query);
		statement.execute(query);
            }catch(SQLException e){
                       System.out.println("AddCustomer(SQLException2): " + e);   
            }

	 return 0;
    }
    public int AddEmployee(int reportsto, int officeCode, String firstname, String lastname, String email, String extension, String jobtitle, String password){
        try{
            
            // check if employee with the same first and last name exists  
            
            // ta ett result set, om den är tom så vet vi att användaren redan finns
            
            String query = "SELECT * FROM employees WHERE email='"+email+"';";
            
            Util util = new Util();
            
            System.out.println("control: " + query);
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.isBeforeFirst()){ // this works in sql....
                System.out.println("result is not null");
                return 1; // user already exists
            }
            String hashed_password = util.HashString(password, "SHA-256");

            query = "INSERT INTO employees (lastName, firstName, extension, email, officeCode, reportsTo, jobTitle, password) VALUES('"+lastname+"','"+firstname+"','"+extension+"','"+email+"','"+officeCode+"','"+reportsto+"','"+jobtitle+"','"+hashed_password+"');";
            System.out.println("control: " + query);
            statement = this.connection.prepareStatement(query);
            statement.execute(query);           
            this.connection.close();
            statement.close();
        }catch(SQLException e){
            System.out.println("AddEmployee(SQLException): " + e);
            return -1; // error case
        }
        return 0;
    }
    

    
    public String ChangePasswords(String NewPassword){
        
        // Get DB connection
        
        Util util = new Util();
        
        
        String hashed_password = util.HashString(NewPassword, "SHA-256");
        System.out.println(hashed_password);
        // Create sql statement
        try{
            
            // skaffa data som kan updateras (jag hade ingen aning om att man skulle spsificera detta...)
            // where är där för att inte göra sql gudarna arga
            Statement statement = this.connection.prepareStatement("UPDATE employees SET password='" + hashed_password + "' WHERE employeeNumber > 0;");
            statement.execute("UPDATE employees SET password='" + hashed_password+"' WHERE employeeNumber > 0;");
            
            this.connection.close();
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
        public Changestuff(Connection connection){
            this.connection = connection;
        }
        // if username does not exist or password is incorrect
        public int RequestPasswordChange(String employeenumber, String value){
	try{
	    String query = "UPDATE employees SET requirePwdChange="+value+" WHERE employeeNumber=" + employeenumber +";";
	    Statement statement = this.connection.createStatement();
	      
	    
	    System.out.println("testest");
	    statement.execute(query);
	    statement.close();
	}catch(SQLException e){
	     System.out.println("RequestPasswordChange(SQLException): " + e);
	     return 1;
	}
	return 0;
      }
	  
	  
 // generic functions
public void closeConnection(){
     try{
	this.connection.close();  
     }catch(SQLException e){
        System.out.println("closeConnection(SQLException): " + e);
     }
}

public int genericQuery(String query){
     
     try{
	    Statement statement = this.connection.createStatement();
	      
	    statement.execute(query);
	    statement.close();

     }catch(SQLException e){
	    System.out.println("genericQuery(SQLException): " + e);
	    return 1;
     }
     return 0;
}	  
	  
	  
}
// update functions
    

