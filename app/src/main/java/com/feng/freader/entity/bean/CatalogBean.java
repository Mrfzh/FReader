package com.feng.freader.entity.bean;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/17
 */
public class CatalogBean {

    private int code;
    private String message;
    private DataBean data;
    private List<ListBean> list;

    public static class DataBean {
        /**
         * status : 连载中
         * name : 无敌经验系统
         * introduce : 带着无敌经验系统来到这个世界。    绝世天才？    一宗之主？    太古大能？    无上巨擘？    全部都是经验值！
         * author : 灰黑色小喵喵
         *  time : 2016-11-16 19:44:54
         * cover : https://www.xbiquge6.com/cover/69/69491/69491s.jpg
         */

        private String status;
        private String name;
        private String introduce;
        private String author;
        private String time;
        private String cover;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }

    public static class ListBean {
        private String url;
        private String num;

        public ListBean(String url, String num) {
            this.url = url;
            this.num = num;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }
}
