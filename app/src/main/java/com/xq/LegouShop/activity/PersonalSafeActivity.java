package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.bean.UserBean;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.UIUtils;

//账户与安全
public class PersonalSafeActivity extends BaseActivity  implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back;
    private RelativeLayout ll_update_phone,ll_update_pwd,ll_out;
    private UserBean userBean;
    private TextView et_nick;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_personal_safe, null);
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
        loadingDialog = DialogUtils.createLoadDialog(PersonalSafeActivity.this, false);
        ll_update_phone=(RelativeLayout) findViewById(R.id.ll_update_phone);
        ll_update_pwd=(RelativeLayout) findViewById(R.id.ll_update_pwd);
        ll_out=(RelativeLayout) findViewById(R.id.ll_out);
        view_back=(View)findViewById(R.id.view_back);
        et_nick=(TextView) findViewById(R.id.et_nick);
//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);

        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        ll_update_phone.setOnClickListener(this);
        ll_update_pwd.setOnClickListener(this);
        ll_out.setOnClickListener(this);
        et_nick.setText(userBean.getUserName());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_out:{
                Intent intent=new Intent(this,DestroyUserActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.ll_update_pwd:{
                Intent intent=new Intent(this,UpdatePwdActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.ll_update_phone:{
                Intent intent=new Intent(this,UpdatePhoneActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
        }
    }

//    //获取验证码
//    public void runGetGode() {
//        loadingDialog.show();
//        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
//        GetCodeRequest getCodeRequest = new GetCodeRequest();
//        String url = getCodeProtocol.getApiFun();
//        getCodeRequest.map.put("phoneNumber", phoneNumber);
//        getCodeRequest.map.put("type", "3");
//        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
//                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
//                if (getCodeResponse.code == 0) {
//                    tv_get_code.setClickable(false);
//                    loadingDialog.dismiss();
//                    Countdowmtimer(60000);
//                } else {
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
//                            getCodeResponse.msg);
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
//
//            }
//        });
//    }
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
