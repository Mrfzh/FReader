package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IPressContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.model.PressModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/22
 */
public class PressPresenter extends BasePresenter<IPressContract.View>
        implements IPressContract.Presenter {

    private IPressContract.Model mModel;

    public PressPresenter() {
        mModel = new PressModel(this);
    }

    @Override
    public void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList) {
        if (isAttachView()) {
            getMvpView().getCategoryNovelsSuccess(dataList);
        }
    }

    @Override
    public void getCategoryNovelsError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getCategoryNovelsError(errorMsg);
        }
    }

    @Override
    public void getCategoryNovels() {
        mModel.getCategoryNovels();
    }
}
