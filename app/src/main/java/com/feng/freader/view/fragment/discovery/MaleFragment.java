package com.feng.freader.view.fragment.discovery;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.feng.freader.R;
import com.feng.freader.adapter.CatalogAdapter;
import com.feng.freader.adapter.CategoryAdapter;
import com.feng.freader.adapter.HotRankAdapter;
import com.feng.freader.base.BaseTabFragment;
import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.presenter.MalePresenter;
import com.feng.freader.util.ACache;
import com.feng.freader.util.NetUtil;
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 发现页面的男生页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class MaleFragment extends BaseTabFragment<MalePresenter> implements IMaleContract.View {
    private static final String TAG = "MaleFragment";
//    private static final String TAG = "TestFragment";

    private static final String KEY_CACHE_HR = "key_cache_male_hr";
    private static final String KEY_CACHE_CN = "key_cache_male_cn";

    private RecyclerView mHotRankRv;
    private RecyclerView mCategoryNovelRv;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshSrv;

    private HotRankAdapter mHotRankAdapter;
    private List<List<String>> mHotRankNovelNameList;

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
        return R.layout.fragment_male;
    }

    @Override
    protected void initData() {
        mCategoryNameList.add("热血玄幻");
        mCategoryNameList.add("都市生活");
        mCategoryNameList.add("武侠世界");
        mMoreList.add("更多玄幻小说");
        mMoreList.add("更多都市小说");
        mMoreList.add("更多武侠小说");

        mCache = ACache.get(getActivity());
    }

    @Override
    protected void initView() {
        mHotRankRv = getActivity().findViewById(R.id.rv_male_hot_rank_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotRankRv.setLayoutManager(manager);

        mCategoryNovelRv = getActivity().findViewById(R.id.rv_male_category_novel_list);
        mCategoryNovelRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressBar = getActivity().findViewById(R.id.pb_male);

        mRefreshSrv = getActivity().findViewById(R.id.srv_male_refresh);
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
        mPresenter.getHotRankData();
        mPresenter.getCategoryNovels();
    }

    @Override
    protected MalePresenter getPresenter() {
        return new MalePresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected int getPosition() {
        return 0;
    }


    /**
     * 获取热门排行榜数据成功
     */
    @Override
    public void getHotRankDataSuccess(List<List<String>> novelNameList) {
        if (novelNameList.isEmpty()) {
            return;
        }
        if (mHotRankAdapter == null) {
            mHotRankNovelNameList = novelNameList;
            initHotRankAdapter();
        } else {
            mHotRankNovelNameList.clear();
            mHotRankNovelNameList.addAll(novelNameList);
            mHotRankAdapter.notifyDataSetChanged();
        }

        mCache.put(KEY_CACHE_HR, (Serializable) novelNameList);
    }

    /**
     * 获取热门排行榜数据失败
     */
    @Override
    public void getHotRankDataError(String errorMsg) {
        Log.d(TAG, "getHotRankDataError: run 1");
        List<List<String>> novelNameList =
                (List<List<String>>) mCache.getAsObject(KEY_CACHE_HR);
        if (novelNameList == null || novelNameList.isEmpty()) {
            Log.d(TAG, "getHotRankDataError: run 2");
            return;
        }
        if (mHotRankAdapter == null) {
            mHotRankNovelNameList = novelNameList;
            initHotRankAdapter();
        } else {
            mHotRankNovelNameList.clear();
            mHotRankNovelNameList.addAll(novelNameList);
            mHotRankAdapter.notifyDataSetChanged();
        }
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

    private void initHotRankAdapter() {
        mHotRankAdapter = new HotRankAdapter(getActivity(),
                Constant.MALE_HOT_RANK_NAME, mHotRankNovelNameList, new HotRankAdapter.HotRankListener() {
            @Override
            public void clickFirstNovel(String name) {
                if (mRefreshSrv.isRefreshing()) {
                    return;
                }
                if (!NetUtil.hasInternet(getActivity())) {
                    showShortToast("当前无网络，请检查网络后重试");
                    return;
                }
                if (!name.equals("")) {
                    jump2Search(name);
                }
            }

            @Override
            public void clickSecondNovel(String name) {
                if (mRefreshSrv.isRefreshing()) {
                    return;
                }
                if (!NetUtil.hasInternet(getActivity())) {
                    showShortToast("当前无网络，请检查网络后重试");
                    return;
                }
                if (!name.equals("")) {
                    jump2Search(name);
                }
            }

            @Override
            public void clickThirdNovel(String name) {
                if (mRefreshSrv.isRefreshing()) {
                    return;
                }
                if (!NetUtil.hasInternet(getActivity())) {
                    showShortToast("当前无网络，请检查网络后重试");
                    return;
                }
                if (!name.equals("")) {
                    jump2Search(name);
                }
            }
        });
        mHotRankRv.setAdapter(mHotRankAdapter);
    }

    private void jump2Search(String name) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        // 传递小说名，进入搜查页后直接显示该小说的搜查结果
        intent.putExtra(SearchActivity.KEY_NOVEL_NAME, name);
        startActivity(intent);
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
                        int gender = 0;
                        String major;
                        switch (position) {
                            case 0:
                                major = Constant.CATEGORY_MAJOR_XH;
                                break;
                            case 1:
                                major = Constant.CATEGORY_MAJOR_DS;
                                break;
                            case 2:
                                major = Constant.CATEGORY_MAJOR_WX;
                                break;
                            default:
                                major = Constant.CATEGORY_MAJOR_XH;
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
