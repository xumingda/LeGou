package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
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
import com.xq.LegouShop.bean.UserBean;
import com.xq.LegouShop.protocol.BalanceToBuyScoreProtocol;
import com.xq.LegouShop.protocol.BalanceToChangeScoreProtocol;
import com.xq.LegouShop.protocol.ChangeScoreToBalanceProtocol;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;

//充值
public class RechargeActivity extends BaseActivity  implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private String score;
    private TextView tv_title,tv_jifen,tv_line_title;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back;
    private String title;
    private Button btn_all_tixian,btn_comit;
    private EditText et_money;
    private UserBean userBean;
    private TextView tv_banlance;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_recharge, null);
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
        title=getIntent().getStringExtra("title");
        loadingDialog = DialogUtils.createLoadDialog(RechargeActivity.this, false);
        tv_line_title=(TextView) findViewById(R.id.tv_line_title);
        et_money=(EditText) findViewById(R.id.et_money);
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_jifen=(TextView) findViewById(R.id.tv_jifen);
        tv_banlance=(TextView) findViewById(R.id.tv_banlance);
        view_back=(View)findViewById(R.id.view_back);
        btn_all_tixian=(Button) findViewById(R.id.btn_all_tixian);
        btn_comit=(Button) findViewById(R.id.btn_comit);
//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);
        tv_title.setText(title);
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        tv_banlance.setText("我的余额:"+userBean.getBalanceMoney());
        tv_jifen.setText("我的购物积分："+userBean.getBuyScore());
        if(title.equals("提现")){
            tv_title.setText("提现");
            tv_line_title.setText("提现金额");
            btn_comit.setText("确认提现");
            tv_jifen.setVisibility(View.GONE);
            btn_all_tixian.setVisibility(View.VISIBLE);
        }else if (title.equals("充值")){
            tv_title.setText("充值");
            tv_line_title.setText("充值金额");
            tv_jifen.setVisibility(View.VISIBLE);
            btn_all_tixian.setVisibility(View.GONE);
            btn_comit.setText("确认充值");
        }else if(title.equals("转去购物积分")){
            tv_title.setText("转去购物积分");
            tv_line_title.setText("将余额转去购物积分");
            tv_jifen.setVisibility(View.VISIBLE);
            btn_all_tixian.setVisibility(View.GONE);
            btn_comit.setText("确认转换");
        }else{
            tv_title.setText("将转换积分转为余额");
            tv_line_title.setText("积分转换");
            tv_jifen.setVisibility(View.VISIBLE);
            btn_all_tixian.setVisibility(View.GONE);
            btn_comit.setText("确认转换");
            et_money.setHint("输入转换积分");
        }
        btn_comit.setOnClickListener(this);





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
    public void balanceToChangeScore() {
        loadingDialog.show();
        BalanceToChangeScoreProtocol balanceToChangeScoreProtocol = new BalanceToChangeScoreProtocol();

        String url = balanceToChangeScoreProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("score", score);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse modifyLoginPwdResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
                if (modifyLoginPwdResponse.code==0) {
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            "操作成功!");
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            modifyLoginPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RechargeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(RechargeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    //转去购物积分
    public void balanceToBuyScore() {
        loadingDialog.show();
        BalanceToBuyScoreProtocol balanceToChangeScoreProtocol = new BalanceToBuyScoreProtocol();

        String url = balanceToChangeScoreProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("score", score);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse modifyLoginPwdResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
                if (modifyLoginPwdResponse.code==0) {
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            "操作成功!");
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            modifyLoginPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RechargeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(RechargeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    //转积分去余额
    public void changeScoreToBalance() {
        loadingDialog.show();
        ChangeScoreToBalanceProtocol changeScoreToBalanceProtocol = new ChangeScoreToBalanceProtocol();

        String url = changeScoreToBalanceProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("score", score);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse modifyLoginPwdResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
                if (modifyLoginPwdResponse.code==0) {
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            "操作成功!");
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RechargeActivity.this,
                            modifyLoginPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RechargeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(RechargeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comit:{
                if(title.equals("提现")){

                }else if (title.equals("充值")){

                }else if(title.equals("转去购物积分")){
                    score=et_money.getText().toString();
                    balanceToBuyScore();
                }else{
                    score=et_money.getText().toString();
                    changeScoreToBalance();
                }
                break;
            }
        }
    }
}
