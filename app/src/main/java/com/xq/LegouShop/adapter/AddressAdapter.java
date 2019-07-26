package com.xq.LegouShop.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.AddressBean;
import com.xq.LegouShop.bean.OrderBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class AddressAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<AddressBean> addressBeanList;
    private Context mContext;

    private Gson gson;
    public AddressAdapter(Context context,List<AddressBean> addressBeanList){
        mContext=context;
        this.addressBeanList=addressBeanList;

        gson = new Gson();
    }

    public void setDate(List<AddressBean> addressBeanList){
        this.addressBeanList=addressBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return addressBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.address_item, null);
            vh = new ViewHolder();
            vh.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            vh.tv_moren = (TextView) view.findViewById(R.id.tv_moren);
            vh.tv_edit = (TextView) view.findViewById(R.id.tv_edit);
            vh.tv_address = (TextView) view.findViewById(R.id.tv_address);
            vh.tv_head = (TextView) view.findViewById(R.id.tv_head);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        AddressBean addressBean=addressBeanList.get(pos);
        vh.tv_name.setText(addressBean.getContacts());
        vh.tv_head.setText(addressBean.getContacts().substring(0,1));
        vh.tv_phone.setText(addressBean.getContactsPhoneNumber());
        if(addressBean.getIsDefault()==1){
            vh.tv_moren.setVisibility(View.VISIBLE);
        }else{
            vh.tv_moren.setVisibility(View.GONE);
        }
        vh.tv_address.setText(addressBean.getAddress());
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
        return addressBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_head;
        TextView tv_name;
        TextView tv_phone;
        TextView tv_moren;
        TextView tv_edit;
        TextView tv_address;
    }

}
