package com.feng.freader.entity.data;

/**
 * 请求分类小说所需的参数
 *
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class RequestCNData {
    private String gender;
    private String major;
    private String minor;
    private String type;
    private int start;
    private int num;

    public RequestCNData() {
    }

    public RequestCNData(String gender, String major, String minor,
                         String type, int start, int num) {
        this.gender = gender;
        this.major = major;
        this.minor = minor;
        this.type = type;
        this.start = start;
        this.num = num;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
