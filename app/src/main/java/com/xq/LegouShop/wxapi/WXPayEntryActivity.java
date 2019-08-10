package com.xq.LegouShop.wxapi;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.OrderManagerActivity;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler,View.OnClickListener{

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private boolean isSuccess=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String msg = "";

            if(resp.errCode == 0)
            {
                msg = "支付成功";
                isSuccess=true;
            }
            else if(resp.errCode == -2)
            {
                msg = "已取消支付";
            }
            else if(resp.errCode == -1)
            {
                msg = "支付失败";
            }
            UIUtils.showToastSafe(msg);
            DialogUtils.showAlertDialog(WXPayEntryActivity.this,msg,this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_roger:
                if(isSuccess) {
                    if(!SharedPrefrenceUtils.getBoolean(WXPayEntryActivity.this,"recharge")){
                        Intent intent=new Intent(this, OrderManagerActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                    }
                }
                finish();
                break;
        }
    }
}