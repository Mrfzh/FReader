package com.feng.freader.model;

import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.bean.DetailedChapterBean;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.http.UrlObtainer;
import com.google.gson.Gson;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class ReadModel implements IReadContract.Model {

    private IReadContract.Presenter mPresenter;
    private Gson mGson;

    public ReadModel(IReadContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mGson = new Gson();
    }

    @Override
    public void getDetailedChapterData(String url) {
        String requestUrl = UrlObtainer.getDetailedChapter(url);
        OkhttpUtil.getRequest(requestUrl, new OkhttpCall() {
            @Override
            public void onResponse(String json) {   // 得到 json 数据
                DetailedChapterBean bean = mGson.fromJson(json, DetailedChapterBean.class);
                if (bean.getCode() != 0) {
                    mPresenter.getDetailedChapterDataError("未找到相关数据");
                    return;
                }
                StringBuilder contentBuilder = new StringBuilder();
                contentBuilder.append("    ");
                for (String s : bean.getContent()) {
                    contentBuilder.append(s);
                    contentBuilder.append("\n");
                }
                DetailedChapterData data = new DetailedChapterData(bean.getNum(),
                        contentBuilder.toString());
                mPresenter.getDetailedChapterDataSuccess(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                mPresenter.getDetailedChapterDataError(errorMsg);
            }
        });
    }
}
