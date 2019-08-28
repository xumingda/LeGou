package com.xq.LegouShop.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetAuthenticationInfoResponse;
import com.xq.LegouShop.response.GetOrderChangeGoodsDescResponse;
import com.xq.LegouShop.response.GetOrderRefundDescResponse;
import com.xq.LegouShop.response.GetOrderReturnGoodsDescResponse;
import com.xq.LegouShop.response.ImageResponse;
import com.xq.LegouShop.util.BitmapUtils;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MobileUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.ProviderUtil;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//退款详情
public class DrawbackInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private ImageLoader imageLoader;
    private Dialog loadingDialog;
    public static boolean isForeground = false;
    private View view_back;
    private TextView tv_time;
    private TextView tv_shopName;
    private TextView tv_order_no, tv_status, tv_createTime;
    private TextView tv_name;
    private TextView tv_history, tv_cancel;
    private TextView tv_num;
    private TextView tv_price, tv_title;
    private ImageView iv_pic, iv_shop_in_pic, iv_shop_in_pic_two, iv_shop_in_pic_three, iv_shop_in_pic_four, iv_shop_in_pic_five, iv_shop_in_pic_six;
    private String orderNo, time, pic, goodName, bugCount, orderMoney, shopName;
    private int userRefundId, userReturnGoodsId, userChangeGoodsId;
    private int type;
    private int orderId;
    private int status;
    private TextView tv_reason, tv_send,tv_tixing;
    private String url;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_drawback_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }


    private void initDate() {
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0);
        type = intent.getIntExtra("type", 0);
        userReturnGoodsId = intent.getIntExtra("userReturnGoodsId", 0);
        userRefundId = intent.getIntExtra("userRefundId", 0);
        userChangeGoodsId= intent.getIntExtra("userChangeGoodsId", 0);
        orderNo = intent.getStringExtra("orderNo");
        shopName = intent.getStringExtra("shopName");
        time = intent.getStringExtra("time");
        pic = intent.getStringExtra("pic");
        goodName = intent.getStringExtra("goodName");
        bugCount = intent.getStringExtra("bugCount");
        orderMoney = intent.getStringExtra("orderMoney");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(DrawbackInfoActivity.this, false);

        view_back = (View) findViewById(R.id.view_back);
        tv_tixing= (TextView) findViewById(R.id.tv_tixing);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_createTime = (TextView) findViewById(R.id.tv_createTime);
        tv_history = (TextView) findViewById(R.id.tv_history);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_shop_in_pic = (ImageView) findViewById(R.id.iv_shop_in_pic);
        iv_shop_in_pic_two = (ImageView) findViewById(R.id.iv_shop_in_pic_two);
        iv_shop_in_pic_three = (ImageView) findViewById(R.id.iv_shop_in_pic_three);
        iv_shop_in_pic_four = (ImageView) findViewById(R.id.iv_shop_in_pic_four);
        iv_shop_in_pic_five = (ImageView) findViewById(R.id.iv_shop_in_pic_five);
        iv_shop_in_pic_six = (ImageView) findViewById(R.id.iv_shop_in_pic_six);
        view_back.setOnClickListener(this);
        iv_shop_in_pic.setOnClickListener(this);
        iv_shop_in_pic_two.setOnClickListener(this);
        iv_shop_in_pic_three.setOnClickListener(this);
        iv_shop_in_pic_four.setOnClickListener(this);
        iv_shop_in_pic_five.setOnClickListener(this);
        iv_shop_in_pic_six.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_tixing.setOnClickListener(this);
        if (userRefundId > 0) {
            url = "/user/getOrderRefundDesc";
            getOrderRefundDesc();
        } else if (userReturnGoodsId > 0) {
            url = "/user/getOrderReturnGoodsDesc";
            getOrderRefundDesc();
        } else if (userChangeGoodsId > 0) {
            url = "/user/getOrderChangeGoodsDesc";
            getOrderRefundDesc();
        }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tixing:{
                UIUtils.showToastSafe("已提醒商家换货!");
                break;
            }
            case R.id.tv_cancel: {
                if (userRefundId > 0) {
                    if (status == 0) {
                        cancelRefund();
                    } else if (status == 2) {
                        Intent intent = new Intent(this, ApplySalesActivity.class);
                        intent.putExtra("orderId", String.valueOf(orderId));
                        intent.putExtra("orderNo",orderNo);
                        intent.putExtra("shopName",shopName);
                        intent.putExtra("time",time);
                        intent.putExtra("pic",pic);
                        intent.putExtra("goodName",goodName);
                        intent.putExtra("orderMoney",orderMoney);
                        intent.putExtra("bugCount",bugCount);
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                        break;
                    }
                } else if (userReturnGoodsId > 0) {
                    if (status == 1 || status == 2) {
                        cancelReturnGoods();
                    } else if (status == 3) {
                        Intent intent = new Intent(this, ApplySalesActivity.class);
                        intent.putExtra("orderNo",orderNo);
                        intent.putExtra("shopName",shopName);
                        intent.putExtra("time",time);
                        intent.putExtra("pic",pic);
                        intent.putExtra("goodName",goodName);
                        intent.putExtra("orderMoney",orderMoney);
                        intent.putExtra("bugCount",bugCount);
                        intent.putExtra("orderId", String.valueOf(orderId));
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                        break;
                    }
                }else if(userChangeGoodsId>0){
                    if (status == 1 || status == 2) {
                        cancelChangeGoods();
                    } else if (status == 6) {
                        receiveChangeGoods();
                        break;
                    }else if (status==7){
                        Intent intent = new Intent(this, ApplySalesActivity.class);
                        intent.putExtra("orderNo",orderNo);
                        intent.putExtra("shopName",shopName);
                        intent.putExtra("time",time);
                        intent.putExtra("pic",pic);
                        intent.putExtra("goodName",goodName);
                        intent.putExtra("orderMoney",orderMoney);
                        intent.putExtra("bugCount",bugCount);
                        intent.putExtra("orderId", String.valueOf(orderId));
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                        break;
                    }
                }
                break;
            }
            case R.id.tv_send: {
                Intent intent = new Intent(this, InputLogisticsActivity.class);
                if(userReturnGoodsId>0) {
                    intent.putExtra("userReturnGoodsId", userReturnGoodsId);
                }else if(userChangeGoodsId>0){
                    intent.putExtra("userChangeGoodsId", userChangeGoodsId);
                }
                UIUtils.startActivityForResultNextAnim(intent,100);
                break;
            }
            case R.id.view_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            getOrderRefundDesc();
        }
    }

    //获取退款详情
    public void getOrderRefundDesc() {
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        if (userReturnGoodsId > 0) {
            hashMap.put("userReturnGoodsId", String.valueOf(userReturnGoodsId));
        }
        if (userRefundId > 0) {
            hashMap.put("userRefundId", String.valueOf(userRefundId));
        }
        if (userChangeGoodsId > 0) {
            hashMap.put("userChangeGoodsId", String.valueOf(userChangeGoodsId));
        }

        LogUtils.e("请求:" + hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                if (userRefundId > 0) {
                    GetOrderRefundDescResponse getOrderRefundDescResponse = gson.fromJson(json, GetOrderRefundDescResponse.class);
                    LogUtils.e("getOrderRefundDescResponse:" + getOrderRefundDescResponse.toString());
                    if (getOrderRefundDescResponse.code.equals("0")) {
                        if (!TextUtils.isEmpty(getOrderRefundDescResponse.orderInfo.shopName)) {
                            tv_shopName.setText(getOrderRefundDescResponse.orderInfo.shopName);
                        } else {
                            tv_shopName.setText("平台自营");
                        }
                        for (int i = 0; i < getOrderRefundDescResponse.dataList.size(); i++) {
                            if (i == 0) {
                                tv_history.setText(getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            } else {
                                tv_history.setText(tv_history.getText().toString() + "\n\n" + getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            }
                        }
                        //0待商家同意退款，1同意，2拒绝，3用户自己取消
                        status = getOrderRefundDescResponse.data.status;
                        if (getOrderRefundDescResponse.data.status == 0) {
                            tv_status.setText("待商家同意退款");
                        } else if (getOrderRefundDescResponse.data.status == 1) {
                            tv_status.setText("商家同意退款");
                        } else if (getOrderRefundDescResponse.data.status == 2) {
                            tv_status.setText("商家拒绝退款");
                            tv_cancel.setText("重新提交");
                        } else {
                            tv_cancel.setVisibility(View.GONE);
                            tv_status.setText("用户自己取消");
                        }
                        LogUtils.e("getOrderRefundDescResponse图：" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0]);
                        switch (convertStrToArray(getOrderRefundDescResponse.data.pics).length) {
                            case 1: {
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 2: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 3: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 4: {
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 5: {
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 6: {
                                iv_shop_in_pic_six.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[5], iv_shop_in_pic_six, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }

                        }
                        tv_createTime.setText(getOrderRefundDescResponse.data.updateTime);
                        tv_order_no.setText("订单号:" + getOrderRefundDescResponse.orderInfo.orderNo);
                        tv_reason.setText("退款原因:" + getOrderRefundDescResponse.data.reason);
                        tv_time.setText(getOrderRefundDescResponse.orderInfo.createTime);
                        tv_name.setText(getOrderRefundDescResponse.orderGoodsList.get(0).getGoodsName());
                        tv_price.setText("¥" + getOrderRefundDescResponse.orderGoodsList.get(0).getSalePrice());
                        tv_num.setText("X" + getOrderRefundDescResponse.orderGoodsList.get(0).getBuyCount());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getOrderRefundDescResponse.orderGoodsList.get(0).getPic(), iv_pic, PictureOption.getSimpleOptions());
                    } else {
                        DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                                getOrderRefundDescResponse.msg);
                    }
                } else if (userReturnGoodsId > 0) {
                    tv_cancel.setText("取消退货");
                    GetOrderReturnGoodsDescResponse getOrderRefundDescResponse = gson.fromJson(json, GetOrderReturnGoodsDescResponse.class);
                    LogUtils.e("getOrderRefundDescResponse:" + getOrderRefundDescResponse.toString());
                    if (getOrderRefundDescResponse.code.equals("0")) {
                        if (!TextUtils.isEmpty(getOrderRefundDescResponse.orderInfo.shopName)) {
                            tv_shopName.setText(getOrderRefundDescResponse.orderInfo.shopName);
                        } else {
                            tv_shopName.setText("平台自营");
                        }
                        for (int i = 0; i < getOrderRefundDescResponse.dataList.size(); i++) {
                            if (i == 0) {
                                tv_history.setText(getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            } else {
                                tv_history.setText(tv_history.getText().toString() + "\n\n" + getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            }
                        }
                        //1申请中，2商家同意=买家待发货，3商家拒绝，4买家已发货，5商家已收货=已完成，已退款，6买家取消退货
                        status = getOrderRefundDescResponse.data.status;
                        if (getOrderRefundDescResponse.data.status == 6) {
                            tv_cancel.setVisibility(View.GONE);
                            tv_status.setText("买家取消退货");
                        } else if (getOrderRefundDescResponse.data.status == 1) {
                            tv_status.setText("申请中");
                        } else if (getOrderRefundDescResponse.data.status == 2) {
                            tv_status.setText("商家同意，买家待发货");
                            tv_send.setVisibility(View.VISIBLE);
                        } else if (getOrderRefundDescResponse.data.status == 3) {
                            tv_status.setText("商家拒绝");
                            tv_cancel.setText("重新提交");
                        } else if (getOrderRefundDescResponse.data.status == 4) {
                            tv_status.setText("商家同意，买家已发货");
                            tv_cancel.setVisibility(View.GONE);
                        } else {
                            tv_status.setText("商家已收货,已退款");
                            tv_cancel.setVisibility(View.GONE);
                        }

                        LogUtils.e("getOrderRefundDescResponse图：" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0]);
                        switch (convertStrToArray(getOrderRefundDescResponse.data.pics).length) {
                            case 1: {
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 2: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 3: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 4: {
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 5: {
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 6: {
                                iv_shop_in_pic_six.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[5], iv_shop_in_pic_six, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }

                        }
                        tv_createTime.setText(getOrderRefundDescResponse.data.updateTime);
                        tv_order_no.setText("订单号:" + getOrderRefundDescResponse.orderInfo.orderNo);
                        tv_reason.setText("换货原因:" + getOrderRefundDescResponse.data.reason);
                        tv_time.setText(getOrderRefundDescResponse.orderInfo.createTime);
                        tv_name.setText(getOrderRefundDescResponse.orderGoodsList.get(0).getGoodsName());
                        tv_price.setText("¥" + getOrderRefundDescResponse.orderGoodsList.get(0).getSalePrice());
                        tv_num.setText("X" + getOrderRefundDescResponse.orderGoodsList.get(0).getBuyCount());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getOrderRefundDescResponse.orderGoodsList.get(0).getPic(), iv_pic, PictureOption.getSimpleOptions());
                    } else {
                        DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                                getOrderRefundDescResponse.msg);
                    }
                } else if (userChangeGoodsId > 0) {
                    tv_cancel.setText("取消换货");
                    GetOrderChangeGoodsDescResponse getOrderRefundDescResponse = gson.fromJson(json, GetOrderChangeGoodsDescResponse.class);
                    LogUtils.e("getOrderRefundDescResponse:" + getOrderRefundDescResponse.toString());
                    if (getOrderRefundDescResponse.code.equals("0")) {
                        if (!TextUtils.isEmpty(getOrderRefundDescResponse.orderInfo.shopName)) {
                            tv_shopName.setText(getOrderRefundDescResponse.orderInfo.shopName);
                        } else {
                            tv_shopName.setText("平台自营");
                        }
                        for (int i = 0; i < getOrderRefundDescResponse.dataList.size(); i++) {
                            if (i == 0) {
                                tv_history.setText(getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            } else {
                                tv_history.setText(tv_history.getText().toString() + "\n\n" + getOrderRefundDescResponse.dataList.get(i).createTime + " " + getOrderRefundDescResponse.dataList.get(i).remark);
                            }
                        }
                        //换货状态，1申请中，2商家同意=买家待发货，3商家拒绝，4买家已发货，5商家已收货=待发货，6商家已发货=买家待收货，7买家已收货=已完成，8买家取消换货
                        status = getOrderRefundDescResponse.data.status;
                        if (getOrderRefundDescResponse.data.status == 8) {
                            tv_cancel.setVisibility(View.GONE);
                            tv_status.setText("买家取消换货");
                        } else if (getOrderRefundDescResponse.data.status == 1) {
                            tv_status.setText("申请中");
                        } else if (getOrderRefundDescResponse.data.status == 2) {
                            tv_status.setText("商家同意，买家待发货");
                            tv_send.setVisibility(View.VISIBLE);
                        } else if (getOrderRefundDescResponse.data.status == 3) {
                            tv_status.setText("商家拒绝");
                            tv_cancel.setText("重新提交");
                        } else if (getOrderRefundDescResponse.data.status == 4) {
                            tv_status.setText("商家同意，买家已发货");
                            tv_tixing.setVisibility(View.VISIBLE);
                            tv_cancel.setVisibility(View.GONE);
                        } else if (getOrderRefundDescResponse.data.status ==5) {
                            tv_status.setText("商家已收货,待发货");
                            tv_tixing.setVisibility(View.VISIBLE);
                            tv_cancel.setVisibility(View.GONE);
                        } else if (getOrderRefundDescResponse.data.status ==6) {
                            tv_status.setText("商家已收货,待收货");
                            tv_cancel.setText("确认收货");
                        }else if (getOrderRefundDescResponse.data.status ==7) {
                            tv_status.setText("买家已收货，已完成");
                            tv_cancel.setText("再次申请售后");
                        }

                        LogUtils.e("getOrderRefundDescResponse图：" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0]);
                        switch (convertStrToArray(getOrderRefundDescResponse.data.pics).length) {
                            case 1: {
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 2: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 3: {
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 4: {
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 5: {
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }
                            case 6: {
                                iv_shop_in_pic_six.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[5], iv_shop_in_pic_six, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[4], iv_shop_in_pic_five, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[3], iv_shop_in_pic_four, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[2], iv_shop_in_pic_three, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[1], iv_shop_in_pic_two, PictureOption.getSimpleOptions());
                                imageLoader.displayImage("http://qiniu.lelegou.pro/" + convertStrToArray(getOrderRefundDescResponse.data.pics)[0], iv_shop_in_pic, PictureOption.getSimpleOptions());
                                break;
                            }

                        }
                        tv_createTime.setText(getOrderRefundDescResponse.data.updateTime);
                        tv_order_no.setText("订单号:" + getOrderRefundDescResponse.orderInfo.orderNo);
                        tv_reason.setText("换货原因:" + getOrderRefundDescResponse.data.reason);
                        tv_time.setText(getOrderRefundDescResponse.orderInfo.createTime);
                        tv_name.setText(getOrderRefundDescResponse.orderGoodsList.get(0).getGoodsName());
                        tv_price.setText("¥" + getOrderRefundDescResponse.orderGoodsList.get(0).getSalePrice());
                        tv_num.setText("X" + getOrderRefundDescResponse.orderGoodsList.get(0).getBuyCount());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getOrderRefundDescResponse.orderGoodsList.get(0).getPic(), iv_pic, PictureOption.getSimpleOptions());
                    } else {
                        DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                                getOrderRefundDescResponse.msg);
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(DrawbackInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    //取消退款
    public void cancelRefund() {
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userRefundId", String.valueOf(userRefundId));

        String url = "/user/cancelRefund";
        LogUtils.e("请求:" + hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                FindPwdResponse getOrderRefundDescResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("cancelRefundResponse:" + getOrderRefundDescResponse.toString());
                if (getOrderRefundDescResponse.code.equals("0")) {
                    UIUtils.showToastSafe(getOrderRefundDescResponse.msg);
                    tv_cancel.setVisibility(View.GONE);
                    getOrderRefundDesc();
                } else {
                    DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                            getOrderRefundDescResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(DrawbackInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    //取消退货
    public void cancelReturnGoods() {
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userReturnGoodsId", String.valueOf(userReturnGoodsId));

        String url = "/user/cancelReturnGoods";
        LogUtils.e("请求:" + hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                FindPwdResponse getOrderRefundDescResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("cancelReturnGoods:" + getOrderRefundDescResponse.toString());
                if (getOrderRefundDescResponse.code.equals("0")) {
                    UIUtils.showToastSafe(getOrderRefundDescResponse.msg);
                    tv_cancel.setVisibility(View.GONE);
                    getOrderRefundDesc();
                } else {
                    DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                            getOrderRefundDescResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(DrawbackInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }
    //取消换货
    public void cancelChangeGoods() {
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userChangeGoodsId", String.valueOf(userChangeGoodsId));

        String url = "/user/cancelChangeGoods";
        LogUtils.e("请求:" + hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                FindPwdResponse getOrderRefundDescResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("cancelReturnGoods:" + getOrderRefundDescResponse.toString());
                if (getOrderRefundDescResponse.code.equals("0")) {
                    UIUtils.showToastSafe(getOrderRefundDescResponse.msg);
                    tv_cancel.setVisibility(View.GONE);
                    getOrderRefundDesc();
                } else {
                    DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                            getOrderRefundDescResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(DrawbackInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }
    //确认收货
    public void receiveChangeGoods() {
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userChangeGoodsId", String.valueOf(userChangeGoodsId));

        String url = "/user/receiveChangeGoods";
        LogUtils.e("请求:" + hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                FindPwdResponse getOrderRefundDescResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("cancelReturnGoods:" + getOrderRefundDescResponse.toString());
                if (getOrderRefundDescResponse.code.equals("0")) {
                    UIUtils.showToastSafe(getOrderRefundDescResponse.msg);
                    tv_cancel.setVisibility(View.GONE);
                    getOrderRefundDesc();
                } else {
                    DialogUtils.showAlertDialog(DrawbackInfoActivity.this,
                            getOrderRefundDescResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(DrawbackInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
}
