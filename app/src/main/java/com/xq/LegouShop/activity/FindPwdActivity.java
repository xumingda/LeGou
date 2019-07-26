package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.FindPwdProtocol;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class FindPwdActivity extends BaseActivity {

    private TextView tv_get_code;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_code;
    private EditText et_pwd;
    private EditText et_phone;
    private Button btn_submit;

    //    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back;
    private RelativeLayout rl_main;
    private TextView tv_title;
    private LinearLayout ll_pwd, ll_code;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_find_pwd, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate() {


        loadingDialog = DialogUtils.createLoadDialog(FindPwdActivity.this, false);
        ll_code = (LinearLayout) findViewById(R.id.ll_code);
        ll_pwd = (LinearLayout) findViewById(R.id.ll_pwd);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        et_phone = (EditText) findViewById(R.id.et_phone);
        view_back = (View) findViewById(R.id.view_back);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            tv_title.setText(getIntent().getStringExtra("title"));
        }
        et_phone.setText(SharedPrefrenceUtils.getString(this, "userphone"));
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = et_phone.getText().toString();
                code = et_code.getText().toString();
                new_password = et_pwd.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(new_password) && !TextUtils.isEmpty(code)) {
                    findPwd();
                } else {
                    DialogUtils.showAlertDialog(FindPwdActivity.this,
                            "请填写完成信息！");
                }

            }
        });
        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    runGetGode();
                } else {
                    DialogUtils.showAlertDialog(FindPwdActivity.this,
                            "手机号不能为空！");
                }

            }
        });
    }

        //获取验证码
    public void runGetGode() {
        loadingDialog.show();
        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        String url = getCodeProtocol.getApiFun();
        getCodeRequest.map.put("phoneNumber", phoneNumber);
        getCodeRequest.map.put("type", "3");
        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code == 0) {
                    tv_get_code.setClickable(false);
                    loadingDialog.dismiss();
                    Countdowmtimer(60000);
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(FindPwdActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(FindPwdActivity.this, error);
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

            }
        }.start();
    }
//
    public void findPwd() {
        loadingDialog.show();
        FindPwdProtocol findPwdProtocol = new FindPwdProtocol();

        String url = findPwdProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("pwd", MD5Utils.MD5(new_password));
        params.put("code", code);
        LogUtils.e("findPwdResponse:" + "请求:"+params.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                FindPwdResponse findPwdResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("findPwdResponse:" + findPwdResponse.toString());
                if (findPwdResponse.code.equals("0")) {
                    DialogUtils.showAlertDialog(FindPwdActivity.this,
                            "修改成功!");
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(FindPwdActivity.this,
                            findPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(FindPwdActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(FindPwdActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }


}
