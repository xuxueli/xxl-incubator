package com.xxl.search.client.es;

import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/9/19.
 */
public class ElasticsearchResult {

    private long totalHits;
    private List<Map<String, Object>> sources;

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public List<Map<String, Object>> getSources() {
        return sources;
    }

    public void setSources(List<Map<String, Object>> sources) {
        this.sources = sources;
    }

}
