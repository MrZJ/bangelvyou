package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.StockApi;
import com.zycreview.system.entity.EntpStockEntity;
import com.zycreview.system.entity.InstockingInfo;
import com.zycreview.system.entity.MedicInstockEntity;
import com.zycreview.system.entity.PlantBaseEntity;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TimeFormatUtil;
import com.zycreview.system.utils.TopClickUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.util.HashMap;
import java.util.List;

/**
 * 入库详情
 */
public class InstockDetailActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener, OnWheelClickedListener, OnDateSetListener {

    private TextView user;
    private TextView baseTitle, depotTitle, dateTitle, stateTitle, harvestWeight, harvestWeightTitle;
    private TextView base, depot, name, type, grow, unit, date, state;
    private EditText weight;
    private String id, showType;
    // 选择地区相关
    private PopupWindow pop = null;
    private WheelView mWheelView;
    private TextView tv_confirm, tv_close;
    private View pop_rl_title, pop_ll_context;
    private String[] baseName, storeName, userName;
    private List<PlantBaseEntity> baseList;
    private List<EntpStockEntity> storeList;
    private List<UserInfoEntity> userList;
    private int bCurrent;
    private int sCurrent;
    private int uCurrent;
    private String mCurrentBaseName, mCurrentStockName, mCurrentUserName;
    private int popType = 0;
    private String baseID, stockID, plantID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instock_detail);
        id = getIntent().getStringExtra("id");
        showType = getIntent().getStringExtra("type");
        initViewDateDialog(this);
        initView();
        initListener();
        getData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
//        baseTitle = (TextView) findViewById(R.id.tv_instock_detail_base_title);
        depotTitle = (TextView) findViewById(R.id.tv_instock_detail_depot_title);
        dateTitle = (TextView) findViewById(R.id.tv_instock_detail_date_title);
        stateTitle = (TextView) findViewById(R.id.tv_instock_detail_state_title);

