package com.xq.LegouShop.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.callback.OnCallBackDeleteShopingCart;
import com.xq.LegouShop.protocol.DelCartGoodProtocol;
import com.xq.LegouShop.response.UpdatePhoneResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.SwipeListView;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingcarAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG = "ClientListAdapter";
    private List<CartBean> dataLists;
    private Context mContext;
    private HashMap<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();
    private int count = 0;
    private Button btn_order;
    private boolean allChecked = true;
    private CheckBox cb_all_good;
    private String url;
    private Dialog loadingDialog;
    private String userCartIds;
    private int del_item = 0;
    private HashMap<CartBean, Boolean> isCheckMap = new HashMap<CartBean, Boolean>();
    //选择的数量
    private String goods_num;
    private AlertDialog alertDialog;
    private int goods_id;
    private String goods_attr_id;
    private String goods_number;
    private  SwipeListView swipeListView;
    private OnCallBackDeleteShopingCart onCallBackDeleteShopingCart;
    public ShoppingcarAdapter(Context context, List<CartBean> dataLists, SwipeListView swipeListView, OnCallBackDeleteShopingCart onCallBackDeleteShopingCart,CheckBox cb_all_good,Dialog loadingDialog) {
        mContext = context;
        this.dataLists = dataLists;
        this.loadingDialog = loadingDialog;
        this.swipeListView=swipeListView;
        count = dataLists.size();
        this.btn_order = btn_order;
        this.cb_all_good = cb_all_good;
        this.onCallBackDeleteShopingCart = onCallBackDeleteShopingCart;
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }

        for (int i = 0; i < dataLists.size(); i++) {
            dataLists.get(i).setSelected(true);
            isCheckMap.put(dataLists.get(i), true);
        }
    }
    public void setData( List<CartBean> dataLists){
        this.dataLists = dataLists;
        notifyDataSetChanged();
    }

    public void remove(CartBean cartBean){
        dataLists.remove(cartBean);
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return dataLists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shoppingcar_item, null);
            vh = new ViewHolder();
            vh.tv_del = (TextView) view.findViewById(R.id.tv_del);
            vh.ch_good = (CheckBox) view.findViewById(R.id.ch_good);
            vh.tv_good_name = (TextView) view.findViewById(R.id.tv_good_name);
            vh.tv_good_des = (TextView) view.findViewById(R.id.tv_good_dec);
            vh.iv_good = (ImageView) view.findViewById(R.id.iv_good);
            vh.et_num = (EditText) view.findViewById(R.id.et_num);
            vh.tv_add = (TextView) view.findViewById(R.id.tv_add);
            vh.tv_reduce = (TextView) view.findViewById(R.id.tv_reduce);
            vh.tv_good_total_price= (TextView) view.findViewById(R.id.tv_good_price_title);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCartIds = dataLists.get(pos).getId();
                runDeleteCart();
                del_item=pos;

            }
        });

        vh.ch_good.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.e("pos:" + pos + "   :" + isChecked);
                dataLists.get(pos).setSelected(isChecked);
                if (isChecked) {
                    onCallBackDeleteShopingCart.addSelectItemCallBack(dataLists.get(pos));
                    count++;
                } else {
                    onCallBackDeleteShopingCart.delSelectItemCallBack(dataLists.get(pos));
                    count--;
                }
                isCheckMap.put(dataLists.get(pos), isChecked);
                if (count == dataLists.size()) {
                    cb_all_good.setChecked(true);
                } else if (count == 0) {
                    cb_all_good.setChecked(false);
                }
//                LogUtils.e("count:" + count);
            }
        });

        if (isCheckMap.size() > 0) {
            if (isCheckMap.get(dataLists.get(pos))) {
                vh.ch_good.setChecked(true);
            } else {
                vh.ch_good.setChecked(false);
            }
        }
        vh.tv_good_des.setText(dataLists.get(pos).getGoodsGroupValues());
         vh.tv_good_name.setText(dataLists.get(pos).getGoodsName());
