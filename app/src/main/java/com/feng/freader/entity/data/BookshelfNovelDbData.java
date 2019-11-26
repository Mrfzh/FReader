package com.feng.freader.entity.data;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/26
 */
public class BookshelfNovelDbData {
    private String novelUrl;
    private String name;
    private String cover;
    private String chapterUrl;
    private int position;

    public BookshelfNovelDbData(String novelUrl, String name,
                                String cover, String chapterUrl, int position) {
        this.novelUrl = novelUrl;
        this.name = name;
        this.cover = cover;
        this.chapterUrl = chapterUrl;
        this.position = position;
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

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "BookshelfNovelDbData{" +
                "novelUrl='" + novelUrl + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", chapterUrl='" + chapterUrl + '\'' +
                ", position=" + position +
                '}';
    }
}
