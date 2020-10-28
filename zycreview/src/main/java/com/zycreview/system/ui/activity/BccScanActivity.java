package com.zycreview.system.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zycreview.system.R;
import com.zycreview.system.utils.ConfigUtil;

public class BccScanActivity extends BaseActivity implements View.OnClickListener{

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;
    private boolean isScaning = false;
    private ScanManager mScanManager;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isScaning = false;
            vibrator.vibrate(100);
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            String barcodeStr = new String(barcode, 0, barcodelen);
            if(barcodeStr.startsWith(ConfigUtil.HTTP)){
                changeToDetail(barcodeStr);
            }else{
                Toast.makeText(context,"无效二维码",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Button startScan;
    private Button stopScan;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcc_scan);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setTitle("扫描枪扫描");
        setLeftBackButton();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startScan = (Button) findViewById(R.id.start_scan);
        stopScan = (Button) findViewById(R.id.stop_scan);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        startScan.setOnClickListener(this);
        stopScan.setOnClickListener(this);
    }

    private void changeToDetail(String result){
        Intent intent = new Intent(this,ScanResultActivity.class);
        intent.putExtra("scanUrl",result);
        startActivity(intent);
    }

    private void initScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initScan();
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_scan:
                mScanManager.stopDecode();
                isScaning = true;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mScanManager.startDecode();
                break;
            case R.id.stop_scan:
                mScanManager.stopDecode();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver);
    }
}
