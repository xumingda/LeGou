package com.xq.LegouShop.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.fragment.ControlTabFragment;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.UIUtils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends BaseActivity {
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int MSG_SET_ALIAS = 1001;
    public static boolean isForeground = false;
    public static ControlTabFragment ctf;
    private static final int BASIC_PERMISSION_REQUEST_CODE = 1000;

    private void requestBasicPermission() {
        boolean isOpen = false;
        for (int i = 0; i < BASIC_PERMISSIONS.length; i++) {
            if (ContextCompat.checkSelfPermission(this, BASIC_PERMISSIONS[i])
                    == PackageManager.PERMISSION_GRANTED) {
                isOpen = true;
            }
        }
        if (!isOpen) {
            ActivityCompat.requestPermissions(this,
                    BASIC_PERMISSIONS, BASIC_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = View.inflate(this, R.layout.activity_main, null);
        setContentView(rootView);
        initFragment();
        requestBasicPermission();

        return rootView;
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 1. 开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 添加主页fragment
        ctf = new ControlTabFragment();
        transaction.replace(R.id.main_container, ctf);
        transaction.commit();

    }

    public static ControlTabFragment getCtf() {
        return ctf;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onKeyDownBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
//        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    @Override
    protected void onResume() {
//        JPushInterface.onResume(this);
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
//        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付返回
        if(requestCode==101){
            ctf.getShopcarPager().initData();
        }
    }
    private LocationManager locationManager;
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};





    /**
     * Android6.0申请权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BASIC_PERMISSION_REQUEST_CODE:

                break;
            default:
                break;
        }
    }

}
