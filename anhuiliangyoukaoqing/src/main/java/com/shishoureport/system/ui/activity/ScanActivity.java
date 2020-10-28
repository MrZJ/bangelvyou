package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.shishoureport.system.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


/**
 * 扫描界面
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private QRCodeView mQRCodeView;
    private String codeType;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeType = getIntent().getStringExtra("codeType");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan;
    }

    public void initView() {
        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        TextView top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("扫一扫");
    }


    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        mQRCodeView.stopSpot();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        MyWebActivity.startActivity(this, result, "扫码结果");
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "扫描二维码失败，请重试", Toast.LENGTH_SHORT).show();
    }

}