package com.feng.freader.view.fragment.discovery;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;

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

    private RecyclerView mHotRankRv;
    private RecyclerView mCategoryNovelRv;

    private HotRankAdapter mHotRankAdapter;
    private HotRankData mHotRankData;

    private CategoryAdapter mCategoryAdapter;
    private List<String> mCategoryNameList = new ArrayList<>();
    private List<String> mMoreList = new ArrayList<>();
    private List<DiscoveryNovelData> mNovelDataList = new ArrayList<>();

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
    }

    @Override
    protected void initView() {
        mHotRankRv = getActivity().findViewById(R.id.rv_male_hot_rank_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotRankRv.setLayoutManager(manager);

        mCategoryNovelRv = getActivity().findViewById(R.id.rv_male_category_novel_list);
        mCategoryNovelRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void doInOnCreate() {
        Log.d(TAG, "doInOnCreate: run");
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
     * 获取热门榜单数据成功
     */
    @Override
    public void getHotRankDataSuccess(HotRankData hotRankData) {
        if (hotRankData == null) {
            return;
        }
        mHotRankData = hotRankData;
        initHotRankAdapter();
//        if (mHotRankAdapter == null) {
//            initHotRankAdapter();
//        } else {
//
//        }
    }

    /**
     * 获取热门榜单数据失败
     */
    @Override
    public void getHotRankDataError(List<String> errorMsgList) {

    }

    /**
     * 获取分类小说数据成功
     */
    @Override
    public void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList) {
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
        Log.d(TAG, "getCategoryNovelsError: " + errorMsg);
    }

    private void initHotRankAdapter() {
        List<HotRankData.NovelInfo> novelInfoList = mHotRankData.getNovelInfoList();
        List<List<String>> hotRankNovelList = new ArrayList<>();
        for (HotRankData.NovelInfo novelInfo : novelInfoList) {
            hotRankNovelList.add(novelInfo.getNameList());
        }
        mHotRankAdapter = new HotRankAdapter(getActivity(),
               mHotRankData.getRankNameList(), hotRankNovelList);
        mHotRankRv.setAdapter(mHotRankAdapter);
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
