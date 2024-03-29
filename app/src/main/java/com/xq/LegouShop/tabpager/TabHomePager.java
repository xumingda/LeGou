package com.xq.LegouShop.tabpager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodsInfoActivity;
import com.xq.LegouShop.activity.MainActivity;
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
import com.zhangke.websocket.util.LogUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * @描述: TODO
 */
public class TabHomePager extends TabBasePager implements View.OnClickListener ,PullToRefreshBase.OnRefreshListener{

    private LocationManager locationManager;
    private String locationProvider;
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
    private TextView tv_address;
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

    @SuppressLint("MissingPermission")
    public void initData() {


        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        rl_search=(LinearLayout) view.findViewById(R.id.rl_search);
        lv_home=(PullToRefreshListView)view.findViewById(R.id.lv_home);
        tv_address=(TextView)view.findViewById(R.id.tv_address);
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

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String addString = null;
            List<Address> addList = null;
            Geocoder ge = new Geocoder(mContext);
            try {
                addList = ge.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (addList != null && addList.size() > 0) {
                for (int i = 0; i < addList.size(); i++) {
                    Address ad = addList.get(i);
                    addString = ad.getLocality();//拿到城市
                }
            }
            String locationStr = "维度：" + location.getLatitude()
                    + "经度：" + location.getLongitude();
            tv_address.setText(addString);
            LogUtils.i("andly"+locationStr + "----" + addString);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_search: {
                Intent intent = new Intent(mContext, SearchGoodsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
        }
    }

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
                            recommendGoodsBeanList.add(null);
                        }
                        recommendGoodsBeanList.addAll(getGoodsListResponse.getDataList());

                        if(tabHomeAdapter==null){
                            tabHomeAdapter=new TabHomeAdapter(mContext,recommendGoodsBeanList,loadingDialog);
                            lv_home.setAdapter(tabHomeAdapter);
                        }else{
                            tabHomeAdapter.setDate(recommendGoodsBeanList);
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
