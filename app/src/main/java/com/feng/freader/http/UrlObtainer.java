package com.feng.freader.http;

import com.feng.freader.constant.Constant;

/**
 * 获取相应的 url
 *
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class UrlObtainer {

    /**
     * 获取排行榜小说
     *
     * @param id 排行榜的 id
     * @return
     */
    public static String getRankNovels(String id) {
        return "http://api.zhuishushenqi.com/ranking/" + id;
    }

    /**
     * 获取小说源
     *
     * @param novelName 小说名
     * @return
     */
    public static String getNovelsSource(String novelName) {
        return "http://api.pingcc.cn/?xsname=" + novelName;
    }

    /**
     * 获取目录信息
     *
     * @param url 对应小说的 url
     * @return
     */
    public static String getCatalogInfo(String url) {
        return "http://api.pingcc.cn/?xsurl1=" + url;
    }

    /**
     * 获取具体章节信息
     *
     * @param url 对应章节的 url
     * @return
     */
    public static String getDetailedChapter(String url) {
        return "http://api.pingcc.cn/?xsurl2=" + url;
    }

    /**
     * 获取分类小说（发现页面）
     *
     * @param gender 性别
     * @param major 一级分类
     * @param num 数量
     * @return
     */
    public static String getCategoryNovels(String gender, String major, int num) {
        return "https://api.zhuishushenqi.com/book/by-categories?gender=" +
                gender +
                "&type=" +
                Constant.CATEGORY_TYPE_HOT +
                "&major=" +
                major +
                "&minor=&start=0&limit=" +
                num;
    }

    /**
     * 获取分类小说（全部书籍页面）
     *
     * @param gender 性别
     * @param major 一级分类
     * @param minor 二级分类
     * @param type 类型
     * @param start 开始的索引（从 0 开始，在分页中，每次请求加上每页的数量）
     * @param num 该页的数量
     * @return
     */
    public static String getCategoryNovels(String gender, String major, String minor, String type,
                                           int start, int num) {
        return "https://api.zhuishushenqi.com/book/by-categories?gender=" +
                gender +
                "&type=" +
                 type +
                "&major=" +
                major +
                "&minor=" +
                minor +
                "&start=" +
                start +
                "&limit=" +
                num;
    }
}
