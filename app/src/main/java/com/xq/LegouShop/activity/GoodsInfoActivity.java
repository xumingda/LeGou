package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.base.ViewTabBasePager;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.callback.GoodAddCartCallBack;
import com.xq.LegouShop.protocol.AddUserCartProtocol;
import com.xq.LegouShop.protocol.CollectionGoodsProtocol;
import com.xq.LegouShop.protocol.GetCodeProtocol;
import com.xq.LegouShop.protocol.GetGoodInfoProtocol;
import com.xq.LegouShop.request.GetCodeRequest;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.GetGoodInfoResponse;
import com.xq.LegouShop.tabpager.DetailsPager;
import com.xq.LegouShop.tabpager.EvaluatePager;
import com.xq.LegouShop.tabpager.SpecificationPager;
import com.xq.LegouShop.tabpager.TabHomePager;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.NoScrollViewPager;
import com.xq.LegouShop.weiget.SelectGoodsSpecificationuwindow;
import com.xq.LegouShop.weiget.SelectPopuwindow;
import com.xq.LegouShop.weiget.TabSlidingIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//商品详情
public class GoodsInfoActivity extends BaseActivity  implements  View.OnClickListener , GoodAddCartCallBack {

    private LayoutInflater mInflater;
    private View rootView;

//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private GetGoodInfoResponse getGoodInfoResponse ;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back,view_update;
    private ImageLoader imageLoader;
    private List<View> dots; // 图片标题正文的那些点
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private List<ImageView> imageViews; // 滑动的图片集合
    private ImageView imageView;
    private ViewPager vp;
    private LinearLayout ll_pager;
    private View v_dot0;
    private View v_dot1;
    private View v_dot2;
    private View v_dot3;
    private View v_dot4;
    private List<String> adsBeanList;
    private boolean isopen;
//    /**
//     * 关注标题指示器
//     */
//    private TabSlidingIndicator titleIndicator;
//    /**
//     * 关注的内容viewpager
//     */
////    private NoScrollViewPager vpContent;
//    /**
//     * 关注标题
//     */
//    private List<String> pagerTitles;
    /**
     * 存放商场,品牌,喜欢我，我喜欢页面的集合
     */
//    private List<ViewTabBasePager> concernBasePagerList;
    private Button left, right,last;
    private RelativeLayout rl_main;
    private SelectGoodsSpecificationuwindow selectGoodsSpecificationuwindow;
    private TextView tv_addcar,tv_buy,tv_dec,tv_price,tv_old_price,tv_num,tv_collect,tv_home;
    private String goodsId,goodsCaseId,buyCount;
    private WebView wv_desc;
    //css显示图片样式
    private String CSS_STYPE = "<head><style>img{max-width:340px !important;}</style></head>";
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_good_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){
        goodsId=getIntent().getStringExtra("goodsId");
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(GoodsInfoActivity.this, false);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        wv_desc=(WebView)findViewById(R.id.wv_desc);
        view_update=(View)findViewById(R.id.view_update);
        view_back=(View)findViewById(R.id.view_back);
        left=(Button)findViewById(R.id.left);
        right=(Button)findViewById(R.id.right);
        last=(Button)findViewById(R.id.last);
        tv_collect=(TextView) findViewById(R.id.tv_collect);
        tv_price=(TextView) findViewById(R.id.tv_price);
        tv_old_price=(TextView) findViewById(R.id.tv_old_price);
        tv_num=(TextView) findViewById(R.id.tv_num);
        tv_dec=(TextView) findViewById(R.id.tv_dec);
        tv_addcar=(TextView) findViewById(R.id.tv_addcar);
        tv_buy=(TextView)findViewById(R.id.tv_buy);
        tv_home=(TextView)findViewById(R.id.tv_home);
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        view_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(GoodsInfoActivity.this, UpdatePersonalInfoActivity.class);
//                UIUtils.startActivityNextAnim(intent);
            }
        });
        tv_collect.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.vp);
        v_dot0 = (View) findViewById(R.id.v_dot0);
        v_dot1 = (View) findViewById(R.id.v_dot1);
        v_dot2 = (View)findViewById(R.id.v_dot2);
        v_dot3 = (View) findViewById(R.id.v_dot3);
        v_dot4 = (View) findViewById(R.id.v_dot4);
        ll_pager = (LinearLayout)findViewById(R.id.ll_pager);
        adsBeanList=new ArrayList<>();




