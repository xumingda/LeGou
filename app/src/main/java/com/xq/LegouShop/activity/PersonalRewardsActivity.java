package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.OrderAdapter;
import com.xq.LegouShop.adapter.RewardsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.OrderBean;
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

//个人奖励
public class PersonalRewardsActivity extends BaseActivity implements View.OnClickListener , PullToRefreshBase.OnRefreshListener{

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private Dialog loadingDialog;
    private RewardsAdapter rewardsAdapter;
    private View view_back;
    private PullToRefreshListView lv_reward;
    private GetUserRewardListResponse getUserRewardListResponse;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    private List<RewardBean> rewardBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_personal_rewards, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(PersonalRewardsActivity.this, false);
        view_back=(View)findViewById(R.id.view_back);
        ll_empty=(LinearLayout) findViewById(R.id.ll_empty);
        lv_reward=(PullToRefreshListView)findViewById(R.id.lv_reward);
        view_back.setOnClickListener(this);
        rewardBeanList = new ArrayList<>();
        lv_reward.setMode(PullToRefreshBase.Mode.BOTH);
        lv_reward.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_reward.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_reward.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_reward.setOnRefreshListener(this);
        getRewardList();
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

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_reward.isHeaderShown()) {
                pageNo = 1;
                rewardBeanList.clear();
                getRewardList();
            } else if (lv_reward.isFooterShown()) {
                pageNo++;
                getRewardList();
            }
        }
    }

    private void getRewardList() {
        loadingDialog.show();
        GetUserRewardListProtocol getUserRewardListProtocol=new GetUserRewardListProtocol();
        String url = getUserRewardListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "10");

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                getUserRewardListResponse = gson.fromJson(json, GetUserRewardListResponse.class);
                LogUtils.e("getUserRewardListResponse:" + getUserRewardListResponse.toString());
                if (getUserRewardListResponse.getCode().equals("0")) {
                    if (getUserRewardListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            rewardBeanList.clear();
                        }
                        setData();
                    } else {
                        if(pageNo==1){
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_reward.setVisibility(View.GONE);
                        }else {
                            DialogUtils.showAlertDialog(PersonalRewardsActivity.this, "没有更多数据了！");
                        }

                    }

                }else {
                    if(getUserRewardListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(PersonalRewardsActivity.this,
                                getUserRewardListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(PersonalRewardsActivity.this, getUserRewardListResponse.msg);
                    }
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_reward.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(PersonalRewardsActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_reward.onRefreshComplete();
                }
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    private void setData() {
        rewardBeanList.addAll(getUserRewardListResponse.getDataList());
        if (rewardsAdapter == null) {
            rewardsAdapter = new RewardsAdapter( PersonalRewardsActivity.this, rewardBeanList);
            lv_reward.setAdapter(rewardsAdapter);
        } else {
            rewardsAdapter.setDate(rewardBeanList);
            rewardsAdapter.notifyDataSetChanged();
        }
    }
}
