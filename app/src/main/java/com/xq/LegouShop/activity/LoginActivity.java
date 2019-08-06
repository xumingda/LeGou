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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.IdentifyingCode;
import com.zhangke.websocket.WebSocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;




public class LoginActivity extends BaseActivity implements View.OnClickListener {

    // 微信登录
    private static IWXAPI WXapi;
    private String WX_APP_ID = "wx177036684b434de9";

    private ImageView identifyingCode;
    private String realCode;
    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    private RelativeLayout rl_clear, rl_clear_pwd;
    /**
     * 手机输入框
     */
    private EditText et_login_phone, et_pwd,et_code;
    private TextView tv_findpwd,tv_register;
    private Dialog loadingDialog;
    private int time = 60;
    private String userphone;
    private String pwd;
    private Button loginButton,loginWeixinButton;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    /**
     * 登录微信
     */
    private void WXLogin() {

        if ( WXapi== null) {
            WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        }
        if (!WXapi.isWXAppInstalled()) {
            Toast.makeText(this,"您手机尚未安装微信，请安装后再登录",Toast.LENGTH_LONG).show();
            return;
        }
        WXapi.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_xb_live_state";//官方说明：用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        WXapi.sendReq(req);

    }

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.login_layout, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(LoginActivity.this, false);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginWeixinButton = (Button) rootView.findViewById(R.id.loginWeixinButton);
        et_login_phone = (EditText) rootView.findViewById(R.id.et_login_phone);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        et_code= (EditText) rootView.findViewById(R.id.et_code);
        rl_clear = (RelativeLayout) rootView.findViewById(R.id.rl_clear);
        rl_clear_pwd = (RelativeLayout) rootView.findViewById(R.id.rl_clear_pwd);
        tv_findpwd = (TextView) rootView.findViewById(R.id.tv_findpwd);
        tv_register= (TextView) rootView.findViewById(R.id.tv_register);
        tv_findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPwdActivity.class);
                UIUtils.startActivityForResult(intent, 100);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                UIUtils.startActivityForResult(intent, 101);
            }
        });
        // 清除登录手机号码的图片监听事件
        rl_clear.setVisibility(View.INVISIBLE);
        rl_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_login_phone.setText("");
            }
        });
        rl_clear_pwd.setVisibility(View.INVISIBLE);
        rl_clear_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_pwd.setText("");
            }
        });

        // 登录手机号码编辑框清除事件
        et_login_phone.addTextChangedListener(new TextWatcher() {
            private CharSequence temp = null;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(temp)) {
                    rl_clear.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear.setVisibility(View.VISIBLE);
                }

            }
        });

        // 当手机号码编辑框输入完毕后，跳到登录密码编辑框时，手机号码编辑框右边的关闭图片消失；当输入手机号码，手机号码编辑框右边的关闭图片显示
        et_login_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_login_phone.getText().toString()))
                        rl_clear.setVisibility(View.VISIBLE);
                }

            }
        });


        et_pwd.addTextChangedListener(new TextWatcher() {
            private CharSequence temp = null;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(temp)) {
                    rl_clear_pwd.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear_pwd.setVisibility(View.VISIBLE);
                }

            }
        });

        // 当手机号码编辑框输入完毕后，跳到登录密码编辑框时，手机号码编辑框右边的关闭图片消失；当输入手机号码，手机号码编辑框右边的关闭图片显示
        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear_pwd.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_login_phone.getText().toString()))
                        rl_clear_pwd.setVisibility(View.VISIBLE);
                }

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userphone = et_login_phone.getText().toString();
                pwd = et_pwd.getText().toString();
                String code=et_code.getText().toString();
                if (!TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(code)
                ) {
                    if(code.equalsIgnoreCase(realCode)){
                        loginButton.setClickable(false);
                        runlogin();
                    }else{
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "验证码不一致！");
                    }

                } else {
                    if (TextUtils.isEmpty(userphone)) {
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "手机不能为空！");
                    } else if(TextUtils.isEmpty(pwd)){
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "密码不能为空！");
                    }else{
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "验证码不能为空！");
                    }
                }
            }
        });
        loginWeixinButton.setOnClickListener(this);
        identifyingCode=(ImageView)findViewById(R.id.identifyingcode_image);
        identifyingCode.setOnClickListener(this);
        identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
        realCode=IdentifyingCode.getInstance().getCode().toLowerCase();
        versionName = getAppVersionName(this);
        getVersion();
        String token=SharedPrefrenceUtils.getString(this,"token");
        LogUtils.e("token"+token);
        if(!TextUtils.isEmpty(token)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            UIUtils.startActivityNextAnim(intent);
            finish();
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

    public void runlogin() {
        loadingDialog.show();
        LoginProtocol loginProtocol = new LoginProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = loginProtocol.getApiFun();
        loginRequest.map.put("phoneNumber", userphone);
        loginRequest.map.put("pwd", MD5Utils.MD5(pwd));
        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                loginButton.setClickable(true);
                Gson gson = new Gson();
                LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
                LogUtils.e("loginresponse:" + loginresponse.toString());
                if (loginresponse.code.equals("0")) {

                    SharedPrefrenceUtils.setString(LoginActivity.this, "userphone", userphone);
                    SharedPrefrenceUtils.setString(LoginActivity.this, "token", loginresponse.getData().getAuthorization());
//                    LoginGame();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();

                } else {
                    DialogUtils.showAlertDialog(LoginActivity.this,
                            loginresponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                loginButton.setClickable(true);
                DialogUtils.showAlertDialog(LoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    private void LoginGame() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(this, "token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(jsonObject.toString());
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

    /**
     * 弹出对话框
     */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage("软件更新");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                downLoadApk();
                Uri uri = Uri.parse(downloadUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dialog.cancel();
            }
        });
        //当点取消按钮时不做任何举动
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    protected void downLoadApk() {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("正在下载更新");
        pd.show();

        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(downloadUrl, pd);
                    //安装APK
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    protected void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(LoginActivity.this, UIUtils.getPackageName() + ".provider_paths", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }


    //获取App版本
    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        return versionName;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.identifyingcode_image:{
                identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                realCode=IdentifyingCode.getInstance().getCode().toLowerCase();

                break;
            }
            case R.id.loginWeixinButton:{
                WXLogin();
                break;
            }
        }
    }
}
