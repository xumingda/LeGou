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
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GameRoomAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.BaseApplication;
import com.xq.LegouShop.response.LoginGameResponse;
import com.xq.LegouShop.response.PlayRoomResponse;
import com.xq.LegouShop.response.ScoreRoomResponse;
import com.xq.LegouShop.util.DateUtils;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.CircleImageLayout;
import com.xq.LegouShop.weiget.CircleImageView;
import com.xq.LegouShop.weiget.CircleLayout;
import com.xq.LegouShop.weiget.PieView;
import com.xq.LegouShop.weiget.wheelsruflibrary.listener.RotateListener;
import com.xq.LegouShop.weiget.wheelsruflibrary.view.WheelSurfView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;
import com.zhangke.websocket.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
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
    private ImageView iv_head;
    private TextView tv_title,tv_mus,tv_name,tv_num,tv_pass,tv_middle_time,tv_num_one,tv_nick_name_one,tv_num_two,tv_nick_name_two,tv_num_three,tv_nick_name_three,tv_num_four,tv_nick_name_four,tv_num_five,tv_nick_name_five,tv_num_six,tv_nick_name_six,tv_num_sev,tv_nick_name_sev,tv_num_eig,tv_nick_name_eig,tv_num_nine,tv_nick_name_nine,tv_num_ten,tv_nick_name_ten;
    private CircleImageView iv_head_one,iv_head_two,iv_head_three,iv_head_four,iv_head_five,iv_head_six,iv_head_sev,iv_head_eig,iv_head_nine,iv_head_ten;
    private WheelSurfView wheelSurfView2;
    private PlayRoomResponse playRoomResponse;
    private long leftTime;
    private long middleTime;
    private ImageLoader imageLoader;

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

    Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    leftTime = 0;
                    handler.removeCallbacks(update_thread);
                    break;
                case 2:{
                    tv_middle_time.setVisibility(View.INVISIBLE);
                    break;
                }
                case 3:{
                    handler.removeCallbacks(update_thread);

                    update_thread.run();
                    break;
                }
            }
            super.handleMessage(msg);
        }

    };

    Handler handler = new Handler();
    Runnable update_thread = new Runnable() {
        @Override
        public void run() {
            leftTime--;
            LogUtils.e("leftTime="+leftTime);
            if (leftTime > 0) {
                //倒计时效果展示
                String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                LogUtils.e("formatLongToTimeStr:"+formatLongToTimeStr.length()+"  "+formatLongToTimeStr);

                tv_mus.setText(formatLongToTimeStr);
                //每一秒执行一次
                handler.postDelayed(this, 1000);
            } else {//倒计时结束
                //处理业务流程

                //发送消息，结束倒计时
                Message message = new Message();
                message.what = 1;
                handlerStop.sendMessage(message);
            }
        }
    };

    Runnable update_thread_sec = new Runnable() {
        @Override
        public void run() {
            middleTime--;
            LogUtils.e("middleTime="+middleTime);
            if (middleTime > 0) {
                //倒计时效果展示
                String formatLongToTimeStr = formatLongToSec(middleTime);
                LogUtils.e("formatLongToTimeStr:"+formatLongToTimeStr.length()+"  "+formatLongToTimeStr);

                tv_middle_time.setText(formatLongToTimeStr);
                //每一秒执行一次
                handler.postDelayed(this, 1000);
            } else {//倒计时结束
                //处理业务流程

                //发送消息，结束倒计时
                Message message = new Message();
                message.what = 2;
                handlerStop.sendMessage(message);
            }
        }
    };
    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime;
        if(second<10){
            strtime = minute+":0"+second;
        }else {
           strtime = minute + ":" + second;
        }
        return strtime;
    }

    public String formatLongToSec(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime;
        if(second<10){
            strtime = "0"+second;
        }else {
            strtime = ""+second;
        }
        return strtime;
    }

    private void initDate() {

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        playRoomResponse=(PlayRoomResponse) getIntent().getSerializableExtra("playRoomResponse");
        loadingDialog = DialogUtils.createLoadDialog(GameActivity.this, false);
        tv_num_one=(TextView)rootView.findViewById(R.id.tv_num_one);
        tv_title=(TextView)rootView.findViewById(R.id.tv_title);
        tv_nick_name_one =(TextView)rootView.findViewById(R.id.tv_nickName_one);
        iv_head_one=(CircleImageView) rootView.findViewById(R.id.iv_head_one);
        tv_num_three=(TextView)rootView.findViewById(R.id.tv_num_three);
        tv_nick_name_three =(TextView)rootView.findViewById(R.id.tv_nickName_three);
        iv_head_three=(CircleImageView) rootView.findViewById(R.id.iv_head_three);
        tv_num_two=(TextView)rootView.findViewById(R.id.tv_num_two);
        tv_nick_name_two =(TextView)rootView.findViewById(R.id.tv_nickName_two);
        iv_head_two=(CircleImageView) rootView.findViewById(R.id.iv_head_two);
        tv_num_four=(TextView)rootView.findViewById(R.id.tv_num_four);
        tv_nick_name_four =(TextView)rootView.findViewById(R.id.tv_nickName_four);
        iv_head_four=(CircleImageView) rootView.findViewById(R.id.iv_head_four);
        tv_num_five=(TextView)rootView.findViewById(R.id.tv_num_five);
        tv_nick_name_five =(TextView)rootView.findViewById(R.id.tv_nickName_five);
        iv_head_five=(CircleImageView) rootView.findViewById(R.id.iv_head_five);
        tv_num_six=(TextView)rootView.findViewById(R.id.tv_num_six);
        tv_nick_name_six =(TextView)rootView.findViewById(R.id.tv_nickName_six);
        iv_head_six=(CircleImageView) rootView.findViewById(R.id.iv_head_six);
        tv_num_sev=(TextView)rootView.findViewById(R.id.tv_num_sev);
        tv_nick_name_sev =(TextView)rootView.findViewById(R.id.tv_nickName_sev);
        iv_head_sev=(CircleImageView) rootView.findViewById(R.id.iv_head_sev);
        tv_num_eig=(TextView)rootView.findViewById(R.id.tv_num_eig);
        tv_nick_name_eig =(TextView)rootView.findViewById(R.id.tv_nickName_eig);
        iv_head_eig=(CircleImageView) rootView.findViewById(R.id.iv_head_eig);
        tv_num_nine=(TextView)rootView.findViewById(R.id.tv_num_nine);
        tv_nick_name_nine =(TextView)rootView.findViewById(R.id.tv_nickName_nine);
        iv_head_nine=(CircleImageView) rootView.findViewById(R.id.iv_head_nine);
        tv_num_ten=(TextView)rootView.findViewById(R.id.tv_num_ten);
        tv_nick_name_ten =(TextView)rootView.findViewById(R.id.tv_nickName_ten);
        iv_head_ten=(CircleImageView) rootView.findViewById(R.id.iv_head_ten);
        btn_out_game = (Button) rootView.findViewById(R.id.btn_out_game);
        view_back=(View)findViewById(R.id.view_back);
        iv_head=(ImageView)findViewById(R.id.iv_head);
        tv_num=(TextView)findViewById(R.id.tv_num) ;
        tv_pass=(TextView)findViewById(R.id.tv_pass) ;
        tv_name=(TextView)findViewById(R.id.tv_name) ;
        tv_middle_time=(TextView)findViewById(R.id.tv_middle_time) ;
        tv_mus= findViewById(R.id.tv_mus);
        if(playRoomResponse.data.scoreId==1){
            tv_title.setText("200积分专区("+playRoomResponse.data.roomName+")");
        }else  if(playRoomResponse.data.scoreId==2){
            tv_title.setText("500积分专区("+playRoomResponse.data.roomName+")");
        }else  if(playRoomResponse.data.scoreId==31){
            tv_title.setText("1000积分专区("+playRoomResponse.data.roomName+")");
        } else {
            tv_title.setText("2000积分专区("+playRoomResponse.data.roomName+")");
        }


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
        String[] des = new String[]{"1号", "2号", "3号"
                , "4号", "5号", "6号",
                "7号", "8号", "9号",
                "10号"};
        //图标
        List<Bitmap> mListBitmap = new ArrayList<>();
        for ( int i = 0; i < colors.length; i++ ) {
            mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }
        //主动旋转一下图片
        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);

        //获取第三个视图
        wheelSurfView2 = findViewById(R.id.wheelSurfView2);
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
//                Toast.makeText(GameActivity.this, "结束了 位置：" + position + "   描述：" + des, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
                //点击开奖，去掉
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(GameActivity.this);
//                builder.setTitle("温馨提示");
//                builder.setMessage("确定要花费100积分抽奖？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //模拟位置
//                        int position = new Random().nextInt(7) + 1;
//                        wheelSurfView2.startRotate(position);
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                builder.show();

            }
        });
        if(playRoomResponse!=null){
            LogUtils.e("数据:"+playRoomResponse.data.status+"   "+playRoomResponse.data.leftSecond);
            if(playRoomResponse.data.status!=0||playRoomResponse.data.status!=4) {
                leftTime = Long.parseLong(playRoomResponse.data.leftSecond);
                update_thread.run();
            }else{
                leftTime = Long.parseLong(playRoomResponse.data.leftSecond);
                String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                LogUtils.e("formatLongToTimeStr:"+formatLongToTimeStr.length()+"  "+formatLongToTimeStr);
                tv_mus.setText(formatLongToTimeStr);


            }
            for(int i=0;i<playRoomResponse.dataList.size();i++){
                switch (playRoomResponse.dataList.get(i).num){
                    case 1:{
                        tv_nick_name_one.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_one.setText("1号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_one, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 2:{
                        tv_nick_name_two.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_two.setText("2号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_two, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 3:{
                        tv_nick_name_three.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_three.setText("3号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_three, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 4:{
                        tv_nick_name_four.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_four.setText("4号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_four, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 5:{
                        tv_nick_name_five.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_five.setText("5号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_five, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 6:{
                        tv_nick_name_six.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_six.setText("6号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_six, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 7:{
                        tv_nick_name_sev.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_sev.setText("7号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_sev, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 8:{
                        tv_nick_name_eig.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_eig.setText("8号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_eig, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 9:{
                        tv_nick_name_nine.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_nine.setText("9号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_nine, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                    case 10:{
                        tv_nick_name_ten.setText(playRoomResponse.dataList.get(i).nickName);
                        tv_num_ten.setText("10号");
                        if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                            imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_ten, PictureOption.getSimpleOptions());
                        }
                        break;
                    }
                }
            }
            tv_pass.setText("X"+playRoomResponse.selfData.passcardNum);
            tv_name.setText(playRoomResponse.selfData.nickName);
            tv_num.setText(playRoomResponse.selfData.num+"号");
            imageLoader.displayImage("http://qiniu.lelegou.pro/"+playRoomResponse.selfData.headurl,iv_head, PictureOption.getSimpleOptions());
        }
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
        WebSocketHandler.getDefault().removeListener(socketListener);
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
                if(scoreRoomResponse.action==1){
                    WebSocketHandler.getDefault().removeListener(socketListener);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                }else if(scoreRoomResponse.action==2){

                    Intent intent = new Intent(GameActivity.this, GameRoomActivity.class);
                    intent.putExtra("id", scoreRoomResponse.dataList.get(0).scoreId);
                    intent.putExtra("scoreRoomBeanList",(Serializable)scoreRoomResponse.dataList);
                    if (scoreRoomResponse.dataList.get(0).scoreId == 1) {
                        intent.putExtra("type", 200);
                        intent.putExtra("title", "200积分专区");
                    } else if (scoreRoomResponse.dataList.get(0).scoreId == 2) {
                        intent.putExtra("type", 500);
                        intent.putExtra("title", "500积分专区");
                    } else if (scoreRoomResponse.dataList.get(0).scoreId == 3) {
                        intent.putExtra("type", 1000);
                        intent.putExtra("title", "1000积分专区");
                    } else {
                        intent.putExtra("type", 2000);
                        intent.putExtra("title", "2000积分专区");
                    }
                    UIUtils.startActivityNextAnim(intent);
                    finish();

                }
                else if(scoreRoomResponse.action==3) {
                    playRoomResponse = gson.fromJson(message, PlayRoomResponse.class);
                    if(playRoomResponse.data.status!=0||playRoomResponse.data.status!=4) {
                        //倒计时复位，重开
                        leftTime = Long.parseLong(playRoomResponse.data.leftSecond);
                        Message message1 = new Message();
                        message1.what = 3;
                        handlerStop.sendMessage(message1);
                    }else{
                        if(!TextUtils.isEmpty(playRoomResponse.data.leftSecond)) {
                            leftTime = Long.parseLong(playRoomResponse.data.leftSecond);
                            String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                            LogUtils.e("formatLongToTimeStr:" + formatLongToTimeStr.length() + "  " + formatLongToTimeStr);
                            tv_mus.setText(formatLongToTimeStr);
                        }
                    }
                    for(int i=0;i<playRoomResponse.dataList.size();i++){
                        switch (playRoomResponse.dataList.get(i).num){
                            case 1:{
                                tv_nick_name_one.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_one.setText("1号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_one, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 2:{
                                tv_nick_name_two.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_two.setText("2号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_two, PictureOption.getSimpleOptions());
                                }
                                break;
                            }  case 3:{
                                tv_nick_name_three.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_three.setText("3号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_three, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 4:{
                                tv_nick_name_four.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_four.setText("4号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_four, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 5:{
                                tv_nick_name_five.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_five.setText("5号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_five, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 6:{
                                tv_nick_name_six.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_six.setText("6号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_six, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 7:{
                                tv_nick_name_sev.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_sev.setText("7号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_sev, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 8:{
                                tv_nick_name_eig.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_eig.setText("8号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_eig, PictureOption.getSimpleOptions());
                                }
                                break;
                            }   case 9:{
                                tv_nick_name_nine.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_nine.setText("9号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_nine, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                            case 10:{
                                tv_nick_name_ten.setText(playRoomResponse.dataList.get(i).nickName);
                                tv_num_ten.setText("10号");
                                if(!TextUtils.isEmpty(playRoomResponse.dataList.get(i).headurl)) {
                                    imageLoader.displayImage("http://qiniu.lelegou.pro/" + playRoomResponse.dataList.get(i).headurl, iv_head_ten, PictureOption.getSimpleOptions());
                                }
                                break;
                            }
                        }
                    }
                    tv_pass.setText("X"+playRoomResponse.selfData.passcardNum);
                    tv_name.setText(playRoomResponse.selfData.nickName);
                    tv_num.setText(playRoomResponse.selfData.num+"号");
                    imageLoader.displayImage("http://qiniu.lelegou.pro/"+playRoomResponse.selfData.headurl,iv_head, PictureOption.getSimpleOptions());
                }else  if(scoreRoomResponse.action==99) {
                    tv_mus.setText("开奖中");
                    tv_middle_time.setVisibility(View.VISIBLE);
                    middleTime=5;
                    update_thread_sec.run();
                    //模拟位置
                    wheelSurfView2.startRotate(scoreRoomResponse.num);
                }else  if(scoreRoomResponse.action==77||scoreRoomResponse.action==88) {
                    //模拟位置
                    UIUtils.showToastSafe(scoreRoomResponse.msg);
                }
                else{
                    UIUtils.showToastSafe(scoreRoomResponse.msg);
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
                dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否确定要离开？","提示",GameActivity.this);
                dialog.show();
                break;
            }
            case R.id.tv_ensure:{
                dialog.dismiss();
                outGame();
                break;
            }
            case R.id.btn_out_game:{
                dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否确定要离开？","提示",GameActivity.this);
                dialog.show();
                break;
            }
        }
    }
    //离开游戏房间
    private void outGame(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(this,"token"));
            jsonObject.put("action", String.valueOf(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(jsonObject.toString());
    }

//    @Override

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            outGame();
//        }
//
//        return super.onKeyDown(keyCode, event);
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog=DialogUtils.showAlertDoubleBtnDialog(this,"是否确定要离开？","提示",GameActivity.this);
            dialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
