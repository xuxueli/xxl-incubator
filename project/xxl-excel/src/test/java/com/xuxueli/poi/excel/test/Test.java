package com.xuxueli.poi.excel.test;

import com.xuxueli.poi.excel.ExcelExportUtil;
import com.xuxueli.poi.excel.ExcelImportUtil;
import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxueli 2017-09-08 22:41:19
 */
public class Test {

    public static void main(String[] args) {
        // data
        List<ShopDTO> shopDTOList = new ArrayList<ShopDTO>();
        for (int i = 0; i < 100; i++) {
            ShopDTO shop = new ShopDTO(i, "商户"+i);
            shopDTOList.add(shop);
        }
        // file path
        String filePath = "/Users/xuxueli/Downloads/demo-sheet.xls";

        // Excel导出
        ExcelExportUtil.exportToFile(shopDTOList, filePath);

        // Excel导入
        List<Object> list = ExcelImportUtil.importExcel(ShopDTO.class, filePath);
        System.out.println(list);


    }


    @ExcelSheet(name = "商户列表", headColor = HSSFColor.GREEN.index)
    public static class ShopDTO{

        @ExcelField(name = "商户ID")
        private int shopId;

        @ExcelField(name = "商户名称")
        private String shopName;

        public ShopDTO() {
        }

        public ShopDTO(int shopId, String shopName) {
            this.shopId = shopId;
            this.shopName = shopName;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        @Override
        public String toString() {
            return "ShopDTO{" +
                    "shopId=" + shopId +
                    ", shopName='" + shopName + '\'' +
                    '}';
        }

    }

}
