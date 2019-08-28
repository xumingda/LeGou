package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.DrawbackInfoActivity;
import com.xq.LegouShop.activity.OrderInfoActivity;
import com.xq.LegouShop.adapter.AfterSaleOrderAdapter;
import com.xq.LegouShop.adapter.OrderAdapter;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.AfterSaleOrderBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.callback.OrderCallBack;
import com.xq.LegouShop.protocol.GetOrderListProtocol;
import com.xq.LegouShop.response.GetAfterOrderListResponse;
import com.xq.LegouShop.response.GetOrderListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class PendingOrderPager extends ViewTabBasePager implements
        PullToRefreshBase.OnRefreshListener, OrderCallBack {
    // 分类listview
    @ViewInject(R.id.lv_pending_order)
    private PullToRefreshListView lv_pending_order;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;

    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private OrderAdapter orderAdapter;
    private List<OrderBean> orderBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private int status;
    private GetOrderListResponse getOrderListResponse;
    private GetAfterOrderListResponse getAfterOrderListResponse;
    private AfterSaleOrderAdapter afterSaleOrderAdapter;
    private List<AfterSaleOrderBean> afterSaleOrderBeanList;

    public PendingOrderPager(Context context, int status) {
        super(context);
        this.status = status;
        LogUtils.e("status:" + status);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.pending_order_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        orderBeanList = new ArrayList<>();
        afterSaleOrderBeanList = new ArrayList<>();
        lv_pending_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pending_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_pending_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_pending_order.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_pending_order.setOnRefreshListener(this);
        if (status == 4) {
            getAfterOrderList();
            lv_pending_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mContext, DrawbackInfoActivity.class);
                    intent.putExtra("orderId", afterSaleOrderBeanList.get(i - 1).getId());
                    intent.putExtra("orderNo",afterSaleOrderBeanList.get(i - 1).getOrderNo());
                    intent.putExtra("shopName",afterSaleOrderBeanList.get(i - 1).getShopName());
                    intent.putExtra("time",afterSaleOrderBeanList.get(i - 1).getCreateTime());
                    intent.putExtra("pic",afterSaleOrderBeanList.get(i - 1).getOrderGoodsList().get(0).getPic());
                    intent.putExtra("goodName",afterSaleOrderBeanList.get(i - 1).getOrderGoodsList().get(0).getGoodsName());
                    intent.putExtra("orderMoney",afterSaleOrderBeanList.get(i - 1).getOrderMoney());
                    intent.putExtra("bugCount",afterSaleOrderBeanList.get(i - 1).getOrderGoodsList().get(0).getBuyCount());
                    intent.putExtra("userReturnGoodsId",afterSaleOrderBeanList.get(i-1).getUserReturnGoodsId());
                    intent.putExtra("userRefundId", afterSaleOrderBeanList.get(i - 1).getUserRefundId());
                    intent.putExtra("userChangeGoodsId",afterSaleOrderBeanList.get(i-1).getUserChangeGoodsId());
                    UIUtils.startActivityNextAnim(intent);
                }
            });
        } else {
            getOrderList();
            lv_pending_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mContext, OrderInfoActivity.class);
                    intent.putExtra("orderId", orderBeanList.get(i - 1).getId());
                    intent.putExtra("status", orderBeanList.get(i - 1).getPayStatus());
                    UIUtils.startActivityNextAnim(intent);
                }
            });
        }



    }

    private void setData() {
        orderBeanList.addAll(getOrderListResponse.dataList);

        orderAdapter = new OrderAdapter(mContext, orderBeanList, this, status);
        lv_pending_order.setAdapter(orderAdapter);
    }

    private void setData1() {
        afterSaleOrderBeanList.addAll(getAfterOrderListResponse.dataList);

        afterSaleOrderAdapter = new AfterSaleOrderAdapter(mContext, afterSaleOrderBeanList, this, status);
        lv_pending_order.setAdapter(afterSaleOrderAdapter);
    }

    private void getOrderList() {
        loadingDialog.show();

        GetOrderListProtocol getOrderListProtocol = new GetOrderListProtocol();
        url = getOrderListProtocol.getApiFun();


        Map<String, String> params = new HashMap<String, String>();

        params.put("payStatus", String.valueOf(status));

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getOrderListResponse = gson.fromJson(json, GetOrderListResponse.class);
                LogUtils.e("getOrderListResponse:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode().equals("0")) {
                    if (getOrderListResponse.dataList.size() > 0) {
                        ll_empty.setVisibility(View.GONE);
                        lv_pending_order.setVisibility(View.VISIBLE);
                        if (pageNo == 1) {
                            orderBeanList.clear();
                        }
                        setData();
                    } else {
                        if (pageNo == 1) {
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_pending_order.setVisibility(View.GONE);
                        } else {
                            DialogUtils.showAlertDialog(mContext, "没有更多数据了！");
                        }

                    }

                } else {
                    if (getOrderListResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getOrderListResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getOrderListResponse.msg);
                    }
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    private void getAfterOrderList() {
        loadingDialog.show();
        url = "/user/getAfterSaleOrderList";
        Map<String, String> params = new HashMap<String, String>();

        params.put("payStatus", String.valueOf(status));

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getAfterOrderListResponse = gson.fromJson(json, GetAfterOrderListResponse.class);
                LogUtils.e("getAfterOrderListResponse:" + getAfterOrderListResponse.toString());
                if (getAfterOrderListResponse.getCode().equals("0")) {
                    if (getAfterOrderListResponse.dataList.size() > 0) {
                        ll_empty.setVisibility(View.GONE);
                        lv_pending_order.setVisibility(View.VISIBLE);
                        if (pageNo == 1) {
                            afterSaleOrderBeanList.clear();
                        }
                        setData1();
                    } else {
                        if (pageNo == 1) {
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_pending_order.setVisibility(View.GONE);
                        } else {
                            DialogUtils.showAlertDialog(mContext, "没有更多数据了！");
                        }

                    }

                } else {
                    if (getOrderListResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getAfterOrderListResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getAfterOrderListResponse.msg);
                    }
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_pending_order.isHeaderShown()) {
                pageNo = 1;
                if(status==4){
                    afterSaleOrderBeanList.clear();
                    getAfterOrderList();
                }else {
                    orderBeanList.clear();
                    getOrderList();
                }
            } else if (lv_pending_order.isFooterShown()) {
                pageNo++;
                if(status==4){
                    getAfterOrderList();
                }else {
                    getOrderList();
                }
            }
        }
    }

    @Override
    public void updateData() {
        pageNo = 1;
        getOrderList();
    }
}