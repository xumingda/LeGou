package com.xq.LegouShop.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.util.PictureOption;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class GoodsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<GoodsBean> goodsBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    public GoodsAdapter(Context context, List<GoodsBean> goodsBeanList){
        mContext=context;
        this.goodsBeanList=goodsBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        gson = new Gson();
    }

    public void setDate( List<GoodsBean> goodsBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.goods_item, null);
            vh = new ViewHolder();
            vh.tv_price = (TextView) view.findViewById(R.id.tv_price);
            vh.tv_old_price=(TextView)view.findViewById(R.id.tv_old_price);
            vh.iv_pic=(ImageView) view.findViewById(R.id.iv_pic);
            vh.tv_title=(TextView)view.findViewById(R.id.tv_title);
            vh.tv_num=(TextView)view.findViewById(R.id.tv_num);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GoodsBean goodsBean=goodsBeanList.get(pos);
//        if(orderBean.getPhoneNumber()!=null&&orderBean.getPhoneNumber().length()>10){
//            vh.tv_phone.setText(orderBean.getPhoneNumber().substring(0,3)+"****"+orderBean.getPhoneNumber().substring(7,11));
//        }else{
//            vh.tv_phone.setText(orderBean.getPhoneNumber());
//        }
//
//        vh.tv_price.setText("¥"+orderBean.getNeedMoney());
//        vh.tv_time.setText(orderBean.getCreateTime());
        String string="¥"+goodsBean.getOriginalPrice();
        SpannableString sp = new SpannableString(string);
        sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.tv_old_price.setText(sp);
        vh.tv_price.setText("¥"+goodsBean.getSalePrice());
        vh.tv_title.setText(goodsBean.getGoodsName());
        vh.tv_num.setText("销量："+goodsBean.getSalesVolume());
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+goodsBean.getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_title;
        TextView tv_price;
        TextView tv_num;
        TextView tv_old_price;
        ImageView iv_pic;
    }

}
