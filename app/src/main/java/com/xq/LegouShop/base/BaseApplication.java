package com.xq.LegouShop.base;


import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.xq.LegouShop.activity.AddressActivity;
import com.xq.LegouShop.response.LoginGameResponse;
import com.xq.LegouShop.response.RoomResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MyExceptionHandler;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.TTSUtility;
import com.xq.LegouShop.util.UIUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;
import com.zhangke.websocket.response.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;


public class BaseApplication extends Application {
    /**
     * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
     */
    private static BaseApplication mInstance;
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

//        SpeechUtility speechUtility=SpeechUtility.createUtility(this, "appid=5cefcc06," + SpeechConstant.FORCE_LOGIN +"=true");
//        Log.e("SpeechUtility","SpeechUtility111:"+speechUtility);


        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        mInstance = this;

        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        /*初始化volley*/
        MyVolley.init(this);
        MyExceptionHandler.create(this);


//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
        //初始化websoket,并监听
        initWebSocket();
//        WebSocketHandler.getDefault().addListener(socketListener);
    }

    private void initWebSocket() {
        WebSocketSetting setting = new WebSocketSetting();
        //连接地址，必填，例如 wss://echo.websocket.org
        setting.setConnectUrl("ws://47.112.96.244:8081/websocket");//必填

        //设置连接超时时间
        setting.setConnectTimeout(15 * 1000);

        //设置心跳间隔时间
        setting.setConnectionLostTimeout(60);

        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(60);

        //网络状态发生变化后是否重连，
        //需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(setting);
        //启动连接
        manager.start();

        //注意，需要在 AndroidManifest 中配置网络状态获取权限
        //注册网路连接状态变化广播
        WebSocketHandler.registerNetworkChangedReceiver(this);
    }

    //旋转适配,如果应用屏幕固定了某个方向不旋转的话(比如qq和微信),下面可不写.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenAdapterTools.getInstance().reset(this);
    }

    public static BaseApplication getApplication() {
        return mInstance;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
//            appendMsgDisplay("onConnected");

            LogUtils.e("messageon_Connected:");
        }

        @Override
        public void onConnectFailed(Throwable e) {
            if (e != null) {
                LogUtils.e("onConnectFailed:" + e.toString());
            } else {
                LogUtils.e("onConnectFailed:null");
            }
        }

        @Override
        public void onDisconnect() {
//            if(!TextUtils.isEmpty(SharedPrefrenceUtils.getString(UIUtils.getContext(), "token"))){
//                LoginGame();
//            }
            LogUtils.e("onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            LogUtils.e("onSendDataError:" + errorResponse.toString());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            LogUtils.e("messa:" + message);
            int action = SharedPrefrenceUtils.getInt(UIUtils.getContext(), "action", 0);
//            if(action==0){
            Gson gson = new Gson();
            LoginGameResponse loginGameResponse = gson.fromJson(message, LoginGameResponse.class);
            if (loginGameResponse.code.equals("0")) {
                if(loginGameResponse.action==10) {
                    if (loginGameResponse.data.authorization.equals(SharedPrefrenceUtils.getString(UIUtils.getContext(), "token"))) {
                        action = loginGameResponse.data.position;
                        SPUtils.saveBean2Sp(UIUtils.getContext(), loginGameResponse.data, "LoginGameBean", "LoginGameBean");
                        SharedPrefrenceUtils.setInt(UIUtils.getContext(), "action", action);
                    }
                }
            }else{
//                UIUtils.showToastSafe(loginGameResponse.msg);
            }

//            }

        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            LogUtils.e("message2:" + bytes);
//            appendMsgDisplay("onMessage(ByteBuffer, T):" + bytes);
        }
    };

}