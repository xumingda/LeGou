package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GoodsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodInfoResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.tabpager.GoodsListPager;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//商品列表
public class HomeGoodsListActivity extends BaseActivity  implements  View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private GetGoodInfoResponse getGoodInfoResponse ;
    private View view_back;
    private ImageLoader imageLoader;
    private GridView gv_goods;
    private String title;
    private int type;
    private TextView tv_title;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_home_good_list, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        title=getIntent().getStringExtra("title");
        type=getIntent().getIntExtra("type",0);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(HomeGoodsListActivity.this, false);
        gv_goods=(GridView) findViewById(R.id.gv_goods);
        view_back=(View)findViewById(R.id.view_back);
        tv_title=(TextView) findViewById(R.id.tv_title);

        tv_title.setText(title);
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });


        getGoodList();



    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_buy:{
                Intent intent=new Intent(this,BuyGoodActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }

        }
    }
    public void getGoodList() {
        loadingDialog.show();
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        if(type==1){
            map.put("qualityLife", "1");
        }else{
            map.put("perDayGoods", "1");
        }
        map.put("pageNo","1");
        map.put("pageSize","999");



        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                final GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponsenew:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.code.equals("0")) {
                    if(getGoodsListResponse.dataList.size()>0){
                        GoodsAdapter adapter = new GoodsAdapter(HomeGoodsListActivity.this, getGoodsListResponse.getDataList());
                        gv_goods.setAdapter(adapter);
                        gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(HomeGoodsListActivity.this, GoodsInfoActivity.class);
                                intent.putExtra("goodsId", getGoodsListResponse.dataList.get(i).id);
                                UIUtils.startActivityNextAnim(intent);
                            }
                        });
                    }
                } else {
                    DialogUtils.showAlertDialog(HomeGoodsListActivity.this,
                            getGoodsListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(HomeGoodsListActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }







}