//        base = (TextView) findViewById(R.id.tv_instock_detail_base);//基地
        depot = (TextView) findViewById(R.id.tv_instock_detail_depot);//仓库
        user = (TextView) findViewById(R.id.edt_instock_detail_user);//入库人
        name = (TextView) findViewById(R.id.tv_instock_detail_name);//药品名
        type = (TextView) findViewById(R.id.tv_instock_detail_type);//药品类型
        grow = (TextView) findViewById(R.id.tv_instock_detail_grow);//药品生长方式
        harvestWeightTitle = (TextView) findViewById(R.id.tv_instock_detail_weight_title);//采收重量
        harvestWeight = (TextView) findViewById(R.id.tv_instock_detail_weight);//采收重量
        weight = (EditText) findViewById(R.id.edt_instock_detail_weight);//入库重量
        unit = (TextView) findViewById(R.id.tv_instock_detail_unit);//重量单位
        date = (TextView) findViewById(R.id.tv_instock_detail_date);//入库时间
        state = (TextView) findViewById(R.id.tv_instock_detail_state);//状态
        if (showType.equals("instock")) {
            setTitle("入库添加/编辑");
            setTopRightTitle("保存", TopClickUtil.TOP_INSTOCK);
//            dateTitle.setVisibility(View.GONE);
//            date.setVisibility(View.GONE);
            date.setText(TimeFormatUtil.longToString(System.currentTimeMillis(), "yyyy-MM-dd"));
            stateTitle.setVisibility(View.GONE);
            state.setVisibility(View.GONE);
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
        } else {
            setTitle("入库详情");
//            baseTitle.setText("基地名称:");
            depotTitle.setText("仓库名称:");
            harvestWeightTitle.setVisibility(View.GONE);
            harvestWeight.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        if (showType.equals("instock")) {
//            base.setOnClickListener(this);
            depot.setOnClickListener(this);
            user.setOnClickListener(this);
            date.setOnClickListener(this);
            // 添加change事件
            mWheelView.addChangingListener(this);
            mWheelView.addClickingListener(this);
        } else {
            weight.setFocusable(false);
            weight.setFocusableInTouchMode(false);
        }
    }

    /**
     * 设置基地数据
     */
    private void setBaseData() {
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, baseName);
        mWheelView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(bCurrent);
        updateBase();
    }

    /**
     * 设置仓库数据
     */
    private void setStockData() {
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, storeName);
        mWheelView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(sCurrent);
        updateStock();
    }

    /**
     * 设置入库人员数据
     */
    private void setUserData() {
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, userName);
        mWheelView.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(uCurrent);
        updateUser();
    }

    /**
     * 根据当前的基地，更新市WheelView的信息
     */
    private void updateBase() {
        bCurrent = mWheelView.getCurrentItem();
        mCurrentBaseName = baseName[bCurrent];
    }

    private void updateStock() {
        sCurrent = mWheelView.getCurrentItem();
        mCurrentStockName = storeName[sCurrent];
    }

    private void updateUser() {
        uCurrent = mWheelView.getCurrentItem();
        mCurrentUserName = userName[uCurrent];
    }

    private void getData() {
        if (showType.equals("instock")) {
            httpGetRequest(StockApi.getInstockingInfo(configEntity.key, id), StockApi.API_INSTOCK_MANAGE_INFO);
        } else {
            httpGetRequest(StockApi.getRecodeInfo(configEntity.key, id), StockApi.API_INSTOCK_RECORD_INFO);
        }
    }

    public void updateInstock() {
        if (StringUtil.isEmpty(stockID) || StringUtil.isEmpty(userID)) {
            Toast.makeText(this, "仓库、入库人信息不全", Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(weight.getText().toString())) {
            Toast.makeText(this, "入库重量不能为空", Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.dataAboveZero(weight.getText().toString())) {
            Toast.makeText(this, "入库重量不符合规则", Toast.LENGTH_SHORT).show();
            weight.setText("");
        } else {
            httpPostRequest(StockApi.instockingUpdate(), getRequestParams(), StockApi.API_INSTOCK_MANAGE_UPDATE);
        }
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
//        params.put("base_id",baseID);
        params.put("store_id", stockID);
        params.put("plantJobReceive_id", plantID);
        params.put("input_name", user.getText().toString());
        params.put("drugs_number", weight.getText().toString());
        params.put("input_date", date.getText().toString());
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case StockApi.API_INSTOCK_MANAGE_INFO:
                infoHander(json);
                break;
            case StockApi.API_INSTOCK_RECORD_INFO:
                recordInfoHander(json);
                break;
            case StockApi.API_INSTOCK_MANAGE_UPDATE:
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void infoHander(String json) {
        InstockingInfo info = JSON.parseObject(json, InstockingInfo.class);
        if (info != null) {
            final MedicInstockEntity entity = info.entity;
            if (entity != null) {
                name.setText(entity.drugs_name);
                type.setText(entity.ingred_type);
                grow.setText(entity.grow_way);
                harvestWeight.setText(entity.receive_weight);
                final String outWeight = entity.receive_weight;
                weight.setText(entity.receive_weight);
                unit.setText(entity.receive_w_unit);
                plantID = entity.plantJobReceive_id;
                weight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!StringUtil.isEmpty(editable.toString())) {
                            if (editable.toString().startsWith(".")) {
                                weight.setText("");
                            } else {
                                float a = Float.parseFloat(editable.toString());
                                float b = Float.parseFloat(outWeight);
                                if (a > b) {
                                    weight.setText("");
                                }
                            }
                        }
                    }
                });
            }
            if (!ListUtils.isEmpty(info.plantBaseList)) {
                baseList = info.plantBaseList;
                baseName = new String[baseList.size()];
                for (int i = 0; i < baseList.size(); i++) {
                    baseName[i] = baseList.get(i).base_name;
                }
            }
            if (!ListUtils.isEmpty(info.entpStoreList)) {
                storeList = info.entpStoreList;
                storeName = new String[storeList.size()];
                for (int i = 0; i < storeList.size(); i++) {
                    storeName[i] = storeList.get(i).store_name;
                }
            }
            if (!ListUtils.isEmpty(info.user_list)) {
                userList = info.user_list;
                userName = new String[userList.size()];
                for (int i = 0; i < userList.size(); i++) {
                    userName[i] = userList.get(i).user_name;
                }
            }
        }
    }

    private void recordInfoHander(String json) {
        MedicInstockEntity info = JSON.parseObject(json, MedicInstockEntity.class);
        if (info != null) {
//            base.setText(info.base_name);
            depot.setText(info.store_name);
            user.setText(info.input_name);
            type.setText(info.drugs_type);
            name.setText(info.ingred_name);
            grow.setText(info.grow_pattern);
            weight.setText(info.drugs_number);
            unit.setText(info.drugs_unit);
            date.setText(info.input_date);
            String stateString;
            if (info.test_flag.equals("1")) {
                stateString = "已检测";
            } else if (info.test_flag.equals("2")) {
                stateString = "未检测";
            } else {
                stateString = "未检测";
            }
            state.setText(stateString);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_instock_detail_base://选择基地
                /*if(baseName != null){
                    popType = 0;
                    setBaseData();
                    pop.showAsDropDown(base);
                } else{
                    Toast.makeText(this,"暂无基地数据",Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.tv_instock_detail_depot://选择仓库
                if (storeName != null) {
                    popType = 1;
                    setStockData();
                    pop.showAsDropDown(depot);
                } else {
                    Toast.makeText(this, "暂无仓库数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edt_instock_detail_user:
                if (userName != null) {
                    popType = 2;
                    setUserData();
                    pop.showAsDropDown(user);
                } else {
                    Toast.makeText(this, "暂无人员数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_instock_detail_date:
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (popType == 1) {
            updateStock();
        } else if (popType == 0) {
            updateBase();
        } else {
            updateUser();
        }
    }

    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mWheelView.getCurrentItem() == itemIndex) {
            pop.dismiss();
            if (popType == 1) {
                depot.setText(mCurrentStockName);
                stockID = storeList.get(itemIndex).store_id;
            } else if (popType == 0) {
//                base.setText(mCurrentBaseName);
//                baseID = baseList.get(itemIndex).base_id;
            } else {
                user.setText(mCurrentUserName);
                userID = userList.get(itemIndex).user_id;
            }
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        date.setText(TimeFormatUtil.longToString(millseconds, "yyyy-MM-dd"));
    }
}
