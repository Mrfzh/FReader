package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.model.MaleModel;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class MalePresenter extends BasePresenter<IMaleContract.View>
        implements IMaleContract.Presenter {

    private IMaleContract.Model mModel;

    public MalePresenter() {
        mModel = new MaleModel(this);
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
