package cn.lw.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者: Created by luow on 2017/9/30.
 * 注释：
 */

public class MyLinearLayout extends ViewGroup {

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     *  获取布局文件中的布局参数
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     *  获取默认的布局参数
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    /**
     *  生成自己的布局参数
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int lineHeight = 0;

        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams p = (MarginLayoutParams) view.getLayoutParams();
            int childheight = view.getMeasuredHeight() + p.topMargin + p.bottomMargin;
            lineHeight += childheight;
        }
        if (n == 0) {
            setMeasuredDimension(0, 0);
        } else {
            setMeasuredDimension(widthSize,
                    heightMode == MeasureSpec.EXACTLY ? heightSize : lineHeight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mWidth = getWidth();
        int count = getChildCount();
        int lineHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams p = (MarginLayoutParams) child.getLayoutParams();
            int left = (mWidth / 2) - (child.getMeasuredWidth() / 2);
            int lc = left;
            int tc = p.topMargin + lineHeight;
            int rc = child.getMeasuredWidth() + left;
            int bc = tc + child.getMeasuredHeight();

            child.layout(lc, tc, rc, bc);
            lineHeight += child.getMeasuredHeight() + p.bottomMargin + p.topMargin;
        }
    }
}
