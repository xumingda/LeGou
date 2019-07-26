package com.xq.LegouShop.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GoodsInfoActivity;
import com.xq.LegouShop.activity.LogisticsActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.protocol.GetAdListProtocol;
import com.xq.LegouShop.protocol.GetGoodListProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.StringUtils;
import com.xq.LegouShop.util.UIUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class TabHomeAdapter extends BaseAdapter implements View.OnClickListener {

    private Dialog alertDialog;
    private String TAG = "HomeAdapter";
    private List<GoodsBean> goodsBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    //布局类型
    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_COUNT = 2;
    private List<View> dots; // 图片标题正文的那些点
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0; // 当前图片的索引号
    private List<ImageView> imageViews; // 滑动的图片集合
    private String url;
    private Dialog loadingDialog;
    private boolean isloaded;
    private ImageView imageView;
    public int type = 1;
    boolean isopen = false;
    private Gson gson;
    private GetAdListResponse getHomeResponse;

    public TabHomeAdapter(Context context, List<GoodsBean> goodsBeanList, Dialog loadingDialog) {
        mContext = context;
        this.loadingDialog = loadingDialog;
        this.goodsBeanList = goodsBeanList;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        gson = new Gson();
    }

    public void setLoading(boolean isloaded) {
        this.isloaded = isloaded;
    }

    public void setDate(List<GoodsBean> goodsBeanList) {
        this.goodsBeanList = goodsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return goodsBeanList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    ViewHolder vh;
    HomeHeadHolder homeHeadHolder;

    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub

        // 获取集合内的对象
        int type = getItemViewType(pos);
        if (view == null) {
            switch (type) {
                case TYPE_ONE:
                    view = View.inflate(UIUtils.getContext(),
                            R.layout.home_head_layout, null);
                    homeHeadHolder = new HomeHeadHolder();
                    homeHeadHolder.vp = (ViewPager) view.findViewById(R.id.vp);
                    homeHeadHolder.v_dot0 = (View) view.findViewById(R.id.v_dot0);
                    homeHeadHolder.v_dot1 = (View) view.findViewById(R.id.v_dot1);
                    homeHeadHolder.v_dot2 = (View) view.findViewById(R.id.v_dot2);
                    homeHeadHolder.v_dot3 = (View) view.findViewById(R.id.v_dot3);
                    homeHeadHolder.v_dot4 = (View) view.findViewById(R.id.v_dot4);
                    homeHeadHolder.ll_pager = (LinearLayout) view.findViewById(R.id.ll_pager);

                    homeHeadHolder.tv_price_one = (TextView) view.findViewById(R.id.tv_price_one);
                    homeHeadHolder.tv_old_price_one = (TextView) view.findViewById(R.id.tv_old_price_one);
                    homeHeadHolder.iv_pic_one = (ImageView) view.findViewById(R.id.iv_pic_one);
                    homeHeadHolder.tv_title_one = (TextView) view.findViewById(R.id.tv_title_one);
                    homeHeadHolder.tv_num_one = (TextView) view.findViewById(R.id.tv_num_one);

                    homeHeadHolder.tv_price_two = (TextView) view.findViewById(R.id.tv_price_two);
                    homeHeadHolder.tv_old_price_two = (TextView) view.findViewById(R.id.tv_old_price_two);
                    homeHeadHolder.iv_pic_two = (ImageView) view.findViewById(R.id.iv_pic_two);
                    homeHeadHolder.tv_title_two = (TextView) view.findViewById(R.id.tv_title_two);
                    homeHeadHolder.tv_num_two = (TextView) view.findViewById(R.id.tv_num_two);

                    homeHeadHolder.tv_price_three = (TextView) view.findViewById(R.id.tv_price_three);
                    homeHeadHolder.tv_old_price_three = (TextView) view.findViewById(R.id.tv_old_price_three);
                    homeHeadHolder.iv_pic_three = (ImageView) view.findViewById(R.id.iv_pic_three);
                    homeHeadHolder.tv_title_three = (TextView) view.findViewById(R.id.tv_title_three);
                    homeHeadHolder.tv_num_three = (TextView) view.findViewById(R.id.tv_num_three);

                    homeHeadHolder.tv_price_four = (TextView) view.findViewById(R.id.tv_price_four);
                    homeHeadHolder.tv_old_price_four = (TextView) view.findViewById(R.id.tv_old_price_four);
                    homeHeadHolder.iv_pic_four = (ImageView) view.findViewById(R.id.iv_pic_four);
                    homeHeadHolder.tv_title_four = (TextView) view.findViewById(R.id.tv_title_four);
                    homeHeadHolder.tv_num_four = (TextView) view.findViewById(R.id.tv_num_four);

                    homeHeadHolder.tv_price_five = (TextView) view.findViewById(R.id.tv_price_five);
                    homeHeadHolder.tv_old_price_five = (TextView) view.findViewById(R.id.tv_old_price_five);
                    homeHeadHolder.iv_pic_five = (ImageView) view.findViewById(R.id.iv_pic_five);
                    homeHeadHolder.tv_title_five = (TextView) view.findViewById(R.id.tv_title_five);
                    homeHeadHolder.tv_num_five = (TextView) view.findViewById(R.id.tv_num_five);

                    homeHeadHolder.tv_price_six = (TextView) view.findViewById(R.id.tv_price_six);
                    homeHeadHolder.tv_old_price_six = (TextView) view.findViewById(R.id.tv_old_price_six);
                    homeHeadHolder.iv_pic_six = (ImageView) view.findViewById(R.id.iv_pic_six);
                    homeHeadHolder.tv_title_six = (TextView) view.findViewById(R.id.tv_title_six);
                    homeHeadHolder.tv_num_six = (TextView) view.findViewById(R.id.tv_num_six);
                    homeHeadHolder.iv_home_left = (ImageView) view.findViewById(R.id.iv_home_left);
                    homeHeadHolder.iv_home_right_top = (ImageView) view.findViewById(R.id.iv_home_right_top);
                    homeHeadHolder.iv_home_right_bottom = (ImageView) view.findViewById(R.id.iv_home_right_bottom);

                    view.setTag(homeHeadHolder);
                    break;
                case TYPE_TWO:
                    view = LayoutInflater.from(mContext).inflate(R.layout.goods_new_item, null);
                    vh = new ViewHolder();
                    vh.tv_price = (TextView) view.findViewById(R.id.tv_price);
                    vh.tv_old_price = (TextView) view.findViewById(R.id.tv_old_price);
                    vh.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
                    vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    vh.tv_num = (TextView) view.findViewById(R.id.tv_num);
                    vh.rl_rihgt = (RelativeLayout) view.findViewById(R.id.rl_right);
                    vh.rl_left = (RelativeLayout) view.findViewById(R.id.rl_left);
                    vh.tv_price_right = (TextView) view.findViewById(R.id.tv_price_right);
                    vh.tv_old_price_right = (TextView) view.findViewById(R.id.tv_old_price_right);
                    vh.iv_pic_right = (ImageView) view.findViewById(R.id.iv_pic_right);
                    vh.tv_title_right = (TextView) view.findViewById(R.id.tv_title_right);
                    vh.tv_num_right = (TextView) view.findViewById(R.id.tv_num_right);
                    view.setTag(vh);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_ONE:
                    homeHeadHolder = (HomeHeadHolder) view.getTag();
                    break;
                case TYPE_TWO:
                    vh = (ViewHolder) view.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_ONE:
                if (!isloaded) {
                    getHome();
                }
//                homeHeadHolder.tv_search.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, SearchActivity.class);
//                        intent.putExtra("int_type", 0);
//                        intent.putExtra("type", 1);
//                        intent.putExtra("position", 2);
//                        UIUtils.startActivityNextAnim(intent);
//                    }
//                });
//                homeHeadHolder.tv_new.setOnClickListener(this);
//                homeHeadHolder.tv_classify.setOnClickListener(this);
//                homeHeadHolder.tv_hot.setOnClickListener(this);
                break;
            case TYPE_TWO:
//                GoodsBean goodsBean = goodsBeanList.get(pos);
//                String string = "¥" + goodsBean.getOriginalPrice();
//                SpannableString sp = new SpannableString(string);
//                sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                vh.tv_old_price.setText(sp);
//                vh.tv_price.setText("¥" + goodsBean.getSalePrice());
//                vh.tv_title.setText(goodsBean.getGoodsName());
//                vh.tv_num.setText("销量：" + goodsBean.getSalesVolume());
//                imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), vh.iv_pic, PictureOption.getSimpleOptions());

                if (goodsBeanList.size() % 2 == 0) {

                    vh.rl_rihgt.setVisibility(View.VISIBLE);
                    ClickListener clickListener1 = new ClickListener(pos * 2 + 0);
                    ClickListener clickListener = new ClickListener(pos * 2 + 1);
                    vh.rl_rihgt.setOnClickListener(clickListener);
                    vh.rl_left.setOnClickListener(clickListener1);
                    imageLoader.displayImage(goodsBeanList.get(pos * 2 + 0).getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
                    imageLoader.displayImage(goodsBeanList.get(pos * 2 + 1).getPic(), vh.iv_pic_right, PictureOption.getSimpleOptions());

                } else {
                    if (pos * 2 == goodsBeanList.size() - 1) {
                        vh.rl_rihgt.setVisibility(View.INVISIBLE);
                        ClickListener clickListener = new ClickListener(pos * 2 + 0);
                        vh.rl_left.setOnClickListener(clickListener);
                        imageLoader.displayImage(goodsBeanList.get(pos * 2 + 0).getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
                    } else {

                        ClickListener clickListener = new ClickListener(pos * 2 + 1);
                        ClickListener clickListener1 = new ClickListener(pos * 2 + 0);
                        vh.rl_rihgt.setOnClickListener(clickListener);
                        vh.rl_left.setOnClickListener(clickListener1);
                        imageLoader.displayImage(goodsBeanList.get(pos * 2 + 0).getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
                        imageLoader.displayImage(goodsBeanList.get(pos * 2 + 1).getPic(), vh.iv_pic_right, PictureOption.getSimpleOptions());
                    }
                }
                GoodsBean goodsBeanLeft = goodsBeanList.get(pos * 2 + 0);
                if (goodsBeanLeft != null) {
                    String string = "¥" + goodsBeanLeft.getOriginalPrice();
                    SpannableString sp = new SpannableString(string);
                    sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    vh.tv_old_price.setText(sp);
                    vh.tv_price.setText("¥" + goodsBeanLeft.getSalePrice());
                    vh.tv_title.setText(goodsBeanLeft.getGoodsName());
                    vh.tv_num.setText("销量：" + goodsBeanLeft.getSalesVolume());
                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBeanLeft.getPic(), vh.iv_pic, PictureOption.getSimpleOptions());
                }
                if (pos * 2 + 1 < goodsBeanList.size()) {
                    GoodsBean goodsBeanRight = goodsBeanList.get(pos * 2 + 1);
                    String string = "¥" + goodsBeanRight.getOriginalPrice();
                    SpannableString sp = new SpannableString(string);
                    sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    vh.tv_old_price_right.setText(sp);
                    vh.tv_price_right.setText("¥" + goodsBeanRight.getSalePrice());
                    vh.tv_title_right.setText(goodsBeanRight.getGoodsName());
                    vh.tv_num_right.setText("销量：" + goodsBeanRight.getSalesVolume());
                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBeanRight.getPic(), vh.iv_pic_right, PictureOption.getSimpleOptions());
                }
                break;
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }

    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public void onClick(View v) {
//        //渐变色
//        Shader shader = new LinearGradient(0, 0, 0, 60, Color.rgb(255, 72, 106
//        ), Color.rgb(254, 211, 70), Shader.TileMode.CLAMP);
//        Shader shader1 = new LinearGradient(0, 0, 0, 0, Color.BLACK, Color.BLACK, Shader.TileMode.CLAMP);
        switch (v.getId()) {


        }
    }

    public class HomeHeadHolder {
        ImageView iv_home_left;
        ImageView iv_home_right_top;
        ImageView iv_home_right_bottom;
        ViewPager vp;
        LinearLayout ll_pager;
        View v_dot0;
        View v_dot1;
        View v_dot2;
        View v_dot3;
        View v_dot4;
        ImageView iv_pic_one;
        TextView tv_title_one;
        TextView tv_price_one;
        TextView tv_num_one;
        TextView tv_old_price_one;

        ImageView iv_pic_two;
        TextView tv_title_two;
        TextView tv_price_two;
        TextView tv_num_two;
        TextView tv_old_price_two;

        ImageView iv_pic_three;
        TextView tv_title_three;
        TextView tv_price_three;
        TextView tv_num_three;
        TextView tv_old_price_three;

        ImageView iv_pic_four;
        TextView tv_title_four;
        TextView tv_price_four;
        TextView tv_num_four;
        TextView tv_old_price_four;

        ImageView iv_pic_five;
        TextView tv_title_five;
        TextView tv_price_five;
        TextView tv_num_five;
        TextView tv_old_price_five;

        ImageView iv_pic_six;
        TextView tv_title_six;
        TextView tv_price_six;
        TextView tv_num_six;
        TextView tv_old_price_six;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsBeanList.size() / 2 + goodsBeanList.size() % 2;
    }


    class ViewHolder {
        TextView tv_title;
        TextView tv_price;
        TextView tv_num;
        TextView tv_old_price;
        ImageView iv_pic;
        TextView tv_title_right;
        TextView tv_price_right;
        TextView tv_num_right;
        TextView tv_old_price_right;
        ImageView iv_pic_right;
        RelativeLayout rl_rihgt;
        RelativeLayout rl_left;
    }

    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ClickListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (getHomeResponse.dataList.size() > 5) {
                return 5;
            } else {
                return getHomeResponse.dataList.size();
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
            synchronized (homeHeadHolder.vp) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            homeHeadHolder.vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    public void startScrollTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);
    }

    public void getHome() {
        loadingDialog.show();
        GetAdListProtocol getHomeProtocol = new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(1));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    getHomeBanner();
                    getHomeBannerRightTop();
                    getHomeBannerRightBottom();
                    getGoodList();
                    getQualityLifeGoodList();
                    if (getHomeResponse.dataList.size() > 0) {
                        setheadDate(getHomeResponse.dataList);
                        homeHeadHolder.ll_pager.setVisibility(View.VISIBLE);
                    } else {
                        homeHeadHolder.ll_pager.setVisibility(View.INVISIBLE);
                    }

                } else {
                    if (getHomeResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void setheadDate(List<GetAdListResponse.DataList> adsBeanList) {
//        Shader shader = new LinearGradient(0, 0, 0, 60, Color.RED, Color.YELLOW, Shader.TileMode.CLAMP);
//        homeHeadHolder.tv_new.getPaint().setShader(shader);
//        homeHeadHolder.tv_new.postInvalidate();


        isloaded = true;
        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            if (i < 5) {
                imageView = new ImageView(mContext);
                ViewListener viewListener = new ViewListener(i);
                imageView.setOnClickListener(viewListener);
                imageLoader.displayImage("http://qiniu.lelegou.pro/" + adsBeanList.get(i).pic, imageView, PictureOption.getSimpleOptions());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViews.add(imageView);
            }

        }


        dots = new ArrayList<View>();
        if (adsBeanList.size() > 5) {
            dots.add(homeHeadHolder.v_dot0);
            dots.add(homeHeadHolder.v_dot1);
            dots.add(homeHeadHolder.v_dot2);
            dots.add(homeHeadHolder.v_dot3);
            dots.add(homeHeadHolder.v_dot4);
        } else {
            switch (adsBeanList.size()) {
                case 1:
                    homeHeadHolder.v_dot0.setVisibility(View.GONE);
                    homeHeadHolder.v_dot1.setVisibility(View.GONE);
                    homeHeadHolder.v_dot2.setVisibility(View.GONE);
                    homeHeadHolder.v_dot3.setVisibility(View.GONE);
                    homeHeadHolder.v_dot4.setVisibility(View.GONE);
                    break;
                case 2:
                    dots.add(homeHeadHolder.v_dot0);
                    dots.add(homeHeadHolder.v_dot1);
                    homeHeadHolder.v_dot2.setVisibility(View.GONE);
                    homeHeadHolder.v_dot3.setVisibility(View.GONE);
                    homeHeadHolder.v_dot4.setVisibility(View.GONE);
                    break;
                case 3:
                    dots.add(homeHeadHolder.v_dot0);
                    dots.add(homeHeadHolder.v_dot1);
                    dots.add(homeHeadHolder.v_dot2);
                    homeHeadHolder.v_dot3.setVisibility(View.GONE);
                    homeHeadHolder.v_dot4.setVisibility(View.GONE);
                    break;
                case 4:
                    dots.add(homeHeadHolder.v_dot0);
                    dots.add(homeHeadHolder.v_dot1);
                    dots.add(homeHeadHolder.v_dot2);
                    dots.add(homeHeadHolder.v_dot3);
                    homeHeadHolder.v_dot4.setVisibility(View.GONE);
                    break;
                case 5:
                    dots.add(homeHeadHolder.v_dot0);
                    dots.add(homeHeadHolder.v_dot1);
                    dots.add(homeHeadHolder.v_dot2);
                    dots.add(homeHeadHolder.v_dot3);
                    dots.add(homeHeadHolder.v_dot4);
                    break;
                default:
                    break;
            }
        }


        homeHeadHolder.vp.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        homeHeadHolder.vp.setOnPageChangeListener(new MyPageChangeListener());
        //设置初始选择的页面，要在setadapter之后
        homeHeadHolder.vp.setCurrentItem(currentItem);
        if (!isopen) {
            LogUtils.e("开");
            startScrollTask();
            isopen = true;
        }
    }


    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ViewListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;

        public ViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (getHomeResponse.dataList.get(position).type) {
                case 1: {
                    if (!TextUtils.isEmpty(getHomeResponse.dataList.get(position).url)) {
                        Intent intent = new Intent(mContext, LogisticsActivity.class);
                        intent.putExtra("logistics", getHomeResponse.dataList.get(position).url);
                        intent.putExtra("title", "详情");
                        intent.putExtra("type", 1);
                        UIUtils.startActivityNextAnim(intent);
                    }
                    break;
                }

                case 0: {
                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra("goodsId", getHomeResponse.dataList.get(position).goodsId);
                    UIUtils.startActivityNextAnim(intent);
                    break;
                }

            }
            LogUtils.e("广告:" + getHomeResponse.dataList.get(position).url);
        }
    }


    //每日精品
    public void getGoodList() {
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String, String> map = new HashMap<>();
        map.put("perDayGoods", "1");
        map.put("pageNo", "1");
        map.put("pageSize", "3");


        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.code.equals("0")) {
                    List<GoodsBean> goodsBeanList = getGoodsListResponse.dataList;
                    if (goodsBeanList.size() >= 3) {
                        GoodsBean goodsBean = goodsBeanList.get(0);
                        String string = "¥" + goodsBean.getOriginalPrice();
                        SpannableString sp = new SpannableString(string);
                        sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        homeHeadHolder.tv_old_price_one.setText(sp);
                        homeHeadHolder.tv_price_one.setText("¥" + goodsBean.getSalePrice());
                        homeHeadHolder.tv_title_one.setText(goodsBean.getGoodsName());
                        homeHeadHolder.tv_num_one.setText("销量：" + goodsBean.getSalesVolume());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), homeHeadHolder.iv_pic_one, PictureOption.getSimpleOptions());

                        GoodsBean goodsBean1 = goodsBeanList.get(1);
                        String string1 = "¥" + goodsBean1.getOriginalPrice();
                        SpannableString sp1 = new SpannableString(string1);
                        sp1.setSpan(new StrikethroughSpan(), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        homeHeadHolder.tv_old_price_two.setText(sp1);
                        homeHeadHolder.tv_price_two.setText("¥" + goodsBean1.getSalePrice());
                        homeHeadHolder.tv_title_two.setText(goodsBean1.getGoodsName());
                        homeHeadHolder.tv_num_two.setText("销量：" + goodsBean1.getSalesVolume());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean1.getPic(), homeHeadHolder.iv_pic_two, PictureOption.getSimpleOptions());

                        GoodsBean goodsBean2 = goodsBeanList.get(2);
                        String string2 = "¥" + goodsBean2.getOriginalPrice();
                        SpannableString sp2 = new SpannableString(string2);
                        sp2.setSpan(new StrikethroughSpan(), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        homeHeadHolder.tv_old_price_three.setText(sp2);
                        homeHeadHolder.tv_price_three.setText("¥" + goodsBean2.getSalePrice());
                        homeHeadHolder.tv_title_three.setText(goodsBean2.getGoodsName());
                        homeHeadHolder.tv_num_three.setText("销量：" + goodsBean2.getSalesVolume());
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean2.getPic(), homeHeadHolder.iv_pic_three, PictureOption.getSimpleOptions());
                    }
