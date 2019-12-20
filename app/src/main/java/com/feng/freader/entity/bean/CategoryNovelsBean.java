package com.feng.freader.entity.bean;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/20
 */
public class CategoryNovelsBean {

    /**
     * total : 2856
     * books : [{"_id":"56332a0db27371a7079fba8b","cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F722127%2F722127_caf0d27040bc456db00f52582cba8842.jpg%2F","site":"zhuishuvip","author":"暗夜行走","majorCate":"都市","minorCate":"都市生活","title":"逍遥兵王","shortIntro":"洛天，神秘部门的最强利刃，针对国内国际敌对势力进行了恐怖的打击，偶然一次意外，他失去了最好的兄弟，为了照顾好兄弟的亲人，他一个人来到了东昌市。","sizetype":-1,"superscript":"","contentType":"txt","allowMonthly":false,"banned":0,"latelyFollower":30853,"retentionRatio":78.57,"lastChapter":"第一卷　正文 第4101章 举手之劳","tags":["兵王","热血","热血兵王","爽文","美女","都市"]},{"_id":"56b1c5b918ee9a7b1b374c3d","cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F863535%2F863535_629fc38dd87047c4b0122a238de66ae1.jpg%2F","site":"zhuishuvip","majorCate":"都市","author":"一缕微风","minorCate":"都市生活","title":"美女总裁的神级保镖","shortIntro":"秦天，让世界雇佣兵为之胆寒的圣王，因承诺回归都市！原本只想低调的保护一下美女总裁，谁曾想跳梁小丑太嚣张，好吧，既然不能低调，那就高调吧！","sizetype":-1,"superscript":"","contentType":"txt","allowMonthly":false,"banned":0,"latelyFollower":7205,"retentionRatio":78.41,"lastChapter":"第一卷　正文 第2989章 玄帝身陨！","tags":["保镖","兵王","爽文","生活","美女","都市","都市生活"]},{"_id":"56cf8b0fccd1812269136543","cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F1187166%2F1187166_290bfe7a944e4d5ba796cd345bba10d5.jpg%2F","site":"zhuishuvip","author":"秦长青","majorCate":"都市","minorCate":"异术超能","title":"极品透视保镖","shortIntro":"地摊小子叶开捡了块玉石，意外得到透视能力，更有个绝代妖女住进他的身体！从此生活多姿多彩，赌石？一眼看穿！美女？一眼看透！还做了美女们的保镖，\u201c大小姐，我是保镖，不是保姆，暖脚洗衣，不是我的工作吧？\u201d","sizetype":-1,"superscript":"","contentType":"txt","allowMonthly":true,"banned":0,"latelyFollower":14887,"retentionRatio":78.4,"lastChapter":"长生篇 完本感言&amp;新书","tags":["升级流","宅男","异术超能","扮猪吃虎","热血","爽文","超能","都市","随身流","风水异术"]}]
     * ok : true
     */

    private int total;
    private boolean ok;
    private List<BooksBean> books;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<BooksBean> getBooks() {
        return books;
    }

    public void setBooks(List<BooksBean> books) {
        this.books = books;
    }

    public static class BooksBean {
        /**
         * _id : 56332a0db27371a7079fba8b
         * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F722127%2F722127_caf0d27040bc456db00f52582cba8842.jpg%2F
         * site : zhuishuvip
         * author : 暗夜行走
         * majorCate : 都市
         * minorCate : 都市生活
         * title : 逍遥兵王
         * shortIntro : 洛天，神秘部门的最强利刃，针对国内国际敌对势力进行了恐怖的打击，偶然一次意外，他失去了最好的兄弟，为了照顾好兄弟的亲人，他一个人来到了东昌市。
         * sizetype : -1
         * superscript :
         * contentType : txt
         * allowMonthly : false
         * banned : 0
         * latelyFollower : 30853
         * retentionRatio : 78.57
         * lastChapter : 第一卷　正文 第4101章 举手之劳
         * tags : ["兵王","热血","热血兵王","爽文","美女","都市"]
         */

        private String _id;
        private String cover;
        private String site;
        private String author;
        private String majorCate;
        private String minorCate;
        private String title;
        private String shortIntro;
        private int sizetype;
        private String superscript;
        private String contentType;
        private boolean allowMonthly;
        private int banned;
        private int latelyFollower;
        private double retentionRatio;
        private String lastChapter;
        private List<String> tags;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getMajorCate() {
            return majorCate;
        }

        public void setMajorCate(String majorCate) {
            this.majorCate = majorCate;
        }

        public String getMinorCate() {
            return minorCate;
        }

        public void setMinorCate(String minorCate) {
            this.minorCate = minorCate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShortIntro() {
            return shortIntro;
        }

        public void setShortIntro(String shortIntro) {
            this.shortIntro = shortIntro;
        }

        public int getSizetype() {
            return sizetype;
        }

        public void setSizetype(int sizetype) {
            this.sizetype = sizetype;
        }

        public String getSuperscript() {
            return superscript;
        }

        public void setSuperscript(String superscript) {
            this.superscript = superscript;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public boolean isAllowMonthly() {
            return allowMonthly;
        }

        public void setAllowMonthly(boolean allowMonthly) {
            this.allowMonthly = allowMonthly;
        }

        public int getBanned() {
            return banned;
        }

        public void setBanned(int banned) {
            this.banned = banned;
        }

        public int getLatelyFollower() {
            return latelyFollower;
        }

        public void setLatelyFollower(int latelyFollower) {
            this.latelyFollower = latelyFollower;
        }

        public double getRetentionRatio() {
            return retentionRatio;
        }

        public void setRetentionRatio(double retentionRatio) {
            this.retentionRatio = retentionRatio;
        }

        public String getLastChapter() {
            return lastChapter;
        }

        public void setLastChapter(String lastChapter) {
            this.lastChapter = lastChapter;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
