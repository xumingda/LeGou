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

import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.UIUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//购物积分
public class ShoppingPointsActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private Dialog loadingDialog;

    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_shop_points, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(ShoppingPointsActivity.this, false);
        view_back=(View)findViewById(R.id.view_back);

        view_back.setOnClickListener(this);

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
    //获取验证码
//    public void runGetGode() {
//        loadingDialog.show();
//        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
//        GetCodeRequest getCodeRequest = new GetCodeRequest();
//        String url = getCodeProtocol.getApiFun();
//        getCodeRequest.map.put("phoneNumber", userphone);
//        getCodeRequest.map.put("type", "1");
//        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
//                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
//                if (getCodeResponse.code == 0) {
//
//                    loadingDialog.dismiss();
////                    auth_code_id = getCodeResponse.auth_code_id;
//                    Countdowmtimer(60000);
//                } else {
//                    tv_get_code.setClickable(true);
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(LoginActivity.this,
//                            getCodeResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                tv_get_code.setClickable(true);
//                loadingDialog.dismiss();
//                DialogUtils.showAlertDialog(LoginActivity.this, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//        });
//    }

    /**
     * 计时器
     */
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
////                SpannableStringBuilder style=new SpannableStringBuilder(tv_time.getText());
////                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink_text)), 8, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
////                tv_time.setText(style);
//            }
//        }.start();
//    }
    public void runlogin() {
//        loadingDialog.show();
//        LoginProtocol loginProtocol = new LoginProtocol();
//        LoginRequest loginRequest = new LoginRequest();
//        String url = loginProtocol.getApiFun();
//        loginRequest.map.put("phoneNumber", userphone);
//        loginRequest.map.put("pwd", MD5Utils.MD5(pwd));
//        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//                loadingDialog.dismiss();
//                loginButton.setClickable(true);
//                Gson gson = new Gson();
//                LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
//                LogUtils.e("loginresponse:" + loginresponse.toString());
//                if (loginresponse.code.equals("0")) {
//
//                    SharedPrefrenceUtils.setString(LoginActivity.this, "userphone", userphone);
//                    SharedPrefrenceUtils.setString(LoginActivity.this, "token", loginresponse.getAuthorization());
//                    if (loginresponse.salesmanObject != null) {
//                        setTagAndAlias("salesman" + loginresponse.salesmanObject.getId());
//                        SharedPrefrenceUtils.setString(LoginActivity.this, "alias", "salesman" + loginresponse.salesmanObject.getId());
//                        SPUtils.saveBean2Sp(LoginActivity.this, loginresponse.salesmanObject, "salesmanObject", "SalesmanBean");
//                        Intent intent = new Intent(LoginActivity.this, MyselfActivity.class);
//                        UIUtils.startActivityNextAnim(intent);
//                        finish();
//                    } else if (loginresponse.businessObject != null) {
//                        setTagAndAlias("business" + loginresponse.businessObject.getId());
//                        SharedPrefrenceUtils.setInt(LoginActivity.this, "master", loginresponse.getBusinessLoginObject().getMaster());
//                        SharedPrefrenceUtils.setString(LoginActivity.this, "alias", "business" + loginresponse.businessObject.getId());
//                        LogUtils.e("alias设置:" + SharedPrefrenceUtils.getString(LoginActivity.this, "alias"));
//                        SPUtils.saveBean2Sp(LoginActivity.this, loginresponse.businessObject, "businessObject", "MerchantBean");
//                        Intent intent = new Intent(LoginActivity.this, MerchantMyselfActivity.class);
//                        UIUtils.startActivityNextAnim(intent);
//                        finish();
//                    }
//
//
//                } else {
//                    DialogUtils.showAlertDialog(LoginActivity.this,
//                            loginresponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                loginButton.setClickable(true);
//                DialogUtils.showAlertDialog(LoginActivity.this, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//
//
//        });
    }



    //获取版本信息
    public void getVersion() {
//        GetVersionProtocol getVersionProtocol = new GetVersionProtocol();
//        LoginRequest loginRequest = new LoginRequest();
//        String url = getVersionProtocol.getApiFun();
//        loginRequest.map.put("platform", "android");
//        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//                Gson gson = new Gson();
//                GetVersionResponse getVersionResponse = gson.fromJson(json, GetVersionResponse.class);
//                LogUtils.e("getVersionResponse:" + getVersionResponse.toString());
//                if (getVersionResponse.code.equals("0")) {
//                    try {
//                        double newVersionName = Double.parseDouble(getVersionResponse.getData().getVersion());
//                        if (newVersionName > Double.parseDouble(versionName)) {
//                            downloadUrl = getVersionResponse.getData().getUrl();
//                            showUpdataDialog();
//                        }else{
//                            String token = SharedPrefrenceUtils.getString(LoginActivity.this, "token");
//                            if (!TextUtils.isEmpty(token)) {
//
//                                SalesmanBean salesmanBean = SPUtils.getBeanFromSp(LoginActivity.this, "salesmanObject", "SalesmanBean");
//                                MerchantBean merchantBean = SPUtils.getBeanFromSp(LoginActivity.this, "businessObject", "MerchantBean");
//                                if (salesmanBean != null) {
//                                    setTagAndAlias("salesman" + salesmanBean.getId());
//                                    Intent intent = new Intent(LoginActivity.this, MyselfActivity.class);
//                                    UIUtils.startActivityNextAnim(intent);
//                                    finish();
//                                } else if (merchantBean != null) {
//                                    setTagAndAlias("business" + merchantBean.getId());
//                                    Intent intent = new Intent(LoginActivity.this, MerchantMyselfActivity.class);
//                                    UIUtils.startActivityNextAnim(intent);
//                                    finish();
//                                }
//
//                            }
//                        }
//                    } catch (Exception e) {
//
//                    }
//
//
//                } else {
//                    DialogUtils.showAlertDialog(LoginActivity.this,
//                            getVersionResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//
//                DialogUtils.showAlertDialog(LoginActivity.this, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//
//
//        });
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
