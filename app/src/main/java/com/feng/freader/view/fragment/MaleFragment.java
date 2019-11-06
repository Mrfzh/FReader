package com.feng.freader.view.fragment;

import android.util.Log;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseTabFragment;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.presenter.MalePresenter;


/**
 * 发现页面的男生页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class MaleFragment extends BaseTabFragment<MalePresenter> implements IMaleContract.View {
    private static final String TAG = "fzh";

//    private TextView mContentTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_male;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        mContentTv = getActivity().findViewById(R.id.tv_content);
    }

    @Override
    protected void doInOnCreate() {
        mPresenter.getHotRankData();
    }

    @Override
    protected MalePresenter getPresenter() {
        return new MalePresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected int getPosition() {
        return 0;
    }

    @Override
    public void getHotRankDataSuccess(HotRankData hotRankData) {
//        mContentTv.setText(hotRankData.toString());
    }

    @Override
    public void getHotRankDataError(String errorMsg) {
        Log.d(TAG, "getHotRankDataError: " + errorMsg);
    }
}
