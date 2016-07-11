package com.xxl.util.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel 操作工具类
 * @author xuxueli 2016-7-11 21:11:52
 */
public class PoiUtil {
	private static Logger logger = LoggerFactory.getLogger(PoiUtil.class);
	
	/**
	 * 生成Excel Demo
	 * @param banquetInfoList
	 * @param banquetHallList
	 * @return
	 */
	public static byte[] exportExcelDemo(){
        Workbook book = new HSSFWorkbook();	// 创建book
        
        Sheet banquetInfoSheet = book.createSheet("demo-sheet");	// banquetInfo
        
        // 红色CellStyle
		CellStyle redStyle = book.createCellStyle();
		Font failFont = book.createFont();
		failFont.setColor(HSSFColor.RED.index);
		redStyle.setFont(failFont);
        
		// 生成Excel一张Sheet
        for (int i = 0; i < 5; i++) {
        	Row rowX = banquetInfoSheet.createRow(i);
        	for (int j = 0; j < 10; j++) {
        		Cell cellX = rowX.createCell(j, Cell.CELL_TYPE_STRING);
        		if (i==0) {
        			cellX.setCellStyle(redStyle);
				}
        		cellX.setCellValue(String.valueOf(i).concat(String.valueOf(j)));
			}
		}
		
        // 数据导出
        FileOutputStream fileOutputStream = null;
     	ByteArrayOutputStream byteArrayOutputStream = null;
     	byte[] result = null;
        try {
        	// 生成本地Excel文件
        	fileOutputStream = new FileOutputStream("/demo-sheet.xlsx");
        	book.write(fileOutputStream);
        	fileOutputStream.flush();
        	
        	// 生成ByteArrayInputStream
        	byteArrayOutputStream = new ByteArrayOutputStream();
			book.write(byteArrayOutputStream);
			byteArrayOutputStream.flush();
			
			result = byteArrayOutputStream.toByteArray();
			//ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			logger.error("export excel error", e);
		} finally {
			try {
				if (fileOutputStream!=null) {
					fileOutputStream.close();
				}
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (Exception e) {
				logger.error("close outputStream err", e);
			}
		}
        
		return result;
	}
	
	/**
	 * 解析Excel Demo
	 */
	public static void importExcelDemo(){
		File uploadXlsFile = new File("/demo-sheet.xlsx");
		
		Workbook book = WorkbookFactory.create(uploadXlsFile);
		Sheet demoSheet = book.getSheet("demo-sheet");
		Iterator<Row> demoSheetIterator = demoSheet.rowIterator();
		while (demoSheetIterator.hasNext()) {
			Row rowX = demoSheetIterator.next();
			System.out.println("---------------------------");
			for (int i = 0; i < 10; i++) {
				System.out.println(rowX.getCell(i));
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		exportExcelDemo();
		importExcelDemo();
	}
}
