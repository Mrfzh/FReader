package com.feng.freader.constant;

import com.feng.freader.app.App;

import java.util.Arrays;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class Constant {
    /* 热门榜单相关 */
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
    // 女生热门榜单的榜单名字
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

    /* 分类小说相关 */
    // gender
    public static final String CATEGORY_GENDER_MALE = "male";   // 男生
    public static final String CATEGORY_GENDER_FEMALE = "female";   // 女生
    public static final String CATEGORY_GENDER_PRESS = "press";   // 出版
    public static final String CATEGORY_GENDER_MALE_TEXT = "男生";
    public static final String CATEGORY_GENDER_FEMALE_TEXT  = "女生";
    public static final String CATEGORY_GENDER_PRESS_TEXT  = "出版";
    // type
    public static final String CATEGORY_TYPE_HOT = "hot";   // 热门
    public static final String CATEGORY_TYPE_NEW = "new";   // 新书
    public static final String CATEGORY_TYPE_REPUTATION = "reputation";   // 好评
    public static final String CATEGORY_TYPE_OVER = "over";   // 完结
    public static final String CATEGORY_TYPE_MONTH = "month";   // 包月
    public static final String CATEGORY_TYPE_HOT_TEXT = "热门";
    public static final String CATEGORY_TYPE_NEW_TEXT = "新书";
    public static final String CATEGORY_TYPE_REPUTATION_TEXT = "好评";
    public static final String CATEGORY_TYPE_OVER_TEXT = "完结";
    public static final String CATEGORY_TYPE_MONTH_TEXT = "包月";
    // major（男生）
    public static final String CATEGORY_MAJOR_XH = "玄幻";
    public static final String CATEGORY_MAJOR_QH = "奇幻";
    public static final String CATEGORY_MAJOR_WX = "武侠";
    public static final String CATEGORY_MAJOR_XX = "仙侠";
    public static final String CATEGORY_MAJOR_DS = "都市";
    public static final String CATEGORY_MAJOR_ZC = "职场";
    public static final String CATEGORY_MAJOR_LS = "历史";
    public static final String CATEGORY_MAJOR_JS = "军事";
    public static final String CATEGORY_MAJOR_YX = "游戏";
    public static final String CATEGORY_MAJOR_JJ = "竞技";
    public static final String CATEGORY_MAJOR_KH = "科幻";
    public static final String CATEGORY_MAJOR_LY = "灵异";
    public static final String CATEGORY_MAJOR_TR = "同人";
    public static final String CATEGORY_MAJOR_QXS = "轻小说";
    // major（女生）
    public static final String CATEGORY_MAJOR_GDYQ = "古代言情";
    public static final String CATEGORY_MAJOR_XDYQ = "现代言情";
    public static final String CATEGORY_MAJOR_QCXY = "青春校园";
    public static final String CATEGORY_MAJOR_CA = "纯爱";
    public static final String CATEGORY_MAJOR_XHQH = "玄幻奇幻";
    public static final String CATEGORY_MAJOR_WXXX = "武侠仙侠";
    // major（出版）
    public static final String CATEGORY_MAJOR_CBXS = "出版小说";
    public static final String CATEGORY_MAJOR_ZJMZ = "传记名著";
    public static final String CATEGORY_MAJOR_CGLZ = "成功励志";
    public static final String CATEGORY_MAJOR_RWSK = "人文社科";
    public static final String CATEGORY_MAJOR_JGLC = "经管理财";
    public static final String CATEGORY_MAJOR_SHSS = "生活时尚";
    public static final String CATEGORY_MAJOR_YEJK = "育儿健康";
    public static final String CATEGORY_MAJOR_QCYQ = "青春言情";
    public static final String CATEGORY_MAJOR_WWYB = "外文原版";
    public static final String CATEGORY_MAJOR_ZZJS = "政治军事";

    /* 全部小说 */
    public static final int NOVEL_PAGE_NUM = 10;    // 每页的小说数
}
