package com.feng.freader.util;

import android.support.v7.widget.RecyclerView;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class RecyclerViewUtil {

    /**
     * 光滑地定位到某个位置（该位置将会位于 RV 顶部）
     *
     * @param recyclerView 要移动的 RV
     * @param position 要定位到的位置
     */
    public static void smoothScrollToPosition(RecyclerView recyclerView, int position) {
        // 第一个可见 item 的索引
        int firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        // 最后一个可见 item 的索引
        int lastItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(
                recyclerView.getChildCount() - 1));
        if (position < firstItem || position>lastItem) {
            recyclerView.smoothScrollToPosition(position);
        } else {
            // 当要移动的 item 位于屏幕中
            int movePosition = position - firstItem;
            int top = recyclerView.getChildAt(movePosition).getTop();
            recyclerView.smoothScrollBy(0, top);
        }
    }
}
