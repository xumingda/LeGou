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
import com.xq.LegouShop.bean.UserBean;
import com.xq.LegouShop.protocol.UpdateUserInfoProtocol;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.ImageResponse;
import com.xq.LegouShop.util.BitmapUtils;
import com.xq.LegouShop.util.Constants;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;
import com.xq.LegouShop.util.MobileUtils;
import com.xq.LegouShop.util.PictureOption;
import com.xq.LegouShop.util.ProviderUtil;
import com.xq.LegouShop.util.SPUtils;
import com.xq.LegouShop.util.SharedPrefrenceUtils;
import com.xq.LegouShop.util.UIUtils;
import com.xq.LegouShop.weiget.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//个人资料
public class PersonalInfoActivity extends BaseActivity  {

    private LayoutInflater mInflater;
    private View rootView;
    private String headurl;
//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;

    private String code;
    private String phoneNumber;
    private View view_back,view_update;
    private UserBean userBean;
    private TextView et_nick,et_name,et_sex,et_phone,et_email;
    private CircleImageView iv_myself_img;

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
    private ImageLoader imageLoader;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_personal_info, null);
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
          iv_myself_img.setImageBitmap(bitmap);
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
                    headurl=imageResponse.getFileKey();
                    UIUtils.showToastCenter(PersonalInfoActivity.this, "上传成功");
                    updateInfo();
                } else {
                    DialogUtils.showAlertDialog(PersonalInfoActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(PersonalInfoActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(PersonalInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }
    public void initDate(){
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
                            if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(PersonalInfoActivity.this,
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
                                    uri = FileProvider.getUriForFile(PersonalInfoActivity.this, ProviderUtil.getFileProviderName(PersonalInfoActivity.this), mFile);
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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));

        userBean= SPUtils.getBeanFromSp(this,"userInfo","userInfo");
        loadingDialog = DialogUtils.createLoadDialog(PersonalInfoActivity.this, false);
        iv_myself_img=(CircleImageView)findViewById(R.id.iv_myself_img);
        rl_main=(RelativeLayout)findViewById(R.id.rl_main);
        et_nick=(TextView) findViewById(R.id.et_nick);
        et_name=(TextView) findViewById(R.id.et_name);
        et_sex=(TextView) findViewById(R.id.et_sex);
        et_phone=(TextView) findViewById(R.id.et_phone);
        et_email=(TextView) findViewById(R.id.et_email);
        view_update=(View)findViewById(R.id.view_update);
        view_back=(View)findViewById(R.id.view_back);

//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);
        iv_myself_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(rl_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

        view_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonalInfoActivity.this, UpdatePersonalInfoActivity.class);
                UIUtils.startActivityNextAnim(intent);
                finish();
            }
        });
        et_nick.setText(userBean.getNickName());
        et_name.setText(userBean.getUserName());
        et_sex.setText(userBean.getSex());
        et_email.setText(userBean.getEmail());
        et_phone.setText(userBean.getPhoneNumber());
        imageLoader.displayImage("http://qiniu.lelegou.pro/"+userBean.getHeadurl(),iv_myself_img, PictureOption.getSimpleOptions());


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
                        uri = BitmapUtils.getPictureUri(data, PersonalInfoActivity.this);
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

    //修改头像
    public void updateInfo() {
        loadingDialog.show();
        UpdateUserInfoProtocol updateUserInfoProtocol = new UpdateUserInfoProtocol();
        HashMap<String,String> hashMap=new HashMap<>();
        String url = updateUserInfoProtocol.getApiFun();
        hashMap.put("headurl", headurl);
   ;


        MyVolley.uploadNoFile(MyVolley.POST, url, hashMap, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code == 0) {
                    userBean.setHeadurl(headurl);
                    SPUtils.saveBean2Sp(PersonalInfoActivity.this,userBean,"userInfo","userInfo");
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(PersonalInfoActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(PersonalInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }




}
