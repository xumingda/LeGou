package com.xq.LegouShop.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.AddAuthenticationInfoProtocol;
import com.xq.LegouShop.protocol.GetAuthenticationInfoProtocol;
import com.xq.LegouShop.protocol.LoginProtocol;
import com.xq.LegouShop.request.LoginRequest;
import com.xq.LegouShop.response.GetAuthenticationInfoResponse;
import com.xq.LegouShop.response.ImageResponse;
import com.xq.LegouShop.response.LoginResponse;
import com.xq.LegouShop.util.BitmapUtils;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.MobileUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.ProviderUtil;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//实名认证
public class CertificationActivity extends BaseActivity implements View.OnClickListener {

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
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private View view_back;
    private ImageLoader imageLoader;
    private ImageView iv_idcard_font,iv_idcard_back,iv_idcard_inhand,iv_idcard_font_add,iv_idcard_back_add,iv_idcard_inhand_add;
    private EditText et_name,et_num;
    private Button btn_submit;
    private String realName,idCard,pic1,pic2,pic3;
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private String path;
    private Bitmap new_photo;
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private RelativeLayout rl_main;
    private File mFile;
    private String timepath;
    //0正面，1反面，2手持
    private int type;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_cartification, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }


    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
    private void addBitmap(Bitmap bitmap){
        switch (type){
            case 0:{
                iv_idcard_font.setImageBitmap(bitmap);
                break;
            }case 1:{
                iv_idcard_back.setImageBitmap(bitmap);
                break;
            }case 2:{
                iv_idcard_inhand.setImageBitmap(bitmap);
                break;
            }
        }

    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        loadingDialog.show();
        String url = Constants.SERVER_URL + "/common/uploadFile";
        Map<String, String> paramMap = new HashMap<String, String>();
//        String token = SharedPrefrenceUtils.getString(UIUtils.getContext(), "token");
//        if (!TextUtils.isEmpty(token)) {
//            paramMap.put("authorization", token);
//        }
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("file", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                ImageResponse imageResponse = gson.fromJson(json, ImageResponse.class);

                LogUtils.e("baseResponsexmd:" + json.toString());
                if (imageResponse.code == 0) {
                    switch (type){
                        case 0:{
                            pic1=imageResponse.getFileKey();
                            break;
                        }case 1:{
                            pic2=imageResponse.getFileKey();
                            break;
                        }case 2:{
                            pic3=imageResponse.getFileKey();
                            break;
                        }
                    }

                    UIUtils.showToastCenter(CertificationActivity.this, "上传成功");
                } else {
                    DialogUtils.showAlertDialog(CertificationActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CertificationActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CertificationActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    addBitmap(new_photo);
                    uploadImage();
                    break;

            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";
            File file = new File(path);
            String img_path = path;
            if (file.exists()) {
                new Thread() {
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getSmallBitmap(path);
                            if (photo != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));


                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
                                path = BitmapUtils.saveMyBitmap(suoName, new_photo);

                                handler.sendEmptyMessage(INTERCEPT);

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    ;
                }.start();
                //通知相册刷新
                Uri uriData = Uri.parse("file://" + img_path);
                UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
            }

        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
            if (data.getData() != null) {
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                Uri uri = data.getData();
                if(uri==null){
                    geturi(getIntent());
                }
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, CertificationActivity.this);
                        cursor = managedQuery(uri, proj, null, null, null);
                    }
                    if (cursor != null) {
                        // 获取索引
                        int photocolumn = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标一直开头
                        cursor.moveToFirst();
                        // 根据索引值获取图片路径
                        path = cursor.getString(photocolumn);
                    } else {
                        path = uri.getPath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (MobileUtils.getSDKVersionNumber() < 14) {
                            cursor.close();
                        }
                    }
                }


                if (!TextUtils.isEmpty(path)) {

                    new Thread() {
                        public void run() {
                            try {
                                Bitmap photo = BitmapUtils.getSmallBitmap(path);
                                if (photo != null) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));

                                    String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                            .format(new Date());
                                    path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                                    handler.sendEmptyMessage(INTERCEPT);

                                }

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initDate() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(CertificationActivity.this, false);
        rl_main=(RelativeLayout)rootView.findViewById(R.id.rl_main);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        iv_idcard_font=(ImageView)rootView.findViewById(R.id.iv_idcard_font);
        iv_idcard_inhand=(ImageView)rootView.findViewById(R.id.iv_idcard_inhand);
        iv_idcard_back=(ImageView)rootView.findViewById(R.id.iv_idcard_back);
        iv_idcard_font_add=(ImageView)rootView.findViewById(R.id.iv_idcard_font_add);
        iv_idcard_inhand_add=(ImageView)rootView.findViewById(R.id.iv_idcard_inhan_add);
        iv_idcard_back_add=(ImageView)rootView.findViewById(R.id.iv_idcard_bac_add);
        view_back=(View)findViewById(R.id.view_back);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_num = (EditText) rootView.findViewById(R.id.et_num);
//        et_old_pwd = (EditText) rootView.findViewById(R.id.et_old_pwd);
        view_back.setOnClickListener(this);
        iv_idcard_font_add.setOnClickListener(this);
        iv_idcard_back_add.setOnClickListener(this);
        iv_idcard_inhand_add.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        getAuthenticationInfo();

        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        root.findViewById(R.id.btn_Phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                UIUtils.startActivityForResult(intent, PHOTO_ZOOM);
            }
        });
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pWindow.dismiss();
                        timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            mFile = new File(dir, timepath + ".jpg");
                            if (ContextCompat.checkSelfPermission(CertificationActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(CertificationActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            } else {
                                //解决7.0以上手机拍照问题
                                Uri uri;
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                                    uri = Uri.fromFile(mFile);
                                }else{
                                    /**
                                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                                     */
                                    uri = FileProvider.getUriForFile(CertificationActivity.this, ProviderUtil.getFileProviderName(CertificationActivity.this), mFile);
                                }
                                UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                        MediaStore.EXTRA_OUTPUT, uri), PHOTO_GRAPH);
                            }
                        }
                    }
                });
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);

        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.dismiss();
            }
        });
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

    public void getAuthenticationInfo() {
        loadingDialog.show();
        GetAuthenticationInfoProtocol getAuthenticationInfoProtocol = new GetAuthenticationInfoProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        String url = getAuthenticationInfoProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                GetAuthenticationInfoResponse getAuthenticationInfoResponse = gson.fromJson(json, GetAuthenticationInfoResponse.class);
                LogUtils.e("getAuthenticationInfoResponse:" + getAuthenticationInfoResponse.toString());
                if (getAuthenticationInfoResponse.code.equals("0")) {
                    if(!TextUtils.isEmpty(getAuthenticationInfoResponse.data.pic1)) {
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic1, iv_idcard_font, PictureOption.getSimpleOptions());
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic2, iv_idcard_back, PictureOption.getSimpleOptions());
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic2, iv_idcard_inhand, PictureOption.getSimpleOptions());
                    }
                    et_name.setText(getAuthenticationInfoResponse.data.realName);
                    et_num.setText(getAuthenticationInfoResponse.data.idCard);
                    if(getAuthenticationInfoResponse.data.status!=2){
                        btn_submit.setVisibility(View.VISIBLE);
                    }else{
                        btn_submit.setVisibility(View.GONE);
                    }
                } else {
                    DialogUtils.showAlertDialog(CertificationActivity.this,
                            getAuthenticationInfoResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CertificationActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    //实名认证
    public void addAuthenticationInfo() {
        loadingDialog.show();
        AddAuthenticationInfoProtocol getAuthenticationInfoProtocol = new AddAuthenticationInfoProtocol();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("realName",realName);
        hashMap.put("idCard",idCard);
        hashMap.put("pic1",pic1);
        hashMap.put("pic2",pic2);
        hashMap.put("pic3",pic3);



        String url = getAuthenticationInfoProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                GetAuthenticationInfoResponse getAuthenticationInfoResponse = gson.fromJson(json, GetAuthenticationInfoResponse.class);
                LogUtils.e("getAuthenticationInfoResponse:" + getAuthenticationInfoResponse.toString());
                if (getAuthenticationInfoResponse.code.equals("0")) {
                    if(!TextUtils.isEmpty(getAuthenticationInfoResponse.data.pic1)) {
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic1, iv_idcard_font, PictureOption.getSimpleOptions());
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic2, iv_idcard_back, PictureOption.getSimpleOptions());
                        imageLoader.displayImage(getAuthenticationInfoResponse.data.pic2, iv_idcard_inhand, PictureOption.getSimpleOptions());
                    }
                    et_name.setText(getAuthenticationInfoResponse.data.realName);
                    et_num.setText(getAuthenticationInfoResponse.data.idCard);
                    if(getAuthenticationInfoResponse.data.status!=2){
                        btn_submit.setVisibility(View.VISIBLE);
                    }else{
                        btn_submit.setVisibility(View.GONE);
                    }
                } else {
                    DialogUtils.showAlertDialog(CertificationActivity.this,
                            getAuthenticationInfoResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CertificationActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }















    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_idcard_font_add:{
                type=0;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_idcard_bac_add:{
                type=1;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            } case R.id.iv_idcard_inhan_add:{
                type=2;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.btn_submit:{
                realName=et_name.getText().toString();
                idCard=et_num.getText().toString();
                if(!TextUtils.isEmpty(realName)&&!TextUtils.isEmpty(idCard)&&!TextUtils.isEmpty(pic1)&&!TextUtils.isEmpty(pic2)&&!TextUtils.isEmpty(pic3)){
                    addAuthenticationInfo();
                }else{
                    DialogUtils.showAlertDialog(CertificationActivity.this,
                            "请先填写完整的信息！");
                }
                break;
            }
        }
    }
}
