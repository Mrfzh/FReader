package com.feng.freader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.feng.freader.R;
import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.util.FileUtil;
import com.feng.freader.util.ScreenUtil;
import com.feng.freader.util.SpUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class PageView extends View {

    private static final String TAG = "PageView";
    private static final int TYPE_TXT = 0;  // 网络小说也属于 txt
    private static final int TYPE_EPUB = 1;

    private Paint mPaint;
    private float mTextSize;      // 字体大小
    private float mRowSpace;     // 行距

    private PageViewListener mListener;
    private boolean mIsShowContent = true;  // 是否显示文本内容
    // 0 为绘制普通文本（网络小说和本地 txt），1 为绘制 epub 文本（本地 epub）
    private int mType;

    /* 纯文本绘制用 */
    private String mContent = "";    // 文本内容
    private int mPosition = 0;  // 当前页第一个字的索引
    private int mNextPosition;  // 下一页第一个字的索引

    /* epub 绘制用 */
    private List<EpubData> mEpubDataList = new ArrayList<>();   // epub 内容
    private int mFirstPos;      // 第一位置索引，指向某个 EpubData
    private int mSecondPos;     // 第二位置索引，指向 EpubData 内部字符串
    private int mNextFirstPos;
    private int mNextSecondPos;


    // 当前页的索引（第几页，并不一定从 0 开始，只是作为 hashMap 的 key 而存在）
    private int mPageIndex = 0;
    private HashMap<Integer, Integer> mFirstPosMap = new HashMap<>();
    private HashMap<Integer, Integer> mSecondPosMap = new HashMap<>();

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
        init();
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.read_theme_0_text));

        mTextSize = SpUtil.getTextSize();
        mRowSpace = SpUtil.getRowSpace();
    }

    /**
     * 初始化，绘制纯文本
     */
    public void initDrawText(String content, int position) {
        mContent = content;
        mPosition = position;
        mIsShowContent = true;
        mPageIndex = 0;
        mFirstPosMap.clear();
        mType = TYPE_TXT;
        // 进行视图重绘
        invalidate();
    }

    /**
     * 初始化，绘制 epub
     */
    public void initDrawEpub(List<EpubData> epubDataList, int pos, int secondPos) {
        mEpubDataList = epubDataList;
        mFirstPos = pos;
        mSecondPos = secondPos;
        mIsShowContent = true;
        mPageIndex = 0;
        mFirstPosMap.clear();
        mSecondPosMap.clear();
        mType = TYPE_EPUB;
        // 进行视图重绘
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!mIsShowContent) {
            return;
        }
        if (mType == TYPE_TXT && mContent.length() == 0) {
            return;
        }
        if (mType == TYPE_EPUB && mEpubDataList.isEmpty()) {
            return;
        }

        mPaint.setTextSize(mTextSize);
        if (mType == TYPE_TXT) {
            drawText(canvas);
        } else if (mType == TYPE_EPUB) {
            drawEpub(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        drawText(canvas, mTextSize + getPaddingTop());
        mFirstPosMap.put(mPageIndex, mPosition);
    }

    private void drawText(Canvas canvas, float currY) {
        int posRecord = 0;  // 记录当前页的头索引
        String content = ""; // 绘制的内容
        if (mType == TYPE_TXT) {
            posRecord = mPosition;
            content = mContent;
        } else if (mType == TYPE_EPUB){
            posRecord = mSecondPos;
            content = mEpubDataList.get(mFirstPos).getData();
        }

        float width = getWidth();
        float height = getHeight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();

        float currX = paddingStart;

        while (currY < height - paddingBottom && posRecord < content.length()) {
            // 绘制下一行
            float add; // 为了左右两端对齐每个字需要增加的距离
            int num = 0;    // 下一行的字数
            float textWidths = 0f;  // 下一行字体所占宽度
            boolean isNeed = false; // 是否需要填充
            // 计算 add 和 num
            for (int i = posRecord; i < content.length(); i++) {
                String currS = content.substring(i, i+1);
                if (currS.equals("\n")) {    // 换行
                    Log.d(TAG, "drawText: 换行");
                    num++;
                    break;
                }
                if (currS.equals("\t")) {
                    Log.d(TAG, "drawText: \\t");
                }
                if (currS.equals("\r")) {
                    Log.d(TAG, "drawText: \\r");
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
                String currS = content.substring(posRecord, posRecord+1);
                if (currS.equals("\n")) {
                    posRecord++;
                    continue;
                }
                canvas.drawText(currS, currX, currY, mPaint);
                if (isNeed) {
                    currX += getTextWidth(mPaint, currS) + add;
                } else {
                    currX += getTextWidth(mPaint, currS);
                }
                posRecord++;
            }
            currX = paddingStart;
            currY += mTextSize + mRowSpace;
        }

        // 更新相关变量
        if (mType == TYPE_TXT) {
            mNextPosition = posRecord;
        } else if (mType == TYPE_EPUB) {
            if (posRecord == content.length()) {
                mNextFirstPos = mFirstPos + 1;
                mNextSecondPos = 0;
            } else {
                mNextFirstPos = mFirstPos;
                mNextSecondPos = posRecord;
            }
        }

        // 计算当前进度
        float f = 0;
        if (mType == TYPE_TXT) {
            f = (float) mNextPosition / (float) mContent.length();
        } else if (mType == TYPE_EPUB) {
            f = (float) mNextFirstPos / (float) mEpubDataList.size();
        }
        String progress;
        if (f < 0.1f) {
            progress = String.valueOf(f * 100);
            int end = Math.min(4, progress.length());
            progress = progress.substring(0, end) + "%";
        } else {
            progress = String.valueOf(f * 100);
            int end = Math.min(5, progress.length());
            progress = progress.substring(0, end) + "%";
        }
        if (mListener != null) {
            mListener.updateProgress(progress);
        }
    }

    private void drawEpub(Canvas canvas) {
        Log.d(TAG, "drawEpub: mFirstPos = " + mFirstPos);
        float width = getWidth();
        float height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        float currY = 0f;
        switch (mEpubDataList.get(mFirstPos).getType()) {
            case TEXT:
                currY = mTextSize + paddingTop;
                break;
            case TITLE:
                currY = mTextSize + mTextSize + paddingTop;
                break;
        }

        boolean isFinished = false;
        int tempFirstPos = mFirstPos;
        int tempSecondPos = mSecondPos;
        while (!isFinished) {
            EpubData epubData = mEpubDataList.get(mFirstPos);
            switch (epubData.getType()) {
                case TEXT:
                    // 普通文本绘制
                    mPaint.setTextSize(mTextSize);  // 标题的字体更大
                    mPaint.setTextAlign(Paint.Align.LEFT);    // 文字居中
                    drawText(canvas, currY);
                    isFinished = true;
                    break;
                case TITLE:
                    // 绘制标题
                    String title = epubData.getData();
                    mPaint.setTextSize(mTextSize * 2);  // 标题的字体更大
                    mPaint.setTextAlign(Paint.Align.CENTER);    // 文字居中
                    while (currY <= height - paddingBottom && mSecondPos < title.length()) {
                        // 1. 计算能够绘制多少个字符
                        int num = 0;
                        float currWidth = 0;
                        for (int i = mSecondPos; i < title.length(); i++) {
                            String currS = title.substring(i, i+1);
                            float textWidth = getTextWidth(mPaint, currS);
                            if (currWidth + textWidth > width - paddingStart - paddingEnd) {
                                break;
                            }
                            num++;
                            currWidth += textWidth;
                        }
                        // 2. 进行绘制
                        String currS = title.substring(mSecondPos, mSecondPos + num);
                        canvas.drawText(currS, width/2, currY, mPaint);
                        // 3. 更新相关值
                        mSecondPos += num;
                        currY += mTextSize + mTextSize + mRowSpace;
                    }
                    // 判断是否绘制完标题
                    if (mSecondPos < title.length()) {  // 没有绘制完
                        mNextFirstPos = mFirstPos;
                        mNextSecondPos = mSecondPos;
                        isFinished = true;
                    } else {    // 绘制完成
                        if (currY >= height - paddingBottom
                                || mFirstPos == mEpubDataList.size() - 1
                                || mEpubDataList.get(mFirstPos+1).getType() == EpubData.TYPE.IMG) {
                            mNextFirstPos = mFirstPos + 1;
                            mNextSecondPos = 0;
                            isFinished = true;
                        }
                        // 还有位置，并且后面的是标题或文本，继续绘制
                        mFirstPos++;
                        mSecondPos= 0;
                    }
                    break;
                case IMG:
                    // 绘制图片
                    String picPath = mEpubDataList.get(mFirstPos).getData();
                    Bitmap bitmap = FileUtil.loadLocalPicture(picPath);
                    if (bitmap != null) {
                        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        float scale = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                        int w = (int)width - paddingStart - paddingEnd;
                        int h = (int) (w * scale);
                        Rect dst = new Rect(paddingStart, paddingTop,
                                (int)width - paddingEnd, paddingTop + h);
                        canvas.drawBitmap(bitmap, src, dst, null);
                    }
                    // 更新变量
                    mNextFirstPos = mFirstPos + 1;
                    mNextSecondPos = 0;
                    isFinished = true;
                    break;
            }
        }
        // 恢复原值
        mFirstPos = tempFirstPos;
        mSecondPos = tempSecondPos;
        // 更新 map
        mFirstPosMap.put(mPageIndex, mFirstPos);
        mSecondPosMap.put(mPageIndex, mSecondPos);
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
                float screenWidth = ScreenUtil.getScreenWidth();
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
        if (mType == TYPE_TXT) {
            mPosition = mNextPosition;
            if (mPosition >= mContent.length()) { // 已经到达最后
                mListener.next();   // 下一章节
                return;
            }
        } else if (mType == TYPE_EPUB) {
            mFirstPos = mNextFirstPos;
            mSecondPos = mNextSecondPos;
            if (mFirstPos == mEpubDataList.size()) {
                mListener.next();   // 下一章节
                return;
            }
        }
        mPageIndex++;
        invalidate();
    }

    /**
     * 绘制上一页
     */
    private void pre() {
        mPageIndex--;
        if (mType == TYPE_TXT) {
            if (mPosition == 0) {  // 已经是第一页
                mListener.pre();    // 上一章节
                return;
            }
            if (mFirstPosMap.containsKey(mPageIndex)) {
                mPosition = mFirstPosMap.get(mPageIndex);
            } else {
                // mPosition 更新为上一页的首字符位置
                updatePrePageFirstPos();
            }
        } else if (mType == TYPE_EPUB) {
            if (mFirstPos == 0 && mSecondPos == 0) {
                mListener.pre();    // 上一章节
                return;
            }
            if (mFirstPosMap.containsKey(mPageIndex)) {
                mFirstPos = mFirstPosMap.get(mPageIndex);
                mSecondPos = mSecondPosMap.get(mPageIndex);
            } else {
                // TODO 计算 epub 的上一章节的位置索引
                mListener.pre();    // 上一章节
            }
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
     * 设置第一个字符的位置
     */
    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getFirstPos() {
        return mFirstPos;
    }

    public int getSecondPos() {
        return mSecondPos;
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

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        // hashMap 缓存作废
        mFirstPosMap.clear();
        invalidate();
    }

    /**
     * 设置行距
     */
    public void setRowSpace(float rowSpace) {
        mRowSpace = rowSpace;
        // hashMap 缓存作废
        mFirstPosMap.clear();
        invalidate();
    }
}
