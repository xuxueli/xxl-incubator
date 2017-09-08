package com.xuxueli.poi.excel;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    public static byte[] export(List<?> dataList){

        // data
        if (dataList==null || dataList.size()==0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data can not be empty.");
        }

        // sheet
        Class<?> sheetClass = dataList.get(0).getClass();
        ExcelSheet excelSheet = sheetClass.getAnnotation(ExcelSheet.class);
        String sheetName = (excelSheet!=null && excelSheet.name()!=null && excelSheet.name().trim().length()>0)?excelSheet.name().trim():dataList.get(0).getClass().getSimpleName();
        short headColor = (excelSheet!=null)?excelSheet.headColor():-1;

        // sheet field
        List<Field> fields = new ArrayList<Field>();
        if (sheetClass.getDeclaredFields()!=null && sheetClass.getDeclaredFields().length>0) {
            for (Field field: sheetClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
        }

        if (fields==null || fields.size()==0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data field can not be empty.");
        }

        // book
        Workbook book = new HSSFWorkbook();     // HSSFWorkbook=2003/xls、XSSFWorkbook=2007/xlsx
        Sheet sheet = book.createSheet(sheetName);

        // sheet header row
        CellStyle headStyle = null;
        if (headColor > 0) {
            headStyle = book.createCellStyle();
            Font failFont = book.createFont();
            failFont.setColor(headColor);
            headStyle.setFont(failFont);
        }

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String fieldName = (excelField!=null && excelField.name()!=null && excelField.name().trim().length()>0)?excelField.name():field.getName();

            Cell cellX = headRow.createCell(i, Cell.CELL_TYPE_STRING);
            if (headStyle != null) {
                cellX.setCellStyle(headStyle);
            }
            cellX.setCellValue(String.valueOf(fieldName));
        }

        // sheet data
        for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
            int rowIndex = dataIndex+1;
            Object rowData = dataList.get(dataIndex);

            Row rowX = sheet.createRow(rowIndex);

            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(rowData);

                    Cell cellX = rowX.createCell(i, Cell.CELL_TYPE_STRING);
                    cellX.setCellValue(String.valueOf(fieldValue));
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }


        // 数据导出
        FileOutputStream fileOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] result = null;
        try {
            // 生成本地Excel文件
            fileOutputStream = new FileOutputStream("/Users/xuxueli/Downloads/demo-sheet.xls");
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


}
