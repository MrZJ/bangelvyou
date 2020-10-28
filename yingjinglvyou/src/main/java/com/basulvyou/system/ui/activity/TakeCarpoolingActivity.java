package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.wibget.CircleImageView;

import java.util.HashMap;

/**
 * 拼车提交订单界面
 */
public class TakeCarpoolingActivity extends BaseActivity implements View.OnClickListener{

    private View relTakeCarpoolingBill,takeCarpoolingBillline;
    private CircleImageView carOwnerImg;
    private TextView startDate,billNum;
    private EditText edtTel,edtContactName,edtNotice;
    private ImageView minuBill,addBill;
    private int num = 1;//默认数量
    private String id;//拼单id
    private int maxNum;//剩余可购买量
    private Button takeBill;
    private String date;
    private String commType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_carpooling);
        commType = getIntent().getStringExtra("commType");
        id = getIntent().getStringExtra("id");
        maxNum = Integer.parseInt(getIntent().getStringExtra("maxNum"));
        date = getIntent().getStringExtra("date");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("订单确认");
        carOwnerImg = (CircleImageView) findViewById(R.id.img_take_carpooling_icon);
        startDate = (TextView) findViewById(R.id.tv_take_carpooling_date);
        startDate.setText(date);
        edtTel = (EditText) findViewById(R.id.edt_take_carpooling_tel);
        edtContactName = (EditText) findViewById(R.id.edt_take_carpooling_contact);
        edtNotice = (EditText) findViewById(R.id.edt_take_carpooling_notice);
        minuBill = (ImageView) findViewById(R.id.minus_bill);
        billNum = (TextView) findViewById(R.id.bill_num);
        addBill = (ImageView) findViewById(R.id.add_bill);
        takeBill = (Button) findViewById(R.id.btn_take_carpooling_sure);
        takeCarpoolingBillline = findViewById(R.id.take_carpooling_billline);
        relTakeCarpoolingBill = findViewById(R.id.rel_take_carpooling_bill);
        if (commType.equals("104")) {
            takeCarpoolingBillline.setVisibility(View.GONE);
            relTakeCarpoolingBill.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        minuBill.setOnClickListener(this);
        addBill.setOnClickListener(this);
        takeBill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus_bill:
                if (num > 1) {
                    num -= 1;
                    billNum.setText(String.valueOf(num));
                }
                break;
            case R.id.add_bill:
                if (num < maxNum) {
                    num += 1;
                    billNum.setText(String.valueOf(num));
                }
                break;
            case R.id.btn_take_carpooling_sure:
                checkEdit();
                break;
        }
    }

    private void checkEdit() {
        if (StringUtil.isEmpty(edtTel.getText().toString().trim())) {
            Toast.makeText(this, "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(edtContactName.getText().toString().trim())) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        sendBillInfo();
    }

    private void sendBillInfo() {
        httpPostRequest(CarpoolingApi.joinCarpooling(), getRequestParams(), CarpoolingApi.API_JOIN_CAPOOLING);
    }

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key",configEntity.key);
        params.put("comm_id",id);
        params.put("num",String.valueOf(num));
        params.put("rel_phone",edtTel.getText().toString().trim());
        params.put("rel_name",edtContactName.getText().toString().trim());
        if (!StringUtil.isEmpty(edtNotice.getText().toString().trim())) {
            params.put("remark", edtNotice.getText().toString().trim());
        }
        return  params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CarpoolingApi.API_JOIN_CAPOOLING:
                Toast.makeText(this,"下单成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MyCarpoolingActivity.class);
                intent.putExtra("type",commType);
                startActivity(intent);
                appManager.finishOtherActivity();
                break;
        }
    }
}
