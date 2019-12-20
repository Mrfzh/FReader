package com.feng.freader.constract;

import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.entity.data.HotRankData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public interface IMaleContract {
    interface View {
        void getHotRankDataSuccess(HotRankData hotRankData);
        void getHotRankDataError(List<String> errorMsgList);
        void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList);
        void getCategoryNovelsError(String errorMsg);
    }
    interface Presenter {
        void getHotRankDataSuccess(HotRankData hotRankData);
        void getHotRankDataError(List<String> errorMsgList);
        void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList);
        void getCategoryNovelsError(String errorMsg);

        void getHotRankData();      // 获取热门排行
        void getCategoryNovels();   // 获取分类小说
    }
    interface Model {
        void getHotRankData();      // 获取热门排行
        void getCategoryNovels();   // 获取分类小说
    }
}
