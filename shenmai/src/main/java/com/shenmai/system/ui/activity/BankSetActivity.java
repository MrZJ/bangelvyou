package com.shenmai.system.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.BandCardEditText;
import com.shenmai.system.R;
import com.shenmai.system.adapter.WheelAdapter;
import com.shenmai.system.api.RegisterApi;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 提现设置
 */
public class BankSetActivity extends BaseActivity implements View.OnClickListener,WheelView.OnWheelItemClickListener{

    private EditText userName;
    private BandCardEditText editNum;
    private TextView bankName;
    private PopupWindow pop = null;
    private WheelView wheelView;
    private List<String> bankList;//银行名称
    private String userNameR, bankNameR, cardNumR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_set);
        userNameR = getIntent().getStringExtra("name");
        bankNameR = getIntent().getStringExtra("bankname");
        cardNumR = getIntent().getStringExtra("cardnum");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("提现设置");
        setTopRightTitle("保存", TopClickUtil.TOP_SET_BANK);
        userName = (EditText) findViewById(R.id.edt_bank_set_user);
        bankName = (TextView) findViewById(R.id.tv_bank_set_bankname);
        editNum = (BandCardEditText) findViewById(R.id.edt_bank_set_cardnum);
        if(!StringUtil.isEmpty(userNameR)){
            userName.setText(userNameR);
            bankName.setText(bankNameR);
            editNum.setText(cardNumR);
        }
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.pop_bank, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        wheelView = (WheelView) view.findViewById(R.id.bank_wheelview);
        wheelView.setWheelAdapter(new WheelAdapter(this));
        wheelView.setWheelSize(5);
        wheelView.setSkin(WheelView.Skin.None);
        wheelView.setWheelClickable(true);
        String[] location = getResources().getStringArray(R.array.bank);
        bankList = new ArrayList<>();
        for (int i = 0; i < location.length; i++) {
            bankList.add(location[i]);
        }
        wheelView.setWheelData(bankList);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#51BAD6");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        wheelView.setStyle(style);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        bankName.setOnClickListener(this);
        wheelView.setOnWheelItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bank_set_bankname:
                pop.showAsDropDown(bankName);
                break;
        }
    }

    @Override
    public void onItemClick(int position, Object o) {
        String  name = (String) o;
        bankName.setText(name);
        pop.dismiss();
    }

    public void setBankInfo(){
        if(checkInfoString()){
            httpPostRequest(RegisterApi.setBankInfoUrl(),getRequestParams(),RegisterApi.API_SET_BANK_INFO);
        }
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("bank_name", bankName.getText().toString());
        params.put("bank_account_name", userName.getText().toString().trim());
        params.put("bank_account", editNum.getText().toString().trim());
        return params;
    }


    private boolean checkInfoString() {
        if (StringUtil.isEmpty(userName.getText().toString().trim())) {
            Toast.makeText(this, "请输入开户人姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bankName.getText().toString().equals("选择开户银行")) {
            Toast.makeText(this, "选择开户银行", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(editNum.getText().toString())) {
            Toast.makeText(this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case RegisterApi.API_SET_BANK_INFO:
                Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
