package com.feng.freader.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class Constant {
    // 男生热门榜单的榜单数
    public static final int MALE_HOT_RANK_NUM = 5;
    // 男生热门榜单的 id
    private static String sZYHotRankId = "564d8003aca44f4f61850fcd";    // 掌阅热销榜
    private static String sSQHotRankId = "564d80457408cfcd63ae2dd0";    // 书旗热搜榜
    private static String sZHHotRankId = "54d430962c12d3740e4a3ed2";    // 纵横月票榜
    private static String sZLHotRankId = "5732aac02dbb268b5f037fc4";    // 逐浪点击榜
    private static String sBDHotRankId = "564ef4f985ed965d0280c9c2";    // 百度热搜榜
    public static final List<String> MALE_HOT_RANK_ID = Arrays.asList(sZYHotRankId,
            sSQHotRankId, sZHHotRankId, sZLHotRankId, sBDHotRankId);
    // 男生热门榜单的榜单名字
    public static final List<String> MALE_HOT_RANK_NAME = Arrays.asList("掌阅热销榜",
            "书旗热搜榜", "纵横月票榜", "逐浪点击榜", "百度热搜榜");

    // 女生热门榜单的榜单数
    public static final int FEMALE_HOT_RANK_NUM = 3;
    // 女生热门榜单的 id
    private static String sKHotRankId = "550b841715db45cd4b022107";    // 17K订阅榜
    private static String sNZYHotRankId = "564d80d0e8c613016446c5aa";    // 掌阅热销榜
    private static String sNSQHotRankId = "564d81151109835664770ad7";    // 书旗热搜榜
    public static final List<String> FEMALE_HOT_RANK_ID = Arrays.asList(
            sKHotRankId, sNZYHotRankId, sNSQHotRankId);
    // 男生热门榜单的榜单名字
    public static final List<String> FEMALE_HOT_RANK_NAME = Arrays.asList(
            "17K订阅榜", "掌阅热销榜", "书旗热搜榜");
}
