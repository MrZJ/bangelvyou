package com.zycreview.system.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.FertilizerOrPesticideApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.CacheObjUtils;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 农药管理添加界面
 */
public class PesticideManageActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener, OnWheelChangedListener, OnWheelClickedListener {
    private EditText et_pesticide_name, et_pesticide_weight, et_pesticide_mobile;
    private TextView tv_pesticide_date, tv_pesticide_person;
    private Button bt_commit;
    private String job_no_in;
    private String job_no;

    private View pop_rl_title, pop_ll_context;
    private PopupWindow pop = null;
    private WheelView mViewProvince;
    private List<UserInfoEntity> userInfoEntities;//作业人
    private String[] userString;//作业人
    private int personCurrent;//当前点击的作业人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticide_manage);
        job_no_in = getIntent().getStringExtra("job_no_in");
        job_no = getIntent().getStringExtra("job_no");
        initView();
        initListener();
        getPersonString();
    }

    private void initView() {
        initTopView();
        setTitle("喷药添加");
        et_pesticide_name = (EditText) findViewById(R.id.et_pesticide_name).findViewById(R.id.et_register);
        et_pesticide_weight = (EditText) findViewById(R.id.et_pesticide_weight).findViewById(R.id.et_register);
        tv_pesticide_date = (TextView) findViewById(R.id.tv_pesticide_date);
        tv_pesticide_person = (TextView) findViewById(R.id.tv_pesticide_person);
        et_pesticide_mobile = (EditText) findViewById(R.id.et_pesticide_mobile).findViewById(R.id.et_register_gray);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        initViewDateDialog(this);

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

    public void initListener() {
        super.initListener();
        initSideBarListener();
        bt_commit.setOnClickListener(this);
        tv_pesticide_date.setOnClickListener(this);
        tv_pesticide_person.setOnClickListener(this);

        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewProvince.addClickingListener(this);
        et_pesticide_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String editable = et_pesticide_weight.getText().toString();
                    if (!StringUtil.isEmpty(editable)) {
                        //数据大于0.00才能满足
                        if (!StringUtil.dataAboveZero(editable)) {
                            Toast.makeText(PesticideManageActivity.this, "农药重量不符规则", Toast.LENGTH_SHORT).show();
                            et_pesticide_weight.setText("");
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        et_pesticide_name.setHint("农药名称");
        et_pesticide_weight.setHint("农药重量(千克)");
        et_pesticide_weight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_pesticide_mobile.setHint("作业人电话");
        et_pesticide_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        et_pesticide_mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:
                commitData();
                break;
            case R.id.tv_pesticide_date:
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.tv_pesticide_person:
                if (userString != null) {
                    setPersonData();
                    pop.showAsDropDown(tv_pesticide_person);
                } else {
                    Toast.makeText(this, "没有可选作业人", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updatePersonDatas();
    }


    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mViewProvince.getCurrentItem() == itemIndex) {
            pop.dismiss();
            tv_pesticide_person.setText(userString[personCurrent]);
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

    /**
     * 提交按钮触发事件
     */
    private void commitData() {
        if (checkData()) {
            bt_commit.setClickable(false);
            httpPostRequest(FertilizerOrPesticideApi.getPesticideSaveTaskUrl(),
                    getRequestParams(), FertilizerOrPesticideApi.API_P_SAVE);
        }
    }

    /**
     * 农药管理提交参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobileLogin", "true");
        params.put("key", configEntity.key);
        params.put("job_no_in", job_no_in);
        params.put("job_no", job_no);
        params.put("yw_type", et_pesticide_name.getText().toString());
        params.put("yw_weight", et_pesticide_weight.getText().toString());
        params.put("work_date", tv_pesticide_date.getText().toString());
        if (!tv_pesticide_person.getText().toString().equals("作业人"))
            params.put("work_man", userInfoEntities.get(personCurrent).real_name);
        if (!StringUtil.isEmpty(et_pesticide_mobile.getText().toString()))
            params.put("work_man_phone", et_pesticide_mobile.getText().toString());
        return params;
    }

    private boolean checkData() {
        if (!checkDataIsFalse(et_pesticide_name, "农药名称不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_pesticide_weight, "农药重量不能为空!")) {
            return false;
        }
        if (tv_pesticide_date.getText().toString().equals("喷药时间")) {
            Toast.makeText(this, "请选择喷药时间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tv_pesticide_person.getText().toString().equals("作业人")) {
            Toast.makeText(this, "请选择作业人！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case FertilizerOrPesticideApi.API_P_SAVE:
                Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        bt_commit.setClickable(true);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tv_pesticide_date.setText(sf.format(d));
    }
}
