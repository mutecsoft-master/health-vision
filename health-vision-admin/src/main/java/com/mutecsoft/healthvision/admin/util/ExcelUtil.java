package com.mutecsoft.healthvision.admin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.mutecsoft.healthvision.admin.constant.HealthExcelHeaderMeta;

@Component
public class ExcelUtil {
	
	public <T extends Enum<T> & HealthExcelHeaderMeta> byte[] healthDataListToExcel(
	        T[] excelHeaderEnumValues, List<Map<String, Object>> dataList) throws IOException {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			 Workbook workbook = new XSSFWorkbook()){

	        Sheet sheet = workbook.createSheet("Sheet");

	        // Header
	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < excelHeaderEnumValues.length; i++) {
	        	if(excelHeaderEnumValues[i].getWidth() != null) {
	        		sheet.setColumnWidth(i, excelHeaderEnumValues[i].getWidth() * 256);
	        	}
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(excelHeaderEnumValues[i].getHeader());
	        }

	        // Data
	        for (int i = 0; i < dataList.size(); i++) {
	            Row row = sheet.createRow(i + 1);
	            Map<String, Object> rowData = dataList.get(i);
	            for (int j = 0; j < excelHeaderEnumValues.length; j++) {
	                Object value = rowData.get(excelHeaderEnumValues[j].getKey());
	                row.createCell(j).setCellValue(value != null ? value.toString() : "");
	            }
	        }
	        
	        workbook.write(out);
	        return out.toByteArray();
	    }
	}
	
}
