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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.adapter.GameRoomAdapter;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.BaseApplication;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.response.PlayRoomResponse;
import com.xq.LegouShop.response.RoomResponse;
import com.xq.LegouShop.response.ScoreRoomResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//游戏房间
public class GameRoomActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private Dialog loadingDialog;
    public static boolean isForeground = false;
    private View view_back;

    private boolean isRunning=false;
    private GridView gridView;
    private List<OrderBean> orderBeanList;
    private int type;
    private int id;
    private String title;
    private TextView tv_title;
    private ScoreRoomResponse scoreRoomResponse;
    private PlayRoomResponse playRoomResponse;
    private  GameRoomAdapter gameRoomAdapter;
    private int scoreId;
    private int scoreRoomId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_game_room, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void goToGameScore(){
        SharedPrefrenceUtils.setInt(UIUtils.getContext(),"action",2);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(this,"token"));
            jsonObject.put("action", String.valueOf(2));
            jsonObject.put("scoreId",String.valueOf(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(jsonObject.toString());
    }


    private void initDate() {
        title=getIntent().getStringExtra("title");
        type=getIntent().getIntExtra("type",0);
        id=getIntent().getIntExtra("id",0);
        loadingDialog = DialogUtils.createLoadDialog(GameRoomActivity.this, false);
        view_back=(View)findViewById(R.id.view_back);
        tv_title=(TextView) findViewById(R.id.tv_title);
        gridView=(GridView) findViewById(R.id.gv_room);

        view_back.setOnClickListener(this);
        tv_title.setText(title);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtils.e("点击了");
                //暂时
//                scoreId=scoreRoomResponse.dataList.get(i).scoreId;
//                scoreRoomId=scoreRoomResponse.dataList.get(i).id;
//                goToPlayRoom();
                Intent intent = new Intent(GameRoomActivity.this, GameActivity.class);
                UIUtils.startActivityNextAnim(intent);
            }
        });
        WebSocketHandler.getDefault().addListener(socketListener);
        goToGameScore();
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
            Gson gson = new Gson();
            scoreRoomResponse = gson.fromJson(message, ScoreRoomResponse.class);
            LogUtils.e("onSendDataError:有接收到数据："+message);
            if( scoreRoomResponse.action==2) {

                if (gameRoomAdapter == null) {
                    gameRoomAdapter = new GameRoomAdapter(GameRoomActivity.this, scoreRoomResponse.dataList, type);
                    gridView.setAdapter(gameRoomAdapter);
                } else {
                    gameRoomAdapter.setDate(scoreRoomResponse.dataList);
                    gameRoomAdapter.notifyDataSetChanged();
                }
            }else if( scoreRoomResponse.action==3){
                if(message.indexOf("selfData")!=-1){
                    Intent intent = new Intent(GameRoomActivity.this, GameActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                }else {
                    playRoomResponse = gson.fromJson(message, PlayRoomResponse.class);
                    DialogUtils.showAlertDialog(GameRoomActivity.this,
                            playRoomResponse.msg);
                }
            }

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
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
    }

    //判断是否有资格进入
    private void goToPlayRoom(){
        SharedPrefrenceUtils.setInt(UIUtils.getContext(),"action",3);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(this,"token"));
            jsonObject.put("action", String.valueOf(3));
            jsonObject.put("scoreId",String.valueOf(scoreId));
            jsonObject.put("scoreRoomId",String.valueOf(scoreRoomId));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e("jsonObject:"+jsonObject.toString());
        WebSocketHandler.getDefault().send(jsonObject.toString());
    }


}
