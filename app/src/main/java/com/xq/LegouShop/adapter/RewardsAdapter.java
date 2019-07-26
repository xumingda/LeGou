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
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.bean.RewardBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class RewardsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<RewardBean> rewardBeanList;
    private Context mContext;

    private Gson gson;
    public RewardsAdapter(Context context, List<RewardBean> rewardBeanList){
        mContext=context;
        this.rewardBeanList=rewardBeanList;

        gson = new Gson();
    }

    public void setDate(List<RewardBean> rewardBeanList){
        this.rewardBeanList=rewardBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return rewardBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.reward_item, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        RewardBean rewardBean=rewardBeanList.get(pos);
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
        return rewardBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_time;
        TextView tv_title;

    }

}
