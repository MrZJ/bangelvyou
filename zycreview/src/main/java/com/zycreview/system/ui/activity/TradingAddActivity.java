package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.TradApi;
import com.zycreview.system.entity.EntpEntity;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 生成管理交易单界面
 */
public class TradingAddActivity extends BaseActivity implements View.OnClickListener,OnDateSetListener, OnWheelChangedListener, OnWheelClickedListener {

    private String trading;
    private TextView tradEntp, tradDate;
    private TextView trader;
    private TagFlowLayout stateTag;
    private String[] mVals = new String[]
            {"未完成", "已完成", "已废除"};
    private List<EntpEntity> entpInfos;
    private List<UserInfoEntity> userInfos;
    private String[] entpName;
    private String[] userName;
    // 选择地区相关
    private PopupWindow pop = null;
    private int popType = 0;
    private WheelView mWheelView;
    private TextView tv_confirm, tv_close, orderState;
    private View pop_rl_title, pop_ll_context;
    private int eCurrent;//交易企业
    private int uCurrent;//交易人
    private String entpId,traderId,stateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_add);
        trading = getIntent().getStringExtra("trading");
        entpInfos = (List<EntpEntity>) getIntent().getSerializableExtra("entps");
        userInfos = (List<UserInfoEntity>) getIntent().getSerializableExtra("userInfos");
        stateString = "1";
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("提交交易单");
        setLeftBackButton();
        setTopRightTitle("提交", TopClickUtil.TOP_UPDATE_TRADING);
        tradEntp = (TextView) findViewById(R.id.tv_add_trading_entpname);
        tradDate = (TextView) findViewById(R.id.tv_add_trading_traddate);
        trader = (TextView) findViewById(R.id.edt_add_trading_trader);
        stateTag = (TagFlowLayout) findViewById(R.id.flowlayout_trad_state);
        orderState = (TextView) findViewById(R.id.order_state);
        orderState.setText("已完成");
        stateTag.setVisibility(View.GONE);
//        stateTag.setAdapter(new TagAdapter<String>(mVals) {
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//                TextView tv = (TextView) LayoutInflater.from(TradingAddActivity.this).inflate(R.layout.item_check_tag,
//                        stateTag, false);
//                tv.setText(s);
//                return tv;
//            }
//
//            @Override
//            public boolean setSelected(int position, String s) {
//                return s.equals("未完成");
//            }
//        });
        entpName = new String[entpInfos.size()];
        for (int i = 0; i < entpInfos.size(); i++) {
            entpName[i] = entpInfos.get(i).entp_name;
        }
        userName = new String[userInfos.size()];
        for (int i = 0; i < userInfos.size(); i++) {
            userName[i] = userInfos.get(i).user_name;
        }
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.pop_address_item, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        mWheelView = (WheelView) view.findViewById(R.id.id_province);
        view.findViewById(R.id.id_city).setVisibility(View.GONE);
        view.findViewById(R.id.id_district).setVisibility(View.GONE);
        tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        tv_close = (TextView) view.findViewById(R.id.tv_close);
        pop_ll_context = view.findViewById(R.id.pop_ll_context);
        pop_rl_title = view.findViewById(R.id.pop_rl_title);
        pop_rl_title.setVisibility(View.GONE);
        LinearLayout.LayoutParams llParames = (LinearLayout.LayoutParams) pop_ll_context.getLayoutParams();
        llParames.rightMargin = 30;
        llParames.leftMargin = 30;
        pop_ll_context.setLayoutParams(llParames);
        initViewDateDialog(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        tradEntp.setOnClickListener(this);
        tradDate.setOnClickListener(this);
        trader.setOnClickListener(this);
        mWheelView.addChangingListener(this);
        mWheelView.addClickingListener(this);
        stateTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                switch (position) {
                    case 0:
                        if (!StringUtil.isEmpty(stateString) && stateString.equals("0")) {
                            stateString = null;
                        } else {
                            stateString = "0";
                        }
                        break;
                    case 1:
                        if (!StringUtil.isEmpty(stateString) && stateString.equals("1")) {
                            stateString = null;
                        } else {
                            stateString = "1";
                        }
                        break;
                    case 2:
                        if (!StringUtil.isEmpty(stateString) && stateString.equals("-1")) {
                            stateString = null;
                        } else {
                            stateString = "-1";
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add_trading_entpname:
                if(entpName != null){
                    popType = 0;
                    setEntpData();
                    pop.showAsDropDown(tradEntp);
                } else {
                    Toast.makeText(this,"暂无企业信息",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_add_trading_traddate:
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.edt_add_trading_trader:
                if(userName != null){
                    popType = 1;
                    setTraderData();
                    pop.showAsDropDown(trader);
                } else {
                    Toast.makeText(this,"暂无人员信息",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void upTradingInfo(){
        if(StringUtil.isEmpty(entpId) || StringUtil.isEmpty(traderId)){
            Toast.makeText(this,"提交的信息不全",Toast.LENGTH_SHORT).show();
        } else if(trader.getText().toString().equals("点击选择时间")){
            Toast.makeText(this,"提交的信息不全",Toast.LENGTH_SHORT).show();
        } else{
            httpPostRequest(TradApi.addTrading(), getRequestParams(), TradApi.API_ADD_TRADING);
        }
    }

    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key",configEntity.key);
        params.put("trade_price",trading);
        params.put("entp_id",entpId);
        params.put("trade_state",stateString);
        params.put("trade_date",tradDate.getText().toString());
        params.put("trade_uname", trader.getText().toString());
        params.put("trade_uname_id", traderId);
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case TradApi.API_ADD_TRADING:
                Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 设置基地数据
     */
    private void setEntpData() {
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, entpName);
        mWheelView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(eCurrent);
        updateEntp();
    }

    /**
     * 设置交易人数据
     */
    private void setTraderData() {
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, userName);
        mWheelView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(uCurrent);
        updateTrader();
    }

    /**
     * 根据当前的企业位置，更新企业WheelView的信息
     */
    private void updateEntp() {
        eCurrent = mWheelView.getCurrentItem();
    }

    private void updateTrader() {
        uCurrent = mWheelView.getCurrentItem();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tradDate.setText(sf.format(d));
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updateEntp();
    }

    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mWheelView.getCurrentItem() == itemIndex) {
            if(popType == 0){
                EntpEntity entity = entpInfos.get(itemIndex);
                tradEntp.setText(entity.entp_name);
                entpId = entity.entp_id;
            }else{
                UserInfoEntity traderInfo = userInfos.get(itemIndex);
                trader.setText(traderInfo.user_name);
                traderId = traderInfo.user_id;
            }
            pop.dismiss();
        }
    }

}
