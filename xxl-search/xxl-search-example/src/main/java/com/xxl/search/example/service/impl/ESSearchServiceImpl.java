package com.xxl.search.example.service.impl;

import com.xxl.search.example.core.model.ShopDTO;
import com.xxl.search.example.service.IXxlSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/21.
 */
@Service("eSSearchServiceImpl")
public class ESSearchServiceImpl implements IXxlSearchService {
    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean addDocument(ShopDTO shopDTO) {
        return false;
    }

    @Override
    public boolean updateDocument(ShopDTO shopDTO) {
        return false;
    }

    @Override
    public boolean deleteDocument(int shopid) {
        return false;
    }

    @Override
    public Map<String, Object> search(int offset, int pagesize, String shopname, List<Integer> cityids, List<Integer> tagids, int sortType) {
        return null;
    }
}
