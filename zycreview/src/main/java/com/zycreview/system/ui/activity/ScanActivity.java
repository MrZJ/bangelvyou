package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.zycreview.system.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setTitle("扫一扫");
        setLeftBackButton();
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startQRcode();
    }

    public void startQRcode() {
        mQRCodeView.openFlashlight();
        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.closeFlashlight();
        mQRCodeView.stopCamera();
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
        if (result != null && result.contains("prceCode")) {
            Intent intent = new Intent(this, ScanResultActivity.class);
            intent.putExtra("scanUrl", result);
            startActivity(intent);
        } else if (result != null) {
            Intent intent = new Intent(this, ScanResultActivity.class);
            result = "http://sxszyczs.com/m/prceCode-" + result + ".shtml";
            intent.putExtra("scanUrl", result);
            startActivity(intent);
        }
        vibrate();
        mQRCodeView.stopSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
    }

}
