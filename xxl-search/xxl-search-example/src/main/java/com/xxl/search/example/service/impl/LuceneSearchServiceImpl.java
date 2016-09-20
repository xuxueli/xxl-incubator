package com.xxl.search.example.service.impl;

import com.xxl.search.client.lucene.LuceneSearchResult;
import com.xxl.search.client.lucene.LuceneUtil;
import com.xxl.search.example.core.model.ShopDTO;
import com.xxl.search.example.service.IXxlSearchService;
import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Document document = new Document();

        document.add(new IntField(ShopDTO.ShopParam.SHOP_ID, shopDTO.getShopid(), Field.Store.YES));
        document.add(new TextField(ShopDTO.ShopParam.SHOP_NAME, shopDTO.getShopname(), Field.Store.YES));
        //document.add(new StringField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid()+"", Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid(), Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.SCORE, shopDTO.getScore(), Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.HOT_SCORE, shopDTO.getHotscore(), Field.Store.YES));

        boolean ret = LuceneUtil.addDocument(document, true);
        return ret;
    }

    @Override
    public boolean updateDocument(ShopDTO shopDTO) {
        Document document = new Document();

        document.add(new IntField(ShopDTO.ShopParam.SHOP_ID, shopDTO.getShopid(), Field.Store.YES));
        document.add(new TextField(ShopDTO.ShopParam.SHOP_NAME, shopDTO.getShopname(), Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.CITY_ID, shopDTO.getCityid(), Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.SCORE, shopDTO.getScore(), Field.Store.YES));
        document.add(new IntField(ShopDTO.ShopParam.HOT_SCORE, shopDTO.getHotscore(), Field.Store.YES));

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
    public LuceneSearchResult search(List<Integer> cityids) {

        // query
        List<Query> querys = new ArrayList<>();
        if (cityids!=null && cityids.size() > 0) {
            BooleanQuery.Builder cityBooleanBuild = new BooleanQuery.Builder();
            for (int cityid: cityids) {
                cityBooleanBuild.add(NumericRangeQuery.newIntRange(ShopDTO.ShopParam.CITY_ID, cityid, cityid, true, true), BooleanClause.Occur.SHOULD);
            }
            querys.add(cityBooleanBuild.build());
        }

        // sort
        Sort scoreSort = new Sort(new SortField(ShopDTO.ShopParam.SCORE, SortField.Type.DOC, true));
        // result
        LuceneSearchResult result = LuceneUtil.search(querys, scoreSort, 0, 20);
        return result;
    }

}
