package com.bible.app.model;

import java.util.List;

public class SearchResult {

    private List<Finding> findings;
    private int hitCount;
    private boolean floodSearch;

    public SearchResult() {
        this.floodSearch = false;
    }

    public List<Finding> getFindings() {
        return findings;
    }

    public void setFindings(List<Finding> findings) {
        this.findings = findings;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int count) {
        this.hitCount = count;
    }

    public boolean isFloodSearch() {
        return floodSearch;
    }

    public void setFloodSearch(boolean floodSearch) {
        this.floodSearch = floodSearch;
    }
}
