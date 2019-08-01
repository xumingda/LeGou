package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.AddressAdapter;
import com.xq.LegouShop.adapter.HomeAdapter;
import com.xq.LegouShop.adapter.MenuAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.CategoryBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.protocol.GetAdListProtocol;
import com.xq.LegouShop.protocol.GetCategoryListProtocol;
import com.xq.LegouShop.protocol.GetUserReceiveAddressListProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetUserReceiveAddressListResponse;
import com.xq.LegouShop.tabpager.TabClassifyPager;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//我的收货地址
public class ClassifyActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private int parentId;
    private String select_parentId,title;
    private Dialog loadingDialog;
    private List<GetCategoryListResponse.DataList> categoryList;
    private String url;
    private Gson gson;

    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean.DataBean> homeList = new ArrayList<>();
    private List<Integer> showTitle;

    private ListView lv_menu;
    private ListView lv_home;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
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
    private ImageLoader imageLoader;
    private boolean isopen;

    private TextView tv_title;
    private GetCategoryListResponse getCategoryListResponse;
    private int selectitem;
    private View view_back;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_classify, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {
        select_parentId=getIntent().getStringExtra("select_parentId");
        title=getIntent().getStringExtra("title");
        selectitem=getIntent().getIntExtra("selectitem",0);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        tv_title = (TextView) findViewById(R.id.tv_titile);
        lv_home = (ListView) findViewById(R.id.lv_home);
        view_back=(View)findViewById(R.id.view_back);
        view_back.setOnClickListener(this);
        tv_title.setText(title);
        //轮播
        vp = (ViewPager) findViewById(R.id.vp);
        v_dot0 = (View) findViewById(R.id.v_dot0);
        v_dot1 = (View) findViewById(R.id.v_dot1);
        v_dot2 = (View) findViewById(R.id.v_dot2);
        v_dot3 = (View) findViewById(R.id.v_dot3);
        v_dot4 = (View) findViewById(R.id.v_dot4);
        ll_pager = (LinearLayout) findViewById(R.id.ll_pager);
        adsBeanList=new ArrayList<>();

        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);

        homeAdapter = new HomeAdapter(this, homeList);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectitem=position;
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                tv_title.setText(menuList.get(position));
                parentId=Integer.parseInt(categoryList.get(position).id);
                getCategoryList();
            }
        });


        categoryList=new ArrayList<>();
        parentId=0;
        getCategoryList();
        getHome();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void getHome() {
        GetAdListProtocol getHomeProtocol=new GetAdListProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("position", String.valueOf(3));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                GetAdListResponse getHomeResponse = gson.fromJson(json, GetAdListResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode().equals("0")) {
                    if (getHomeResponse.dataList.size() > 0) {
                        setImageViews(getHomeResponse.dataList);
                        ll_pager.setVisibility(View.VISIBLE);
                    }else{
                        ll_pager.setVisibility(View.INVISIBLE);
                    }

                } else {
                    if(getHomeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(ClassifyActivity.this,
                                getHomeResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(ClassifyActivity.this, getHomeResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                DialogUtils.showAlertDialog(ClassifyActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void setImageViews(List<GetAdListResponse.DataList> dataListList){
        for(int i=0;i<dataListList.size();i++){
            adsBeanList.add(dataListList.get(i).pic);
        }

        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < adsBeanList.size(); i++) {
            if (i < 5) {
                imageView = new ImageView(ClassifyActivity.this);
//                ViewListener viewListener = new ViewListener(i);
//                imageView.setOnClickListener(viewListener);
                imageLoader.displayImage("http://qiniu.lelegou.pro/"+adsBeanList.get(i), imageView, PictureOption.getSimpleOptions());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    public void getCategoryList() {
        loadingDialog.show();
        GetCategoryListProtocol getCategoryListProtocol = new GetCategoryListProtocol();
        String url = getCategoryListProtocol.getApiFun();
        final HashMap<String,String> map=new HashMap<>();
        if(parentId>0){
            map.put("parentId",String.valueOf(parentId));
        }
        MyVolley.uploadNoFile(MyVolley.POST, url, map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                getCategoryListResponse = gson.fromJson(json, GetCategoryListResponse.class);
                LogUtils.e("getCategoryListResponse:" + getCategoryListResponse.toString());
                if (getCategoryListResponse.code.equals("0")) {
                    if(parentId>0){
                        CategoryBean.DataBean dataBean=new CategoryBean.DataBean();
                        dataBean.setDataList(getCategoryListResponse.dataList);
                        homeList.clear();
                        homeList.add(dataBean);
                        homeAdapter.notifyDataSetChanged();
                    }else{
                        categoryList.clear();
                        categoryList.addAll(getCategoryListResponse.dataList);
                        for(int i=0;i<getCategoryListResponse.dataList.size();i++){
                            menuList.add(getCategoryListResponse.dataList.get(i).categoryName);
                        }
                        menuAdapter.notifyDataSetChanged();
                        //默认选择第一个
                        if(menuList.size()>0){
                            if(selectitem<9) {
                                menuAdapter.setSelectItem(selectitem);
                                menuAdapter.notifyDataSetInvalidated();
                                parentId = Integer.parseInt(select_parentId);
                                getCategoryList();
                            }else{
                                menuAdapter.setSelectItem(0);
                                menuAdapter.notifyDataSetInvalidated();
                                if(categoryList.size()>0) {
                                    parentId = Integer.parseInt(categoryList.get(0).id);
                                    getCategoryList();
                                }
                            }
                        }
                    }

                } else {
                    DialogUtils.showAlertDialog(ClassifyActivity.this,
                            getCategoryListResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ClassifyActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }


}
