package com.xxl.search.example.service.impl;

import com.xxl.search.client.lucene.response.SearchResult;
import com.xxl.search.client.lucene.LuceneUtil;
import com.xxl.search.example.core.model.ShopDTO;
import com.xxl.search.example.service.IXxlSearchService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/20.
 */
@Service("luceneSearchServiceImpl")
public class LuceneSearchServiceImpl implements IXxlSearchService {

    @Override
    public boolean deleteAll() {
        return LuceneUtil.deleteAll();
    }

    @Override
    public boolean addDocument(ShopDTO shopDTO) {

        Document document = buildDucument(shopDTO);

        boolean ret = LuceneUtil.addDocument(document, true);
        return ret;
    }

    private Document buildDucument(ShopDTO shopDTO){
        Document document = new Document();

        document.add(new IntField(ShopDTO.ShopParam.SHOP_ID, shopDTO.getShopid(), Field.Store.YES));
        document.add(new TextField(ShopDTO.ShopParam.SHOP_NAME, shopDTO.getShopname(), Field.Store.YES));
        //document.add(new StringField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid()+"", Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid(), Field.Store.YES));
        if (shopDTO.getTaglist()!=null && shopDTO.getTaglist().size()>0) {
            for (int tagid: shopDTO.getTaglist()) {
                document.add(new IntField(ShopDTO.ShopParam.TAG_ID, tagid, Field.Store.YES));
            }
        }
        document.add(new IntField(ShopDTO.ShopParam.SCORE, shopDTO.getScore(), LuceneUtil.INT_FIELD_TYPE_STORED_SORTED));
        document.add(new IntField(ShopDTO.ShopParam.HOT_SCORE, shopDTO.getHotscore(), LuceneUtil.INT_FIELD_TYPE_STORED_SORTED));

        return document;
    }

    @Override
    public boolean updateDocument(ShopDTO shopDTO) {

        Document document = buildDucument(shopDTO);

        BytesRefBuilder bytes = new BytesRefBuilder();
        NumericUtils.intToPrefixCoded(shopDTO.getShopid(), 0, bytes);
        Term term = new Term(ShopDTO.ShopParam.SHOP_ID, bytes);

        boolean ret = LuceneUtil.updateDocument(term, document);
        return ret;
    }

    @Override
    public boolean deleteDocument(int shopid) {
        BytesRefBuilder bytes = new BytesRefBuilder();
        NumericUtils.intToPrefixCoded(shopid, 0, bytes);
        Term term = new Term(ShopDTO.ShopParam.SHOP_ID, bytes);

        boolean ret = LuceneUtil.deleteDocument(term);
        return ret;
    }

    @Override
    public Map<String, Object> search(int offset, int pagesize, String shopname, List<Integer> cityids, List<Integer> tagids, int sortType) {

        // query
        List<Query> querys = new ArrayList<>();

        // shopname
        if (shopname!=null && shopname.trim().length()>0) {
            try {
                Analyzer analyzer = new SmartChineseAnalyzer();
                QueryParser parser = new QueryParser(ShopDTO.ShopParam.SHOP_NAME, analyzer);
                Query shopNameQuery = parser.parse(shopname);
                querys.add(shopNameQuery);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // cityids
        if (cityids!=null && cityids.size() > 0) {
            BooleanQuery.Builder cityBooleanBuild = new BooleanQuery.Builder();
            for (int cityid: cityids) {
                cityBooleanBuild.add(NumericRangeQuery.newIntRange(ShopDTO.ShopParam.CITY_ID, cityid, cityid, true, true), BooleanClause.Occur.SHOULD);
            }
            querys.add(cityBooleanBuild.build());
        }

        // tagids
        if (tagids!=null && tagids.size() > 0) {
            BooleanQuery.Builder cityBooleanBuild = new BooleanQuery.Builder();
            for (int tagid: tagids) {
                cityBooleanBuild.add(NumericRangeQuery.newIntRange(ShopDTO.ShopParam.TAG_ID, tagid, tagid, true, true), BooleanClause.Occur.SHOULD);
            }
            querys.add(cityBooleanBuild.build());
        }

        // sort
        Sort scoreSort = null;
        if (sortType==1) {
            scoreSort = new Sort(new SortField(ShopDTO.ShopParam.HOT_SCORE, SortField.Type.INT, true));
        } else {
            scoreSort = new Sort(new SortField(ShopDTO.ShopParam.SCORE, SortField.Type.INT, true));
        }

        // result
        Map<String, Object> retMap = new HashMap();
        SearchResult result = LuceneUtil.search(querys, scoreSort, offset, pagesize);
        retMap.put("total", result.getTotalHits());
        retMap.put("data", result.getDocuments());
        return retMap;
    }

}
