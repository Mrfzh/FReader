package com.feng.freader.model;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.ISearchResultContract;
import com.feng.freader.entity.bean.NovelsSourceBean;
import com.feng.freader.entity.data.NovelSourceData;
import com.feng.freader.http.UrlObtainer;
import com.feng.freader.httpUrlUtil.HttpUrlRequestBuilder;
import com.feng.freader.httpUrlUtil.Request;
import com.feng.freader.httpUrlUtil.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class SearchResultModel implements ISearchResultContract.Model {

    private ISearchResultContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    public SearchResultModel(ISearchResultContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getNovelsSource(String novelName) {
        // 构造请求
        Request request = new Request.Builder()
                .setUrl(UrlObtainer.getNovelsSource(novelName))
                .build();
        // 执行请求
        HttpUrlRequestBuilder.getInstance()
                .setRequest(request)
                .setResponse(new Response() {
                    @Override
                    public void success(String response) {
                        try {
                            NovelsSourceBean novelsSourceBean = mGson.fromJson(response,
                                    NovelsSourceBean.class);
                            int code = novelsSourceBean.getCode();
                            if (code == 0) {
                                // 请求成功
                                List<NovelSourceData> novelSourceDataList = new ArrayList<>();
                                List<NovelsSourceBean.ListBean> list = novelsSourceBean.getList();
                                for (int i = 0; i < list.size(); i++) {
                                    NovelsSourceBean.ListBean curr = list.get(i);
                                    NovelSourceData novelSourceData = new NovelSourceData(curr.getName(),
                                            curr.getAuthor(), curr.getIntroduce(), curr.getUrl(), curr.getCover());
                                    novelSourceDataList.add(novelSourceData);
                                }
                                mPresenter.getNovelsSourceSuccess(novelSourceDataList);
                            } else {
                                // 请求失败，没有找到相关小说
                                mPresenter.getNovelsSourceError(Constant.NOT_FOUND_NOVELS);
                            }
                        } catch (Throwable t) {
                            mPresenter.getNovelsSourceError(Constant.NOT_FOUND_NOVELS);
                        }
                    }

                    @Override
                    public void error(String errorMsg) {
                        // 请求失败，返回失败原因
                        mPresenter.getNovelsSourceError(errorMsg);
                    }
                })
                .build()
                .doRequest();
    }
}
