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
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.presenter.MalePresenter;
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
        Log.d(TAG, "getHotRankDataSuccess: run");
        mHotRankData = hotRankData;
        initHotRankAdapter();
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
        mNovelDataList = dataList;
        initCategoryAdapter();
    }

    /**
     * 获取分类小说数据失败
     */
    @Override
    public void getCategoryNovelsError(String errorMsg) {
        Log.d(TAG, "getCategoryNovelsError: " + errorMsg);
    }

    private void initHotRankAdapter() {
        if (mHotRankData == null) {
            return;
        }

        List<HotRankData.NovelInfo> novelInfoList = mHotRankData.getNovelInfoList();
        List<List<String>> hotRankNovelList = new ArrayList<>();
        for (HotRankData.NovelInfo novelInfo : novelInfoList) {
            hotRankNovelList.add(novelInfo.getNameList());
        }
        HotRankAdapter adapter = new HotRankAdapter(getActivity(),
               mHotRankData.getRankNameList(), hotRankNovelList);
        mHotRankRv.setAdapter(adapter);
    }

    private void initCategoryAdapter() {
        if (mCategoryAdapter == null) {
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
            });
            mCategoryNovelRv.setAdapter(mCategoryAdapter);
        }
    }
}
