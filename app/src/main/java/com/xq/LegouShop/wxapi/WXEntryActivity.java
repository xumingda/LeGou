package com.xq.LegouShop.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.LegouShop.activity.LoginActivity;
import com.xq.LegouShop.activity.MainActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.protocol.WeixinLoginProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private BaseResp resp = null;
    private String WX_APP_ID = "wx177036684b434de9";
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    private String WX_APP_SECRET = "d7c36967eaaf0bd328b13968e93f6814";
    private Dialog loadingDialog;
    private String openid;
    private String unionid;
    private String wechatName;
    private String headurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = DialogUtils.createLoadDialog(WXEntryActivity.this, false);
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        if (resp != null) {
            resp = resp;
        }
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                String code = ((SendAuth.Resp) resp).code;
                Log.i("TGA", code + "------------");
                /*
                 * 将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
                 */
                String get_access_token = getCodeRequest(code);
                Map<String, String> reqBody = new ConcurrentSkipListMap<>();
                NetUtils netUtils = NetUtils.getInstance();
                netUtils.postDataAsynToNet(get_access_token, reqBody,
                        new NetUtils.MyNetCall() {


                            @Override
                            public void success(Call call, Response response) throws IOException {

                                String responseData = response.body().string();
                                Log.i("TGA",  "success------------"+responseData);
                                parseJSONWithGSON(responseData);
                            }

                            @Override
                            public void failed(Call call, IOException e) {
                                Log.i("TGA",  "failed------------");
                            }
                        });

                finish();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;

        }
    }
    // 解析返回的数据
    private void parseJSONWithGSON(String jsonData) {
        // 使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        WeixinBean appList = gson.fromJson(jsonData,
                new TypeToken<WeixinBean>() {
                }.getType());
        // 控制台输出结果，便于查看
        String str = getUserInfo(appList.getAccess_token(), appList.getOpenid());
        Log.i("TGA",  "getUserInfosuccess------------"+str);
        getUserInfo(str);
    }



    /**
     * 通过拼接的用户信息url获取用户信息
     *
     * @param user_info_url
     */
    private void getUserInfo(String user_info_url) {
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(user_info_url, reqBody,
                new NetUtils.MyNetCall() {
                    @Override
                    public void success(Call call, Response response) throws IOException{

                        String str = response.body().string();
                        Log.i("TGA",  "successgetUserInfosuccess------------"+str);
                        parseJSONUser(str);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        Log.i("TGA",  "failedUserInfosuccess------------");
                    }

                });

    }
    // 解析用户信息
    private void parseJSONUser(String jsonData) {
        // 使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        WeixinBean appList = gson.fromJson(jsonData,
                new TypeToken<WeixinBean>() {
                }.getType());
        Log.e("信息","信息xmd:"+appList.toString());
        // 控制台输出结果，便于查看
//        Intent intent=new Intent(WXEntryActivity.this,MainActivity.class);
//        String str=appList.getSex()==1?"   性别:男" : "   性别:女";
//
//        intent.putExtra("username", "微信昵称:"+appList.getNickname());
//        intent.putExtra("sex", str);
//        startActivity(intent);
        openid=appList.getOpenid();
        unionid=appList.getUnionid();
        wechatName=appList.getNickname();
        headurl=appList.getHeadimgurl();
        runlogin();
//        Intent intent=new Intent(this, MainActivity.class);
//        intent.putExtra("url", Constants.SERVER_URL+"login.html?openid="+appList.getOpenid()+"&userimg="+appList.getHeadimgurl());
//        UIUtils.startActivityNextAnim(intent);
//        finish();
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code
     *            授权时，微信回调给的
     * @return URL
     */
    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID",
                urlEnodeUTF8(WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET",
                urlEnodeUTF8(WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token
     *            获取access_token时给的
     * @param openid
     *            获取access_token时给的
     * @return URL
     */
    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
                urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void runlogin() {
        WeixinLoginProtocol loginProtocol = new WeixinLoginProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = loginProtocol.getApiFun();
        loginRequest.map.put("openid", openid);
        loginRequest.map.put("unionid",unionid);
        loginRequest.map.put("wechatName", wechatName);
        loginRequest.map.put("headurl",headurl);
        Log.e("信息","信息xmd:1");
        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Log.e("信息","信息xmd:2"+json);
                Gson gson = new Gson();
                LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
                LogUtils.e("weixinloginresponse:" + loginresponse.toString());
                if (loginresponse.code.equals("0")) {
                    SharedPrefrenceUtils.setString(WXEntryActivity.this, "token", loginresponse.getData().getAuthorization());
                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                    finish();

                } else {
                    UIUtils.showToastSafe(  loginresponse.msg);

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                Log.e("信息","信息xmd:3"+error);
                UIUtils.showToastSafe( error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

}