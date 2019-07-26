package com.xq.LegouShop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodsInfoActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.bean.SpecificationsBean;
import com.xq.LegouShop.callback.GoodNumAndPriceCallBack;
import com.xq.LegouShop.protocol.CollectionGoodsProtocol;
import com.xq.LegouShop.protocol.GetGoodsNumAndPriceProtocol;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetGoodsNumAndPriceResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.LineBreakLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class GoodSpecificationAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<SpecificationsBean> orderBeanList;
    private Context mContext;
    private List<String> selectedLables;
    private Gson gson;
    private HashMap<Integer,Boolean> hashMap=new HashMap();
    private GoodSpecificationItemAdapter goodSpecificationItemAdapter;
    private Dialog loadingDialog;
    private String goodsId;
    private String goodsGroupValueId;
    private GoodNumAndPriceCallBack goodNumAndPriceCallBack;
    public GoodSpecificationAdapter(Context context, List<SpecificationsBean> orderBeanList,String goodsId,GoodNumAndPriceCallBack goodNumAndPriceCallBack){
        mContext=context;
        this.orderBeanList=orderBeanList;
        this.goodsId=goodsId;
        this.goodNumAndPriceCallBack=goodNumAndPriceCallBack;
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        gson = new Gson();
    }

    public List<String> getSelectedLables() {
        return selectedLables;
    }

    public void setDate(List<SpecificationsBean> orderBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.specifcation_item, null);
            vh = new ViewHolder();
            vh.tv_goodsGroupName = (TextView) view.findViewById(R.id.tv_goodsGroupName);
            vh.gv = (GridView) view.findViewById(R.id.gv_spec);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final SpecificationsBean specificationsBean=orderBeanList.get(pos);
        List<String> lable = new ArrayList<>();
        for(int i=0;i<specificationsBean.attrList.size();i++){
            lable.add(specificationsBean.attrList.get(i).goodsGroupNameValue);
        }
        LogUtils.e("specificationsBeanList:"+lable.toString());

        vh.tv_goodsGroupName.setText(specificationsBean.goodsGroupName);
        if(goodSpecificationItemAdapter==null){
            goodSpecificationItemAdapter=new GoodSpecificationItemAdapter(mContext,specificationsBean.attrList);
            vh.gv.setAdapter(goodSpecificationItemAdapter);
        }else{
            goodSpecificationItemAdapter.setDate(specificationsBean.attrList);
            goodSpecificationItemAdapter.notifyDataSetChanged();
        }
        vh.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                HashMap<Integer,Boolean> hashMap=goodSpecificationItemAdapter.getHashMap();
                hashMap.put(0,true);
                for(int i=0;i<hashMap.size();i++){
                    hashMap.put(i,false);
                }
                hashMap.put(postion,true);
                goodsGroupValueId=specificationsBean.attrList.get(postion).id;
                getGoodsNumAndPrice();
                goodSpecificationItemAdapter.setHashMap(hashMap);
            }
        });
        //默认选中第一
        if(specificationsBean.attrList.size()>0) {
            HashMap<Integer, Boolean> hashMap = goodSpecificationItemAdapter.getHashMap();
            hashMap.put(0, true);
            goodsGroupValueId = specificationsBean.attrList.get(0).id;
            getGoodsNumAndPrice();
            goodSpecificationItemAdapter.setHashMap(hashMap);
        }
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
        TextView tv_goodsGroupName;
        GridView  gv;

    }

    //查询库存和价格
    public void getGoodsNumAndPrice() {
        loadingDialog.show();
        GetGoodsNumAndPriceProtocol getGoodsNumAndPriceProtocol = new GetGoodsNumAndPriceProtocol();

        String url = getGoodsNumAndPriceProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("goodsId", goodsId);
        params.put("goodsGroupValueId", goodsGroupValueId);
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetGoodsNumAndPriceResponse getGoodsNumAndPriceResponse = gson.fromJson(json, GetGoodsNumAndPriceResponse.class);
                loadingDialog.dismiss();
                LogUtils.e("getGoodsNumAndPriceResponse:" + getGoodsNumAndPriceResponse.toString());
                if(getGoodsNumAndPriceResponse.code.equals("0")){
                    goodNumAndPriceCallBack.setData(getGoodsNumAndPriceResponse.data);
                }else {
                    DialogUtils.showAlertDialog(mContext,
                            getGoodsNumAndPriceResponse.msg);
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(mContext,
                        "登录超时，请重新登录！");
            }
        });
    }
}
