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

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.RegisterProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.request.RegisterRequest;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.RegisterResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.IdentifyingCode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private EditText et_login_phone, et_code,et_pwd,et_again_pwd;
    private Dialog loadingDialog;
    private int time = 60;
    private String userphone,code;
    private String pwd,again_pwd;
    private Button btn_submit;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
    private TextView tv_get_code;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_register, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(RegisterActivity.this, false);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        view_back=(View)findViewById(R.id.view_back);
        et_login_phone = (EditText) rootView.findViewById(R.id.et_phone);
        et_code = (EditText) rootView.findViewById(R.id.et_code);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        et_again_pwd = (EditText) rootView.findViewById(R.id.et_again_pwd);
        tv_get_code= (TextView) rootView.findViewById(R.id.tv_get_code);
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
        getCodeRequest.map.put("phoneNumber", userphone);
        getCodeRequest.map.put("type", "1");
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
                    DialogUtils.showAlertDialog(RegisterActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                tv_get_code.setClickable(true);
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RegisterActivity.this, error);
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
    public void runRegister() {
        loadingDialog.show();
        RegisterProtocol registerProtocol = new RegisterProtocol();
        RegisterRequest registerRequest = new RegisterRequest();
        String url = registerProtocol.getApiFun();
        registerRequest.map.put("phoneNumber", userphone);
        registerRequest.map.put("code", code);
        registerRequest.map.put("pwd", MD5Utils.MD5(pwd));
        MyVolley.uploadNoFile(MyVolley.POST, url, registerRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                btn_submit.setClickable(true);
                Gson gson = new Gson();
                RegisterResponse registerResponse = gson.fromJson(json, RegisterResponse.class);
                LogUtils.e("registerResponse:" + registerResponse.toString());
                if (registerResponse.code.equals("0")) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    DialogUtils.showAlertDialog(RegisterActivity.this,
                            registerResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                btn_submit.setClickable(true);
                DialogUtils.showAlertDialog(RegisterActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
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
            Uri apkUri = FileProvider.getUriForFile(RegisterActivity.this, UIUtils.getPackageName() + ".provider_paths", file);
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
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_submit:{
                userphone=et_login_phone.getText().toString();
                code=et_code.getText().toString();
                pwd=et_pwd.getText().toString();
                again_pwd=et_again_pwd.getText().toString();
                if(!TextUtils.isEmpty(userphone)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(again_pwd)){
                    if(pwd.equals(again_pwd)){
                        runRegister();
                    }else{
                        DialogUtils.showAlertDialog(RegisterActivity.this,
                           "请确认密码一致！");
                    }
                }else{
                    DialogUtils.showAlertDialog(RegisterActivity.this,
                            "请输入完整的信息！");
                }
                break;
            }
            case R.id.tv_get_code:{
                userphone=et_login_phone.getText().toString();
                if(!TextUtils.isEmpty(userphone)){
                    tv_get_code.setClickable(false);
                    runGetGode();
                }
                break;
            }
        }
    }
}
