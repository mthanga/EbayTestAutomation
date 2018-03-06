package initialize;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import appium.DeviceConfiguration;
import constant.CommonConstant;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import rwData.ReadData;

public class Setup {

	WebDriver driver;
	public static HashMap<String, String> commonConfig = new HashMap<String, String>();

	public HashMap<String, Object> initialize(String deviceConfigFileName) throws MalformedURLException {

		HashMap<String, Object> driverMap = new HashMap<String, Object>();
		HashMap<String, String> inputMap = new HashMap<String, String>();
		FileInputStream inputStream = null;
		String driverUrl = null;

		try {

			inputStream = new FileInputStream(new File(deviceConfigFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

			//reading common config values from config.xlsx file
			commonConfig = ReadData.readExcelKeyValueConfig(workbook.getSheet("CommonConfig"));
			
			//reading device config values from config.xlsx file
			inputMap = ReadData.readExcelKeyValueConfig(workbook.getSheet("DeviceConfig"));

			// Getting device details
			DeviceConfiguration deviceDetails = new DeviceConfiguration();
			if (deviceDetails.getDevices(CommonConstant.ALL_DEVICE).size() == 0) {
				System.out.println("No physical devices connected try again..........");
				deviceDetails.stopADB();
				System.exit(0);
			}

			//to launch specified app which is mentioned in the config file
			File app = new File(inputMap.get(CommonConstant.APK_FILE_OR_WEBDRIVER_PATH));

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("app", app.getAbsolutePath());
			capabilities.setCapability("noReset", "true");
			capabilities.setCapability("BROWSER_NAME", inputMap.get(CommonConstant.BROWSER_NAME));
			capabilities.setCapability("VERSION", inputMap.get(CommonConstant.BUILD_VERSION));
			capabilities.setCapability("deviceName", inputMap.get(CommonConstant.DEVICE_NAME));
			capabilities.setCapability("platformName", inputMap.get(CommonConstant.PLATFORM_NAME));
			capabilities.setCapability("appPackage", inputMap.get(CommonConstant.APP_PACKAGE));
			capabilities.setCapability("appActivity", inputMap.get(CommonConstant.APP_ACTIVITY));
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 500000);
			capabilities.setJavascriptEnabled(true);
			driverUrl = inputMap.get(CommonConstant.DRIVER_URL);

			driver = new AndroidDriver(new URL(driverUrl), capabilities);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

			driverMap.put(CommonConstant.DEVICE_IDENTIFIER, driver);
			driverMap.put(CommonConstant.DEVICE_NAME, inputMap.get(CommonConstant.DEVICE_NAME));
			
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return driverMap;
	}

	public static WebDriver getDriver(String deviceName, HashMap<String, Object> driverDetailsMap) {

		AndroidDriver androidDriver;
		androidDriver = (AndroidDriver) driverDetailsMap.get(deviceName);
		return androidDriver;

	}
}