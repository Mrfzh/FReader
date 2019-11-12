package com.feng.freader.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/10
 */
public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";

    private List<Rect> mChildrenPositionList = new ArrayList<>();   // 记录各子 View 的位置
    private int mMaxLines = Integer.MAX_VALUE;      // 最多显示的行数，默认无限制
    private int mVisibleItemCount;       // 可见的 item 数
    private Adapter mAdapter;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 清除之前的位置
        mChildrenPositionList.clear();
        // 测量所有子元素（这样 child.getMeasuredXXX 才能获取到值）
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int[] a = helper(widthSize);
        int measuredHeight = 0;
        // EXACTLY 模式：对应指定大小和 match_parent
        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        }
        // AT_MOST 模式，对应 wrap_content
        else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = a[0];
        }
        int measuredWidth = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        }
        else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = a[1];
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * 在 wrap_content 情况下，得到布局的测量高度和测量宽度
     * 返回值是一个有两个元素的数组 a，a[0] 代表测量高度，a[1] 代表测量宽度
     */
    private int[] helper(int widthSize) {
        boolean isOneRow = true;    // 是否是单行
        int width = getPaddingLeft();   // 记录当前行已有的宽度
        int height = getPaddingTop();   // 记录当前行已有的高度
        int maxHeight = 0;      // 记录当前行的最大高度
        int currLine = 1;       // 记录当前行数

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // 获取当前子元素的 margin
            LayoutParams params = child.getLayoutParams();
            MarginLayoutParams mlp;
            if (params instanceof MarginLayoutParams) {
                mlp = (MarginLayoutParams) params;
            } else {
                mlp = new MarginLayoutParams(params);
            }
            // 记录子元素所占宽度和高度
            int childWidth = mlp.leftMargin + child.getMeasuredWidth() + mlp.rightMargin;
            int childHeight = mlp.topMargin + child.getMeasuredHeight() + mlp.bottomMargin;
            maxHeight = Math.max(maxHeight, childHeight);

            // 判断是否要换行
            if (width + childWidth + getPaddingRight() > widthSize) {
                // 加上该行的最大高度
                height += maxHeight;
                // 重置 width 和 maxHeight
                width = getPaddingLeft();
                maxHeight = childHeight;
                isOneRow = false;
                currLine++;
                if (currLine > mMaxLines) {
                    break;
                }
            }
            // 存储该子元素的位置，在 onLayout 时设置
            Rect rect = new Rect(width + mlp.leftMargin,
                    height + mlp.topMargin,
                    width + childWidth - mlp.rightMargin,
                    height + childHeight - mlp.bottomMargin);
            mChildrenPositionList.add(rect);

            // 加上该子元素的宽度
            width += childWidth;
        }

        int[] res = new int[2];
        res[0] = height + maxHeight + getPaddingBottom();
        res[1] = isOneRow? width + getPaddingRight() : widthSize;

        return res;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 布置子 View 的位置
        int n = Math.min(getChildCount(), mChildrenPositionList.size());
        for (int i = 0; i < n; i++) {
            View child = getChildAt(i);
            Rect rect = mChildrenPositionList.get(i);
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
        mVisibleItemCount = n;
    }

    /**
     * 设置 Adapter
     */
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        updateView();
    }

    /**
     * 更新列表视图
     */
    public void updateView() {
        if (mAdapter == null) {
            return;
        }
        // 移除之前的视图
        removeAllViews();
        // 添加 item
        int n = mAdapter.getItemCount();
        for (int i = 0; i < n; i++) {
            ViewHolder holder = mAdapter.onCreateViewHolder(this);
            mAdapter.onBindViewHolder(holder, i);
            View child = holder.itemView;
            addView(child);
        }
    }

    /**
     * 设置最多显示的行数
     */
    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
    }

    /**
     * 获取显示的 item 数
     */
    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    public abstract static class Adapter<VH extends ViewHolder> {

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(VH holder, int position);

        public abstract int getItemCount();
    }

    public abstract static class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }
    }
}
