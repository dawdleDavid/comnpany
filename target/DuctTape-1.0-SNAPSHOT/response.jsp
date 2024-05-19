<%-- 
    Document   : response
    Created on : 8 Feb 2024, 15:41:48
    Author     : david
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="com.company.back.Util" %>
<%@page import="com.company.back.Getstuff" %>
<%@page import="com.company.back.Changestuff" %>
<%@page import="java.sql.*" %>
<%@page import="java.util.ArrayList;" %>

<%@page import="jakarta.servlet.http.HttpServletRequest;" %>
<%@page import="jakarta.servlet.http.HttpServletResponse;" %>
<%@page import="jakarta.servlet.RequestDispatcher;" %>
<%@page import="java.lang.String;" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style/mainpage.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <!-- call script-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="cls/editbox.js"></script>
    </head>
    <body>
       
    <%

        
        

       // https://www.tutorialspoint.com/jsp/jsp_architecture.htm
       Getstuff get = new Getstuff(); 
       Changestuff change = new Changestuff(get.GetConnection()); 
       Util util = new Util(); 
       Connection connection = get.GetConnection(); 
    %>
   
    <table id="list">
<% 
        int mode = 0;
        ArrayList<String> res = new ArrayList<String>();
        String query = null;
	String logintime = "<p style=\"color: red;\">time set err</p>";
        String userCookie = "";
        /*
            ArrayList som lägger till alla options som skall vara med,
            valideras en extra gåg i minb backend innan ett query körs.
        */
        ArrayList<String> options = new ArrayList();
        if (request.getSession(false) == null){
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
   



        
        if(request.getSession().getAttribute("Job").equals("Sales Rep")){ // change to contains
            mode = 1;  
	  
    
			
            query = "SELECT customerNumber FROM customers WHERE salesRepEmployeeNumber='" + request.getSession().getAttribute("EmployeeNumber") + "';";	
            
            String paymentsQuery="";
            
           
            res = get.GetStringListFromQuery(query, "customerNumber");    
             //for(int i = 0; i <= (res.size()-1); i++){
             //        out.print("----" + get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "customerName", "customers", "customerNumber")  + "---");
	    //}

            

            for(int i = 0; i <= (res.size()-1); i++){ 

            out.print("<tr><th>Customer name</th><th>country</th><th>Customer number</th><th>Postal code</th><th>City</th><th>Credit limit</th></tr>");        
            out.print("<tr class=\"customer\">");
            out.print("<td>"+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "customerName", "customers", "customerNumber").get(0) +"</td>");
            out.print("<td>"+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "country", "customers", "customerNumber").get(0) +"</td>");
            out.print("<td>"+ res.get(i) +"</td>");
            out.print("<td>"+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "postalCode", "customers", "customerNumber").get(0) +"</td>");
            out.print("<td>"+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "city", "customers",  "customerNumber").get(0) +"</td>");
            out.print("<td>"+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "creditLimit", "customers",  "customerNumber").get(0) +"</td>");
  	
            
            out.print("</tr>");

                //System.out.println("p_size:" + Integer.toString((MetaRes.size()-1)));
                
                   // add options
                options.add("<option value="+res.get(i)+">"+res.get(i)+"("+ get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "customerName", "customers", "customerNumber").get(0)+")"+"</option>");

                %>
                <td><h4 class="orders-for">orders for <%= get.GetFromEmployeeNumber(Integer.parseInt(res.get(i)), "customerName", "customers", "customerNumber").get(0)  %></h4></td>
                <tr><th>orderNumber</th><th>requiredDate</th><th>shippedDate</th><th>status</th><th>comments</th></tr>
                <%
                ResultSet result;
                
                
                            
                String orderNumber = "";
                result = get.GetResultSetFromQuery("SELECT * from orders WHERE customerNumber="+res.get(i)+";", connection);
              
                while(result.next()){
                    out.print("<tr class=\"changecolor\">");
                    // out.print("cuz:" +  customernumber);
                    orderNumber = result.getString("orderNumber");
                    out.print("<td>" + orderNumber + "</td>");	//  kontrollera html för table
                    out.print("<td>" + result.getString("requiredDate") + "</td>");	
                    out.print("<td>" + result.getString("shippedDate") + "</td>");	
                    out.print("<td>" + result.getString("status") + "</td>");	
                    out.print("<td>" + result.getString("comments") + "</td>");
                    

                    out.print("<td><button id="+ orderNumber +" onclick="+"editbtn(this.id)"+">edit</button></td>");	
                    
                    out.print("</tr>");


                   %>
                <tr><td colspan="6">
                <section class="info-dump" id="form-<%= orderNumber %>">  <!-- TODO: this id has to go --> 

                <table class="orderdetails">
                        <form class="order-member" method="post" action="updateOrder">
                            <input autocomplete="off" name="requiredDate" type="date" placeholder="requredDate" value=<%=result.getString("requiredDate")%> /><br>
                            <input autocomplete="off" name="shippedDate" type="date" placeholder="shippedDate"value=<%=result.getString("shippedDate")%> /><br>
                            <input autocomplete="off" name="status" type="text" placeholder="status" value=<%=result.getString("status")%> /><br>
                            <textarea name="comment" rows="5" cols="33" placehoder="message"><%=result.getString("comments")%></textarea><br>
                            <input type="submit" value="commit" /><br>
                        </form>


                    <tr><th>Product code</th><th>Quantity</th><th>Price</th><th>Line number</th><th>edit</th></tr>
                    <%
                        String productCode;
                        ResultSet orders;
                        orders = get.GetResultSetFromQuery("SELECT * FROM orderdetails WHERE orderNumber="+orderNumber+" ORDER BY orderLineNumber;", connection);

                        while(orders.next()){      
                    %>

                    
                    
                            <tr class="hidden-td">
                                <% productCode = orders.getString("productCode"); %>
                                

                                <td> <%= productCode %></td>
                                <td> <%= orders.getString("quantityOrdered") %></td>
                                <td> <%= orders.getString("priceEach") %></td>
                                <td> <%= orders.getString("orderLineNumber") %></td>
                                <td><button id="<%= orders.getString("productCode")+orderNumber%>" onclick="editbtn(this.id)">edit</button></td>    
                            </tr>
                      
                            <tr class="hidden-tr" id="form-<%= productCode%><%=orderNumber%>" style="display: none;">
                                <td style="background-color: white;">
                            <!--<section class="info-dump" id="form-<%= productCode %>" style="display: none;">  -->
                                <form class="order-member" method="post" action="updateOrderDetails">
                                    <input autocomplete="off" name="productCode" type="text" placeholder="productCode" value=<%=productCode%> /><br>
                                    <input autocomplete="off" name="quantityOrdered" type="text" placeholder="quantityOrdered"value=<%=orders.getString("quantityOrdered")%> /><br>
                                    <input autocomplete="off" name="priceEach" type="text" placeholder="priceEach" value=<%=orders.getString("priceEach")%> /><br>
                                    <input name="orderLineNumber" placehoder="orderLineNumber" type="number" value="<%=orders.getString("orderLineNumber")%>"/><br>
                                    <input type="submit" value="commit"/>
                                    
                                </form>
                                </td>
                             <!--</section> -->
                            </tr> 
                    <%}%>
                </table>
                    <form class="post-order" method="post" action="postToOrder" onsubmit="setCookie('orderCookie', <%=orderNumber%>)">
                        
                        <select width=300 name="productCode"> 
                            <%
                            
                            query = "SELECT productCode, productName FROM products;";
                            ResultSet products = get.GetResultSetFromQuery(query, connection);
                            while(products.next()){
                                out.print("<option value="+products.getString("productCode")+">"+products.getString("productName")+"</option>");
                            }
                            %>
                        </select><br>   
                        <input autocomplete="off" name="quantityOrdered" type="text" placeholder="quantityOrdered" value="" /><br>
                        <input autocomplete="off" name="priceEach" type="text" placeholder="priceEach" value="" /><br>
                        <input autocomplete="off" name="orderLineNumber" type="text" placeholder="orderLineNumber" value="" /><br>
                        <input type="submit" value="commit" />
                    </form>
                </section></td></tr>
                
                
                
               
<%
    }
            paymentsQuery = "SELECT * FROM payments WHERE customerNumber="+res.get(i)+";";
            ResultSet payments = get.GetResultSetFromQuery(paymentsQuery, connection);
            
            
            if(!payments.isBeforeFirst()){ // !payments.isBeforeFirst()
            %>
                <td style="color: red; background-color: transparent;">no payments</td>
            <%}else{
                %>
                <tr><th><h4 class="orders-for">Payments</h4></th></tr>
                <tr><th>checkNumber</th><th>paymentDate</th><th>amount</th></tr>

    
                    
               
                <%
                while(payments.next()){ 
                %>
                    <tr class="customer">
                        <td> <%= payments.getString("checkNumber") %></td>
                        <td> <%= payments.getString("paymentDate") %></td>
                        <td> <%= payments.getFloat("amount") %> </td>
                    </tr>
                <%
                    
                }

            }  
    }
}else{
%>

<div id="scroll">
          <tr><th>id</th><th>First Name</th><th>last name</th><th>Extension</th><th>Email</th><th>Job Title</th></tr>
  <%
        query = "SELECT * FROM employees WHERE reportsTo='"+ request.getSession().getAttribute("EmployeeNumber") +"';";
        //res = get.GetStringListFromQuery(query, "employeeNumber");
        String employeeNumber = "";    
        // samma resultset som tidigare
        ResultSet result = get.GetResultSetFromQuery(query, connection);
        while(result.next()){
            employeeNumber = result.getString("employeeNumber");

            out.print("<tr class="+"changecolor"+">");
            out.print("<td>" + employeeNumber + "</td>");	
            out.print("<td>" + result.getString("firstName") + "</td>");	
            out.print("<td>" + result.getString("lastName") + "</td>");	
            out.print("<td>" + result.getString("extension") + "</td>");	
            out.print("<td>" + result.getString("email") + "</td>");
            out.print("<td>" + result.getString("jobTitle") + "</td>");
            out.print("<td><button id="+  result.getString("employeeNumber") +" onclick="+"editbtn(this.id)"+">more</button></td>");
            out.print("</tr>");
      	
    
    %>

     <tr><td colspan="6"> 
        <section class="info-dump" id="form-<%=employeeNumber %>"> 
            <table class="orderdetails">
            <%   
            query = "SELECT officeCode, requirePwdChange, extension FROM employees WHERE employeeNumber='"+ employeeNumber +"';"; // onödigt dubbelquery, men jag börjar få ont om tid 
            
            //String cityQuery;
            //ResultSet officeRes;          
            ResultSet empinfo = get.GetResultSetFromQuery(query, connection);
            while(empinfo.next()){
            //cityQuery = "SELECT city, addressLine1 FROM offices WHERE officeCode='"+empinfo.getString("officeCode")+"';";            
            //officeRes = get.GetResultSetFromQuery(query, connection);
            //officeRes.next();
            %>
            <tr><td>officeCode</td><td>requirePwdChange</td><td>extension</td></tr>
            <tr>
        
                <td><%=empinfo.getString("officeCode")%>)</td>
                <td><%=empinfo.getString("requirePwdChange")%></td>
                <td><%=empinfo.getString("extension")%></td> <!-- repeat info, just to make css3 happy -->
            </tr>
            <tr><th>Subordinates:</th></tr>
            <%
            ResultSet subord = get.GetResultSetFromQuery("SELECT * FROM employees WHERE reportsTo='"+ employeeNumber +"';", connection);
            while(subord.next()){
            %>
            <tr>
                <td><%=subord.getString("employeeNumber") %></td>
                <td><%=subord.getString("firstName")%></td>
                <td><%=subord.getString("lastName") %></td>
                <td><%=subord.getString("extension") %></td>
                <td><%=subord.getString("email") %></td>
                <td><%=subord.getString("jobTitle") %></td>
            </tr>
            <%}%>
        <%}%>
            </table>
        </section>
    </td></tr>       
 <%}%>
