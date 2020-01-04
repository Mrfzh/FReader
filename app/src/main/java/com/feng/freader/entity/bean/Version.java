package com.feng.freader.entity.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author Feng Zhaohao
 * Created on 2020/1/3
 */
public class Version extends BmobObject {
    private int id;
    private int versionCode;
    private String addr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Version{" +
                "id=" + id +
                ", versionCode=" + versionCode +
                ", addr='" + addr + '\'' +
                '}';
    }
}
