package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.AfterSaleOrderBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.callback.OrderCallBack;
import com.xq.LegouShop.protocol.CancelOrderProtocol;
import com.xq.LegouShop.protocol.FinishOrderProtocol;
import com.xq.LegouShop.protocol.PayOrderProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class AfterSaleOrderAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG="ShowAdapter";
    private List<AfterSaleOrderBean> orderBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    private Dialog loadingDialog;
    private int orderId;
    //取消0.1付款
    private int type;
    private Dialog dialog;
    private IWXAPI api;
    //0,1,2,3
    private int show_type;
    private  OrderCallBack orderCallBack;
    public AfterSaleOrderAdapter(Context context, List<AfterSaleOrderBean> orderBeanList, OrderCallBack orderCallBack, int show_type){
        mContext=context;
        this.orderBeanList=orderBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        this.orderCallBack=orderCallBack;
        gson = new Gson();
        this.show_type=show_type;
        api = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
    }

    public void setDate(List<AfterSaleOrderBean> orderBeanList){
        this.orderBeanList=orderBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return orderBeanList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.after_sale_order_item, null);
            vh = new ViewHolder();
            vh.tv_shopName = (TextView) view.findViewById(R.id.tv_shopName);
            vh.tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
            vh.tv_status = (TextView) view.findViewById(R.id.tv_status);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_order_no= (TextView) view.findViewById(R.id.tv_order_no);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_user_name);
            vh.tv_num= (TextView) view.findViewById(R.id.tv_num);
            vh.tv_info= (TextView) view.findViewById(R.id.tv_info);
            vh.iv_pic= (ImageView) view.findViewById(R.id.iv_pic);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final AfterSaleOrderBean orderBean=orderBeanList.get(pos);
        if(!TextUtils.isEmpty(orderBean.getShopName())) {
            vh.tv_shopName.setText(orderBean.getShopName());
        }else{
            vh.tv_shopName.setText("平台自营");
        }
        vh.tv_order_no.setText("订单号:"+orderBean.getOrderNo());


        vh.tv_time.setText(orderBean.getCreateTime());
        vh.tv_name.setText(orderBean.getOrderGoodsList().get(0).getGoodsName());
        vh.tv_info.setText(orderBean.getOrderGoodsList().get(0).getGoodsGroupValues());
        vh.tv_num.setText("X"+orderBean.getOrderGoodsList().get(0).getBuyCount());
        if(orderBean.refundStatus==10){
            vh.tv_status.setText("售后状态(申请退款中)");
        }else if(orderBean.refundStatus==11){
            vh.tv_status.setText("售后状态(商家同意退款)");
        }else if(orderBean.returnGoodsStatus==10){
            vh.tv_status.setText("售后状态(申请退货中)");
        }else if(orderBean.returnGoodsStatus==11){
            vh.tv_status.setText("售后状态(买家待发货)");
        }else if(orderBean.returnGoodsStatus==13){
            vh.tv_status.setText("售后状态(买家已发货)");
        }else if(orderBean.returnGoodsStatus==14){
            vh.tv_status.setText("售后状态(已退款)");
        }else if(orderBean.changeGoodsStatus==10){
            vh.tv_status.setText("售后状态(申请换货中)");
        }else if(orderBean.changeGoodsStatus==11){
            vh.tv_status.setText("售后状态(买家待发货)");
        }else if(orderBean.changeGoodsStatus==13){
            vh.tv_status.setText("售后状态(买家已发货)");
        }else if(orderBean.changeGoodsStatus==14){
            vh.tv_status.setText("售后状态(商家已收货=待发货)");
        }else if(orderBean.changeGoodsStatus==15){
            vh.tv_status.setText("售后状态(商家已发货=买家待收货)");
        }else if(orderBean.changeGoodsStatus==16){
            vh.tv_status.setText("售后状态(买家已收货=已完成)");
        }else{
            vh.tv_status.setText("售后状态(未申请)");
        }
        SpannableString sp = new SpannableString(vh.tv_status.getText().toString());
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.tv_status.setText(sp);
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+orderBean.getOrderGoodsList().get(0).getPic(), vh.iv_pic, PictureOption.getSimpleOptions());


        vh.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                orderId=orderBean.getId();
//                if(show_type==0||show_type==1){
//                    type=0;
//                    dialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"是否取消订单？","提示", AfterSaleOrderAdapter.this);
//                }else {
//                    type=2;
//                    dialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"是否确认收货？","提示", AfterSaleOrderAdapter.this);
//                }
//                dialog.show();
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.tv_ensure:{
                if(type==0) {
                    cancelOrderList();
                }else if(type==1){
                    payOrderList();
                }else{
                    finishOrderList();
                }
                dialog.dismiss();
                break;
            }
        }
    }


    class ViewHolder {
        TextView tv_time;
        TextView tv_cancel;
        TextView tv_shopName;
        TextView tv_status;
        TextView tv_order_no;
        TextView tv_name;
        TextView tv_num;
        TextView tv_info;
        ImageView iv_pic;
    }
    //确认订单
    private void finishOrderList() {
        loadingDialog.show();
        FinishOrderProtocol cancelOrderProtocol=new FinishOrderProtocol();
        String url  = cancelOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddAuthenticationInfoResponse getOrderListResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode() .equals("0")) {
                    UIUtils.showToastSafe(getOrderListResponse.msg);
                    orderCallBack.updateData();
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getOrderListResponse.msg);
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
    //取消订单
    private void cancelOrderList() {
        loadingDialog.show();
        CancelOrderProtocol cancelOrderProtocol=new CancelOrderProtocol();
        String url  = cancelOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddAuthenticationInfoResponse getOrderListResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode() .equals("0")) {
                    UIUtils.showToastSafe(getOrderListResponse.msg);
                    orderCallBack.updateData();
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getOrderListResponse.msg);
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
    //付款订单
    private void payOrderList() {
        loadingDialog.show();
        PayOrderProtocol payOrderProtocol=new PayOrderProtocol();
        String url  = payOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                CreateOrderResponse getOrderListResponse = gson.fromJson(json, CreateOrderResponse.class);
                LogUtils.e("cancelOrder:" + getOrderListResponse.toString());
                if (getOrderListResponse.code .equals("0")) {
                    if(TextUtils.isEmpty(getOrderListResponse.data.appid)) {
                        UIUtils.showToastSafe(getOrderListResponse.msg);
                        orderCallBack.updateData();
                    }else{
                        orderCallBack.updateData();
                        PayReq req = new PayReq();
                        req.appId = getOrderListResponse.data.appid;
                        req.partnerId = getOrderListResponse.data.partnerid;
                        req.prepayId = getOrderListResponse.data.prepayid;
                        req.nonceStr = getOrderListResponse.data.noncestr;
                        req.timeStamp = getOrderListResponse.data.timestamp;
                        req.packageValue = "Sign=WXPay";
                        req.sign = getOrderListResponse.data.sign;
                        req.extData = "app data"; // optional
                        api.registerApp(Constants.APP_ID);
                        api.sendReq(req);
                    }
                }else {
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getOrderListResponse.msg);
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
}
