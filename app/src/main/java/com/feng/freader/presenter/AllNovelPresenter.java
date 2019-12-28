package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IAllNovelContract;
import com.feng.freader.entity.data.ANNovelData;
import com.feng.freader.entity.data.RequestCNData;
import com.feng.freader.model.AllNovelModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class AllNovelPresenter extends BasePresenter<IAllNovelContract.View>
        implements IAllNovelContract.Presenter {

    private IAllNovelContract.Model mModel;

    public AllNovelPresenter() {
        mModel = new AllNovelModel(this);
    }

    @Override
    public void getNovelsSuccess(List<ANNovelData> dataList, boolean isEnd) {
        if (isAttachView()) {
            getMvpView().getNovelsSuccess(dataList, isEnd);
        }
    }

    @Override
    public void getNovelsError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getNovelsError(errorMsg);
        }
    }

    @Override
    public void getNovels(RequestCNData requestCNData) {
        mModel.getNovels(requestCNData);
    }
}
