package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.objectreview.system.R;
import com.objectreview.system.utlis.AppManager;
import com.objectreview.system.utlis.StringUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * 扫描界面
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate{

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

    private void initView(){
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
        String codeString = null;
        String code = null;
        if(result.contains("method=view&nu=")){
            String[] strings = result.split("method=view&nu=");
            codeString = "pd_code=" + strings[1];//产品码
            if(!StringUtil.isEmpty(codeType) && codeType.equals("2")) {
                code = strings[1];
            }
        }
        if(result.startsWith("B")){
            codeString = "pack_code=" + result;//包装码
            if(!StringUtil.isEmpty(codeType) && codeType.equals("1")) {
                code = result;
            }
        }
        if(StringUtil.isEmpty(codeType)) {
            if (StringUtil.isEmpty(codeString)) {
                Toast.makeText(this, "无效二维码", Toast.LENGTH_SHORT).show();
                mQRCodeView.startSpot();
            } else {
                if(!(AppManager.getInstance().getPreviousActivity() instanceof ProjectScanInfoActivity)) {
                    Intent projectIntent = new Intent(this, ProjectScanInfoActivity.class);
                    projectIntent.putExtra("code", codeString);
                    startActivity(projectIntent);
                    this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("code", codeString);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        } else {
            if (StringUtil.isEmpty(code)) {
                Toast.makeText(this, "无效二维码", Toast.LENGTH_SHORT).show();
                mQRCodeView.startSpot();
            } else {
                Intent intent = new Intent();
                intent.putExtra("code", code);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this,"扫描二维码失败",Toast.LENGTH_SHORT).show();
    }

}