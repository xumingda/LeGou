package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.IogisticsInfoAdapter;
import com.xq.LegouShop.adapter.OrderAdapter;
import com.xq.LegouShop.adapter.OrderDescGoodsAdapter;
import com.xq.LegouShop.adapter.OrderGoodsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.bean.LogisticsInfo;
import com.xq.LegouShop.protocol.CancelOrderProtocol;
import com.xq.LegouShop.protocol.CreateOrderProtocol;
import com.xq.LegouShop.protocol.FinishOrderProtocol;
import com.xq.LegouShop.protocol.GetOderDescProtocol;
import com.xq.LegouShop.protocol.GetUserReceiveAddressListProtocol;
import com.xq.LegouShop.protocol.PayOrderProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.response.GetOrderDescResponse;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.ScollViewListView;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//订单详情
public class OrderInfoActivity extends BaseActivity  implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private IWXAPI api;
    private String userReceiveAddressId,buyerMessage;
    private View view_back,view_update;
    private ImageLoader imageLoader;
    private RelativeLayout rl_main;
    private TextView tv_total_price;
    private ScollViewListView lv_goods;
    private LinearLayout ll_wuliu,ll_bottom;
    private ScollViewListView lv_wuliu;
    private OrderDescGoodsAdapter orderGoodsAdapter;
    private IogisticsInfoAdapter iogisticsInfoAdapter;
    private TextView tv_name,tv_phone,tv_address,tv_buyerMessage,tv_order_no,tv_status,tv_createTime,tv_receiveTime,tv_payTime,tv_sendTime,tv_logistics,tv_wuliu_title,tv_logisticsNo,tv_finish,tv_send,tv_cancle,tv_pay;
    private int orderId;
    private int status;
    private Dialog dialog;
    //1取消，2付款，3完成
    private int type;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_order_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        orderId=getIntent().getIntExtra("orderId",0);
        status=getIntent().getIntExtra("status",0);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(OrderInfoActivity.this, false);
        ll_wuliu=(LinearLayout)findViewById(R.id.ll_wuliu);
        ll_bottom=(LinearLayout)findViewById(R.id.ll_bottom);
        tv_wuliu_title=(TextView)findViewById(R.id.tv_wuliu_title);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        lv_goods=(ScollViewListView)findViewById(R.id.lv_goods);
        lv_wuliu=(ScollViewListView)findViewById(R.id.lv_wuliu);
        tv_buyerMessage=(TextView)findViewById(R.id.tv_buyerMessage);
        view_back=(View)findViewById(R.id.view_back);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_address=(TextView)findViewById(R.id.tv_address);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_logistics=(TextView)findViewById(R.id.tv_logistics);
        tv_logisticsNo=(TextView)findViewById(R.id.tv_logisticsNo);
        tv_order_no=(TextView)findViewById(R.id.tv_order_no);
        tv_status=(TextView)findViewById(R.id.tv_status);
        tv_createTime=(TextView)findViewById(R.id.tv_createTime);
        tv_receiveTime=(TextView)findViewById(R.id.tv_receiveTime);
        tv_payTime=(TextView)findViewById(R.id.tv_payTime);
        tv_sendTime=(TextView)findViewById(R.id.tv_sendTime);
        tv_cancle=(TextView)findViewById(R.id.tv_cancle);
        tv_pay=(TextView)findViewById(R.id.tv_pay);
        tv_send=(TextView)findViewById(R.id.tv_send);
        tv_finish=(TextView)findViewById(R.id.tv_finish);
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });



        getOrderDesc();
        switch (status){
            case 0:{
                tv_pay.setVisibility(View.VISIBLE);
                tv_cancle.setVisibility(View.VISIBLE);
                tv_send.setVisibility(View.GONE);
                tv_finish.setVisibility(View.GONE);
                ll_wuliu.setVisibility(View.GONE);
                tv_wuliu_title.setVisibility(View.GONE);
                lv_wuliu.setVisibility(View.GONE);
                break;
            }
            case 1:{
                tv_pay.setVisibility(View.GONE);
                tv_cancle.setVisibility(View.GONE);
                tv_send.setVisibility(View.VISIBLE);
                tv_finish.setVisibility(View.GONE);
                ll_wuliu.setVisibility(View.GONE);
                tv_wuliu_title.setVisibility(View.GONE);
                lv_wuliu.setVisibility(View.GONE);
                break;
            }
            case 2:{
                tv_pay.setVisibility(View.GONE);
                tv_cancle.setVisibility(View.GONE);
                tv_send.setVisibility(View.GONE);
                tv_finish.setVisibility(View.VISIBLE);
                ll_wuliu.setVisibility(View.VISIBLE);
                tv_wuliu_title.setVisibility(View.VISIBLE);
                lv_wuliu.setVisibility(View.VISIBLE);
                break;
            } case 3:{
                tv_pay.setVisibility(View.GONE);
                tv_cancle.setVisibility(View.GONE);
                tv_send.setVisibility(View.GONE);
                tv_finish.setVisibility(View.GONE);
                ll_wuliu.setVisibility(View.VISIBLE);
                tv_wuliu_title.setVisibility(View.VISIBLE);
                lv_wuliu.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.GONE);
                break;
            }
        }
        tv_send.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finish:{
                type=3;
                dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否完成订单？","提示", OrderInfoActivity.this);
                dialog.show();
                break;
            }
            case R.id.tv_cancle:{
                type=1;
                dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否取消订单？","提示", OrderInfoActivity.this);
                dialog.show();
                break;
            }
            case R.id.tv_pay:{
                type=2;
                dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否付款订单？","提示", OrderInfoActivity.this);
                dialog.show();
                break;
            }
            case R.id.tv_send:{
                UIUtils.showToast(this,"已提醒发货!");
                break;
            }
            case R.id.tv_ensure:{
                if(type==1) {
                    cancelOrderList();
                }else if(type==2){
                    payOrderList();
                }else{
                    finishOrderList();
                }
                dialog.dismiss();
                break;
            }
            case R.id.tv_add_address:{
                Intent intent=new Intent(this, AddAddressActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,100);
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }





    //获取订单详情
    public void getOrderDesc() {
        loadingDialog.show();
        GetOderDescProtocol getOderDescProtocol = new GetOderDescProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        String url = getOderDescProtocol.getApiFun();
        hashMap.put("orderId", String.valueOf(orderId));

        LogUtils.e("参数:"+hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("getOrderDescResponse:" + json);
                Gson gson = new Gson();
                GetOrderDescResponse getOrderDescResponse = gson.fromJson(json, GetOrderDescResponse.class);

                if (getOrderDescResponse.code .equals("0")) {
                    loadingDialog.dismiss();
                    tv_name.setText("收件人:"+getOrderDescResponse.data.userReceiveAddress.contacts);
                    tv_address.setText("地址"+getOrderDescResponse.data.userReceiveAddress.address);
                    tv_phone.setText(getOrderDescResponse.data.userReceiveAddress.contactsPhoneNumber);
                    tv_buyerMessage.setText(getOrderDescResponse.data.buyerMessage);
                    if(getOrderDescResponse.data.orderGoodsList!=null&&getOrderDescResponse.data.orderGoodsList.size()>0) {
                        orderGoodsAdapter = new OrderDescGoodsAdapter(OrderInfoActivity.this, getOrderDescResponse.data.orderGoodsList);
                        lv_goods.setAdapter(orderGoodsAdapter);
                    }
                    tv_order_no.setText("订单号:"+getOrderDescResponse.data.orderNo);
                    switch (getOrderDescResponse.data.payStatus){
                        case 0:{
                            tv_status.setText("订单状态: 待付款");
                            tv_payTime.setVisibility(View.GONE);
                            tv_sendTime.setVisibility(View.GONE);
                            tv_receiveTime.setVisibility(View.GONE);
                            break;
                        }
                        case 1:{
                            tv_status.setText("订单状态: 待发货");
                            tv_sendTime.setVisibility(View.GONE);
                            tv_receiveTime.setVisibility(View.GONE);
                            break;
                        }
                        case 2:{
                            tv_status.setText("订单状态: 待收货");
                            tv_receiveTime.setVisibility(View.GONE);
                            break;
                        }
                        case 3:{
                            tv_status.setText("订单状态: 已完成");
                            break;
                        }

                    }
                    tv_createTime.setText("创建时间:"+getOrderDescResponse.data.createTime);
                    tv_payTime.setText("付款时间:"+getOrderDescResponse.data.payTime);
                    tv_sendTime.setText("发货时间:"+getOrderDescResponse.data.sendTime);
                    tv_receiveTime.setText("收货时间:"+getOrderDescResponse.data.receiveTime);
                    tv_logistics.setText("快递公司："+getOrderDescResponse.data.logisticsInfo.logistics);
                    tv_logisticsNo.setText("快递单号："+getOrderDescResponse.data.logisticsInfo.logisticsNo);
                    if(!TextUtils.isEmpty(getOrderDescResponse.data.logisticsInfo.info)){
                        // TypeToken，它是gson提供的数据类型转换器，可以支持各种数据集合类型转换
//                        List<Object> infoList=StringToList(getOrderDescResponse.data.logisticsInfo.info);
                        LogisticsInfo.Info[] array = new Gson().fromJson(getOrderDescResponse.data.logisticsInfo.info,LogisticsInfo.Info[].class);
                        List<LogisticsInfo.Info> infoList = Arrays.asList(array);
                        LogUtils.e("infoList:"+infoList.size());
                        iogisticsInfoAdapter = new IogisticsInfoAdapter(OrderInfoActivity.this, infoList);
                        lv_wuliu.setAdapter(iogisticsInfoAdapter);
                    }
                    switch (status){
                        case 0:{
                            break;
                        }
                    }
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(OrderInfoActivity.this,
                            getOrderDescResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


    //确认订单
    private void finishOrderList() {
        loadingDialog.show();
        FinishOrderProtocol cancelOrderProtocol=new FinishOrderProtocol();
        String url  = cancelOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                AddAuthenticationInfoResponse getOrderListResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode() .equals("0")) {
                    UIUtils.showToastSafe(getOrderListResponse.msg);
                    finish();
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(OrderInfoActivity.this,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(OrderInfoActivity.this, getOrderListResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    //取消订单
    private void cancelOrderList() {
        loadingDialog.show();
        CancelOrderProtocol cancelOrderProtocol=new CancelOrderProtocol();
        String url  = cancelOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                AddAuthenticationInfoResponse getOrderListResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode() .equals("0")) {
                    UIUtils.showToastSafe(getOrderListResponse.msg);
                    finish();
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(OrderInfoActivity.this,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(OrderInfoActivity.this, getOrderListResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    //付款订单
    private void payOrderList() {
        loadingDialog.show();
        PayOrderProtocol payOrderProtocol=new PayOrderProtocol();
        String url  = payOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                CreateOrderResponse getOrderListResponse = gson.fromJson(json, CreateOrderResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.code .equals("0")) {
                    if(TextUtils.isEmpty(getOrderListResponse.data.appid)) {
                        UIUtils.showToastSafe(getOrderListResponse.msg);
                        finish();
                    }else{
                        finish();
                        PayReq req = new PayReq();
                        req.appId = getOrderListResponse.data.appid;
                        req.partnerId = getOrderListResponse.data.partnerid;
                        req.prepayId = getOrderListResponse.data.prepayid;
                        req.nonceStr = getOrderListResponse.data.noncestr;
                        req.timeStamp = getOrderListResponse.data.timestamp;
                        req.packageValue = "Sign=WXPay";
                        req.sign = getOrderListResponse.data.sign;
                        req.extData = "app data"; // optional
                        api.registerApp(Constants.APP_ID);
                        api.sendReq(req);
                    }
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(OrderInfoActivity.this,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(OrderInfoActivity.this, getOrderListResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}
