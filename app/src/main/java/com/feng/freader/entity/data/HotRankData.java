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
    private List<NovelInfo> novelInfoList;  // 小说信息

    public HotRankData(List<String> rankNameList, List<NovelInfo> novelInfoList) {
        this.rankNameList = rankNameList;
        this.novelInfoList = novelInfoList;
    }

    public List<String> getRankNameList() {
        return rankNameList;
    }

    public void setRankNameList(List<String> rankNameList) {
        this.rankNameList = rankNameList;
    }

    public List<NovelInfo> getNovelInfoList() {
        return novelInfoList;
    }

    public void setNovelInfoList(List<NovelInfo> novelInfoList) {
        this.novelInfoList = novelInfoList;
    }

    public static class NovelInfo {
        private List<String> idList;      // 小说 id，可用于其他 api
        private List<String> nameList;    // 小说名
        private List<String> shortInfoList;   // 简介
        private List<String> coverList;   // 封面 url（需在其前面拼接 statics.zhuishushenqi.com）

        public NovelInfo() {
        }

        public NovelInfo(List<String> idList, List<String> nameList, List<String> shortInfoList, List<String> coverList) {
            this.idList = idList;
            this.nameList = nameList;
            this.shortInfoList = shortInfoList;
            this.coverList = coverList;
        }

        public List<String> getIdList() {
            return idList;
        }

        public void setIdList(List<String> idList) {
            this.idList = idList;
        }

        public List<String> getNameList() {
            return nameList;
        }

        public void setNameList(List<String> nameList) {
            this.nameList = nameList;
        }

        public List<String> getShortInfoList() {
            return shortInfoList;
        }

        public void setShortInfoList(List<String> shortInfoList) {
            this.shortInfoList = shortInfoList;
        }

        public List<String> getCoverList() {
            return coverList;
        }

        public void setCoverList(List<String> coverList) {
            this.coverList = coverList;
        }

        @Override
        public String toString() {
//            return "NovelInfo{" +
//                    "idList=" + idList +
//                    ", nameList=" + nameList +
//                    ", shortInfoList=" + shortInfoList +
//                    ", coverList=" + coverList +
//                    '}';
            return "NovelInfo{ nameList= " + nameList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HotRankData{" +
                "rankNameList=" + rankNameList +
                ", novelInfoList=" + novelInfoList +
                '}';
    }
}
