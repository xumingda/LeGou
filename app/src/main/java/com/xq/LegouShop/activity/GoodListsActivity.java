package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.protocol.CollectionGoodsProtocol;
import com.xq.LegouShop.protocol.GetGoodInfoProtocol;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodInfoResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.tabpager.DetailsPager;
import com.xq.LegouShop.tabpager.EvaluatePager;
import com.xq.LegouShop.tabpager.GoodsListPager;
import com.xq.LegouShop.tabpager.PendingOrderPager;
import com.xq.LegouShop.tabpager.SpecificationPager;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.SelectGoodsSpecificationuwindow;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//商品列表
public class GoodListsActivity extends BaseActivity  implements ViewPager.OnPageChangeListener , View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private GetGoodInfoResponse getGoodInfoResponse ;
    private View view_back;
    private ImageLoader imageLoader;

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
    private RelativeLayout rl_main;
    private String categoryId;
    private      ArrayList<GetCategoryListResponse.DataList> dataListArrayList;
    private int item;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_good_list, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        dataListArrayList =  (ArrayList<GetCategoryListResponse.DataList>) getIntent().getSerializableExtra("dataList");

        categoryId=getIntent().getStringExtra("categoryId");
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(GoodListsActivity.this, false);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        view_back=(View)findViewById(R.id.view_back);



        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });




        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp);
        pagerTitles = new ArrayList<String>();
        concernBasePagerList = new ArrayList<ViewTabBasePager>();

        for(int i=0;i<dataListArrayList.size();i++){
            if(categoryId.equals(dataListArrayList.get(i).id)){
                item=i;
            }
            GoodsListPager goodsListPager = new GoodsListPager(this,dataListArrayList.get(i).id);
            LogUtils.e("GetRegionListResponse列表id:"+dataListArrayList.get(i).id+"   categoryId:"+categoryId);
            pagerTitles.add(dataListArrayList.get(i).categoryName);
            concernBasePagerList.add(goodsListPager);
        }


        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_color_black),
                UIUtils.getColor(R.color.errorColor));

//        vpContent.setCurrentItem(item);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        GoodsListPager goodsListPager=(GoodsListPager) concernBasePagerList.get(position);
        goodsListPager.categoryId=dataListArrayList.get(position).id;
        concernBasePagerList.get(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
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







}
