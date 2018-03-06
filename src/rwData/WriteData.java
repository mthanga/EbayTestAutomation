package rwData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import constant.CommonConstant;
import util.Utility;

public class WriteData {
	
	public static String outputToMailAddress = null;
	
	public static void writeResult(String testCaseName, int rowNumber, int sheetNumber,  int statusCellNumber, int actualMessageCellNumber, String testCaseStatus, String actualMessage) {
		
		FileInputStream inputStream;
		FileOutputStream outputStream;
		try {
			inputStream = new FileInputStream(new File(testCaseName));
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
			
			XSSFRow row = sheet.getRow(rowNumber);
			
			row.getCell(statusCellNumber).setCellValue(testCaseStatus);
			row.getCell(actualMessageCellNumber).setCellValue(actualMessage);
			
			outputStream = new FileOutputStream(new File(testCaseName));
			
			inputStream.close();
			workbook.setForceFormulaRecalculation(true);
			workbook.write(outputStream);
			outputStream.close();
			
		} catch (Exception e) {
			writeTestCaseError(testCaseName, null, null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void writeSummaryDetails(String testCaseName, int rowNumber, int passCount, int failCount) {
		
		int totalStepsColumnNumber = CommonConstant.SUMMARY_SHEET_TOTAL_STEPS_COLUMN_STARTING_NUMBER;
		int totalSteps = passCount+failCount;
		
		FileInputStream inputStream;
		FileOutputStream outputStream;
		try {
			
			inputStream = new FileInputStream(new File(testCaseName));
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			outputStream = new FileOutputStream(new File(testCaseName));
			XSSFRow row = sheet.getRow(rowNumber);
			row.getCell(totalStepsColumnNumber).setCellValue(totalSteps);
			row.getCell(totalStepsColumnNumber+1).setCellValue(passCount);
			row.getCell(totalStepsColumnNumber+2).setCellValue(failCount);
			inputStream.close();
			workbook.setForceFormulaRecalculation(true);
			workbook.write(outputStream);
			outputStream.close();
			
		} catch (Exception e) {
			writeTestCaseError(testCaseName, "Summary", null, e.getMessage());
			e.printStackTrace();
		}
	}
	

    public static String copyTestCaseFiles(String sourceTestCaseName, String destinationTestCasePath, String folderNameTime){
    	
    	String[] sourcePathArray = sourceTestCaseName.split("/");
    	String destFileName = sourcePathArray[sourcePathArray.length-1];
    	
    	File source = new File(sourceTestCaseName);
		File dest = new File(destinationTestCasePath+File.separator+folderNameTime+File.separator+destFileName);	
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        is.close();
	        os.close();
	        
	    }catch(Exception e){
	    	writeTestCaseError(dest.getName(), null, null, e.getMessage());
	    }
	    
    	return destFileName;
    }
    
    
    public static void writeTestCaseError(String testCaseName, String sheetName, String deviceName, String errorMessage){
    	FileInputStream inputStream;
		FileOutputStream outputStream;
		try {
			Thread.sleep(1000);
			inputStream = new FileInputStream(new File(testCaseName));
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheet("DataSheetError");
			int lastRowNumber = sheet.getLastRowNum();
			
			for(int rowNumber=1; rowNumber<lastRowNumber; rowNumber++){
				XSSFRow row = sheet.getRow(rowNumber);
				if(row.getCell(0).toString().isEmpty()){
					if(Utility.isNotNull(sheetName)){
						row.getCell(0).setCellValue(sheetName);
					}
					if(Utility.isNotNull(deviceName)){
						row.getCell(1).setCellValue(deviceName);
					}
					if(Utility.isNotNull(errorMessage)){
						row.getCell(2).setCellValue(errorMessage);
					}
					break;
				}
			}
			
			outputStream = new FileOutputStream(new File(testCaseName));
			inputStream.close();
			workbook.setForceFormulaRecalculation(true);
			workbook.write(outputStream);
			outputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
}
