package com.feng.freader.test;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

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

    private TestAdapter mAdapter;

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
        for (int i = 0; i < 20; i++) {
            mContentList.add("content " + i);
        }
    }

    @Override
    protected void initView() {
        mListRv = findViewById(R.id.rv_test_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));
        mListRv.addOnScrollListener(new LoadMoreScrollListener(new LoadMoreScrollListener.LoadMore() {
            @Override
            public void loadMore() {
                Log.d(TAG, "loadMore: run");
                if (mAdapter != null) {
                    // 加载更多
                    mAdapter.loadingMore();
                }
            }
        }));
        mAdapter = new TestAdapter(this, mContentList, new BasePagingLoadAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                update();
            }
        });
        mListRv.setAdapter(mAdapter);
    }

    private void update() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mContentList.add("content " + i);
                }
                // 更新列表
                mAdapter.updateList();
                mAdapter.setErrorStatus();
                mAdapter.setLastedStatus();
            }
        }, 1000);
    }

    @Override
    protected void doAfterInit() {
    }


    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

}
