package com.xxl.glue.core.loader.impl;

import com.xxl.glue.core.loader.GlueLoader;
import com.xxl.glue.core.loader.util.DBUtil;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/5/29.
 */
public class DBGlueLoader implements GlueLoader {

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String load(String name) {
        String sql = "SELECT source FROM xxl_glue_glueinfo WHERE name = ?";
        List<Map<String, Object>> result = DBUtil.query(dataSource, sql, new Object[]{name});
        if (result!=null && result.size()==1 && result.get(0)!=null && result.get(0).get("source")!=null ) {
            return (String) result.get(0).get("source");
        }
        return null;
    }
}
