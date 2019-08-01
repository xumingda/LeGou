package com.xq.LegouShop.weiget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xq.LegouShop.R;
import com.xq.LegouShop.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouhui on 17-6-8.
 * 添加圆形子控件实现时钟环绕效果
 */

public class CircleImageLayout extends LinearLayout{

    private double mAngle = 0;//初始角度
    private int mX, mY;//子控件位置
    private int mWidth, mHeight;//控件长宽
    private int mRadius;//子控件距离控件圆心位置
    private int mCount;
    private List<CircleImageView> mCircleImageViewList;
    private CircleImageView mCircleImageView;

    public CircleImageLayout(Context context) {
        this(context, null);
    }

    public CircleImageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleImageViewList = new ArrayList<>();
    }

    /**
     * 设置子控件到控件圆心的位置
     */
    public void setRadius(int radius) {
        mRadius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initDraw();
    }

    public void initDraw() {
        mCount = getChildCount();
        LogUtils.e("mCount:"+mCount);
        for (int i = 0; i < mCount; i++) {
            View child = getChildAt(i);
            child.getWidth();
            child.getHeight();
            if (i == 0) {
                mX = mWidth / 2;
                mY = mHeight / 2;
            } else {
                mAngle = 180 - 180 / (mCount - 1) * (i - 1);
                mX = (int) (mWidth / 2 + Math.cos(Math.toRadians(mAngle)) * mRadius);
                mY = (int) (mHeight / 2 - Math.sin(Math.toRadians(mAngle)) * mRadius);
            }
            child.layout(mX - child.getWidth() / 2, mY - child.getHeight() / 2, mX + child.getWidth() / 2, mY + child.getHeight() / 2);
        }
    }

    /**
     * 初始化环绕数量半径
     */
    public void init(int count, int radius) {
        mRadius = radius;
        for (int i = 0; i < count + 1; i++) {
            CircleImageView imageView = new CircleImageView(getContext());
            if (i == 0) {
                //i为0时为圆型头像
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_header, null, true);
                mCircleImageView = (CircleImageView) view.findViewById(R.id.iv_header);
                addView(view);
            } else {
                addView(imageView, 15, 15);
                mCircleImageViewList.add(imageView);
            }
        }
    }

}
