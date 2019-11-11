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
import com.feng.freader.adapter.HistoryAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.test.FlowAdapter;
import com.feng.freader.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    private FlowLayout mHistoryListFv;

    private List<String> mContentList = new ArrayList<>();  // 搜索内容集合

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 10; i++) {
            mContentList.add("java");
            mContentList.add("kotlin");
            mContentList.add("android");
            mContentList.add("android-studio");
            mContentList.add("app");
        }
    }

    @Override
    protected void initView() {
        mHistoryListFv = getActivity().findViewById(R.id.fv_history_history_list);
        // 设置 Adapter
        HistoryAdapter adapter = new HistoryAdapter(getActivity(), mContentList);
        mHistoryListFv.setAdapter(adapter);
        // 设置最多显示的行数
        mHistoryListFv.setMaxLines(3);
        // 获取显示的 item 数
        mHistoryListFv.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "count = " + mHistoryListFv.getVisibleItemCount());
            }
        });
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
