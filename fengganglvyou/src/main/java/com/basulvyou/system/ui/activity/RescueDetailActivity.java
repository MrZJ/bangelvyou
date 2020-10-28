package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 救援厕所详情页面
 */
public class RescueDetailActivity extends BaseActivity implements View.OnClickListener{

    private ImageView rescueImage;
    private TextView rescueName,rescueTel,rescueAddress,rescueTime;
    private Button btnCall;
    private RelativeLayout relCall,relNav;
    private CarpoolingInfoEntity rescue;
    private String telNum;
    private TextView localAds;
    private String type;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_detail);
        type = getIntent().getStringExtra("type");
        rescue = (CarpoolingInfoEntity) getIntent().getSerializableExtra("data");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("详细信息");
        setLeftBackButton();
        rescueImage = (ImageView) findViewById(R.id.img_rescue_detail_icon);
        rescueName = (TextView) findViewById(R.id.tv_rescue_detail_name);
        rescueTel = (TextView) findViewById(R.id.tv_rescue_detail_tel);
        relCall = (RelativeLayout) findViewById(R.id.rel_rescue_detail_call);
        rescueAddress = (TextView) findViewById(R.id.tv_rescue_detail_address);
        localAds = (TextView) findViewById(R.id.tv_item_alarm_rescue_ads);
        relNav = (RelativeLayout) findViewById(R.id.rel_rescue_detail_nav);
        rescueTime = (TextView) findViewById(R.id.tv_rescue_detail_time);
        btnCall = (Button) findViewById(R.id.btn_rescue_detail_call);
        if(null != rescue) {
            if (!ListUtils.isEmpty(rescue.main_pics)) {
                imageLoader.displayImage(rescue.main_pics.get(0).main_pic, rescueImage,
                        AsynImageUtil.getImageOptions(), null);
            } else {
                rescueImage.setBackgroundResource(R.mipmap.alarm_icon);
            }
            rescueName.setText(rescue.goods_name.trim());
            rescueTel.setText(rescue.goods_content.trim());
            rescueAddress.setText(rescue.goods_qtcontent.trim());
            rescueTime.setText(rescue.goods_jdcontent.trim());
            if (!StringUtil.isEmpty(rescue.p_name)) {
                localAds.setText(rescue.p_name.trim());
            } else {
                localAds.setVisibility(View.GONE);
            }
            telNum = rescue.goods_content.trim();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        relCall.setOnClickListener(this);
        relNav.setOnClickListener(this);
        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_rescue_detail_call:
                callTel(telNum);
                break;
            case R.id.rel_rescue_detail_nav:
                if (null != rescue) {
                    if (!StringUtil.isEmpty(rescue.goods_latlng)) {
                        String[] destinationLatLon = rescue.goods_latlng.split(",");
                        Intent intent = new Intent(this, MapActivity.class);
                        intent.putExtra("lat", Double.parseDouble(destinationLatLon[1]));
                        intent.putExtra("lng", Double.parseDouble(destinationLatLon[0]));
                        intent.putExtra("name", rescue.goods_name);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.btn_rescue_detail_call:
                callTel(telNum);
                break;
        }
    }

    /**
     * 拨打电话
     * @param str
     */
    private void callTel(String str) {
        if (!StringUtil.isEmpty(str)) {
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + str));
            startActivity(phoneIntent);
        }
    }

}
