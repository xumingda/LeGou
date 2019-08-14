package com.xq.LegouShop.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.protocol.UpdatePwdProtocol;
import com.xq.LegouShop.response.ImageResponse;
import com.xq.LegouShop.response.UpdatePwdResponse;
import com.xq.LegouShop.util.BitmapUtils;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MD5Utils;
import com.xq.LegouShop.util.MobileUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.ProviderUtil;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//退款申请
public class ApplicationDrawbackActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private ImageLoader imageLoader;
    private Dialog loadingDialog;
    private Bitmap new_photo;
    public static boolean isForeground = false;
    private View view_back;
    private TextView tv_time;
    private TextView tv_shopName;
    private TextView tv_order_no;
    private TextView tv_name;
    private TextView tv_num;
    private TextView tv_price,tv_title;
    private ImageView iv_pic,iv_shop_in_pic,iv_shop_in_pic_two,iv_shop_in_pic_three,iv_shop_in_pic_four,iv_shop_in_pic_five,iv_shop_in_pic_six;
    private  String orderNo,time,pic,goodName,bugCount,orderMoney,shopName;
    private RelativeLayout rl_main,rl_state,rl_money;
    private File mFile;
    private String timepath;
    private int type;


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
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    //0-5,总共6张
    private int pic_type;
    private TextView tv_why_title;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_application_drawback, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }




    private void initDate() {
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        orderNo=intent.getStringExtra("orderNo");
        shopName=intent.getStringExtra("shopName");
        time=intent.getStringExtra("time");
        pic=intent.getStringExtra("pic");
        goodName=intent.getStringExtra("goodName");
        bugCount=intent.getStringExtra("bugCount");
        orderMoney=intent.getStringExtra("orderMoney");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        loadingDialog = DialogUtils.createLoadDialog(ApplicationDrawbackActivity.this, false);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        rl_state=(RelativeLayout) findViewById(R.id.rl_state);
        rl_money=(RelativeLayout) findViewById(R.id.rl_money);
        view_back=(View)findViewById(R.id.view_back);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_why_title= (TextView) findViewById(R.id.tv_why_title);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_order_no= (TextView) findViewById(R.id.tv_order_no);
        tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_price= (TextView)findViewById(R.id.tv_price);
        iv_pic= (ImageView) findViewById(R.id.iv_pic);
        iv_shop_in_pic= (ImageView) findViewById(R.id.iv_shop_in_pic);
        iv_shop_in_pic_two= (ImageView) findViewById(R.id.iv_shop_in_pic_two);
        iv_shop_in_pic_three= (ImageView) findViewById(R.id.iv_shop_in_pic_three);
        iv_shop_in_pic_four= (ImageView) findViewById(R.id.iv_shop_in_pic_four);
        iv_shop_in_pic_five= (ImageView) findViewById(R.id.iv_shop_in_pic_five);
        iv_shop_in_pic_six= (ImageView) findViewById(R.id.iv_shop_in_pic_six);
        view_back.setOnClickListener(this);
        iv_shop_in_pic.setOnClickListener(this);
        iv_shop_in_pic_two.setOnClickListener(this);
        iv_shop_in_pic_three.setOnClickListener(this);
        iv_shop_in_pic_four.setOnClickListener(this);
        iv_shop_in_pic_five.setOnClickListener(this);
        iv_shop_in_pic_six.setOnClickListener(this);
        if(type==1){
            rl_state.setVisibility(View.GONE);
            tv_why_title.setText("退款原因");
            tv_title.setText("退款申请");
        }else if(type==2){
            rl_state.setVisibility(View.VISIBLE);
            tv_why_title.setText("退货原因");
            tv_title.setText("退货申请");
        }else{
            rl_state.setVisibility(View.GONE);
            tv_why_title.setText("换货原因");
            rl_money.setVisibility(View.GONE);
            tv_title.setText("换货申请");
        }
        if(!TextUtils.isEmpty(shopName)) {
            tv_shopName.setText(shopName);
        }else{
            tv_shopName.setText("平台自营");
        }
        tv_order_no.setText("订单号:"+orderNo);

        tv_time.setText(time);
        tv_name.setText(goodName);
        tv_price.setText("¥"+orderMoney);
        tv_num.setText("X"+bugCount);
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+pic, iv_pic, PictureOption.getSimpleOptions());

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
                            if (ContextCompat.checkSelfPermission(ApplicationDrawbackActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ApplicationDrawbackActivity.this,
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
                                    uri = FileProvider.getUriForFile(ApplicationDrawbackActivity.this, ProviderUtil.getFileProviderName(ApplicationDrawbackActivity.this), mFile);
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
    private void addBitmap(Bitmap bitmap){

        switch (pic_type){
            case 0: {
                iv_shop_in_pic.setImageBitmap(bitmap);
                iv_shop_in_pic_two.setVisibility(View.VISIBLE);
                break;
            } case 1: {
                iv_shop_in_pic_two.setImageBitmap(bitmap);
                iv_shop_in_pic_three.setVisibility(View.VISIBLE);
                break;
            }case 2: {
                iv_shop_in_pic_three.setImageBitmap(bitmap);
                iv_shop_in_pic_four.setVisibility(View.VISIBLE);
                break;
            }case 3: {
                iv_shop_in_pic_four.setImageBitmap(bitmap);
                iv_shop_in_pic_five.setVisibility(View.VISIBLE);
                break;
            }
            case 4: {
                iv_shop_in_pic_five.setImageBitmap(bitmap);
                iv_shop_in_pic_six.setVisibility(View.VISIBLE);
                break;
            }case 5: {
                iv_shop_in_pic_six.setImageBitmap(bitmap);
                break;
            }
        }
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

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    addBitmap(new_photo);
//                    uploadImage();
                    break;

            }
        }
    };
    /**
     * 上传图片
     */
    private void uploadImage() {
        loadingDialog.show();
        String url = Constants.SERVER_URL + "/common/uploadImg";
        Map<String, String> paramMap = new HashMap<String, String>();
        String token = SharedPrefrenceUtils.getString(UIUtils.getContext(), "token");
        if (!TextUtils.isEmpty(token)) {
            paramMap.put("authorization", token);
        }
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("picFile", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                ImageResponse imageResponse = gson.fromJson(json, ImageResponse.class);

                LogUtils.e("baseResponsexmd:" + json.toString());
//                if (imageResponse.code == 0) {
//                    LogUtils.e("baseResponsexmd:" + pic_type);
//                    switch (pic_type){
//
//                        case 0: {
//                            licensePic=imageResponse.picUrl;
//                            break;
//                        } case 1: {
//                            doorOutPic=imageResponse.picUrl;
//                            break;
//                        }case 2: {
//                            indoorPic=imageResponse.picUrl;
//                            doorInPics.add(0,imageResponse.picUrl);
//                            break;
//                        }case 3: {
//                            doorInPics.add(1,imageResponse.picUrl);
//                            break;
//                        }
//                        case 4: {
//                            doorInPics.add(2,imageResponse.picUrl);
//                            break;
//                        }case 5: {
//                            managerIdcardPic1=imageResponse.picUrl;
//                            break;
//                        }
//                    }
//                    UIUtils.showToastCenter(ApplicationDrawbackActivity.this, "上传成功");
//                } else {
//                    DialogUtils.showAlertDialog(ApplicationDrawbackActivity.this,
//                            imageResponse.msg);
//
//                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ApplicationDrawbackActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ApplicationDrawbackActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_shop_in_pic:{
                pic_type=0;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_two:{
                pic_type=1;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_three:{
                pic_type=2;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_four:{
                pic_type=3;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_five:{
                pic_type=4;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_six:{
                pic_type=5;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
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
                            Bitmap photo = BitmapUtils.getimage(path);
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
                        uri = BitmapUtils.getPictureUri(data, ApplicationDrawbackActivity.this);
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
                                Bitmap photo = BitmapUtils.getimage(path);
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
}
