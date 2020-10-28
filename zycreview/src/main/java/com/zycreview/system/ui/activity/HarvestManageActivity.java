package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.zycreview.system.api.HarvestManageApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.HarvestManageEntity;
import com.zycreview.system.entity.PlantTaskEntity;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.CacheObjUtils;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.wibget.UpdateDialog;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收获管理界面
 */
public class HarvestManageActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener, OnWheelChangedListener, OnWheelClickedListener, UpdateDialog.DialogClickInterface {

    private View tv_plant_task_name, tv_plant_name, tv_plant_base_address,
            tv_plant_address_code, tv_plant_time;
    private TextView tv_plant_task_name_name, tv_plant_name_name, tv_plant_base_address_name,
            tv_plant_address_code_name, tv_plant_time_name,tv_harvest_person;
    private TextView tv_plant_task_name_attribute, tv_plant_name_attribute, tv_plant_base_address_attribute,
            tv_plant_address_code_attribute, tv_plant_time_attribute;
    private TextView tv_harvest_date;
    private Button bt_commit;
    private PlantTaskEntity plantTaskEntity;
    private LinearLayout ll_medicine;
    private ArrayList<EditText> et_reality_weights = new ArrayList();
    private StringBuffer codeNums;//标识药材
    private StringBuilder planNo;
    private StringBuffer weightNums;//药材实际计量

