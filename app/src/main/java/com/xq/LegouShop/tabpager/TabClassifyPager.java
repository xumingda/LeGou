package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.HomeAdapter;
import com.xq.LegouShop.adapter.MenuAdapter;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.protocol.GetAdListProtocol;
import com.xq.LegouShop.protocol.GetCategoryListProtocol;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.protocol.GetUserInfoProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.response.GetUserInfoResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.MyLinearLayout;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * 购物车
 * @描述: TODO
 */
public class TabClassifyPager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private int parentId;
    private Dialog loadingDialog;
    private List<GetCategoryListResponse.DataList> categoryList;
    private String url;
    private Gson gson;

    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean.DataBean> homeList = new ArrayList<>();
    private List<Integer> showTitle;

    private ListView lv_menu;
    private ListView lv_home;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private List<View> dots; // 图片标题正文的那些点
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private List<ImageView> imageViews; // 滑动的图片集合
    private ImageView imageView;
    private ViewPager vp;
    private LinearLayout ll_pager;
    private View v_dot0;
    private View v_dot1;
    private View v_dot2;
    private View v_dot3;
    private View v_dot4;
    private List<String> adsBeanList;
    private ImageLoader imageLoader;
    private boolean isopen;

    private TextView tv_title;
    private boolean isLoad;
    private GetCategoryListResponse getCategoryListResponse;
    private int selectitem=0;
    /**
     * @param context
     */
    public TabClassifyPager(Context context, FrameLayout mDragLayout,
                            MyLinearLayout mLinearLayout ) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;
    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.classify_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        return view;
    }

    public void initData() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        lv_menu = (ListView) view.findViewById(R.id.lv_menu);
        tv_title = (TextView) view.findViewById(R.id.tv_titile);
        lv_home = (ListView) view.findViewById(R.id.lv_home);

        //轮播
        vp = (ViewPager) view.findViewById(R.id.vp);
        v_dot0 = (View) view.findViewById(R.id.v_dot0);
        v_dot1 = (View) view.findViewById(R.id.v_dot1);
        v_dot2 = (View) view.findViewById(R.id.v_dot2);
        v_dot3 = (View) view.findViewById(R.id.v_dot3);
        v_dot4 = (View) view.findViewById(R.id.v_dot4);
        ll_pager = (LinearLayout) view.findViewById(R.id.ll_pager);
        adsBeanList=new ArrayList<>();

        menuAdapter = new MenuAdapter(mContext, menuList);
        lv_menu.setAdapter(menuAdapter);

        homeAdapter = new HomeAdapter(mContext, homeList);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectitem=position;
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                tv_title.setText(menuList.get(position));
                parentId=Integer.parseInt(categoryList.get(position).id);
                getCategoryList();
//                lv_home.setSelection(showTitle.get(position));
            }
        });


