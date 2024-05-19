/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.back;

import jakarta.servlet.http.*;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




/**
 *
 * @author david
 */

/*
    I denna klass hanteras hashning. Den heter util eftersom'
    här fanns annat en gpng i tiden, men nu ör det bara hashningen som
    lever kvar här. borde troligtvis byta namn.
*/
public class Util {
        
    /*
        Metod för att applicera algoritmen mode på en sträng input
    */
    public String HashString(String input, String mode){
        String result;
        try{
            
            MessageDigest md = MessageDigest.getInstance(mode);
            
            result = ByteToString(md.digest(input.getBytes(StandardCharsets.UTF_8)));
            
        }catch(NoSuchAlgorithmException e){
           result = "F"; // Thats an acai refrence "Big F" (EN subpopkult refrens hoppas jag att du godtar).
           System.out.println(e.getMessage());
        }

        
        return result;
    }
    /*
        Konverterar datatypen 'byte' till en sträng.
        Jag skall vara helt ärlig, jag vet inte riktigt varför denna funcktin gör 
        det den gör. Men FRÅGA MIG, JAG BORDE HA VARIT SMART NOG ATT TA REDA PÅ DET INNAN KODSAMTALET.
    */
    public String ByteToString(byte[] in){
        
        BigInteger number = new BigInteger(1, in);
            
            
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return (hexString.toString().toLowerCase());
    }
    public String getcookie(HttpRequest request, String name){

        System.out.println("cookie error ):");
        return "err";
    }

    
   
    /*
        Tom 'konstruktor'.
    */
    
    public Util(){
        
    }
}
