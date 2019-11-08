package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IFemaleContract;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.model.FemaleModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/8
 */
public class FemalePresenter extends BasePresenter<IFemaleContract.View>
        implements IFemaleContract.Presenter{

    private IFemaleContract.Model mModel;

    public FemalePresenter() {
        mModel = new FemaleModel(this);
    }

    @Override
    public void getHotRankDataSuccess(HotRankData hotRankData) {
        if (isAttachView()) {
            getMvpView().getHotRankDataSuccess(hotRankData);
        }
    }

    @Override
    public void getHotRankDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getHotRankDataError(errorMsg);
        }
    }

    @Override
    public void getHotRankData() {
        mModel.getHotRankData();
    }
}
