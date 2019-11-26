package com.feng.freader.view.fragment.main;

import android.content.Intent;
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
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.util.RecyclerViewUtil;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.view.activity.ReadActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class BookshelfFragment extends BaseFragment {

    private static final String TAG = "BookshelfFragment";

    private RecyclerView mBookshelfNovelsRv;

    private List<BookshelfNovelDbData> mDataList = new ArrayList<>();
    private DatabaseManager mDbManager;
    private BookshelfNovelsAdapter mBookshelfNovelsAdapter;

    @Override
    protected void doInOnCreate() {
        StatusBarUtil.setLightColorStatusBar(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initData() {
        mDbManager = DatabaseManager.getInstance();
        mDataList = mDbManager.queryAllBookshelfNovel();
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
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.BOOKSHELF_UPDATE_LIST:
                updateList();
                break;
            default:
                break;
        }
    }

    private void initBookshelfNovelsRv() {
        mBookshelfNovelsRv = getActivity().findViewById(R.id.rv_bookshelf_bookshelf_novels_list);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mBookshelfNovelsRv.setLayoutManager(gridLayoutManager);
        initAdapter();
        mBookshelfNovelsRv.setAdapter(mBookshelfNovelsAdapter);
    }

    private void initAdapter() {
        mBookshelfNovelsAdapter = new BookshelfNovelsAdapter(getActivity(), mDataList);
        mBookshelfNovelsAdapter.setBookshelfNovelListener(new BookshelfNovelsAdapter.BookshelfNovelListener() {
            @Override
            public void clickItem(int position) {
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                intent.putExtra(ReadActivity.KEY_NOVEL_URL, mDataList.get(position).getNovelUrl());
                intent.putExtra(ReadActivity.KEY_NAME, mDataList.get(position).getName());
                intent.putExtra(ReadActivity.KEY_COVER, mDataList.get(position).getCover());
                intent.putExtra(ReadActivity.KEY_CHAPTER_URL, mDataList.get(position).getChapterUrl());
                intent.putExtra(ReadActivity.KEY_POSITION, mDataList.get(position).getPosition());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新列表信息
     */
    private void updateList() {
        mDataList.clear();
        mDataList.addAll(mDbManager.queryAllBookshelfNovel());
        mBookshelfNovelsAdapter.notifyDataSetChanged();
    }

}
