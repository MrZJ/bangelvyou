package com.shenmaireview.system.ui.fragment;

import android.content.Intent;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.shenmaireview.system.R;
import com.shenmaireview.system.ui.activity.ScanResultActivity;
import com.shenmaireview.system.utils.ConfigUtil;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;



/**
 * Created by Administrator on 2016/9/18.
 */
public class ScanFragment extends BaseFragment implements QRCodeView.Delegate{

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    @Bind(R.id.zxingview)
    QRCodeView mQRCodeView;
    @Bind(R.id.choose_qrcde_from_gallery)
    TextView chosePic;
    /*private final int WRITE_EXTERNAL_STORAGE = 2;
    private final int CAMERA = 1;*/

    @Override
    public void onResume() {
        super.onResume();
        startQRcode();
    }

    @Override
    public void onStart() {
        super.onStart();
        setTopTitle("扫一扫");
        setLeftBackButton(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_scan;
    }

    @Override
    protected void initView() {
        mQRCodeView.setDelegate(this);
    }

    public void startQRcode(){
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
        if(mQRCodeView != null){
            mQRCodeView.onDestroy();
        }
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if(!result.startsWith(ConfigUtil.HTTP)){
            Toast.makeText(getActivity(), "无效的二维码", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getActivity(), ScanResultActivity.class);
            intent.putExtra("scanUrl",result);
            startActivity(intent);
        }
        vibrate();
        mQRCodeView.stopSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        /*if(mQRCodeView != null){
            Toast.makeText(getActivity(), "扫描失败", Toast.LENGTH_SHORT).show();
        }*/
    }

    @OnClick(R.id.choose_qrcde_from_gallery)
    void onGalleyClick(){
        intentExternal();
    }

    /**
     * 跳转读取图片界面
     */
    public void intentExternal(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
