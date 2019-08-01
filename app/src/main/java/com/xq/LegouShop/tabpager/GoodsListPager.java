package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodsInfoActivity;
import com.xq.LegouShop.adapter.GoodsAdapter;
import com.xq.LegouShop.adapter.HomeAdapter;
import com.xq.LegouShop.adapter.HomeItemAdapter;
import com.xq.LegouShop.adapter.MenuAdapter;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.protocol.GetCategoryListProtocol;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.zhangke.websocket.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * 购物车
 * @描述: TODO
 */
public class GoodsListPager extends ViewTabBasePager {


    // 分类listview

    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    public String categoryId;
    @ViewInject(R.id.gv_goods)
    private GridView gv_goods;
    private boolean isRequest;
    public GoodsListPager(Context context,String categoryId) {
        super(context);
        this.categoryId=categoryId;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.goodslist_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        LogUtils.e("getGoodsListResponse请求:"+categoryId);
        if(!isRequest) {
            isRequest=true;
            getGoodList();
        }


    }


    public void getGoodList() {
        loadingDialog.show();
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        map.put("categoryId",categoryId);
        map.put("pageNo","1");
        map.put("pageSize","999");



        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                final GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponse da:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.code.equals("0")) {
                    if(getGoodsListResponse.dataList.size()>0){
                        GoodsAdapter adapter = new GoodsAdapter(mContext, getGoodsListResponse.getDataList());
                        gv_goods.setAdapter(adapter);
                    }
                    gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                            intent.putExtra("goodsId", getGoodsListResponse.getDataList().get(i).id);
                            UIUtils.startActivityNextAnim(intent);
                        }
                    });

                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getGoodsListResponse.msg);
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

}
