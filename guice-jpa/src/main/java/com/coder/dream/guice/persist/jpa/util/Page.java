package com.coder.dream.guice.persist.jpa.util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by konghang on 15-12-23.
 */
public class Page<T> implements Serializable {
    /**
     * 当前页返回数据列表
     */
    private List<T> results;

    private Long totalCount;

    public static <T> Page<T> build(List<T> results, Long totalCount) {
        return new Page<T>().setResults(results).setTotalCount(totalCount);
    }

    public List<T> getResults() {
        return results;
    }

    public Page<T> setResults(List<T> results) {
        this.results = results;
        return this;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Page<T> setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        return this;
    }
}
