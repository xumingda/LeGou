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
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.CircleImageView;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class HomeClassifyAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<GetCategoryListResponse.DataList> orderBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    public HomeClassifyAdapter(Context context, List<GetCategoryListResponse.DataList> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        gson = new Gson();
    }

    public void setDate(List<GetCategoryListResponse.DataList> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.home_classify_item, null);
            vh = new ViewHolder();
            vh.tv_classify_name = (TextView) view.findViewById(R.id.tv_classify_name);
            vh.iv_classify = (CircleImageView) view.findViewById(R.id.iv_classify);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GetCategoryListResponse.DataList orderBean=orderBeanList.get(pos);
        if(pos==orderBeanList.size()-1){
            vh.tv_classify_name.setText("更多");
            vh.iv_classify.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_more_good));
        }else{
            vh.tv_classify_name.setText(orderBean.getCategoryName());
            imageLoader.displayImage("http://qiniu.lelegou.pro/" + orderBean.pic, vh.iv_classify, PictureOption.getSimpleOptions());
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_classify_name;
        CircleImageView iv_classify;

    }

}
