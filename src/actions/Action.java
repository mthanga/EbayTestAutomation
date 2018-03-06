package actions;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import constant.CommonConstant;
import initialize.Setup;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import rwData.WriteData;
import util.Utility;

public class Action {
	
	public static String winHandleBefore;
	
	
	public static String actionExecuteIfExpectedDeviceMatched(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		String message = null;
		try{
			message = inputParamMap.get("actualDeviceName");
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		return message;
	}
	
	
	public static String actionTabByCordinates(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		String[] userInput = inputParamMap.get("userInput").split(",");
		int xCordinateValue = Integer.parseInt(userInput[0]);
		int yCordinateValue = Integer.parseInt(userInput[1]);
		String componentStatus = null;
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			MultiTouchAction maction = new MultiTouchAction((MobileDriver) driver);
			TouchAction action = new TouchAction((MobileDriver) driver).tap(xCordinateValue, yCordinateValue).waitAction(5000);
			maction.add(action).perform();
			
		}catch(Exception e){
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		return componentStatus;
	}

	
	public static String actionLongPressByCordinates(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		String[] userInput = inputParamMap.get("userInput").split(",");
		int xCordinateValue = Integer.parseInt(userInput[0]);
		int yCordinateValue = Integer.parseInt(userInput[1]);
		String componentStatus = null;
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			MultiTouchAction maction = new MultiTouchAction((MobileDriver) driver);
			TouchAction action = new TouchAction((MobileDriver) driver).longPress(xCordinateValue, yCordinateValue).waitAction(3000);
			maction.add(action).perform();
			
		}catch(Exception e){
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		return componentStatus;
	}
	
	public static String actionLongpress(HashMap<String, Object> driverDetailsMap,HashMap<String, String> inputParamMap){
	       String componentType = inputParamMap.get("componentType");
	       String componentValue = inputParamMap.get("componentValue");
	       String deviceName = (String) inputParamMap.get("deviceToExecute");
		   
		   String actionStarttime = inputParamMap.get("actionStarttime");
		   String componentStatus = null;
		   MobileDriver driver;
	       
	       try{
				driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
				checkScreenOrientationIsInPortrait(driver);
	            By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
	            componentStatus = waitUntilComponentDisplayed(driver, identifierFindMethod, componentValue, actionStarttime);
				if(Utility.isNull(componentStatus)){
				TouchAction action = new TouchAction(driver);
				        action.longPress(driver.findElement(identifierFindMethod)).release().perform();
				}
	          
	       }catch(Exception e){
	       	
	    	   WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
	           e.printStackTrace();
	       }
	       return componentStatus;
		}
	
	public static String actionEnterTextByKeyEvent(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		String input = inputParamMap.get("userInput");
		String componentStatus = null;
		AndroidDriver driver;
		
		try{
			
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			driver.getKeyboard().sendKeys(input);
			
		}catch(Exception e){
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		return componentStatus;
	}

	public static String actionWaitUntilComponentDisplayed(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		
		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			System.out.println("waiting for the component");
			driver.findElement(identifierFindMethod).isDisplayed();
			
		}catch(Exception e){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			actionWaitUntilComponentDisplayed(driverDetailsMap, inputParamMap);
		}
		
		return CommonConstant.DISPLAYED;
	}

	public static String actionClick(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String actionStarttime = inputParamMap.get("actionStarttime");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		String componentStatus = null;
		
		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			componentStatus = waitUntilComponentDisplayed(driver, identifierFindMethod, componentValue, actionStarttime);
			if(Utility.isNull(componentStatus)){
				driver.findElement(identifierFindMethod).click();
			}
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		
		return componentStatus;
		
	}
	
	
	public static String actionEnterText(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String userInput = inputParamMap.get("userInput");
		String actionStarttime = inputParamMap.get("actionStarttime");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		String componentStatus = null;
		
		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			componentStatus = waitUntilComponentDisplayed(driver, identifierFindMethod, componentValue, actionStarttime);
			if(Utility.isNull(componentStatus)){
				driver.findElement(identifierFindMethod).clear();
				driver.findElement(identifierFindMethod).sendKeys(userInput);
			}
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		
		return componentStatus;
	}
	
	public static String actionGetMessage(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String actionStarttime = inputParamMap.get("actionStarttime");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		String message = null;
		
		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			message = waitUntilComponentDisplayed(driver, identifierFindMethod, componentValue, actionStarttime);
			if(Utility.isNull(message)){
				message =  driver.findElement(identifierFindMethod).getText();
			}
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		return message;
	}

	public static String actionWaitUntilComponenetInvisibile(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){

		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		

		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			System.out.println("waiting for the component to disappear");	
			WebDriverWait wait = new WebDriverWait(driver, 30);
			 wait.until(ExpectedConditions.invisibilityOfElementLocated(identifierFindMethod));

		}catch(Exception e){
			actionWaitUntilComponenetInvisibile(driverDetailsMap, inputParamMap);
		}

		return CommonConstant.NOT_DISPLAYED;
	}
	
