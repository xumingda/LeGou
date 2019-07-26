package com.xq.LegouShop.tabpager;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.lidroid.xutils.ViewUtils;
import com.xq.LegouShop.R;
import com.xq.LegouShop.activity.GameActivity;
import com.xq.LegouShop.activity.GameRoomActivity;
import com.xq.LegouShop.adapter.GameRoomAdapter;
import com.xq.LegouShop.adapter.RecommendAdapter;
import com.xq.LegouShop.adapter.ShoppingcarAdapter;
import com.xq.LegouShop.base.BaseApplication;
import com.xq.LegouShop.base.TabBasePager;
import com.xq.LegouShop.bean.CartBean;
import com.xq.LegouShop.bean.LoginGameBean;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.response.GetUserInfoResponse;
import com.xq.LegouShop.response.RoomResponse;
import com.xq.LegouShop.socket.common.Constants;
import com.xq.LegouShop.socket.service.SocketService;
import com.xq.LegouShop.socket.service.TcpService;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.MyLinearLayout;
import com.xq.LegouShop.weiget.SwipeListView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 游戏
 * 购物车
 * @描述: TODO
 */
public class TabGamePager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private ImageView iv_200,iv_500,iv_1000,iv_2000;
    private ServiceConnection sc;
    public SocketService socketService;
    public RoomResponse roomResponse;
    public int action;
    /**
     * @param context
     */
    public TabGamePager(Context context, FrameLayout mDragLayout,
                        MyLinearLayout mLinearLayout ) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;
    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.game_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        return view;
    }

    public void initData() {
        action =SharedPrefrenceUtils.getInt(UIUtils.getContext(),"action",0);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
//        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        iv_200=(ImageView) view.findViewById(R.id.iv_200);
        iv_500=(ImageView) view.findViewById(R.id.iv_500);
        iv_1000=(ImageView) view.findViewById(R.id.iv_1000);
        iv_2000=(ImageView) view.findViewById(R.id.iv_2000);
//        tv_todayTurnover=(TextView)view.findViewById(R.id.tv_todayTurnover);
//        tv_qrPayTurnover=(TextView)view.findViewById(R.id.tv_qrPayTurnover);
//        tv_todayOrderSucCount=(TextView)view.findViewById(R.id.tv_todayOrderSucCount);
//        rl_menu=(RelativeLayout)view.findViewById(R.id.rl_menu);
//        rl_report=(RelativeLayout)view.findViewById(R.id.rl_report);
//        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message);
//        rl_member_manager=(RelativeLayout)view.findViewById(R.id.rl_member_manager);
//        rl_marketing=(RelativeLayout)view.findViewById(R.id.rl_marketing);
//        rl_order_manager=(RelativeLayout)view.findViewById(R.id.rl_order_manager);
//        rl_tongji=(RelativeLayout)view.findViewById(R.id.rl_tongji);
//        rl_set=(RelativeLayout)view.findViewById(R.id.rl_set);
//        rl_order_manager.setOnClickListener(this);
//        rl_marketing.setOnClickListener(this);
//        rl_set.setOnClickListener(this);
//        rl_message.setOnClickListener(this);
//        rl_member_manager.setOnClickListener(this);
//        rl_report.setOnClickListener(this);
//        rl_menu.setOnClickListener(this);
        iv_200.setOnClickListener(this);
        iv_500.setOnClickListener(this);
        iv_1000.setOnClickListener(this);
        iv_2000.setOnClickListener(this);
//        tv_shop_name.setText(merchantBean.getStoreName());
//        getHome();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);

//        bindSocketService();
//        ButterKnife.bind(UIUtils.getActivity());
//        Intent intent = new Intent(mContext, TcpService.class);
//        UIUtils.getActivity().bindService(intent,connection,BIND_AUTO_CREATE);
//        LoginGameBean loginGameBean=SPUtils.getBeanFromSp(mContext, "LoginGameBean", "LoginGameBean");
//        int action =SharedPrefrenceUtils.getInt(UIUtils.getContext(),"action",0);
//        if(action==2){
//            Intent intent=new Intent(mContext, GameRoomActivity.class);
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
//            Intent intent=new Intent(mContext, GameActivity.class);
//            UIUtils.startActivityNextAnim(intent);
//        }else {
        WebSocketHandler.getDefault().addListener(socketListener);
        addGameLobby();
//        }
    }
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TcpService.ClientBinder clientBinder = (TcpService.ClientBinder) service;
            clientBinder.startConnect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };




    private void addGameLobby(){
        LogUtils.e("roomResponse请求");
        SharedPrefrenceUtils.setInt(UIUtils.getContext(),"action",1);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("authorization", SharedPrefrenceUtils.getString(mContext,"token"));
            jsonObject.put("action", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(jsonObject.toString());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_200: {
                Intent intent = new Intent(mContext, GameRoomActivity.class);
                intent.putExtra("type",200);
                intent.putExtra("title",roomResponse.dataList.get(0).title);
                intent.putExtra("id",1);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.iv_500: {
                Intent intent = new Intent(mContext, GameRoomActivity.class);
                intent.putExtra("type",500);
                intent.putExtra("title",roomResponse.dataList.get(1).title);
                intent.putExtra("id",2);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.iv_1000: {
                Intent intent = new Intent(mContext, GameRoomActivity.class);
                intent.putExtra("type",1000);
                intent.putExtra("title",roomResponse.dataList.get(2).title);
                intent.putExtra("id",3);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.iv_2000: {
                Intent intent = new Intent(mContext, GameRoomActivity.class);
                intent.putExtra("type",2000);
                intent.putExtra("title",roomResponse.dataList.get(3).title);
                intent.putExtra("id",4);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
//            case R.id.rl_menu:{
//                Intent intent=new Intent(mContext, MenuManagementActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_report:{
//                Intent intent=new Intent(mContext, InformationReportActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_message:{
//                Intent intent=new Intent(mContext, MessageActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_set:{
//                Intent intent=new Intent(mContext,MerchantSettingActivity   .class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_marketing:{
//                Intent intent=new Intent(mContext,MarketingActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_order_manager:{
//                Intent intent=new Intent(mContext,OrderManagerActivity.class);
//                com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                break;
//            }
//            case R.id.rl_member_manager:{
//                if(com.ciba.wholefinancial.util.SharedPrefrenceUtils.getInt(mContext,"master",0)==1){
//                    Intent intent=new Intent(mContext, MemberManagerActivity.class);
//                    com.ciba.wholefinancial.util.UIUtils.startActivityNextAnim(intent);
//                }else{
//                    com.ciba.wholefinancial.util.DialogUtils.showAlertDialog(mContext, "您没有权限查看！");
//                }
//
//                break;
//            }
        }
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
//            if( SharedPrefrenceUtils.getInt(UIUtils.getContext(),"action",0)==1) {
                Gson gson = new Gson();
                roomResponse = gson.fromJson(message, RoomResponse.class);
                LogUtils.e("roomResponse:" + roomResponse.toString());
//            }
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            LogUtils.e("message2:"+bytes);
//            appendMsgDisplay("onMessage(ByteBuffer, T):" + bytes);
        }
    };


}
