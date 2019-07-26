package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.SelectBean;
import com.xq.LegouShop.response.GetUserCollectionListResponse;
import com.xq.LegouShop.util.DateUtils;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.StringUtils;
import com.xq.LegouShop.util.UIUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class ShopCollectionAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GetUserCollectionListResponse.DataList> dataList;
    private Dialog loadingDialog;
    private String url;

    private Handler handler;
    private String comment_id;
    private  GroupHolder groupHolder = null;
    private boolean isShow;
    private HashMap<Integer,SelectBean> hashMap=new HashMap<>();
//    HashMap<Integer,Boolean> isSelectmap=new HashMap<>();


    public HashMap<Integer, SelectBean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, SelectBean> hashMap) {
        this.hashMap = hashMap;
        notifyDataSetChanged();
    }



    public ShopCollectionAdapter(Context context, List<GetUserCollectionListResponse.DataList> dataList) {
        this.context = context;
        this.dataList = dataList;
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance()
                    .init(ImageLoaderConfiguration.createDefault(UIUtils
                            .getContext()));
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                notifyDataSetChanged();
                super.handleMessage(msg);
            }
        };
    }
    public void setDataList(List<GetUserCollectionListResponse.DataList> dataList){
        this.dataList = dataList;
    }
    public void refresh() {
        handler.sendMessage(new Message());
    }
    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(groupPosition).userCollectionList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).userCollectionList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.collection_shop_item, null);
            groupHolder = new GroupHolder();
            // groupHolder.img = (ImageView) convertView
            // .findViewById(R.id.img);
            groupHolder.tv_shop_name=(TextView) convertView.findViewById(R.id.tv_shop_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.tv_shop_name.setText("—" + dataList.get(groupPosition).shopName+"—");

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemHolder itemHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.collection_item, null);
            itemHolder = new ItemHolder();
            itemHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            itemHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            itemHolder.mCbCheckbox= (CheckBox) convertView.findViewById(R.id.cb_checkbox);
            itemHolder.iv_pic= (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
//        itemHolder.tv_user_name.setText(dataList.get(groupPosition).userCollectionList.get(childPosition).);
        itemHolder.tv_user_name.setText(dataList.get(groupPosition).userCollectionList.get(childPosition).goodsName);
        if(dataList.get(groupPosition).userCollectionList.get(childPosition).goodsGroupValueId1!=0){
            itemHolder.tv_info.setText(DateUtils.milliToSimpleDate(Long.parseLong(dataList.get(groupPosition).userCollectionList.get(childPosition).goodsGroupNameValue1)));
        }

        ImageLoader.getInstance().displayImage("http://qiniu.lelegou.pro/"+dataList.get(groupPosition).userCollectionList.get(childPosition).pic,itemHolder.iv_pic,PictureOption.getSimpleOptions());
        if(isShow){
            itemHolder.mCbCheckbox.setVisibility(View.VISIBLE);
        }else {
            itemHolder.mCbCheckbox.setVisibility(View.GONE);
        }
        SelectBean selectBean=hashMap.get(groupPosition);
        if(selectBean!=null&&Integer.parseInt(selectBean.id)==groupPosition){
            LogUtils.e("groupPosition:selectBeanid:"+selectBean.id+"   groupPosition:"+groupPosition+"  childPosition:"+selectBean.hashMap.get(childPosition)+"    :"+childPosition);
            if(selectBean.hashMap.get(childPosition)!=null) {
                itemHolder.mCbCheckbox.setChecked(selectBean.hashMap.get(childPosition));
            }else{
                itemHolder.mCbCheckbox.setChecked(false);
            }
        }
        return convertView;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
        notifyDataSetChanged();
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupHolder {
        TextView tv_shop_name;

    }

    class ItemHolder {
        TextView tv_user_name;
        TextView tv_info;
        ImageView iv_pic;
        CheckBox mCbCheckbox;
    }

}


