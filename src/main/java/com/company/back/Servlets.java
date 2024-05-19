/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;



/**
 *
 * @author david
 */

class Validation{
    Util util = new Util();
    boolean checkCookie(HttpSession session, HttpServletRequest request){
        String check="";
       	    
	Cookie cookies[]=request.getCookies();  
	
	if (cookies != null) {
            for (Cookie cookie : cookies) {
		if (cookie.getName().equals("empnum" + util.HashString(session.toString(), "SHA-256"))){
                    check = cookie.getValue();
                } 
            }
        }
        if(check.equals(request.getSession().getAttribute("EmployeeNumber").toString())){
            return true;
        }
        return false;
    }
    public Validation(){
    }
}


@WebServlet("/add")
public class Servlets extends HttpServlet{
    
    Changestuff change;
    Getstuff get;
    ArrayList<String> parameters = new ArrayList();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
            

            Validation validation = new Validation();
            this.get = new Getstuff();
            this.change = new Changestuff(this.get.GetConnection());
        
        
            //System.out.println("SESSION_VALUE: " + request.getSession().getAttribute("EmployeeNumber").toString());
            this.parameters.add(request.getSession().getAttribute("EmployeeNumber").toString());
            this.parameters.add(request.getParameter("officeCode"));
            this.parameters.add(request.getParameter("firstname"));
            this.parameters.add(request.getParameter("lastname"));
            this.parameters.add(request.getParameter("email"));
            this.parameters.add(request.getParameter("extension"));
            this.parameters.add(request.getParameter("jobtitle"));
            this.parameters.add(request.getParameter("password"));


            if(!validation.checkCookie(request.getSession(), request)){
                request.setAttribute("error", "Cookie value error");
                request.getRequestDispatcher("response.jsp").forward(request, response);   
            }
            
            
            
            if(this.parameters.contains("")){
                System.out.println("this.parameters.contains(null)");
                request.setAttribute("error", "All fields are mandatory");
                this.parameters.clear();
                request.getRequestDispatcher("response.jsp").forward(request, response);   
            }

            int res = this.change.AddEmployee(
            Integer.parseInt(this.parameters.get(0)),  
            Integer.parseInt(this.parameters.get(1)),
            this.parameters.get(2),
            this.parameters.get(3),
            this.parameters.get(4),
            this.parameters.get(5),
            this.parameters.get(6),
            this.parameters.get(7)
            );
            
            
            
            if(res == 1){
                request.setAttribute("error", "User already exists");
                this.parameters.clear();
                request.getRequestDispatcher("response.jsp").forward(request, response);   
            }
            // this has not worked too well, too bad!
            // ???

            this.parameters.clear();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("response.jsp");   
            requestDispatcher.forward(request, response);
    }

}
@WebServlet("/addOrder")
class AddOrder extends HttpServlet{

    Getstuff get;
    ArrayList<String> parameters = new ArrayList();
    String sql;
    Connection connection;
    
    
    //Changestuff change;
    
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 

            this.get = new Getstuff();
            //this.change = new Changestuff(this.get.GetConnection());
        
        
            System.out.println("SESSION_VALUE: " + request.getSession().getAttribute("EmployeeNumber").toString());
            this.parameters.add(request.getParameter("customerNumber"));
            this.parameters.add(request.getParameter("requiredDate"));
            this.parameters.add(request.getParameter("shippedDate"));
            this.parameters.add(request.getParameter("orderDate"));
            this.parameters.add(request.getParameter("comment"));
            this.parameters.add(request.getParameter("status"));
            
            
            // input validation
            if(this.parameters.contains("")){
		request.setAttribute("error", "All fields are mandatory");
		this.parameters.clear();
		request.getRequestDispatcher("response.jsp").forward(request, response);   
	    }
            
            Validation validation = new Validation();
            if(!validation.checkCookie(request.getSession(),request)){
                request.setAttribute("error", "Cookie value error");
                request.getRequestDispatcher("response.jsp").forward(request, response);   
            }
            

