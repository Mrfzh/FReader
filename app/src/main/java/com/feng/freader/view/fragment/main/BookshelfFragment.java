package com.feng.freader.view.fragment.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.BookshelfNovelsAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.util.RecyclerViewUtil;
import com.feng.freader.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class BookshelfFragment extends BaseFragment {

    private static final String TAG = "BookshelfFragment";

    private RecyclerView mBookshelfNovelsRv;

    private List<String> mContentList = new ArrayList<>();

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 20; i++) {
            mContentList.add("Content " + i);
        }
    }

    @Override
    protected void initView() {
        initBookshelfNovelsRv();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    private void initBookshelfNovelsRv() {
        mBookshelfNovelsRv = getActivity().findViewById(R.id.rv_bookshelf_bookshelf_novels_list);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == 0? 3 : 1;
                // 表示第 0 个 item 占 3 列（即占一整行），其他 item 占一列
            }
        });
        mBookshelfNovelsRv.setLayoutManager(gridLayoutManager);
        mBookshelfNovelsRv.setAdapter(new BookshelfNovelsAdapter(getActivity(), mContentList));
        mBookshelfNovelsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当滑动停止时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果第 0 个 item 不完全可见，则第 1 个 item 上滑，隐藏第 0 个 item
                    if (gridLayoutManager.findFirstVisibleItemPosition() == 0 &&
                            gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                        RecyclerViewUtil.smoothScrollToPosition(mBookshelfNovelsRv, 1);
                    }
                }
            }
        });
    }
}
