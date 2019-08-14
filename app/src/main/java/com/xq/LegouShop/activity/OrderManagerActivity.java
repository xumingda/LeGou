package com.xq.LegouShop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;


import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.tabpager.PendingOrderPager;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//订单管理
public class OrderManagerActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 关注标题指示器
     */
    private TabSlidingIndicator titleIndicator;
    /**
     * 关注的内容viewpager
     */
    private NoScrollViewPager vpContent;
    /**
     * 关注标题
     */
    private List<String> pagerTitles;
    /**
     * 存放商场,品牌,喜欢我，我喜欢页面的集合
     */
    private List<ViewTabBasePager> concernBasePagerList;

    private View view_back;
    private int status = 0;
    private boolean isMain = false;
    private TextToSpeech textToSpeech = null;//创建自带语音对象

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_order_manager, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        status = getIntent().getIntExtra("status", 0);
        view_back = (View) findViewById(R.id.view_back);
        titleIndicator = (TabSlidingIndicator) findViewById(R.id.indicator_concern_title);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp);
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("待付款");
        pagerTitles.add("待发货");
        pagerTitles.add("待收货");
        pagerTitles.add("已完成");
        pagerTitles.add("售后");

        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        PendingOrderPager pendingOrderPager = new PendingOrderPager(this,0);
        PendingOrderPager pendingOrderPager1=new PendingOrderPager(this,1);
        PendingOrderPager pendingOrderPager2=new PendingOrderPager(this,2);

        PendingOrderPager pendingOrderPager3=new PendingOrderPager(this,3);
        PendingOrderPager pendingOrderPager4=new PendingOrderPager(this,-1);
        concernBasePagerList.add(pendingOrderPager);
        concernBasePagerList.add(pendingOrderPager1);
        concernBasePagerList.add(pendingOrderPager2);
        concernBasePagerList.add(pendingOrderPager3);
        concernBasePagerList.add(pendingOrderPager4);

        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_color_black),
                UIUtils.getColor(R.color.errorColor));
        view_back.setOnClickListener(this);
        //设置跳转再哪一页
        if (status > 0) {
            isMain = true;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        concernBasePagerList.get(position).initData();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ConcernInfoPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {

            return pagerTitles.get(position);
        }

        @Override
        public int getCount() {
            return pagerTitles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewTabBasePager concernBasePager = concernBasePagerList
                    .get(position);
            View rootView = concernBasePager.getRootView();
            container.removeView(rootView);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {


                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);

        }

        return super.onKeyDown(keyCode, event);

    }






}
