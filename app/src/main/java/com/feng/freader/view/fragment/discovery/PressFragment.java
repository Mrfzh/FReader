package com.feng.freader.view.fragment.discovery;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.CategoryAdapter;
import com.feng.freader.adapter.HotRankAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.base.BaseTabFragment;
import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IPressContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.presenter.PressPresenter;
import com.feng.freader.util.ACache;
import com.feng.freader.util.NetUtil;
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发现页面的出版页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class PressFragment extends BaseTabFragment<PressPresenter>
        implements IPressContract.View {
    private static final String TAG = "PressFragment";
//    private static final String TAG = "TestFragment";

    private static final String KEY_CACHE_CN = "key_cache_press_cn";

    private RecyclerView mCategoryNovelRv;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshSrv;

    private CategoryAdapter mCategoryAdapter;
    private List<String> mCategoryNameList = new ArrayList<>();
    private List<String> mMoreList = new ArrayList<>();
    private List<DiscoveryNovelData> mNovelDataList = new ArrayList<>();

    private boolean mIsVisited = false;
    private boolean mIsLoadedData = false;
    private boolean mIsCreatedView = false;

    private ACache mCache;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_press;
    }

    @Override
    protected void initData() {
        mCategoryNameList.add("出版小说");
        mCategoryNameList.add("青春言情");
        mCategoryNameList.add("传记名著");
        mCategoryNameList.add("人文社科");
        mMoreList.add("更多出版小说");
        mMoreList.add("更多青春言情");
        mMoreList.add("更多传记名著");
        mMoreList.add("更多人文社科");

        mCache = ACache.get(getActivity());
    }

    @Override
    protected void initView() {
        mCategoryNovelRv = getActivity().findViewById(R.id.rv_press_category_novel_list);
        mCategoryNovelRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressBar = getActivity().findViewById(R.id.pb_press);

        mRefreshSrv = getActivity().findViewById(R.id.srv_press_refresh);
        mRefreshSrv.setColorSchemeColors(getResources().getColor(R.color.colorAccent));   //设置颜色
        mRefreshSrv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新时的操作
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestUpdate();
                    }
                }, 500);
            }
        });
    }

    @Override
    protected void doInOnCreate() {
        mIsCreatedView = true;
        if (mIsVisited && !mIsLoadedData) {
            requestUpdate();
            mProgressBar.setVisibility(View.VISIBLE);
            mIsLoadedData = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisited = true;
        }
        if (mIsVisited && !mIsLoadedData && mIsCreatedView) {
            requestUpdate();
            mProgressBar.setVisibility(View.VISIBLE);
            mIsLoadedData = true;
        }
    }

    private void requestUpdate() {
        mPresenter.getCategoryNovels();
    }

    @Override
    protected PressPresenter getPresenter() {
        return new PressPresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected int getPosition() {
        return 2;
    }

    /**
     * 获取分类小说数据成功
     */
    @Override
    public void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);

        if (dataList.isEmpty()) {
            return;
        }
        if (mCategoryAdapter == null) {
            mNovelDataList = dataList;
            initCategoryAdapter();
        } else {
            mNovelDataList.clear();
            mNovelDataList.addAll(dataList);
            mCategoryAdapter.notifyDataSetChanged();
        }

        mCache.put(KEY_CACHE_CN, (Serializable) dataList);
    }

    /**
     * 获取分类小说数据失败
     */
    @Override
    public void getCategoryNovelsError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);

        List<DiscoveryNovelData> dataList =
                (List<DiscoveryNovelData>) mCache.getAsObject(KEY_CACHE_CN);

        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        if (mCategoryAdapter == null) {
            mNovelDataList = dataList;
            initCategoryAdapter();
        } else {
            mNovelDataList.clear();
            mNovelDataList.addAll(dataList);
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    private void initCategoryAdapter() {
        mCategoryAdapter = new CategoryAdapter(getActivity(),
                mCategoryNameList, mMoreList, mNovelDataList,
                new CategoryAdapter.CategoryListener() {
                    @Override
                    public void clickNovel(String novelName) {
                        if (mRefreshSrv.isRefreshing()) {
                            return;
                        }
                        if (!NetUtil.hasInternet(getActivity())) {
                            showShortToast("当前无网络，请检查网络后重试");
                            return;
                        }
                        // 跳转到该小说的搜索结果页
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra(SearchActivity.KEY_NOVEL_NAME, novelName);
                        startActivity(intent);
                    }

                    @Override
                    public void clickMore(int position) {
                        if (mRefreshSrv.isRefreshing()) {
                            return;
                        }
                        if (!NetUtil.hasInternet(getActivity())) {
                            showShortToast("当前无网络，请检查网络后重试");
                            return;
                        }
                        int gender = 2;
                        String major;
                        switch (position) {
                            case 0:
                                major = Constant.CATEGORY_MAJOR_CBXS;
                                break;
                            case 1:
                                major = Constant.CATEGORY_MAJOR_QCYQ;
                                break;
                            case 2:
                                major = Constant.CATEGORY_MAJOR_ZJMZ;
                                break;
                            default:
                                major = Constant.CATEGORY_MAJOR_RWSK;
                                break;
                        }
                        // 跳转到全部小说页面
                        Intent intent = new Intent(getActivity(), AllNovelActivity.class);
                        intent.putExtra(AllNovelActivity.KEY_GENDER, gender);   // 性别
                        intent.putExtra(AllNovelActivity.KEY_MAJOR, major);     // 一级分类
                        startActivity(intent);
                    }
                });
        mCategoryNovelRv.setAdapter(mCategoryAdapter);
    }
}
