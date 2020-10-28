package com.objectreview.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.objectreview.system.R;
import com.objectreview.system.api.FactoryLogDetailApi;
import com.objectreview.system.entity.FactoryLogDetailEntity;
import com.objectreview.system.utlis.StringUtil;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class FactoryLogDetailActivity extends BaseActivity {

    private TextView tv_pd_name, tv_p_name, tv_entp_name, tv_prod_date,
            tv_batch_num, tv_pack_code, tv_pack_num, tv_list;
    private ListView lv_list;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_log_detail);
        id = getIntent().getStringExtra("id");
        initView();
        initListener();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("产品出厂纪录详情");
        setLeftBackButton();
        tv_pd_name = (TextView) findViewById(R.id.tv_pd_name).findViewById(R.id.tv_content);
        tv_p_name = (TextView) findViewById(R.id.tv_p_name).findViewById(R.id.tv_content);
        tv_entp_name = (TextView) findViewById(R.id.tv_entp_name).findViewById(R.id.tv_content);
        tv_prod_date = (TextView) findViewById(R.id.tv_prod_date).findViewById(R.id.tv_content);
        tv_batch_num = (TextView) findViewById(R.id.tv_batch_num).findViewById(R.id.tv_content);
        tv_pack_code = (TextView) findViewById(R.id.tv_pack_code).findViewById(R.id.tv_content);
        tv_pack_num = (TextView) findViewById(R.id.tv_pack_num).findViewById(R.id.tv_content);
        tv_list = (TextView) findViewById(R.id.tv_list).findViewById(R.id.tv_content);
        lv_list = (ListView) findViewById(R.id.lv_list);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void loadData() {
        httpGetRequest(FactoryLogDetailApi.getFactoryLogDetail(configEntity.key, id), FactoryLogDetailApi.API_FACTORY_LOG_DETAIL);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case FactoryLogDetailApi.API_FACTORY_LOG_DETAIL:
                factoryLogDetailHander(json);
                break;
        }
    }

    private void factoryLogDetailHander(String json) {
        if (!StringUtil.isEmpty(json)) {
            FactoryLogDetailEntity entity = JSON.parseObject(json, FactoryLogDetailEntity.class);
            if (entity != null) {
                if (!StringUtil.isEmpty(entity.p_name)) {
                    tv_pd_name.setText("产品名称:" + entity.pd_name);
                } else {
                    tv_pd_name.setText("产品名称:无");
                }
                if (!StringUtil.isEmpty(entity.p_name)) {
                    tv_p_name.setText("所属地区:" + entity.p_name);
                } else {
                    tv_p_name.setText("所属地区:无");
                }
                if (!StringUtil.isEmpty(entity.entp_name)) {
                    tv_entp_name.setText("所属企业:" + entity.entp_name);
                } else {
                    tv_entp_name.setText("所属企业:无");
                }
                if (!StringUtil.isEmpty(entity.prod_date)) {
                    tv_prod_date.setText("生产日期:" + entity.prod_date);
                } else {
                    tv_prod_date.setText("生产日期:无");
                }
                if (!StringUtil.isEmpty(entity.batch_num)) {
                    tv_batch_num.setText("批号:" + entity.batch_num);
                } else {
                    tv_batch_num.setText("批号:无");
                }
                if (!StringUtil.isEmpty(entity.pack_code)) {
                    tv_pack_code.setText("包装码:" + entity.pack_code);
                } else {
                    tv_pack_code.setText("包装码:无");
                }
                if (!StringUtil.isEmpty(entity.pack_num)) {
                    tv_pack_num.setText("包装数量:" + entity.pack_num);
                } else {
                    tv_pack_num.setText("包装数量:无");
                }
                tv_list.setText("产品码信息:");
                if (!StringUtil.isEmpty(entity.list)) {
                    String[] pruCodes = entity.list.split(",");
                    lv_list.setVisibility(View.VISIBLE);
                    lv_list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, pruCodes) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView tv = (TextView) super.getView(position, convertView, parent);
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            return tv;
                        }
                    });
                }
            }
        }
    }
}
