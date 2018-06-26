package com.nextramile.util;

import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
  
public class Emailer  
{  
 public static boolean send(String from, String to, String subject, String body){  
      //String host = ConfigExtractor.returnConfig("smtphost");
	  String host = "aspmx.l.google.com";
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
         message.setText(body);  
         Transport.send(message);  
         System.out.println("message sent successfully...");  
         return true;
      } catch (MessagingException mex) {
    	  mex.printStackTrace();
    	  return false;
      }  
   }  
}  