//        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
//            private int scrollState;
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                this.scrollState = scrollState;
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    return;
//                }
//                int current = showTitle.indexOf(firstVisibleItem);
////				lv_home.setSelection(current);
//                if (currentItem != current && current >= 0) {
//                    currentItem = current;
//                    tv_title.setText(menuList.get(currentItem));
//                    menuAdapter.setSelectItem(currentItem);
//                    menuAdapter.notifyDataSetInvalidated();
//                }
//            }
//        });
        if(!isLoad) {
            categoryList=new ArrayList<>();
            parentId=0;
            getCategoryList();
            getHome();
        }else{
            //加载过默认选中第一项
            menuAdapter.setSelectItem(selectitem);
        }

    }


    public void getHome() {
        GetAdListProtocol getHomeProtocol=new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(3));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    if (getHomeResponse.dataList.size() > 0) {
                        setImageViews(getHomeResponse.dataList);
                        ll_pager.setVisibility(View.VISIBLE);
                    }else{
                        ll_pager.setVisibility(View.INVISIBLE);
                    }

                } else {
                    if(getHomeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void setImageViews(List<GetAdListResponse.DataList> dataListList){
        for(int i=0;i<dataListList.size();i++){
            adsBeanList.add(dataListList.get(i).pic);
        }

        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            if (i < 5) {
                imageView = new ImageView(mContext);
//                ViewListener viewListener = new ViewListener(i);
//                imageView.setOnClickListener(viewListener);
                imageLoader.displayImage("http://qiniu.lelegou.pro/"+adsBeanList.get(i), imageView, PictureOption.getSimpleOptions());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViews.add(imageView);
            }

        }


        dots = new ArrayList<View>();
        if (adsBeanList.size() > 5) {
            dots.add(v_dot0);
            dots.add(v_dot1);
            dots.add(v_dot2);
            dots.add(v_dot3);
            dots.add(v_dot4);
        } else {
            switch (adsBeanList.size()) {
                case 1:
                    v_dot0.setVisibility(View.GONE);
                    v_dot1.setVisibility(View.GONE);
                    v_dot2.setVisibility(View.GONE);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 2:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    v_dot2.setVisibility(View.GONE);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 3:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 4:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    dots.add(v_dot3);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 5:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    dots.add(v_dot3);
                    dots.add(v_dot4);
                    break;
                default:
                    break;
            }
        }


        vp.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        vp.setOnPageChangeListener(new MyPageChangeListener());
        //设置初始选择的页面，要在setadapter之后
        vp.setCurrentItem(currentItem);
        if (!isopen) {
            startScrollTask();
            isopen = true;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_tongji: {
//                Intent intent = new Intent(mContext, StatisticsMerchantActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_menu:{
//                Intent intent=new Intent(mContext, MenuManagementActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_report:{
//                Intent intent=new Intent(mContext, InformationReportActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_message:{
//                Intent intent=new Intent(mContext, MessageActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_set:{
//                Intent intent=new Intent(mContext,MerchantSettingActivity   .class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_marketing:{
//                Intent intent=new Intent(mContext,MarketingActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_order_manager:{
//                Intent intent=new Intent(mContext,OrderManagerActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_member_manager:{
//                if(com.ciba.wholefinancial.util.SharedPrefrenceUtils.getInt(mContext,"master",0)==1){
//                    Intent intent=new Intent(mContext, MemberManagerActivity.class);
//                    com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                }else{
//                    com.ciba.wholefinancial.util.DialogUtils.showAlertDialog(mContext, "您没有权限查看！");
//                }
//
//                break;
//            }
        }
    }

    public void getCategoryList() {
        loadingDialog.show();
        GetCategoryListProtocol getCategoryListProtocol = new GetCategoryListProtocol();
        String url = getCategoryListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        if(parentId>0){
            map.put("parentId",String.valueOf(parentId));
        }
        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getCategoryListResponse = gson.fromJson(json, GetCategoryListResponse.class);
                LogUtils.e("getCategoryListResponse:" + getCategoryListResponse.toString());
                if (getCategoryListResponse.code.equals("0")) {
                    if(parentId>0){
                        CategoryBean.DataBean dataBean=new CategoryBean.DataBean();
                        dataBean.setDataList(getCategoryListResponse.dataList);
                        homeList.clear();
                        homeList.add(dataBean);
                        homeAdapter.notifyDataSetChanged();
                    }else{
                        isLoad=true;
                        categoryList.clear();
                        categoryList.addAll(getCategoryListResponse.dataList);
                        for(int i=0;i<getCategoryListResponse.dataList.size();i++){
                            menuList.add(getCategoryListResponse.dataList.get(i).categoryName);
                        }
                        menuAdapter.notifyDataSetChanged();
                        //默认选择第一个
                        if(menuList.size()>0){
                            menuAdapter.setSelectItem(0);
                            menuAdapter.notifyDataSetInvalidated();
                            if(categoryList.size()>0) {
                                parentId = Integer.parseInt(categoryList.get(0).id);
                                getCategoryList();
                            }
                        }
                    }

                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getCategoryListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }
    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (adsBeanList.size() > 5) {
                return 5;
            } else {
                return adsBeanList.size();
            }
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
//            currentItem = position;
//            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
//            oldPosition = position;
            LogUtils.e("dots.size()" + dots.size());
            if (dots.size() > 0) {
                currentItem = position % imageViews.size();
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                dots.get(position % imageViews.size()).setBackgroundResource(R.drawable.dot_focused);
                oldPosition = position % imageViews.size();
            }

        }

        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (vp) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    public void startScrollTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);
    }
}
