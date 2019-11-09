package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.ISearchResultContract;
import com.feng.freader.entity.data.NovelSourceData;
import com.feng.freader.model.SearchResultModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class SearchResultPresenter extends BasePresenter<ISearchResultContract.View>
        implements ISearchResultContract.Presenter{

    private ISearchResultContract.Model mModel;

    public SearchResultPresenter() {
        mModel = new SearchResultModel(this);
    }

    @Override
    public void getNovelsSourceSuccess(List<NovelSourceData> novelSourceDataList) {
        if (isAttachView()) {
            getMvpView().getNovelsSourceSuccess(novelSourceDataList);
        }
    }

    @Override
    public void getNovelsSourceError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getNovelsSourceError(errorMsg);
        }
    }

    @Override
    public void getNovelsSource(String novelName) {
        mModel.getNovelsSource(novelName);
    }
}
