package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.zycreview.system.entity.TestPerson;
import com.zycreview.system.entity.TradedEntity;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.zycreview.system.ui.activity.TradedManageActivity.TEST_PERSION;

/**
 * 交易详情或修改交易状态
 */
public class TradedDetailOrChangeStateActivity extends BaseActivity implements OnDateSetListener, View.OnClickListener, OnWheelChangedListener, OnWheelClickedListener {

    private TextView tradCode, entrCode, tradPrice, tardState, tardDate, orderState;
    private EditText trader;
    private TagFlowLayout stateTag;
    private String[] mVals = new String[]
            {"未完成", "已完成", "已废除"};
    private String type, stateString;
    private TradedEntity entity;
    private String changingState;
    private List<TestPerson> testPersonList;
    private String[] testPersionNames;
    private WheelView mSelPersionView;
    private PopupWindow pop = null;
    private int mCurSelPersion = 0;
    private String trade_uname_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traded_detail_or_change_state);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            entity = (TradedEntity) intent.getSerializableExtra("entity");
            if (entity == null) {
                return;
            }
            trade_uname_id = entity.trade_uname_id;
            testPersonList = (List<TestPerson>) intent.getSerializableExtra(TEST_PERSION);
            if (testPersonList != null && testPersonList.size() > 0) {
                testPersionNames = new String[testPersonList.size()];
                for (int i = 0; i < testPersonList.size(); i++) {
                    testPersionNames[i] = testPersonList.get(i).user_name;
                }
            }
        }
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        tradCode = (TextView) findViewById(R.id.tv_traded_detail_or_state_tradcode);
        entrCode = (TextView) findViewById(R.id.tv_traded_detail_or_state_entrcode);
        tradPrice = (TextView) findViewById(R.id.tv_traded_detail_or_state_price);
        trader = (EditText) findViewById(R.id.edt_traded_detail_or_state_trader);
        trader.setOnClickListener(this);
        tardState = (TextView) findViewById(R.id.tv_traded_detail_or_state_tradstate);
        tardDate = (TextView) findViewById(R.id.tv_traded_detail_or_state_date);
        stateTag = (TagFlowLayout) findViewById(R.id.flowlayout_state);
        orderState = (TextView) findViewById(R.id.order_state);
        if (entity.trade_state.equals("1")) {
            stateString = "已完成";
        } else if (entity.trade_state.equals("0")) {
            stateString = "未完成";
        } else {
            stateString = "已废除";
        }
        orderState.setText(stateString);
        changingState = entity.trade_state;
        tradCode.setText(entity.trade_no);
        entrCode.setText(entity.bsi_code_in);
        trader.setText(entity.trade_uname);
        tradPrice.setText(entity.trade_money);
        tardDate.setText(entity.trade_date);
        if (type.equals("detail")) {
            setTitle("交易单详情");
            tardState.setVisibility(View.VISIBLE);
            stateTag.setVisibility(View.GONE);
            orderState.setVisibility(View.GONE);
            tardState.setText(stateString);
        } else {
            setTitle("修改交易单");
            setTopRightTitle("保存", TopClickUtil.TOP_UPDATE_STATE);
            tardState.setVisibility(View.GONE);
            orderState.setVisibility(View.VISIBLE);
            stateTag.setVisibility(View.GONE);
//            stateTag.setAdapter(new TagAdapter<String>(mVals) {
//                @Override
//                public View getView(FlowLayout parent, int position, String s) {
//                    TextView tv = (TextView) LayoutInflater.from(TradedDetailOrChangeStateActivity.this).inflate(R.layout.item_check_tag,
//                            stateTag, false);
//                    tv.setText(s);
//                    return tv;
//                }
//
//                @Override
//                public boolean setSelected(int position, String s) {
//                    return s.equals(stateString);
//                }
//            });
        }
        initViewDateDialog(this);
        initSelPersionWindow();
    }

    private void initSelPersionWindow() {
        //选择地区相关
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.pop_address_item, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        mSelPersionView = (WheelView) view.findViewById(R.id.id_province);
        view.findViewById(R.id.id_city).setVisibility(View.GONE);
        view.findViewById(R.id.id_district).setVisibility(View.GONE);
        View pop_ll_context = view.findViewById(R.id.pop_ll_context);
        View pop_rl_title = view.findViewById(R.id.pop_rl_title);
        pop_rl_title.setVisibility(View.GONE);
        LinearLayout.LayoutParams llParames = (LinearLayout.LayoutParams) pop_ll_context.getLayoutParams();
        llParames.rightMargin = 30;
        llParames.leftMargin = 30;
        pop_ll_context.setLayoutParams(llParames);
        // 添加change事件
        mSelPersionView.addChangingListener(this);
        mSelPersionView.addClickingListener(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        trader.setFocusable(false);
        trader.setFocusableInTouchMode(false);
        if (type.equals("detail")) {
            tardDate.setClickable(false);
        } else {
            tardDate.setOnClickListener(this);
            stateTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    switch (position) {
                        case 0:
                            if (!StringUtil.isEmpty(changingState) && changingState.equals("0")) {
                                changingState = null;
                            } else {
                                changingState = "0";
                            }
                            break;
                        case 1:
                            if (!StringUtil.isEmpty(changingState) && changingState.equals("1")) {
                                changingState = null;
                            } else {
                                changingState = "1";
                            }
                            break;
                        case 2:
                            if (!StringUtil.isEmpty(changingState) && changingState.equals("-1")) {
                                changingState = null;
                            } else {
                                changingState = "-1";
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tardDate.setText(sf.format(d));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_traded_detail_or_state_date:
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.edt_traded_detail_or_state_trader:
                if (testPersonList != null) {
                    setSelPersionData();
                    pop.showAsDropDown(trader);
                } else {
                    Toast.makeText(this, "没有可选贮藏条件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setSelPersionData() {
        if (testPersionNames == null) {
            return;
        }
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, testPersionNames);
        mSelPersionView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mSelPersionView.setVisibleItems(3);
        mSelPersionView.setCurrentItem(mCurSelPersion);
        upCurSelectPersion();
    }

    private void upCurSelectPersion() {
        mCurSelPersion = mSelPersionView.getCurrentItem();
        trade_uname_id = testPersonList.get(mCurSelPersion).user_id;
    }

    public void updateTraded() {
        if (StringUtil.isEmpty(trader.getText().toString().trim())) {
            Toast.makeText(this, "请输入交易人", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(changingState)) {
            Toast.makeText(this, "请选择交易状态", Toast.LENGTH_SHORT).show();
        } else {
            httpPostRequest(TradApi.updateTradedState(), getRequestParams(), TradApi.API_UPDATE_TRADED);
        }
    }

    /**
     * 整理参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("trade_id", entity.trade_id);
        params.put("key", configEntity.key);
        params.put("trade_uname", trader.getText().toString().trim());
        params.put("trade_uname_id", trade_uname_id);
        params.put("trade_state", changingState);
        params.put("trade_date", tardDate.getText().toString());
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case TradApi.API_UPDATE_TRADED:
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        upCurSelectPersion();
    }

    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mSelPersionView.getCurrentItem() == itemIndex) {
            pop.dismiss();
            trader.setText(testPersionNames[itemIndex]);
        }
    }
}
