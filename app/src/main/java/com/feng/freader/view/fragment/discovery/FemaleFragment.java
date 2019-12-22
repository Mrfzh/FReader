package com.feng.freader.view.fragment.discovery;

import android.content.Intent;
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
import com.feng.freader.constract.IFemaleContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.presenter.FemalePresenter;
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现页面的女生页
 *
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class FemaleFragment extends BaseTabFragment<FemalePresenter>
        implements IFemaleContract.View {

    private RecyclerView mHotRankRv;
    private RecyclerView mCategoryNovelRv;
    private ProgressBar mProgressBar;

    private HotRankAdapter mHotRankAdapter;
    private List<List<String>> mHotRankNovelNameList;

    private CategoryAdapter mCategoryAdapter;
    private List<String> mCategoryNameList = new ArrayList<>();
    private List<String> mMoreList = new ArrayList<>();
    private List<DiscoveryNovelData> mNovelDataList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_female;
    }

    @Override
    protected void initData() {
        mCategoryNameList.add("古代言情");
        mCategoryNameList.add("现代言情");
        mCategoryNameList.add("青春校园");
        mMoreList.add("更多古代言情小说");
        mMoreList.add("更多现代言情小说");
        mMoreList.add("更多青春校园小说");
    }

    @Override
    protected void initView() {
        mHotRankRv = getActivity().findViewById(R.id.rv_female_hot_rank_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotRankRv.setLayoutManager(manager);

        mCategoryNovelRv = getActivity().findViewById(R.id.rv_female_category_novel_list);
        mCategoryNovelRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressBar = getActivity().findViewById(R.id.pb_female);
    }

    @Override
    protected void doInOnCreate() {
        mPresenter.getHotRankData();
        mPresenter.getCategoryNovels();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected FemalePresenter getPresenter() {
        return new FemalePresenter();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected int getPosition() {
        return 1;
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
    }

    /**
     * 获取热门排行榜数据失败
     */
    @Override
    public void getHotRankDataError(String errorMsg) {

    }

    /**
     * 获取分类小说数据成功
     */
    @Override
    public void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList) {
        mProgressBar.setVisibility(View.GONE);

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
    }

    /**
     * 获取分类小说数据失败
     */
    @Override
    public void getCategoryNovelsError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
    }

    private void initHotRankAdapter() {
        mHotRankAdapter = new HotRankAdapter(getActivity(),
                Constant.FEMALE_HOT_RANK_NAME, mHotRankNovelNameList, new HotRankAdapter.HotRankListener() {
            @Override
            public void clickFirstNovel(String name) {
                if (!name.equals("")) {
                    jump2Search(name);
                }
            }

            @Override
            public void clickSecondNovel(String name) {
                if (!name.equals("")) {
                    jump2Search(name);
                }
            }

            @Override
            public void clickThirdNovel(String name) {
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
                        // 跳转到该小说的搜索结果页
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra(SearchActivity.KEY_NOVEL_NAME, novelName);
                        startActivity(intent);
                    }

                    @Override
                    public void clickMore(int position) {
                        int gender = 1;
                        String major;
                        switch (position) {
                            case 0:
                                major = Constant.CATEGORY_MAJOR_GDYQ;
                                break;
                            case 1:
                                major = Constant.CATEGORY_MAJOR_XDYQ;
                                break;
                            case 2:
                                major = Constant.CATEGORY_MAJOR_QCXY;
                                break;
                            default:
                                major = Constant.CATEGORY_MAJOR_GDYQ;
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
