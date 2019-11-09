package com.feng.freader.view.fragment.discovery;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.base.BaseTabFragment;

/**
 * 发现页面的出版页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class PressFragment extends BaseTabFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_press;
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
        return 2;
    }
}
