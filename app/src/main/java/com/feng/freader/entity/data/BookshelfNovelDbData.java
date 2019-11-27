package com.feng.freader.entity.data;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/26
 */
public class BookshelfNovelDbData {
    private String novelUrl;
    private String name;
    private String cover;
    private int chapterIndex;
    private int position;
    private int pageIndex;

    public BookshelfNovelDbData(String novelUrl, String name, String cover,
                                int chapterIndex, int position, int pageIndex) {
        this.novelUrl = novelUrl;
        this.name = name;
        this.cover = cover;
        this.chapterIndex = chapterIndex;
        this.position = position;
        this.pageIndex = pageIndex;
    }

    public String getNovelUrl() {
        return novelUrl;
    }

    public void setNovelUrl(String novelUrl) {
        this.novelUrl = novelUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public String toString() {
        return "BookshelfNovelDbData{" +
                "novelUrl='" + novelUrl + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", chapterIndex=" + chapterIndex +
                ", position=" + position +
                ", pageIndex=" + pageIndex +
                '}';
    }
}
