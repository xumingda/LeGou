package com.xq.LegouShop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodListsActivity;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.GridViewForScrollView;

import java.io.Serializable;
import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean.DataBean> foodDatas;

    public HomeAdapter(Context context, List<CategoryBean.DataBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        CategoryBean.DataBean dataBean = foodDatas.get(position);
        final List<GetCategoryListResponse.DataList> dataList = dataBean.getDataList();
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home, null);
            viewHold = new ViewHold();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        HomeItemAdapter adapter = new HomeItemAdapter(context, dataList);
//        viewHold.blank.setText(dataBean.getModuleTitle());
        viewHold.gridView.setAdapter(adapter);
        viewHold.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtils.e("点击:"+i);
                Intent intent=new Intent(context, GoodListsActivity.class);
                intent.putExtra("dataList",(Serializable)dataList);
                intent.putExtra("categoryId",dataList.get(i).id);
                UIUtils.startActivityNextAnim(intent);
            }
        });
        return convertView;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
        private TextView blank;
    }

}
