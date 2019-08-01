package com.xq.LegouShop.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.bean.UserBalanceLogBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class AccountStatementAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<UserBalanceLogBean> orderBeanList;
    private Context mContext;

    private Gson gson;
    public AccountStatementAdapter(Context context, List<UserBalanceLogBean> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;

        gson = new Gson();
    }

    public void setDate(List<UserBalanceLogBean> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.account_item, null);
            vh = new ViewHolder();
            vh.tv_other = (TextView) view.findViewById(R.id.tv_other);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) view.findViewById(R.id.tv_money);
            vh.iv_head = (ImageView) view.findViewById(R.id.iv_head);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        UserBalanceLogBean orderBean=orderBeanList.get(pos);
        //类型 1充值，2提现，3购物，4退款，5转去购物积分，6转去转换积分，7转换积分转来，8拼团中奖
        switch (orderBean.getType()){
            case 1:{
                vh.tv_name.setText("充值");
                break;
            }
            case 2:{
                vh.tv_name.setText("提现");
                break;
            }
            case 3:{
                vh.tv_name.setText("购物");
                break;
            }
            case 4:{
                vh.tv_name.setText("退款");
                break;
            }
            case 5:{
                vh.tv_name.setText("转去购物积分");
                break;
            }
            case 6:{
                vh.tv_name.setText("转去转换积分");
                break;
            }
            case 7:{
                vh.tv_name.setText("转换积分转来");
                break;
            }
            case 8:{
                vh.tv_name.setText("拼团中奖");
                break;
            }
        }
        vh.tv_money.setText(orderBean.getMoney());
        vh.tv_time.setText(orderBean.getCreateTime());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }


    class ViewHolder {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_other;
        TextView tv_time;
        TextView tv_money;
    }

}
