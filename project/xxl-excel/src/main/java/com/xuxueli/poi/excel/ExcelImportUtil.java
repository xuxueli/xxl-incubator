package com.xuxueli.poi.excel;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ExcelImportUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelImportUtil.class);

    /**
     * 解析Excel Demo
     * @throws IOException
     * @throws InvalidFormatException
     * @throws EncryptedDocumentException
     */
    public static void importExcelDemo() throws EncryptedDocumentException, InvalidFormatException, IOException{
        File uploadXlsFile = new File("/Users/xuxueli/Downloads/demo-sheet.xlsx");

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

}
