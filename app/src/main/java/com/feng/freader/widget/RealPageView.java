package com.feng.freader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.feng.freader.R;
import com.feng.freader.entity.data.CatalogData;
import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.util.ScreenUtil;

import java.util.List;


/**
 * @author Feng Zhaohao
 * Created on 2019/12/29
 */
public class RealPageView extends PageView{
    private static final String TAG = "RealPageView";

    public static final String STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT";
    public static final String STYLE_BOTTOM_RIGHT = "STYLE_BOTTOM_RIGHT";
    public static final String STYLE_CENTER_RIGHT = "STYLE_CENTER_RIGHT";
    private String mCurrStyle = "";     // 记录当前 Style

    private Paint pathAPaint;   // 绘制 A 区域画笔
    private Paint pathBPaint;   // 绘制 B 区域画笔
    private Paint pathCPaint;   // 绘制 C 区域画笔
    private Paint pathCContentPaint;   // 绘制C区域内容的画笔
    private Paint mBgPaint = new Paint();     // 绘制背景颜色
    private Path pathA;
    private Path pathB;
    private Path pathC;

    private MyPoint a,f,g,e,h,c,j,b,k,d,i;
    float lPathAShadowDis = 0;      // A 区域左阴影矩形短边长度参考值
    float rPathAShadowDis = 0;      // A 区域右阴影矩形短边长度参考值

//    private int defaultWidth = 1000;   //默认宽度
//    private int defaultHeight = 1800;  //默认高度
    private int viewWidth;
    private int viewHeight;

    private Scroller mScroller;     // 用于取消翻页动画

    // 重用部分
    private float[] mMatrixArray = new float[9];
    private Matrix mMatrix;

    private GradientDrawable drawableLeftTopRight;
    private GradientDrawable drawableLeftLowerRight;

    private GradientDrawable drawableRightTopRight;
    private GradientDrawable drawableRightLowerRight;
    private GradientDrawable drawableHorizontalLowerRight;

    private GradientDrawable drawableBTopRight;
    private GradientDrawable drawableBLowerRight;

    private GradientDrawable drawableCTopRight;
    private GradientDrawable drawableCLowerRight;

    private Bitmap mContentABitmap;  //A区域内容Bitmap
    private Bitmap mContentBBitmap;  //B区域内容Bitmap
    private Bitmap mContentCBitmap;  //C区域内容Bitmap


    public RealPageView(Context context) {
        super(context);
        init(context);
    }

    public RealPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RealPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        a = new MyPoint();
        f = new MyPoint();
        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();

        pathAPaint = new Paint();
        pathAPaint.setColor(Color.GREEN);
        pathAPaint.setAntiAlias(true);  //设置抗锯齿

        pathCPaint = new Paint();
        pathCPaint.setColor(Color.YELLOW);
        pathCPaint.setAntiAlias(true);  //设置抗锯齿

        pathBPaint = new Paint();
        pathBPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        pathBPaint.setAntiAlias(true);  //设置抗锯齿

        pathCContentPaint = new Paint();
        pathCContentPaint.setColor(Color.YELLOW);
        pathCContentPaint.setAntiAlias(true);//设置抗锯齿

        mScroller = new Scroller(context, new LinearInterpolator());

