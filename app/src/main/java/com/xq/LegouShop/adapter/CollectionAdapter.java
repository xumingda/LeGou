package com.xq.LegouShop.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.OrderBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class CollectionAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<OrderBean> orderBeanList;
    private Context mContext;

    private Gson gson;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
        notifyDataSetChanged();
    }

    public CollectionAdapter(Context context, List<OrderBean> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;

        gson = new Gson();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.collection_item, null);
            vh = new ViewHolder();
            vh.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            vh.tv_info = (TextView) view.findViewById(R.id.tv_info);
            vh.mCbCheckbox= (CheckBox) view.findViewById(R.id.cb_checkbox);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        OrderBean orderBean=orderBeanList.get(pos);
        if(isShow){
            vh.mCbCheckbox.setVisibility(View.VISIBLE);
        }else {
            vh.mCbCheckbox.setVisibility(View.INVISIBLE);
        }
        //判断CheckBox的状态
//        if(pos%2==0){
//            vh.mCbCheckbox.setChecked(true);//选中
//        }else {
//            vh.mCbCheckbox.setChecked(false);//未选中
//        }
//        if(orderBean.getPhoneNumber()!=null&&orderBean.getPhoneNumber().length()>10){
//            vh.tv_phone.setText(orderBean.getPhoneNumber().substring(0,3)+"****"+orderBean.getPhoneNumber().substring(7,11));
//        }else{
//            vh.tv_phone.setText(orderBean.getPhoneNumber());
//        }
//
//        vh.tv_price.setText("¥"+orderBean.getNeedMoney());
//        vh.tv_time.setText(orderBean.getCreateTime());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_user_name;
        TextView tv_title;
        TextView tv_info;
        CheckBox mCbCheckbox;
    }

}
