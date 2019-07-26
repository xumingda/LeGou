package com.xq.LegouShop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.util.PictureOption;

import java.util.List;

/**
 * author：wangzihang
 * date： 2017/8/8 19:15
 * desctiption：
 * e-mail：wangzihang@xiaohongchun.com
 */

public class HomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<GetCategoryListResponse.DataList> foodDatas;
    private ImageLoader imageLoader;
    public HomeItemAdapter(Context context, List<GetCategoryListResponse.DataList> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(context)));
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GetCategoryListResponse.DataList subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_category, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(subcategory.getCategoryName());
//        Uri uri = Uri.parse(subcategory.getImgURL());
//        viewHold.iv_icon.setImageURI(uri);
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+subcategory.getPic(), viewHold.iv_icon, PictureOption.getSimpleOptions());
        return convertView;


    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }

}
