package com.xxl.cache.service;

import com.xxl.cache.core.model.MemcachedTemplate;
import com.xxl.cache.core.util.ReturnT;

import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
public interface IMemcachedService {

    public Map<String,Object> pageList(int offset, int pagesize, String key);

    public ReturnT<String> save(MemcachedTemplate memcachedTemplate);

    public ReturnT<String> update(MemcachedTemplate memcachedTemplate);

    public ReturnT<String> delete(int id);

    public ReturnT<Map<String, Object>> getCacheInfo(String finalKey);

    public ReturnT<String> removeCache(String finalKey);
}
