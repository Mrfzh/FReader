package com.feng.freader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class PageView extends View {

    private static final String TAG = "PageView";

    private Paint mPaint;
    private float mTextSize = 48f;      // 字体大小
    private float mRowSpace = 15f;     // 行距
    private String mContent;
    private int mPosition = 0;

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
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float width = getWidth();
        float height = getHeight();

        mPaint.setTextSize(mTextSize);
        float currY = mTextSize;
        float currX = 0f;

        while (currY < height && mPosition < mContent.length()) {
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
                if (textWidths + textWidth >= width) {  // 达到最大字数
                    isNeed = true;
                    break;
                }
                textWidths += textWidth;
                num++;
            }
            add = num <= 1? 0f : (width - textWidths) / (num - 1);
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
            currX = 0;
            currY += mTextSize + mRowSpace;
        }
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
}
