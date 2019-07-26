package com.xq.LegouShop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.xq.LegouShop.activity.MainActivity;
import com.xq.LegouShop.util.Constants;
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
    private String WX_APP_ID = "wx4924485b2d780ead";
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    private String WX_APP_SECRET = "ed447e1a8188fb8aca5f809994b699b5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                parseJSONWithGSON(responseData);
                            }

                            @Override
                            public void failed(Call call, IOException e) {

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
                        parseJSONUser(str);
                    }

                    @Override
                    public void failed(Call call, IOException e) {

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
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("url", Constants.SERVER_URL+"login.html?openid="+appList.getOpenid()+"&userimg="+appList.getHeadimgurl());
        UIUtils.startActivityNextAnim(intent);
        finish();
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

}