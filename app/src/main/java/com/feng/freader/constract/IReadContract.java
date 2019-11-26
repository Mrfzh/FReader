package com.feng.freader.constract;

import com.feng.freader.entity.data.DetailedChapterData;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public interface IReadContract {
    interface View {
        void getDetailedChapterDataSuccess(DetailedChapterData data);
        void getDetailedChapterDataError(String errorMsg);
    }
    interface Presenter {
        void getDetailedChapterDataSuccess(DetailedChapterData data);
        void getDetailedChapterDataError(String errorMsg);

        void getDetailedChapterData(String url);    // 获取具体章节信息
    }
    interface Model {
        void getDetailedChapterData(String url);    // 获取具体章节信息
    }
}
