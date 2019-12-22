package com.feng.freader.entity.data;

import java.util.List;

/**
 * 热门排行数据
 *
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class HotRankData {
    private List<String> rankNameList;          // 排行榜名称
    private List<List<String>> novelNameList;  // 小说名称集合

    public HotRankData(List<String> rankNameList,
                       List<List<String>> novelNameList) {
        this.rankNameList = rankNameList;
        this.novelNameList = novelNameList;
    }

    public List<String> getRankNameList() {
        return rankNameList;
    }

    public void setRankNameList(List<String> rankNameList) {
        this.rankNameList = rankNameList;
    }

    public List<List<String>> getNovelNameList() {
        return novelNameList;
    }

    public void setNovelNameList(List<List<String>> novelNameList) {
        this.novelNameList = novelNameList;
    }
}