//        vh.tv_good_num.setText("x" + dataLists.get(pos).getGoods_number());
        DecimalFormat df = new DecimalFormat("0.00");
        vh.tv_good_total_price.setText("￥" + df.format(Double.parseDouble(dataLists.get(pos).getSalePrice()) * Integer.parseInt(dataLists.get(pos).getBuyCount())));
        ImageLoader.getInstance().displayImage("http://qiniu.lelegou.pro/"+dataLists.get(pos).getPic(), vh.iv_good, PictureOption.getSimpleOptions());
        vh.et_num.setText("" + dataLists.get(pos).getBuyCount());
        ViewListener viewListener=new ViewListener(pos,vh);
        vh.tv_add.setOnClickListener(viewListener);
        vh.tv_reduce.setOnClickListener(viewListener);

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataLists.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    class ViewHolder {
        TextView tv_editor;
        TextView tv_del;
        CheckBox ch_good;
        TextView tv_time;
        TextView tv_good_name;
        TextView tv_good_des;
        TextView tv_good_total_price;
        TextView tv_good_num;
        ImageView iv_good;
        EditText et_num;
        TextView tv_reduce;
        TextView tv_add;
    }

    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ViewListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;
        public ViewHolder vh;

        public ViewListener(int position, ViewHolder vh) {
            this.position = position;
            this.vh = vh;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

//                case R.id.tv_editor: {
//                    if (hashMap.get(position) != null) {
//                        if (hashMap.get(position)) {
//                            hashMap.put(position, false);
//                        } else {
//                            hashMap.put(position, true);
//                            goods_id=dataLists.get(position).getGoods_id();
//                            goods_attr_id=dataLists.get(position).getGoods_attr_id();
//                            goods_number=dataLists.get(position).getGoods_number();
//                            UpdateCartGoodsNumber();
//                        }
//                    } else {
//                        hashMap.put(position, false);
//                    }
//                    notifyDataSetChanged();
//                    break;
//                }
                case R.id.tv_add: {
                    goods_num = vh.et_num.getText().toString();
                    int num=Integer.parseInt(goods_num);
                    num = num + 1;
                    dataLists.get(position).setBuyCount(String.valueOf(num));
                    notifyDataSetChanged();
                    onCallBackDeleteShopingCart.changeSelectItemCallBack(dataLists);
                    break;
                }
                case R.id.tv_reduce: {

                    int good_num = 1;
                    goods_num = vh.et_num.getText().toString();
                    if (TextUtils.isEmpty(goods_num)) {
                        vh.et_num.setText("1");
                    } else {
                        good_num = Integer.parseInt(goods_num);
                    }
                    good_num = good_num - 1;
                    if (good_num <= 0) {
                        del_item = position;
                        alertDialog = DialogUtils.showAlertDoubleBtnDialog(mContext,
                                "确定要删除这个商品吗？", "提示",ShoppingcarAdapter.this);
                    } else {
                        dataLists.get(position).setBuyCount(String.valueOf(good_num));
                        notifyDataSetChanged();
                        onCallBackDeleteShopingCart.changeSelectItemCallBack(dataLists);
                    }
                    break;
                }
            }
        }
    }


//
    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
        for (int i = 0; i < dataLists.size(); i++) {
            isCheckMap.put(dataLists.get(i), allChecked);
        }
        notifyDataSetChanged();
    }
//
    public void runDeleteCart() {
        LogUtils.e("userCartIds:"+userCartIds);
        loadingDialog.show();
        DelCartGoodProtocol deleteCartProtocol = new DelCartGoodProtocol();
        Map<String, String> params = new HashMap<String, String>();
        url = deleteCartProtocol.getApiFun();
        params.put("userCartIds", userCartIds);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                UpdatePhoneResponse deleteCartResponse = gson.fromJson(json, UpdatePhoneResponse.class);
                LogUtils.e("sendVerificationCodeResponse:" + json.toString());
                if (deleteCartResponse.code.equals("0")) {
                    CartBean cartBean = dataLists.get(del_item);
                    dataLists.remove(del_item);
                    count--;
                    onCallBackDeleteShopingCart.delCallBack(dataLists.size(), cartBean);
                    loadingDialog.dismiss();
                    notifyDataSetChanged();
                    swipeListView.turnToNormal();

                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(mContext,
                            deleteCartResponse.msg);
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
