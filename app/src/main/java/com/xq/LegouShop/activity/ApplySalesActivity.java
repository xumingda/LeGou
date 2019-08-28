package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.protocol.UpdatePwdProtocol;
import com.xq.LegouShop.response.UpdatePwdResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;

//申请售后
public class ApplySalesActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private ImageLoader imageLoader;
    private Dialog loadingDialog;
    private String newPwd;
    private String oldPwd;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
    private TextView tv_time;
    private TextView tv_shopName;
    private TextView tv_order_no;
    private TextView tv_name;
    private TextView tv_num;
    private TextView tv_info;
    private ImageView iv_pic;
    private RelativeLayout rl_tuikuan,rl_tuihuo,rl_huanhuo;
    private  String orderNo,time,pic,goodName,bugCount,orderMoney,shopName,orderId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_apply_sales, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {
        Intent intent=getIntent();
        orderId=intent.getStringExtra("orderId");
        orderNo=intent.getStringExtra("orderNo");
        shopName=intent.getStringExtra("shopName");
        time=intent.getStringExtra("time");
        pic=intent.getStringExtra("pic");
        goodName=intent.getStringExtra("goodName");
        bugCount=intent.getStringExtra("bugCount");
        orderMoney=intent.getStringExtra("orderMoney");
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(ApplySalesActivity.this, false);
        view_back=(View)findViewById(R.id.view_back);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_order_no= (TextView) findViewById(R.id.tv_order_no);
        tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_info= (TextView)findViewById(R.id.tv_info);
        iv_pic= (ImageView) findViewById(R.id.iv_pic);
        rl_tuikuan= (RelativeLayout) findViewById(R.id.rl_tuikuan);
        rl_tuihuo= (RelativeLayout) findViewById(R.id.rl_tuihuo);
        rl_huanhuo= (RelativeLayout) findViewById(R.id.rl_huanhuo);
        rl_tuihuo.setOnClickListener(this);
        view_back.setOnClickListener(this);
        rl_tuikuan.setOnClickListener(this);
        rl_huanhuo.setOnClickListener(this);
        if(!TextUtils.isEmpty(shopName)) {
            tv_shopName.setText(shopName);
        }else{
            tv_shopName.setText("平台自营");
        }
        tv_order_no.setText("订单号:"+orderNo);

        tv_time.setText(time);
        tv_name.setText(goodName);
//        vh.tv_info.setText(orderBean.getOrderGoodsList().get(0).getGoodsGroupValues());
        tv_num.setText("X"+bugCount);
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+pic, iv_pic, PictureOption.getSimpleOptions());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updatePwd() {
        loadingDialog.show();
        UpdatePwdProtocol updatePhoneProtocol = new UpdatePwdProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        String url = updatePhoneProtocol.getApiFun();
        hashMap.put("newPwd", MD5Utils.MD5(newPwd));
        hashMap.put("oldPwd", MD5Utils.MD5(oldPwd));
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                UpdatePwdResponse updatePhoneResponse = gson.fromJson(json, UpdatePwdResponse.class);
                LogUtils.e("updatePhoneResponse:" + updatePhoneResponse.toString());
                if (updatePhoneResponse.code.equals("0")) {
                    SharedPrefrenceUtils.setString(ApplySalesActivity.this, "userphone", "");
                    SharedPrefrenceUtils.setString(ApplySalesActivity.this, "token", "");
                    Intent intent=new Intent(ApplySalesActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                } else {
                    DialogUtils.showAlertDialog(ApplySalesActivity.this,
                            updatePhoneResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ApplySalesActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_huanhuo:{
                Intent intent=new Intent(this, ApplicationDrawbackActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("type",3);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("shopName",shopName);
                intent.putExtra("time",time);
                intent.putExtra("pic",pic);
                intent.putExtra("goodName",goodName);
                intent.putExtra("orderMoney",orderMoney);
                intent.putExtra("bugCount",bugCount);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_tuihuo:{
                Intent intent=new Intent(this, ApplicationDrawbackActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("type",2);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("shopName",shopName);
                intent.putExtra("time",time);
                intent.putExtra("pic",pic);
                intent.putExtra("goodName",goodName);
                intent.putExtra("orderMoney",orderMoney);
                intent.putExtra("bugCount",bugCount);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_tuikuan:{
                Intent intent=new Intent(this, ApplicationDrawbackActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("type",1);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("shopName",shopName);
                intent.putExtra("time",time);
                intent.putExtra("pic",pic);
                intent.putExtra("goodName",goodName);
                intent.putExtra("orderMoney",orderMoney);
                intent.putExtra("bugCount",bugCount);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }
}