        mMatrix = new Matrix();
        createGradientDrawable();
    }

    @Override
    public void initDrawText(String content, int position) {
        super.initDrawText(content, position);
        updateBitmap();
    }

    @Override
    public void initDrawEpub(List<EpubData> epubDataList, int pos, int secondPos) {
        super.initDrawEpub(epubDataList, pos, secondPos);
        updateBitmap();
    }

    /**
     * 初始化各区域阴影 GradientDrawable
     */
    private void createGradientDrawable(){
        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableLeftTopRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawableLeftTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableLeftLowerRight = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        drawableLeftLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x22333333;
        lightColor = 0x01333333;
        gradientColors =  new int[]{deepColor,lightColor,lightColor};
        drawableRightTopRight = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
        drawableRightTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableRightLowerRight = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
        drawableRightLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x44333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableHorizontalLowerRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);;
        drawableHorizontalLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55111111;
        lightColor = 0x00111111;
        gradientColors = new int[] {deepColor,lightColor};//渐变颜色数组
        drawableBTopRight =new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,gradientColors);
        drawableBTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变
        drawableBLowerRight =new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,gradientColors);
        drawableBLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x00333333;
        gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableCTopRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawableCTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableCLowerRight = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        drawableCLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    /**
     * 绘制 A 区域的内容（本页内容）
     */
    private void drawContentABitmap(Bitmap bitmap, Paint pathPaint){
        Canvas mCanvas = new Canvas(bitmap);
        mCanvas.drawPath(getPathDefault(), mBgPaint);
        if (mType == TYPE_TXT) {
            drawText(mCanvas);
        } else if (mType == TYPE_EPUB) {
            drawEpub(mCanvas);
        }
    }

    /**
     * 绘制 B 区域的内容（下一页内容）
     */
    private void drawPathBContentBitmap(Bitmap bitmap, Paint pathPaint){
        Canvas mCanvas = new Canvas(bitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int height = measureSize(defaultHeight, heightMeasureSpec);
//        int width = measureSize(defaultWidth, widthMeasureSpec);
//        setMeasuredDimension(width, height);


        a.x = -1;
        a.y = -1;

//        // 先绘制内容
//        mContentABitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
//        drawPathAContentBitmap(mContentABitmap,pathAPaint);
//
//        mContentBBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
//        drawPathBContentBitmap(mContentBBitmap,pathBPaint);
//
//        mContentCBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
//        drawPathAContentBitmap(mContentCBitmap,pathCPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        viewWidth = getWidth();
        viewHeight = getHeight();
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!checkBeforeDraw()) {
            return;
        }

        if (mTurnType == TURN_TYPE.NORMAL) {
            if (mContentABitmap != null) {
                canvas.drawBitmap(mContentABitmap, 0, 0, null);
            }
        } else if (mTurnType == TURN_TYPE.REAL) {
            if(a.x==-1 && a.y==-1){
                drawPathAContent(canvas,getPathDefault());
            }else {
                if(f.x==viewWidth && f.y==0){
                    drawPathAContent(canvas,getPathATopRight());
                    drawPathCContent(canvas,getPathATopRight());
                    drawPathBContent(canvas,getPathATopRight());
                }else if(f.x==viewWidth && f.y==viewHeight){
                    drawPathAContent(canvas,getPathABottomRight());
                    drawPathCContent(canvas,getPathABottomRight());
                    drawPathBContent(canvas,getPathABottomRight());
                }
            }
        }

        // 计算当前进度
        calCurrProgress();
    }
    
    /**
     * 计算各点坐标
     * @param a
     * @param f
     */
    private void calcPointsXY(MyPoint a, MyPoint f){
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a,e,c,j);
        k = getIntersectionPoint(a,h,c,j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;

        //计算d点到直线ae的距离
        float lA = a.y-e.y;
        float lB = e.x-a.x;
        float lC = a.x*e.y-e.x*a.y;
        lPathAShadowDis = Math.abs((lA*d.x+lB*d.y+lC)/(float) Math.hypot(lA,lB));
        //计算i点到ah的距离
        float rA = a.y-h.y;
        float rB = h.x-a.x;
        float rC = a.x*h.y-h.x*a.y;
        rPathAShadowDis = Math.abs((rA*i.x+rB*i.y+rC)/(float) Math.hypot(rA,rB));
    }

    /**
     * 计算两线段相交点坐标
     */
    private MyPoint getIntersectionPoint(MyPoint lineOne_My_pointOne, MyPoint lineOne_My_pointTwo,
                                         MyPoint lineTwo_My_pointOne, MyPoint lineTwo_My_pointTwo){
        float x1,y1,x2,y2,x3,y3,x4,y4;
        x1 = lineOne_My_pointOne.x;
        y1 = lineOne_My_pointOne.y;
        x2 = lineOne_My_pointTwo.x;
        y2 = lineOne_My_pointTwo.y;
        x3 = lineTwo_My_pointOne.x;
        y3 = lineTwo_My_pointOne.y;
        x4 = lineTwo_My_pointTwo.x;
        y4 = lineTwo_My_pointTwo.y;

        float pointX =((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY =((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return  new MyPoint(pointX,pointY);
    }

    /**
     * 绘制区域 A（从右下角翻页）
     */
    private Path getPathABottomRight(){
        if (pathA == null) {
            pathA = new Path();
        } else {
            pathA.reset();
        }
        Path pathA = new Path();
        pathA.lineTo(0, viewHeight);    //移动到左下角
        pathA.lineTo(c.x,c.y);              //移动到c点
        pathA.quadTo(e.x,e.y,b.x,b.y);      //从c到b画二阶贝塞尔曲线，控制点为e
        pathA.lineTo(a.x,a.y);              //移动到a点
        pathA.lineTo(k.x,k.y);              //移动到k点
        pathA.quadTo(h.x,h.y,j.x,j.y);      //从k到j画二阶贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth,0);      //移动到右上角
        pathA.close();  //闭合区域
        return pathA;
    }

    /**
     * 绘制区域 A（从右上角翻页）
     */
    private Path getPathATopRight(){
        if (pathA == null) {
            pathA = new Path();
        } else {
            pathA.reset();
        }
        pathA.lineTo(c.x,c.y);              //移动到c点
        pathA.quadTo(e.x,e.y,b.x,b.y);      //从c到b画二阶贝塞尔曲线，控制点为e
        pathA.lineTo(a.x,a.y);              //移动到a点
        pathA.lineTo(k.x,k.y);              //移动到k点
        pathA.quadTo(h.x,h.y,j.x,j.y);      //从k到j画二阶贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth,viewHeight); //移动到右下角
        pathA.lineTo(0, viewHeight);    //移动到左下角
        pathA.close();  //闭合区域
        return pathA;
    }

    /**
     * 绘制区域 C
     */
    private Path getPathC(){
        if (pathC == null) {
            pathC = new Path();
        } else {
            pathC.reset();
        }
        pathC.moveTo(i.x,i.y);//移动到i点
        pathC.lineTo(d.x,d.y);//移动到d点
        pathC.lineTo(b.x,b.y);//移动到b点
        pathC.lineTo(a.x,a.y);//移动到a点
        pathC.lineTo(k.x,k.y);//移动到k点
        pathC.close();//闭合区域
        return pathC;
    }

    /**
     * 绘制区域B
     */
    private Path getPathB(){
        if (pathB == null) {
            pathB = new Path();
        } else {
            pathB.reset();
        }
        pathB.lineTo(0, viewHeight);//移动到左下角
        pathB.lineTo(viewWidth,viewHeight);//移动到右下角
        pathB.lineTo(viewWidth,0);//移动到右上角
        pathB.close();//闭合区域
        return pathB;
    }

    /**
     * 绘制默认的界面
     */
    private Path getPathDefault(){
        if (pathA == null) {
            pathA = new Path();
        } else {
            pathA.reset();
        }
        pathA.lineTo(0, viewHeight);
        pathA.lineTo(viewWidth,viewHeight);
        pathA.lineTo(viewWidth,0);
        pathA.close();
        return pathA;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTurnType == TURN_TYPE.NORMAL) {
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
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX() > viewWidth / 3 && event.getY() < viewHeight / 3){  //从上半部分翻页
                    mCurrStyle = STYLE_TOP_RIGHT;
                    setTouchPoint(event.getX(),event.getY());
                } else if(event.getX() > viewWidth / 3 && event.getY() >= viewHeight - viewHeight / 3) {  //从下半部分翻页
                    mCurrStyle = STYLE_BOTTOM_RIGHT;
                    setTouchPoint(event.getX(),event.getY());
                } else if(event.getX() > viewWidth / 3) {  //从中间翻页
                    mCurrStyle = STYLE_CENTER_RIGHT;
                    setTouchPoint(event.getX(),event.getY());
                } else {
                    mCurrStyle = "";
                }
                break;
            case MotionEvent.ACTION_MOVE:
                setTouchPoint(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                startCancelAnim();
                break;
        }
        return true;
    }

    @Override
    protected boolean next() {
        if (!super.next()) {
            return false;
        }
        updateBitmap();

        return true;
    }

    @Override
    protected boolean pre() {
        if (!super.pre()) {
            return false;
        }
        updateBitmap();

        return true;
    }

    /**
     * 设置触摸点并更新视图
     */
    public void setTouchPoint(float x, float y){
        a.x = x;
        a.y = y;
        switch (mCurrStyle){
            case STYLE_TOP_RIGHT:
                f.x = viewWidth;
                f.y = 0;
                calcPointsXY(a,f);
                // 如果c点x坐标小于0则重新测量a点坐标
                if(calcPointCX(new MyPoint(x,y),f) < 0){
                    calcPointAByTouchPoint();
                    calcPointsXY(a,f);
                }
                postInvalidate();
                break;
            case STYLE_BOTTOM_RIGHT:
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a,f);
                // 如果c点x坐标小于0则重新测量a点坐标
                if(calcPointCX(new MyPoint(x,y),f) < 0){
                    calcPointAByTouchPoint();
                    calcPointsXY(a,f);
                }
                postInvalidate();
                break;
            case STYLE_CENTER_RIGHT:
                a.y = viewHeight - 1;
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a,f);
                postInvalidate();
                break;
            default:
                break;
        }

    }

    /**
     * 回到默认状态
     */
    public void setDefaultPath(){
        a.x = -1;
        a.y = -1;
        postInvalidate();
    }

    /**
     * 计算 C 点的 X 值
     */
    private float calcPointCX(MyPoint a, MyPoint f){
        MyPoint g,e;
        g = new MyPoint();
        e = new MyPoint();
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        return e.x - (f.x - e.x) / 2;
    }

    /**
     * 如果c点x坐标小于0,根据触摸点重新测量a点坐标
     */
    private void calcPointAByTouchPoint(){
        float w0 = viewWidth - c.x;

        float w1 = Math.abs(f.x - a.x);
        float w2 = viewWidth * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

    public class MyPoint {
        float x,y;
        MyPoint(){}
        MyPoint(float x, float y){
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y){
                setDefaultPath();//重置默认界面
            }
        }
    }

    /**
     * 取消翻页动画,计算滑动位置与时间
     */
    public void startCancelAnim(){
        int dx,dy;
        //让a滑动到f点所在位置，留出1像素是为了防止当a和f重叠时出现View闪烁的情况
        if(mCurrStyle.equals(STYLE_TOP_RIGHT)){
            dx = (int) (viewWidth-1-a.x);
            dy = (int) (1-a.y);
        }else {
            dx = (int) (viewWidth-1-a.x);
            dy = (int) (viewHeight-1-a.y);
        }
        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 400);
    }

    /**
     * 绘制A区域内容
     */
    private void drawPathAContent(Canvas canvas, Path pathA){
        canvas.save();
        canvas.clipPath(pathA, Region.Op.INTERSECT);//对绘制内容进行裁剪，取和A区域的交集
        canvas.drawBitmap(mContentABitmap, 0, 0, null);

        if(mCurrStyle.equals(STYLE_CENTER_RIGHT)){
            drawPathAHorizontalShadow(canvas,pathA);
        }else {
            drawPathALeftShadow(canvas,pathA);
            drawPathARightShadow(canvas,pathA);
        }
        canvas.restore();
    }

    /**
     * 绘制A区域左阴影
     * @param canvas
     */
    private void drawPathALeftShadow(Canvas canvas, Path pathA){
        int left;
        int right;
        int top = (int) e.y;
        int bottom = (int) (e.y+viewHeight);

        GradientDrawable gradientDrawable;
        if (mCurrStyle.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = drawableLeftTopRight;
            left = (int) (e.x - lPathAShadowDis /2);
            right = (int) (e.x);
        } else {
            gradientDrawable = drawableLeftLowerRight;
            left = (int) (e.x);
            right = (int) (e.x + lPathAShadowDis /2);
        }

        // 裁剪出我们需要的区域
        canvas.restore();
        canvas.save();
        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
        mPath.lineTo(d.x,d.y);
        mPath.lineTo(e.x,e.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x-a.x, a.y-e.y));
        canvas.rotate(mDegrees, e.x, e.y);
        gradientDrawable.setBounds(left,top,right,bottom);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制A区域右阴影
     * @param canvas
     */
    private void drawPathARightShadow(Canvas canvas, Path pathA){

        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//view对角线长度
        int left = (int) h.x;
        int right = (int) (h.x + viewDiagonalLength*10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (mCurrStyle.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = drawableRightTopRight;
            top = (int) (h.y- rPathAShadowDis /2);
            bottom = (int) h.y;
        } else {
            gradientDrawable = drawableRightLowerRight;
            top = (int) h.y;
            bottom = (int) (h.y+ rPathAShadowDis /2);
        }
        gradientDrawable.setBounds(left,top,right,bottom);

        //裁剪出我们需要的区域
        canvas.restore();
        canvas.save();
        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
        mPath.lineTo(h.x,h.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(a.y-h.y, a.x-h.x));
        canvas.rotate(mDegrees, h.x, h.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制A区域水平翻页阴影
     */
    private void drawPathAHorizontalShadow(Canvas canvas, Path pathA){
        int maxShadowWidth = 30;//阴影矩形最大的宽度
        int left = (int) (a.x - Math.min(maxShadowWidth,(rPathAShadowDis/2)));
        int right = (int) (a.x);
        int top = 0;
        int bottom = viewHeight;
        GradientDrawable gradientDrawable = drawableHorizontalLowerRight;
        gradientDrawable.setBounds(left,top,right,bottom);

        canvas.restore();
        canvas.save();
        canvas.clipPath(pathA, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(f.x-a.x,f.y-h.y));
        canvas.rotate(mDegrees, a.x, a.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制B区域内容
     */
    private void drawPathBContent(Canvas canvas, Path pathA){
        canvas.save();
        canvas.clipPath(pathA);//裁剪出A区域
        canvas.clipPath(getPathC(), Region.Op.UNION);//裁剪出A和C区域的全集
        canvas.clipPath(getPathB(), Region.Op.REVERSE_DIFFERENCE);//裁剪出B区域中不同于与AC区域的部分
        canvas.drawBitmap(mContentBBitmap, 0, 0, null);

        drawPathBShadow(canvas);
        canvas.restore();
    }

    /**
     * 绘制B区域阴影
     */
    private void drawPathBShadow(Canvas canvas){

        int deepOffset = 0;//深色端的偏移值
        float aTof =(float) Math.hypot((a.x - f.x),(a.y - f.y));    //a到f的距离
        int lightOffset = (int) (aTof/4);//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//对角线长度

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if(mCurrStyle.equals(STYLE_TOP_RIGHT)){//f点在右上角
            //从左向右线性渐变
            gradientDrawable = drawableBTopRight;
            left = (int) (c.x - deepOffset);//c点位于左上角
            right = (int) (c.x + lightOffset);
        } else {
            //从右向左线性渐变
            gradientDrawable = drawableBLowerRight;
            left = (int) (c.x  - lightOffset);//c点位于左下角
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left,top,right,bottom);//设置阴影矩形

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x- f.x, h.y - f.y));//旋转角度
        canvas.rotate(rotateDegrees, c.x, c.y);//以c为中心点旋转
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制C区域内容
     */
    private void drawPathCContent(Canvas canvas, Path pathA){
        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(getPathC(), Region.Op.REVERSE_DIFFERENCE);  //裁剪出C区域不同于A区域的部分
        canvas.drawPath(getPathC(),pathCPaint); //绘制背景色


        // 设置 Matrix 矩阵
        // 先关于 y 轴对称，然后进行旋转
        float eh = (float) Math.hypot(f.x - e.x,h.y - f.y);
        float sin0 = (f.x - e.x) / eh;
        float cos0 = (h.y - f.y) / eh;
        mMatrixArray[0] = -(1 - 2*sin0*sin0);
        mMatrixArray[1] = 2 * sin0 * cos0;
        mMatrixArray[3] = 2 * sin0 * cos0;
        mMatrixArray[4] = 1 - 2 * sin0 * sin0;
        mMatrixArray[8] = 1f;
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);
        // 平移到正确位置
        mMatrix.preTranslate(-e.x, -e.y);   //沿当前XY轴负方向位移得到 矩形A₃B₃C₃D₃
        mMatrix.postTranslate(e.x, e.y);    //沿原XY轴方向位移得到 矩形A4 B4 C4 D4

        canvas.drawBitmap(mContentCBitmap, mMatrix, null);

        // 绘制 C 区域阴影
        drawPathCShadow(canvas);

        canvas.restore();
    }

    /**
     * 绘制C区域阴影，阴影左浅右深
     */
    private void drawPathCShadow(Canvas canvas){
        int deepOffset = 0;//深色端的偏移值
        float aTof =(float) Math.hypot((a.x - f.x),(a.y - f.y));    //a到f的距离
        int lightOffset = (int) (aTof/4);//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//对角线长度

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if(mCurrStyle.equals(STYLE_TOP_RIGHT)){//f点在右上角
            //从左向右线性渐变
            gradientDrawable = drawableCTopRight;
            left = (int) (c.x - deepOffset);//c点位于左上角
            right = (int) (c.x + lightOffset);
        } else {
            //从右向左线性渐变
            gradientDrawable = drawableCLowerRight;
            left = (int) (c.x  - lightOffset);//c点位于左下角
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left,top,right,bottom);//设置阴影矩形

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x- f.x, h.y - f.y));//旋转角度
        canvas.rotate(rotateDegrees, c.x, c.y);//以c为中心点旋转
        gradientDrawable.draw(canvas);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        // hashMap 缓存作废
        mFirstPosMap.clear();
        updateBitmap();
    }

    /**
     * 设置行距
     */
    public void setRowSpace(float rowSpace) {
        mRowSpace = rowSpace;
        // hashMap 缓存作废
        mFirstPosMap.clear();
        updateBitmap();
    }

    /**
     * 设置背景颜色
     */
    public void setBgColor(int color) {
        mBgPaint.setColor(color);
    }

    /**
     * 重新绘制 Bitmap
     */
    public void updateBitmap() {
        if (mContentABitmap == null) {
            mContentABitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        }
        drawContentABitmap(mContentABitmap, null);
        invalidate();
    }
}

