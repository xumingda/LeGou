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
import com.xq.LegouShop.bean.PassBean;
import com.xq.LegouShop.bean.RewardBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class PassAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<PassBean> rewardBeanList;
    private Context mContext;

    private Gson gson;
    public PassAdapter(Context context, List<PassBean> rewardBeanList){
        mContext=context;
        this.rewardBeanList=rewardBeanList;

        gson = new Gson();
    }

    public void setDate(List<PassBean> rewardBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.pass_item, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        PassBean passBean=rewardBeanList.get(pos);
        //1表示200积分的pass卡，2表示500积分的pass卡，3表示1000积分的pass卡，4表示2000积分的pass卡
        switch (passBean.getType()){
            case 1:{
                vh.tv_title.setText("200积分的pass卡"+passBean.getCount()+"张");
                break;
            }
            case 2:{
                vh.tv_title.setText("500积分的pass卡"+passBean.getCount()+"张");
                break;
            }
            case 3:{
                vh.tv_title.setText("1000积分的pass卡"+passBean.getCount()+"张");
                break;
            }
            case 4:{
                vh.tv_title.setText("2000积分的pass卡"+passBean.getCount()+"张");
                break;
            }
        }


        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return rewardBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_title;

    }

}
