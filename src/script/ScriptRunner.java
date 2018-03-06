package script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import constant.CommonConstant;
import initialize.Setup;
import rwData.ReadData;
import rwData.WriteData;
import util.Utility;

public class ScriptRunner {

	public static Method method[];
	public static int totalPassedCount = 0;
	public static int totalFailedCount = 0;
	public static String outputFolderTime = null;
	public static HashMap<String, Object> driverMap = new HashMap<String, Object>();
	public static String outputTestCasePath = null;
	static ExtentReports extentReport;
	static ExtentTest extentTest;
	static Logger logger = null;
	
	@BeforeSuite
	@Parameters({ "deviceConfigFileName" })
	public void setup(String deviceConfigFileName) {
		try {

			Setup setup = new Setup();
			driverMap = setup.initialize(deviceConfigFileName);

			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			outputFolderTime = dateFormat.format(date);
			outputTestCasePath = Setup.commonConfig.get(CommonConstant.OUTPUT_TESTCASE_PATH);

			// Creating output folder
			new File(outputTestCasePath + File.separator + outputFolderTime).mkdir();

			extentReport = new ExtentReports(outputTestCasePath + File.separator + outputFolderTime + File.separator + "TestReport.html");

			//For Logging
			logger = Logger.getLogger("ScriptRunner");
			FileHandler fileHandler = new FileHandler(outputTestCasePath + File.separator + outputFolderTime + File.separator + "log.txt");
            logger.addHandler(fileHandler);
            
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            
            //to disable console logger
            logger.setUseParentHandlers(false);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Parameters({ "inputTestCaseName", "devicesToRun" })
	public static void executeTestCase(String inputTestCaseName, String devicesToRun) throws IOException {

		System.out.println("Looking device.........." + Thread.currentThread().getId());

		int testCaseStartNumber = CommonConstant.TESTCASE_ACTION_STARTING_ROW_NUMBER;
		int totalStepsRowNumber = CommonConstant.SUMMARY_SHEET_TOTAL_STEPS_ROW_STARTING_NUMBER;
		int exceptScenarioSheetCount = CommonConstant.TESTCASE_EXCEPT_SCENARIO_SHEET_COUNT;

		String sourceTestCase = null;
		String destinationTestCase = null;
		FileInputStream file = null;
		int passCount = 0;
		int failCount = 0;
		int preStepsSheetNumber = 0;

		HashMap<String, String> locatorMap = new HashMap<String, String>();
		HashMap<String, Object> resultCountMap = new HashMap<String, Object>();

		try {

			sourceTestCase = WriteData.copyTestCaseFiles(inputTestCaseName, outputTestCasePath, outputFolderTime);

			extentTest = extentReport.startTest(sourceTestCase);

			destinationTestCase = outputTestCasePath + File.separator + outputFolderTime + File.separator + sourceTestCase;

			file = new FileInputStream(new File(destinationTestCase));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			int sheetCount = workbook.getNumberOfSheets();

			for (int sheetNumber = 0; sheetNumber < sheetCount; sheetNumber++) {
				XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
				String sheetName = sheet.getSheetName();
				int lastRowNumber = sheet.getLastRowNum();

				if (sheetName.equals("LocatorMapping")) {
					locatorMap = ReadData.readExcelKeyValueForLocatorMapping(sheet);
				}

				if (!sheetName.equals("LocatorMapping") && !sheetName.equals("Summary")
						&& !(sheetName.equals("DataSheet"))) {

					resultCountMap = executeExcelAction(devicesToRun, locatorMap, testCaseStartNumber, lastRowNumber, sheetNumber, sheet, destinationTestCase);
				}

				// writing test case pass or fail count on summary sheet
				if (!(sheetName.equals("Summary")) && !(sheetName.equals("LocatorMapping"))
						&& !(sheetName.equals("DataSheet"))) {
					if (resultCountMap.containsKey("passCount") && resultCountMap.containsKey("failCount")) {

						if (resultCountMap.containsKey("passCount")) {
							passCount = (Integer) resultCountMap.get("passCount");
						} else {
							passCount = 0;
						}
						if (resultCountMap.containsKey("failCount")) {
							failCount = (Integer) resultCountMap.get("failCount");
						} else {
							failCount = 0;
						}

						WriteData.writeSummaryDetails(destinationTestCase, totalStepsRowNumber, passCount, failCount);

						totalStepsRowNumber += 1;
						totalPassedCount += passCount;
						totalFailedCount += failCount;
						passCount = 0;
						failCount = 0;
						if (preStepsSheetNumber > 0) {
							totalStepsRowNumber = totalStepsRowNumber + (preStepsSheetNumber - exceptScenarioSheetCount);
						}
					}
				}
				if (preStepsSheetNumber > 0) {
					sheetNumber = preStepsSheetNumber - 1;
					preStepsSheetNumber = 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			file.close();
			extentReport.endTest(extentTest);
		}
	}

	private static HashMap<String, Object> executeExcelAction(String deviceName, HashMap<String, String> locatorMap,
			int testCaseStartNumber, int lastRowNumber, int sheetNumber, XSSFSheet sheet, String destinationTestCase) {

		int descriptionColumnNumber = CommonConstant.TESTCASE_DESCRIPTION_COLUMN_NUMBER;
		int actionMethodColumnNumber = CommonConstant.TESTCASE_ACTION_COLUMN_NUMBER;
		int locatorColumnNumber = CommonConstant.TESTCASE_LOCATOR_COLUMN_NUMBER;
		int inputColumnNumber = CommonConstant.TESTCASE_INPUT_COLUMN_NUMBER;
		int expectedColumnNumber = CommonConstant.TESTCASE_EXPECTED_COLUMN_NUMBER;
		int statusCellNumber = CommonConstant.TESTCASE_STATUS_COLUMN_NUMBER;
		int actualMessageCellNumber = CommonConstant.TESTCASE_ACTUAL_COLUMN_NUMBER;

		String description= null;
		String actionMethodName = null;
		String componentLocator = null;
		String userInput = null;
		String expectedMessage = null;
		String actualMessage = null;
		String testCaseStatus = null;
		int trueSteps = 0;
		int falseSteps = 0;
		int ifConditionTrueSteps = 0;
		int passCount = 0;
		int failCount = 0;
		XSSFRow row = null;

		HashMap<String, String> inputParamMap = new HashMap<String, String>();
		HashMap<String, Object> resultCountMap = new HashMap<String, Object>();
		String sheetName = sheet.getSheetName();

		try {

			for (int rowNumber = testCaseStartNumber; rowNumber < lastRowNumber; rowNumber++) {
				description = actualMessage = userInput = expectedMessage = null;
				row = sheet.getRow(rowNumber);

				if (Utility.isNotEmpty(row.getCell(actionMethodColumnNumber).toString())) {

					actionMethodName = row.getCell(actionMethodColumnNumber).toString();

					if (Utility.isNotEmpty(row.getCell(descriptionColumnNumber).toString()))
						description = row.getCell(descriptionColumnNumber).toString();
					if (Utility.isNotEmpty(row.getCell(locatorColumnNumber).toString()))
						componentLocator = row.getCell(locatorColumnNumber).toString();

					if (Utility.isNotEmpty(row.getCell(inputColumnNumber).toString()))
						userInput = row.getCell(inputColumnNumber).toString();

					if (Utility.isNotEmpty(row.getCell(expectedColumnNumber).toString()))
						expectedMessage = row.getCell(expectedColumnNumber).toString();

					if (Utility.isNotNull(actionMethodName) && !actionMethodName.isEmpty()) {
						String componentValue = null;

						if (Utility.isNotNull(componentLocator) && !componentLocator.isEmpty()) {

							componentValue = getComponentValueFromMapping(locatorMap, destinationTestCase, sheetName, deviceName, componentLocator);

							System.out.println("componentValue ==> " + componentValue);
							inputParamMap.put("componentType", locatorMap.get(componentLocator).split("-")[0]);
							inputParamMap.put("componentValue", componentValue);
						}
						inputParamMap.put("testCaseName", destinationTestCase);
						inputParamMap.put("sheetName", sheetName);
						inputParamMap.put("actualDeviceName", driverMap.get(CommonConstant.DEVICE_NAME).toString());

						if (Utility.isNotNull(userInput)) {
							inputParamMap.put("userInput", userInput);
						}

						if (Utility.isNotNull(expectedMessage)) {
							inputParamMap.put("expectedMessage", expectedMessage);
						}

						logger.info("Current Execution Method  => "+actionMethodName);
						logger.info("Current method input  => "+userInput);
						
						// Invoking test case actions in the action class methods
						actualMessage = invokeAction(actionMethodName, deviceName, inputParamMap);

						logger.info("Current method expected   => "+expectedMessage);
						logger.info("Current method actual   => "+actualMessage);
						
						testCaseStatus = CommonConstant.PASS;
						if (!actionMethodName.equals("actionExecuteIfComponentTextMatching")
								&& !actionMethodName.equals("actionExecuteIfComponentDisplayed")
								&& !actionMethodName.equals("actionExecuteIfExpectedDeviceMatched")) {

							if (Utility.isNotNull(expectedMessage) && Utility.isNotNull(actualMessage)) {

								actualMessage = actualMessage.trim();
								expectedMessage = expectedMessage.trim();

								if (actionMethodName.contains("ExpectedContains")) {
									if (!(actualMessage.contains(expectedMessage))) {
										testCaseStatus = CommonConstant.FAIL;
										failCount = failCount + 1;

										extentTest.log(LogStatus.FAIL, description + " - [ Expected : "
												+ expectedMessage + " ," + " Actual : " + actualMessage + " ]");
									} else {
										actualMessage = expectedMessage;
										passCount = passCount + 1;

										extentTest.log(LogStatus.PASS, description + " - [ Expected : "
												+ expectedMessage + " ," + " Actual : " + actualMessage + " ]");
									}

								} else {
									if (!(actualMessage.equalsIgnoreCase(expectedMessage))
											|| actualMessage.equalsIgnoreCase(CommonConstant.COMPONENT_NOT_DISPLAYED)) {
										testCaseStatus = CommonConstant.FAIL;
										failCount = failCount + 1;

										extentTest.log(LogStatus.FAIL, description + " - [ Expected : "
												+ expectedMessage + " ," + " Actual : " + actualMessage + " ]");
									} else {
										passCount = passCount + 1;

										extentTest.log(LogStatus.PASS, description + " - [ Expected : "
												+ expectedMessage + " ," + " Actual : " + actualMessage + " ]");
									}
								}

							} else if (Utility.isNotNull(actualMessage)) {
								if (actualMessage.equalsIgnoreCase(CommonConstant.COMPONENT_NOT_DISPLAYED)) {
									testCaseStatus = CommonConstant.FAIL;
									failCount = failCount + 1;

									extentTest.log(LogStatus.FAIL, description);

								} else {
									// actualMessage = " ";
									passCount = passCount + 1;

									extentTest.log(LogStatus.PASS, description);
								}
							} else if (Utility.isNotNull(expectedMessage) && Utility.isNull(actualMessage)) {
								testCaseStatus = CommonConstant.FAIL;
								failCount = failCount + 1;

								extentTest.log(LogStatus.FAIL, description);
							} else {
								passCount = passCount + 1;

								extentTest.log(LogStatus.PASS, description);
							}
						} else {
							passCount = passCount + 1;
							extentTest.log(LogStatus.PASS, description);
						}
						resultCountMap.put("passCount", passCount);
						resultCountMap.put("failCount", failCount);

						// writing test case status against each step
						WriteData.writeResult(destinationTestCase, rowNumber, sheetNumber, statusCellNumber,
								actualMessageCellNumber, testCaseStatus, actualMessage);

						// Resetting status and actual message cell number
						statusCellNumber = CommonConstant.TESTCASE_STATUS_COLUMN_NUMBER;
						actualMessageCellNumber = CommonConstant.TESTCASE_ACTUAL_COLUMN_NUMBER;

						// Skipping test case steps based on if action
						if (actionMethodName.equals("actionExecuteIfComponentTextMatching")
								|| actionMethodName.equals("actionExecuteIfComponentDisplayed")
								|| actionMethodName.equals("actionExecuteIfExpectedDeviceMatched")) {
							String[] inputArray = userInput.split(",");
							trueSteps = Integer.parseInt(inputArray[0].trim().split("\\.")[0]);
							if (inputArray.length > 1) {
								falseSteps = Integer.parseInt(inputArray[1].trim().split("\\.")[0]);
							}
							if (!expectedMessage.trim().equalsIgnoreCase(actualMessage.trim())) {
								rowNumber += trueSteps;
								trueSteps = 0;
							}
						} else if (trueSteps != 0) {
							ifConditionTrueSteps += 1;

							if (ifConditionTrueSteps == trueSteps) {
								rowNumber += falseSteps;
								ifConditionTrueSteps = 0;
								falseSteps = 0;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			logger.info("Exception on executing current method => " +e.getMessage());
			e.printStackTrace();
		}
		return resultCountMap;
	}

	private static String invokeAction(String actionMethodName, String deviceName, HashMap<String, String> inputParamMap) {
		String actualMessage = null;
		String actionStarttime = " ";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
			String classNameToBeLoaded = "actions.Action";
			Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
			Object whatInstance = myClass.newInstance();
			method = myClass.getMethods();
			System.out.println("Currently executing method............" + actionMethodName + "  "
					+ Thread.currentThread().getId());
			Date date = new Date();

			for (int index = 0; index < method.length; index++) {

				if (method[index].getName().equals(actionMethodName)) {

					actionStarttime = dateFormat.format(date);

					inputParamMap.put("actionStarttime", actionStarttime);
					inputParamMap.put("deviceToExecute", deviceName);

					Method myMethod = myClass.getMethod(actionMethodName, new Class[] { HashMap.class, HashMap.class });
					actualMessage = (String) myMethod.invoke(whatInstance, new Object[] { driverMap, inputParamMap });

					if (Utility.isNotNull(actualMessage)) {
						actualMessage = actualMessage.trim();
					}
					break;
				}

			}

		} catch (Exception e) {
			logger.info("No such method exception => " +e.getMessage());
			e.printStackTrace();
		}
		return actualMessage;
	}

	private static String getComponentValueFromMapping(HashMap<String, String> locatorMap, String destinationTestCase, String sheetName, String deviceName, String componentLocator) {

		String componentValue = null;
		String[] locator = locatorMap.get(componentLocator).split("-");

		for (int locatorIndex = 1; locatorIndex < locator.length; locatorIndex++) {
			if (Utility.isNull(componentValue)) {
				componentValue = locator[locatorIndex];
			} else {
				componentValue = componentValue + "-" + locator[locatorIndex];
			}
		}
		return componentValue;
	}

	@AfterSuite
	public void tearDown() {

		try {
			extentReport.flush();
			Utility.sendMailForTestCaseResult(outputTestCasePath, outputFolderTime, totalPassedCount, totalFailedCount);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
