package com.feng.freader.entity.epub;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/13
 */
public class TocItem {
    private String title;   // 目录标题
    private String path;    // 文件绝对路径

    public TocItem() {
    }

    public TocItem(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "TocItem{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
