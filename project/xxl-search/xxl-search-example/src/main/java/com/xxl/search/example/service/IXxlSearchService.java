package com.xxl.search.example.service;

import com.xxl.search.example.core.model.ShopDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/20.
 */
public interface IXxlSearchService {

    public boolean deleteAll();

    public boolean addDocument(ShopDTO shopDTO);

    public boolean updateDocument(ShopDTO shopDTO);

    public boolean deleteDocument(int shopid);

    /**
     *
     * @param offset    分页
     * @param pagesize
     * @param shopname  分词查询
     * @param cityids   精确查询
     * @param tagids    范围查询 (一个Field绑定多值, 多值范围查询)
     * @param sortType  排序
     * @return
     */
    public Map<String, Object> search(int offset, int pagesize, String shopname, List<Integer> cityids, List<Integer> tagids, int sortType);

}
