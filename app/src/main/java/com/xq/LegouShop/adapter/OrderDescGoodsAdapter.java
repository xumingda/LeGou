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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.util.PictureOption;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class OrderDescGoodsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<CartBean> goodsBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    public OrderDescGoodsAdapter(Context context, List<CartBean> goodsBeanList){
        mContext=context;
        this.goodsBeanList=goodsBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        gson = new Gson();
    }

    public void setDate( List<CartBean> goodsBeanList){
        this.goodsBeanList=goodsBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return goodsBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.order_desc_good_item, null);
            vh = new ViewHolder();
            vh.tv_price = (TextView) view.findViewById(R.id.tv_total_price);
            vh.iv_pic=(ImageView) view.findViewById(R.id.iv_good);
            vh.tv_good_dec=(TextView)view.findViewById(R.id.tv_good_dec);
            vh.tv_num=(TextView)view.findViewById(R.id.tv_good_num);
            vh.tv_good_name=(TextView)view.findViewById(R.id.tv_good_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        CartBean goodsBean=goodsBeanList.get(pos);
//        if(orderBean.getPhoneNumber()!=null&&orderBean.getPhoneNumber().length()>10){
//            vh.tv_phone.setText(orderBean.getPhoneNumber().substring(0,3)+"****"+orderBean.getPhoneNumber().substring(7,11));
//        }else{
//            vh.tv_phone.setText(orderBean.getPhoneNumber());
//        }
//
//        vh.tv_price.setText("¥"+orderBean.getNeedMoney());
        vh.tv_good_name.setText(goodsBean.getGoodsName());
        vh.tv_good_dec.setText(goodsBean.getGoodsGroupValues());
        vh.tv_price.setText("合计¥"+Double.parseDouble(goodsBean.getBuyCount())*Double.parseDouble(goodsBean.getSalePrice()));
        vh.tv_good_dec.setText(goodsBean.getGoodsGroupValues());
        vh.tv_num.setText("X："+goodsBean.getBuyCount());
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+goodsBean.getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_good_dec;
        TextView tv_price;
        TextView tv_num;
        TextView tv_good_name;
        ImageView iv_pic;
    }

}
