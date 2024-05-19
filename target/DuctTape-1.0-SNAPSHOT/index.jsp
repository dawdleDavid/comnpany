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
        <h3><i>Employee login</i></h3>
        <form name="Login form" method="post" action="home"  accept-charset="utf-8">
            <input type="text" name="username" placeholder="username" /><br>    
            <input type="password" name="password" placeholder="password" /><br>
            <input id="sub_btn" type="submit" value="Login" />
        </form>
        <a href="changepswd.jsp">request password change</a> <br>
        <span style="color: lightcoral;"name="error">${error}</span> <br>
        <span style="color: lightgreen;"name="response">${response}</span> 
        </section>
    </body>
</html>
