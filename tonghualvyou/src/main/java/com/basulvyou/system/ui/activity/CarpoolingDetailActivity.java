package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.PassengerEntity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;

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
    private String state;
    private LinearLayout ll_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_detail);
        info = (CarpoolingInfoEntity) getIntent().getSerializableExtra("carpoolingInfo");
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("订单详情");
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        carpoolStatus = (TextView) findViewById(R.id.tv_carpooling_detail_status);
        titlecarpoolDate = (TextView) findViewById(R.id.carpooling_detail_date);
        carpoolDate = (TextView) findViewById(R.id.tv_carpooling_detail_date);
        carpoolStartAds = (TextView) findViewById(R.id.tv_carpooling_detail_startads);
        carpoolEndAds = (TextView) findViewById(R.id.tv_carpooling_detail_endads);
        ownerInfo = findViewById(R.id.rel_carpooling_detail_info);
        senderName = (TextView) findViewById(R.id.tv_rescue_detail_tel);//信息发布者名称
        linearBtn = findViewById(R.id.lin_carpooling_detail_btn);
        cancel = (Button) findViewById(R.id.btn_carpooling_detail_cancel);
        sure = (Button) findViewById(R.id.btn_carpooling_detail_sure);
        if (null != info) {
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
                }
            }
            if (!ListUtils.isEmpty(info.order_list)) {
                ownerInfo.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                sure.setVisibility(View.GONE);
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
                    ll_parent.addView(ll_carpooling_detail_info);
                }
            }
            carpoolStartAds.setText(info.pcqd);
            carpoolEndAds.setText(info.pczd);
            telNum = info.goods_content.trim();
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
}
