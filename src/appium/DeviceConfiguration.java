package appium;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import constant.CommonConstant;

public class DeviceConfiguration {

	CommandPrompt cmd = new CommandPrompt();
	Map<String, String> devices = new HashMap<String, String>();
	
	/**
	 * This method start adb server
	 */
	public void startADB() throws Exception{
		String output = cmd.runCommand("adb start-server");
		String[] lines = output.split("\n");
		if(lines.length==1)
			System.out.println("adb service already started");
		else if(lines[1].equalsIgnoreCase("* daemon started successfully *"))
			System.out.println("adb service started");
		else if(lines[0].contains("internal or external command")){
			System.out.println("adb path not set in system varibale");
			System.exit(0);
		}
	}
	
	/**
	 * This method stop adb server
	 */
	public void stopADB() throws Exception{
		cmd.runCommand("adb kill-server");
		System.out.println("adb service stopped");
	}
	
	/**
	 * This method return connected devices
	 * @return hashmap of connected devices information
	 */
	public Map<String, String> getDevices(String deviceType) throws Exception	{
		
		try{
			startADB(); // start adb service
			String output = cmd.runCommand("adb devices");
			String[] lines = output.split("\n");

			int emulatorIndex = 1;
			for(int i=1;i<lines.length;i++){
				lines[i]=lines[i].replaceAll("\\s+", "");
				
				if(lines[i].contains("device")){
					lines[i]=lines[i].replaceAll("device", "");
					String deviceID = lines[i];
					String model = cmd.runCommand("adb -s "+deviceID+" shell getprop ro.product.model").replaceAll("\\s+", "");
					String brand = cmd.runCommand("adb -s "+deviceID+" shell getprop ro.product.brand").replaceAll("\\s+", "");
					String osVersion = cmd.runCommand("adb -s "+deviceID+" shell getprop ro.build.version.release").replaceAll("\\s+", "");
					String deviceName = brand+" "+model;
					
					if(deviceType.equals(CommonConstant.ALL_DEVICE)){
						devices.put("deviceID"+i, deviceID);
					}else if(deviceType.equals(CommonConstant.PHYSICAL_DEVICE)){
						if(!lines[i].contains("emulator")){
							devices.put(deviceID, deviceID);
						}
					}else if(deviceType.equals(CommonConstant.EMULATOR_DEVICE)){
						if(lines[i].contains("emulator")){
							devices.put("deviceID"+emulatorIndex, deviceID);
							emulatorIndex++;
						}
					}
					
					System.out.println("Following device is connected");
					System.out.println(deviceID+" "+deviceName+" "+osVersion+"\n");
				}else if(lines[i].contains("unauthorized")){
					lines[i]=lines[i].replaceAll("unauthorized", "");
					String deviceID = lines[i];
					
					System.out.println("Following device is unauthorized");
					System.out.println(deviceID+"\n");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return devices;
	}
	

	public static void main(String[] args) throws Exception {
		DeviceConfiguration gd = new DeviceConfiguration();
		
		 String adbPath = "/home/ionixx/Android/Sdk/platform-tools/adb";
		   System.out.println("Killing emulator...");
		   String[] aCommand = new String[] { adbPath, "adb -s emulator-5554 emu", "kill" };
		   try {
		    Process process = new ProcessBuilder(aCommand).start();
		    process.waitFor(60, TimeUnit.SECONDS);
		    System.out.println("Emulator closed successfully!");
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
	}
}
