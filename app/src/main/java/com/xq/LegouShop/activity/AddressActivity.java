package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.AddressAdapter;
import com.xq.LegouShop.adapter.RewardsAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.protocol.GetUserReceiveAddressListProtocol;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.GetUserReceiveAddressListResponse;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

//我的收货地址
public class AddressActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private Dialog loadingDialog;
    private AddressAdapter addressAdapter;
    private List<OrderBean> orderBeanList;
    private View view_back;
    private ListView lv_address;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    private View view_add;
    private  GetUserReceiveAddressListResponse getUserReceiveAddressListResponse;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_address, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {

        loadingDialog = DialogUtils.createLoadDialog(AddressActivity.this, false);
        ll_empty=(LinearLayout)findViewById(R.id.ll_empty);
        view_back=(View)findViewById(R.id.view_back);
        view_add=(View)findViewById(R.id.view_add);
        lv_address=(ListView)findViewById(R.id.lv_address);
        view_back.setOnClickListener(this);
        view_add.setOnClickListener(this);
        orderBeanList = new ArrayList<>();
        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);

        getAddressLists();

        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(AddressActivity.this, UpdateAddressActivity.class);
                intent.putExtra("AddressBean",getUserReceiveAddressListResponse.getDataList().get(i));
                UIUtils.startActivityForResultNextAnim(intent,100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getAddressLists() {
        loadingDialog.show();
        GetUserReceiveAddressListProtocol getUserReceiveAddressListProtocol = new GetUserReceiveAddressListProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = getUserReceiveAddressListProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getUserReceiveAddressListResponse = gson.fromJson(json, GetUserReceiveAddressListResponse.class);
                LogUtils.e("getUserReceiveAddressListResponse:" + getUserReceiveAddressListResponse.toString());
                if (getUserReceiveAddressListResponse.code.equals("0")) {
                    if(getUserReceiveAddressListResponse.dataList.size()==0){
                        ll_empty.setVisibility(View.VISIBLE);
                        lv_address.setVisibility(View.GONE);
                    }else{
                        if (addressAdapter == null) {
                            addressAdapter = new AddressAdapter( AddressActivity.this, getUserReceiveAddressListResponse.getDataList());
                            lv_address.setAdapter(addressAdapter);
                        } else {
                            addressAdapter.setDate(getUserReceiveAddressListResponse.getDataList());
                            addressAdapter.notifyDataSetChanged();
                        }
                    }

                } else {
                    DialogUtils.showAlertDialog(AddressActivity.this,
                            getUserReceiveAddressListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddressActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.view_add:{
                Intent intent=new Intent(this, AddAddressActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,100);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100){
            getAddressLists();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
