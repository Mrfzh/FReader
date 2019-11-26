package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.model.ReadModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class ReadPresenter extends BasePresenter<IReadContract.View>
        implements IReadContract.Presenter {

    private IReadContract.Model mModel;

    public ReadPresenter() {
        mModel = new ReadModel(this);
    }

    @Override
    public void getDetailedChapterDataSuccess(DetailedChapterData data) {
        if (isAttachView()) {
            getMvpView().getDetailedChapterDataSuccess(data);
        }
    }

    @Override
    public void getDetailedChapterDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getDetailedChapterDataError(errorMsg);
        }
    }

    @Override
    public void getDetailedChapterData(String url) {
        mModel.getDetailedChapterData(url);
    }
}
