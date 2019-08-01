package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.AddressAdapter;
import com.xq.LegouShop.adapter.OrderGoodsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.protocol.CreateOrderProtocol;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.GetUserReceiveAddressListProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.GetUserReceiveAddressListResponse;
import com.xq.LegouShop.tabpager.DetailsPager;
import com.xq.LegouShop.tabpager.EvaluatePager;
import com.xq.LegouShop.tabpager.SpecificationPager;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.SelectGoodsSpecificationuwindow;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//立即购买
public class BuyGoodActivity extends BaseActivity  implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String userReceiveAddressId,buyerMessage;
    private int buyScorePay;//	是否使用购物积分支付 0否1是，不传表示否
    private int changeScorePay;//	是否使用转换积分支付 0否1是，不传表示否
    private int  balancePay;//	是否使用余额支付 0否1是，不传表示否
    private int weixinPay	;//是否使用微信支付 0否1是，不传表示否
    private int zhifubaoPay	;//是否使用支付宝支付 0否1是，不传表示否
    private EditText et_buyerMessage;
    private View view_back,view_update;
    private ImageLoader imageLoader;
    private double total_price;
    private List<CartBean> cartBeanList;
    private RelativeLayout rl_main;
    private TextView tv_total_price;
    private ListView lv_goods;
    private OrderGoodsAdapter orderGoodsAdapter;
    private TextView tv_add_address,tv_name,tv_phone,tv_address,tv_commit;
    private View view_select;
    private RadioButton rb_jifen,rb_zhuanhua,rb_balance,rb_weixin,rb_zhifubao;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_good_buy, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        cartBeanList = (List<CartBean>) getIntent().getSerializableExtra("orderGoodBeanList");
        total_price = getIntent().getDoubleExtra("total_price", 0);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(BuyGoodActivity.this, false);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        lv_goods=(ListView)findViewById(R.id.lv_goods);
        et_buyerMessage=(EditText)findViewById(R.id.et_buyerMessage);
        view_select=(View)findViewById(R.id.view_select);
        view_back=(View)findViewById(R.id.view_back);
        tv_total_price=(TextView) findViewById(R.id.tv_total_price);
        tv_add_address=(TextView) findViewById(R.id.tv_add_address);
        tv_commit=(TextView) findViewById(R.id.tv_commit);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_address=(TextView)findViewById(R.id.tv_address);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_total_price.setText("总计：￥" + total_price);
        rb_jifen=(RadioButton)findViewById(R.id.rb_jifen);
        rb_zhuanhua=(RadioButton)findViewById(R.id.rb_zhuanhua);
        rb_balance=(RadioButton)findViewById(R.id.rb_balance);
        rb_weixin=(RadioButton)findViewById(R.id.rb_weixin);
        rb_zhifubao=(RadioButton)findViewById(R.id.rb_zhifubao);
