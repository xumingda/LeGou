package com.xq.LegouShop.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.SpecificationsBean;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class GoodSpecificationItemAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<SpecificationsBean.AttrBean> orderBeanList;
    private Context mContext;
    private List<String> selectedLables;
    private Gson gson;
    private HashMap<Integer,Boolean> hashMap=new HashMap();

    public HashMap<Integer, Boolean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, Boolean> hashMap) {
        this.hashMap = hashMap;
        notifyDataSetChanged();
    }

    public GoodSpecificationItemAdapter(Context context, List<SpecificationsBean.AttrBean> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;

        gson = new Gson();
    }

    public List<String> getSelectedLables() {
        return selectedLables;
    }

    public void setDate(List<SpecificationsBean.AttrBean> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lable, null);
            vh = new ViewHolder();
            vh.tv_goodsGroupNameValue = (TextView) view.findViewById(R.id.tv_goodsGroupNameValue);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        SpecificationsBean.AttrBean attrBean=orderBeanList.get(pos);
        if(hashMap.get(pos)!=null){
            if(hashMap.get(pos)){
                vh.tv_goodsGroupNameValue.setTextColor(UIUtils.getResources().getColor(R.color.tab_background));
                vh.tv_goodsGroupNameValue.setBackgroundColor(UIUtils.getColor(R.color.errorColor));
            }else{
                vh.tv_goodsGroupNameValue.setTextColor(UIUtils.getResources().getColor(R.color.text_black));
                vh.tv_goodsGroupNameValue.setBackgroundColor(UIUtils.getColor(R.color.text_color_gray));
            }
        }else{
            vh.tv_goodsGroupNameValue.setSelected(false);
        }
        vh.tv_goodsGroupNameValue.setText(attrBean.goodsGroupNameValue);
//        //获取选中的标签
//        selectedLables = vh.lineBreakLayout.getSelectedLables();
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_goodsGroupNameValue;

    }

}
