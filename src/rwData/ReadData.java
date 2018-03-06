package rwData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import constant.CommonConstant;

public class ReadData {
	
	
	public static HashMap<String, String> readExcelKeyValueConfig(XSSFSheet sheet) throws IOException {
		 
		HashMap<String, String> locatorMap = new HashMap<String, String>();
		
		try{
			
			 Iterator rows = sheet.rowIterator();
	            while (rows.hasNext()) {
	                XSSFRow row = (XSSFRow) rows.next();
	                Iterator cells = row.cellIterator();

	                List data = new ArrayList();
	                while (cells.hasNext()) {
	                    XSSFCell cell = (XSSFCell) cells.next();
	                    data.add(cell);
	                }
	                
	                if(!data.get(0).toString().isEmpty()){
	                	locatorMap.put(data.get(0).toString(), data.get(1).toString());
	                }
	                
	            }
		 }
		 catch (Exception e) 
	     {
	         e.printStackTrace();
	     }
		return locatorMap;
	    }
	
	public static HashMap<String, String> readExcelKeyValueForLocatorMapping(XSSFSheet sheet) throws IOException {
		 
		HashMap<String, String> locatorMap = new HashMap<String, String>();
		
		try{
			int lastRowNumber = sheet.getLastRowNum();
			XSSFRow row;
			String mappingName;
			String mappingValue;
			for(int rowNumber=1; rowNumber<=lastRowNumber; rowNumber++){
				
				row = sheet.getRow(rowNumber);
				
				if(row.getCell(0).toString() != "" && row.getCell(1).toString() != ""){
				
					mappingName = row.getCell(0).toString();
					mappingValue = row.getCell(1).getStringCellValue();
	                
	                locatorMap.put(mappingName, mappingValue);
				}	
			}
			
		 }
		 catch (Exception e) 
	     {
			e.printStackTrace();
	         
	     }
		return locatorMap;
	    }
	
	
}
