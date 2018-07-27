package com.nextramile.util;

import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
  
public class Emailer  
{  
 public static boolean send(String from, String to, String subject, String body){  
      //String host = ConfigExtractor.returnConfig("smtphost");
	  String host = "localhost";
      Properties properties = System.getProperties();  
      properties.setProperty("mail.smtp.host", host);  
      Session session = Session.getDefaultInstance(properties, null);
      
      body = 
    		  "________________________________________________________________________________________________\n\n" 
      + subject + 
      "\n________________________________________________________________________________________________\n" +
      body +
      "\n\n***************************************************************************************************************************";
      
      try {  
         MimeMessage message = new MimeMessage(session);  
         message.setFrom(new InternetAddress(from));  
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
         message.setSubject(subject);  
         message.setContent(getMailHTML(body),"text/html");  
         Transport.send(message);  
         System.out.println("message sent successfully...");  
         return true;
      } catch (MessagingException mex) {
    	  mex.printStackTrace();
    	  return false;
      }  
   } 
 
   private static String getMailHTML(String body) {
	   StringBuilder builder = new StringBuilder();
	   builder.append("<div style=\"background:#eee;\"><table><tr><td><img src=\"https://nextramile.com/images/home/nm-logo.png\" width=\"100\"></td>"
	   		+ "<td><h3>Nextramile</h3>We Go the Extra Mile to Help You and Make You Happy</td></tr></table></div>");
	   builder.append("<div style=\"background:#eee;min-height:300px;\"><table><tr><td>" + body + "</td></tr></table></div>");
	   return builder.toString();
   }
}  
