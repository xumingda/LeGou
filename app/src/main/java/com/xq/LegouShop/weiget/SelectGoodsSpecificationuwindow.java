package com.xq.LegouShop.weiget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GoodSpecificationAdapter;
import com.xq.LegouShop.adapter.SelectShopAdapter;
import com.xq.LegouShop.bean.GoodInfoBean;
import com.xq.LegouShop.bean.GoodNumAndPriceBean;
import com.xq.LegouShop.bean.SpecificationsBean;
import com.xq.LegouShop.callback.GoodAddCartCallBack;
import com.xq.LegouShop.callback.GoodNumAndPriceCallBack;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class SelectGoodsSpecificationuwindow extends PopupWindow implements GoodNumAndPriceCallBack {

    private View conentView;
    private Activity context;
    private ListView lv;
    private ImageView iv_cancle,iv_pic,iv_add,iv_reduce;
    private TextView tv_price,tv_num;
    private GoodSpecificationAdapter goodSpecificationAdapter;
    private ImageLoader imageLoader;
    private Button btn_commit;
    private String goodsCaseId,buyCount;
    private EditText et_imput_num;
    //售价
    private String salePrice;
    /**
     * @param context 上下文
     * @param string 获取到未打开列表时显示的值
      */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public SelectGoodsSpecificationuwindow(final Activity context, final String string, final GoodInfoBean goodInfoBean, List<SpecificationsBean> specificationsBeanList,final GoodAddCartCallBack goodAddCartCallBack) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(context)));
        conentView = inflater.inflate(R.layout.select_good_specification, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //    this.setWidth(view.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 刷新状态
        this.update();
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
        //解决软键盘挡住弹窗问题
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        btn_commit=conentView.findViewById(R.id.btn_commit);
        iv_cancle=conentView.findViewById(R.id.iv_cancle);
        et_imput_num=conentView.findViewById(R.id.et_imput_num);
        iv_pic=conentView.findViewById(R.id.iv_pic);
        tv_num=conentView.findViewById(R.id.tv_num);
        tv_price=conentView.findViewById(R.id.tv_price);
        iv_add=conentView.findViewById(R.id.iv_add);
        iv_reduce=conentView.findViewById(R.id.iv_reduce);
        lv=conentView.findViewById(R.id.lv_spec);

        if(goodSpecificationAdapter==null){
           goodSpecificationAdapter=new GoodSpecificationAdapter(context,specificationsBeanList,goodInfoBean.id,this);
           lv.setAdapter(goodSpecificationAdapter);
        }else{
            goodSpecificationAdapter.setDate(specificationsBeanList);
            goodSpecificationAdapter.notifyDataSetChanged();
        }
        tv_price.setText("￥"+goodInfoBean.salePrice);
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+goodInfoBean.pic, iv_pic, PictureOption.getSimpleOptions());
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCount=et_imput_num.getText().toString();
                goodAddCartCallBack.setData(goodsCaseId,buyCount);
                dismiss();
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goods_num = et_imput_num.getText().toString();
                int num=Integer.parseInt(goods_num);
                num = num + 1;
                et_imput_num.setText(String.valueOf(num));
                if(TextUtils.isEmpty(salePrice)){
                    salePrice=goodInfoBean.getSalePrice();
                }
                tv_price.setText("¥"+Double.parseDouble(et_imput_num.getText().toString())*Double.parseDouble(salePrice));
            }
        });
        iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goods_num = et_imput_num.getText().toString();
                int num=Integer.parseInt(goods_num);
                num = num - 1;
                if(num==0){
                    return;
                }
                if(TextUtils.isEmpty(salePrice)){
                    salePrice=goodInfoBean.getSalePrice();
                }
                tv_price.setText("¥"+Double.parseDouble(et_imput_num.getText().toString())*Double.parseDouble(salePrice));
                et_imput_num.setText(String.valueOf(num));
            }
        });
    }

    //给下拉列表的设置标题，增加复用性
    public void setTitleText(String str){

    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
//              this.showAsDropDown(parent);
            // this.showAsDropDown(parent,0,10);
            this.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER, 0, 0);

            darkenBackground(0.4f);//弹出时让页面背景回复给原来的颜色降低透明度，让背景看起来变成灰色
        }
    }

    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

        /**
         * 关闭popupWindow
         */
    public void dismissPopupWindow() {
        this.dismiss();
        darkenBackground(1f);//关闭时让页面背景回复为原来的颜色

    }
    /**
     * 改变背景颜色，主要是在PopupWindow弹出时背景变化，通过透明度设置
     */
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void setData(GoodNumAndPriceBean goodNumAndPriceBean) {

        imageLoader.displayImage("http://qiniu.lelegou.pro/"+goodNumAndPriceBean.getPic(), iv_pic, PictureOption.getSimpleOptions());
        tv_price.setText("￥"+goodNumAndPriceBean.salePrice);
        salePrice=goodNumAndPriceBean.salePrice;
        tv_num.setText("库存:"+goodNumAndPriceBean.num);
        goodsCaseId = goodNumAndPriceBean.goodsCaseId;

        LogUtils.e("goodsCaseId:"+goodsCaseId);

    }

}