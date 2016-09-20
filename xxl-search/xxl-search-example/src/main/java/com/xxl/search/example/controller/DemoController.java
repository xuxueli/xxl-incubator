package com.xxl.search.example.controller;

import com.xxl.search.client.lucene.LuceneSearchResult;
import com.xxl.search.client.lucene.LuceneUtil;
import com.xxl.search.example.core.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by xuxueli on 16/7/30.
 */

@Controller
public class DemoController {
    private static Logger logger = LogManager.getLogger(DemoController.class.getName());

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("")
    public String index (Model model, Integer cityid){
        // 搜索
        List<Query> querys = new ArrayList<>();
        if (cityid!=null) {
            querys.add(new TermQuery(new Term(ShopDTO.ShopParam.CITY_ID, cityid+"")));
        }

        Sort sort = new Sort(new SortField(ShopDTO.ShopParam.SCORE, SortField.Type.SCORE));

        LuceneSearchResult result = LuceneUtil.search(querys, sort, 0, 20);
        model.addAttribute("result", result);

        // 原始数据
        model.addAttribute("shopOriginMapVal", shopOriginMap.values());
        model.addAttribute("cityEnum", ShopDTO.CityEnum.values());
        model.addAttribute("tagEnum", ShopDTO.TagEnum.values());
        model.addAttribute("cityid", cityid);
        return "demo";
    }

    // ---------------------- 原始数据, 索引操作 ----------------------
    /**
     * 初始化MOCK数据
     */
    public static Map<Integer, ShopDTO> shopOriginMap = new HashMap<Integer, ShopDTO>();
    static {
        shopOriginMap.put(100001, new ShopDTO(100001, "和平饭店", ShopDTO.CityEnum.BEIJING.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 100, 90));
        shopOriginMap.put(100002, new ShopDTO(100002, "香格里拉酒店", ShopDTO.CityEnum.BEIJING.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 95, 70));
        shopOriginMap.put(100003, new ShopDTO(100003, "希尔顿酒店", ShopDTO.CityEnum.BEIJING.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 90, 60));
        shopOriginMap.put(100004, new ShopDTO(100004, "小南国花园酒店", ShopDTO.CityEnum.BEIJING.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 85, 100));
        shopOriginMap.put(100005, new ShopDTO(100005, "丽思卡尔顿酒店", ShopDTO.CityEnum.SHNGHAI.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 80, 30));
        shopOriginMap.put(100006, new ShopDTO(100006, "绅公馆", ShopDTO.CityEnum.SHNGHAI.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 75, 20));
        shopOriginMap.put(100007, new ShopDTO(100007, "新天地朗庭酒店", ShopDTO.CityEnum.SHNGHAI.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 70, 10));
        shopOriginMap.put(100008, new ShopDTO(100008, "四季酒店", ShopDTO.CityEnum.SHNGHAI.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 65, 45));
        shopOriginMap.put(100009, new ShopDTO(100009, "皇冠假日酒店", ShopDTO.CityEnum.GUANGZHOU.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 60, 35));
        shopOriginMap.put(100010, new ShopDTO(100010, "峰味香黄焖鸡米饭", ShopDTO.CityEnum.GUANGZHOU.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 55, 15));
        shopOriginMap.put(100011, new ShopDTO(100011, "沙县小吃", ShopDTO.CityEnum.GUANGZHOU.cityid, Arrays.asList(ShopDTO.TagEnum.TAG01.tagid), 50, 95));
    }

    /**
     * 清空索引库
     * @return
     */
    @RequestMapping("/deleteAllIndex")
    @ResponseBody
    public String deleteAllIndex (){
        boolean ret = LuceneUtil.deleteAll();
        return ret?"S":"E";
    }

    /**
     * 全量索引
     * @return
     */
    @RequestMapping("/createAllIndex")
    @ResponseBody
    public String createAllIndex (){
        boolean ret = true;
        for (Map.Entry<Integer, ShopDTO> item: shopOriginMap.entrySet()) {
            boolean temp = addDocument(item.getValue());
            if (!temp) {
                ret = false;
            }
        }
        return ret?"S":"E";
    }

    public boolean addDocument(ShopDTO shopDTO){
        Document document = new Document();

        document.add(new IntField(ShopDTO.ShopParam.SHOP_ID, shopDTO.getShopid(), Field.Store.YES));
        document.add(new TextField(ShopDTO.ShopParam.SHOP_NAME, shopDTO.getShopname(), Field.Store.YES));
        document.add(new StringField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid()+"", Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.SCORE, shopDTO.getScore(), LuceneUtil.INT_FIELD_TYPE_STORED_SORTED));
        document.add(new IntField(ShopDTO.ShopParam.HOT_SCORE, shopDTO.getHotscore(), LuceneUtil.INT_FIELD_TYPE_STORED_SORTED));

        boolean ret = LuceneUtil.addDocument(document, true);
        return ret;
    }

    /**
     * 新增/覆盖, 一条索引
     * @return
     */
    @RequestMapping("/createDocument")
    @ResponseBody
    public Object createDocument (Integer shopid, String shopname, Integer cityid, String taglist, Integer score, Integer hotscore){

        // shopid
        if (shopid==null || shopid<=0) {
            return "shopid error";
        }
        // shopname
        if (shopname==null || shopname.trim().length()==0) {
            return "shopname error";
        }
        // cityid check
        if (cityid==null || ShopDTO.CityEnum.match(Integer.valueOf(cityid))==null) {
            return "cityid error";
        }
        // taglist check
        List<Integer> taglistReal = null;
        if (taglist!=null) {
            taglistReal = new ArrayList<Integer>();
            String[] tagids = taglist.split(",");
            for (String tagIdStr: tagids) {
                if (ShopDTO.TagEnum.match(Integer.valueOf(tagIdStr))!=null) {
                    taglistReal.add(Integer.valueOf(tagIdStr));
                }
            }
        }
        // score
        if (score==null) {
            return "score error";
        }
        // hotscore
        if (hotscore==null) {
            return "hotscore error";
        }

        // 原始数据操作
        ShopDTO shopDTO = new ShopDTO(shopid, shopname, cityid, taglistReal, score, hotscore);
        shopOriginMap.put(shopDTO.getShopid(), shopDTO);

        // 索引操作
        boolean ret = addDocument(shopDTO);

        return ret?"S":"E";
    }

    /**
     * 删除, 一条索引
     * @return
     */
    @RequestMapping("/deleteDocument")
    @ResponseBody
    public String deleteDocument (int shopid){

        // 原始数据操作
        shopOriginMap.remove(shopid);

        // 索引操作
        boolean ret = LuceneUtil.deleteDocument(new Term(ShopDTO.ShopParam.SHOP_ID, shopid+""));

        return ret?"S":"E";
    }

}
