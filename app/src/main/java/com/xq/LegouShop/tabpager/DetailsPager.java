package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.util.DialogUtils;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class DetailsPager extends ViewTabBasePager {

    // 分类listview
    @ViewInject(R.id.wv_desc)
    private WebView wv_desc;
    private String goods_desc;
    //css显示图片样式
    private String CSS_STYPE = "<head><style>img{max-width:340px !}</style></head>";
    public DetailsPager(Context context, String goods_desc) {
        super(context);
        this.goods_desc = goods_desc;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.details_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        wv_desc.loadDataWithBaseURL(null, CSS_STYPE + goods_desc, "text/html", "utf-8", null);
    }



}