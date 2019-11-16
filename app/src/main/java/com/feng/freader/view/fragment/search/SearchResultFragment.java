package com.feng.freader.view.fragment.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.NovelSourceAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.constant.Constant;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.constract.ISearchResultContract;
import com.feng.freader.entity.data.NovelSourceData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.NovelIntroInitEvent;
import com.feng.freader.presenter.SearchResultPresenter;
import com.feng.freader.util.EventBusUtil;
import com.feng.freader.view.activity.NovelIntroActivity;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class SearchResultFragment extends BaseFragment<SearchResultPresenter>
        implements ISearchResultContract.View {
    private static final String TAG = "SearchResultFragment";
    private static final String KEY_SEARCH_CONTENT = "tag_search_content";

    private ProgressBar mProgressBar;
    private RecyclerView mNovelSourceRv;
    private TextView mNoneTv;       // 没有找到相关小说时显示

    private NovelSourceAdapter mNovelSourceAdapter;

    private String mSearchContent;  // 搜索内容
    private List<NovelSourceData> mNovelSourceDataList; // 小说源列表

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mNoneTv = getActivity().findViewById(R.id.tv_search_result_none);
        mProgressBar = getActivity().findViewById(R.id.pb_search_result_progress_bar);

        mNovelSourceRv = getActivity().findViewById(R.id.rv_search_result_novel_source_list);
        mNovelSourceRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void doInOnCreate() {
        mPresenter.getNovelsSource(mSearchContent);
    }

    @Override
    protected SearchResultPresenter getPresenter() {
        return new SearchResultPresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    public static SearchResultFragment newInstance(String searchContent) {
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SEARCH_CONTENT, searchContent);
        searchResultFragment.setArguments(bundle);

        return searchResultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSearchContent = getArguments().getString(KEY_SEARCH_CONTENT);
        }
    }

    @Override
    public void getNovelsSourceSuccess(List<NovelSourceData> novelSourceDataList) {
        mNoneTv.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        // 列表显示小说源
        mNovelSourceDataList = novelSourceDataList;
        initAdapter();
    }

    @Override
    public void getNovelsSourceError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if (errorMsg.equals(Constant.NOT_FOUND_NOVELS)) {
            // 没有找到相关小说，设置状态页
            mNoneTv.setVisibility(View.VISIBLE);
        } else {
            // 其他错误
            showShortToast(errorMsg);
        }
    }

    private void initAdapter() {
        if (mNovelSourceDataList == null || mNovelSourceDataList.isEmpty()) {
            return;
        }

        mNovelSourceAdapter = new NovelSourceAdapter(getActivity(), mNovelSourceDataList);
        mNovelSourceAdapter.setOnNovelSourceListener(new NovelSourceAdapter.NovelSourceListener() {
            @Override
            public void clickItem(int position) {
                if (mNovelSourceDataList.isEmpty()) {
                    return;
                }
                Event<NovelIntroInitEvent> event = new Event<>(EventBusCode.NOVEL_INTRO_INIT,
                        new NovelIntroInitEvent(mNovelSourceDataList.get(position)));
                EventBusUtil.sendStickyEvent(event);
                jump2Activity(NovelIntroActivity.class);
            }
        });
        mNovelSourceRv.setAdapter(mNovelSourceAdapter);
    }

    public void update(String novelName) {
        mPresenter.getNovelsSource(novelName);
        mProgressBar.setVisibility(View.VISIBLE);
    }
}
