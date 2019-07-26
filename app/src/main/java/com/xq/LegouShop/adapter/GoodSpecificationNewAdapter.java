package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.SelectBean;
import com.xq.LegouShop.bean.SpecificationsBean;
import com.xq.LegouShop.response.GetUserCollectionListResponse;
import com.xq.LegouShop.util.DateUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class GoodSpecificationNewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<SpecificationsBean> dataList;
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



    public GoodSpecificationNewAdapter(Context context, List<SpecificationsBean> dataList) {
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
    public void setDataList(List<SpecificationsBean> dataList){
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
        return dataList.get(groupPosition).attrList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).attrList.get(childPosition);
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
                    R.layout.specifcation_title_item, null);
            groupHolder = new GroupHolder();
            // groupHolder.img = (ImageView) convertView
            // .findViewById(R.id.img);
            groupHolder.tv_goodsGroupName=(TextView) convertView.findViewById(R.id.tv_goodsGroupName);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.tv_goodsGroupName.setText(dataList.get(groupPosition).goodsGroupName);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemHolder itemHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_lable, null);
            itemHolder = new ItemHolder();
            itemHolder.tv_goodsGroupNameValue= (TextView) convertView.findViewById(R.id.tv_goodsGroupNameValue);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        itemHolder.tv_goodsGroupNameValue.setText(dataList.get(groupPosition).attrList.get(childPosition).goodsGroupNameValue);
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
        TextView tv_goodsGroupName;

    }

    class ItemHolder {
        TextView tv_goodsGroupNameValue;

    }

}