            try{
                if(dateformat.parse(parameters.get(2)).after(dateformat.parse(parameters.get(1)))){
                    request.setAttribute("error", "The shipped date may not be later than the required date");
                    // add code to report that the order was late
                    
		    parameters.clear();  
                }
                }catch(ParseException e){
                    System.out.println("DateFormat parse exception: " + e);
                            }
            
            
            try{
               this.connection = this.get.GetConnection();
                           
                // check that the customer is registered to the sales rep
               sql = "SELECT customerNumber FROM customers WHERE salesRepEmployeeNumber='"+request.getSession().getAttribute("EmployeeNumber").toString()+"';";
               ResultSet result = get.GetResultSetFromQuery(sql, connection);
               
               while(result.next()){
                   if(result.getString("customerNumber").equals(parameters.get(0))){        
                        sql = "INSERT INTO orders (customerNumber, requiredDate, shippedDate, orderDate, comments, status) VALUES('"+parameters.get(0)
                                +"','"+parameters.get(1)+"','"+parameters.get(2)+"','"+parameters.get(3)+ "','"+parameters.get(4)+"','"+parameters.get(5)+"');";
                        System.out.println(sql);
                        Statement statement = this.connection.prepareStatement(sql);
                        statement.execute(sql);
                        this.connection.close();
                        request.getRequestDispatcher("response.jsp").forward(request, response);   
                        
                   }
               }
               /*
                vi gör helt enkelt ingenting om den kunden inte finns, borde vara ett error i framtiden
               */
               this.connection.close();
            }catch(SQLException e){
                System.out.println("class UpdateOrder(SQLException 1) " + e);
            }catch(Exception e){
                System.out.println("class UpdateOrder(Exception 1) " + e);    
            }
            
            // wooo
            request.setAttribute("error", "you do not own that customer");
        
            request.getRequestDispatcher("response.jsp").forward(request, response);   
    }
}

@WebServlet("/addCustomer")
class AddCustomer extends HttpServlet{
        
    Changestuff change;
    Getstuff get;
    ArrayList<String> parameters = new ArrayList();
    Util util = new Util();
    @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    // cookies code
	    
	    HttpSession session = request.getSession();
	    Cookie cookies[]=request.getCookies();  
	

	     if (cookies != null) {
		   for (Cookie cookie : cookies) {
			  if (cookie.getName().equals("empnum" + util.HashString(session.toString(), "SHA-256"))){
                                System.out.println("Empnum_cookie: " + cookie.getValue());
				 this.get = new Getstuff();
				 this.change = new Changestuff(this.get.GetConnection());
					this.parameters.add(request.getParameter("customerName"));
					this.parameters.add(get.GetFromEmployeeNumber(Integer.parseInt(cookie.getValue()),"lastName" , "employees","employeeNumber").get(0));
					this.parameters.add(get.GetFromEmployeeNumber(Integer.parseInt(cookie.getValue()),"firstName" , "employees","employeeNumber").get(0));
					this.parameters.add(request.getParameter("phone"));
					this.parameters.add(request.getParameter("addressline1"));
					this.parameters.add(request.getParameter("addressline2"));
					this.parameters.add(request.getParameter("city"));
					this.parameters.add(request.getParameter("state"));	
					this.parameters.add(request.getParameter("postalCode"));
					this.parameters.add(request.getParameter("country"));
					this.parameters.add(cookie.getValue());
					this.parameters.add(request.getParameter("creditLimit"));
                                        // debug print for late scrappers
                                        System.out.println("contact firstname: " + this.parameters.get(1));
                                        
					if(this.parameters.contains("")){
					   System.out.println("this.parameters.contains(null)");
						request.setAttribute("error", "All fields are mandatory");
					     this.parameters.clear();
					     request.getRequestDispatcher("response.jsp").forward(request, response);   
					}
                                        
                                        
                                        Validation validation = new Validation();
                                        if(!validation.checkCookie(request.getSession(), request)){
                                            request.setAttribute("error", "Cookie value error");
                                            request.getRequestDispatcher("response.jsp").forward(request, response);   
                                        }
                                        
                                        
                                        
                                        try{
                                            System.out.println("parameters 11: " + this.parameters.get(11));
                                            if(Float.parseFloat(this.parameters.get(11)) > 99999.0){
                                                request.setAttribute("error", "The credit limit is set too high!");
                                                request.getRequestDispatcher("response.jsp").forward(request, response);   
                                            }
                                        }catch(Exception e){
                                            request.setAttribute("error", "invalid number format for credit limit");
                                            request.getRequestDispatcher("response.jsp").forward(request, response);   
                                        }
                                            
				   if(this.change.AddCustomer(parameters) == 1){
                                        System.out.println("user exists");
					request.setAttribute("error", "Customer already exists");
					request.getRequestDispatcher("response.jsp").forward(request, response);   
				   }
				   this.parameters.clear();
                                   
                                   // may be scuffed
				   RequestDispatcher requestDispatcher = request.getRequestDispatcher("response.jsp");   
				   requestDispatcher.forward(request, response);
			}
	    }
}else{
     request.setAttribute("error", "Cookie error, have you allowed cookies on this site?");
     RequestDispatcher requestDispatcher = request.getRequestDispatcher("/logout");  
     requestDispatcher.forward(request, response);
}
}
}
@WebServlet("/updateOrder")
class UpdateOrder extends HttpServlet{
        