//        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
//        vpContent=(NoScrollViewPager)findViewById(R.id.vp_goods);

        tv_home.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        last.setOnClickListener(this);
        left.setEnabled(false);
        right.setEnabled(true);
        last.setEnabled(true);

        tv_addcar.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        getGoodInfo();
    }

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        concernBasePagerList.get(position).initData();
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_collect:{
                collectionGoods();
                break;
            }
            case R.id.tv_buy:{
                if(getGoodInfoResponse.data.isUserSet==1||(getGoodInfoResponse.dataList!=null&&getGoodInfoResponse.dataList.size()>0)){
                    if (right.isEnabled()) {
                        right.setEnabled(false);
                        left.setEnabled(true);
                        last.setEnabled(true);
                        wv_desc.setVisibility(View.GONE);
                        selectGoodsSpecificationuwindow = new SelectGoodsSpecificationuwindow(UIUtils.getActivity(),"筛选条件",getGoodInfoResponse.data,getGoodInfoResponse.dataList,this);
                        selectGoodsSpecificationuwindow.showPopupWindow(rl_main);
                    }
                }else{
                    Intent intent=new Intent(this,BuyGoodActivity.class);
                    List<CartBean> orderGoodBeanList = new ArrayList<CartBean>();
                    CartBean cartBean = new CartBean();
                    cartBean.setGoodsName(getGoodInfoResponse.data.goodsName);
                    if(getGoodInfoResponse.dataList!=null&&getGoodInfoResponse.dataList.size()>0){
                        cartBean.setGoodsGroupValues(getGoodInfoResponse.dataList.get(0).goodsGroupName);
                    }
                    cartBean.setPic(getGoodInfoResponse.data.getPic());
                    cartBean.setGoodsId(Integer.parseInt(getGoodInfoResponse.data.getId()));
                    cartBean.setBuyCount("1");
                    cartBean.setSalePrice(getGoodInfoResponse.data.getSalePrice());
                    orderGoodBeanList.add(cartBean);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderGoodBeanList", (Serializable) orderGoodBeanList);
                    intent.putExtras(bundle);
                    intent.putExtra("total_price", Double.parseDouble(getGoodInfoResponse.data.getSalePrice()));
                    UIUtils.startActivityNextAnim(intent);
                }

                break;
            }
            case R.id.tv_addcar:{
                selectGoodsSpecificationuwindow = new SelectGoodsSpecificationuwindow(UIUtils.getActivity(),"筛选条件",getGoodInfoResponse.data,getGoodInfoResponse.dataList,this);
                selectGoodsSpecificationuwindow.showPopupWindow(rl_main);
                break;
            }
            case R.id.left:
                if (left.isEnabled()) {
                    left.setEnabled(false);
                    right.setEnabled(true);
                    last.setEnabled(true);
                    wv_desc.setVisibility(View.VISIBLE);
                    if(selectGoodsSpecificationuwindow!=null) {
                        selectGoodsSpecificationuwindow.dismissPopupWindow();
                    }
                }
                break;
            case R.id.right:
                if (right.isEnabled()) {
                    right.setEnabled(false);
                    left.setEnabled(true);
                    last.setEnabled(true);
                    wv_desc.setVisibility(View.GONE);
                    selectGoodsSpecificationuwindow = new SelectGoodsSpecificationuwindow(UIUtils.getActivity(),"筛选条件",getGoodInfoResponse.data,getGoodInfoResponse.dataList,this);
                    selectGoodsSpecificationuwindow.showPopupWindow(rl_main);
                }
                break;
            case R.id.last: {
                if (last.isEnabled()) {
                    last.setEnabled(false);
                    left.setEnabled(true);
                    right.setEnabled(true);
                    wv_desc.setVisibility(View.GONE);
                    if(selectGoodsSpecificationuwindow!=null) {
                        selectGoodsSpecificationuwindow.dismissPopupWindow();
                    }

                }
                break;
            }
        }
    }

