package com.xxl.cache.dao;

import com.xxl.cache.core.model.MemcachedTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IMemcachedDao {

    public List<MemcachedTemplate> pageList(Map<String, Object> params);
    public int pageListCount(Map<String, Object> params);

    public int save(MemcachedTemplate memcachedTemplate);
    public int update(MemcachedTemplate memcachedTemplate);
    public int delete(int id);

    public MemcachedTemplate load(int id);

}
