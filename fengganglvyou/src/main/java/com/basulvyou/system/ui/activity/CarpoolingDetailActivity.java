package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.PassengerEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * 拼车详情
 */
public class CarpoolingDetailActivity extends BaseActivity implements View.OnClickListener {

    private CarpoolingInfoEntity info;
    private TextView carpoolStatus, titlecarpoolDate, carpoolDate, carpoolStartAds, carpoolEndAds, senderName;
    private String telNum;
    private View ownerInfo, linearBtn;
    private Button cancel, sure;
    private String state, orderType;
    private String type;//区分发布还是预定
    private ImageView carpooling_detail_icon;
    private LinearLayout ll_parent;
    private ImageLoader imageLoader;
    private View carpooling_detail_status, carpooling_detail_statusline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_detail);
        imageLoader = ImageLoader.getInstance();
        info = (CarpoolingInfoEntity) getIntent().getSerializableExtra("carpoolingInfo");
        type = getIntent().getStringExtra("type");
        orderType = getIntent().getStringExtra("orderType");
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("订单详情");
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        carpoolStatus = (TextView) findViewById(R.id.tv_carpooling_detail_status);
        carpooling_detail_status = findViewById(R.id.carpooling_detail_status);
        carpooling_detail_statusline = findViewById(R.id.carpooling_detail_statusline);
        titlecarpoolDate = (TextView) findViewById(R.id.carpooling_detail_date);
        carpoolDate = (TextView) findViewById(R.id.tv_carpooling_detail_date);
        carpoolStartAds = (TextView) findViewById(R.id.tv_carpooling_detail_startads);
        carpoolEndAds = (TextView) findViewById(R.id.tv_carpooling_detail_endads);
        ownerInfo = findViewById(R.id.rel_carpooling_detail_info);
        senderName = (TextView) findViewById(R.id.tv_rescue_detail_tel);//信息发布者名称
        carpooling_detail_icon = (ImageView) findViewById(R.id.img_carpooling_detail_icon);//用户头像
        linearBtn = findViewById(R.id.lin_carpooling_detail_btn);
        cancel = (Button) findViewById(R.id.btn_carpooling_detail_cancel);
        sure = (Button) findViewById(R.id.btn_carpooling_detail_sure);
        if (null != info) {
            if (info.user_logo != null) {
                imageLoader.displayImage(info.user_logo, carpooling_detail_icon,
                        AsynImageUtil.getImageOptions(R.mipmap.user_log), null);
            }
            if (info.comm_type.equals("104")) {
                titlecarpoolDate.setText("发布时间");
                carpoolDate.setText(info.up_date);
            } else {
                carpoolDate.setText(info.down_date);
            }
            if (!StringUtil.isEmpty(info.order_state)) {
                if (info.order_state.equals("0")) {
                    carpoolStatus.setText("进行中");
                    linearBtn.setVisibility(View.VISIBLE);
                } else if (info.order_state.equals("-10")) {
                    carpoolStatus.setText("已取消");
                } else if (info.order_state.equals("90")) {
                    carpoolStatus.setText("已完成");
                } else if (info.order_state.equals("40")) {
                    carpoolStatus.setText("已确定");
                }
            } else {
                if (!ListUtils.isEmpty(info.order_list)) {
                    if (info.order_list.get(0).order_state.equals("0")) {
                        carpoolStatus.setText("进行中");
                        linearBtn.setVisibility(View.VISIBLE);
                    } else if (info.order_list.get(0).order_state.equals("-10")) {
                        carpoolStatus.setText("已取消");
                    } else if (info.order_list.get(0).order_state.equals("90")) {
                        carpoolStatus.setText("已完成");
                    } else if (info.order_list.get(0).order_state.equals("40")) {
                        carpoolStatus.setText("已确定");
                    }
                }
            }
            hideStatus();
            if (!ListUtils.isEmpty(info.order_list)) {
//                cancel.setVisibility(View.GONE);
//                sure.setVisibility(View.GONE);
                for (int i = 0; i < info.order_list.size(); i++) {
                    final PassengerEntity passengerEntity = info.order_list.get(i);
                    View ll_carpooling_detail_info = View.inflate(this, R.layout.item_carpooling_detail_info, null);
                    ll_carpooling_detail_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(passengerEntity.rel_phone)) {
                                callTel(passengerEntity.rel_phone.trim());
                            }
                        }
                    });
                    TextView name = (TextView) ll_carpooling_detail_info.findViewById(R.id.tv_rescue_detail_tel);
                    name.setText(passengerEntity.rel_name);
                    ImageView log_iv = (ImageView) ll_carpooling_detail_info.findViewById(R.id.img_carpooling_detail_icon);
                    if (passengerEntity.order_pic != null) {
                        imageLoader.displayImage(passengerEntity.order_pic, log_iv,
                                AsynImageUtil.getImageOptions(R.mipmap.owner_icon), null);
                    }
                    ll_parent.addView(ll_carpooling_detail_info);
                }
            }
            carpoolStartAds.setText(info.pcqd);
            carpoolEndAds.setText(info.pczd);
            telNum = info.goods_content.trim();
            senderName.setText(info.user_name);
            if ("yd".equals(type)){
                ownerInfo.setVisibility(View.VISIBLE);
            } else {
                ownerInfo.setVisibility(View.GONE);
            }
            if ("101".equals(orderType) || "102".equals(orderType)) {
                linearBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        ownerInfo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_carpooling_detail_info:
                callTel();
                break;
            case R.id.btn_carpooling_detail_cancel:
                state = "-10";
                chageStatus();
                break;
            case R.id.btn_carpooling_detail_sure:
                state = "90";
                chageStatus();
                break;
        }
    }

    private void chageStatus() {
        httpPostRequest(CarpoolingApi.changeCarpoolingStatus(), getRequestParams(), CarpoolingApi.API_CHANGE_CAPOOLING);
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("order_id", info.order_id);
        params.put("comm_id", info.goods_id);
        params.put("order_state", state);
        return params;
    }

    @Override
    public void httpOnResponse(String json, int action) {
        super.httpOnResponse(json, action);
        switch (action) {
            case CarpoolingApi.API_CHANGE_CAPOOLING:
                Toast.makeText(this, "修改订单状态成功", Toast.LENGTH_SHORT).show();
                CarpoolingDetailActivity.this.finish();
                break;
        }
    }

    /**
     * 拨打电话
     */
    private void callTel() {
        if (!StringUtil.isEmpty(telNum)) {
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telNum));
            startActivity(phoneIntent);
        }
    }

    /**
     * 拨打电话
     */
    private void callTel(String telNum) {
        if (!StringUtil.isEmpty(telNum)) {
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telNum));
            startActivity(phoneIntent);
        }
    }

    private void hideStatus() {
        if (StringUtil.isEmpty(carpoolStatus.getText().toString())) {
            carpooling_detail_status.setVisibility(View.GONE);
            carpooling_detail_statusline.setVisibility(View.GONE);
            carpoolStatus.setVisibility(View.GONE);
        }
    }
}
