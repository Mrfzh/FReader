package com.feng.freader.model;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IAllNovelContract;
import com.feng.freader.entity.bean.CategoryNovelsBean;
import com.feng.freader.entity.data.ANNovelData;
import com.feng.freader.entity.data.RequestCNData;
import com.feng.freader.http.OkhttpBuilder;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.http.UrlObtainer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class AllNovelModel implements IAllNovelContract.Model {

    private IAllNovelContract.Presenter mPresenter;
    private Gson mGson;

    public AllNovelModel(IAllNovelContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mGson = new Gson();
    }

    /**
     * 获取小说信息
     */
    @Override
    public void getNovels(final RequestCNData requestCNData) {
        String url = UrlObtainer.getCategoryNovels(requestCNData.getGender(), requestCNData.getMajor(),
                requestCNData.getMinor(), requestCNData.getType(),
                requestCNData.getStart(), requestCNData.getNum());
        OkhttpBuilder builder = new OkhttpBuilder.Builder()
                .setUrl(url)
                .setOkhttpCall(new OkhttpCall() {
                    @Override
                    public void onResponse(String json) {   // 得到 json 数据
                        CategoryNovelsBean bean = mGson.fromJson(json, CategoryNovelsBean.class);
                        boolean isToEnd = false;
                        if (bean.getTotal() <= requestCNData.getStart() + Constant.NOVEL_PAGE_NUM - 1) {
                            isToEnd = true;
                        }
                        List<ANNovelData> dataList = new ArrayList<>();
                        List<CategoryNovelsBean.BooksBean> books = bean.getBooks();
                        for (int i = 0; i < Math.min(books.size(), requestCNData.getNum()); i++) {
                            dataList.add(new ANNovelData(books.get(i).getTitle(), books.get(i).getAuthor(),
                                    books.get(i).getShortIntro(),
                                    "http://statics.zhuishushenqi.com" + books.get(i).getCover()));
                        }
                        mPresenter.getNovelsSuccess(dataList, isToEnd);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        mPresenter.getNovelsError(errorMsg);
                    }
                })
                .build();
        OkhttpUtil.getRequest(builder);
    }
}