    Getstuff get;
    Connection connection;
    String sql;
    ArrayList<String> parameters = new ArrayList();
    

    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.parameters.clear();
	    Cookie cookies[]=request.getCookies();  

	     if (cookies != null) {
		   for (Cookie cookie : cookies) {
			if (cookie.getName().equals("selord")){ // we need this to find out were we need to edit
                            this.get = new Getstuff();
                            // get the form data
                            parameters.add(request.getParameter("requiredDate"));
                            parameters.add(request.getParameter("shippedDate"));
                            parameters.add(request.getParameter("status"));
                            parameters.add(request.getParameter("comment"));
                            
                            System.out.println(parameters);
                            if(parameters.contains("")){
				System.out.println("this.parameters.contains(null)");
                                request.setAttribute("error", "All fields are mandatory");
				parameters.clear();
				request.getRequestDispatcher("response.jsp").forward(request, response);   
                            }
                            
                            
                            
                            Validation validation = new Validation();
                            if(!validation.checkCookie(request.getSession(), request)){
                                request.setAttribute("error", "Cookie value error");
                                request.getRequestDispatcher("response.jsp").forward(request, response);   
                            }
                            
                            if(parameters.get(3).length() > 500){
                                request.setAttribute("error", "Comments can be no longer than 500 characters");
				parameters.clear();
				request.getRequestDispatcher("response.jsp").forward(request, response);   
                            }
                            try{
                                if(dateformat.parse(parameters.get(1)).after(dateformat.parse(parameters.get(0)))){
                                    request.setAttribute("error", "Shipped date was too late, this has been reported to your higher up");
                                    // add code to report that the order was late
                                    
				    parameters.clear();  
                                }
                            }catch(ParseException e){
                                System.out.println("DateFormat parse exception: " + e);
                            }
                            try{
                                this.connection = this.get.GetConnection();
                                sql = "UPDATE orders " +
                                        "SET requiredDate='"+parameters.get(0) + "', shippedDate='"+parameters.get(1) + "', comments='"+this.parameters.get(3) + "' " +
                                        ", status='"+parameters.get(2)+"' WHERE orderNumber='"+cookie.getValue()+"'";
                                System.out.println(sql);
                                Statement statement = this.connection.prepareStatement(sql);
                                statement.execute(sql);
                                this.connection.close();
                            }catch(SQLException e){
                                System.out.println("class UpdateOrder(exception 1) " + e);
                            }
                           
                            RequestDispatcher requestDispatcher = request.getRequestDispatcher("response.jsp");   
			    requestDispatcher.forward(request, response);
                        }
                   }
             }
    }
}
@WebServlet("/updateOrderDetails")
class updateOrderDetails extends HttpServlet{
        

