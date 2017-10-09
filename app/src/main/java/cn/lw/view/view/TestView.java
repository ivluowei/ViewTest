package cn.lw.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Created by luow on 2017/9/18.
 * 注释：
 */

public class TestView extends ViewGroup {
    private static String TAG = "TestView";

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //获取子view个数
        int count = getChildCount();

        //记录每一行的width
        int lineWidth = 0;

        //记录每一行的height
        int lineHeight = 0;

        int mWidth = 0;
        int mHeight = 0;

        for (int i = 0; i < count; i++) {//0 1 2 3
            View child = getChildAt(i);
            //测量每一个子view宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams l = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + l.leftMargin + l.rightMargin;
            int childHeight = child.getMeasuredHeight() + l.topMargin + l.bottomMargin;

            if (childWidth + lineWidth > widthSize) {//换行,累加height
                mWidth = Math.max(lineWidth, childWidth);
                mHeight += childHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {
                mWidth = Math.max(mWidth, lineWidth);
                mHeight += lineHeight;
            }
        }

        setMeasuredDimension(
                (widthMode == MeasureSpec.EXACTLY) ? widthSize : mWidth,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : mHeight
        );
    }

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mListHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean o, int l, int t, int r, int b) {
        mAllViews.clear();
        mListHeight.clear();

        //获取所有子view个数
        int count = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> mView = new ArrayList<>();
        int width = getWidth();
        Log.d(TAG, "mCount:"+count);
        Log.d(TAG, "getMeasuredWidth:"+getMeasuredWidth());
        Log.d(TAG, "getWidth:"+getWidth());
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childwidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.bottomMargin + params.topMargin;
            Log.d(TAG, "ssssss"+(lineWidth + childwidth));

            if (lineWidth + childwidth > width) {
                mAllViews.add(mView);
                mListHeight.add(lineHeight);
                lineHeight = 0;
                lineWidth = 0;
                mView = new ArrayList<>();
            }

            lineWidth += childwidth;
            lineHeight = Math.max(lineHeight, childHeight);
            mView.add(child);
        }

        mAllViews.add(mView);
        mListHeight.add(lineHeight);

        int left = 0;
        int top = 0;

        Log.d(TAG, "mListHeight.size"+mListHeight.size());
        Log.d(TAG, "mAllViews"+mAllViews.size());
        for (int i = 0; i < mListHeight.size(); i++) {
            List<View> view = mAllViews.get(i);
            lineHeight = mListHeight.get(i);
            for (int j = 0; j < view.size(); j++) {//0 1 2 3
                View child = view.get(j);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + params.leftMargin;
                int tc = top + params.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            top += lineHeight;
            left = 0;
        }
    }

}