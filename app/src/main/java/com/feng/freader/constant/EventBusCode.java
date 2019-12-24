package com.feng.freader.constant;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/12
 */
public class EventBusCode {
    public static final int SEARCH_UPDATE_INPUT = 0;    // 搜索活动，更新搜索栏输入
    public static final int NOVEL_INTRO_INIT = 1;       // 小说介绍活动，接收初始化数据
    public static final int BOOKSHELF_UPDATE_LIST = 2;  // 书架页面，更新列表数据
    public static final int CATALOG_HOLD_READ_ACTIVITY = 3;  // 目录页面，持有传来的 ReadActivity 引用
    public static final int EPUB_CATALOG_INIT = 4;      // 目录页面初始化
    public static final int MORE_INTO = 5;        // 通过切换 Fragment 进入 MoreActivity 页面
}
