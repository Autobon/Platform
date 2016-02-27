package com.autobon.platform.utils;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by dave on 16/2/26.
 */
public class JsonPage<E> {
    private int page;
    private long totalElements;
    private int totalPages;
    private int pageSize;
    private int count;
    private List<E> list;

    public JsonPage() {}

    public JsonPage(int page, int pageSize, int totalElements, int totalPages, int count, List<E> list) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.count = count;
        this.list = list;
    }

    public JsonPage(Page<E> page) {
        this.page = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.count = page.getNumberOfElements();
        this.list = page.getContent();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
