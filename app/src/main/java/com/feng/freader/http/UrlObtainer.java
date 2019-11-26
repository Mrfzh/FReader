package com.feng.freader.http;

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
}
