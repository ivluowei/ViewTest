package cn.lw.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.lw.view.R;

/**
 * 作者: Created by luow on 2017/9/12.
 * 注释：
 */

public class CircleView extends View {

    private static final String TAG = "CircleView";

    //默认圆的大小
    private final static float CIRCLE_SIZE = 150;

    //圆圈的画笔
    private Paint mPaint;

    //文字画笔
    private Paint mTextPaint;

    //圆圈的宽度
    private int mCircleWidth;

    //圆圈的颜色
    private int mFirstColor;

    //圆弧的颜色
    private int mSecondColor;

    //圆弧的进度
    private int mProgress = 0;

    //圆的中心
    private int mCenter;

    //圆的半径
    private int mRadius;

    //圆弧最大进度
    private int mMaxProgress = 100;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CircleView_firstColor:
                    mFirstColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CircleView_secondColor:
                    mSecondColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CircleView_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        mPaint = new Paint();
        mTextPaint = new Paint();
    }

    /**
     * 计算当前view的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int mWidth = 0;
        int mHeight = 0;

        Log.d(TAG, widthSize+":"+heightSize);

        if (widthMode == MeasureSpec.AT_MOST){
            mWidth = (int) dptopx(CIRCLE_SIZE);
        }else{
            mWidth = Math.min(widthSize, heightSize);;
        }

        if (heightMode == MeasureSpec.AT_MOST){
            mHeight = (int) dptopx(CIRCLE_SIZE);
        }else{
            mHeight = Math.min(widthSize, heightSize);;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenter = getWidth() / 2;
        mRadius = mCenter - mCircleWidth;

        //画圆
        drawCircle(canvas);

        //绘制进度
        drawTextView(canvas);

        //换圆弧
        drawArc(canvas);

    }

    private void drawCircle(Canvas canvas) {

        //设置圆的宽度
        mPaint.setStrokeWidth(mCircleWidth);
        //设置画笔的风格
        mPaint.setStyle(Paint.Style.STROKE);
        // 消除锯齿
        mPaint.setAntiAlias(true);
        //设置画笔颜色
        mPaint.setColor(mFirstColor);
        //画圆
        canvas.drawCircle(mCenter, mCenter, mRadius, mPaint);
    }

    private void drawArc(Canvas canvas) {
        mPaint.setColor(mSecondColor);
        //画笔的笔触风格(ROUND，表示是圆角的笔触)
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        float sweepAngle = 360 * (getProgress() / (mMaxProgress * 1.0f));

        RectF rect = new RectF(mCenter - mRadius, mCenter - mRadius,
                mCenter + mRadius, mCenter + mRadius);
        canvas.drawArc(rect, -90, sweepAngle, false, mPaint);
    }

    private void drawTextView(Canvas canvas) {
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(sptopx(20.0f));
        // 消除锯齿
        mTextPaint.setAntiAlias(true);
        String text = (int) (100 * ((getProgress() / (1.0f * mMaxProgress))))
                + "%";
        Rect mTextRect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        canvas.drawText(text, mCenter - (mTextRect.width() / 2),
                mCenter + (mTextRect.height() / 2), mTextPaint);
    }

    /**
     * 注意每一次set要调用invalidate 方法 重走ondraw方法
     *
     * */
    public void setProgress(int progress) {
        this.mProgress = progress;
        if (mProgress > mMaxProgress || mProgress < 0) {
            return;
        }
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     *  dp转px相关
     */
    private float dptopx(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return scale * dp + 0.5f;
    }

    private float sptopx(float sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp;
    }

}