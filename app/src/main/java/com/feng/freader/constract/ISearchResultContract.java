package com.feng.freader.constract;

import com.feng.freader.entity.data.NovelSourceData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public interface ISearchResultContract {
    interface View {
        void getNovelsSourceSuccess(List<NovelSourceData> novelSourceDataList);     // 获取小说源成功
        void getNovelsSourceError(String errorMsg);     // 获取小说源失败
    }
    interface Presenter {
        void getNovelsSourceSuccess(List<NovelSourceData> novelSourceDataList);     // 获取小说源成功
        void getNovelsSourceError(String errorMsg);     // 获取小说源失败

        void getNovelsSource(String novelName);     // 获取小说源
    }
    interface Model {
        void getNovelsSource(String novelName);     // 获取小说源
    }
}
