package com.xxl.search.client.es.response;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/19.
 */
public class SearchResult {

    private long total;
    private List<Map<String, Object>> data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
