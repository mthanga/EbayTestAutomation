package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import constant.CommonConstant;
import initialize.Setup;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import script.ScriptRunner;


public class Utility {

	public static void componentWaitTime(int seconds){
		try {
			Thread.sleep(seconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void swipingRightToLeft(MobileDriver driver, float startxValue, float endxValue) throws InterruptedException {
		
	try{
		  if(startxValue == 0 && endxValue == 0){
			  startxValue = (float) 0.90;
			  endxValue = (float) 0.10;
			  
		  }
		  
		  Dimension size;
		  size = driver.manage().window().getSize();
		  System.out.println(size);
		  
		  int startx = (int) (size.width * startxValue);
		  int endx = (int) (size.width * endxValue);
		  int starty = size.height / 2;
		  System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);

		  //Swipe from Right to Left.
		  driver.swipe(startx, starty, endx, starty, 3000);
		  Thread.sleep(2000);
		}catch(Exception e){
			e.printStackTrace();
		}
	  
	 }
	
	public static void swipingLeftToRight(MobileDriver driver, float startxValue, float endxValue) throws InterruptedException {
		
	  try{
		  if(startxValue == 0 && endxValue == 0){
			  startxValue = (float) 0.90;
			  endxValue = (float) 0.10;
		  }
		  
		  Dimension size;
		  size = driver.manage().window().getSize();
		  System.out.println(size);
		  
		  int startx = (int) (size.width * startxValue);
		  int endx = (int) (size.width * endxValue);
		  int starty = size.height / 2;
		  System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);

		  //Swipe from Left to Right.
		  driver.swipe(endx, starty, startx, starty, 3000);
		  Thread.sleep(2000);
	  	}catch(Exception e){
	  		e.printStackTrace();
	  	}
		  	
	}
	
	public static void swipingTopToBottom(MobileDriver driver, float startyValue, float endyValue) throws InterruptedException {
		
	  try{
		  if(startyValue == 0 && endyValue == 0){
			  startyValue = (float) 0.70;
			  endyValue = (float) 0.40;
		  }
		  
		  Dimension size;
		  size = driver.manage().window().getSize();
		  System.out.println(size);
		   
		  int starty = (int) (size.height * startyValue);
		  int endy = (int) (size.height * endyValue);
		  int startx = size.width / 2;
		  System.out.println("starty = " + starty + " ,endy = " + endy + " , startx = " + startx);

		  //Swipe from Top to Bottom.
		  driver.swipe(startx, endy, startx, starty, 3000);
	  }catch(Exception e){
		  e.printStackTrace();
	  }
		  
	}
	
	public static void swipingBottomToTop(MobileDriver driver, float startyValue, float endyValue) throws InterruptedException {
		
	  try{
		  if(startyValue == 0 && endyValue == 0){
			  startyValue = (float) 0.80;
			  endyValue = (float) 0.20;
		  }
		  
		  Dimension size;
		  size = driver.manage().window().getSize();
		  System.out.println(size);
		   
		  int starty = (int) (size.height * startyValue);
		  int endy = (int) (size.height * endyValue);
		  int startx = size.width / 2;
		  System.out.println("starty = " + starty + " ,endy = " + endy + " , startx = " + startx);

		  //Swipe from Bottom to Top.
		  driver.swipe(startx, starty, startx, endy, 3000);
	  }catch(Exception e){
		  e.printStackTrace();
	  }
		  
	}
	
    public static boolean isNull(String parameter){
    	
    	if(parameter == null){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    
    public static boolean isNotNull(String parameter){
    	
    	if(parameter != null && parameter != " "){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static boolean isNotEmpty(String parameter){
    	
    	if(!parameter.isEmpty() || parameter != ""){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static void sendMailForTestCaseResult(String outputTestCasePath, String outputFolderTime, int passCount, int failCount) throws IOException{
		 try{
			 	Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String currentTime = dateFormat.format(date);
				
			    final String fromEmail = "testautomation.result1@gmail.com"; //requires valid gmail id
				final String password = "automation"; // correct password for gmail id
				
				final String toEmail = Setup.commonConfig.get(CommonConstant.OUTPUT_TO_MAIL_ADDRESS);
				
				System.out.println("Sending Email Started ............");
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
				props.put("mail.smtp.port", "587"); //TLS Port
				props.put("mail.smtp.auth", "true"); //enable authentication
				props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
				
		         //create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, password);
					}
				};
				Session session = Session.getInstance(props, auth);
				
				MimeMessage msg = new MimeMessage(session);
			      //set message headers
			      msg.addHeader("Content-type", "text/html; charset=UTF-8");
			      msg.addHeader("format", "flowed");
			      msg.addHeader("Content-Transfer-Encoding", "8bit");

			      msg.setFrom(new InternetAddress("no_reply@journaldev.com", "Automation Result"));

			      msg.setReplyTo(InternetAddress.parse("testautomation.result1@gmail.com", false));

			      msg.setSubject("Automation Execution Result - "+currentTime, "UTF-8");
			      //msg.setSubject("Automation execution result - "+testCaseIdentifier, "UTF-8");
			      String body = "<html><body>"
			    		  		+"<h4>Automation execution result short summary listed below:</h4>"
			    		  		+"<table border='0' style='border-collapse:collapse'><tbody>"
			    		  		+"<tr><td style='border:1px solid rgb(0,0,0)'> <i>Total Testcase Count :</i> </td>"
			    		  		+"<td width='50' style='border:1px solid rgb(0,0,0);font-weight:bold;text-align:center'>"+(passCount+failCount)+"</td></tr>"
			    		  		+"<tr><td style='border:1px solid rgb(0,0,0)'><i>Total Passed Testcase Count : </i> </td>"
			    		  		+"<td width='50' style='border:1px solid rgb(0,0,0);font-weight:bold;text-align:center'>"+passCount+"</td></tr>"
			    		  		+"<tr><td style='border:1px solid rgb(0,0,0)'><i>Total Failed Testcase Count :</i> </td>"
			    		  		+"<td width='5' style='border:1px solid rgb(0,0,0);font-weight:bold;text-align:center;background-color:rgb(255,0,0)'>"+failCount+"</td></tr>"
			    		  		+"</tbody></table>"
			    		  		+"&nbsp;"
			    		  		+"<h4>Download html file and double click on it to view the test report in detail.</h4>"
			    		  		+"<h4>Automation Test results attached on e-mail. </h4>"
			    		  		+"&nbsp;"
			    		  		+"Thanks <br>"
			    		  		+"&nbsp;"
			    		  		+"<i>Ionixx Technologies</i>"
			    		  		+"</body></html>";
			      
			      msg.setText(body, "UTF-8");

			      msg.setSentDate(new Date());
			      msg.setContent(body, "text/HTML");

			      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			      
			      
		        // Create the message body part
		         BodyPart messageBodyPart = new MimeBodyPart();
		         
		         // Fill the message
		         messageBodyPart.setText(body);
		         
		         messageBodyPart.setHeader("Content-type", "text/html; charset=UTF-8");
		         // Create a multipart message for attachment
		         Multipart multipart = new MimeMultipart();
		         
		         // Set text message part
		         multipart.addBodyPart(messageBodyPart);

		         File directory = new File(outputTestCasePath+File.separator+outputFolderTime+File.separator);
		         File[] fileList = directory.listFiles();
		         
		         for(int fileCount=0; fileCount<fileList.length; fileCount++){
		        	 
		        	 // Second part is attachment
			         messageBodyPart = new MimeBodyPart();
			         
		        	 String filename = fileList[fileCount].getName();
		        	 if(!filename.equals("log.txt.lck")) {
		        		 DataSource source = new FileDataSource(directory+File.separator+filename);
				         messageBodyPart.setDataHandler(new DataHandler(source));
				         
				         messageBodyPart.setFileName(filename);
				         multipart.addBodyPart(messageBodyPart, fileCount);
		        	 }
			         
		         }
		         
		         // Send the complete message parts
		         msg.setContent(multipart);

		         // Send message
		         Transport.send(msg);
		         System.out.println("EMail Sent Successfully ............");
		 	}
		    catch (Exception e) {
		      e.printStackTrace();
		    }
				
	 }
    
}
