package com.xxl.search.example.service.impl;

import com.xxl.search.client.es.ElasticsearchResult;
import com.xxl.search.client.es.ElasticsearchUtil;
import com.xxl.search.client.es.JacksonUtil;
import com.xxl.search.example.core.model.ShopDTO;
import com.xxl.search.example.service.IXxlSearchService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/21.
 */
@Service("eSSearchServiceImpl")
public class ESSearchServiceImpl implements IXxlSearchService {

    public static final String index = "demo-index";
    public static final String type = "shop";

    @Override
    public boolean deleteAll() {
        ElasticsearchUtil.bulkDelete(index, type);
        return true;
    }

    @Override
    public boolean addDocument(ShopDTO shopDTO) {
        Map<String, Object> source = buildSource(shopDTO);
        String sourceJson = JacksonUtil.writeValueAsString(source);

        IndexResponse indexResponse = ElasticsearchUtil.prepareIndex(index, type, String.valueOf(shopDTO.getShopid()), sourceJson);
        return true;
    }

    private Map<String, Object> buildSource(ShopDTO shopDTO){
        Map<String, Object> source = new HashMap<>();
        source.put(ShopDTO.ShopParam.SHOP_ID, shopDTO.getShopid());
        source.put(ShopDTO.ShopParam.SHOP_NAME, shopDTO.getShopname());     // 分词
        source.put(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid());
        source.put(ShopDTO.ShopParam.TAG_ID, shopDTO.getTaglist());
        source.put(ShopDTO.ShopParam.SCORE, shopDTO.getScore());
        source.put(ShopDTO.ShopParam.HOT_SCORE, shopDTO.getHotscore());
        return source;
    }

    @Override
    public boolean updateDocument(ShopDTO shopDTO) {
        Map<String, Object> source = buildSource(shopDTO);
        String sourceJson = JacksonUtil.writeValueAsString(source);

        UpdateResponse updateResponse = ElasticsearchUtil.prepareUpdate(index, type, String.valueOf(shopDTO.getShopid()), sourceJson);
        return true;
    }

    @Override
    public boolean deleteDocument(int shopid) {
        ElasticsearchUtil.prepareDelete(index, type, String.valueOf(shopid));
        return false;
    }

    @Override
    public Map<String, Object> search(int offset, int pagesize, String shopname, List<Integer> cityids, List<Integer> tagids, int sortType) {
        List<QueryBuilder> queryBuilders = new ArrayList<>();

        // shopname
        if (shopname!=null && shopname.trim().length()>0) {
            queryBuilders.add(QueryBuilders.fuzzyQuery(ShopDTO.ShopParam.SHOP_NAME, shopname));
        }

        // cityids
        if (cityids!=null && cityids.size() > 0) {
            queryBuilders.add(QueryBuilders.termsQuery(ShopDTO.ShopParam.CITY_ID, cityids));
        }

        // tagids
        if (tagids!=null && tagids.size() > 0) {
            queryBuilders.add(QueryBuilders.termsQuery(ShopDTO.ShopParam.TAG_ID, tagids));
        }

        // sort
        SortBuilder sort = null;
        if (sortType==1) {
            sort = SortBuilders.fieldSort(ShopDTO.ShopParam.HOT_SCORE).order(SortOrder.DESC);
        } else {
            sort = SortBuilders.fieldSort(ShopDTO.ShopParam.SCORE).order(SortOrder.DESC);
        }

        // result
        ElasticsearchResult elasticsearchResult = ElasticsearchUtil.prepareSearch(index, type, queryBuilders, sort, offset, pagesize);

        Map<String, Object> retMap = new HashMap();
        retMap.put("total", elasticsearchResult.getTotalHits());
        retMap.put("data", elasticsearchResult.getSources());
        return retMap;
    }
}
