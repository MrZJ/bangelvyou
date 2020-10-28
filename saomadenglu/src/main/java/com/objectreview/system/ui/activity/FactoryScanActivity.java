package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.objectreview.system.R;
import com.objectreview.system.api.AddProductCodeApi;
import com.objectreview.system.api.FactoryScanSaveApi;
import com.objectreview.system.entity.AddCodesEntity;
import com.objectreview.system.utlis.ListUtils;
import com.objectreview.system.utlis.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品出厂扫描
 */
public class FactoryScanActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener {

    private TextView tv_san_pack_code, tv_san_pack_code_click, tv_san_product_code,
            tv_san_product_code_click, tv_san_pack_more_code, tv_code_add,
            tv_product_date, tv_product_date_click, tv_batch_number, tv_scan_number;
    private EditText et_san_pack_more_code, et_batch_number;
    private ListView lv_scan_number;
    private Button btn_save, btn_clear;
    private List<String> lvCodes = new ArrayList();
    private ArrayAdapter<String> adapter;
    public static final int PACK_CODE = 1;
    public static final int PRO_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_scan);
        initView();
        setAdapter();
        initListener();
        setData();
        initViewDateDialog(this);
    }

    private void initView() {
        initTopView();
        setTitle("产品出厂扫描");
        tv_san_pack_code = (TextView) findViewById(R.id.tv_san_pack_code).findViewById(R.id.tv_content);
        tv_san_pack_code_click = (TextView) findViewById(R.id.tv_san_pack_code_click);
        tv_san_product_code = (TextView) findViewById(R.id.tv_san_product_code).findViewById(R.id.tv_content);
        tv_san_product_code_click = (TextView) findViewById(R.id.tv_san_product_code_click);
        tv_san_pack_more_code = (TextView) findViewById(R.id.tv_san_pack_more_code).findViewById(R.id.tv_content);
        et_san_pack_more_code = (EditText) findViewById(R.id.et_san_pack_more_code);
        tv_code_add = (TextView) findViewById(R.id.tv_code_add);
        tv_product_date = (TextView) findViewById(R.id.tv_product_date).findViewById(R.id.tv_content);
        tv_product_date_click = (TextView) findViewById(R.id.tv_product_date_click);
        tv_batch_number = (TextView) findViewById(R.id.tv_batch_number).findViewById(R.id.tv_content);
        et_batch_number = (EditText) findViewById(R.id.et_batch_number);
        tv_scan_number = (TextView) findViewById(R.id.tv_scan_number).findViewById(R.id.tv_content);
        lv_scan_number = (ListView) findViewById(R.id.lv_scan_number);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_clear = (Button) findViewById(R.id.btn_clear);
    }

    private void setAdapter() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvCodes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                return tv;
            }
        };
        lv_scan_number.setAdapter(adapter);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        tv_san_pack_code_click.setOnClickListener(this);
        tv_san_product_code_click.setOnClickListener(this);
        tv_product_date_click.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_code_add.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    private void setData() {
        tv_san_pack_code.setText("扫描包装码:");
        tv_san_product_code.setText("扫描产品码(请先扫描一个包装码):");
        tv_san_pack_more_code.setText("产品数量(输入数量后,点击顺序添加系统自动添加):");
        tv_product_date.setText("生产日期:");
        tv_batch_number.setText("批次号:");
        tv_scan_number.setText("扫入的产品码:");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ScanActivity.class);
        switch (v.getId()) {
            case R.id.tv_san_pack_code_click:
                intent.putExtra("codeType", "1");
                startActivityForResult(intent, PACK_CODE);
                break;
            case R.id.tv_san_product_code_click:
                if (!tv_san_pack_code_click.getText().toString().equals("点击扫描包装码")) {
                    intent.putExtra("codeType", "2");
                    startActivityForResult(intent, PRO_CODE);
                } else {
                    Toast.makeText(this, "请先扫描一个包装码！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_code_add:
                addProductCode();
                break;
            case R.id.tv_product_date_click:
                addProduceDate();
                break;
            case R.id.btn_save:
                commit();
                break;
            case R.id.btn_clear:
                lvCodes.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 保存提交
     */
    private void commit() {
        if (checkString()) {
            new Thread() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < lvCodes.size(); i++) {
                        if (i != lvCodes.size() - 1) {
                            sb.append(lvCodes.get(i)).append(",");
                        } else {
                            sb.append(lvCodes.get(i));
                        }
                    }
                    String proCodes = sb.toString();
                    Map<String, String> params = getRequestParams(proCodes);
                    httpPostRequest(FactoryScanSaveApi.getFactoryScanSave(), params, FactoryScanSaveApi.API_FACTORY_SCAN_SAVE);
                }
            }.start();
        }
    }

    /**
     * 获取保存请求参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams(String pd_codes) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("pack_code", tv_san_pack_code_click.getText().toString());
        params.put("num", et_san_pack_more_code.getText().toString());
        params.put("pd_codes", pd_codes);
        params.put("prod_date", tv_product_date_click.getText().toString());
        params.put("batch_num", et_batch_number.getText().toString());
        return params;
    }

    /**
     * 检查所有params是否为空
     */
    private boolean checkString() {
        if (tv_san_pack_code_click.getText().toString().equals("点击扫描包装码")) {
            Toast.makeText(this, "包装码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(et_san_pack_more_code.getText().toString())) {
            Toast.makeText(this, "产品数量不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ListUtils.isEmpty(lvCodes)) {
            Toast.makeText(this, "产品码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(tv_product_date_click.getText().toString())) {
            Toast.makeText(this, "生产日期不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(et_batch_number.getText().toString())) {
            Toast.makeText(this, "批次号不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 添加生产日期
     */
    private void addProduceDate() {
        dialog.show(getSupportFragmentManager(), "all");
    }

    /**
     * 批量添加产品码
     */
    private void addProductCode() {
        if (ListUtils.isEmpty(lvCodes)) {
            Toast.makeText(this, "产品码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String num = et_san_pack_more_code.getText().toString();
        if (!StringUtil.isEmpty(num) && (Integer.valueOf(num)) > 0) {
            Map<String, String> params = getAddCodeRequestParams();
            httpPostRequest(AddProductCodeApi.getAddProductCode(), params, AddProductCodeApi.API_ADD_PRODUCT_CODE);
        } else {
            Toast.makeText(this, "产品数量必须数必须大于0!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取保存请求参数
     *
     * @return
     */
    private HashMap<String, String> getAddCodeRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("pd_code", lvCodes.get(0));
        params.put("num", et_san_pack_more_code.getText().toString());
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AddProductCodeApi.API_ADD_PRODUCT_CODE://自动添加返回json
                AddProCodes(json);
                break;
            case FactoryScanSaveApi.API_FACTORY_SCAN_SAVE://保存返回json
                Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void AddProCodes(String json) {
        AddCodesEntity entity = JSON.parseObject(json, AddCodesEntity.class);
        if (entity != null && !StringUtil.isEmpty(entity.state) && entity.state.equals("0")) {
            if (!StringUtil.isEmpty(entity.pd_codes)) {
                String[] codes = entity.pd_codes.split(",");
                lvCodes.clear();
                lvCodes.addAll(Arrays.asList(codes));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && !StringUtil.isEmpty(data.getStringExtra("code"))) {
            String code = data.getStringExtra("code");
            switch (requestCode) {
                case PACK_CODE:
                    tv_san_pack_code_click.setText(code);
                    break;
                case PRO_CODE:
                    if (checkCodes(code)) {
                        lvCodes.add(code);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    /**
     * 检验产品码是否重复添加
     *
     * @param code
     * @return
     */
    private boolean checkCodes(String code) {
        if (!ListUtils.isEmpty(lvCodes)) {
            for (String lvCode : lvCodes) {
                if (code.equals(lvCode)) {
                    Toast.makeText(this, "此产品码已存在,请勿重复添加!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tv_product_date_click.setText(sf.format(d));
    }
}
