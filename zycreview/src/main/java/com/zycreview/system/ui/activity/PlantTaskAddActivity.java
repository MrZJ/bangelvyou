package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.PlantTaskAddApi;
import com.zycreview.system.entity.AddViewEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.PlantAddDataEntity;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.CacheObjUtils;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import org.xutils.common.util.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 种植管理添加任务
 */
public class PlantTaskAddActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener, OnWheelChangedListener, OnWheelClickedListener {

    private View et_plant_task_name, et_plant_name, et_plant_production_code,
            et_plant_size, et_plant_source, et_plant_weight,
            et_plant_person_mobile, et_plant_style, et_plant_week, pop_rl_title, pop_ll_context;
    private EditText et_plant_task_name_text, et_plant_name_text, et_plant_production_code_text,
            et_plant_size_text, et_plant_source_text, et_plant_weight_text,
            et_plant_person_mobile_text, et_plant_week_text, et_choose_medicine;
    private TextView tv_base_province, tv_year_month_day, tv_choose_medicine, tv_plant_person, tv_plant_way;
    private ImageView iv_add_medicine;
    private LinearLayout ll_add_medicine;
    private Button bt_commit;
    private List<AddViewEntity> views = new ArrayList();//所有添加view

    // 选择药材和基地相关
    private PopupWindow pop = null;
    private WheelView mViewProvince;
    private int popType = 0;
    private int pCurrent;
    private int personCurrent;//当前点击的负责人
    private int iCurrent;//当前显示的药材
    private int wCurrent;//当前显示的方式
    private List<Integer> iCurrents = new ArrayList<>();//总共选择的药材
    private int viewCurrent;//当前点击的药材
    private List<PlantAddDataEntity.IngredInfos> ingredInfoses;//药材
    private List<PlantAddDataEntity.PlantBases> plantBases;//基地
    private List<PlantAddDataEntity.PlantWay> plantWay;//种植方式
    private List<UserInfoEntity> userInfoEntities;//种植负责人
    private String[] ingredString;//药材
    private String[] plantString;//基地
    private String[] userString;//负责人
    private String[] wayString;//种植方式
    private HashMap<String, String> plantIngreMap;//存储药材名称和id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_task_add);
        initView();
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
        setTitle("种植任务添加");
        et_plant_task_name = findViewById(R.id.et_plant_task_name);
        et_plant_task_name_text = (EditText) et_plant_task_name.findViewById(R.id.et_register);
        et_plant_name = findViewById(R.id.et_plant_name);
        et_plant_name_text = (EditText) et_plant_name.findViewById(R.id.et_register);
        tv_base_province = (TextView) findViewById(R.id.tv_base_province);
        et_plant_production_code = findViewById(R.id.et_plant_production_code);
//        et_plant_production_code_text = (EditText) et_plant_production_code.findViewById(R.id.et_register);
//        et_plant_size = findViewById(R.id.et_plant_size);
        et_plant_size_text = (EditText) findViewById(R.id.et_plant_size);
        et_plant_source = findViewById(R.id.et_plant_source);
        et_plant_source_text = (EditText) et_plant_source.findViewById(R.id.et_register_gray);
//        et_plant_weight = findViewById(R.id.et_plant_weight);
        et_plant_weight_text = (EditText) findViewById(R.id.et_plant_weight);
        tv_plant_person = (TextView) findViewById(R.id.tv_plant_person);
        et_plant_person_mobile = findViewById(R.id.et_plant_person_mobile);
        et_plant_person_mobile_text = (EditText) et_plant_person_mobile.findViewById(R.id.et_register_gray);
        tv_plant_way = (TextView) findViewById(R.id.tv_plant_way);
        tv_year_month_day = (TextView) findViewById(R.id.tv_year_month_day);
        et_plant_week = findViewById(R.id.et_plant_week);
        et_plant_week_text = (EditText) et_plant_week.findViewById(R.id.et_register_gray);
        iv_add_medicine = (ImageView) findViewById(R.id.iv_add_medicine);
        ll_add_medicine = (LinearLayout) findViewById(R.id.ll_add_medicine);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        et_choose_medicine = (EditText) findViewById(R.id.et_choose_medicine).findViewById(R.id.et_register);
        tv_choose_medicine = (TextView) findViewById(R.id.tv_choose_medicine);
        tv_choose_medicine.setTag(-1);
        et_choose_medicine.setTag(-1);
        initViewDateDialog(this);

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
        initSideBarListener();
        tv_base_province.setOnClickListener(this);
        tv_year_month_day.setOnClickListener(this);
        iv_add_medicine.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        tv_choose_medicine.setOnClickListener(this);
        tv_plant_person.setOnClickListener(this);
        tv_plant_way.setOnClickListener(this);
        //选择地区相关
        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewProvince.addClickingListener(this);
        //预收货判断
        et_choose_medicine.addTextChangedListener(new MyTextWatcher(et_choose_medicine));
        //种植面积判断
        et_plant_size_text.addTextChangedListener(new MyTextWatcher(et_plant_size_text));
        //种子重量判断
        et_plant_weight_text.addTextChangedListener(new MyTextWatcher(et_plant_weight_text));
    }

    private class MyTextWatcher implements TextWatcher {

        private EditText editText;

        private MyTextWatcher(EditText selectEdit) {
            editText = selectEdit;
        }

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
                    editText.setText("");
                }
            }
        }
    }

    private void setData() {
        et_plant_task_name_text.setHint("种植物任务名称");
        et_plant_name_text.setHint("种植物名称");
//        et_plant_production_code_text.setHint("产地编码");
//        et_plant_production_code_text.setInputType(InputType.TYPE_CLASS_NUMBER);
//        et_plant_size_text.setHint("种植面积");
        et_plant_size_text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_plant_source_text.setHint("种子来源");
//        et_plant_weight_text.setHint("种子重量(千克)");
        et_plant_weight_text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_plant_person_mobile_text.setHint("负责人电话");
        et_plant_person_mobile_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        et_plant_person_mobile_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_plant_week_text.setHint("种植周期");
        et_choose_medicine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        httpGetRequest(PlantTaskAddApi.getPlantTaskAddGetUrl(configEntity.key), PlantTaskAddApi.API_PLANTTASKADDGET);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_base_province://基地种植药材生产
                if (plantString != null) {
                    popType = 0;
                    setBaseData();
                    pop.showAsDropDown(tv_base_province);
                } else {
                    Toast.makeText(this, "没有可选基地", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_plant_person://种植负责人
                if (userString != null) {
                    popType = 2;
                    setPersonData();
                    pop.showAsDropDown(tv_plant_person);
                } else {
                    Toast.makeText(this, "没有可选负责人", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_plant_way:
                if (wayString != null) {
                    popType = 3;
                    setWayData();
                    int h = DensityUtil.dip2px(200) + tv_plant_way.getHeight();
                    pop.showAsDropDown(tv_plant_way, 0, -h);
                } else {
                    Toast.makeText(this, "没有可选方式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_year_month_day://年月日选择
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.iv_add_medicine://增加产出药材信息
                addChildView();
                break;
            case R.id.bt_commit://提交按钮
                commitData();
                break;
            case R.id.tv_choose_medicine://选择药材
                if (ingredString != null) {
                    popType = 1;
                    setMedicineData();
                    if ((int) v.getTag() >= 0) {//添加的view
                        viewCurrent = (int) v.getTag();
                        int h = DensityUtil.dip2px(200) + tv_choose_medicine.getHeight();
                        pop.showAsDropDown(views.get(viewCurrent).tvChild, 0, -h);
                    } else {//第一个原始的view
                        viewCurrent = -1;
                        int h = DensityUtil.dip2px(200) + tv_choose_medicine.getHeight();
                        pop.showAsDropDown(tv_choose_medicine, 0, -h);
                    }

                } else {
                    Toast.makeText(this, "没有可选药材", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_clear_medicine://移除药材(移除view)
                if ((int) v.getTag() >= 0) {
                    ll_add_medicine.removeAllViews();
                    views.remove((int) v.getTag());
                    for (int i = 0; i < views.size(); i++) {
                        AddViewEntity entity = views.get(i);
                        ll_add_medicine.addView(entity.vChild);
                        entity.ivChild.setTag(i);
                        entity.tvChild.setTag(i);
                        entity.etChild.setTag(i);
                    }
                }
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (popType == 0) {
            updateBaseDatas();
        } else if (popType == 1) {
            updateTypes();
        } else if (popType == 2) {
            updatePersonDatas();
        } else if (popType == 3) {
            updateWayDatas();
        }
    }


    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mViewProvince.getCurrentItem() == itemIndex) {
            pop.dismiss();
            if (popType == 0) {
                tv_base_province.setText(plantBases.get(pCurrent).base_name);
            } else if (popType == 1) {
                if (!iCurrents.contains(iCurrent)) {
                    iCurrents.add(iCurrent);
                    if (viewCurrent == -1) {
                        if (!StringUtil.isEmpty(ingredInfoses.get(iCurrent).ingred_name)) {
                            if (!tv_choose_medicine.getText().equals("选择药材")) {
                                //此处删除以前选择的
                                iCurrents.remove(Integer.valueOf(tv_choose_medicine.getHint().toString()));
                            }
                            tv_choose_medicine.setText(ingredInfoses.get(iCurrent).ingred_name);
                            tv_choose_medicine.setHint(iCurrent + "");//此处为了将以前选择的删除
                        }
                    } else {
                        if (!StringUtil.isEmpty(ingredInfoses.get(iCurrent).ingred_name)) {
                            if (!views.get(viewCurrent).tvChild.getText().equals("选择药材")) {
                                //此处删除以前选择的
                                iCurrents.remove(Integer.valueOf(views.get(viewCurrent).tvChild.getHint().toString()));
                            }
                            views.get(viewCurrent).tvChild.setText(ingredInfoses.get(iCurrent).ingred_name);
                            views.get(viewCurrent).tvChild.setHint(iCurrent + "");//此处为了将以前选择的删除
                        }
                    }
                } else {
                    Toast.makeText(this, "不能重复选择！", Toast.LENGTH_SHORT).show();
                }
            } else if (popType == 2) {
                tv_plant_person.setText(userString[personCurrent]);
            } else if (popType == 3) {
                tv_plant_way.setText(wayString[wCurrent]);
            }
        }
    }


    private void setMedicineData() {
        //选择类型相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, ingredString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(0);
        updateTypes();
    }

    /**
     * 设置地域数据
     */
    private void setBaseData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, plantString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(pCurrent);
        updateBaseDatas();
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

    /**
     * 设置种植方式
     */
    private void setWayData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(this, wayString);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(wCurrent);
        updateWayDatas();
    }

    private void updateBaseDatas() {
        pCurrent = mViewProvince.getCurrentItem();
    }

    private void updatePersonDatas() {
        personCurrent = mViewProvince.getCurrentItem();
    }

    private void updateTypes() {
        iCurrent = mViewProvince.getCurrentItem();
    }

    private void updateWayDatas() {
        wCurrent = mViewProvince.getCurrentItem();
    }

    /**
     * 添加预产出药材信息
     */
    private void addChildView() {
        final View view = View.inflate(this, R.layout.plant_add_medicine, null);
        final AddViewEntity viewChild = new AddViewEntity();
        ll_add_medicine.addView(view);
        viewChild.vChild = view;
        viewChild.tvChild = (TextView) view.findViewById(R.id.tv_choose_medicine);
        viewChild.etChild = (EditText) view.findViewById(R.id.et_choose_medicine).findViewById(R.id.et_register_gray);
        viewChild.etChild.addTextChangedListener(new TextWatcher() {
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
                        viewChild.etChild.setText("");
                    }
                }
            }
        });
        viewChild.ivChild = (ImageView) view.findViewById(R.id.iv_clear_medicine);
        viewChild.ivChild.setTag(views.size());
        viewChild.etChild.setTag(views.size());
        viewChild.tvChild.setTag(views.size());
        viewChild.ivChild.setOnClickListener(this);
        viewChild.tvChild.setOnClickListener(this);
        viewChild.etChild.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        views.add(viewChild);
    }

    /**
     * 提交按钮触发事件
     */
    private void commitData() {
        if (checkData()) {
            bt_commit.setClickable(false);
            httpPostRequest(PlantTaskAddApi.getPlantTaskAddUrl(),
                    getRequestParams(), PlantTaskAddApi.API_PLANTTASKADD);
        }
    }

    /**
     * 添加任务参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobileLogin", "true");
        params.put("mod_id", "5002000100");
        params.put("key", configEntity.key);
        params.put("base_code_in", plantBases.get(pCurrent).base_code_in);
        params.put("grow_way", tv_plant_way.getText().toString());
        params.put("manager", userInfoEntities.get(personCurrent).real_name);
        if (!StringUtil.isEmpty(et_plant_person_mobile_text.getText().toString())) {
            params.put("manager_phone", et_plant_person_mobile_text.getText().toString());
        }
        params.put("name", et_plant_task_name_text.getText().toString());
//        params.put("place_code", et_plant_production_code_text.getText().toString());
        if (!StringUtil.isEmpty(et_plant_size_text.getText().toString())) {
            params.put("plant_area", et_plant_size_text.getText().toString());
        }
        if (!tv_year_month_day.getText().toString().equals("年/月/日")) {
            params.put("plant_date", tv_year_month_day.getText().toString());
        }
        params.put("plant_name", et_plant_name_text.getText().toString());
        if (!StringUtil.isEmpty(et_plant_week_text.getText().toString())) {
            params.put("plant_zq", et_plant_week_text.getText().toString());
        }
        if (!StringUtil.isEmpty(et_plant_source_text.getText().toString())) {
            params.put("seed_source", et_plant_source_text.getText().toString());
        }
        if (!StringUtil.isEmpty(et_plant_weight_text.getText().toString())) {
            params.put("seed_weight", et_plant_weight_text.getText().toString());
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer nums = new StringBuffer();
        sb.append(plantIngreMap.get(tv_choose_medicine.getText().toString()));
        nums.append(et_choose_medicine.getText().toString());
        if (iCurrents.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                if (!views.get(i).tvChild.getText().toString().equals("选择药材")) {
                    sb.append(",").append(plantIngreMap.get(views.get(i).tvChild.getText().toString()));
                    nums.append(",").append(views.get(i).etChild.getText().toString());
                }
            }
        }
        params.put("ingred_code", sb.toString());
        params.put("plan_output", nums.toString());
        return params;
    }

    /**
     * 必填项是否有空
     *
     * @return
     */
    private boolean checkData() {
        if (!checkDataIsFalse(et_plant_task_name_text, "种植任务名称不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_plant_name_text, "种植物名称不能为空!")) {
            return false;
        }
        if (tv_base_province.getText().equals("基地药材生产")) {
            Toast.makeText(this, "种植基地不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(et_plant_size_text.getText().toString().trim()) || !StringUtil.dataAboveZero(et_plant_size_text.getText().toString().trim())) {
            Toast.makeText(this, "种植面积不符合规则", Toast.LENGTH_SHORT).show();
            et_plant_size_text.setText("");
            return false;
        }
        if (tv_plant_person.getText().toString().equals("种植负责人")) {
            Toast.makeText(this, "种植负责人不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!StringUtil.isEmpty(et_plant_weight_text.getText().toString().trim())) {
            if (!StringUtil.dataAboveZero(et_plant_weight_text.getText().toString().trim())) {
                Toast.makeText(this, "种植重量不符合规则", Toast.LENGTH_SHORT).show();
                et_plant_weight_text.setText("");
                return false;
            }
        }
        if (tv_plant_way.getText().equals("种植方式")) {
            Toast.makeText(this, "种植方式不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tv_choose_medicine.getText().toString().equals("选择药材")) {
            Toast.makeText(this, "产出药材信息至少有一个!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(et_choose_medicine.getText().toString()) || !StringUtil.dataAboveZero(et_choose_medicine.getText().toString())) {
            Toast.makeText(this, "预产量不符合规则", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (iCurrents.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                if (!views.get(i).tvChild.getText().toString().equals("选择药材")) {
                    if (StringUtil.isEmpty(views.get(i).etChild.getText().toString()) || !StringUtil.dataAboveZero(views.get(i).etChild.getText().toString())) {
                        Toast.makeText(this, views.get(i).tvChild.getText().toString() + "预产量不符合规则", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "请选择药材", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PlantTaskAddApi.API_PLANTTASKADD:
                Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case PlantTaskAddApi.API_PLANTTASKADDGET:
                PlantAddDataEntity entity = JSON.parseObject(json, PlantAddDataEntity.class);
                if (entity != null) {
                    ingredInfoses = entity.ingredInfos;
                    plantBases = entity.plantBases;
                    plantWay = entity.zzfsList;
                    if (!ListUtils.isEmpty(ingredInfoses)) {
                        ingredString = new String[ingredInfoses.size()];
                        plantIngreMap = new HashMap<>();
                        for (int i = 0; i < ingredInfoses.size(); i++) {
                            ingredString[i] = ingredInfoses.get(i).ingred_name;
                            plantIngreMap.put(ingredInfoses.get(i).ingred_name, ingredInfoses.get(i).ingred_code);
                        }
                    }
                    if (!ListUtils.isEmpty(plantBases)) {
                        plantString = new String[plantBases.size()];
                        for (int i = 0; i < plantBases.size(); i++) {
                            plantString[i] = plantBases.get(i).base_name;
                        }
                    }
                    if (!ListUtils.isEmpty(plantWay)) {
                        wayString = new String[plantWay.size()];
                        for (int i = 0; i < plantWay.size(); i++) {
                            wayString[i] = plantWay.get(i).grow_way;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case PlantTaskAddApi.API_PLANTTASKADD:
                bt_commit.setClickable(true);
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tv_year_month_day.setText(sf.format(d));
    }
}
