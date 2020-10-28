package com.yishangshuma.bangelvyou.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.util.AsyncExecuter;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.wibget.CircleImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 账户管理中心
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener{

    private View imgItem;
    private CircleImageView myImg;// 我的头像
    private TextView myName;// 我的用户名
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button cancel;//取消
    private View parentView;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取
    private static final int TAKE_PICTURE_Lo = 2;//本地获取
    private Bitmap icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView=getLayoutInflater().inflate(R.layout.activity_account,null);
        setContentView(parentView);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("账户中心");
        setLeftBackButton();
        imgItem = findViewById(R.id.rel_account_selecticon);
        myImg = (CircleImageView) findViewById(R.id.img_account_usericon);
        myName = (TextView) findViewById(R.id.tv_account_username);
        myName.setText(configEntity.username);//设置用户名
        pop = new PopupWindow(AccountActivity.this);
        View view = getLayoutInflater().inflate(R.layout.pop_user_choseicon,null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout pop_layout=(RelativeLayout) view.findViewById(R.id.pop_parent);
        pop_layout.getBackground().setAlpha(60);//设置背景透明度
        use_camera = view.findViewById(R.id.use_camera);
        use_local = view.findViewById(R.id.use_local);
        cancel = (Button) view.findViewById(R.id.item_cancel);
        File iconFile = new File(CacheImgUtil.user_icon);
        if (iconFile.exists()) {
            String path=iconFile.getAbsolutePath();
            icon = BitmapFactory.decodeFile(path);
            myImg.setImageBitmap(icon);
        }else{
            myImg.setImageResource(R.mipmap.touxiang);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        imgItem.setOnClickListener(this);
        use_camera.setOnClickListener(this);
        use_local.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_account_selecticon://显示图像获取方式（拍照/本地/取消）
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.use_camera://打开照相机
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAKE_PICTURE_Lo);
                pop.dismiss();
                break;
            case R.id.item_cancel://取消
                pop.dismiss();
                break;
            default:
                break;
        }
    }

    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.arg1) {
            case 1:
//                disMissDialog();
                File iconFile = new File(CacheImgUtil.user_icon);
                String path=iconFile.getAbsolutePath();
                icon= BitmapFactory.decodeFile(path);
                myImg.setImageBitmap(icon);
                break;
        }
    }

    /**
     * 返回方法获取到头像
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_Ca://相机获取图片
                if (resultCode == RESULT_OK) {
                    if (data == null|| "".equals(data)) {
                        return;
                    }
                    icon = (Bitmap) data.getExtras().get("data");
                    saveIconImg();
                }
                break;
            case TAKE_PICTURE_Lo://本地图片
                if (resultCode == RESULT_OK) {
                    if (data==null || "".equals(data)) {
                        return;
                    }
//                    showLoadingDialog();
                    Uri thisUri = data.getData();
                    ContentResolver resolver = getContentResolver();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // 设置options.inJustDecodeBounds为true
                    options.inJustDecodeBounds = true;
                    // 节约内存
                    options.inPreferredConfig = Bitmap.Config.RGB_565; // 默认是Bitmap.Config.ARGB_8888
				/* 下面两个字段需要组合使用 */
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    try {
                        //此时不会把图片读入内存，只会获取图片宽高等信息
                        icon = BitmapFactory.decodeStream(resolver
                                .openInputStream(thisUri), null, options);
                        int heitht = options.outHeight;
                        // 根据需要设置压缩比例
                        int size = heitht / 600;
                        if (size <= 0) {
                            size = 1;
                        }
                        options.inSampleSize = size;
                        // 设置options.inJustDecodeBounds重新设置为false
                        options.inJustDecodeBounds = false;
                        //此时图片会按比例压缩后被载入内存中
                        icon = BitmapFactory.decodeStream(resolver
                                .openInputStream(thisUri), null, options);
                        saveIconImg();
                    } catch (FileNotFoundException e) {
//                        disMissDialog();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 存储用户图像手机存储卡
     * 图片大小超过100KB会进行压缩
     */
    public void saveIconImg(){
        AsyncExecuter.getInstance().execute(new Runnable() {
            public void run() {
                try {
                    File imgFile = new File(CacheImgUtil.user_icon);
                    if (imgFile.exists()) {
                        imgFile.delete();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    int option=100;
                    while (baos.toByteArray().length/1024 > 100) {
                        //头像大于100k进行压缩
                        baos.reset();
                        option -= 10;
                        icon.compress(Bitmap.CompressFormat.JPEG, option, baos);//压缩图片
                    }
                    ByteArrayInputStream is =  new ByteArrayInputStream(baos.toByteArray());
                    FileOutputStream fos;
                    fos = new FileOutputStream(CacheImgUtil.user_icon);
                    byte[] buffer = new byte[8192];
                    int count = 0;
                    while ((count = is.read(buffer)) > 0)
                    {
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();
                    icon.recycle();
                    icon = null;
                    Message msg = mHandler.obtainMessage();
                    msg.arg1=1;
                    msg.sendToTarget();
                } catch (FileNotFoundException e) {
//                    disMissDialog();
                    e.printStackTrace();
                } catch (IOException e) {
//                    disMissDialog();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != icon) {
            icon.recycle();
            icon = null;
        }
    }

}
