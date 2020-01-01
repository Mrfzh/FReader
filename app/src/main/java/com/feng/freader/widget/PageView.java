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
    protected static final int TYPE_TXT = 0;  // 网络小说也属于 txt
    protected static final int TYPE_EPUB = 1;

    public static final boolean IS_TEST = false;    // 是否进行单独测试

    protected Paint mPaint;
    protected float mTextSize;      // 字体大小
    protected float mRowSpace;     // 行距

    protected PageViewListener mListener;
    private boolean mIsShowContent = true;  // 是否显示文本内容
    // TYPE_TXT 为绘制普通文本（网络小说和本地 txt），TYPE_EPUB 为绘制 epub 文本（本地 epub）
    protected int mType;

    // 翻页模式，NORMAL 为普通翻页，REAL 为仿真翻页
    protected TURN_TYPE mTurnType = TURN_TYPE.NORMAL;
    public enum TURN_TYPE {
        NORMAL, REAL
    }

    /* 纯文本绘制用 */
    protected String mContent = "";    // 文本内容
    protected int mPosition = 0;  // 当前页第一个字的索引
    protected int mNextPosition;  // 下一页第一个字的索引

    /* epub 绘制用 */
    protected List<EpubData> mEpubDataList = new ArrayList<>();   // epub 内容
    protected int mFirstPos;      // 第一位置索引，指向某个 EpubData
    protected int mSecondPos;     // 第二位置索引，指向 EpubData 内部字符串
    protected int mNextFirstPos;
    protected int mNextSecondPos;

    // 当前页的索引（第几页，并不一定从 0 开始，只是作为 hashMap 的 key 而存在）
    protected int mPageIndex = 0;
    protected HashMap<Integer, Integer> mFirstPosMap = new HashMap<>();
    protected HashMap<Integer, Integer> mSecondPosMap = new HashMap<>();

    public interface PageViewListener {
        void updateProgress(String progress);     // 通知主活动更新进度
        void next();    // 显示下一章节
        void pre();     // 显示上一章节
        void nextPage();   // 下一页
        void prePage();   // 上一页
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

        if (IS_TEST) {
            invalidate();
        }
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

        if (IS_TEST) {
            invalidate();
        }
    }

    /**
     * 绘制前检查
     */
    protected boolean checkBeforeDraw() {
        if (!mIsShowContent) {
            return false;
        }
        if (mType == TYPE_TXT && mContent.length() == 0) {
            return false;
        }
        if (mType == TYPE_EPUB && mEpubDataList.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * 计算当前进度
     */
    protected void calCurrProgress() {
        float f = 0;
        if (mType == TYPE_TXT) {
            f = (float) mNextPosition / (float) mContent.length();
        } else if (mType == TYPE_EPUB) {
            f = calEpubProgress(mNextFirstPos, mNextSecondPos);
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

    protected void drawText(Canvas canvas, Paint textPaint) {
        textPaint.setTextSize(mTextSize);
        drawText(canvas, textPaint, mTextSize + getPaddingTop());
        mFirstPosMap.put(mPageIndex, mPosition);
    }

    protected void drawTextB(Canvas canvas, Paint textPaint) {
        textPaint.setTextSize(mTextSize);
        drawTextB(canvas, textPaint, mTextSize + getPaddingTop());
    }

    protected void drawText(Canvas canvas, Paint textPaint, float currY) {
        int posRecord = 0;  // 记录当前页的头索引
        String content = ""; // 绘制的内容
        if (mType == TYPE_TXT) {
            posRecord = mPosition;
            content = mContent;
        } else if (mType == TYPE_EPUB){
            posRecord = mSecondPos;
            content = mEpubDataList.get(mFirstPos).getData();
        }
        posRecord = drawTextImpl(canvas,textPaint,currY, content, posRecord);

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
    }

    protected void drawTextB(Canvas canvas, Paint textPaint, float currY) {
        int posRecord = 0;  // 记录当前页的头索引
        String content = ""; // 绘制的内容
        if (mType == TYPE_TXT) {
            posRecord = mNextPosition;
            content = mContent;
        } else if (mType == TYPE_EPUB){
            posRecord = mNextSecondPos;
            content = mEpubDataList.get(mNextFirstPos).getData();
        }
        posRecord = drawTextImpl(canvas,textPaint,currY, content, posRecord);
    }

    /**
     * 真正进行文本绘制
     *
     * @param canvas 进行绘制的画布
     * @param currY 当前 Y 坐标
     * @param content 要绘制的文本内容
     * @param firstPos 要绘制的第一个字符的位置
     * @return
     */
    private int drawTextImpl(Canvas canvas, Paint textPaint, float currY, String content, int firstPos) {
        int posRecord = firstPos;
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
                    num++;
                    break;
                }
                float textWidth = getTextWidth(textPaint, currS);
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
                canvas.drawText(currS, currX, currY, textPaint);
                if (isNeed) {
                    currX += getTextWidth(textPaint, currS) + add;
                } else {
                    currX += getTextWidth(textPaint, currS);
                }
                posRecord++;
            }
            currX = paddingStart;
            currY += mTextSize + mRowSpace;
        }

        return posRecord;
    }

    protected void drawEpub(Canvas canvas, Paint textPaint) {
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
        // 开始绘制
        boolean isFinished = false;
        int tempFirstPos = mFirstPos;
        int tempSecondPos = mSecondPos;
        while (!isFinished) {
            EpubData epubData = mEpubDataList.get(mFirstPos);
            switch (epubData.getType()) {
                case TEXT:
                    // 普通文本绘制
                    textPaint.setTextSize(mTextSize);
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    drawText(canvas, textPaint, currY);
                    isFinished = true;
                    break;
                case TITLE:
                    // 绘制标题
                    String title = epubData.getData();
                    textPaint.setTextSize(mTextSize * 2);  // 标题的字体更大
                    textPaint.setTextAlign(Paint.Align.CENTER);    // 文字居中
                    while (currY <= height - paddingBottom && mSecondPos < title.length()) {
                        // 1. 计算能够绘制多少个字符
                        int num = 0;
                        float currWidth = 0;
                        for (int i = mSecondPos; i < title.length(); i++) {
                            String currS = title.substring(i, i+1);
                            float textWidth = getTextWidth(textPaint, currS);
                            if (currWidth + textWidth > width - paddingStart - paddingEnd) {
                                break;
                            }
                            num++;
                            currWidth += textWidth;
                        }
                        // 2. 进行绘制
                        String currS = title.substring(mSecondPos, mSecondPos + num);
                        canvas.drawText(currS, width/2, currY, textPaint);
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
                    textPaint.setTextSize(mTextSize);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    String picPath = mEpubDataList.get(mFirstPos).getData();
                    Bitmap bitmap = FileUtil.loadLocalPicture(picPath);
                    if (bitmap == null) {
                        String secondPath = mEpubDataList.get(mFirstPos).getSecondData();
                        bitmap = FileUtil.loadLocalPicture(secondPath);
                    }
                    if (bitmap != null) {
                        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        float scale = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                        int w = (int)width - paddingStart - paddingEnd;
                        int h = (int) (w * scale);
                        Rect dst = new Rect(paddingStart, paddingTop,
                                (int)width - paddingEnd, paddingTop + h);
                        canvas.drawBitmap(bitmap, src, dst, null);
                    } else {
                        canvas.drawText("图片加载失败", width/2, height/2, textPaint);
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

    protected void drawEpubB(Canvas canvas, Paint textPaint) {
        float width = getWidth();
        float height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        float currY = 0f;
        switch (mEpubDataList.get(mNextFirstPos).getType()) {
            case TEXT:
                currY = mTextSize + paddingTop;
                break;
            case TITLE:
                currY = mTextSize + mTextSize + paddingTop;
                break;
        }
        // 开始绘制
        boolean isFinished = false;
        int tempFirstPos = mNextFirstPos;
        int tempSecondPos = mNextSecondPos;
        while (!isFinished) {
            EpubData epubData = mEpubDataList.get(mNextFirstPos);
            switch (epubData.getType()) {
                case TEXT:
                    // 普通文本绘制
                    textPaint.setTextSize(mTextSize);
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    drawTextB(canvas, textPaint, currY);
                    isFinished = true;
                    break;
                case TITLE:
                    // 绘制标题
                    String title = epubData.getData();
                    textPaint.setTextSize(mTextSize * 2);  // 标题的字体更大
                    textPaint.setTextAlign(Paint.Align.CENTER);    // 文字居中
                    while (currY <= height - paddingBottom && mNextSecondPos < title.length()) {
                        // 1. 计算能够绘制多少个字符
                        int num = 0;
                        float currWidth = 0;
                        for (int i = mNextSecondPos; i < title.length(); i++) {
                            String currS = title.substring(i, i+1);
                            float textWidth = getTextWidth(textPaint, currS);
                            if (currWidth + textWidth > width - paddingStart - paddingEnd) {
                                break;
                            }
                            num++;
                            currWidth += textWidth;
                        }
                        // 2. 进行绘制
                        String currS = title.substring(mNextSecondPos, mNextSecondPos + num);
                        canvas.drawText(currS, width/2, currY, textPaint);
                        // 3. 更新相关值
                        mNextSecondPos += num;
                        currY += mTextSize + mTextSize + mRowSpace;
                    }
                    // 判断是否绘制完标题
                    if (mNextSecondPos < title.length()) {  // 没有绘制完
                        isFinished = true;
                    } else {    // 绘制完成
                        if (currY >= height - paddingBottom
                                || mNextFirstPos == mEpubDataList.size() - 1
                                || mEpubDataList.get(mNextFirstPos+1).getType() == EpubData.TYPE.IMG) {
                            mNextFirstPos = mNextFirstPos + 1;
                            mNextSecondPos = 0;
                            isFinished = true;
                        }
                        // 还有位置，并且后面的是标题或文本，继续绘制
                        mNextFirstPos++;
                        mNextSecondPos= 0;
                    }
                    break;
                case IMG:
                    // 绘制图片
                    textPaint.setTextSize(mTextSize);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    String picPath = mEpubDataList.get(mNextFirstPos).getData();
                    Bitmap bitmap = FileUtil.loadLocalPicture(picPath);
                    if (bitmap == null) {
                        String secondPath = mEpubDataList.get(mNextFirstPos).getSecondData();
                        bitmap = FileUtil.loadLocalPicture(secondPath);
                    }
                    if (bitmap != null) {
                        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        float scale = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                        int w = (int)width - paddingStart - paddingEnd;
                        int h = (int) (w * scale);
                        Rect dst = new Rect(paddingStart, paddingTop,
                                (int)width - paddingEnd, paddingTop + h);
                        canvas.drawBitmap(bitmap, src, dst, null);
                    } else {
                        canvas.drawText("图片加载失败", width/2, height/2, textPaint);
                    }
                    isFinished = true;
                    break;
            }
        }
        // 恢复原值
        mNextFirstPos = tempFirstPos;
        mNextSecondPos = tempSecondPos;
    }

    /**
     * epub 根据 firstPos 和 secondPos 计算进度
     */
    private float calEpubProgress(int firstPos, int secondPos) {
        if (firstPos == mEpubDataList.size()) {
            return 1f;
        }
        // 计算数据量
        int curr = 0;
        int sum = 0;
        for (int i = 0; i < mEpubDataList.size(); i++) {
            EpubData epubData = mEpubDataList.get(i);
            if (firstPos == i) {
                curr = sum + secondPos;
            }
            switch (epubData.getType()) {
                case IMG:
                    sum += 1;
                    break;
                case TEXT:
                case TITLE:
                    sum += epubData.getData().length();
                    break;
            }
        }

        return (float) curr / (float) sum;
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
    protected boolean next() {
        if (mType == TYPE_TXT) {
            mPosition = mNextPosition;
            if (mPosition >= mContent.length()) { // 已经到达最后
                mListener.next();   // 下一章节
                return false;
            }
        } else if (mType == TYPE_EPUB) {
            mFirstPos = mNextFirstPos;
            mSecondPos = mNextSecondPos;
            if (mFirstPos == mEpubDataList.size()) {
                mListener.next();   // 下一章节
                return false;
            }
        }
        mListener.nextPage();
        mPageIndex++;

        if (IS_TEST) {
            invalidate();
        }

        return true;
    }

    /**
     * 绘制上一页
     */
    protected boolean pre() {
        mPageIndex--;
        if (mType == TYPE_TXT) {
            if (mPosition == 0) {  // 已经是第一页
                mListener.pre();    // 上一章节
                return false;
            }
            if (mFirstPosMap.containsKey(mPageIndex)) {
                mPosition = mFirstPosMap.get(mPageIndex);
            } else {
                // mPosition 更新为上一页的首字符位置
                updatePrePosTxt();
            }
        } else if (mType == TYPE_EPUB) {
            if (mFirstPos == 0 && mSecondPos == 0) {
                mListener.pre();    // 上一章节
                return false;
            }
            if (mFirstPosMap.containsKey(mPageIndex)) {
                mFirstPos = mFirstPosMap.get(mPageIndex);
                mSecondPos = mSecondPosMap.get(mPageIndex);
            } else {
                // 计算 epub 的上一章节的位置索引
                updatePrePosEpub();
            }
        }
        mListener.prePage();

        if (IS_TEST) {
            invalidate();
        }

        return true;
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
    private void updatePrePosTxt() {
        int currPos = mPosition - 1;  // 当前页的字符位置
        float width = getWidth();
        float height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();

        float currY = height - paddingBottom;
        mPaint.setTextSize(mTextSize);

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
     * 将 mFirstPos, mSecondPos 更新为上一页的值
     */
    private void updatePrePosEpub() {
        if (mSecondPos == 0) {   // 当前页是新的一页
            // 上一数据是图片
            if (mEpubDataList.get(mFirstPos-1).getType() == EpubData.TYPE.IMG) {
                mFirstPos--;
                return;
            }
            // 上一数据是标题或文本
            boolean finished = false;
            int tempFirst = mFirstPos - 1;
            int tempSecond = mEpubDataList.get(tempFirst).getData().length()-1;
            int remainHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            while (!finished) {
                EpubData epubData = mEpubDataList.get(tempFirst);
                switch (epubData.getType()) {
                    case IMG:
                        mFirstPos = tempFirst+1;
                        mSecondPos = 0;
                        finished = true;
                        break;
                    case TITLE:
                        float textSize = mTextSize * 2;
                        mPaint.setTextSize(textSize);
                        while (remainHeight >= textSize && tempSecond >= 0) {
                            float totalWidth = 0f;
                            int num = 0;
                            for (int i = tempSecond; i >= 0; i--) {
                                String currS = mEpubDataList.get(tempFirst).getData().substring(i, i+1);
                                float textWidth = getTextWidth(mPaint, currS);
                                if (textWidth + totalWidth > getWidth() - getPaddingStart() - getPaddingEnd()) {
                                    break;
                                }
                                totalWidth += textWidth;
                                num++;
                            }
                            tempSecond -= num;
                            remainHeight -= textSize + mRowSpace;
                        }
                        // 判断是否绘制完
                        if (tempSecond < 0) {  // 绘制完
                            tempFirst--;
                            if (tempFirst < 0) {
                                mFirstPos = 0;
                                mSecondPos = 0;
                                finished = true;
                                break;
                            }
                            tempSecond = mEpubDataList.get(tempFirst).getData().length()-1;
                        } else {    // 没有绘制完
                            mFirstPos = tempFirst;
                            mSecondPos = tempSecond+1;
                            finished = true;
                        }
                        break;
                    case TEXT:
                        mPaint.setTextSize(mTextSize);
                        while (remainHeight >= mTextSize && tempSecond >= 0) {
                            float totalWidth = 0f;
                            int num = 0;
                            for (int i = tempSecond; i >= 0; i--) {
                                String currS = mEpubDataList.get(tempFirst).getData().substring(i,i+1);
                                if (currS.equals("\n")) {
                                    num++;
                                    break;
                                }
                                float textWidth = getTextWidth(mPaint, currS);
                                if (textWidth + totalWidth > getWidth() - getPaddingStart() - getPaddingEnd()) {
                                    break;
                                }
                                totalWidth += textWidth;
                                num++;
                            }
                            tempSecond -= num;
                            remainHeight -= mTextSize + mRowSpace;
                        }
                        // 判断是否绘制完
                        if (tempSecond < 0) {  // 绘制完
                            tempFirst--;
                            if (tempFirst < 0) {
                                mFirstPos = 0;
                                mSecondPos = 0;
                                finished = true;
                                break;
                            }
                            tempSecond = mEpubDataList.get(tempFirst).getData().length()-1;
                        } else {    // 没有绘制完
                            mFirstPos = tempFirst;
                            mSecondPos = tempSecond+1;
                            finished = true;
                        }
                        break;
                }
            }
        } else {    // 当前页不是新的
            boolean finished = false;
            int tempFirst = mFirstPos;
            int tempSecond = mSecondPos-1;
            int remainHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            while (!finished) {
                EpubData epubData = mEpubDataList.get(tempFirst);
                switch (epubData.getType()) {
                    case IMG:
                        mFirstPos = tempFirst+1;
                        mSecondPos = 0;
                        finished = true;
                        break;
                    case TITLE:
                        float textSize = mTextSize * 2;
                        mPaint.setTextSize(textSize);
                        while (remainHeight >= textSize && tempSecond >= 0) {
                            float totalWidth = 0f;
                            int num = 0;
                            for (int i = tempSecond; i >= 0; i--) {
                                String currS = mEpubDataList.get(tempFirst).getData().substring(i, i+1);
                                float textWidth = getTextWidth(mPaint, currS);
                                if (textWidth + totalWidth > getWidth() - getPaddingStart() - getPaddingEnd()) {
                                    break;
                                }
                                totalWidth += textWidth;
                                num++;
                            }
                            tempSecond -= num;
                            remainHeight -= textSize + mRowSpace;
                        }
                        // 判断是否绘制完
                        if (tempSecond < 0) {  // 绘制完
                            tempFirst--;
                            if (tempFirst < 0) {
                                mFirstPos = 0;
                                mSecondPos = 0;
                                finished = true;
                                break;
                            }
                            tempSecond = mEpubDataList.get(tempFirst).getData().length()-1;
                        } else {    // 没有绘制完
                            mFirstPos = tempFirst;
                            mSecondPos = tempSecond+1;
                            finished = true;
                        }
                        break;
                    case TEXT:
                        mPaint.setTextSize(mTextSize);
                        while (remainHeight >= mTextSize && tempSecond >= 0) {
                            float totalWidth = 0f;
                            int num = 0;
                            for (int i = tempSecond; i >= 0; i--) {
                                String currS = mEpubDataList.get(tempFirst).getData().substring(i,i+1);
                                if (currS.equals("\n")) {
                                    num++;
                                    break;
                                }
                                float textWidth = getTextWidth(mPaint, currS);
                                if (textWidth + totalWidth > getWidth() - getPaddingStart() - getPaddingEnd()) {
                                    break;
                                }
                                totalWidth += textWidth;
                                num++;
                            }
                            tempSecond -= num;
                            remainHeight -= mTextSize + mRowSpace;
                        }
                        // 判断是否绘制完
                        if (tempSecond < 0) {  // 绘制完
                            tempFirst--;
                            if (tempFirst < 0) {
                                mFirstPos = 0;
                                mSecondPos = 0;
                                finished = true;
                                break;
                            }
                            tempSecond = mEpubDataList.get(tempFirst).getData().length()-1;
                        } else {    // 没有绘制完
                            mFirstPos = tempFirst;
                            mSecondPos = tempSecond+1;
                            finished = true;
                        }
                        break;
                }
            }
        }
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 根据进度跳转到指定位置（适合本地 txt 小说）
     */
    public void jumpWithProgress(float progress) {
        mPosition = (int) (mContent.length() * progress);
        mFirstPosMap.clear();
        invalidate();
    }

    /**
     * 设置翻页模式
     */
    public void setTurnType(TURN_TYPE mTurnType) {
        this.mTurnType = mTurnType;
    }
}
