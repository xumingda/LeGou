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
    private TextView tv_order_no;
    private TextView tv_name;
    private TextView tv_num;
    private TextView tv_price,tv_title;
    private ImageView iv_pic,iv_shop_in_pic,iv_shop_in_pic_two,iv_shop_in_pic_three,iv_shop_in_pic_four,iv_shop_in_pic_five,iv_shop_in_pic_six;
    private  String orderNo,time,pic,goodName,bugCount,orderMoney,shopName;

    private int type;

    private TextView tv_why_title;
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
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        orderNo=intent.getStringExtra("orderNo");
        shopName=intent.getStringExtra("shopName");
        time=intent.getStringExtra("time");
        pic=intent.getStringExtra("pic");
        goodName=intent.getStringExtra("goodName");
        bugCount=intent.getStringExtra("bugCount");
        orderMoney=intent.getStringExtra("orderMoney");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(DrawbackInfoActivity.this, false);

        view_back=(View)findViewById(R.id.view_back);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_why_title= (TextView) findViewById(R.id.tv_why_title);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_order_no= (TextView) findViewById(R.id.tv_order_no);
        tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_price= (TextView)findViewById(R.id.tv_price);
        iv_pic= (ImageView) findViewById(R.id.iv_pic);
        iv_shop_in_pic= (ImageView) findViewById(R.id.iv_shop_in_pic);
        iv_shop_in_pic_two= (ImageView) findViewById(R.id.iv_shop_in_pic_two);
        iv_shop_in_pic_three= (ImageView) findViewById(R.id.iv_shop_in_pic_three);
        iv_shop_in_pic_four= (ImageView) findViewById(R.id.iv_shop_in_pic_four);
        iv_shop_in_pic_five= (ImageView) findViewById(R.id.iv_shop_in_pic_five);
        iv_shop_in_pic_six= (ImageView) findViewById(R.id.iv_shop_in_pic_six);
        view_back.setOnClickListener(this);
        iv_shop_in_pic.setOnClickListener(this);
        iv_shop_in_pic_two.setOnClickListener(this);
        iv_shop_in_pic_three.setOnClickListener(this);
        iv_shop_in_pic_four.setOnClickListener(this);
        iv_shop_in_pic_five.setOnClickListener(this);
        iv_shop_in_pic_six.setOnClickListener(this);
        if(type==1){
            tv_why_title.setText("退款原因");
            tv_title.setText("退款申请");
        }else if(type==2){
            tv_why_title.setText("退货原因");
            tv_title.setText("退货申请");
        }else{
            tv_why_title.setText("换货原因");
            tv_title.setText("换货申请");
        }
        if(!TextUtils.isEmpty(shopName)) {
            tv_shopName.setText(shopName);
        }else{
            tv_shopName.setText("平台自营");
        }
        tv_order_no.setText("订单号:"+orderNo);

        tv_time.setText(time);
        tv_name.setText(goodName);
        tv_price.setText("¥"+orderMoney);
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

}
