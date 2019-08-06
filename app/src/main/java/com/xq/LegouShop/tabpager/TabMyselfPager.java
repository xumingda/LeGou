package com.xq.LegouShop.tabpager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.AccountStatementActivity;
import com.xq.LegouShop.activity.AddressActivity;
import com.xq.LegouShop.activity.CertificationActivity;
import com.xq.LegouShop.activity.LoginActivity;
import com.xq.LegouShop.activity.MainActivity;
import com.xq.LegouShop.activity.MyCollectionActivity;
import com.xq.LegouShop.activity.OrderManagerActivity;
import com.xq.LegouShop.activity.PassCardsActivity;
import com.xq.LegouShop.activity.PersonalInfoActivity;
import com.xq.LegouShop.activity.PersonalRewardsActivity;
import com.xq.LegouShop.activity.PersonalSafeActivity;
import com.xq.LegouShop.activity.RechargeActivity;
import com.xq.LegouShop.activity.ShoppingPointsActivity;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.protocol.GetUserInfoProtocol;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.GetUserInfoResponse;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.CircleImageView;
import com.xq.LegouShop.weiget.MyLinearLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * @描述: TODO
 */
public class TabMyselfPager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private RelativeLayout rl_order,rl_integral;
    private RelativeLayout rl_award,rl_address,rl_info,rl_collect,rl_erial,rl_recharge,rl_tixian,rl_myself_change,rl_gouwu_change,rl_safe,rl_certification,rl_out,rl_passcard;
    private Dialog loadingDialog;
    private GetUserInfoResponse getUserInfoResponse;
    private CircleImageView iv_myself_img;
    private TextView tv_name,tv_balanceMoney,tv_changeScore,tv_buyScore,tv_pass;
    private AlertDialog alertDialog;
    private ImageLoader imageLoader;
    /**
     * @param context
     */
    public TabMyselfPager(Context context, FrameLayout mDragLayout,
                          MyLinearLayout mLinearLayout) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;

    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.my_pager, null);
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
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_pass=(TextView)view.findViewById(R.id.tv_pass);
        iv_myself_img=(CircleImageView)view.findViewById(R.id.iv_myself_img);
        tv_balanceMoney=(TextView)view.findViewById(R.id.tv_balanceMoney);
        tv_changeScore=(TextView)view.findViewById(R.id.tv_changeScore);
        tv_buyScore=(TextView)view.findViewById(R.id.tv_buyScore);
        rl_certification=(RelativeLayout)view.findViewById(R.id.rl_certification);
        rl_order=(RelativeLayout)view.findViewById(R.id.rl_order);
        rl_integral=(RelativeLayout)view.findViewById(R.id.rl_integral);
        rl_award=(RelativeLayout)view.findViewById(R.id.rl_award);
        rl_address=(RelativeLayout)view.findViewById(R.id.rl_address);
        rl_info=(RelativeLayout)view.findViewById(R.id.rl_info);
        rl_collect=(RelativeLayout)view.findViewById(R.id.rl_collect);
        rl_erial=(RelativeLayout)view.findViewById(R.id.rl_erial);
        rl_recharge=(RelativeLayout)view.findViewById(R.id.rl_recharge);
        rl_tixian=(RelativeLayout)view.findViewById(R.id.rl_tixian);
        rl_myself_change=(RelativeLayout)view.findViewById(R.id.rl_myself_change);
        rl_gouwu_change=(RelativeLayout)view.findViewById(R.id.rl_gouwu_change);
        rl_safe=(RelativeLayout)view.findViewById(R.id.rl_safe);
        rl_out=(RelativeLayout)view.findViewById(R.id.rl_out);
        rl_passcard=(RelativeLayout)view.findViewById(R.id.rl_passcard);
        rl_gouwu_change.setOnClickListener(this);
        rl_myself_change.setOnClickListener(this);
        rl_tixian.setOnClickListener(this);
        rl_recharge.setOnClickListener(this);
        rl_erial.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_info.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_award.setOnClickListener(this);
        rl_integral.setOnClickListener(this);
        rl_order.setOnClickListener(this);
        rl_safe.setOnClickListener(this);
        rl_certification.setOnClickListener(this);
        rl_out.setOnClickListener(this);
        rl_passcard.setOnClickListener(this);
        getUserInfo();
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_passcard:{
                Intent intent=new Intent(mContext, PassCardsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("passList", (Serializable) getUserInfoResponse.getDataList());
                intent.putExtras(bundle);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.tv_ensure:{
                BaseActivity.finishAll();
                SharedPrefrenceUtils.setString(mContext,"userphone","");
                SharedPrefrenceUtils.setString(mContext,"token","");
                Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                UIUtils.startActivityNextAnim(intent);
                alertDialog.cancel();
                break;
            }
            case R.id.rl_out:{
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"你真的要退出登录吗？","退出登录",TabMyselfPager.this);
                break;
            }
            case R.id.rl_certification:{
                Intent intent=new Intent(mContext, CertificationActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_safe:{
                Intent intent=new Intent(mContext, PersonalSafeActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_myself_change:{
                Intent intent=new Intent(mContext, RechargeActivity.class);
                intent.putExtra("title","将转换积分转为余额");
                UIUtils.startActivityForResultNextAnim(intent,102);
                break;
            }
            case R.id.rl_gouwu_change:{
                Intent intent=new Intent(mContext, RechargeActivity.class);
                intent.putExtra("title","转去购物积分");
                UIUtils.startActivityForResultNextAnim(intent,102);
                break;
            }
            case R.id.rl_recharge:{
                Intent intent=new Intent(mContext, RechargeActivity.class);
                intent.putExtra("title","充值");
                UIUtils.startActivityForResultNextAnim(intent,102);
                break;
            }
            case R.id.rl_tixian:{
                Intent intent=new Intent(mContext, RechargeActivity.class);
                intent.putExtra("title","提现");
                UIUtils.startActivityForResultNextAnim(intent,102);
                break;
            }
            case R.id.rl_erial:{
                Intent intent=new Intent(mContext, AccountStatementActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_collect:{
                Intent intent=new Intent(mContext, MyCollectionActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_info:{
                Intent intent=new Intent(mContext, PersonalInfoActivity.class);

                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_address:{
                Intent intent=new Intent(mContext, AddressActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_award:{
                Intent intent=new Intent(mContext, PersonalRewardsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_order:{
                Intent intent=new Intent(mContext, OrderManagerActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_integral:{
                Intent intent=new Intent(mContext, ShoppingPointsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }

        }
    }

    public void getUserInfo() {
        loadingDialog.show();
        GetUserInfoProtocol getUserInfoProtocol = new GetUserInfoProtocol();
        String url = getUserInfoProtocol.getApiFun();
        Map<String,String> map=new HashMap<>();
        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("getUserInfoResponse:" + json);
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getUserInfoResponse = gson.fromJson(json, GetUserInfoResponse.class);
                LogUtils.e("getUserInfoResponse:" + getUserInfoResponse.toString());
                if (getUserInfoResponse.code.equals("0")) {
                    tv_name.setText(getUserInfoResponse.data.getNickName());
                    tv_balanceMoney.setText(getUserInfoResponse.data.getBalanceMoney());
                    tv_changeScore.setText(""+getUserInfoResponse.data.getChangeScore());
                    tv_buyScore.setText(""+getUserInfoResponse.data.getBuyScore());
                    tv_pass.setText(getUserInfoResponse.dataList.size()+"");
                    imageLoader.displayImage("http://qiniu.lelegou.pro/"+getUserInfoResponse.data.getHeadurl(),iv_myself_img, PictureOption.getSimpleOptions());
                    SPUtils.saveBean2Sp(mContext,getUserInfoResponse.data,"userInfo","userInfo");
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getUserInfoResponse.msg);
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
