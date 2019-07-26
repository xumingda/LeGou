package com.xq.LegouShop.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GameRoomAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.BaseApplication;
import com.xq.LegouShop.response.PlayRoomResponse;
import com.xq.LegouShop.response.ScoreRoomResponse;
import com.xq.LegouShop.util.DateUtils;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.PieView;
import com.xq.LegouShop.weiget.wheelsruflibrary.listener.RotateListener;
import com.xq.LegouShop.weiget.wheelsruflibrary.view.WheelSurfView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//游戏
public class GameActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    /**
     * 手机输入框
     */
    private Dialog loadingDialog;
    private int time = 60;
    private String userphone;
    private String pwd;
    private Button loginButton;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
//    private ImageView imageView;
//    private PieView pieView;
    private boolean isRunning=false;
    private Dialog dialog;
    private Button btn_out_game;
;
    private PlayRoomResponse playRoomResponse;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_game, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {
        loadingDialog = DialogUtils.createLoadDialog(GameActivity.this, false);
        btn_out_game = (Button) rootView.findViewById(R.id.btn_out_game);
        view_back=(View)findViewById(R.id.view_back);
//        imageView= findViewById(R.id.image);
//        pieView = findViewById(R.id.zpan);

        view_back.setOnClickListener(this);
        WebSocketHandler.getDefault().addListener(socketListener);
        /**
         * 新增使用代码设置属性的方式
         *
         * 请注意：
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *  使用这种方式需要在引入布局文件的时候在布局文件中设置mTypeNums = -1 来告诉我你现在要用代码传入这些属性
         *
         *  重要的事情说三遍
         *
         *  例如
         *  <com.cretin.www.wheelsruflibrary.view.WheelSurfView
         *      android:id="@+id/wheelSurfView2"
         *      android:layout_width="match_parent"
         *      android:layout_height="match_parent"
         *      wheelSurfView:typenum="-1"
         *      android:layout_margin="20dp">
         *
         *  然后调用setConfig()方法来设置你的属性
         *
         * 请注意：
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *  你在传入所有的图标文件之后需要调用 WheelSurfView.rotateBitmaps() 此方法来处理一下你传入的图片
         *
         *  重要的事情说三遍
         *
         * 请注意：
         *  .setmColors(colors)
         *  .setmDeses(des)
         *  .setmIcons(mListBitmap)
         *  这三个方法中的参数长度必须一致 否则会报运行时异常
         */
        //颜色
        Integer[] colors = new Integer[]{Color.parseColor("#fef9f7"), Color.parseColor("#fbc6a9")
                , Color.parseColor("#ffdecc"), Color.parseColor("#fbc6a9")
                , Color.parseColor("#ffdecc"), Color.parseColor("#fbc6a9")
                , Color.parseColor("#ffdecc") , Color.parseColor("#ffdecc"), Color.parseColor("#fbc6a9")
                , Color.parseColor("#ffdecc")};
        //文字
        String[] des = new String[]{"王 者 皮 肤", "1 8 0 积 分", "L O L 皮 肤"
                , "谢 谢 参 与", "2 8 积 分", "微 信 红 包",
                "5 Q 币", "2 8 积 分", "微 信 红 包",
                "5 Q 币"};
        //图标
        List<Bitmap> mListBitmap = new ArrayList<>();
        for ( int i = 0; i < colors.length; i++ ) {
            mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.iphone));
        }
        //主动旋转一下图片
        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);

        //获取第三个视图
        final WheelSurfView wheelSurfView2 = findViewById(R.id.wheelSurfView2);
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmIcons(mListBitmap)
                .setmType(1)
                .setmTypeNum(10)
                .build();
        wheelSurfView2.setConfig(build);

        //添加滚动监听
        wheelSurfView2.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                Toast.makeText(GameActivity.this, "结束了 位置：" + position + "   描述：" + des, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(GameActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("确定要花费100积分抽奖？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //模拟位置
                        int position = new Random().nextInt(7) + 1;
                        wheelSurfView2.startRotate(position);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });

        btn_out_game.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            LogUtils.e("onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            LogUtils.e("onSendDataError:" + errorResponse.toString());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
//            if( SharedPrefrenceUtils.getInt(UIUtils.getContext(),"action",0)==4){
                Gson gson = new Gson();
                ScoreRoomResponse scoreRoomResponse= gson.fromJson(message, ScoreRoomResponse.class);
                if(scoreRoomResponse.action==1||scoreRoomResponse.action==2||scoreRoomResponse.action==3){
                    SharedPrefrenceUtils.setInt(UIUtils.getContext(),"action",0);
                    WebSocketHandler.getDefault().removeListener(socketListener);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                }
                else {
                    DialogUtils.showAlertDialog(GameActivity.this,
                            scoreRoomResponse.msg);
                }

//            }
            LogUtils.e("play::"+message);
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            LogUtils.e("message2:"+bytes);
//            appendMsgDisplay("onMessage(ByteBuffer, T):" + bytes);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                outGame();
                break;
            }
            case R.id.btn_out_game:{
                Dialog dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否确定要离开？","提示",GameActivity.this);
                dialog.show();
                break;
            }
        }
    }
    //离开游戏房间
    private void outGame(){
        SharedPrefrenceUtils.setInt(UIUtils.getContext(),"action",4);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(this,"token"));
            jsonObject.put("action", String.valueOf(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(jsonObject.toString());
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            outGame();
        }

        return super.onKeyDown(keyCode, event);

    }
}
