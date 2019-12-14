package com.feng.freader.entity.epub;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/13
 */
public class EpubData {
    private String data; // 对于图片，为图片的绝对地址
    private String secondData;  // 图片用，备份地址，data 不行就来找这个
    private TYPE type;  // Epub 数据类型

    public enum TYPE {
        TEXT,   // 文本(<div>, <p>)
        IMG,    // 图片(<img>)
        TITLE,  // 标题（<h1> - <h6>）
        LINK,   // 超链接（<a>）
    }

    public EpubData(String data, TYPE type) {
        this(data, "", type);
    }

    public EpubData(String data, String secondData, TYPE type) {
        this.data = data;
        this.secondData = secondData;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getSecondData() {
        return secondData;
    }

    public void setSecondData(String secondData) {
        this.secondData = secondData;
    }

    @Override
    public String toString() {
        return "EpubData{" +
                "data='" + data + '\'' +
                ", secondData='" + secondData + '\'' +
                ", type=" + type +
                '}';
    }
}
