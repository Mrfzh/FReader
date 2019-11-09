package com.feng.freader.view.fragment.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
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

}
