package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.LogisticsInfo;
import com.xq.LegouShop.protocol.UpdatePwdProtocol;
import com.xq.LegouShop.response.UpdatePwdResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.SelectGoodsSpecificationuwindow;
import com.xq.LegouShop.weiget.SelectPopuwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//物流信息输入
public class InputLogisticsActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private EditText et_logisticsNo;
    private TextView tv_logistics_name;
    private Dialog loadingDialog;
    private String url;
    private RelativeLayout rl_main;
    private Button btn_submit;
    public static boolean isForeground = false;
    private View view_back;
    private SelectPopuwindow selectPopuwindow;
    private List<LogisticsInfo> dataList;
    private int userReturnGoodsId,userChangeGoodsId;
    //物流单号
    private String logisticsNo,logistics;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_input_logistics, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {
        userChangeGoodsId=getIntent().getIntExtra("userChangeGoodsId",0);
        userReturnGoodsId=getIntent().getIntExtra("userReturnGoodsId",0);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        loadingDialog = DialogUtils.createLoadDialog(InputLogisticsActivity.this, false);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        view_back=(View)findViewById(R.id.view_back);
        et_logisticsNo = (EditText) rootView.findViewById(R.id.et_logisticsNo);
        tv_logistics_name = (TextView) rootView.findViewById(R.id.tv_logistics_name);
        view_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_logistics_name.setOnClickListener(this);
        dataList=new ArrayList<>();
        LogisticsInfo logisticsInfo=new LogisticsInfo();
        logisticsInfo.logistics="shunfeng";
        logisticsInfo.logisticsNo="顺丰";
        dataList.add(logisticsInfo);

        LogisticsInfo logisticsInfo1=new LogisticsInfo();
        logisticsInfo1.logistics="shentong";
        logisticsInfo1.logisticsNo="申通";
        dataList.add(logisticsInfo1);

        LogisticsInfo logisticsInfo2=new LogisticsInfo();
        logisticsInfo2.logistics="yuantong";
        logisticsInfo2.logisticsNo="圆通";
        dataList.add(logisticsInfo2);

        LogisticsInfo logisticsInfo3=new LogisticsInfo();
        logisticsInfo3.logistics="zhongtong";
        logisticsInfo3.logisticsNo="中通";
        dataList.add(logisticsInfo3);

        LogisticsInfo logisticsInfo4=new LogisticsInfo();
        logisticsInfo4.logistics="huitongkuaidi";
        logisticsInfo4.logisticsNo="百世汇通";
        dataList.add(logisticsInfo4);

        LogisticsInfo logisticsInfo5=new LogisticsInfo();
        logisticsInfo5.logistics="baishiwuliu";
        logisticsInfo5.logisticsNo="百世物流";
        dataList.add(logisticsInfo5);

        LogisticsInfo logisticsInfo6=new LogisticsInfo();
        logisticsInfo6.logistics="yunda";
        logisticsInfo6.logisticsNo="韵达";
        dataList.add(logisticsInfo6);

        LogisticsInfo logisticsInfo7=new LogisticsInfo();
        logisticsInfo7.logistics="zhaijisong";
        logisticsInfo7.logisticsNo="宅急送";
        dataList.add(logisticsInfo7);

        LogisticsInfo logisticsInfo8=new LogisticsInfo();
        logisticsInfo8.logistics="tiantian";
        logisticsInfo8.logisticsNo="天天";
        dataList.add(logisticsInfo8);

        LogisticsInfo logisticsInfo9=new LogisticsInfo();
        logisticsInfo9.logistics="debangwuliu";
        logisticsInfo9.logisticsNo="德邦";
        dataList.add(logisticsInfo9);

        LogisticsInfo logisticsInfo10=new LogisticsInfo();
        logisticsInfo10.logistics="guotongkuaidi";
        logisticsInfo10.logisticsNo="国通";
        dataList.add(logisticsInfo10);

        LogisticsInfo logisticsInfo11=new LogisticsInfo();
        logisticsInfo11.logistics="zengyisudi";
        logisticsInfo11.logisticsNo="增益";
        dataList.add(logisticsInfo11);

        LogisticsInfo logisticsInfo12=new LogisticsInfo();
        logisticsInfo12.logistics="suer";
        logisticsInfo12.logisticsNo="速尔";
        dataList.add(logisticsInfo12);

        LogisticsInfo logisticsInfo13=new LogisticsInfo();
        logisticsInfo13.logistics="ztky";
        logisticsInfo13.logisticsNo="中铁物流";
        dataList.add(logisticsInfo13);

        LogisticsInfo logisticsInfo14=new LogisticsInfo();
        logisticsInfo14.logistics="zhongtiewuliu";
        logisticsInfo14.logisticsNo="中铁快运";
        dataList.add(logisticsInfo14);

        LogisticsInfo logisticsInfo15=new LogisticsInfo();
        logisticsInfo15.logistics="ganzhongnengda";
        logisticsInfo15.logisticsNo="能达";
        dataList.add(logisticsInfo15);

        LogisticsInfo logisticsInfo16=new LogisticsInfo();
        logisticsInfo16.logistics="youshuwuliu";
        logisticsInfo16.logisticsNo="优速";
        dataList.add(logisticsInfo16);

        LogisticsInfo logisticsInfo17=new LogisticsInfo();
        logisticsInfo17.logistics="quanfengkuaidi";
        logisticsInfo17.logisticsNo="全峰";
        dataList.add(logisticsInfo17);

        LogisticsInfo logisticsInfo18=new LogisticsInfo();
        logisticsInfo18.logistics="jd";
        logisticsInfo18.logisticsNo="京东";
        dataList.add(logisticsInfo18);

        LogisticsInfo logisticsInfo19=new LogisticsInfo();
        logisticsInfo19.logistics="youzhengguonei";
        logisticsInfo19.logisticsNo="邮政包裹";
        dataList.add(logisticsInfo19);

        LogisticsInfo logisticsInfo20=new LogisticsInfo();
        logisticsInfo20.logistics="youzhengguoji";
        logisticsInfo20.logisticsNo="国际包裹";
        dataList.add(logisticsInfo20);

        LogisticsInfo logisticsInfo21=new LogisticsInfo();
        logisticsInfo21.logistics="ems";
        logisticsInfo21.logisticsNo="EMS";
        dataList.add(logisticsInfo21);
//        dataList.add("EMS-国际件");
//        dataList.add("北京EMS");


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

    public void deliverReturnGoods() {
        loadingDialog.show();
        HashMap<String,String> hashMap = new HashMap<>();
        url = "/user/deliverReturnGoods";
        hashMap.put("logisticsNo", logisticsNo);
        if(userReturnGoodsId>0) {
            url = "/user/deliverReturnGoods";
            hashMap.put("userReturnGoodsId", String.valueOf(userReturnGoodsId));
        }else {
            url = "/user/deliverChangeGoods";
            hashMap.put("userChangeGoodsId", String.valueOf(userChangeGoodsId));
        }

        hashMap.put("logistics",logistics);
        LogUtils.e("请求："+hashMap.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                UpdatePwdResponse updatePhoneResponse = gson.fromJson(json, UpdatePwdResponse.class);
                LogUtils.e("updatePhoneResponse:" + updatePhoneResponse.toString());
                if (updatePhoneResponse.code.equals("0")) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    DialogUtils.showAlertDialog(InputLogisticsActivity.this,
                            updatePhoneResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(InputLogisticsActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:{
                logisticsNo=et_logisticsNo.getText().toString();
                deliverReturnGoods();
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            } case R.id.tv_logistics_name:{
                List<String> list=new ArrayList<>();
                for(int i=0;i<dataList.size();i++){
                    list.add(dataList.get(i).logisticsNo);
                }
                selectPopuwindow = new SelectPopuwindow(  InputLogisticsActivity.this,"物流公司名称",list,itemsOnClick);
                selectPopuwindow.showPopupWindow(rl_main);
                break;
            }
        }
    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = dataList.get(position).logisticsNo;
            logistics=dataList.get(position).logistics;
            tv_logistics_name.setText(value);
            selectPopuwindow.dismissPopupWindow();
        }
    };
}
