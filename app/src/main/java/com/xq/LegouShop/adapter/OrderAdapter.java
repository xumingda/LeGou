package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GameRoomActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.callback.OrderCallBack;
import com.xq.LegouShop.protocol.CancelOrderProtocol;
import com.xq.LegouShop.protocol.FinishOrderProtocol;
import com.xq.LegouShop.protocol.GetOrderListProtocol;
import com.xq.LegouShop.protocol.PayOrderProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.response.GetOrderListResponse;
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
public class OrderAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG="ShowAdapter";
    private List<OrderBean> orderBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    private Dialog loadingDialog;
    private int orderId;
    //取消0.1付款
    private int type;
    private Dialog dialog;
    //0,1,2,3
    private int show_type;
    private  OrderCallBack orderCallBack;
    public OrderAdapter(Context context, List<OrderBean> orderBeanList,  OrderCallBack orderCallBack, int show_type){
        mContext=context;
        this.orderBeanList=orderBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        this.orderCallBack=orderCallBack;
        gson = new Gson();
        this.show_type=show_type;
    }

    public void setDate(List<OrderBean> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
            vh = new ViewHolder();
            vh.tv_shopName = (TextView) view.findViewById(R.id.tv_shopName);
            vh.tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
            vh.tv_price = (TextView) view.findViewById(R.id.tv_price);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_order_no= (TextView) view.findViewById(R.id.tv_order_no);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_user_name);
            vh.tv_num= (TextView) view.findViewById(R.id.tv_num);
            vh.tv_info= (TextView) view.findViewById(R.id.tv_info);
            vh.iv_pic= (ImageView) view.findViewById(R.id.iv_pic);
            vh.tv_pay= (TextView) view.findViewById(R.id.tv_pay);
            vh.tv_more= (TextView) view.findViewById(R.id.tv_more);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final OrderBean orderBean=orderBeanList.get(pos);
        if(!TextUtils.isEmpty(orderBean.getShopName())) {
            vh.tv_shopName.setText(orderBean.getShopName());
        }else{
            vh.tv_shopName.setText("平台自营");
        }
        vh.tv_order_no.setText("订单号:"+orderBean.getOrderNo());

        vh.tv_price.setText("¥"+orderBean.getNeedMoney());
        vh.tv_time.setText(orderBean.getCreateTime());
        vh.tv_name.setText(orderBean.getOrderGoodsList().get(0).getGoodsName());
        vh.tv_info.setText(orderBean.getOrderGoodsList().get(0).getGoodsGroupValues());
        vh.tv_num.setText("X"+orderBean.getOrderGoodsList().get(0).getBuyCount());
        vh.tv_price.setText("合计：¥"+orderBean.getNeedMoney());
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+orderBean.getOrderGoodsList().get(0).getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
        if(orderBean.getOrderGoodsList().size()>1){
            vh.tv_more.setVisibility(View.VISIBLE);
        }else{
            vh.tv_more.setVisibility(View.INVISIBLE);
        }
        if(show_type==0){
            vh.tv_pay.setVisibility(View.VISIBLE);
            vh.tv_cancel.setVisibility(View.VISIBLE);
            vh.tv_cancel.setText("取消");
        }else if(show_type==1){
            vh.tv_pay.setVisibility(View.INVISIBLE);
            vh.tv_cancel.setVisibility(View.VISIBLE);
            vh.tv_cancel.setText("取消");
        }
        else if(show_type==2){
            vh.tv_pay.setVisibility(View.INVISIBLE);
            vh.tv_cancel.setVisibility(View.VISIBLE);
            vh.tv_cancel.setText("确认收货");
        }else{
            vh.tv_pay.setVisibility(View.INVISIBLE);
            vh.tv_cancel.setVisibility(View.INVISIBLE);
        }
        vh.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderId=orderBean.getId();
                if(show_type==0||show_type==1){
                    type=0;
                    dialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"是否取消订单？","提示", OrderAdapter.this);
                }else {
                    type=2;
                    dialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"是否确认收货？","提示", OrderAdapter.this);
                }
                dialog.show();
            }
        });
        vh.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=1;
                orderId=orderBean.getId();
                dialog=DialogUtils.showAlertDoubleBtnDialog(mContext,"是否付款订单？","提示", OrderAdapter.this);
                dialog.show();
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
        TextView tv_price;
        TextView tv_order_no;
        TextView tv_name;
        TextView tv_num;
        TextView tv_pay;
        TextView tv_info;
        ImageView iv_pic;
        TextView tv_more;
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
}
