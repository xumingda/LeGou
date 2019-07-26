package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.AddressBean;
import com.xq.LegouShop.protocol.AddOrUpdateReceiveAddressProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;

//添加地址
public class AddAddressActivity extends BaseActivity  implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    //联系人
    private String contacts;
    private String contactsPhoneNumber;
    private String provinceId;
    private String cityId;
    private String areaId;
    private String address;
    private int isDefault;
    private TextView tv_address;
    private EditText et_name,et_phone,et_address_info;
    private View view_back,view_finish;
    private Switch switch1;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_add_address, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){


        loadingDialog = DialogUtils.createLoadDialog(AddAddressActivity.this, false);
        tv_address=(TextView) findViewById(R.id.tv_address);

        view_back=(View)findViewById(R.id.view_back);
        view_finish=(View)findViewById(R.id.view_finish);
        et_name=(EditText) findViewById(R.id.et_name);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_address_info=(EditText) findViewById(R.id.et_address_info);
        switch1=(Switch) findViewById(R.id.switch1);
        view_back.setOnClickListener(this);
        view_finish.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }


    public void addAddress() {
        loadingDialog.show();
        AddOrUpdateReceiveAddressProtocol addOrUpdateReceiveAddressProtocol = new AddOrUpdateReceiveAddressProtocol();

        String url = addOrUpdateReceiveAddressProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("contacts", contacts);
        params.put("contactsPhoneNumber",  contactsPhoneNumber);
        params.put("provinceId", provinceId);
        params.put("cityId",  cityId);
        params.put("areaId", areaId);
        params.put("address",  address);
        params.put("isDefault", String.valueOf(isDefault));





        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AddAuthenticationInfoResponse addAuthenticationInfoResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
                LogUtils.e("addAuthenticationInfoResponse:" + addAuthenticationInfoResponse.toString());
                if (addAuthenticationInfoResponse.code.equals("0")) {
                    DialogUtils.showAlertDialog(AddAddressActivity.this,
                            "添加成功！");
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(AddAddressActivity.this,
                            addAuthenticationInfoResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddAddressActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddAddressActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_finish: {
                contacts = et_name.getText().toString().trim();
                address = et_address_info.getText().toString().trim();
                contactsPhoneNumber= et_phone.getText().toString().trim();
                if(switch1.isChecked()){
                    isDefault=1;
                }else{
                    isDefault=0;
                }
                if (!TextUtils.isEmpty(contacts) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(contactsPhoneNumber)&&!TextUtils.isEmpty(provinceId) && !TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(areaId)) {
                    addAddress();
                }else{
                    DialogUtils.showAlertDialog(AddAddressActivity.this,
                            "请填写完整的信息！");
                }
                break;
            }
            case R.id.view_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_address: {
                Intent intent = new Intent(AddAddressActivity.this, RegionSelectActivity.class);
                UIUtils.startActivityForResultNextAnim(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                tv_address.setText(data.getStringExtra("region"));
                provinceId = data.getStringExtra("province");
                cityId = data.getStringExtra("city");
                areaId = data.getStringExtra("district");
                LogUtils.e("选择："+provinceId+"   cityId:"+cityId+"  areaId:"+areaId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
