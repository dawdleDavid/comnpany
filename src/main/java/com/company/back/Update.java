/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
/**
 *
 * @author david
 */
/*

/*
    Gammal kod, gör ingenting.

*/

/*
@WebServlet("/update")
public class Update extends HttpServlet{
    
    Changestuff change;
    Getstuff get;
    ArrayList<String> parameters = new ArrayList();
    
    
    @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    this.get = new Getstuff();
	    this.change = new Changestuff(this.get.GetConnection());
        
        
	    
	     if(request.getSession().getAttribute("Job").equals("Sales Rep")){
	     	   //System.out.println("SESSION_VALUE: " + request.getSession().getAttribute("EmployeeNumber").toString());	
	     }else{
		   this.parameters.add(request.getSession().getAttribute("EmployeeNumber").toString()); // attribut, skiljer sig från parameter
		   this.parameters.add(request.getParameter("customerName"));
		   this.parameters.add(request.getParameter("contactLastname"));
		   this.parameters.add(request.getParameter("contactFirstname"));
		   this.parameters.add(request.getParameter("extension"));
		   this.parameters.add(request.getParameter("addressline1"));
		   this.parameters.add(request.getParameter("addressline2"));
		   this.parameters.add(request.getParameter("city"));
		   this.parameters.add(request.getParameter("state"));	
		   this.parameters.add(request.getParameter("postalCode"));
		   this.parameters.add(request.getParameter("country"));
		   this.parameters.add(request.getParameter("creditLimit"));
		   get.GetFromEmployeeNumber(Integer.parseInt( this.parameters.get(0)),"*","employees", "employeenumner");


	     }

	    
	    

	    // gör en stor lista med alla queries som skall exekveras och gör det på en gång?
	     
	     
	     
	     
     }
    
    public Update(){

    }
    
}
*/