package com.feng.freader.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.feng.freader.R;
import com.feng.freader.adapter.BookshelfNovelsAdapter;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.util.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private static final String TAG = "fzh";

    private RecyclerView mBookshelfNovelsRv;

    private List<String> mContentList = new ArrayList<>();

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
            mContentList.add("Content " + i);
        }
    }

    @Override
    protected void initView() {
        mBookshelfNovelsRv = findViewById(R.id.rv_test_bookshelf_novels);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == 0? 3 : 1;
                // 表示第 0 个 item 占 3 列（即占一整行），其他 item 占一列
            }
        });
        mBookshelfNovelsRv.setLayoutManager(gridLayoutManager);
        mBookshelfNovelsRv.setAdapter(new BookshelfNovelsAdapter(this, mContentList));
        mBookshelfNovelsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当滑动停止时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果第 0 个 item 不完全可见，则第 1 个 item 上滑，隐藏第 0 个 item
                    if (gridLayoutManager.findFirstVisibleItemPosition() == 0 &&
                        gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                        Log.d(TAG, "onScrollStateChanged: run");
                        RecyclerViewUtil.smoothScrollToPosition(mBookshelfNovelsRv, 1);
                    }
                }
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
