package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.PassAdapter;
import com.xq.LegouShop.adapter.RewardsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.bean.PassBean;
import com.xq.LegouShop.bean.RewardBean;
import com.xq.LegouShop.protocol.GetUserRewardListProtocol;
import com.xq.LegouShop.response.GetUserRewardListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//我的pass卡
public class PassCardsActivity extends BaseActivity implements View.OnClickListener{

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private Dialog loadingDialog;
    private PassAdapter passAdapter;
    private View view_back;
    private ListView lv_pass;
    private GetUserRewardListResponse getUserRewardListResponse;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    private List<PassBean> passBeanList;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_pass_card, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(PassCardsActivity.this, false);
        view_back=(View)findViewById(R.id.view_back);
        ll_empty=(LinearLayout) findViewById(R.id.ll_empty);
        lv_pass=(ListView)findViewById(R.id.lv_pass);
        view_back.setOnClickListener(this);
        passBeanList = (List<PassBean>) getIntent().getSerializableExtra("passList");
        if(passBeanList!=null&&passBeanList.size()>0){
            setData();
        }else {
            ll_empty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }









    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }




    private void setData() {
        passAdapter = new PassAdapter( PassCardsActivity.this, passBeanList);
        lv_pass.setAdapter(passAdapter);

    }
}
