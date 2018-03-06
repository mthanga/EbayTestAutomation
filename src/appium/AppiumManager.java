package appium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import script.ScriptRunner;
/**
 * Appium Manager - this class contains method to start and stops appium server  
 */
public class AppiumManager {

	CommandPrompt cp = new CommandPrompt();
	AvailabelPorts ap = new AvailabelPorts();
	String output = null;
	
	public void startAppiumByShellScript(){
		try{
			String currentProjectPath = System.getProperty("user.dir")+File.separator;
			String os = System.getProperty("os.name");
			if(!os.contains("Windows")){
				cp.runCommand("chmod -R 777 "+currentProjectPath+"appiumcommands");
				output = cp.runCommand("appiumcommands/RunAppium.sh");
				System.out.println(output);
			}else{
				output = cp.runCommand("appium --session-override");
				System.out.println(output);
			}

			if(!output.toString().contains("Welcome to Appium")){
				System.out.println("Appium not installed on this computer ..........");
				System.out.println("Please install appium and then continue ..........");
				System.exit(0);
			}
			System.out.println("Prechecking completed for appium \n");
			System.out.println("Appium started successfully .....");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initializeDevicePortByShellScript(){
		try{
			String os = System.getProperty("os.name");
			if(!os.contains("Windows")){
				String output = cp.runCommand("appiumcommands/Device.sh");
				System.out.println(output);
			}else{
				String output = null;
				String line = null;
				FileReader fr = new FileReader("appiumcommands/Device.bat");
	            BufferedReader br = new BufferedReader(fr);
	            while ((line = br.readLine()) != null) {
	            	output = cp.runCommand(line);
	            }
	            fr.close();
	            br.close();
	            System.out.println(output);
	            
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void validatePreInstalledSoftwares() throws Exception	{
		
		try{
			String output = System.getProperty("java.version");
			
			if(output.isEmpty()){
				System.out.println("Java not installed on this computer ..........");
				System.out.println("Please install java and then continue ..........");
				System.exit(0);
			}
			System.out.println("Prechecking completed for java \n");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
//	public static void main(String a[]) {
//		System.out.println(System.getProperty("java.class.path"));
//	}
	
//	public static void main(String[] args) {
//
//		
//		try {
//			String devicePort = null;
//			String deviceID = null;
//			String hubPort = null;
//			int nodePort = 2252;
//			Map<String, String> deviceMap = new HashMap<String, String>();
//			
//			AppiumManager appiumManager = new AppiumManager();
//		//	appiumManager.startAppiumByShellScript();
//			System.out.println("Appium started successfully");
//			
//			AvailabelPorts availablePort = new AvailabelPorts();
//			DeviceConfiguration deviceDetails = new DeviceConfiguration();
//			deviceMap = deviceDetails.getDivces();
//			
//			for(int index=1; index<=2; index++){
//				nodePort = nodePort+2;
//				devicePort = availablePort.getPort();
//				hubPort = Integer.toString(nodePort);
//				deviceID = deviceMap.get("deviceID"+index);
//			//	appiumManager.updateNodeDevicePort(devicePort, hubPort, deviceID);
//			//	appiumManager.initializeDevicePortByShellScript();
//				System.out.println("device id "+deviceID +" is started...");
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
