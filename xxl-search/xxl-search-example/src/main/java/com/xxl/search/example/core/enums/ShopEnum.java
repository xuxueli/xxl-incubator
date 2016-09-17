package com.xxl.search.example.core.enums;

import com.xxl.search.example.core.model.ShopDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuxueli on 16/9/17.
 */
public class ShopEnum {

    /**
     * 城市字典
     */
    public enum CityEnum{
        BEIJING(1, "北京"),
        SHNGHAI(2, "上海"),
        GUANGZHOU(3, "深圳");

        public final int cityid;
        public final String cityname;
        CityEnum(int cityid, String cityname) {
            this.cityid = cityid;
            this.cityname = cityname;
        }
    }

    /**
     * 标签字典
     */
    public enum TagEnum{
        TAG01(1, "地铁沿线"),
        TAG02(2, "独立门面"),
        TAG03(3, "西式"),
        TAG04(4, "中式"),
        TAG05(5, "草坪"),
        TAG06(6, "免费WIFI"),
        TAG07(7, "免费停车");
        public final int tagid;
        public final String tagname;
        TagEnum(int tagid, String tagname) {
            this.tagid = tagid;
            this.tagname = tagname;
        }
    }

    // 初始化MOCK数据
    public static List<ShopDTO> shoplist = new ArrayList<ShopDTO>();
    static {
        shoplist.add(new ShopDTO(100001, "和平饭店", CityEnum.BEIJING.cityid, Arrays.asList(TagEnum.TAG01.tagid, TagEnum.TAG02.tagid), 100, 90));
        shoplist.add(new ShopDTO(100002, "香格里拉酒店", CityEnum.BEIJING.cityid, Arrays.asList(TagEnum.TAG01.tagid, TagEnum.TAG02.tagid), 95, 70));
        shoplist.add(new ShopDTO(100003, "希尔顿酒店", CityEnum.BEIJING.cityid, Arrays.asList(TagEnum.TAG01.tagid, TagEnum.TAG02.tagid), 90, 60));
        shoplist.add(new ShopDTO(100004, "小南国花园酒店", CityEnum.BEIJING.cityid, Arrays.asList(TagEnum.TAG03.tagid, TagEnum.TAG04.tagid), 85, 100));
        shoplist.add(new ShopDTO(100005, "丽思卡尔顿酒店", CityEnum.SHNGHAI.cityid, Arrays.asList(TagEnum.TAG03.tagid, TagEnum.TAG04.tagid), 80, 30));
        shoplist.add(new ShopDTO(100006, "绅公馆", CityEnum.SHNGHAI.cityid, Arrays.asList(TagEnum.TAG03.tagid, TagEnum.TAG04.tagid), 75, 20));
        shoplist.add(new ShopDTO(100007, "新天地朗庭酒店", CityEnum.SHNGHAI.cityid, Arrays.asList(TagEnum.TAG05.tagid, TagEnum.TAG06.tagid), 70, 10));
        shoplist.add(new ShopDTO(100008, "四季酒店", CityEnum.SHNGHAI.cityid, Arrays.asList(TagEnum.TAG05.tagid, TagEnum.TAG06.tagid), 65, 45));
        shoplist.add(new ShopDTO(100009, "皇冠假日酒店", CityEnum.GUANGZHOU.cityid, Arrays.asList(TagEnum.TAG05.tagid, TagEnum.TAG06.tagid), 60, 35));
        shoplist.add(new ShopDTO(100010, "峰味香黄焖鸡米饭", CityEnum.GUANGZHOU.cityid, Arrays.asList(TagEnum.TAG07.tagid), 55, 15));
        shoplist.add(new ShopDTO(100011, "沙县小吃", CityEnum.GUANGZHOU.cityid, Arrays.asList(TagEnum.TAG07.tagid), 50, 95));
    }

}
