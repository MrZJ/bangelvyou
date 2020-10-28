package com.shishoureport.system.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.activity.BaseActivity;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @date
 **/
public class PreViewActivity extends BaseActivity {
    private WebView webView;
    private ImageView imageView;
    private TextView text;
    private View textContainer;

    public static void startActivity(Context context, String path) {
        Intent intent = new Intent(context, PreViewActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.webView);
        imageView = (ImageView) findViewById(R.id.imageView);
        textContainer = findViewById(R.id.textContainer);
        text = (TextView) findViewById(R.id.text);
        String filePath = getIntent().getStringExtra("path");
        if (filePath == null) {
            return;
        }
        Log.e("jianzhang", filePath);
        if (filePath.endsWith(".pdf")) {
            webView.setVisibility(View.VISIBLE);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowFileAccessFromFileURLs(true);
                webSettings.setAllowUniversalAccessFromFileURLs(true);
            }
            webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + filePath);
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".jepg")) {
            imageView.setVisibility(View.VISIBLE);
            getLocBitmap(filePath);
        } else if (filePath.endsWith(".doc")) {
//            {".doc", "application/msword"},
//            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
//            {".xls", "application/vnd.ms-excel"},
//            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}
            doOpenWord(filePath, "application/msword");
        } else if (filePath.endsWith(".docx")) {
            doOpenWord(filePath, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        } else if (filePath.endsWith(".xls")) {
            doOpenWord(filePath, "application/vnd.ms-excel");
        } else if (filePath.endsWith(".xlsx")) {
            doOpenWord(filePath, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (filePath.endsWith(".txt")) {
            String charSet = "UTF-8";
            try {
                charSet = getCharset(filePath);
            } catch (Exception e) {
            }
            textContainer.setVisibility(View.VISIBLE);
            String texts = loadFromSDFile(filePath, charSet);
            text.setText(texts);
        }
    }

    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord(String path, String fileMimeType) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        String fileMimeType = ;
        try {
            intent.setDataAndType(Uri.fromFile(new File(path)), fileMimeType);
            getApplication().startActivity(intent);
            finish();
        } catch (Exception e) {
//检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
//请先到www.olivephone.com/e.apk下载并安装
        }
    }

    private String loadFromSDFile(String path, String charSet) {
        String result = null;
        try {
            File f = new File(path);
            int length = (int) f.length();
            byte[] buff = new byte[length];
            FileInputStream fin = new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result = new String(buff, charSet);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有找到指定文件", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        int orientation = 0;
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() != 1) {
                return -1;
            }
            cursor.moveToFirst();
            orientation = cursor.getInt(0);
            cursor.close();
        }
        return orientation;
    }

    public static Bitmap rotateImage(Bitmap bmp, int degrees) {
        if (degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return bmp;
    }

    public void getLocBitmap(String path) {
        ContentResolver cr = this.getContentResolver();
        try {
            Uri uri = Uri.fromFile(new File(path));
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            int systemWidth = DensityUtil.getScreenWidth();
            if (bitmap.getWidth() > bitmap.getHeight()) {
                if (bitmap.getHeight() > systemWidth) {
                    bitmap = fitBitmap(bitmap, systemWidth);
                }
                bitmap = rotateImage(bitmap, 90);
            } else if (bitmap.getWidth() > systemWidth) {
                bitmap = fitBitmap(bitmap, systemWidth);
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
        }
    }

    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target   需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }

    private String getCharset(String fileName) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }

}
