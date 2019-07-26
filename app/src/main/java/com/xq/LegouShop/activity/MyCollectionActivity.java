package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.CollectionAdapter;
import com.xq.LegouShop.adapter.OrderAdapter;
import com.xq.LegouShop.adapter.ShopCollectionAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.bean.SelectBean;
import com.xq.LegouShop.protocol.DelCollectionGoodsProtocol;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.GetUserCollectionListProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.GetUserCollectionListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.CustomExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//我的收藏
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private Dialog loadingDialog;
    private View view_back, view_update;
    private CustomExpandableListView lv_collection;
    private ShopCollectionAdapter collectionAdapter;
    private List<OrderBean> orderBeanList;
    private Button btn_check;
    private CheckBox cb_checkbox;
    private RelativeLayout ll_bottom;
    private String userCollectionId="";
    private Button btn_del;
    private GetUserCollectionListResponse getUserCollectionListResponse;
    private List<String> selectId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_collection, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate() {
        selectId=new ArrayList<>();
        orderBeanList = new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(MyCollectionActivity.this, false);
        ll_bottom=(RelativeLayout) findViewById(R.id.ll_bottom);
        cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
        btn_check = (Button) findViewById(R.id.btn_check);
        btn_del= (Button) findViewById(R.id.btn_delete);
        lv_collection = (CustomExpandableListView) findViewById(R.id.lv_collection);
        view_update = (View) findViewById(R.id.view_update);
        view_back = (View) findViewById(R.id.view_back);

        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        view_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=ll_bottom.getVisibility();
                if(i!=0) {
                    collectionAdapter.setShow(true);
                    ll_bottom.setVisibility(View.VISIBLE);
                }else{
                    collectionAdapter.setShow(false);
                    ll_bottom.setVisibility(View.GONE);
                }
            }
        });
        btn_del.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        getUserCollectionList();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_check: {
                if (cb_checkbox.isChecked()) {
                    HashMap<Integer,SelectBean> selectBeanHashMap=collectionAdapter.getHashMap();
                    for(int i=0;i<getUserCollectionListResponse.getDataList().size();i++){
                        SelectBean selectBean=new SelectBean();
                        selectBean.id=String.valueOf(i);
                        HashMap<Integer,Boolean>isSelectmap=new HashMap<>();
                        for(int j=0;j<getUserCollectionListResponse.getDataList().get(i).userCollectionList.size();j++){
                            isSelectmap.put(j,false);
                        }
                        selectBean.hashMap=isSelectmap;
                        selectBeanHashMap.put(i,selectBean);
                        collectionAdapter.setHashMap(selectBeanHashMap);
                    }
                    cb_checkbox.setChecked(false);
                    btn_check.setText("全选");
//                    SelectBean selectBean=collectionAdapter.getSelectBean();
//                    HashMap<Integer,Boolean> isSelectmap;
//                    if(selectBean.hashMap==null){
//                        isSelectmap=new HashMap<>();
//                    }else{
//                        isSelectmap=selectBean.hashMap;
//                    }
//                    for(int i=0;i<getUserCollectionListResponse.getDataList().size();i++){
//                        for(int j=0;j<getUserCollectionListResponse.getDataList().get(i).userCollectionList.size();j++){
//                            isSelectmap.put(j,false);
//                        }
//                    }
//                    selectBean.hashMap=isSelectmap;
//                    collectionAdapter.setSelectBean(selectBean);
//                    cb_checkbox.setChecked(false);
//                    btn_check.setText("全选");
                } else {
                    HashMap<Integer,SelectBean> selectBeanHashMap=collectionAdapter.getHashMap();
                    for(int i=0;i<getUserCollectionListResponse.getDataList().size();i++){
                        SelectBean selectBean=new SelectBean();
                        selectBean.id=String.valueOf(i);
                        HashMap<Integer,Boolean>isSelectmap=new HashMap<>();
                        for(int j=0;j<getUserCollectionListResponse.getDataList().get(i).userCollectionList.size();j++){
                            isSelectmap.put(j,true);
                        }
                        selectBean.hashMap=isSelectmap;
                        selectBeanHashMap.put(i,selectBean);
                        collectionAdapter.setHashMap(selectBeanHashMap);
                    }

//                    for(int i=0;i<getUserCollectionListResponse.getDataList().size();i++){
//
//                    }
//                    SelectBean selectBean=collectionAdapter.getSelectBean();
//                    HashMap<Integer,Boolean> isSelectmap;
//                    if(selectBean.hashMap==null){
//                        isSelectmap=new HashMap<>();
//                    }else{
//                        isSelectmap=selectBean.hashMap;
//                    }
//                    for(int i=0;i<getUserCollectionListResponse.getDataList().size();i++){
//                        for(int j=0;j<getUserCollectionListResponse.getDataList().get(i).userCollectionList.size();j++){
//                            isSelectmap.put(j,true);
//                        }
//                    }
//                    selectBean.hashMap=isSelectmap;
//                    collectionAdapter.setSelectBean(selectBean);
                    cb_checkbox.setChecked(true);
                    btn_check.setText("取消全选");
                }
                break;
            }
            case R.id.btn_delete:{
                for (int i=0;i<selectId.size();i++){
                    if(TextUtils.isEmpty(userCollectionId)){
                        userCollectionId=selectId.get(i);
                    }else {
                        userCollectionId=userCollectionId+","+selectId.get(i);
                    }
                }
                LogUtils.e("选中： userCollectionId:"+userCollectionId);
                if(!TextUtils.isEmpty(userCollectionId)){
                    delCollectionGoods();
                }
                break;
            }
        }
    }

    //获取验证码
    public void getUserCollectionList() {
        loadingDialog.show();
        GetUserCollectionListProtocol getUserCollectionListProtocol = new GetUserCollectionListProtocol();
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        String url = getUserCollectionListProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getUserCollectionListResponse = gson.fromJson(json, GetUserCollectionListResponse.class);
                LogUtils.e("getUserCollectionListResponse:" + getUserCollectionListResponse.toString());
                if (getUserCollectionListResponse.code .equals("0")) {
                    loadingDialog.dismiss();
                    if (collectionAdapter == null) {
                        collectionAdapter = new ShopCollectionAdapter(MyCollectionActivity.this, getUserCollectionListResponse.dataList);
                        lv_collection.setAdapter(collectionAdapter);
                    } else {
                        collectionAdapter.setDataList(getUserCollectionListResponse.dataList);
                        collectionAdapter.notifyDataSetChanged();
                    }
                    lv_collection.setGroupIndicator(null);
                    //全展开
                    for(int i = 0; i < collectionAdapter.getGroupCount(); i++){
                        lv_collection.expandGroup(i);

                    }
                    lv_collection.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {
                            // TODO Auto-generated method stub
                            LogUtils.e("groupPosition:" + groupPosition);

                            return true;
                        }
                    });
                    lv_collection.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            int i=ll_bottom.getVisibility();
                            if(i==0){
                                HashMap<Integer,SelectBean> selectBeanHashMap=collectionAdapter.getHashMap();
                                if(selectBeanHashMap.get(groupPosition)==null){
                                    SelectBean selectBean=new SelectBean();
                                    selectBean.id=String.valueOf(groupPosition);
                                    HashMap<Integer,Boolean>isSelectmap=new HashMap<>();

                                    if(isSelectmap.get(childPosition)!=null) {
                                        if(isSelectmap.get(childPosition)){
                                            selectId.remove(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                        }else{
                                            selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                        }
                                        isSelectmap.put(childPosition, !isSelectmap.get(childPosition));
                                    }else{
                                        isSelectmap.put(childPosition,true);
                                        selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                    }
                                    selectBean.hashMap=isSelectmap;
                                    selectBeanHashMap.put(groupPosition,selectBean);
                                    collectionAdapter.setHashMap(selectBeanHashMap);
                                }else{
                                    SelectBean selectBean=selectBeanHashMap.get(groupPosition);
                                    HashMap<Integer,Boolean>isSelectmap=selectBean.hashMap;
                                    if(isSelectmap.get(childPosition)!=null) {
                                        if(isSelectmap.get(childPosition)){
                                            selectId.remove(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                        }else{
                                            selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                        }
                                        isSelectmap.put(childPosition, !isSelectmap.get(childPosition));
                                    }else{
                                        isSelectmap.put(childPosition,true);
                                        selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
                                    }
                                    selectBean.hashMap=isSelectmap;
                                    selectBeanHashMap.put(groupPosition,selectBean);
                                    collectionAdapter.setHashMap(selectBeanHashMap);
                                }

//                                SelectBean selectBean=collectionAdapter.getSelectBean();
//                                selectBean.id=String.valueOf(groupPosition);
//                                HashMap<Integer,Boolean> isSelectmap;
//                                if(selectBean.hashMap==null){
//                                    isSelectmap=new HashMap<>();
//                                }else{
//                                    isSelectmap=selectBean.hashMap;
//                                }
//                                if(isSelectmap.get(childPosition)!=null) {
//                                    if(isSelectmap.get(childPosition)){
//                                        selectId.remove(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
//                                    }else{
//                                        selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
//                                    }
//                                    isSelectmap.put(childPosition, !isSelectmap.get(childPosition));
//                                }else{
//                                    isSelectmap.put(childPosition,true);
//                                    selectId.add(getUserCollectionListResponse.getDataList().get(groupPosition).userCollectionList.get(childPosition).userCollectionId);
//                                }
//                                selectBean.hashMap=isSelectmap;
//                                collectionAdapter.setSelectBean(selectBean);
//                                LogUtils.e("groupPosition childPosition:" + childPosition+"    status:"+isSelectmap.get(childPosition));
                            }
                            return false;
                        }
                    });
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(MyCollectionActivity.this,
                            getUserCollectionListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MyCollectionActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void delCollectionGoods() {
        loadingDialog.show();
        DelCollectionGoodsProtocol delCollectionGoodsProtocol = new DelCollectionGoodsProtocol();

        String url = delCollectionGoodsProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("userCollectionId", userCollectionId);
//        if(salesmanBean!=null){
//            params.put("type", "0");
//            params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
//        }else{
//            params.put("type", "1");
//            params.put("businessId",  String.valueOf(merchantBean.getId()));
//        }


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                FindPwdResponse modifyLoginPwdResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
                if (modifyLoginPwdResponse.code.equals("0")) {
                    int i=ll_bottom.getVisibility();
                    if(i!=0) {
                        collectionAdapter.setShow(true);
                        ll_bottom.setVisibility(View.VISIBLE);
                    }else{
                        collectionAdapter.setShow(false);
                        ll_bottom.setVisibility(View.GONE);
                    }
                    loadingDialog.dismiss();
                    UIUtils.showToastSafe(modifyLoginPwdResponse.msg);
                    getUserCollectionList();
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(MyCollectionActivity.this,
                            modifyLoginPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MyCollectionActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(MyCollectionActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }


}
