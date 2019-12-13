package com.feng.freader.constant;

import com.feng.freader.app.App;

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

    /* 错误信息 */
    // 小说源 api 没有找到相关小说
    public static final String NOT_FOUND_NOVELS = "没有找到相关小说";
    // 没有获取到相关目录信息
    public static final String NOT_FOUND_CATALOG_INFO = "没有找到相关目录";
    // json 格式错误
    public static final String JSON_ERROR = "json 格式错误";
    // 该小说已从本地删除
    public static final String NOT_FOUND_FROM_LOCAL = "该小说已从本地删除";

    /* 数据库相关 */
    // 数据库名
    public static final String DB_NAME = "FReader.db";
    // 历史记录表
    public static final String TABLE_HISTORY = "TABLE_HISTORY";
    // 历史记录表的记录
    public static final String TABLE_HISTORY_ID = "TABLE_HISTORY_ID";       // 自增 id（主键）
    public static final String TABLE_HISTORY_WORD = "TABLE_HISTORY_WORD";   // 搜索词
    // 书架书籍表
    public static final String TABLE_BOOKSHELF_NOVEL = "TABLE_BOOKSHELF_NOVEL";
    // 书架书籍表的记录
    public static final String TABLE_BOOKSHELF_NOVEL_NOVEL_URL = "TABLE_BOOKSHELF_NOVEL_NOVEL_URL"; // 小说 URL（主键）
    public static final String TABLE_BOOKSHELF_NOVEL_NAME = "TABLE_BOOKSHELF_NOVEL_NAME"; // 小说名
    public static final String TABLE_BOOKSHELF_NOVEL_COVER = "TABLE_BOOKSHELF_NOVEL_COVER"; // 小说封面
    // 章节索引：网络小说和本地 epub 小说为目录索引，本地 txt 小说无需该属性
    public static final String TABLE_BOOKSHELF_NOVEL_CHAPTER_INDEX = "TABLE_BOOKSHELF_NOVEL_CHAPTER_INDEX";
    // 位置索引（用于跳转到上一次进度）：网络小说和 txt 是 String 文本的位置
    public static final String TABLE_BOOKSHELF_NOVEL_POSITION = "TABLE_BOOKSHELF_NOVEL_POSITION";
    // 第二位置索引（epub 解析用）
    public static final String TABLE_BOOKSHELF_NOVEL_SECOND_POSITION = "TABLE_BOOKSHELF_NOVEL_SECOND_POSITION";
    // 类型：0 为网络小说， 1 为本地 txt 小说, 2 为本地 epub 小说
    public static final String TABLE_BOOKSHELF_NOVEL_TYPE = "TABLE_BOOKSHELF_NOVEL_TYPE";


    /* 文件存储 */
    public static final String EPUB_SAVE_PATH = App.getContext().getFilesDir() + "/epubFile";
}
