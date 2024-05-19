/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author david
 */
/*
    Servlet för att hantera logout. Klassen invaliderar sessionen och förstår de kakor som skapats vid login.
*/
@WebServlet("/logout")
public class Logout extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
    try  {
        // set error attribut
        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("EmployeeNumber"); 
        request.getSession().removeAttribute("Firstname"); 
        request.getSession().removeAttribute("Job"); 
        request.getSession().removeAttribute("Lastname");        
        request.getSession().removeAttribute("Extension");       

	   
        // request.getSession().removeAttribute(LEGACY_DO_HEAD);
        HttpSession session = request.getSession();
        System.out.println("SESSION(Logout): " + session.getId());
        Util util = new Util();
        // //
        Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    /*
                        Check if the cookie belongs to the current session
                    */
                    if(cookie.getName().contains(util.HashString(session.toString(), "SHA-256"))){
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);   
                    } 
                }
        }
        // invalidera session
        request.getSession().invalidate();
        // post till index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
         
    }catch(Exception e){ // generic exception
	    System.out.println("logout exception: " + e);
     }    
    }
}
