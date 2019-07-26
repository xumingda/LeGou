package com.xq.LegouShop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.fragment.ControlTabFragment;

public class MainActivity extends BaseActivity {
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_SETTINGS
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
        ctf.setChecked(0);
    }
}
