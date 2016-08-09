package com.xxl.cache.dao.impl;

import com.xxl.cache.core.model.MemcachedTemplate;
import com.xxl.cache.dao.IMemcachedDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/9.
 */
@Component
public class MemcachedDaoImpl implements IMemcachedDao{

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<MemcachedTemplate> pageList(Map<String, Object> params) {
        return sqlSessionTemplate.selectList("MemcachedTemplateMapper.pageList", params);
    }

    @Override
    public int pageListCount(Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("MemcachedTemplateMapper.pageListCount", params);
    }

    @Override
    public int save(MemcachedTemplate memcachedTemplate) {
        return sqlSessionTemplate.insert("MemcachedTemplateMapper.save", memcachedTemplate);
    }

    @Override
    public int update(MemcachedTemplate memcachedTemplate) {
        return sqlSessionTemplate.update("MemcachedTemplateMapper.update", memcachedTemplate);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("MemcachedTemplateMapper.delete", id);
    }

    @Override
    public MemcachedTemplate load(int id) {
        return sqlSessionTemplate.selectOne("MemcachedTemplateMapper.load", id);
    }
}
