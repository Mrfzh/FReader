package com.feng.freader.test;

import android.util.Log;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private FlowLayout mFlowLayout;
    private List<String> mContentList = new ArrayList<>();

    @Override
    protected void doBeforeSetContentView() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
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
        for (int i = 0; i < 4; i++) {
            mContentList.add("java");
            mContentList.add("kotlin");
            mContentList.add("android");
            mContentList.add("android-studio");
            mContentList.add("app");
        }
    }

    @Override
    protected void initView() {
        mFlowLayout = findViewById(R.id.fv_test_flow_layout);
        // 设置 Adapter
        FlowAdapter adapter = new FlowAdapter(this, mContentList);
        mFlowLayout.setAdapter(adapter);
        // 设置最多显示的行数
        mFlowLayout.setMaxLines(3);
        // 获取显示的 item 数
        mFlowLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "count = " + mFlowLayout.getVisibleItemCount());
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
