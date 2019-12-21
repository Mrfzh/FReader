package com.feng.freader.entity.data;

/**
 * AllNovel 页面的小说信息
 *
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class ANNovelData {
    private String title;
    private String author;
    private String shortInfo;
    private String cover;

    public ANNovelData(String title, String author, String shortInfo, String cover) {
        this.title = title;
        this.author = author;
        this.shortInfo = shortInfo;
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {
        this.shortInfo = shortInfo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "ANNovelData{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", shortInfo='" + shortInfo + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
