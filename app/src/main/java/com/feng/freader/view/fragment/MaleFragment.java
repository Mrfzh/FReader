package com.feng.freader.view.fragment;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.base.BaseTabFragment;

/**
 * 发现页面的男生页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class MaleFragment extends BaseTabFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_male;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected int getPosition() {
        return 0;
    }
}
