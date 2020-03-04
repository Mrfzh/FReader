package com.feng.freader.model;

import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.ICatalogContract;
import com.feng.freader.entity.bean.CatalogBean;
import com.feng.freader.entity.bean.CategoryNovelsBean;
import com.feng.freader.entity.data.ANNovelData;
import com.feng.freader.entity.data.CatalogData;
import com.feng.freader.http.OkhttpBuilder;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/17
 */
public class CatalogModel implements ICatalogContract.Model {

    private ICatalogContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    public CatalogModel(ICatalogContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getCatalogData(String url) {
        Log.d("fzh", "getCatalogData: url = " + url);
        OkhttpBuilder builder = new OkhttpBuilder.Builder()
                .setUrl(url)
                .setOkhttpCall(new OkhttpCall() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            CatalogBean catalogBean = mGson.fromJson(json, CatalogBean.class);
                            if (catalogBean.getCode() != 0) {
                                mPresenter.getCatalogDataError(Constant.NOT_FOUND_CATALOG_INFO);
                                return;
                            }
                            List<CatalogBean.ListBean> list = catalogBean.getList();
                            List<String> chapterNameList = new ArrayList<>();
                            List<String> chapterUrlList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                chapterNameList.add(list.get(i).getNum());
                                chapterUrlList.add(list.get(i).getUrl());
                            }
                            mPresenter.getCatalogDataSuccess(new CatalogData(chapterNameList, chapterUrlList));
                        } catch (JsonSyntaxException e) {
                            mPresenter.getCatalogDataError(Constant.JSON_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        mPresenter.getCatalogDataError(errorMsg);
                    }
                })
                .build();
        OkhttpUtil.getRequest(builder);
    }
}
