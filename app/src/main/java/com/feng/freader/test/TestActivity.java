package com.feng.freader.test;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePagingLoadAdapter;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.widget.LoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private RecyclerView mListRv;
    private List<String> mContentList = new ArrayList<>();
    private List<Boolean> mCheckedList = new ArrayList<>();


    @Override
    protected void doBeforeSetContentView() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 100; i++) {
            mContentList.add("content " + i);
            mCheckedList.add(false);
        }
    }

    @Override
    protected void initView() {
        mListRv = findViewById(R.id.rv_test_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));
        mListRv.setAdapter(new TestAdapter(this, mContentList, mCheckedList));

        Button button = findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: mCheckedList = " + mCheckedList);
            }
        });
    }

    @Override
    protected void doAfterInit() {
    }


    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

}
