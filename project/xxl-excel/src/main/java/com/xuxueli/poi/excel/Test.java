package com.xuxueli.poi.excel;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<ShopDTO> shopDTOList = new ArrayList<ShopDTO>();
        for (int i = 0; i < 100; i++) {
            ShopDTO shop = new ShopDTO(i, "商户"+i);
            shopDTOList.add(shop);
        }

        ExcelExportUtil.export(shopDTOList);
    }


    @ExcelSheet(name = "商户列表", headColor = HSSFColor.GREEN.index)
    public static class ShopDTO{

        @ExcelField(name = "商户ID")
        private int shopId;

        @ExcelField(name = "商户名称")
        private String shopName;

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
    }

}
