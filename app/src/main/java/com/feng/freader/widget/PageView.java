package com.feng.freader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.feng.freader.R;
import com.feng.freader.util.BaseUtil;
import com.feng.freader.util.BlurUtil;

import java.util.HashMap;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class PageView extends View {

    private static final String TAG = "PageView";

    private Paint mPaint;
    private float mTextSize = 64f;      // 字体大小
    private float mRowSpace = 24f;     // 行距

    private PageViewListener mListener;
    private String mContent;    // 文本内容
    private boolean mIsShowContent = true;  // 是否显示文本内容

    private int mPosition = 0;  // 当前页第一个字的索引
    private int mNextPosition;  // 下一页第一个字的索引
    private int mPageIndex = 0; // 当前页的索引（第几页，并不一定从 0 开始，只是作为 hashMap 的 key 而存在）
    // 记录每页的第一个字的索引，key 为页号，value 为第一个字的索引
    private HashMap<Integer, Integer> mFirstPosMap = new HashMap<>();

    public interface PageViewListener {
        void updateProgress(String progress);     // 通知主活动更新进度
        void next();    // 显示下一章节
        void pre();     // 显示上一章节
        void showOrHideSettingBar();  // 弹出或隐藏设置栏
    }

    public void setPageViewListener(PageViewListener listener) {
        mListener = listener;
    }

    public PageView(Context context) {
        super(context);
        initPaint();
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.read_novel_text));
    }

    /**
     * 初始化
     *
     * @param content 文本
     * @param position 该页的首字索引
     */
    public void init(String content, int position) {
        mContent = content;
        mPosition = position;
        mIsShowContent = true;
        mPageIndex = 0;
        mFirstPosMap.clear();
        // 进行视图重绘
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!mIsShowContent || mContent == null || mContent.length() == 0) {
            return;
        }

        mPaint.setTextSize(mTextSize);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        int posRecord = mPosition;  // 记录当前页的头索引
        float width = getWidth();
        float height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();

        float currY = mTextSize + paddingTop;
        float currX = paddingStart;

        while (currY < height - paddingBottom && mPosition < mContent.length()) {
            // 绘制下一行
            float add; // 为了左右两端对齐每个字需要增加的距离
            int num = 0;    // 下一行的字数
            float textWidths = 0f;  // 下一行字体所占宽度
            boolean isNeed = false; // 是否需要填充
            // 计算 add 和 num
            for (int i = mPosition; i < mContent.length(); i++) {
                String currS = mContent.substring(i, i+1);
                if (currS.equals("\n")) {    // 换行
                    num++;
                    break;
                }
                float textWidth = getTextWidth(mPaint, currS);
                if (textWidths + textWidth >= width - paddingStart - paddingEnd) {  // 达到最大字数
                    isNeed = true;
                    break;
                }
                textWidths += textWidth;
                num++;
            }
            add = num <= 1? 0f : (width - paddingStart - paddingEnd - textWidths) / (num - 1);
            // 进行绘制
            for (int i = 0; i < num; i++) {
                String currS = mContent.substring(mPosition, mPosition+1);
                if (currS.equals("\n")) {
                    mPosition++;
                    continue;
                }
                canvas.drawText(currS, currX, currY, mPaint);
                if (isNeed) {
                    currX += getTextWidth(mPaint, currS) + add;
                } else {
                    currX += getTextWidth(mPaint, currS);
                }
                mPosition++;
            }
            currX = paddingStart;
            currY += mTextSize + mRowSpace;
        }

        // 更新相关变量
        mNextPosition = mPosition;
        mPosition = posRecord;
        mFirstPosMap.put(mPageIndex, mPosition);

        // 计算当前进度
        float f = (float) mNextPosition / (float) mContent.length();
        String progress;
        if (f < 0.1f) {
            progress = String.valueOf(f * 100).substring(0,4) + "%";
        } else {
            progress = String.valueOf(f * 100).substring(0,5) + "%";
        }
        if (mListener != null) {
            mListener.updateProgress(progress);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // 根据离开时的位置进行不同操作
                float rawX = event.getRawX();
                float screenWidth = BaseUtil.getScreenWidth();
                if (rawX <= 0.35f * screenWidth) {
                    // 上一页
                    pre();
                } else if (rawX >= 0.65f * screenWidth) {
                    // 下一页
                    next();
                } else {
                    // 弹出或隐藏菜单
                    mListener.showOrHideSettingBar();
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 绘制下一页
     */
    private void next() {
        mPosition = mNextPosition;
        if (mPosition >= mContent.length()) { // 已经到达最后
            mListener.next();   // 下一章节
            return;
        }
        mPageIndex++;
        invalidate();
    }

    /**
     * 绘制上一页
     */
    private void pre() {
        if (mPosition == 0) {  // 已经是第一页
            mListener.pre();    // 上一章节
            return;
        }
        mPageIndex--;
        if (mFirstPosMap.containsKey(mPageIndex)) {
            mPosition = mFirstPosMap.get(mPageIndex);
        } else {
            // mPosition 更新为上一页的首字符位置
            updatePrePageFirstPos();
        }

        invalidate();
    }

    /**
     * 获取字符串 str 利用 paint 绘制时的长度
     */
    private float getTextWidth(Paint paint, String str) {
        float res = 0;
        if (str != null && str.length() > 0) {
            float[] widths = new float[str.length()];
            paint.getTextWidths(str, widths);
            for (float width : widths) {
                res += width;
            }
        }
        return res;
    }

    /**
     * 获取当前页第一个字符的位置
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 获取当前页索引
     */
    public int getPageIndex() {
        return mPageIndex;
    }

    /**
     * 设置第一个字符的位置
     */
    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    /**
     * 清除所有内容
     */
    public void clear() {
        mIsShowContent = false;
        invalidate();
    }

    /**
     * 将 mPosition 更新为上一页的首字符位置
     */
    private void updatePrePageFirstPos() {
        int currPos = mPosition - 1;  // 当前页的字符位置
        float width = getWidth();
        float height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();

        float currY = height - paddingBottom;

        while (currY >= mTextSize + paddingTop && currPos >= 0) {
            // 绘制上一行
            int num = 0;    // 上一行的字数
            float textWidths = 0f;  // 上一行字体所占宽度
            for (int i = currPos; i >= 0; i--) {
                String currS = mContent.substring(i, i+1);
                if (currS.equals("\n")) {    // 换行
                    num++;
                    break;
                }
                float textWidth = getTextWidth(mPaint, currS);
                if (textWidths + textWidth >= width - paddingStart - paddingEnd) {  // 达到最大字数
                    break;
                }
                textWidths += textWidth;
                num++;
            }

            currPos -= num;
            currY -= mTextSize + mRowSpace;
        }

        // 更新
        mPosition = currPos - 1 < 0? 0 : currPos - 1;
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        mPaint.setColor(color);
    }
}
