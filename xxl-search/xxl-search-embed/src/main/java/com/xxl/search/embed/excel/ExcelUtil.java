package com.xxl.search.embed.excel;

import com.xxl.search.embed.lucene.LuceneUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * Created by xuxueli on 16/9/22.
 */
public class ExcelUtil {

    public static final String TEMPLATE_NAME = "search-template.xls";
    public static final String SEARCH_FS = "search_fs";

    public static void generateTemplate(File directoryFile) throws IOException {


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("search-template");

        {
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell0 = row.createCell(0);
            HSSFCell cell1 = row.createCell(1);
            HSSFCell cell2 = row.createCell(2);



            cell0.setCellValue(LuceneUtil.SearchDto.ID);
            cell1.setCellValue(LuceneUtil.SearchDto.TITLE);
            cell2.setCellValue(LuceneUtil.SearchDto.KEYWORD);
        }
        {
            HSSFRow row = sheet.createRow(1);
            HSSFCell cell0 = row.createCell(0);
            HSSFCell cell1 = row.createCell(1);
            HSSFCell cell2 = row.createCell(2);

            cell0.setCellValue("索引数据的主键ID【int类型】");
            cell1.setCellValue("索引数据的标题【String类型】");
            cell2.setCellValue("索引数据的关键词,将会被分词供搜索使用【String类型】");
        }

        File templateFile = new File(directoryFile, ExcelUtil.TEMPLATE_NAME);
        FileOutputStream os = new FileOutputStream(templateFile);
        workbook.write(os);
        os.close();
    }

    public static void createIndexByTemplate(File templateFile, File directoryFile) throws IOException {
        InputStream is = new FileInputStream(templateFile);
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheet("search-template");

        HSSFCellStyle successStyle = workbook.createCellStyle();
        successStyle.setFillBackgroundColor(HSSFColor.GREEN.index);

        HSSFCellStyle failStyle = workbook.createCellStyle();
        failStyle.setFillBackgroundColor(HSSFColor.GREEN.index);

        List<LuceneUtil.SearchDto> list = new ArrayList<>();

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            HSSFRow row = sheet.getRow(i);

            HSSFCellStyle celltype = failStyle;
            HSSFCell cell0 =row.getCell(0);
            HSSFCell cell1 =row.getCell(1);
            HSSFCell cell2 =row.getCell(2);

            try {
                if ((cell0!=null && cell1!=null && cell2!=null)
                        && (!LuceneUtil.SearchDto.ID.equals(cell0.getStringCellValue())) ) {
                    cell0.setCellType(CELL_TYPE_STRING);
                    cell1.setCellType(CELL_TYPE_STRING);
                    cell2.setCellType(CELL_TYPE_STRING);

                    int id = (cell0!=null) ? Integer.valueOf(cell0.getStringCellValue()) : -1;
                    String title = (cell1!=null) ? cell1.getStringCellValue() : null;
                    String keyword = (cell2!=null) ? cell2.getStringCellValue() : null;

                    if (id!=-1 && title!=null && keyword!=null) {
                        list.add(new LuceneUtil.SearchDto(id, title, keyword));
                        celltype = successStyle;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (cell0!=null) {
                cell0.setCellStyle(celltype);
            }
            if (cell1!=null) {
                cell1.setCellStyle(celltype);
            }
            if (cell2!=null) {
                cell2.setCellStyle(celltype);
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
        }

    }

}
