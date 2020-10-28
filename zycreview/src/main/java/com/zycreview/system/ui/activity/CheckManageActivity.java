package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.zycreview.system.api.CheckManageApi;
import com.zycreview.system.entity.CheckManageEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.TestPerson;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 检验管理检查界面
 */
public class CheckManageActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener, OnWheelChangedListener, OnWheelClickedListener {

    private TextView medicinal_name;
    private TextView recovery_weight;
    private TextView recovery_time;
    private TextView recovery_person;
    private EditText et_check_name;
    private TextView et_executive_standard;
    private TextView et_check_stockpile;
    private TextView et_check_time;
    private TextView et_check_person;
    private LinearLayout checkTestLin;
    private Button bt_commit;
    private String id;

    // 选择药材和基地相关
    private View pop_rl_title, pop_ll_context;
    private PopupWindow pop = null;
    private WheelView mViewProvince;
    private int popType = 0;
    private int pCurrent = -1;//当前贮藏条件
    private int iCurrent = -1;//当前执行标准
    private int tCurrent = -1;//当前检测人员
    private List<CheckManageEntity.RmNorm> rm_norm;//执行标准
    private List<CheckManageEntity.HoldzCond> holdz_cond;//贮藏条件
    private List<TestPerson> testPersons;
    private String[] roNormString;//执行标准
    private String[] holdzCodeString;//贮藏条件
    private String[] testPersonList;//检验人员
    private List<CheckManageEntity.TestMethods> testMethod;//检验项目
    private HashMap<String, String> testResultMap;//保存检验结果
    private String[] checkID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_manage);
        id = getIntent().getStringExtra("id");
        initView();
        initListener();
        setData();
    }

    private void initView() {
        initTopView();
        setTitle("药材检验添加/编辑");
        initViewDateDialog(this);

        medicinal_name = (TextView) findViewById(R.id.medicinal_name);
        recovery_weight = (TextView) findViewById(R.id.recovery_weight);
        recovery_time = (TextView) findViewById(R.id.recovery_time);
        recovery_person = (TextView) findViewById(R.id.recovery_person);
        et_check_name = (EditText) findViewById(R.id.et_check_name);
        et_executive_standard = (TextView) findViewById(R.id.et_executive_standard);
        et_check_stockpile = (TextView) findViewById(R.id.et_check_stockpile);
        et_check_time = (TextView) findViewById(R.id.et_check_time);
        et_check_person = (TextView) findViewById(R.id.et_check_person);
        et_check_person.setOnClickListener(this);
        checkTestLin = (LinearLayout) findViewById(R.id.lin_check_manage);
        bt_commit = (Button) findViewById(R.id.bt_commit);

        //选择地区相关
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.pop_address_item, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        view.findViewById(R.id.id_city).setVisibility(View.GONE);
        view.findViewById(R.id.id_district).setVisibility(View.GONE);
        pop_ll_context = view.findViewById(R.id.pop_ll_context);
        pop_rl_title = view.findViewById(R.id.pop_rl_title);
        pop_rl_title.setVisibility(View.GONE);
        LinearLayout.LayoutParams llParames = (LinearLayout.LayoutParams) pop_ll_context.getLayoutParams();
        llParames.rightMargin = 30;
        llParames.leftMargin = 30;
        pop_ll_context.setLayoutParams(llParames);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener(TopClickUtil.TOP_CHECK_BACK);
        et_executive_standard.setOnClickListener(this);
        et_check_stockpile.setOnClickListener(this);
        et_check_time.setOnClickListener(this);
        //选择地区相关
        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewProvince.addClickingListener(this);
        bt_commit.setOnClickListener(this);
    }

    private void setData() {
        httpGetRequest(CheckManageApi.getCheckManageCheckUrl(id, configEntity.key), CheckManageApi.API_CHECKMANAGE_CHECK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_executive_standard://执行标准
                if (roNormString != null) {
                    popType = 0;
                    setRoNormData();
                    pop.showAsDropDown(et_executive_standard);
                } else {
                    Toast.makeText(this, "没有可选执行标准", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_check_stockpile://贮藏条件
                if (holdz_cond != null) {
                    popType = 1;
                    setHoldzCodeData();
                    pop.showAsDropDown(et_check_stockpile);
                } else {
                    Toast.makeText(this, "没有可选贮藏条件", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_check_time://检验时间
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.bt_commit:
                commitData();
                break;
            case R.id.et_check_person:
                if (testPersonList != null) {
                    popType = 2;
                    setTestPersonData();
                    pop.showAsDropDown(et_check_person);
                } else {
                    Toast.makeText(this, "没有可选贮藏条件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (popType == 0) {
            updateRoNorm();
        } else if (popType == 1) {
            updateHoldzCodeDatas();
        } else if (popType == 2) {
            updateTestDatas();
        }
    }


    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mViewProvince.getCurrentItem() == itemIndex) {
            pop.dismiss();
            if (popType == 0) {
                et_executive_standard.setText(roNormString[iCurrent]);
            } else if (popType == 1) {
                et_check_stockpile.setText(holdzCodeString[pCurrent]);
            } else if (popType == 2) {
                et_check_person.setText(testPersonList[tCurrent]);
            }
        }
    }


    private void setRoNormData() {
        //选择类型相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, roNormString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(iCurrent);
        updateRoNorm();
    }

    /**
     * 设置地域数据
     */
    private void setHoldzCodeData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, holdzCodeString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(pCurrent);
        updateHoldzCodeDatas();
    }

    /**
     * 设置检测人员数据
     */
    private void setTestPersonData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, testPersonList);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(tCurrent);
        updateTestDatas();
    }

    private void updateHoldzCodeDatas() {

        pCurrent = mViewProvince.getCurrentItem();
    }

    private void updateTestDatas() {
        tCurrent = mViewProvince.getCurrentItem();
    }

    private void updateRoNorm() {
        iCurrent = mViewProvince.getCurrentItem();
    }

    /**
     * 提交按钮触发事件
     */
    private void commitData() {
        if (checkData()) {
            httpPostRequest(CheckManageApi.getCheckManageSaveUrl(),
                    getRequestParams(), CheckManageApi.API_CHECKMANAGE_SAVE);
        }
    }

    /**
     * 添加任务参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        try {
            StringBuffer itemId = new StringBuffer();
            StringBuffer result = new StringBuffer();
            HashMap<String, String> params = new HashMap<>();
            if (testResultMap != null){
                Iterator iter = testResultMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    itemId.append(entry.getKey() + ",");
                    result.append(entry.getValue() + ",");
                }
            }
            if (!StringUtil.isEmpty(itemId.toString())) {
                params.put("item_id", itemId.toString());
                params.put("result", result.toString());
            } else {
                params.put("item_id", "");
                params.put("result", "");
            }
            params.put("mobileLogin", "true");
            params.put("mod_id", "5002000100");
            params.put("key", configEntity.key);
            params.put("id", id);
            params.put("test_name", et_check_name.getText().toString());
            params.put("rm_norm", rm_norm.get(iCurrent).value);
            params.put("holdz_cond", holdz_cond.get(pCurrent).value);
            params.put("test_date", et_check_time.getText().toString());
            params.put("test_man", testPersons.get(tCurrent).user_name);
            params.put("test_man_id", testPersons.get(tCurrent).user_id);
            return params;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 必填项是否有空
     *
     * @return
     */
    private boolean checkData() {
        if (StringUtil.isEmpty(et_check_name.getText().toString())) {
            Toast.makeText(this, "检验名称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (iCurrent == -1) {
            Toast.makeText(this, "执行标准不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pCurrent == -1) {
            Toast.makeText(this, "贮藏条件不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tCurrent == -1) {
            Toast.makeText(this, "检测人员不能为空", Toast.LENGTH_SHORT).show();
        }
        if (et_check_time.getText().equals("检验时间")) {
            Toast.makeText(this, "检验时间不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(et_check_person.getText().toString())) {
            Toast.makeText(this, "检验人员不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (testResultMap != null && testResultMap.size() > 0) {
//            for (String str : checkID) {
//                if (testResultMap.get(str) == null) {
//                }
//            }
//        } else {
//            Toast.makeText(this, "检验结果不能为空", Toast.LENGTH_SHORT).show();
//        }
        return true;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CheckManageApi.API_CHECKMANAGE_SAVE:
                Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case CheckManageApi.API_CHECKMANAGE_CHECK:
                CheckManageEntity entity = JSON.parseObject(json, CheckManageEntity.class);
                if (entity != null) {
                    rm_norm = entity.rm_norm;
                    holdz_cond = entity.holdz_cond;
                    testPersons = entity.user_list;
                    final List<CheckManageEntity.TestMethods> testMethods = entity.testItemList;
                    if (!StringUtil.isEmpty(entity.entity.test_name)) {
                        et_check_name.setText(entity.entity.test_name);
                    }
                    if (!ListUtils.isEmpty(rm_norm)) {
                        et_executive_standard.setText(rm_norm.get(0).name);
                        pCurrent = 0;
                        roNormString = new String[rm_norm.size()];
                        for (int i = 0; i < rm_norm.size(); i++) {
                            roNormString[i] = rm_norm.get(i).name;
                        }
                    }
                    if (!ListUtils.isEmpty(holdz_cond)) {
                        et_check_stockpile.setText(holdz_cond.get(0).name);
                        iCurrent = 0;
                        holdzCodeString = new String[holdz_cond.size()];
                        for (int i = 0; i < holdz_cond.size(); i++) {
                            holdzCodeString[i] = holdz_cond.get(i).name;
                        }
                    }
                    if (!ListUtils.isEmpty(testPersons)) {
                        et_check_person.setText(testPersons.get(0).user_name);
                        tCurrent = 0;
                        testPersonList = new String[testPersons.size()];
                        for (int i = 0; i < testPersons.size(); i++) {
                            testPersonList[i] = testPersons.get(i).user_name;
                        }
                    }
                    //处理检验项目数据
                    if (!ListUtils.isEmpty(testMethods)) {
                        testResultMap = new HashMap<>();
                        checkID = new String[testMethods.size()];
                        for (int i = 0; i < testMethods.size(); i++) {
                            final CheckManageEntity.TestMethods testMethod = testMethods.get(i);
                            checkID[i] = testMethod.item_id;
                            testResultMap.put(testMethod.item_id, null);
                            View testView = View.inflate(this, R.layout.item_test_info, null);
                            TextView testName = (TextView) testView.findViewById(R.id.item_tv_test_name);
                            TextView testDes = (TextView) testView.findViewById(R.id.item_tv_test_des);
                            final EditText testResult = (EditText) testView.findViewById(R.id.item_edt_test_result);
                            testName.setText(testMethod.item_name);
                            testDes.setText(testMethod.standrad);
                            testResult.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    String result = testResult.getText().toString();
                                    if (!StringUtil.isEmpty(result)) {
                                        testResultMap.put(testMethod.item_id, result);
                                    } else {
                                        if (null != testResultMap.get(testMethod.item_id)) {
                                            testResultMap.remove(testMethod.item_id);
                                        }
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            checkTestLin.addView(testView);
                        }
                    }
                    if (entity.entity == null) {
                        return;
                    }
                    if (!StringUtil.isEmpty(entity.entity.drugs_name)) {
                        medicinal_name.setText("药材名称:" + entity.entity.drugs_name);
                    }
                    if (!StringUtil.isEmpty(entity.entity.receive_weight)) {
                        recovery_weight.setText("实际采收量:" + entity.entity.receive_weight);
                    }
                    if (!StringUtil.isEmpty(entity.entity.receive_date)) {
                        recovery_time.setText("采收时间:" + entity.entity.receive_date);
                    }
                    if (!StringUtil.isEmpty(entity.entity.receive_man)) {
                        recovery_person.setText("采收负责人:" + entity.entity.receive_man);
                    }
                }
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        et_check_time.setText(sf.format(d));
    }
}
