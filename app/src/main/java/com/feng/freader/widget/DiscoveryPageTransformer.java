package com.feng.freader.widget;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.HashMap;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/2
 */
public class DiscoveryPageTransformer implements ViewPager.PageTransformer {

    private static final String TAG = "DiscoveryPageTransformer";
    // 非当前页文字相比当前页文字的倍数
    public static final float MIN_SCALE = 0.75f;

    private TabLayout mTabLayout;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Float> mLastMap = new HashMap<>(); // 存储各页面的上一

    public DiscoveryPageTransformer(TabLayout mTabLayout) {
        this.mTabLayout = mTabLayout;
    }

    @Override
    public void transformPage(@NonNull View view, float v) {
        if (v > -1 && v < 1) {
            int currPosition = (int) view.getTag();
            final float currV = Math.abs(v);
            if (!mLastMap.containsKey(currPosition)) {
                mLastMap.put(currPosition, currV);
                return;
            }
            float lastV = mLastMap.get(currPosition);
            // 获取当前 Tab 的 View
            View tabView = mTabLayout.getTabAt(currPosition).getCustomView();

            // 先判断是要变大还是变小
            // 如果 currV > lastV，则为变小；如果 currV < lastV，则为变大
            if (currV > lastV) {
                // 变小
                float leavePercent = currV; // 计算离开屏幕的百分比
                tabView.setScaleX(1.0f + (MIN_SCALE - 1.0f) * leavePercent);
                tabView.setScaleY(1.0f + (MIN_SCALE - 1.0f) * leavePercent);

            } else if (currV < lastV) {
                // 变大
                float enterPercent = 1 - currV; // 进入屏幕的百分比
                tabView.setScaleX(MIN_SCALE + (1.0f - MIN_SCALE) * enterPercent);
                tabView.setScaleY(MIN_SCALE + (1.0f - MIN_SCALE) * enterPercent);
            }
            mLastMap.put(currPosition, currV);
        }
    }
}
