package com.feng.freader.constract;

import com.feng.freader.entity.data.DetailedChapterData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public interface IReadContract {
    interface View {
        void getChapterUrlListSuccess(List<String> chapterUrlList);
        void getChapterUrlListError(String errorMsg);
        void getDetailedChapterDataSuccess(DetailedChapterData data);
        void getDetailedChapterDataError(String errorMsg);
        void loadTxtSuccess(String text);
        void loadTxtError(String errorMsg);
    }
    interface Presenter {
        void getChapterUrlListSuccess(List<String> chapterUrlList);
        void getChapterUrlListError(String errorMsg);
        void getDetailedChapterDataSuccess(DetailedChapterData data);
        void getDetailedChapterDataError(String errorMsg);
        void loadTxtSuccess(String text);
        void loadTxtError(String errorMsg);

        void getChapterUrlList(String url);         // 获取章节 url 列表
        void getDetailedChapterData(String url);    // 获取具体章节信息
        void loadTxt(String filePath);      // 加载 txt 文本
    }
    interface Model {
        void getChapterUrlList(String url);         // 获取章节 url 列表
        void getDetailedChapterData(String url);    // 获取具体章节信息
        void loadTxt(String filePath);      // 加载 txt 文本
    }
}
