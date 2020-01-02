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
import com.feng.freader.adapter.CategoryAdapter;
import com.feng.freader.adapter.HotRankAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.base.BaseTabFragment;
import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IPressContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.presenter.PressPresenter;
import com.feng.freader.util.NetUtil;
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;

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

    private RecyclerView mCategoryNovelRv;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshSrv;

    private CategoryAdapter mCategoryAdapter;
    private List<String> mCategoryNameList = new ArrayList<>();
    private List<String> mMoreList = new ArrayList<>();
    private List<DiscoveryNovelData> mNovelDataList = new ArrayList<>();

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
        requestUpdate();
        mProgressBar.setVisibility(View.VISIBLE);
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
        Log.d(TAG, "getCategoryNovelsSuccess: run");
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);

        if (dataList.isEmpty()) {
            Log.d(TAG, "getCategoryNovelsSuccess: run 2");
            return;
        }
        if (mCategoryAdapter == null) {
            Log.d(TAG, "getCategoryNovelsSuccess: run 3");
            mNovelDataList = dataList;
            initCategoryAdapter();
        } else {
            Log.d(TAG, "getCategoryNovelsSuccess: run 4");
            mNovelDataList.clear();
            mNovelDataList.addAll(dataList);
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取分类小说数据失败
     */
    @Override
    public void getCategoryNovelsError(String errorMsg) {
        Log.d(TAG, "getCategoryNovelsError: run");
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);
    }

    private void initCategoryAdapter() {
        Log.d(TAG, "initCategoryAdapter: run 1");
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
        Log.d(TAG, "initCategoryAdapter: run 2");
        mCategoryNovelRv.setAdapter(mCategoryAdapter);
    }
}
