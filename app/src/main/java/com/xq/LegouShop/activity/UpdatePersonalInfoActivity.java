package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.UserBean;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.UpdateUserInfoProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;

//修改个人资料
public class UpdatePersonalInfoActivity extends BaseActivity  {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back,view_ok;
    private EditText et_nick,et_name,et_email;
    private RadioButton rb_man,rb_woman;
    private UserBean userBean;
    private String nickName,userName,sex,email;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_personal_info_update, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        userBean= SPUtils.getBeanFromSp(this,"userInfo","userInfo");
        loadingDialog = DialogUtils.createLoadDialog(UpdatePersonalInfoActivity.this, false);
        rb_man=(RadioButton) findViewById(R.id.rb_man);
        rb_woman=(RadioButton) findViewById(R.id.rb_woman);
        et_nick=(EditText) findViewById(R.id.et_nick);
        et_name=(EditText) findViewById(R.id.et_name);
        et_email=(EditText) findViewById(R.id.et_email);
        view_back=(View)findViewById(R.id.view_back);
        view_ok=(View)findViewById(R.id.view_ok);


        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        view_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName=et_nick.getText().toString();
                userName=et_name.getText().toString();
                email=et_email.getText().toString();
                if(rb_man.isChecked()){
                    sex="男";
                }else{
                    sex="女";
                }
                updateInfo();
            }
        });

        et_nick.setText(userBean.getNickName());
        et_name.setText(userBean.getUserName());
        et_email.setText(userBean.getEmail());
        if(userBean.getSex().equals("男")){
            rb_man.setChecked(true);
        }else{
            rb_woman.setChecked(true);
        }

    }

    //获取验证码
    public void updateInfo() {
        loadingDialog.show();
        UpdateUserInfoProtocol updateUserInfoProtocol = new UpdateUserInfoProtocol();
        HashMap<String,String> hashMap=new HashMap<>();
        String url = updateUserInfoProtocol.getApiFun();
        hashMap.put("nickName", nickName);
        hashMap.put("userName", userName);
        hashMap.put("sex", sex);
        hashMap.put("email", email);


        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code == 0) {
                    userBean.setNickName(nickName);
                    userBean.setUserName(userName);
                    userBean.setSex(sex);
                    userBean.setEmail(email);
                    SPUtils.saveBean2Sp(UpdatePersonalInfoActivity.this,userBean,"userInfo","userInfo");
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdatePersonalInfoActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePersonalInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }





}
