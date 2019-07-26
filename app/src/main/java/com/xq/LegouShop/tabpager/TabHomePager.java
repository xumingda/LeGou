package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodsInfoActivity;
import com.xq.LegouShop.activity.SearchGoodsActivity;
import com.xq.LegouShop.adapter.GoodsAdapter;
import com.xq.LegouShop.adapter.HomeAdapter;
import com.xq.LegouShop.adapter.TabHomeAdapter;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.protocol.GetAdListProtocol;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.xq.LegouShop.weiget.MyScrollview;

import java.text.DecimalFormat;
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
 * @描述: TODO
 */
public class TabHomePager extends TabBasePager implements View.OnClickListener ,PullToRefreshBase.OnRefreshListener{

//    private GridView gv_jingxuan,gv_pinzhi,gv_tuijian;
    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private PullToRefreshListView lv_home;
    private int pageNo=1;
    private boolean isopen;
    private TabHomeAdapter tabHomeAdapter;
    private List<GoodsBean> recommendGoodsBeanList;
    private LinearLayout rl_search;
    private boolean isRefresh = false;
    /**
     * @param context
     */
    public TabHomePager(Context context, FrameLayout mDragLayout,
                        MyLinearLayout mLinearLayout ) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;
    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.home_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        return view;
    }

    public void initData() {


        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        rl_search=(LinearLayout) view.findViewById(R.id.rl_search);
        lv_home=(PullToRefreshListView)view.findViewById(R.id.lv_home);
//        tv_shop_name=(TextView)view.findViewById(R.id.tv_shop_name);
//        tv_todayTurnover=(TextView)view.findViewById(R.id.tv_todayTurnover);
//        tv_qrPayTurnover=(TextView)view.findViewById(R.id.tv_qrPayTurnover);
//        tv_todayOrderSucCount=(TextView)view.findViewById(R.id.tv_todayOrderSucCount);
//        rl_menu=(RelativeLayout)view.findViewById(R.id.rl_menu);
//        rl_report=(RelativeLayout)view.findViewById(R.id.rl_report);
//        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message);
//        rl_member_manager=(RelativeLayout)view.findViewById(R.id.rl_member_manager);
//        rl_marketing=(RelativeLayout)view.findViewById(R.id.rl_marketing);
//        rl_order_manager=(RelativeLayout)view.findViewById(R.id.rl_order_manager);
//        rl_tongji=(RelativeLayout)view.findViewById(R.id.rl_tongji);
//        rl_set=(RelativeLayout)view.findViewById(R.id.rl_set);
//        rl_order_manager.setOnClickListener(this);
//        rl_marketing.setOnClickListener(this);
//        rl_set.setOnClickListener(this);
//        rl_message.setOnClickListener(this);
//        rl_member_manager.setOnClickListener(this);
//        rl_report.setOnClickListener(this);
//        rl_menu.setOnClickListener(this);
//        rl_tongji.setOnClickListener(this);
//        tv_shop_name.setText(merchantBean.getStoreName());
//        getHome();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);

        recommendGoodsBeanList=new ArrayList<>();
        rl_search.setOnClickListener(this);

        lv_home.setOnRefreshListener(this);
        if (!isopen) {
            getRecommendGoodList();
        }
    }









    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_womenshoe:{

                break;
            }
            case R.id.rl_search: {
                Intent intent = new Intent(mContext, SearchGoodsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
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

//    public void getHome() {
//        loadingDialog.show();
//        GetAdListProtocol getHomeProtocol=new GetAdListProtocol();
//        url = getHomeProtocol.getApiFun();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("position", String.valueOf(1));
//
//        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
//
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                loadingDialog.dismiss();
//                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
//                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
//                if (getHomeResponse.getCode().equals("0")) {
//                    getGoodList();
//                    getQualityLifeGoodList();
//                    getRecommendGoodList();
//                    if (getHomeResponse.dataList.size() > 0) {
//                        setImageViews(getHomeResponse.dataList);
//                        ll_pager.setVisibility(View.VISIBLE);
//                    }else{
//                        ll_pager.setVisibility(View.INVISIBLE);
//                    }
//
//                } else {
//                    if(getHomeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){
//
//                        DialogUtils.showAlertToLoginDialog(mContext,
//                                getHomeResponse.msg);
//                    }else{
//                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
//                    }
//                }
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                DialogUtils.showAlertDialog(mContext, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//        });
//    }
//
//    //每日精品
//    public void getGoodList() {
//        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
//        String url = getGoodListProtocol.getApiFun();
//        final HashMap<String,String> map=new HashMap<>();
//        map.put("perDayGoods","1");
//        map.put("pageNo","1");
//        map.put("pageSize","999");
//
//
//
//        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//                Gson gson = new Gson();
//                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
//                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
//                if (getGoodsListResponse.code.equals("0")) {
//                    if(getGoodsListResponse.dataList.size()>0){
//                        perDayGoodsBeanList.clear();
//                        perDayGoodsBeanList.addAll(getGoodsListResponse.dataList);
//                        if(goodsAdapter==null){
//                            goodsAdapter=new GoodsAdapter(mContext,getGoodsListResponse.dataList);
//                            gv_jingxuan.setAdapter(goodsAdapter);
//                        }else{
//                            goodsAdapter.setDate(getGoodsListResponse.dataList);
//                            goodsAdapter.notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    DialogUtils.showAlertDialog(mContext,
//                            getGoodsListResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                DialogUtils.showAlertDialog(mContext, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//
//
//        });
//    }
//    //品质生活
//    public void getQualityLifeGoodList() {
//        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
//        String url = getGoodListProtocol.getApiFun();
//        final HashMap<String,String> map=new HashMap<>();
//        map.put("qualityLife","1");
//        map.put("pageNo","1");
//        map.put("pageSize","999");
//
//
//
//        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//                Gson gson = new Gson();
//                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
//                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
//                if (getGoodsListResponse.code.equals("0")) {
//                    if(getGoodsListResponse.dataList.size()>0){
//                        qualityLifeGoodsBeanList.clear();
//                        qualityLifeGoodsBeanList.addAll(getGoodsListResponse.dataList);
//                        if(goodsAdapter1==null){
//                            goodsAdapter1=new GoodsAdapter(mContext,getGoodsListResponse.dataList);
//                            gv_pinzhi.setAdapter(goodsAdapter1);
//                        }else{
//                            goodsAdapter1.setDate(getGoodsListResponse.dataList);
//                            goodsAdapter1.notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    DialogUtils.showAlertDialog(mContext,
//                            getGoodsListResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                DialogUtils.showAlertDialog(mContext, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//
//
//        });
//    }
    //推荐
    public void getRecommendGoodList() {
        loadingDialog.show();
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        map.put("recommend","1");
        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize","10");



        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.code.equals("0")) {
                    if(getGoodsListResponse.dataList.size()>0){
                        isopen = true;
                        if (pageNo == 1) {
                            recommendGoodsBeanList.clear();
                            recommendGoodsBeanList.add(null);
                            recommendGoodsBeanList.add(null);
                        }
                        recommendGoodsBeanList.addAll(getGoodsListResponse.getDataList());
                        if(tabHomeAdapter==null){
                            tabHomeAdapter=new TabHomeAdapter(mContext,getGoodsListResponse.dataList,loadingDialog);
                            lv_home.setAdapter(tabHomeAdapter);
                        }else{
                            tabHomeAdapter.setDate(recommendGoodsBeanList);
                            tabHomeAdapter.notifyDataSetChanged();
                        }
                        lv_home.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getGoodsListResponse.msg);
                }

                if (isRefresh) {
                    isRefresh = false;
                    lv_home.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                if (isRefresh) {
                    isRefresh = false;
                    lv_home.onRefreshComplete();
                }
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {
                if (isRefresh) {
                    isRefresh = false;
                    lv_home.onRefreshComplete();
                }
                loadingDialog.dismiss();
            }


        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_home.isHeaderShown()) {
                pageNo = 1;
                recommendGoodsBeanList.clear();
                if(tabHomeAdapter!=null) {
                    tabHomeAdapter.setLoading(false);
                }
                getRecommendGoodList();
            } else if (lv_home.isFooterShown()) {
                pageNo++;

                getRecommendGoodList();
            }
        }
    }
}
