package com.xq.LegouShop.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.protocol.UpdatePhoneProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.response.UpdatePhoneResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class UpdatePhoneActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private EditText et_login_phone, et_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String newPhoneNumber;
    private String code;
    private Button btn_submit;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
    private TextView tv_get_code;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_phone, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(UpdatePhoneActivity.this, false);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        view_back=(View)findViewById(R.id.view_back);
        et_login_phone = (EditText) rootView.findViewById(R.id.et_phone);
        et_code = (EditText) rootView.findViewById(R.id.et_code);
        tv_get_code=(TextView)rootView.findViewById(R.id.tv_get_code);
        view_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);
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
    //获取验证码
    public void runGetGode() {
        loadingDialog.show();
        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        String url = getCodeProtocol.getApiFun();
        getCodeRequest.map.put("phoneNumber", newPhoneNumber);
        getCodeRequest.map.put("type", "2");
        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code == 0) {

                    loadingDialog.dismiss();
//                    auth_code_id = getCodeResponse.auth_code_id;
                    Countdowmtimer(60000);
                } else {
                    tv_get_code.setClickable(true);
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdatePhoneActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                tv_get_code.setClickable(true);
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePhoneActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    /**
     * 计时器
     */
    public void Countdowmtimer(long dodate) {
        new CountDownTimer(dodate, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time - 1;
                tv_get_code.setText(time + "s后重发");
            }

            @Override
            // 计时结束
            public void onFinish() {
                time = 60;
                tv_get_code.setText("获取验证码");
                tv_get_code.setClickable(true);
//                SpannableStringBuilder style=new SpannableStringBuilder(tv_time.getText());
//                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink_text)), 8, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tv_time.setText(style);
            }
        }.start();
    }
    public void updatePhone() {
        loadingDialog.show();
        UpdatePhoneProtocol updatePhoneProtocol = new UpdatePhoneProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        String url = updatePhoneProtocol.getApiFun();
        hashMap.put("newPhoneNumber", newPhoneNumber);
        hashMap.put("code", code);
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                UpdatePhoneResponse updatePhoneResponse = gson.fromJson(json, UpdatePhoneResponse.class);
                LogUtils.e("updatePhoneResponse:" + updatePhoneResponse.toString());
                if (updatePhoneResponse.code.equals("0")) {
                    DialogUtils.showAlertDialog(UpdatePhoneActivity.this,
                           "修改成功!");

                } else {
                    DialogUtils.showAlertDialog(UpdatePhoneActivity.this,
                            updatePhoneResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePhoneActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }









    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_submit:{
                newPhoneNumber=et_login_phone.getText().toString();
                code=et_code.getText().toString();
                if(!TextUtils.isEmpty(newPhoneNumber)&&!TextUtils.isEmpty(code)){
                    updatePhone();
                }else{
                    DialogUtils.showAlertDialog(UpdatePhoneActivity.this,
                            "请先填写完成的信息!");
                }
                break;
            }
            case R.id.tv_get_code:{
                newPhoneNumber=et_login_phone.getText().toString();
                runGetGode();
                break;
            }
        }
    }
}