//    private class ConcernInfoPagerAdapter extends PagerAdapter {
//        @Override
//        public CharSequence getPageTitle(int position) {
//
//            return pagerTitles.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return pagerTitles.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ViewTabBasePager concernBasePager = concernBasePagerList
//                    .get(position);
//            View rootView = concernBasePager.getRootView();
//            container.removeView(rootView);
//            container.addView(rootView);
//            return rootView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//    }
    public void setImageViews(){
        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            if (i < 5) {
                imageView = new ImageView(this);
//                ViewListener viewListener = new ViewListener(i);
//                imageView.setOnClickListener(viewListener);
                imageLoader.displayImage(adsBeanList.get(i), imageView, PictureOption.getSimpleOptions());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageViews.add(imageView);
            }

        }


        dots = new ArrayList<View>();
        if (adsBeanList.size() > 5) {
            dots.add(v_dot0);
            dots.add(v_dot1);
            dots.add(v_dot2);
            dots.add(v_dot3);
            dots.add(v_dot4);
        } else {
            switch (adsBeanList.size()) {
                case 1:
                    v_dot0.setVisibility(View.GONE);
                    v_dot1.setVisibility(View.GONE);
                    v_dot2.setVisibility(View.GONE);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 2:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    v_dot2.setVisibility(View.GONE);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 3:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    v_dot3.setVisibility(View.GONE);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 4:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    dots.add(v_dot3);
                    v_dot4.setVisibility(View.GONE);
                    break;
                case 5:
                    dots.add(v_dot0);
                    dots.add(v_dot1);
                    dots.add(v_dot2);
                    dots.add(v_dot3);
                    dots.add(v_dot4);
                    break;
                default:
                    break;
            }
        }


        vp.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        vp.setOnPageChangeListener(new MyPageChangeListener());
        //设置初始选择的页面，要在setadapter之后
        vp.setCurrentItem(currentItem);
        if (!isopen) {
            startScrollTask();
            isopen = true;
        }
    }

    @Override
    public void setData(String goodsCaseId, String buyCount) {
        this.goodsCaseId=goodsCaseId;
        this.buyCount=buyCount;
        addCart();
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (adsBeanList.size() > 5) {
                return 5;
            } else {
                return adsBeanList.size();
            }
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
//            currentItem = position;
//            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
//            oldPosition = position;
            LogUtils.e("dots.size()" + dots.size());
            if (dots.size() > 0) {
                currentItem = position % imageViews.size();
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                dots.get(position % imageViews.size()).setBackgroundResource(R.drawable.dot_focused);
                oldPosition = position % imageViews.size();
            }

        }

        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (vp) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    public void startScrollTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);
    }
    //商品详情
    public void getGoodInfo() {
        loadingDialog.show();
        GetGoodInfoProtocol getGoodInfoProtocol = new GetGoodInfoProtocol();
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        String url = getGoodInfoProtocol.getApiFun();
        getCodeRequest.map.put("goodsId", goodsId);
        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getGoodInfoResponse = gson.fromJson(json, GetGoodInfoResponse.class);
                LogUtils.e("getGoodInfoResponse:" + getGoodInfoResponse.toString());
                if (getGoodInfoResponse.code.equals("0")) {
                    String last=getGoodInfoResponse.data.pics;
                    LogUtils.e("图片："+convertStrToArray(getGoodInfoResponse.data.pics)[0]);
                    for (int i=0;i<convertStrToArray(last).length;i++){
                        adsBeanList.add("http://qiniu.lelegou.pro/"+convertStrToArray(last)[i]);
                    }
                    tv_dec.setText(getGoodInfoResponse.data.goodsName);
                    String string="¥"+getGoodInfoResponse.data.getOriginalPrice();
                    SpannableString sp = new SpannableString(string);
                    sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_old_price.setText(sp);
                    tv_price.setText("¥"+getGoodInfoResponse.data.getSalePrice());
                    tv_num.setText("销量："+getGoodInfoResponse.data.getSalesVolume());
                    wv_desc.loadDataWithBaseURL(null, CSS_STYPE + getGoodInfoResponse.data.details, "text/html", "utf-8", null);
                    setImageViews();
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(GoodsInfoActivity.this,
                            getGoodInfoResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    //收藏商品
    public void collectionGoods() {
        loadingDialog.show();
        CollectionGoodsProtocol collectionGoodsProtocol = new CollectionGoodsProtocol();

        String url = collectionGoodsProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("goodsId", goodsId);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                FindPwdResponse findPwdResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("findPwdResponse:" + findPwdResponse.toString());
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsInfoActivity.this,
                        findPwdResponse.msg);


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(GoodsInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    //加入购物车
    public void addCart() {
        loadingDialog.show();
        AddUserCartProtocol addUserCartProtocol = new AddUserCartProtocol();

        String url = addUserCartProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("goodsId", goodsId);
        if(!TextUtils.isEmpty(goodsCaseId)) {
            params.put("goodsCaseId", String.valueOf(goodsCaseId));
        }
        params.put("buyCount", buyCount);
        LogUtils.e("addcart:" + params.toString());
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                FindPwdResponse findPwdResponse = gson.fromJson(json, FindPwdResponse.class);
                LogUtils.e("addcart:" + findPwdResponse.toString());
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsInfoActivity.this,
                        findPwdResponse.msg);


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(GoodsInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

}
