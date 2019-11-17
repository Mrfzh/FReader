package com.feng.freader.entity.data;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/17
 */
public class CatalogData {
    private List<String> chapterNameList;
    private List<String> chapterUrlList;

    public CatalogData(List<String> chapterNameList, List<String> chapterUrlList) {
        this.chapterNameList = chapterNameList;
        this.chapterUrlList = chapterUrlList;
    }

    public List<String> getChapterNameList() {
        return chapterNameList;
    }

    public void setChapterNameList(List<String> chapterNameList) {
        this.chapterNameList = chapterNameList;
    }

    public List<String> getChapterUrlList() {
        return chapterUrlList;
    }

    public void setChapterUrlList(List<String> chapterUrlList) {
        this.chapterUrlList = chapterUrlList;
    }
}
