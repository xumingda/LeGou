package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.AccountStatementAdapter;
import com.xq.LegouShop.adapter.CollectionAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.datepicker.CustomDatePicker;
import com.xq.LegouShop.datepicker.DateFormatUtils;
import com.xq.LegouShop.util.DateUtils;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.SelectPopuwindow;

import java.util.ArrayList;
import java.util.List;

//账户流水
public class AccountStatementActivity extends BaseActivity implements View.OnClickListener {

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
    private ListView lv_account_statement;
    private AccountStatementAdapter accountStatementAdapter;
    private List<OrderBean> orderBeanList;
    private SelectPopuwindow selectShopPopuwindow;
    private  List<String> shopList;
    private TextView tv_select,tv_select_time;
    private RelativeLayout rl_main;
    private String startTime;
    private CustomDatePicker mTimerPicker;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_account_statement, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = shopList.get(position);
            tv_select.setText(value);
            selectShopPopuwindow.dismissPopupWindow();
        }
    };
    public void initDate() {
        shopList=new ArrayList<>();
        shopList.add("充值");
        shopList.add("提现");
        shopList.add("购物");
        shopList.add("退款");
        shopList.add("积分转换");
        orderBeanList = new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(AccountStatementActivity.this, false);
        tv_select=(TextView) findViewById(R.id.tv_select);
        tv_select_time=(TextView) findViewById(R.id.tv_select_time);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        lv_account_statement=(ListView) findViewById(R.id.lv_account_statement);
        view_back = (View) findViewById(R.id.view_back);

//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);

        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });



        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        if (accountStatementAdapter == null) {
            accountStatementAdapter = new AccountStatementAdapter(this, orderBeanList);
            lv_account_statement.setAdapter(accountStatementAdapter);
        } else {
            accountStatementAdapter.setDate(orderBeanList);
            accountStatementAdapter.notifyDataSetChanged();
        }
        tv_select_time.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        initTimerPicker();
    }
    private void initTimerPicker() {
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2028-10-17 18:00";

        tv_select_time.setText(beginTime.substring(0,10));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_select_time.setText(DateUtils.milliToSimpleDateYear(timestamp));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_time:{
                mTimerPicker.show(tv_select_time.getText().toString());
                break;
            }
            case R.id.tv_select: {
                selectShopPopuwindow = new SelectPopuwindow(UIUtils.getActivity(),"筛选条件",shopList,itemsOnClick);
                selectShopPopuwindow.showPopupWindow(rl_main);
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
