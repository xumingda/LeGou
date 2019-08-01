package com.xq.LegouShop.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GoodsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 搜索页面
 */
public class SearchGoodsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView tv_top_back;
    private View rootView;
    private String keyword;
    private Dialog loadingDialog;
    private TextView tv_search;
    private EditText edittext;
    private GridView gv_goods;
    private  GoodsAdapter adapter;
    private View view_back;

    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_search_goods, null);
        setContentView(rootView);
        tv_top_back = (ImageView) rootView.findViewById(R.id.iv_top_back);
        tv_search= (TextView) rootView.findViewById(R.id.tv_search);
        edittext= (EditText) rootView.findViewById(R.id.edittext);
        gv_goods= (GridView) rootView.findViewById(R.id.gv_goods);
        view_back=(View)rootView.findViewById(R.id.view_back);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(SearchGoodsActivity.this, true);
        Intent intent = getIntent();
        keyword =  intent.getStringExtra("keyword");
        tv_search.setOnClickListener(this);
        view_back.setOnClickListener(this);
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_search: {
                keyword=edittext.getText().toString();
                getGoodList();
                break;
            }


        }
    }

    public void getGoodList() {
        loadingDialog.show();
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        map.put("keyword",keyword);
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
                        if(adapter==null) {
                            adapter = new GoodsAdapter(SearchGoodsActivity.this, getGoodsListResponse.getDataList());
                            gv_goods.setAdapter(adapter);
                        }else{
                            adapter.setDate(getGoodsListResponse.getDataList());
                            adapter.notifyDataSetChanged();
                        }
                        gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(SearchGoodsActivity.this, GoodsInfoActivity.class);
                                intent.putExtra("goodsId", getGoodsListResponse.getDataList().get(i).id);
                                UIUtils.startActivityNextAnim(intent);
                            }
                        });
                    }

                } else {
                    DialogUtils.showAlertDialog(SearchGoodsActivity.this,
                            getGoodsListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(SearchGoodsActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }
}