<%}%>
          
    <!-- Audio tag -->
    <audio controls id="phone" style="display: none;">
     <source src="cls/office_phone-ring_medium-loudaif-14604.mp3" type="audio/mp3">
     Your browser does not support the audio tag.
   </audio> 
    </table>   
        
     
<section class="controls"><br>
            <form class="controls-member" method="post" action="logout">
                <input class="controls-member"  type="submit" value="logout">
            </form>
            <a href="status.jsp">product refrence</a>
    

               <% 
                    if(mode == 0)	
                    { 
                %>
                    <h2>Add Employee</h2>
                    <form class="controls-member" method="post" action="add">
                        <input autocomplete="off" name="firstname" type="text" placeholder="firstname" value=""><br>
                        
                        
                        <input autocomplete="off" name="lastname" type="text" placeholder="lastname" value=""><br>         

  
                        <input autocomplete="off" name="email" type="text" placeholder="email" value=""><br>
                        <input autocomplete="off" name="extension" type="text" placeholder="extension" value=""><br>
                        <input autocomplete="off" name="jobtitle" type="text" placeholder="jobtitle" value=""><br>
                        <label for="officeCode">designated office: </label><br>
                        <select width=300 name="officeCode"> 
                        <% // actuallt officecodes
                        
                        query = "SELECT officeCode, addressLine1, city FROM offices;";
                        ResultSet result = get.GetResultSetFromQuery(query, connection);
                        while(result.next()){
                            out.print("<option value="+result.getInt("officeCode")+">"+result.getString("city")+"-office at "+ result.getString("addressLine1")+"</option>");
                        }
                        %>
                        </select><br>   
                        
                        <input autocomplete="off" name="password" type="password" placeholder="password" value=""><br>
                        <input type="submit" value="commit">
                    </form>
                    <h2>Add Office</h2>
                    <form class="controls-member" method="post" action="addOffice">
                        <!--<input autocomplete="off" name="officeCode" type="text" placeholder="officeCode" value=""><br>-->
                        <input autocomplete="off" name="city" type="text" placeholder="city" value="test"><br>
                        <input autocomplete="off" name="phone" type="text" placeholder="phone" value="090 342 23 23"><br>
                        <input autocomplete="off" name="addressLine1" type="text" placeholder="addressLine1" value="addressLine1"><br>
                        <input autocomplete="off" name="addressLine2" type="text" placeholder="addressLine2" value="addressLine2"><br>
                        <input autocomplete="off" name="state" type="text" placeholder="state" value="state"><br>
                        <input autocomplete="off" name="country" type="text" placeholder="country" value="country"><br>
                        <input autocomplete="off" name="postalCode" type="text" placeholder="postalCode" value="postalCode"><br>
                        <input autocomplete="off" name="terretory" type="text" placeholder="territory" value="territory"><br>
                        <input type="submit" value="commit">
                    </form>
                    <section id="log">
                        
              <%  
     
     
     Cookie cookies[]=request.getCookies();  
     if (cookies != null) {
	    for (Cookie cookie : cookies) {
		   if (cookie.getName().equals("empnum" + util.HashString(session.toString(), "SHA-256")) && request.getSession().getAttribute("ChangePswd").equals("2")) {
			  System.out.println("this is test");
			  out.print(" <h4> You must change password within 3 days, temp password is</h4>"
			  + "<form method=\"post\" action=\"updatepswd\"><br>"
			  + "<input name=\"newpass\" type=\"text\" value=\"\" placeholder=\"password\"><br>"
			  + "<input name=\"newpass_confirm\" type=\"text\" value=\"\" placeholder=\"confirm\"><br>"
			  + "<input type=\"submit\" value=\"change\">"
			  + "</form>");
		   } 
                   if (cookie.getName().equals("empnum" + util.HashString(session.toString(), "SHA-256"))) {
                        userCookie = cookie.getValue();
		   }
	}                      
}
                    // loop trough all the subordinates and find if any subordinates require a password change.
                    query = "SELECT employeeNumber, firstName, lastName FROM employees WHERE reportsTo='"+session.getAttribute("EmployeeNumber")+"' AND requirePwdChange='2';"; // may be dangerous
                    result = get.GetResultSetFromQuery(query, connection);
                    while(result.next()){
                    %>
                        <!-- Kanske skall användas som ett generellt kontaktsystem i framtiden-->
                        <section class="change-password-box">
                            <h4><%=result.getString("firstName")%> <%=result.getString("lastname")%> would like to change their password</h4>
                            <form name="<%=result.getString("employeeNumber")%>" method="post" action="changepswd">
                                <input name="deny" type="submit" value="deny">
                                <input name="allow" type="submit" value="allow">
                            </form>
                        </section>
                    <%}%>
                                           
                    </section>
                <% }else if(mode == 1){ 
                %>

                    <section id="phone-section">                
                    <!-- <img src="https://upload.wikimedia.org/wikipedia/commons/6/6c/Phone_icon.png" alt="phone"> -->
                    <h4>phone controls</h4>
                    <button id="busy" onclick="answerphone(this.id)">Decline</button>
                    <button id="busy" onclick="answerphone(this.id)">Answer</button>


                    </section>
                    <h2>Add Customer</h2>
                    <form class="controls-member" method="post" action="addCustomer">
                        <input autocomplete="off" name="customerName" type="text" placeholder="Full name off customer" value=""><br>
                        <input autocomplete="off" name=phone" type="text" placeholder="phone" value=""><br>
                        <input autocomplete="off" name="addressline1" type="text" placeholder="addressline 1" value=""><br>
                        <input autocomplete="off" name="addressline2" type="text" placeholder="addressline 2" value=""><br>
                        <input autocomplete="off" name="city" type="text" placeholder="city" value=""><br>
                        <input autocomplete="off" name="state" type="text" placeholder="state" value=""><br>
                        <input autocomplete="off" name="postalCode" type="text" placeholder="postalCode" value=""><br>
                        <input autocomplete="off" name="country" type="text" placeholder="country" value=""><br>
                        <input autocomplete="off" name="creditLimit" type="text" placeholder="creditLimit" value=""><br>
                        <input type="submit" value="commit">
                    </form>
                    <h2>Add order</h2>                  
                    <form class="controls-member" method="post" action="addOrder">
                        <!-- <input id="controls-member" autocomplete="off" name="customerNumber" type="text" placeholder="customerNumber" value=""><br> -->
                        
                        <select width=300 name="customerNumber">
                            <%
                            for(int i = 0; i <= options.size()-1; i++){
                                out.print(options.get(i));
                            }
                            %>
                        </select><br>    
                        <label for="requiredDate">required date</label><br>
                        <input class="controls-member" autocomplete="off" name="requiredDate" type="date" placeholder="startdate" value=""><br>
                        <label for="shippedDate">shipped date</label><br> 
                        <input class="controls-member" autocomplete="off" name="shippedDate" type="date" placeholder="shippeddate" value=""><br>
                        <label for="orderDate">order date</label><br>
                        <input class="controls-member" autocomplete="off" name="orderDate" type="date" placeholder="orderdate" value=""><br>
                        <select width=300  name="status">
                            <option value="Shipped">Shipped</option>
                            <option value="Not shipped">Not shipped</option>
                            <option value="Shipping">Shipping</option>
                            <option value="Awaiting payment">Awaiting payment</option>
                            <option value="see comment">see comment</option>
                        </select><br>    
                        <label for="html">comment</label><br>
                        <textarea class="controls-member" autocomplete="off" name="comment" rows="5" cols="33" placehoder="message"></textarea>
                        <input type="submit" value="commit">
                    </form>
                    <button id="">Call in sick</button>    <br>
                    
                <%}
                    /* closing connection */
                    connection.close();
                %>             
                <span style="color: lightcoral;"name="error">${error}</span> 
                <section id="log">
                <%  


                     Cookie cookies[]=request.getCookies();  
                     if (cookies != null) {
                            for (Cookie cookie : cookies) {
                                   if (cookie.getName().equals("empnum" + util.HashString(session.toString(), "SHA-256")) && request.getSession().getAttribute("ChangePswd").equals("2")) {
                                          out.print(" <fieldset> <legend>Message</legend> <h4> You must change password during this session</h4>"
                                          + "<form method=\"post\" action=\"updatepswd\"><br>"
                                          + "<input name=\"newpass\" type=\"text\" value=\"\" placeholder=\"password\"><br>"
                                          + "<input name=\"newpass_confirm\" type=\"text\" value=\"\" placeholder=\"confirm\"><br>"
                                          + "<input type=\"submit\" value=\"change\">"
                                          + "</form></fieldset> ");
                                   } 
                                   if (cookie.getName().equals("empnum")) {
                                        // add cookie to response so that the Cahngestuff might use it
                                        // set the usercookie locally to string
                                        userCookie = cookie.getValue();
                        }
                    }                      
                }

                %>                       
                </section>
            </section>
        </section> 	  
    </section>
 </section>
</body>
</html>
<!-- https://waitbutwhy.com/2013/11/how-to-beat-procrastination.html -->