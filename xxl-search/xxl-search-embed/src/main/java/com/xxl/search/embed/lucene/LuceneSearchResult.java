package com.xxl.search.embed.lucene;

import org.apache.lucene.document.Document;

import java.util.List;

/**
 * Created by xuxueli on 16/9/19.
 */
public class LuceneSearchResult {

    private int totalHits;
    private List<Document> documents;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