	public static String actionDelay(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		String userInput = inputParamMap.get("userInput");
		String[] userInputArray = userInput.split("\\.");
		int delayTime = Integer.parseInt(userInputArray[0]); 
		 
		try{
			Utility.componentWaitTime(delayTime);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static void actionSwipeBottomToTop(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap) throws InterruptedException{
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			Utility.swipingBottomToTop(driver, 0, 0);
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void actionSwipeTopToBottom(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap) throws InterruptedException{
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			Utility.swipingTopToBottom(driver, 0, 0);
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	public static void actionSwipeRightToLeft(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap) throws InterruptedException{
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			Utility.swipingRightToLeft(driver, 0, 0);
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void actionSwipeLeftToRight(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap) throws InterruptedException{
		
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		MobileDriver driver;
		
		try{
			driver = (AndroidDriver) Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			Utility.swipingLeftToRight(driver, 0, 0);
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static String actionIsDisplayed(HashMap<String, Object> driverDetailsMap, HashMap<String, String> inputParamMap){
		
		String componentType = inputParamMap.get("componentType");
		String componentValue = inputParamMap.get("componentValue");
		String actionStarttime = inputParamMap.get("actionStarttime");
		String deviceName = (String) inputParamMap.get("deviceToExecute");
		
		String componentStatus = null;
		
		try{
			WebDriver driver = Setup.getDriver(deviceName, driverDetailsMap);
			checkScreenOrientationIsInPortrait(driver);
			By identifierFindMethod = getIdentifierFindMethod(componentType, componentValue);
			
			componentStatus = waitUntilComponentDisplayed(driver, identifierFindMethod, componentValue, actionStarttime);
			if(Utility.isNull(componentStatus)){
				componentStatus = CommonConstant.DISPLAYED;
			}else{
				componentStatus = CommonConstant.NOT_DISPLAYED;
			}
			
		}catch(Exception e){
			
			WriteData.writeTestCaseError(inputParamMap.get("testCaseName"), inputParamMap.get("sheetName"), deviceName, e.getMessage());
			e.printStackTrace();
		}
		
		return componentStatus;
		
	}
	
		
	private static String waitUntilComponentDisplayed(WebDriver driver,	By identifierFindMethod, String identifier, String actionStarttime) throws Exception {

		String componentStatus = null;
		try {
			String test = Setup.commonConfig.get(CommonConstant.COMPONENT_DELAY_SECONDS).split("\\.")[0];
			int n = Integer.parseInt(test);
			System.out.println("Common delay time ===> " +Integer.parseInt(Setup.commonConfig.get(CommonConstant.COMPONENT_DELAY_SECONDS).split("\\.")[0]));
			System.out.println();
			
			WebDriverWait wait = new WebDriverWait((MobileDriver)driver, Integer.parseInt(Setup.commonConfig.get(CommonConstant.COMPONENT_DELAY_SECONDS).split("\\.")[0]));
			wait.until(ExpectedConditions.visibilityOfElementLocated(identifierFindMethod));
			
		}catch(Exception e) {
			componentStatus = CommonConstant.COMPONENT_NOT_DISPLAYED;
		}
		
		return componentStatus;
		
	}
	
	//to check screen is in portrait
	private static void checkScreenOrientationIsInPortrait(WebDriver driver) {
		
		try {
			
			if(((AndroidDriver) driver).getOrientation().equals(org.openqa.selenium.ScreenOrientation.LANDSCAPE)) {
				((Rotatable) driver).rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private static By getIdentifierFindMethod(String componentType, String componentValue) {
		
		if(componentType.equals("id")){
			return (By)By.id(componentValue.toString());
		}else if (componentType.equals("class")){
			return (By)By.className((componentValue.toString()));
		}else if (componentType.equals("xpath")){
			return (By)By.xpath((componentValue.toString()));
		}else if (componentType.equals("link")){
			return (By)By.linkText((componentValue.toString()));
		}else if (componentType.equals("name")){
			return (By)By.name((componentValue.toString()));
		}else if (componentType.equals("tagName")){
			return (By)By.tagName((componentValue.toString()));
		}else if (componentType.equals("partialLink")){
			return (By)By.partialLinkText((componentValue.toString()));
		}else if (componentType.equals("cssSelector")){
			return (By)By.cssSelector((componentValue.toString()));
		}
		return null;
	}

}
