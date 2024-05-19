/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;


import java.sql.*;
//import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.net.URLEncoder;
/**
 * Get the java to java real good like, ye
 * @author david
 */


@WebServlet("/login")
public class Login extends HttpServlet{
    String username;
    String password;
    
 
    Getstuff get;
    Util util;
    
    int session_state; // state of session, (employeeNumber)

    public int getSessionstate(){
        return this.session_state;
    }
    
    
    private int CheckCredentials(String username, String password, Getstuff get){
        
        // Get DB connection
  
        System.out.println("CheckCredentials[in]: " + username + " " + password);
        // Create sql statement
        int i = 0;  String query = "SELECT * FROM employees";
    
        Connection connection = get.GetConnection();


        try{ // kanske blit onödigt långsamt, men det är som det är
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                if(result.getInt("employeeNumber") == Integer.parseInt(username) && result.getString("password").equals(password)){
                    return result.getInt("employeeNumber");
                }
            }
        }catch(SQLException e){
            System.out.println("SQLException(CheckCredentials): " + e);
        }catch(NumberFormatException e){ // om det inte är en konverterbar sträng som har använts
            return -1;
        }
        return -1;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        this.username = request.getParameter("username");
        this.password = request.getParameter("password");

        
        this.password = this.util.HashString(this.password, "SHA-256");
        
        this.session_state = CheckCredentials(this.username, this.password, get);
        
        
        // testa om check lyckades eller inte
        if(this.session_state == -1){
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else{
            System.out.println("found the password");
            //this.username = this.get.GetFromEmployeeNumber(this.session_state, "firstName");
            // init session 
            HttpSession session = request.getSession();
            // init session 
            System.out.println("SESSION(Login): " + session.getId());		
	// attributes
        
            /*
                Set session attributes...
            */
            session.setAttribute("EmployeeNumber", this.username);
            session.setAttribute("Firstname", this.get.GetFromEmployeeNumber(Integer.parseInt(this.username), "firstName", "employees", "employeeNumber").get(0));
            session.setAttribute("Job", this.get.GetFromEmployeeNumber(Integer.parseInt(this.username), "jobTitle", "employees", "employeeNumber").get(0));
            session.setAttribute("Lastname", this.get.GetFromEmployeeNumber(Integer.parseInt(this.username), "lastName", "employees", "employeeNumber").get(0));
            session.setAttribute("Extension", this.get.GetFromEmployeeNumber(Integer.parseInt(this.username), "extension", "employees", "employeeNumber").get(0));
            session.setAttribute("ChangePswd", this.get.GetFromEmployeeNumber(Integer.parseInt(this.username), "requirePwdChange", "employees", "employeeNumber").get(0));
            // cookies
            Cookie UserCookie = new Cookie("empnum" + util.HashString(session.toString(), "SHA-256"), this.username);	
            Cookie LoginTimeCookie = new Cookie("logintime", URLEncoder.encode( new java.util.Date().toString(), "UTF-8" ));
            
            UserCookie.setMaxAge(60 * 60 * 24); // max login time, (om man laddar om sidan)
            LoginTimeCookie.setMaxAge(60 * 60 * 24 * 7 * 4);
            
            
            
            response.addCookie(UserCookie);
	    response.addCookie(LoginTimeCookie);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("response.jsp");
            
            
            requestDispatcher.forward(request, response);
        }
    }
    public Login(){
        this.get = new Getstuff();
        this.util = new Util();
    }
    
}
