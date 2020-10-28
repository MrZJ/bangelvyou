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

import com.objectreview.system.R;
import com.objectreview.system.api.MergePackageSaveApi;
import com.objectreview.system.utlis.ListUtils;
import com.objectreview.system.utlis.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装码合并打包
 */
public class MergePackagActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_san_pack_code, tv_san_pack_code_click, tv_san_product_code,
            tv_san_product_code_click, tv_scan_number,tv_san_pro_num;
    private EditText et_san_pro_num;
    private ListView lv_scan_number;
    private Button btn_save, btn_clear;
    private List<String> lvCodes = new ArrayList();
    private ArrayAdapter<String> adapter;
    public static final int PACK_CODE = 1;
    public static final int PRO_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_packag);
        initView();
        setAdapter();
        initListener();
        setData();
    }

    private void initView() {
        initTopView();
        setTitle("包装码出厂扫描");
        tv_san_pack_code = (TextView) findViewById(R.id.tv_san_pack_code).findViewById(R.id.tv_content);
        tv_san_pack_code_click = (TextView) findViewById(R.id.tv_san_pack_code_click);
        tv_san_pro_num = (TextView) findViewById(R.id.tv_san_pro_num).findViewById(R.id.tv_content);
        et_san_pro_num = (EditText) findViewById(R.id.et_san_pro_num);
        tv_san_product_code = (TextView) findViewById(R.id.tv_san_product_code).findViewById(R.id.tv_content);
        tv_san_product_code_click = (TextView) findViewById(R.id.tv_san_product_code_click);
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
        btn_save.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    private void setData() {
        tv_san_pack_code.setText("扫描外包装码:");
        tv_san_product_code.setText("扫描内包装码(请先扫描一个外包装码):");
        tv_scan_number.setText("扫入的内包装码:");
        tv_san_pro_num.setText("内包装数量:");
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
                    intent.putExtra("codeType", "1");
                    startActivityForResult(intent, PRO_CODE);
                } else {
                    Toast.makeText(this, "请先扫描一个外包装码！", Toast.LENGTH_SHORT).show();
                }
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
                    httpPostRequest(MergePackageSaveApi.getMergePackageSave(), params, MergePackageSaveApi.API_MERGE_PACKAGE_SAVE);
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
        params.put("parent_code", tv_san_pack_code_click.getText().toString());
        params.put("num", et_san_pro_num.getText().toString());
        params.put("son_codes", pd_codes);
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
        if (StringUtil.isEmpty(et_san_pro_num.getText().toString())) {
            Toast.makeText(this, "包装数量不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ListUtils.isEmpty(lvCodes)) {
            Toast.makeText(this, "产品码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case MergePackageSaveApi.API_MERGE_PACKAGE_SAVE://保存返回json
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
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
}
