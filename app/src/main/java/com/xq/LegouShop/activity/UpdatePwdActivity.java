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
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.protocol.UpdatePhoneProtocol;
import com.xq.LegouShop.protocol.UpdatePwdProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.response.UpdatePhoneResponse;
import com.xq.LegouShop.response.UpdatePwdResponse;
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


public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private EditText et_again_pwd, et_old_pwd,et_new_pwd;
    private Dialog loadingDialog;
    private int time = 60;
    private String newPwd;
    private String oldPwd;
    private Button btn_submit;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_pwd, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(UpdatePwdActivity.this, false);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        view_back=(View)findViewById(R.id.view_back);
        et_again_pwd = (EditText) rootView.findViewById(R.id.et_again_pwd);
        et_new_pwd = (EditText) rootView.findViewById(R.id.et_new_pwd);
        et_old_pwd = (EditText) rootView.findViewById(R.id.et_old_pwd);
        view_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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
                    SharedPrefrenceUtils.setString(UpdatePwdActivity.this, "userphone", "");
                    SharedPrefrenceUtils.setString(UpdatePwdActivity.this, "token", "");
                    Intent intent=new Intent(UpdatePwdActivity.this,LoginActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                } else {
                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                            updatePhoneResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePwdActivity.this, error);
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
            } case R.id.btn_submit:{
                String again_pwd=et_again_pwd.getText().toString();
                newPwd=et_new_pwd.getText().toString();
                oldPwd=et_old_pwd.getText().toString();
                if(!TextUtils.isEmpty(newPwd)&&!TextUtils.isEmpty(oldPwd)&&!TextUtils.isEmpty(again_pwd)){
                    if(newPwd.equals(again_pwd)){
                        updatePwd();
                    }else{
                        DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                "密码输入不一致!");
                    }
                }else{
                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                            "请先填写完成的信息!");
                }
                break;
            }
        }
    }
}
