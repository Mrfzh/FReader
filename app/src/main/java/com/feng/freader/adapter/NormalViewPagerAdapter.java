package com.feng.freader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/4
 */
public class NormalViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;   // 碎片集合
    private List<String> mPageTitleList;    // tab 的标题集合

    public NormalViewPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mPageTitleList) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mPageTitleList = mPageTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * 当TabLayout与ViewPager绑定的时候能够绑定Tab标签的标题
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitleList.get(position);
    }
}
