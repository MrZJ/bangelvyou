package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.OrderStatusAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.EvaluateDetailEntity;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.ui.fragment.CarpoolingListFragment;
import com.basulvyou.system.wibget.MyListView;
import com.basulvyou.system.wibget.MyProgressDialog;

/**
 * Created by jianzhang.
 * on 2017/6/20.
 * copyright easybiz.
 */

public class CarpoolingDetailActivity2 extends BaseActivity implements OrderStatusAdapter.ListItemClick {
    public static final String TYPE = "type";
    public static final int TYPE_COMFIRM = 1;
    public static final int TYPE_GRAB = 2;
    public static final int TYPE_ROBBED = 3;
    private String comm_id, resType;
    private int type;
    private TextView budget_tv, passenger_count, time, start_point, destination, passenger_tip;
    private MyListView myListView;
    private EvaluateDetailEntity evaluateDetailEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_detail);
        Intent intent = getIntent();
        comm_id = intent.getStringExtra(CarpoolingListFragment.COMM_ID);
        resType = intent.getStringExtra("resType");
        type = intent.getIntExtra(TYPE, 0);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        initTopView();
        initListener();
        initSideBarListener();
        if (TYPE_COMFIRM == type) {
            setTitle("待确认订单");
        } else if (TYPE_GRAB == type) {
            setTitle("被抢订单");
        } else if (TYPE_ROBBED == type) {
            setTitle("已抢订单");
        }
        setLeftBackButton();
        budget_tv = (TextView) findViewById(R.id.budget_tv);
        passenger_count = (TextView) findViewById(R.id.passenger_count);
        time = (TextView) findViewById(R.id.time);
        start_point = (TextView) findViewById(R.id.start_point);
        destination = (TextView) findViewById(R.id.destination);
        myListView = (MyListView) findViewById(R.id.listview);
        passenger_tip = (TextView) findViewById(R.id.passenger_tip);
        if ("102".equals(resType)) {
            passenger_tip.setText("车主信息");
        } else {
            passenger_tip.setText("乘客信息");
        }
    }

    private void loadData() {
        httpGetRequest(CarpoolingApi.getOrderCommInfo(comm_id, type, configEntity.key, resType), CarpoolingApi.API_CAPOOLING_ORDER_COMM_INFO);
    }

    @Override
    protected void httpResponse(String json, int action) {
        dismissProgress();
        super.httpResponse(json, action);
        switch (action) {
            case CarpoolingApi.API_CAPOOLING_ORDER_COMM_INFO:
                setData(json);
                break;
            case CarpoolingApi.API_CAPOOLING_ORDER_COMMITE_EVALUATE_ACTION:
                Toast.makeText(this, "下单成功", Toast.LENGTH_SHORT).show();
                loadData();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        dismissProgress();
    }

    private void setData(String jsonString) {
        if (jsonString != null) {
            try {
                evaluateDetailEntity = JSONObject.parseObject(jsonString, EvaluateDetailEntity.class);
                if (evaluateDetailEntity != null && evaluateDetailEntity.comm_info != null) {
                    CarpoolingInfoEntity entity = evaluateDetailEntity.comm_info;
                    budget_tv.setText(entity.comm_price);
                    if ("102".equals(resType)) {
                        passenger_count.setText(entity.inventory);
                    } else {
                        passenger_count.setText(entity.pd_stock);
                    }
                    time.setText(entity.down_date);
                    start_point.setText(entity.pcqd);
                    destination.setText(entity.pczd);
                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(this, this, entity.order_list, resType,
                            type == TYPE_COMFIRM, type == TYPE_ROBBED, entity.user_name);
                    myListView.setAdapter(orderStatusAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(int p) {
        if (type == TYPE_COMFIRM) {
            commitData(p);
        } else {
            Intent intent = new Intent(this, CarpoolingEvaluateActivity.class);
            intent.putExtra(CarpoolingEvaluateActivity.KEY_OBJ, evaluateDetailEntity);
            intent.putExtra("resType", resType);
            try {
                intent.putExtra(CarpoolingEvaluateActivity.ORDER_ID, evaluateDetailEntity.comm_info.order_list.get(p).order_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }

    private MyProgressDialog mDialog;

    private void showProgress() {
        if (mDialog == null) {
            mDialog = new MyProgressDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void dismissProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void commitData(int p) {
        try {
            httpGetRequest(CarpoolingApi.comfirmOrderInfo(configEntity.key, comm_id, evaluateDetailEntity.comm_info.order_list.get(p).order_id),
                    CarpoolingApi.API_CAPOOLING_ORDER_COMMITE_EVALUATE_ACTION);
            showProgress();
        } catch (Exception e) {
            dismissProgress();
            e.printStackTrace();
        }
    }
}
