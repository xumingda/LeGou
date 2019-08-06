package com.xq.LegouShop.weiget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ScollViewListView extends ListView {
    public ScollViewListView(Context context) {
        super(context);
    }

    public ScollViewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScollViewListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
