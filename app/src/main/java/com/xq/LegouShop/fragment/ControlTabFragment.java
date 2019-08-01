package com.xq.LegouShop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;



import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GameActivity;
import com.xq.LegouShop.activity.GameRoomActivity;
import com.xq.LegouShop.base.BaseApplication;
import com.xq.LegouShop.base.BaseFragment;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.LoginGameBean;
import com.xq.LegouShop.tabpager.TabClassifyPager;
import com.xq.LegouShop.tabpager.TabGamePager;
import com.xq.LegouShop.tabpager.TabHomePager;
import com.xq.LegouShop.tabpager.TabMyselfPager;
import com.xq.LegouShop.tabpager.TabShopcarPager;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.zhangke.websocket.util.LogUtil;

;import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016年5月25日上午10:14:18
 * @描述: 控制侧滑菜单以及 附近 找店 发现 我的四个页面
 */
public class ControlTabFragment extends BaseFragment implements
        OnCheckedChangeListener {

    @ViewInject(R.id.rg_content)
    private RadioGroup mRadioGroup;

    @ViewInject(R.id.rb_content_home)
    private RadioButton rb_content_home;
    @ViewInject(R.id.rb_content_order)
    private RadioButton rb_content_order;
    @ViewInject(R.id.rb_content_set)
    private RadioButton rb_content_set;
    @ViewInject(R.id.rb_content_shopcar)
    private RadioButton rb_content_shopcar;
    @ViewInject(R.id.rb_content_myself)
    private RadioButton rb_content_myself;
    // 内容区域
    @ViewInject(R.id.fl_content_fragment)
    private FrameLayout mFrameLayout;
    // 底部区域
    @ViewInject(R.id.fl_bottom)
    private FrameLayout bFrameLayout;
    @ViewInject(R.id.dl)
    private FrameLayout mDragLayout;

    // 处理事件分发的自定义LinearLayout
    @ViewInject(R.id.my_ll)
    private MyLinearLayout mLinearLayout;

    // 默认选中第0页面
    public static int mCurrentIndex = 0;
    // 中间变量
    public static int temp = 0;
    // 底部页面的集合
    private List<TabBasePager> mPagerList;
    private TabHomePager tabHomePager;
    private TabShopcarPager shopcarPager;

    public TabShopcarPager getShopcarPager() {
        return shopcarPager;
    }

    /**嗅探闪烁动画*/
    AlphaAnimation snifferflashAnimation = new AlphaAnimation(0.1f, 1.0f);


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.control_tab, null);
        // Viewutil工具的注入
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void initData() {


        // 添加实际的页面
        mPagerList = new ArrayList<TabBasePager>();
        tabHomePager = new TabHomePager(mActivity, mDragLayout,
                mLinearLayout);
        TabGamePager gamePager= new TabGamePager(mActivity, mDragLayout,
                mLinearLayout);
        TabMyselfPager tabMyselfPager= new TabMyselfPager(mActivity, mDragLayout,
                mLinearLayout);
        TabClassifyPager tabClassifyPager= new TabClassifyPager(mActivity, mDragLayout,
                mLinearLayout);
        shopcarPager= new TabShopcarPager(mActivity, mDragLayout,
                mLinearLayout);
        mPagerList.add(tabHomePager);
        mPagerList.add(tabClassifyPager);
        mPagerList.add(gamePager);
        mPagerList.add(shopcarPager);
        mPagerList.add(tabMyselfPager);
        // 给RadioGroup 设置监听
        getmRadioGroup().setOnCheckedChangeListener(this);
        LoginGameBean loginGameBean= SPUtils.getBeanFromSp(UIUtils.getContext(), "LoginGameBean", "LoginGameBean");
        int action =SharedPrefrenceUtils.getInt(UIUtils.getContext(),"action",0);
//        if(action==2){
//            LogUtils.e("id:"+loginGameBean.getScoreId());
//            Intent intent=new Intent(UIUtils.getContext(), GameRoomActivity.class);
//            intent.putExtra("id",loginGameBean.getScoreId());
//            if(loginGameBean.getScoreId()==1){
//                intent.putExtra("type",200);
//                intent.putExtra("title","200积分专区");
//            }else  if(loginGameBean.getScoreId()==2){
//                intent.putExtra("type",500);
//                intent.putExtra("title","500积分专区");
//            }else  if(loginGameBean.getScoreId()==3){
//                intent.putExtra("type",1000);
//                intent.putExtra("title","1000积分专区");
//            }else{
//                intent.putExtra("type",2000);
//                intent.putExtra("title","2000积分专区");
//            }
//            UIUtils.startActivityNextAnim(intent);
//        }else if(action==3){
//            Intent intent=new Intent(UIUtils.getContext(), GameActivity.class);
//            UIUtils.startActivityNextAnim(intent);
//        }
        switchCurrentPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setChecked(int item) {
        switch (item) {
            case 0:
                rb_content_home.setChecked(true);
                break;
            case 1:
                rb_content_order.setChecked(true);
                break;
            case 2:
                rb_content_set.setChecked(true);
                break;
            case 3:
                rb_content_shopcar.setChecked(true);
                break;
            case 4:
                rb_content_myself.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 根据选中的RadioButton的id切换页面
        switch (checkedId) {
            case R.id.rb_content_home:

                mCurrentIndex = 0;
                break;
            case R.id.rb_content_order: {
                mCurrentIndex = 1;
                break;
            }
            case R.id.rb_content_set:{
                mCurrentIndex = 2;
                break;
            }
            case R.id.rb_content_shopcar:{
                mCurrentIndex = 3;
                break;
            }
            case R.id.rb_content_myself:{
                mCurrentIndex = 4;
                break;
            }
            default:
                break;
        }
        switchCurrentPage();
    }

    /**
     * 切换RadioGroup对应的页面
     */
    public void switchCurrentPage() {
        mFrameLayout.removeAllViews();

        TabBasePager tabBasePager = mPagerList.get(mCurrentIndex);
        // 获得每个页面对应的布局
        View rootView = tabBasePager.getRootView();
        tabBasePager.initData();
        mFrameLayout.addView(rootView);
    }

    public RadioGroup getmRadioGroup() {
        return mRadioGroup;
    }


    public TabHomePager getTabNearByPager() {
        return tabHomePager;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