    Getstuff get;
    Connection connection;
    String sql;
    ArrayList<String> parameters = new ArrayList();
   
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.get = new Getstuff();
	Cookie cookies[]=request.getCookies();  
        if (cookies != null) {
	    for (Cookie cookie : cookies) {
		if (cookie.getName().equals("selord")){ // vi behöver en annan kaka för det här. Det kommer inte att fungera eftersom den används i andra konte
                        parameters.add(request.getParameter("productCode"));
                        parameters.add(request.getParameter("quantityOrdered"));
                        parameters.add(request.getParameter("priceEach"));
                        parameters.add(request.getParameter("orderLineNumber"));

                    if(parameters.contains("")){
                        System.out.println("this.parameters.contains(null)");
                        request.setAttribute("error", "All fields are mandatory");
                        parameters.clear();
                        request.getRequestDispatcher("response.jsp").forward(request, response);   
                    }

                      
                            
                    Validation validation = new Validation();
                    if(!validation.checkCookie(request.getSession(), request)){
                        request.setAttribute("error", "Cookie value error");
                        request.getRequestDispatcher("response.jsp").forward(request, response);   
                    }
                    try{
                        this.connection = this.get.GetConnection();
                        
                        sql = "SELECT orderLineNumber FROM orderdetails WHERE=orderLineNumber='"+parameters.get(3)+"';";
                        ResultSet result = get.GetResultSetFromQuery(sql, connection);
                        
                        
                        if(result == null){
                            this.connection.close();
                            request.setAttribute("error", "line number is already present in the order");
                            request.getRequestDispatcher("response.jsp").forward(request, response);   
                        }
                        sql = "UPDATE orderdetails " +
                                "SET productCode='"+parameters.get(0) + "', quantityOrdered='"+parameters.get(1) + "', priceEach='"+this.parameters.get(2) + "' " +
                                ", orderLineNumber='"+parameters.get(3)+"' WHERE productCode='"+cookie.getValue()+"'";
                        System.out.println(sql);
                        Statement statement = this.connection.prepareStatement(sql);
                        statement.execute(sql);
                        this.connection.close();
                        request.getRequestDispatcher("response.jsp").forward(request, response);  
                    }catch(SQLException e){
                        System.out.println("class UpdateOrder(exception 1) " + e);
                        request.setAttribute("error", "SQL excception");
                        request.getRequestDispatcher("response.jsp").forward(request, response);  
                    }
                }
            }
        }              
    }
}

@WebServlet("/postToOrder")
class postToOrder extends HttpServlet{

    
    Getstuff get;
    Connection connection;
    String sql;
    ArrayList<String> parameters = new ArrayList();
   
    String orderNumber = "";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.get = new Getstuff();
        parameters.add(request.getParameter("productCode"));
        parameters.add(request.getParameter("quantityOrdered"));
        parameters.add(request.getParameter("priceEach"));
        parameters.add(request.getParameter("orderLineNumber"));
       
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("orderCookie")) {
                    if(parameters.contains("")){
                        System.out.println("this.parameters.contains(null)");
                        request.setAttribute("error", "All fields are mandatory");
                        parameters.clear();
                        request.getRequestDispatcher("response.jsp").forward(request, response);   
                    }
                    orderNumber = cookie.getValue();
                    // validate that the input data are integers

                    try{
                        Integer.valueOf(parameters.get(3));
                        Integer.valueOf(parameters.get(1));
                    }catch(NumberFormatException e){
                        request.setAttribute("error", "invalid integer value for column 2 or 4");
                        request.getRequestDispatcher("response.jsp").forward(request, response);          
                    }
                    
                                             
                    Validation validation = new Validation();
                    if(!validation.checkCookie(request.getSession(), request)){
                        request.setAttribute("error", "Cookie value error");
                        request.getRequestDispatcher("response.jsp").forward(request, response);   
                    }
                    
                    
                    try{
                        this.connection = this.get.GetConnection();
                        sql = "INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES('"+ orderNumber +"', '"+ parameters.get(0) +"', '"+ parameters.get(1) +"', '"+ parameters.get(2) +"', '"+ parameters.get(3) +"');";
                        System.out.println(sql);
                        Statement statement = this.connection.prepareStatement(sql);
                        statement.execute(sql);
                        this.connection.close();
                        request.getRequestDispatcher("response.jsp").forward(request, response);     
                    }catch(SQLException e){
                        request.setAttribute("error", "SQL exception, does the product allready exist in the order?");
                        request.getRequestDispatcher("response.jsp").forward(request, response);      
                        System.out.println("class UpdateOrder(exception 1) " + e);
                    }
                }
                
            }
        }
        request.setAttribute("error", "cookie not set error");
        request.getRequestDispatcher("response.jsp").forward(request, response);     
    }
}

