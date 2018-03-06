package constant;

public interface CommonConstant {

	
	//Device config details
	public String BROWSER_NAME = "BROWSER_NAME";
	public String BUILD_VERSION = "VERSION";
	public String DEVICE_NAME = "DEVICE_NAME";
	public String DEVICE_TYPE = "DEVICE_TYPE";
	public String PLATFORM_NAME = "PLATFORM_NAME";
	public String APP_PACKAGE = "APP_PACKAGE";
	public String APP_ACTIVITY = "APP_ACTIVITY";
	public String DRIVER_URL = "DRIVER_URL";
	public String APK_FILE_OR_WEBDRIVER_PATH = "APK_FILE_OR_WEBDRIVER_PATH";
	public String OUTPUT_TESTCASE_PATH = "OUTPUT_TESTCASE_PATH";
	public String OUTPUT_TO_MAIL_ADDRESS = "OUTPUT_TO_MAIL_ADDRESS";
	public String COMPONENT_DELAY_SECONDS = "COMPONENT_DELAY_SECONDS";
	public String EMULATOR_PATH = "EMULATOR_PATH";
	public String SOURCE_TESTCASE_PATH = "SOURCE_TESTCASE_PATH";
	public String EXECUTION_TYPE = "EXECUTION_TYPE";
	public String PHYSICAL_DEVICE_ID = "PHYSICAL_DEVICE_ID";
	public String NO_OF_SCREEN_SHOTS_TO_VERIFY_TOAST = "NO_OF_SCREEN_SHOTS_TO_VERIFY_TOAST";
	public String IS_INITIALISE = "IS_INITIALISE";
	
	
	//Test case containing action steps column or row number
	public int TESTCASE_DEVICES_TO_RUN_ROW_NUMBER = 5;
	public int TESTCASE_DEVICES_TO_RUN_COLUMN_NUMBER = 1;
	public int TESTCASE_ACTION_STARTING_ROW_NUMBER = 7;
	public int TESTCASE_DESCRIPTION_COLUMN_NUMBER = 1;
	public int TESTCASE_DEVICE_COLUMN_NUMBER = 2;
	public int TESTCASE_ACTION_COLUMN_NUMBER = 3;
	public int TESTCASE_LOCATOR_COLUMN_NUMBER = 4;
	public int TESTCASE_INPUT_COLUMN_NUMBER = 5;
	public int TESTCASE_EXPECTED_COLUMN_NUMBER = 6;
	public int TESTCASE_ACTUAL_COLUMN_NUMBER = 7;
	public int TESTCASE_STATUS_COLUMN_NUMBER = 8;
	public int TESTCASE_PRESTEPS_RUN_FROM_SHEET_ROW_NUMBER = 3;
	public int TESTCASE_PRESTEPS_RUN_FROM_SHEET_COLUMN_NUMBER = 1;
	public int TESTCASE_PRESTEPS_IS_RUN_PRESTEPS_ROW_NUMBER = 4;
	public int TESTCASE_SCENARIO_ROW_NUMBER = 3;
	public int TESTCASE_SCENARIO_COLUMN_NUMBER = 1;
	public int SUMMARY_SHEET_TOTAL_STEPS_COLUMN_STARTING_NUMBER = 6;
	public int SUMMARY_SHEET_TOTAL_STEPS_ROW_STARTING_NUMBER = 8;
	
	
	public int DEVICES_STATUS_COLUMN_MULTIPLIES_COUNT = 2;
	public int TESTCASE_EXCEPT_SCENARIO_SHEET_COUNT = 6;
	
	//Test case containing build details column number or row numbers
	public int TESTCASE_BUILD_DETAILS_ROW_NUMBER = 0;
	public int TESTCASE_BUILD_DETAILS_COLUMN_NUMBER = 8;
	public String TESTED_BY = "Ionixx";
	
	public int CONFIG_FILE_DEVICES_STARTING_COLUMN_NUMBER = 1;
	
	public String PASS = "Pass";
	public String FAIL = "Fail";
	public String DISPLAYED = "Displayed";
	public String NOT_DISPLAYED = "Not Displayed";
	public String ENABLED = "Enabled";
	public String NOT_ENABLED = "Not Enabled";
	public String YES = "Yes";
	public String NO = "No";
	
	
	//notification clear button id
	public String CLEAR_NOTIFICATION_ID = "com.android.systemui:id/clear_all_button";
	
	
	public String COMPONENT_NOT_DISPLAYED = "Component not displayed";
	public String COMPONENT_DISABLED = "Component is disabled";
	public String COMPONENT_NOT_DISABLED = "Component not disabled";
	public String COMPONENT_ENABLED = "Component is enabled";
	public String COMPONENT_NOT_ENABLED = "Component not enabled";
	public String COMPONENT_NOT_CLICKABLE = "Component not clickable";
	public String COMPONENT_IS_CLICKABLE = "Component is clickable";
	public String COMPONENT_IS_SCROLLABLE = "Component is scrollable";
	public String COMPONENT_NOT_SCROLLABLE = "Component not scrollable";
	public String COMPONENT_IS_CHECKED = "Component is checked";
	public String COMPONENT_NOT_CHECKED = "Component not checked";
	public String COMPONENT_IS_CHECKABLE = "Component is checkable";
	public String COMPONENT_NOT_CHECKABLE = "Component not checkable";
	
	
	public String SCREEN_SHOT_DIRECTORY_NAME = "capturedimages";
	public String CROPPED_SCREEN_SHOT_DIRECTORY_NAME = "croppedimages"; 
	
	public String DEVICE = "Device";
	public String DEVICE_DETAILS = "DeviceDetails";
	public String PHYSICAL_DEVICE = "Physical";
	public String EMULATOR_DEVICE = "Emulator";
	public String ALL_DEVICE = "All";
	
	public String EXECUTION_PLATFORM_FOR_MOBILE = "Android";
	public String EXECUTION_PLATFORM_FOR_IOS = "IOS";
	public String EXECUTION_PLATFORM_FOR_WEB = "Web";
	public String EXECUTE_TYPE_AS_SEQUENCE = "Sequence";
	public String EXECUTE_TYPE_AS_PARALLEL = "Parallel";
	
	public String DEVICE_IDENTIFIER = "device1";
	
	public String POSSIBLE_LOCATOR_TYPES = "id,class,xpath,link,name,tagName,partialLink,cssSelector";
	
}