//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);

        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        view_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyGoodActivity.this, AddressActivity.class);
                UIUtils.startActivityNextAnim(intent);
            }
        });
        tv_commit.setOnClickListener(this);
        if(cartBeanList!=null&&cartBeanList.size()>0) {
            orderGoodsAdapter = new OrderGoodsAdapter(this, cartBeanList);
            lv_goods.setAdapter(orderGoodsAdapter);
        }
        getAddressLists();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:{
                buyerMessage=et_buyerMessage.getText().toString();
                if(rb_balance.isChecked()){
                    balancePay=1;
                }else{
                    balancePay=0;
                }
                if(rb_jifen.isChecked()){
                    buyScorePay=1;
                }else{
                    buyScorePay=0;
                }
                if(rb_zhuanhua.isChecked()){
                    changeScorePay=1;
                }else{
                    changeScorePay=0;
                }
                if(rb_weixin.isChecked()){
                    weixinPay=1;
                }else{
                    weixinPay=0;
                }
                if(rb_zhifubao.isChecked()){
                    zhifubaoPay=1;
                }else{
                    zhifubaoPay=0;
                }
                createOrder();
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
        if(requestCode==100){
            getAddressLists();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getAddressLists() {
        loadingDialog.show();
        GetUserReceiveAddressListProtocol getUserReceiveAddressListProtocol = new GetUserReceiveAddressListProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = getUserReceiveAddressListProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                GetUserReceiveAddressListResponse getUserReceiveAddressListResponse = gson.fromJson(json, GetUserReceiveAddressListResponse.class);
                LogUtils.e("getUserReceiveAddressListResponse:" + getUserReceiveAddressListResponse.toString());
                if (getUserReceiveAddressListResponse.code.equals("0")) {
                    if(getUserReceiveAddressListResponse.dataList.size()==0){
                        tv_add_address.setVisibility(View.VISIBLE);
                        tv_add_address.setOnClickListener(BuyGoodActivity.this);
                    }else{
                        tv_add_address.setVisibility(View.GONE);
                        for(int i=0;i<getUserReceiveAddressListResponse.dataList.size();i++){
                            if(getUserReceiveAddressListResponse.dataList.get(i).isDefault==1){
                                tv_name.setText("收货人:"+getUserReceiveAddressListResponse.dataList.get(i).getContacts());
                                tv_phone.setText(getUserReceiveAddressListResponse.dataList.get(i).getContactsPhoneNumber());
                                tv_address.setText("地址:"+getUserReceiveAddressListResponse.dataList.get(i).getAddress());
                                userReceiveAddressId=getUserReceiveAddressListResponse.dataList.get(i).getId();
                            }
                        }
                        if(tv_name.getText().toString().length()==0){
                            tv_name.setText("收货人:"+getUserReceiveAddressListResponse.dataList.get(0).getContacts());
                            tv_phone.setText(getUserReceiveAddressListResponse.dataList.get(0).getContactsPhoneNumber());
                            tv_address.setText("地址:"+getUserReceiveAddressListResponse.dataList.get(0).getAddress());
                            userReceiveAddressId=getUserReceiveAddressListResponse.dataList.get(0).getId();
                        }
                    }

                } else {
                    DialogUtils.showAlertDialog(BuyGoodActivity.this,
                            getUserReceiveAddressListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(BuyGoodActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }




    //获取验证码
    public void createOrder() {
        loadingDialog.show();
        CreateOrderProtocol createOrderProtocol = new CreateOrderProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        String url = createOrderProtocol.getApiFun();
        hashMap.put("userReceiveAddressId", userReceiveAddressId);//收货地址id
        hashMap.put("buyScorePay", String.valueOf(buyScorePay));// 0否1是，不传表示否
        hashMap.put("changeScorePay", String.valueOf(changeScorePay));// 是否使用转换积分支付 0否1是，不传表示否
        hashMap.put("balancePay", String.valueOf(balancePay));// 	是否使用余额支付 0否1是，不传表示否
        hashMap.put("weixinPay", String.valueOf(weixinPay));// 是否使用微信支付 0否1是，不传表示否
        hashMap.put("zhifubaoPay", String.valueOf(zhifubaoPay));// 	是否使用支付宝支付 0否1是，不传表示否
        hashMap.put("buyerMessage", buyerMessage);//买家留言

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cartBeanList.size(); i++) {
            JSONObject json = new JSONObject();
            try {
                json.put("goodsId", cartBeanList.get(i).getGoodsId());
                json.put("goodsCaseId", cartBeanList.get(i).getGoodsCaseId());
                json.put("buyCount", cartBeanList.get(i).getBuyCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(json);
        }
        hashMap.put("orderGoodsList", jsonArray.toString());//商品json数组字符串 [{}]

        LogUtils.e("参数:"+hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("appSendMsgResponse:" + json.toString());
                Gson gson = new Gson();
                CreateOrderResponse getCodeResponse = gson.fromJson(json, CreateOrderResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code .equals("0")) {
                    loadingDialog.dismiss();
                    UIUtils.showToastSafe(getCodeResponse.msg);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);

                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(BuyGoodActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(BuyGoodActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
//
//    /**
//     * 计时器
//     */
//    public void Countdowmtimer(long dodate) {
//        new CountDownTimer(dodate, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                time = time - 1;
//                tv_get_code.setText(time + "s后重发");
//            }
//
//            @Override
//            // 计时结束
//            public void onFinish() {
//                time = 60;
//                tv_get_code.setText("获取验证码");
//                tv_get_code.setClickable(true);
//
//            }
//        }.start();
//    }
//
//    public void runUpdatePwd() {
//        loadingDialog.show();
//        ModifyLoginPwdProtocol modifyLoginPwdProtocol = new ModifyLoginPwdProtocol();
//
//        String url = modifyLoginPwdProtocol.getApiFun();
//        HashMap<String, String> params = new HashMap<String, String>();
//
//        params.put("phoneNumber", phoneNumber);
////        if(salesmanBean!=null){
////            params.put("type", "0");
////            params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
////        }else{
////            params.put("type", "1");
////            params.put("businessId",  String.valueOf(merchantBean.getId()));
////        }
//
//        params.put("password",  MD5Utils.MD5(new_password));
//        params.put("code", code);
//        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                ModifyLoginPwdResponse modifyLoginPwdResponse = gson.fromJson(json, ModifyLoginPwdResponse.class);
//                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
//                if (modifyLoginPwdResponse.code.equals("0")) {
//                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UpdatePwdActivity.this,UpdatePwdActivity.this,"修改成功");
//                    clockPopuwindow.showPopupWindow(rl_main);
//                    loadingDialog.dismiss();
//
//                } else {
//
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
//                            modifyLoginPwdResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                DialogUtils.showAlertDialog(UpdatePwdActivity.this, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//                loadingDialog.dismiss();
//                DialogUtils.showAlertToLoginDialog(UpdatePwdActivity.this,
//                        "登录超时，请重新登录！");
//            }
//        });
//    }




}
