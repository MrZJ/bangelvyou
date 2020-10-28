package com.zycreview.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zycreview.system.R;
import com.zycreview.system.ui.activity.ScanActivity;
import com.zycreview.system.ui.activity.ScanResultActivity;
import com.zycreview.system.utils.ConfigUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ScanFragment extends BaseFragment implements QRCodeView.Delegate, View.OnClickListener {

    private View mView;
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    private TextView chosePic;
    /*private final int WRITE_EXTERNAL_STORAGE = 2;
    private final int CAMERA = 1;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_scan, container, false);
        initView();
        initListener();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        setTitle("中药材追溯扫描");
        hideBackBtn();
        mQRCodeView = (ZXingView) mView.findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        chosePic = (TextView) mView.findViewById(R.id.choose_qrcde_from_gallery);

    }

    private void initListener() {
        chosePic.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        startQRcode();
    }

    public void startQRcode() {
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    public void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result != null && result.contains("prceCode")) {
            Intent intent = new Intent(getActivity(), ScanResultActivity.class);
            intent.putExtra("scanUrl", result);
            startActivity(intent);
        } else if (result != null) {
            Intent intent = new Intent(getActivity(), ScanResultActivity.class);
            result = ConfigUtil.HTTP +"/m/prceCode-" + result + ".shtml";
            intent.putExtra("scanUrl", result);
            startActivity(intent);
        }
        vibrate();
        mQRCodeView.stopSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(getActivity(), "扫描失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_qrcde_from_gallery:
                intentExternal();
                break;
        }
    }

    /**
     * 跳转读取图片界面
     */
    public void intentExternal() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
