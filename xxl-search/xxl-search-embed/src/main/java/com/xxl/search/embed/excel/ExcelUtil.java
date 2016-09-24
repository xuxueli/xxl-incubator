package com.xxl.search.embed.excel;

import com.xxl.search.embed.lucene.LuceneUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;

import java.io.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * Created by xuxueli on 16/9/22.
 */
public class ExcelUtil {

    public static final String KEYWORDS = "keywords";
    public static final String TEMPLATE_NAME = "search-template.xls";
    public static final String SEARCH_FS = "search_fs";

    public static void generateTemplate(File directoryFile, LinkedHashSet<String> fields) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("search-template");

        // field set
        HSSFRow row0 = sheet.createRow(0);
        int index = 0;
        for (String field: fields) {
            HSSFCell cellX = row0.createCell(index++);
            cellX.setCellValue(field);
        }

        File templateFile = new File(directoryFile, ExcelUtil.TEMPLATE_NAME);
        FileOutputStream os = new FileOutputStream(templateFile);
        workbook.write(os);
        os.close();
    }

    public static void createIndexByTemplate(File templateFile, File directoryFile, LinkedHashSet<String> fields) throws IOException {
        InputStream is = new FileInputStream(templateFile);
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheet("search-template");

        // color
        HSSFCellStyle successStyle = workbook.createCellStyle();
        Font successFont = workbook.createFont();
        successFont.setColor(HSSFColor.GREEN.index);
        successStyle.setFont(successFont);

        HSSFCellStyle failStyle = workbook.createCellStyle();
        Font failFont = workbook.createFont();
        failFont.setColor(HSSFColor.RED.index);
        failStyle.setFont(failFont);

        // result
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            // each line
            HSSFRow row = sheet.getRow(i);

            Map<String, String> itemMap = new HashMap<>();

            // field 2 map
            int index = 0;
            for (String field: fields) {
                HSSFCell cellX = (row.getCell(index)!=null) ? row.getCell(index) : row.createCell(index, HSSFCell.CELL_TYPE_STRING);
                cellX.setCellType(CELL_TYPE_STRING);

                String fieldVal = cellX.getStringCellValue();
                itemMap.put(field, fieldVal);
                index++;
            }

            // status
            HSSFCellStyle cellStyle = failStyle;
            if (itemMap.get(KEYWORDS)==null || itemMap.get(KEYWORDS).equals(KEYWORDS)){
                cellStyle = failStyle;
            } else {
                cellStyle = successStyle;
                list.add(itemMap);
            }

            // mark status
            index = 0;
            for (String field: fields) {
                HSSFCell cellX = (row.getCell(index)!=null) ? row.getCell(index) : row.createCell(index, HSSFCell.CELL_TYPE_STRING);
                cellX.setCellStyle(cellStyle);
                index++;
            }
        }

        FileOutputStream os = new FileOutputStream(templateFile);
        workbook.write(os);
        os.close();

        workbook.close();
        is.close();

        if (list.size()>0) {
            // LuceneUtil
            LuceneUtil.setDirectory(directoryFile.getPath() + "/" + SEARCH_FS);
            LuceneUtil.createIndex(list);
            LuceneUtil.destory();
        } else {
            throw new RuntimeException("生成索引文件失败, 索引数据为空");
        }

    }

}