//                    switch (getGoodsListResponse.dataList.size()){
//                        case 3:{
//                            GoodsBean goodsBean=goodsBeanList.get(0);
//                            String string = "¥" + goodsBean.getOriginalPrice();
//                            SpannableString sp = new SpannableString(string);
//                            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_one.setText(sp);
//                            homeHeadHolder.tv_price_one.setText("¥" + goodsBean.getSalePrice());
//                            homeHeadHolder.tv_title_one.setText(goodsBean.getGoodsName());
//                            homeHeadHolder.tv_num_one.setText("销量：" + goodsBean.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), homeHeadHolder.iv_pic_one, PictureOption.getSimpleOptions());
//
//                            GoodsBean goodsBean1=goodsBeanList.get(1);
//                            String string1 = "¥" + goodsBean1.getOriginalPrice();
//                            SpannableString sp1 = new SpannableString(string1);
//                            sp1.setSpan(new StrikethroughSpan(), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_two.setText(sp1);
//                            homeHeadHolder.tv_price_two.setText("¥" + goodsBean1.getSalePrice());
//                            homeHeadHolder.tv_title_two.setText(goodsBean1.getGoodsName());
//                            homeHeadHolder.tv_num_two.setText("销量：" + goodsBean1.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean1.getPic(), homeHeadHolder.iv_pic_two, PictureOption.getSimpleOptions());
//
//                            GoodsBean goodsBean2=goodsBeanList.get(2);
//                            String string2 = "¥" + goodsBean2.getOriginalPrice();
//                            SpannableString sp2 = new SpannableString(string2);
//                            sp2.setSpan(new StrikethroughSpan(), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_three.setText(sp2);
//                            homeHeadHolder.tv_price_three.setText("¥" + goodsBean2.getSalePrice());
//                            homeHeadHolder.tv_title_three.setText(goodsBean2.getGoodsName());
//                            homeHeadHolder.tv_num_three.setText("销量：" + goodsBean2.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean2.getPic(), homeHeadHolder.iv_pic_two, PictureOption.getSimpleOptions());
//                            break;
//                        }
//                        case 2:{
//                            GoodsBean goodsBean=goodsBeanList.get(0);
//                            String string = "¥" + goodsBean.getOriginalPrice();
//                            SpannableString sp = new SpannableString(string);
//                            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_one.setText(sp);
//                            homeHeadHolder.tv_price_one.setText("¥" + goodsBean.getSalePrice());
//                            homeHeadHolder.tv_title_one.setText(goodsBean.getGoodsName());
//                            homeHeadHolder.tv_num_one.setText("销量：" + goodsBean.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), homeHeadHolder.iv_pic_one, PictureOption.getSimpleOptions());
//
//                            GoodsBean goodsBean1=goodsBeanList.get(1);
//                            String string1 = "¥" + goodsBean1.getOriginalPrice();
//                            SpannableString sp1 = new SpannableString(string1);
//                            sp1.setSpan(new StrikethroughSpan(), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_two.setText(sp1);
//                            homeHeadHolder.tv_price_two.setText("¥" + goodsBean1.getSalePrice());
//                            homeHeadHolder.tv_title_two.setText(goodsBean1.getGoodsName());
//                            homeHeadHolder.tv_num_two.setText("销量：" + goodsBean1.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean1.getPic(), homeHeadHolder.iv_pic_two, PictureOption.getSimpleOptions());
//                            break;
//                        }
//                        case 1:{
//                            GoodsBean goodsBean=goodsBeanList.get(0);
//                            String string = "¥" + goodsBean.getOriginalPrice();
//                            SpannableString sp = new SpannableString(string);
//                            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            homeHeadHolder.tv_old_price_one.setText(sp);
//                            homeHeadHolder.tv_price_one.setText("¥" + goodsBean.getSalePrice());
//                            homeHeadHolder.tv_title_one.setText(goodsBean.getGoodsName());
//                            homeHeadHolder.tv_num_one.setText("销量：" + goodsBean.getSalesVolume());
//                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), homeHeadHolder.iv_pic_one, PictureOption.getSimpleOptions());
//                            break;
//                        }
//                    }
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getGoodsListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    //品质生活
    public void getQualityLifeGoodList() {
        GetGoodListProtocol getGoodListProtocol = new GetGoodListProtocol();
        String url = getGoodListProtocol.getApiFun();
        final HashMap<String, String> map = new HashMap<>();
        map.put("qualityLife", "1");
        map.put("pageNo", "1");
        map.put("pageSize", "3");


        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.code.equals("0")) {
                    if (getGoodsListResponse.dataList.size() > 0) {
                        List<GoodsBean> goodsBeanList = getGoodsListResponse.dataList;
                        if (goodsBeanList.size() >= 3) {
                            GoodsBean goodsBean = goodsBeanList.get(0);
                            String string = "¥" + goodsBean.getOriginalPrice();
                            SpannableString sp = new SpannableString(string);
                            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            homeHeadHolder.tv_old_price_four.setText(sp);
                            homeHeadHolder.tv_price_four.setText("¥" + goodsBean.getSalePrice());
                            homeHeadHolder.tv_title_four.setText(goodsBean.getGoodsName());
                            homeHeadHolder.tv_num_four.setText("销量：" + goodsBean.getSalesVolume());
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean.getPic(), homeHeadHolder.iv_pic_four, PictureOption.getSimpleOptions());

                            GoodsBean goodsBean1 = goodsBeanList.get(1);
                            String string1 = "¥" + goodsBean1.getOriginalPrice();
                            SpannableString sp1 = new SpannableString(string1);
                            sp1.setSpan(new StrikethroughSpan(), 0, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            homeHeadHolder.tv_old_price_five.setText(sp1);
                            homeHeadHolder.tv_price_five.setText("¥" + goodsBean1.getSalePrice());
                            homeHeadHolder.tv_title_five.setText(goodsBean1.getGoodsName());
                            homeHeadHolder.tv_num_five.setText("销量：" + goodsBean1.getSalesVolume());
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean1.getPic(), homeHeadHolder.iv_pic_five, PictureOption.getSimpleOptions());

                            GoodsBean goodsBean2 = goodsBeanList.get(2);
                            String string2 = "¥" + goodsBean2.getOriginalPrice();
                            SpannableString sp2 = new SpannableString(string2);
                            sp2.setSpan(new StrikethroughSpan(), 0, string2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            homeHeadHolder.tv_old_price_six.setText(sp2);
                            homeHeadHolder.tv_price_six.setText("¥" + goodsBean2.getSalePrice());
                            homeHeadHolder.tv_title_six.setText(goodsBean2.getGoodsName());
                            homeHeadHolder.tv_num_six.setText("销量：" + goodsBean2.getSalesVolume());
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + goodsBean2.getPic(), homeHeadHolder.iv_pic_six, PictureOption.getSimpleOptions());
                        }
                    }
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            getGoodsListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }


    public void getHomeBanner() {
        GetAdListProtocol getHomeProtocol = new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(4));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    if (getHomeResponse.dataList.size() > 0) {
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getHomeResponse.dataList.get(0).pic, homeHeadHolder.iv_home_left, PictureOption.getSimpleOptions());
                    }
                } else {
                    if (getHomeResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    public void getHomeBannerRightTop() {
        GetAdListProtocol getHomeProtocol = new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(5));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    if (getHomeResponse.dataList.size() > 0) {
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getHomeResponse.dataList.get(0).pic, homeHeadHolder.iv_home_right_top, PictureOption.getSimpleOptions());
                    }
                } else {
                    if (getHomeResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    public void getHomeBannerRightBottom() {
        GetAdListProtocol getHomeProtocol = new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(6));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    if (getHomeResponse.dataList.size() > 0) {
                        imageLoader.displayImage("http://qiniu.lelegou.pro/" + getHomeResponse.dataList.get(0).pic, homeHeadHolder.iv_home_right_bottom, PictureOption.getSimpleOptions());
                    }
                } else {
                    if (getHomeResponse.msg.indexOf("此账号在其他地方登陆") != -1) {

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    } else {
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}
