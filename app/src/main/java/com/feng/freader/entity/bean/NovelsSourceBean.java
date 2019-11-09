package com.feng.freader.entity.bean;

import java.util.List;

/**
 * 小说源
 *
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class NovelsSourceBean {

    /**
     * message : 成功!
     * code : 0
     * list : [{"num":" 第两千六百一十九章 聚源宝丹 ","cover":"https://www.xbiquge6.com/cover/0/657/657s.jpg","time":"2019-11-09","url":"https://www.xbiquge6.com/0_657/","name":"斗破苍穹之无上之境","author":"夜雨闻铃0","introduce":"你常说，复仇才是我的使命，但你却不知道，守护你，才是我一生的宿命\n等级制度：斗帝、斗仙、斗神、帝之不朽\n各位书友要是觉得《斗破苍穹之无上之境》还不错的话请不要忘记向您QQ群和微博里的朋友推荐哦！","tag":"武侠仙侠"},{"num":"第两千六百一十九章 聚源宝丹","cover":"https://www.33xs.com/image/263/263786/263786s.jpg","time":"2019-11-09","url":"/33xs/263/263786/","name":"斗破苍穹之无上之境","author":"夜雨闻铃0","introduce":"    你常说，复仇才是我的使命，但你却不知道，守护你，才是我一生的宿命    等级制度：斗帝、斗仙、斗神、帝之不朽","tag":"其他小说"}]
     */

    private String message;
    private int code;
    private List<ListBean> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * num :  第两千六百一十九章 聚源宝丹
         * cover : https://www.xbiquge6.com/cover/0/657/657s.jpg
         * time : 2019-11-09
         * url : https://www.xbiquge6.com/0_657/
         * name : 斗破苍穹之无上之境
         * author : 夜雨闻铃0
         * introduce : 你常说，复仇才是我的使命，但你却不知道，守护你，才是我一生的宿命
         等级制度：斗帝、斗仙、斗神、帝之不朽
         各位书友要是觉得《斗破苍穹之无上之境》还不错的话请不要忘记向您QQ群和微博里的朋友推荐哦！
         * tag : 武侠仙侠
         */

        private String num;
        private String cover;
        private String time;
        private String url;
        private String name;
        private String author;
        private String introduce;
        private String tag;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
