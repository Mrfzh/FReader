package com.feng.freader.entity.data;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class NovelSourceData {
    private String name;
    private String author;
    private String introduce;
    private String url;
    private String cover;   // 封面图片 url

    public NovelSourceData(String name, String author,
                           String introduce, String url, String cover) {
        this.name = name;
        this.author = author;
        this.introduce = introduce;
        this.url = url;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "NovelSourceData{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", introduce='" + introduce + '\'' +
                ", url='" + url + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
