package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.BuyGoodActivity;
import com.xq.LegouShop.activity.MainActivity;
import com.xq.LegouShop.adapter.RecommendAdapter;
import com.xq.LegouShop.adapter.ShoppingcarAdapter;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.callback.OnCallBackDeleteShopingCart;
import com.xq.LegouShop.protocol.GetCartListProtocol;
import com.xq.LegouShop.response.GetCartListsResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.DeleteClickListener;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.xq.LegouShop.weiget.MyListView;
import com.xq.LegouShop.weiget.SwipeListView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * 购物车
 * @描述: TODO
 */
public class TabShopcarPager extends TabBasePager implements View.OnClickListener , OnCallBackDeleteShopingCart {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private RelativeLayout rl_order_manager;
    private RelativeLayout rl_marketing;
    private RelativeLayout rl_set,rl_member_manager,rl_message,rl_report,rl_menu,rl_tongji;
    private Dialog loadingDialog;
    private  GetCartListsResponse getCartListsResponse;
    private String url;
    private Gson gson;
    private TextView tv_shop_name,tv_todayTurnover,tv_qrPayTurnover,tv_todayOrderSucCount;
    private ShoppingcarAdapter shoppingcarAdapter;
    private RecommendAdapter recommendAdapter;
    private SwipeListView lv_shopping_goods;
    private List<CartBean> selectedGoods;
    private List<OrderBean> orderBeanList;
    private GridView gv_tuijian;
    private TextView tv_total_choose;
    private CheckBox cb_all_good;
    private TextView tv_total_price;
    private double total_price;
    private Button btn_order;
    private View view_cb;
    /**
     * @param context
     */
    public TabShopcarPager(Context context, FrameLayout mDragLayout,
                           MyLinearLayout mLinearLayout ) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;
    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.shopping_car_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        return view;
    }

    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        btn_order=(Button)view.findViewById(R.id.btn_order);
        lv_shopping_goods=(SwipeListView) view.findViewById(R.id.lv_shopping_goods);
        gv_tuijian=(GridView) view.findViewById(R.id.gv_tuijian);
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        tv_total_choose=(TextView)view.findViewById(R.id.tv_total_choose);
        cb_all_good=(CheckBox) view.findViewById(R.id.cb_all_good);
        view_cb=(View) view.findViewById(R.id.view_cb);
        tv_total_price=(TextView)view.findViewById(R.id.tv_total_price);
//        tv_qrPayTurnover=(TextView)view.findViewById(R.id.tv_qrPayTurnover);
//        tv_todayOrderSucCount=(TextView)view.findViewById(R.id.tv_todayOrderSucCount);
//        rl_menu=(RelativeLayout)view.findViewById(R.id.rl_menu);
//        rl_report=(RelativeLayout)view.findViewById(R.id.rl_report);
//        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message);
//        rl_member_manager=(RelativeLayout)view.findViewById(R.id.rl_member_manager);
//        rl_marketing=(RelativeLayout)view.findViewById(R.id.rl_marketing);
//        rl_order_manager=(RelativeLayout)view.findViewById(R.id.rl_order_manager);
//        rl_tongji=(RelativeLayout)view.findViewById(R.id.rl_tongji);
//        rl_set=(RelativeLayout)view.findViewById(R.id.rl_set);
//        rl_order_manager.setOnClickListener(this);
//        rl_marketing.setOnClickListener(this);
//        rl_set.setOnClickListener(this);
//        rl_message.setOnClickListener(this);
        btn_order.setOnClickListener(this);
