package com.feng.freader.constract;

import com.feng.freader.entity.data.HotRankData;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public interface IMaleContract {
    interface View {
        void getHotRankDataSuccess(HotRankData hotRankData);    // 获取热门排行成功
        void getHotRankDataError(String errorMsg);              // 获取热门排行失败
    }
    interface Presenter {
        void getHotRankDataSuccess(HotRankData hotRankData);    // 获取热门排行成功
        void getHotRankDataError(String errorMsg);              // 获取热门排行失败
        void getHotRankData();      // 获取热门排行
    }
    interface Model {
        void getHotRankData();      // 获取热门排行
    }
}
