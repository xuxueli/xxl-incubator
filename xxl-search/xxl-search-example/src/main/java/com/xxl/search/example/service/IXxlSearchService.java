package com.xxl.search.example.service;

import com.xxl.search.client.lucene.LuceneSearchResult;
import com.xxl.search.example.core.model.ShopDTO;

import java.util.List;

/**
 * Created by xuxueli on 16/9/20.
 */
public interface IXxlSearchService {

    public boolean deleteAll();

    public boolean addDocument(ShopDTO shopDTO);

    public boolean updateDocument(ShopDTO shopDTO);

    public boolean deleteDocument(int shopid);

    public LuceneSearchResult search(List<Integer> cityids);

}
