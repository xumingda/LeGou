package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
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
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.LogisticsInfo;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.callback.OrderCallBack;
import com.xq.LegouShop.protocol.CancelOrderProtocol;
import com.xq.LegouShop.protocol.FinishOrderProtocol;
import com.xq.LegouShop.protocol.PayOrderProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class IogisticsInfoAdapter extends BaseAdapter {

    private String TAG="ShowAdapter";
    private List<LogisticsInfo.Info> orderBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    private Dialog loadingDialog;
    private int orderId;
    //取消0.1付款
    private int type;
    private Dialog dialog;

    public IogisticsInfoAdapter(Context context, List<LogisticsInfo.Info> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;

    }

    public void setDate(List<LogisticsInfo.Info> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.logistics_item, null);
            vh = new ViewHolder();

            vh.tv_info= (TextView) view.findViewById(R.id.tv_info);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final LogisticsInfo.Info orderBean=orderBeanList.get(pos);

        vh.tv_info.setText(orderBean.ftime+" "+orderBean.context);

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }




    class ViewHolder {

        TextView tv_info;

    }

}