    private View pop_rl_title, pop_ll_context;
    private PopupWindow pop = null;
    private WheelView mViewProvince;
    private List<UserInfoEntity> userInfoEntities;//作业人
    private String [] userString;//作业人
    private int personCurrent;//当前点击的作业人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_manage);
        plantTaskEntity = (PlantTaskEntity) getIntent().getSerializableExtra("plantTaskEntity");
        initView();
        initViewDateDialog(this);
        initListener();
        setData();
        getPersonString();
    }

    private void getPersonString() {
        try {
            userInfoEntities = (List<UserInfoEntity>) CacheObjUtils.getObj(this, "person");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ListUtils.isEmpty(userInfoEntities)) {
            userString = new String[userInfoEntities.size()];
            for (int i = 0; i < userInfoEntities.size(); i++) {
                userString[i] = userInfoEntities.get(i).real_name;
            }
        }
    }

    private void initView() {
        initTopView();
        setTitle("收获管理");
        tv_plant_task_name = findViewById(R.id.tv_plant_task_name);
        tv_plant_task_name_name = (TextView) tv_plant_task_name.findViewById(R.id.item_tv_plant_name);
        //种植任务名称
        tv_plant_task_name_attribute = (TextView) tv_plant_task_name.findViewById(R.id.item_tv_plant_attribute);
        tv_plant_name = findViewById(R.id.tv_plant_name);
        tv_plant_name_name = (TextView) tv_plant_name.findViewById(R.id.item_tv_plant_name);
        //种植物名称
        tv_plant_name_attribute = (TextView) tv_plant_name.findViewById(R.id.item_tv_plant_attribute);
        tv_plant_base_address = findViewById(R.id.tv_plant_base_address);
        tv_plant_base_address_name = (TextView) tv_plant_base_address.findViewById(R.id.item_tv_plant_name);
        //种植基地
        tv_plant_base_address_attribute = (TextView) tv_plant_base_address.findViewById(R.id.item_tv_plant_attribute);
        tv_plant_address_code = findViewById(R.id.tv_plant_address_code);
        tv_plant_address_code_name = (TextView) tv_plant_address_code.findViewById(R.id.item_tv_plant_name);
        //产地编码
        tv_plant_address_code_attribute = (TextView) tv_plant_address_code.findViewById(R.id.item_tv_plant_attribute);
        tv_plant_time = findViewById(R.id.tv_plant_time);
        tv_plant_time_name = (TextView) tv_plant_time.findViewById(R.id.item_tv_plant_name);
        //种植时间
        tv_plant_time_attribute = (TextView) tv_plant_time.findViewById(R.id.item_tv_plant_attribute);
        tv_harvest_person = (TextView) findViewById(R.id.tv_harvest_person);//采收人
        tv_harvest_date = (TextView) findViewById(R.id.tv_harvest_date);//采收时间
        bt_commit = (Button) findViewById(R.id.bt_commit);//提交
        ll_medicine = (LinearLayout) findViewById(R.id.ll_medicine);//添加药材

        //选择作业人相关
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
        initSideBarListener();
        bt_commit.setOnClickListener(this);
        tv_harvest_date.setOnClickListener(this);
        tv_harvest_person.setOnClickListener(this);

        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewProvince.addClickingListener(this);
    }

    private void setData() {
        tv_plant_task_name_name.setText("种植任务名称:");
        tv_plant_name_name.setText("种植物名称:");
        tv_plant_base_address_name.setText("种植基地:");
        tv_plant_address_code_name.setText("产地编码:");
        tv_plant_address_code_name.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_plant_time_name.setText("种植时间:");

        httpGetRequest(HarvestManageApi.getHarvestManageUrl(plantTaskEntity.plant_id, configEntity.key), HarvestManageApi.API_HARVESTMANAGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:
                commitData();
                break;
            case R.id.tv_harvest_date:
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.tv_harvest_person:
                if (userString != null) {
                    setPersonData();
                    pop.showAsDropDown(tv_harvest_person);
                } else {
                    Toast.makeText(this,"没有可选作业人",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 设置负责人数据
     */
    private void setPersonData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, userString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(personCurrent);
        updatePersonDatas();
    }

    private void updatePersonDatas() {
        personCurrent = mViewProvince.getCurrentItem();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updatePersonDatas();
    }


    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mViewProvince.getCurrentItem() == itemIndex) {
            pop.dismiss();
            tv_harvest_person.setText(userString[personCurrent]);
        }
    }

    /**
     * 提交按钮触发事件
     */
    private void commitData() {
        if (checkData()) {
            bt_commit.setClickable(false);
            UpdateDialog dialog = new UpdateDialog(this);
            dialog.setOkText("是");
            dialog.setCancelText("否");
            dialog.setTitleText("提示");
            dialog.setMessage("是否是最后一次收获？");
            dialog.setDialogClick(this);
            dialog.show();
        }
    }

    private boolean checkData() {
        if (tv_harvest_person.getText().toString().equals("采收人")) {
            Toast.makeText(this, "采收人不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tv_harvest_date.getText().toString().equals("采收时间")) {
            Toast.makeText(this, "采收时间不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ListUtils.isEmpty(et_reality_weights)){
            for (EditText e : et_reality_weights) {
                if (StringUtil.isEmpty(e.getText().toString())) {
                    Toast.makeText(this, "实际产量不能为空!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        weightNums = new StringBuffer();
        for(int i=0;i<et_reality_weights.size();i++) {
            if (i == 0) {
                weightNums.append(et_reality_weights.get(i).getText().toString());
            } else {
                weightNums.append(",").append(et_reality_weights.get(i).getText().toString());
            }
        }
        return true;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HarvestManageApi.API_HARVESTMANAGE:
                harverstHander(json);
                break;
            case HarvestManageApi.API_HARVESTMANAGE_SAVE:
                Toast.makeText(this, "提交成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void harverstHander(String json) {
        if (!StringUtil.isEmpty(json)) {
            HarvestManageEntity entity = JSON.parseObject(json, HarvestManageEntity.class);
            if (entity != null) {
                setTextData(entity.name, tv_plant_task_name_attribute);
                setTextData(entity.plant_name, tv_plant_name_attribute);
                setTextData(entity.base_code_in, tv_plant_base_address_attribute);
                setTextData(entity.place_code, tv_plant_address_code_attribute);
                setTextData(entity.plant_date, tv_plant_time_attribute);
                if (!ListUtils.isEmpty(entity.list)) {
                    List<HarvestManageEntity.IngredInfos> list = entity.list;
                    codeNums = new StringBuffer();
                    planNo = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_harvest_ins, null);
                        TextView tv_medicine_name = (TextView) view.findViewById(R.id.tv_medicine_name);
                        TextView tv_before_weight = (TextView) view.findViewById(R.id.tv_before_weight);
                        final EditText et_reality_weight = (EditText) view.findViewById(R.id.et_reality_weight);
                        et_reality_weight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        tv_medicine_name.setText(list.get(i).drugs_name);
                        et_reality_weight.setText(list.get(i).plan_output);
                        et_reality_weight.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if(!StringUtil.isEmpty(editable.toString())){
                                    if(editable.toString().startsWith(".")){
                                        et_reality_weight.setText("");
                                    }
                                }
                            }
                        });
                        et_reality_weights.add(et_reality_weight);
                        tv_before_weight.setText(list.get(i).plan_output);

                        ll_medicine.addView(view);
                        if (i == 0) {
                            codeNums.append(list.get(i).drugs_code);
                            planNo.append(list.get(i).plan_no);
                        } else {
                            codeNums.append(",").append(list.get(i).drugs_code);
                            planNo.append(",").append(list.get(i).plan_no);
                        }
                    }
                }
            }
        }
    }

    private void setTextData(String name, TextView tv) {
        if (!StringUtil.isEmpty(name)) {
            tv.setText(name);
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case HarvestManageApi.API_HARVESTMANAGE_SAVE:
                bt_commit.setClickable(true);
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tv_harvest_date.setText(sf.format(d));
    }

    @Override
    public void onclickOk() {
        httpGetRequest(HarvestManageApi.getHarvestManageSaveUrl(
                plantTaskEntity.plant_id,
                configEntity.key,
                userInfoEntities.get(personCurrent).real_name,
                tv_harvest_date.getText().toString(),
                codeNums.toString(),
                weightNums.toString(),
                plantTaskEntity.job_no,
                plantTaskEntity.job_no_in, planNo.toString(), "1"),
                HarvestManageApi.API_HARVESTMANAGE_SAVE);
    }

    @Override
    public void onclickCancel() {
        httpGetRequest(HarvestManageApi.getHarvestManageSaveUrl(
                plantTaskEntity.plant_id,
                configEntity.key,
                userInfoEntities.get(personCurrent).real_name,
                tv_harvest_date.getText().toString(),
                codeNums.toString(),
                weightNums.toString(),
                plantTaskEntity.job_no,
                plantTaskEntity.job_no_in, planNo.toString(), "0"),
                HarvestManageApi.API_HARVESTMANAGE_SAVE);
    }
}
