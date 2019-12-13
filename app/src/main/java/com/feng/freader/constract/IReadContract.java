package com.feng.freader.constract;

import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.entity.epub.OpfData;

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
        void getOpfDataSuccess(OpfData opfData);
        void getOpfDataError(String errorMsg);
        void getEpubChapterDataSuccess(List<EpubData> dataList);
        void getEpubChapterDataError(String errorMsg);
    }
    interface Presenter {
        void getChapterUrlListSuccess(List<String> chapterUrlList);
        void getChapterUrlListError(String errorMsg);
        void getDetailedChapterDataSuccess(DetailedChapterData data);
        void getDetailedChapterDataError(String errorMsg);
        void loadTxtSuccess(String text);
        void loadTxtError(String errorMsg);
        void getOpfDataSuccess(OpfData opfData);
        void getOpfDataError(String errorMsg);
        void getEpubChapterDataSuccess(List<EpubData> dataList);
        void getEpubChapterDataError(String errorMsg);

        void getChapterUrlList(String url);         // 获取章节 url 列表
        void getDetailedChapterData(String url);    // 获取具体章节信息
        void loadTxt(String filePath);      // 加载 txt 文本
        void getOpfData(String filePath);  // 解压 epub，得到 opf 文件中的数据
        void getEpubChapterData(String filePath);   // 解析 html/xhtml 文件，得到章节数据
    }
    interface Model {
        void getChapterUrlList(String url);         // 获取章节 url 列表
        void getDetailedChapterData(String url);    // 获取具体章节信息
        void loadTxt(String filePath);      // 加载 txt 文本
        void getOpfData(String filePath);  // 解压 epub，得到 opf 文件中的数据
        void getEpubChapterData(String filePath);   // 解析 html/xhtml 文件，得到章节数据
    }
}
