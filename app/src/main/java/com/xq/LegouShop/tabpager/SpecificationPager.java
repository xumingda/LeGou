package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.GoodInfoBean;
import com.xq.LegouShop.bean.SpecificationsBean;
import com.xq.LegouShop.util.DialogUtils;

import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class SpecificationPager extends ViewTabBasePager {
    // 分类listview

    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private GoodInfoBean goodInfoBean;
    public List<SpecificationsBean> specificationsBeanList;
    public SpecificationPager(Context context,GoodInfoBean goodInfoBean,List<SpecificationsBean> specificationsBeanList) {
        super(context);
        this.goodInfoBean=goodInfoBean;
        this.specificationsBeanList=specificationsBeanList;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.specification_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);




    }



}