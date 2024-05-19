<%-- 
    Document   : status
    Created on : 23 Apr 2024, 14:28:23
    Author     : david
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="com.company.back.Util" %>
<%@page import="com.company.back.Getstuff" %>
<%@page import="com.company.back.Changestuff" %>
<%@page import="java.sql.*" %>
<%@page import="java.util.ArrayList;" %>
    <%
       // https://www.tutorialspoint.com/jsp/jsp_architecture.htm
       Getstuff get = new Getstuff(); 
       Changestuff change = new Changestuff(get.GetConnection()); 
       Util util = new Util(); 
       Connection connection = get.GetConnection(); 
    %>
   



<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style/status.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Status</title>
    </head>
    <body>
        <a href="response.jsp">back</a>
        <table id="big-table">
            <tr><th>Product code</th><th>Product name</th><th>Product line</th><th>Product vendor</th><th>quantity in stock</th><th>Description</th></tr>
                <%

                    ResultSet products;
                    products = get.GetResultSetFromQuery("SELECT * FROM products;", connection);
                    while(products.next()){      
                %>
                    <tr>
                        <td> <%= products.getString("productCode") %></td>
                        <td> <%= products.getString("productName") %></td>
                        <td> <%= products.getString("productLine") %></td>
                        <td> <%= products.getString("productVendor") %></td>
                        <td> <%= products.getString("quantityInStock") %></td>
                        <td> <%= products.getString("productDescription") %></td>
                    </tr>  
            <%}%>
        </table>
    </body>
</html>
