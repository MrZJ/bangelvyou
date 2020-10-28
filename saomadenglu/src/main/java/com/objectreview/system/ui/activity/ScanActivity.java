package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.objectreview.system.R;
import com.objectreview.system.entity.MyCodeResult;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

import static com.objectreview.system.ui.activity.MainActivity.EXTRA_DATA;

/**
 * 扫描界面
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private QRCodeView mQRCodeView;
    private String codeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        codeType = getIntent().getStringExtra("codeType");
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("扫一扫");
        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
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
        Log.e("jianzhang", result);
        MyCodeResult myCodeResult = JSONObject.parseObject(result, MyCodeResult.class);
        if (result == null || myCodeResult.scope == null || myCodeResult.access_token == null) {
            Toast.makeText(this, "扫描二维码失败，请重试", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, myCodeResult);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "扫描二维码失败，请重试", Toast.LENGTH_SHORT).show();
    }

}