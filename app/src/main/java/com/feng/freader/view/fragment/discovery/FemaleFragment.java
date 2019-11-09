package com.feng.freader.view.fragment.discovery;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feng.freader.R;
import com.feng.freader.adapter.HotRankAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.base.BaseTabFragment;
import com.feng.freader.constract.IFemaleContract;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.presenter.FemalePresenter;

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

    private HotRankData mHotRankData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_female;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mHotRankRv = getActivity().findViewById(R.id.rv_female_hot_rank_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotRankRv.setLayoutManager(manager);
    }

    @Override
    protected void doInOnCreate() {
        mPresenter.getHotRankData();
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

    @Override
    public void getHotRankDataSuccess(HotRankData hotRankData) {
        mHotRankData = hotRankData;
        initHotRankAdapter();
    }

    @Override
    public void getHotRankDataError(String errorMsg) {
        showShortToast(errorMsg);
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
}
