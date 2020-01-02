package com.feng.freader.view.fragment.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.NormalViewPagerAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.rewrite.TabLayout;
import com.feng.freader.util.NetUtil;
import com.feng.freader.view.activity.AllNovelActivity;
import com.feng.freader.view.activity.SearchActivity;
import com.feng.freader.view.fragment.discovery.FemaleFragment;
import com.feng.freader.view.fragment.discovery.MaleFragment;
import com.feng.freader.view.fragment.discovery.PressFragment;
import com.feng.freader.widget.DiscoveryPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class DiscoveryFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "fzh";
    private View mSearchView;
    private TextView mAllBookTv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();   // 碎片集合
    private List<String> mPageTitleList = new ArrayList<>();    // tab 的标题集合

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initData() {
        mFragmentList.add(new MaleFragment());
        mFragmentList.add(new FemaleFragment());
        mFragmentList.add(new PressFragment());

        mPageTitleList.add(getString(R.string.discovery_male));
        mPageTitleList.add(getString(R.string.discovery_female));
        mPageTitleList.add(getString(R.string.discovery_press));
    }

    @Override
    protected void initView() {
        mSearchView = getActivity().findViewById(R.id.v_discovery_search_bg);
        mSearchView.setOnClickListener(this);
        mAllBookTv = getActivity().findViewById(R.id.tv_discovery_all_book);
        mAllBookTv.setOnClickListener(this);

        // TabLayout + ViewPager
        mViewPager = getActivity().findViewById(R.id.vp_discovery_view_pager);
        // 在 Fragment 中只能使用 getChildFragmentManager() 获取 FragmentManager 来处理子 Fragment
        mViewPager.setAdapter(new NormalViewPagerAdapter(getChildFragmentManager(),
                mFragmentList, mPageTitleList));
        // 缓存左右两侧的两个页面（很重要！！！，不设置这个切换到前两个的时候就会重新加载数据）
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout = getActivity().findViewById(R.id.tv_discovery_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setScale(0, DiscoveryPageTransformer.MAX_SCALE);
            }
        });

        Log.d(TAG, "initView: run");
        mViewPager.setPageTransformer(false, new DiscoveryPageTransformer(mTabLayout));
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_discovery_search_bg:
                jump2Activity(SearchActivity.class);
                break;
            case R.id.tv_discovery_all_book:
                if (!NetUtil.hasInternet(getActivity())) {
                    showShortToast("当前无网络，请检查网络后重试");
                    return;
                }
                jump2Activity(AllNovelActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 将 Tab[index] 放大为初始的 scale 倍
     */
    private void setScale(int index, float scale) {
        LinearLayout ll = (LinearLayout) mTabLayout.getChildAt(0);
        TabLayout.TabView tb = (TabLayout.TabView) ll.getChildAt(0);
        View view  = tb.getTextView();
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
