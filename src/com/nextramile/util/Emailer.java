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
      
      body = "<h3>" + subject + "</h3>" + body;
      
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
	   builder.append("<div style=\"border:solid 2px #777; border-radius:5px;padding:10px; font-family:Arial;color:#555;\">");
	   builder.append("<div style=\"border-bottom:solid 1px #999;\">");
	   builder.append("<table style=\"width:100%;\">");
	   builder.append("<tr><td><img src=\"https://nextramile.com/images/home/nm-logo.png\" width=\"120\"></td>");
	   builder.append("<td><div style=\"text-align:center;font-size:110%;font-weight:bold;\">We Go the Extra Mile to Bring You Happiness</div></td>");
	   builder.append("<td><div style=\"text-align:center;font-weight:bold;\">");
	   builder.append("<a style=\"text-decoration:none;color:#070;\" href=\"www.nextramile.com/home.xhtml\">Home</a>&nbsp;|&nbsp;");
	   builder.append("<a style=\"text-decoration:none;color:#070;\" href=\"www.nextramile.com/shop.xhtml\">Shop</a>&nbsp;|&nbsp;");
	   builder.append("<a style=\"text-decoration:none;color:#070;\" href=\"www.nextramile.com/blog.xhtml\">Blog</a>&nbsp;|&nbsp;");
	   builder.append("<a style=\"text-decoration:none;color:#070;\" href=\"www.nextramile.com/about.xhtml\">About Us</a>");
	   builder.append("</div></td>");
	   builder.append("</tr>");
	   builder.append("</table>");
	   builder.append("</div>");
	   builder.append("<div><table><tr><td><div style=\"padding:25px 0 25px 0;\">" + body + "</div></td></tr></table></div>");
	   builder.append("<div style=\"border-top:solid 1px #999;\">");
	   builder.append("<p>Nextramile &copy;&nbsp;2018 |&nbsp;<a style=\"text-decoration:none;color:#070;\" href=\"www.nextramile.com\">www.nextramile.com</a> |&nbsp;+254 720 317929</p>");
	   builder.append("</div>");
	   builder.append("</div>");
	   return builder.toString();
   }
}  
