<%-- 
    Document   : index
    Created on : 8 Feb 2024, 15:15:10
    Author     : david
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style/login.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Login</title>
    </head>
    <body>
        <section id="login-form">
        <img id="logo_main" src="graphics/Mage_hat.svg" alt="logo">
        <h1>Company</h1>
        <h3><i>Change password</i></h3>
        <form name="Change password form" method="post" action="changeps">
            <input type="text" name="employee" placeholder="employeenumber" /><br>    
            <input type="text" name="email" placeholder="email" /><br>   
            <!--<input type="text" name="firstName" placeholder="firstName" /><br>-->
            <!--<input type="text" name="lastName" placeholder="lastName" /><br>-->
            <input id="sub_btn" type="submit" value="change password" />
        </form><br>
        <a href="index.jsp">back</a>
        <span style="color: lightcoral;"name="error">${error}</span> 
        </section>
    </body>
</html>
