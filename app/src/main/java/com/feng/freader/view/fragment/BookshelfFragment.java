package com.feng.freader.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class BookshelfFragment extends BaseFragment {

    private static final String TAG = "BookshelfFragment";

    private TextView mContentTv;

    public BookshelfFragment() {
        Log.d(TAG, "BookshelfFragment: run");
    }

    @Override
    protected void doInOnCreate() {
        Log.d(TAG, "doInOnCreate: run");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mContentTv = getActivity().findViewById(R.id.tv_bookshelf_content);
        mContentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentTv.setText("text changed");
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: run");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: run");
    }
}