//        rl_report.setOnClickListener(this);
//        rl_menu.setOnClickListener(this);
//        rl_tongji.setOnClickListener(this);
//        tv_shop_name.setText(merchantBean.getStoreName());
//        getHome();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);

        view_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=cb_all_good.isChecked();
                cb_all_good.setChecked(!isChecked);

                if (shoppingcarAdapter != null) {
                    shoppingcarAdapter.setAllChecked(!isChecked);
                }
                if(!isChecked){
                    total_price=0;
                    for(int i=0;i<getCartListsResponse.dataList.size();i++){
                        total_price=total_price+(Double.parseDouble(getCartListsResponse.dataList.get(i).getBuyCount())*Double.parseDouble(getCartListsResponse.dataList.get(i).getSalePrice()));
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    tv_total_price.setText("￥" + df.format(total_price));
                    tv_total_choose.setText("取消全选");
                }else{
                    total_price=0;
                    tv_total_price.setText("￥" + 0.00);
                    tv_total_choose.setText("全选");
                }

            }
        });
        orderBeanList=new ArrayList<>();
        selectedGoods=new ArrayList<>();

        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        recommendAdapter=new RecommendAdapter(mContext,orderBeanList);
        gv_tuijian.setAdapter(recommendAdapter);
        lv_shopping_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        getCartList();
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_order: {
                Intent intent = new Intent(mContext, BuyGoodActivity.class);
                List<CartBean> orderGoodBeanList = new ArrayList<CartBean>();
                for(int i=0;i<selectedGoods.size();i++){
                    CartBean cartBean = new CartBean();
                    cartBean.setGoodsName(selectedGoods.get(i).getGoodsName());
                    cartBean.setGoodsGroupValues(selectedGoods.get(i).getGoodsGroupValues());
                    cartBean.setPic(selectedGoods.get(i).getPic());
                    cartBean.setGoodsId(selectedGoods.get(i).getGoodsId());
                    cartBean.setBuyCount(selectedGoods.get(i).getBuyCount());
                    cartBean.setSalePrice(selectedGoods.get(i).getSalePrice());
                    cartBean.setGoodsCaseId(selectedGoods.get(i).getGoodsCaseId());
                    orderGoodBeanList.add(cartBean);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("orderGoodBeanList", (Serializable) orderGoodBeanList);
                intent.putExtras(bundle);
                intent.putExtra("total_price", total_price);
                UIUtils.startActivityForResultNextAnim(intent,101);
                break;
            }
//            case R.id.rl_menu:{
//                Intent intent=new Intent(mContext, MenuManagementActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_report:{
//                Intent intent=new Intent(mContext, InformationReportActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_message:{
//                Intent intent=new Intent(mContext, MessageActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_set:{
//                Intent intent=new Intent(mContext,MerchantSettingActivity   .class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_marketing:{
//                Intent intent=new Intent(mContext,MarketingActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_order_manager:{
//                Intent intent=new Intent(mContext,OrderManagerActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_member_manager:{
//                if(com.ciba.wholefinancial.util.SharedPrefrenceUtils.getInt(mContext,"master",0)==1){
//                    Intent intent=new Intent(mContext, MemberManagerActivity.class);
//                    com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                }else{
//                    com.ciba.wholefinancial.util.DialogUtils.showAlertDialog(mContext, "您没有权限查看！");
//                }
//
//                break;
//            }
        }
    }

    public void getCartList() {
        loadingDialog.show();
        GetCartListProtocol getCartListProtocol=new GetCartListProtocol();
        url = getCartListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getCartListsResponse = gson.fromJson(json, GetCartListsResponse.class);
                LogUtils.e("getCartListsResponse:" + getCartListsResponse.toString());
                if (getCartListsResponse.getCode().equals("0") ) {
                    total_price=0;
                    selectedGoods.clear();
                    selectedGoods.addAll(getCartListsResponse.dataList);
                    shoppingcarAdapter = new ShoppingcarAdapter(mContext, getCartListsResponse.dataList, lv_shopping_goods,TabShopcarPager.this,cb_all_good,loadingDialog);
                    lv_shopping_goods.setAdapter(shoppingcarAdapter);

                    for (int i=0;i<selectedGoods.size();i++){
                        total_price=total_price+(Double.parseDouble(selectedGoods.get(i).getBuyCount())*Double.parseDouble(selectedGoods.get(i).getSalePrice()));
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    tv_total_price.setText("￥" + df.format(total_price));
                } else {
                    if(getCartListsResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getCartListsResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getCartListsResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


    @Override
    public void delCallBack(int par, CartBean cartBean) {

        if(selectedGoods.contains(cartBean)){
            selectedGoods.remove(cartBean);
            total_price=0;
            for (int i=0;i<selectedGoods.size();i++){
                total_price=total_price+(Double.parseDouble(selectedGoods.get(i).getBuyCount())*Double.parseDouble(selectedGoods.get(i).getSalePrice()));
            }
            DecimalFormat df = new DecimalFormat("0.00");
            tv_total_price.setText("￥" + df.format(total_price));
        }
        if(selectedGoods.size()==getCartListsResponse.dataList.size()){
            cb_all_good.setChecked(true);
        }else {
            cb_all_good.setChecked(false);
        }
    }

    @Override
    public void delSelectItemCallBack(CartBean cartBea) {
        total_price=0;
        selectedGoods.remove(cartBea);
        if(selectedGoods.size()==getCartListsResponse.dataList.size()){
            cb_all_good.setChecked(true);
        }else {
            cb_all_good.setChecked(false);
        }
        for (int i=0;i<selectedGoods.size();i++){
            total_price=total_price+(Double.parseDouble(selectedGoods.get(i).getBuyCount())*Double.parseDouble(selectedGoods.get(i).getSalePrice()));
        }
        DecimalFormat df = new DecimalFormat("0.00");
        tv_total_price.setText("￥" + df.format(total_price));
//        tv_total_price.setText("￥" + total_price);
    }
    @Override
    public void addSelectItemCallBack(CartBean cartBea) {
        total_price=0;
        if(!selectedGoods.contains(cartBea)){
            selectedGoods.add(cartBea);
        }
        if(selectedGoods.size()==getCartListsResponse.dataList.size()){
            cb_all_good.setChecked(true);
        }else {
            cb_all_good.setChecked(false);
        }
        for (int i=0;i<selectedGoods.size();i++){
            total_price=total_price+(Double.parseDouble(selectedGoods.get(i).getBuyCount())*Double.parseDouble(selectedGoods.get(i).getSalePrice()));
        }
        DecimalFormat df = new DecimalFormat("0.00");
        tv_total_price.setText("￥" + df.format(total_price));
//        tv_total_price.setText("￥" + total_price);
    }

    @Override
    public void changeSelectItemCallBack(List<CartBean> cartBeanList) {
        total_price=0;
        for (int i=0;i<cartBeanList.size();i++){
            if(cartBeanList.get(i).isSelected()) {
                total_price = total_price + (Double.parseDouble(cartBeanList.get(i).getBuyCount()) * Double.parseDouble(cartBeanList.get(i).getSalePrice()));
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        tv_total_price.setText("￥" + df.format(total_price));
    }
}
