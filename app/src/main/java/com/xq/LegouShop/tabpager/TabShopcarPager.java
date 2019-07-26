package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.xq.LegouShop.activity.MainActivity;
import com.xq.LegouShop.adapter.RecommendAdapter;
import com.xq.LegouShop.adapter.ShoppingcarAdapter;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.DeleteClickListener;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.xq.LegouShop.weiget.MyListView;
import com.xq.LegouShop.weiget.SwipeListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * 购物车
 * @描述: TODO
 */
public class TabShopcarPager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private RelativeLayout rl_order_manager;
    private RelativeLayout rl_marketing;
    private RelativeLayout rl_set,rl_member_manager,rl_message,rl_report,rl_menu,rl_tongji;
    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private TextView tv_shop_name,tv_todayTurnover,tv_qrPayTurnover,tv_todayOrderSucCount;
    private ShoppingcarAdapter shoppingcarAdapter;
    private RecommendAdapter recommendAdapter;
    private SwipeListView lv_shopping_goods;
    private List<CartBean> selectedGoods;
    private List<OrderBean> orderBeanList;
    private GridView gv_tuijian;
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
        lv_shopping_goods=(SwipeListView) view.findViewById(R.id.lv_shopping_goods);
        gv_tuijian=(GridView) view.findViewById(R.id.gv_tuijian);
//        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
//        tv_shop_name=(TextView)view.findViewById(R.id.tv_shop_name);
//        tv_todayTurnover=(TextView)view.findViewById(R.id.tv_todayTurnover);
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
//        rl_member_manager.setOnClickListener(this);
//        rl_report.setOnClickListener(this);
//        rl_menu.setOnClickListener(this);
//        rl_tongji.setOnClickListener(this);
//        tv_shop_name.setText(merchantBean.getStoreName());
//        getHome();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
        orderBeanList=new ArrayList<>();
        selectedGoods=new ArrayList<>();
        selectedGoods.add(null);
        selectedGoods.add(null);
        selectedGoods.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        orderBeanList.add(null);
        shoppingcarAdapter=new ShoppingcarAdapter(mContext,selectedGoods,lv_shopping_goods);
        recommendAdapter=new RecommendAdapter(mContext,orderBeanList);
        lv_shopping_goods.setAdapter(shoppingcarAdapter);
        gv_tuijian.setAdapter(recommendAdapter);
        lv_shopping_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_tongji: {
//                Intent intent = new Intent(mContext, StatisticsMerchantActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
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

//    public void getHome() {
//        loadingDialog.show();
//        GetHomeProtocol getHomeProtocol=new GetHomeProtocol();
//        url = getHomeProtocol.getApiFun();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("businessId", String.valueOf(merchantBean.getId()));
//
//        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
//
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                loadingDialog.dismiss();
//                GetHomeResponse getHomeResponse = gson.fromJson(json, GetHomeResponse.class);
//                com.ciba.wholefinancial.util.LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
//                if (getHomeResponse.getCode() == 0) {
//                    DecimalFormat df = new DecimalFormat("#.00");
//                    if(getHomeResponse.getQrPayTurnover()>0){
//                        tv_qrPayTurnover.setText(df.format(getHomeResponse.getQrPayTurnover()));
//                    }else{
//                        tv_qrPayTurnover.setText("0.00");
//                    }
//                    if (getHomeResponse.getTodayTurnover()>0){
//                        tv_todayTurnover.setText(df.format(getHomeResponse.getTodayTurnover()));
//                    }else{
//                        tv_todayTurnover.setText("0.00");
//                    }
//                   tv_todayOrderSucCount.setText(getHomeResponse.getTodayOrderSucCount());
//
//                } else {
//                    if(getHomeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){
//
//                        com.ciba.wholefinancial.util.DialogUtils.showAlertToLoginDialog(mContext,
//                                getHomeResponse.msg);
//                    }else{
//                        com.ciba.wholefinancial.util.DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
//                    }
//                }
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                loadingDialog.dismiss();
//                com.ciba.wholefinancial.util.DialogUtils.showAlertDialog(mContext, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//        });
//    }


}
