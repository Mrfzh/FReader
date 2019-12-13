package com.feng.freader.entity.epub;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/12
 */
public class OpfData {
    private String title;   // 小说名
    private String cover;   // 小说封面路径
    private String ncx;     // ncx 文件路径
    private String parentPath;  // opf 文件的父目录
    private List<String> spine;     // 小说阅读顺序，每个元素为 text 的相对位置

    public OpfData() {
        title = "";
        cover = "";
        ncx = "";
        spine = new ArrayList<>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNcx() {
        return ncx;
    }

    public void setNcx(String ncx) {
        this.ncx = ncx;
    }

    public List<String> getSpine() {
        return spine;
    }

    public void setSpine(List<String> spine) {
        this.spine = spine;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    public String toString() {
        return "OpfData{" +
                "title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", ncx='" + ncx + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", spine=" + spine +
                '}';
    }
}