// when in doubt, en till sörvlet!

@WebServlet("/addOffice")
class addOffice extends HttpServlet{
    ArrayList<String> parameters = new ArrayList();
 
    Getstuff get = new Getstuff();
    
    Connection connection = get.GetConnection();
    
    String sql;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            
        this.get = new Getstuff();
        this.parameters.add(request.getParameter("city"));
        this.parameters.add(request.getParameter("phone"));
        this.parameters.add(request.getParameter("addressLine1"));
        this.parameters.add(request.getParameter("addressLine2"));
        this.parameters.add(request.getParameter("state"));
        this.parameters.add(request.getParameter("country"));
        this.parameters.add(request.getParameter("postalCode"));
        this.parameters.add(request.getParameter("terretory"));
        this.parameters.add(request.getParameter("officeCode"));
        if(parameters.contains("")){
            System.out.println("this.parameters.contains(null)");
            request.setAttribute("error", "All fields are mandatory");
            parameters.clear();
            request.getRequestDispatcher("response.jsp").forward(request, response);   
        }
        
         
        /*
        sql = "SELECT orderLineNumber FROM orderdetails WHERE=orderLineNumber='"+parameters.get(3)+"';";
        ResultSet result = this.get.GetResultSetFromQuery(sql, connection);
        if(result == null){
            try{
                this.connection.close();
                request.setAttribute("error", "officecode is already present in the database");
                request.getRequestDispatcher("response.jsp").forward(request, response);      
            }catch(SQLException e){
                request.setAttribute("error", "SQL exception, failed to close connection");
                request.getRequestDispatcher("response.jsp").forward(request, response);      
            }
        }
        */
        
        
       

        try{
            
            sql = "SELECT MAX(officeCode) FROM offices;";
            ResultSet result = this.get.GetResultSetFromQuery(sql, connection);
            
            result.next();
            int officeCode = Integer.parseInt(result.getString("officeCode")) + 1;
            System.out.println("OfficeCode:" + String.valueOf(officeCode));
               
            //
            
            

                sql = "INSERT INTO offices (city, phone, addressLine1, addressLine2, state, country, postalCode, territory, officeCode) VALUES('"+ parameters.get(0) +"', '"+ parameters.get(1) +"', '"+ parameters.get(2) +"', '"+ parameters.get(3) +"', '"+ parameters.get(4) +"', '"+ parameters.get(5) +"', '"+ parameters.get(6) +"', '"+ parameters.get(7) +"', '"+ String.valueOf(officeCode) +"');";
                System.out.println(sql);
                Statement statement = this.connection.prepareStatement(sql);
                statement.execute(sql);
                this.connection.close();
                request.getRequestDispatcher("response.jsp").forward(request, response);     

            request.getRequestDispatcher("response.jsp").forward(request, response);    
        }catch(SQLException e){
            request.setAttribute("error", "SQL exception");
            request.getRequestDispatcher("response.jsp").forward(request, response);      
            System.out.println("class UpdateOrder(exception 1) " + e);
        }
        request.setAttribute("error", "Unknown error encountered");
        request.getRequestDispatcher("response.jsp").forward(request, response);  
        

    }
}