package com.objectreview.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.objectreview.system.R;
import com.objectreview.system.adapter.WheelAdapter;
import com.objectreview.system.api.CommitInfoApi;
import com.objectreview.system.api.ScanApi;
import com.objectreview.system.entity.FieldErrors;
import com.objectreview.system.entity.LogisticEntity;
import com.objectreview.system.entity.ScanResultEntity;
import com.objectreview.system.utlis.ConfigUtil;
import com.objectreview.system.utlis.StringUtil;
import com.wx.wheelview.widget.WheelView;

import java.util.HashMap;


/**
 * 服务中心产品扫描信息
 */
public class ProjectScanInfoActivity extends BaseActivity implements View.OnClickListener,WheelView.OnWheelItemClickListener{

    private PopupWindow pop = null;
    private WheelView wheelView;
    private TextView code,logisticsName;
    private EditText message,logisticsNum;
    private String scanedCode;//扫描得到的code
    private ScanResultEntity entity;//信息实体
    private Button commint;
    private View logisticsInfo;
    private String logisticsId;//物流ID
    private String ids;//产品id或包装码ids
    private boolean haveLogisticsInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_scan_info);
        scanedCode = getIntent().getStringExtra("code");
        getResultCode();
        initView();
        initListener();
    }

    private void getResultCode(){
        if(scanedCode.contains("pack_code")){
            getBZInfo();
        } else {
            getCPInfo();
        }
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("出库扫描");
        code = (TextView) findViewById(R.id.tv_project_scan);
        message = (EditText) findViewById(R.id.edt_project_beizhu);
        logisticsInfo = findViewById(R.id.lin_logistics_info);
        logisticsNum = (EditText) findViewById(R.id.edt_project_logistics_num);
        logisticsName = (TextView) findViewById(R.id.tv_project_logistics_name);
        commint = (Button) findViewById(R.id.btn_update_project_info);
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.pop_logistics, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        wheelView = (WheelView) view.findViewById(R.id.logistics_wheelview);
        wheelView.setWheelAdapter(new WheelAdapter(this));
        wheelView.setWheelSize(5);
        wheelView.setSkin(WheelView.Skin.None);
        wheelView.setWheelClickable(true);
        wheelView.setWheelData(ConfigUtil.logisticsList);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        wheelView.setStyle(style);
        if(configEntity.usertype.equals("4")){
            logisticsInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        code.setOnClickListener(this);
        logisticsName.setOnClickListener(this);
        commint.setOnClickListener(this);
        wheelView.setOnWheelItemClickListener(this);
    }

    /**
     * 获取包装信息
     */
    private void getBZInfo(){
        httpGetRequest(ScanApi.getBZUrl(configEntity.key, scanedCode), ScanApi.API_SCAN_BZ);
    }

    /**
     * 获取产品信息
     */
    private void getCPInfo(){
        httpGetRequest(ScanApi.getCPUrl(configEntity.key, scanedCode), ScanApi.API_SCAN_CP);
    }

    /**
     * 上传信息
     */
    private void commintInfo(){
        if(checkLogisticsInfo()){
            httpPostRequest(CommitInfoApi.commitLogisticsInfoUrl(), getRequestParams(), CommitInfoApi.API_COMMIT_LOGISTICS);
        }
    }

    /**
     * 物流信息不能一个为空
     */
    private boolean checkLogisticsInfo(){
        if(configEntity.usertype.equals("5")){
            if (haveLogisticsInfo){
                return true;
            }
            if(StringUtil.isEmpty(logisticsNum.getText().toString()) && !StringUtil.isEmpty(logisticsId)){
                Toast.makeText(this,"请填写物流单号",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!StringUtil.isEmpty(logisticsNum.getText().toString()) && StringUtil.isEmpty(logisticsId)){
                Toast.makeText(this,"请选择物流公司",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * 获取提交信息参数
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("ids", ids);
        if (!haveLogisticsInfo){
            if(!StringUtil.isEmpty(logisticsNum.getText().toString())){
                params.put("wl_no", logisticsNum.getText().toString());
            }
            if(!StringUtil.isEmpty(logisticsId)){
                params.put("wl_id", logisticsId);
            }
        }
        if(!StringUtil.isEmpty(message.getText().toString())){
            params.put("remark", message.getText().toString());
        }
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case ScanApi.API_SCAN_BZ:
            case ScanApi.API_SCAN_CP:
                commint.setClickable(true);
                bZCpHander(json);
                break;
            case CommitInfoApi.API_COMMIT_LOGISTICS:
                Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        switch (action){
            case ScanApi.API_SCAN_BZ:
            case ScanApi.API_SCAN_CP:
                if(null != error){
                    Toast.makeText(this, error.msg+",请重新扫码", Toast.LENGTH_SHORT).show();
                }
                code.setText("点击重新扫码");
                commint.setClickable(false);
                break;
            case CommitInfoApi.API_COMMIT_LOGISTICS:
                if(null != error){
                    Toast.makeText(this, error.msg, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 包装产品信息解析
     * @param json
     */
    private void bZCpHander(String json){
        entity = JSON.parseObject(json, ScanResultEntity.class);
        if(!StringUtil.isEmpty(entity.state)&& entity.state.equals("-1")){
            commint.setClickable(false);
            Toast.makeText(this,"标码状态错误",Toast.LENGTH_SHORT).show();
        }
        setData();
    }

    private void setData(){
        if(scanedCode.contains("pack_code")){
            code.setText(entity.pack_code);
        } else {
            code.setText(entity.pd_code);
        }
        ids = entity.ids;
        //有物流信息时只展示
        if (!StringUtil.isEmpty(entity.wl_no) || !StringUtil.isEmpty(entity.wl_name) ){
            logisticsNum.setText(entity.wl_no);
            logisticsName.setText(entity.wl_name);
            logisticsNum.setInputType(InputType.TYPE_NULL);
            logisticsName.setClickable(false);
            haveLogisticsInfo = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update_project_info:
                commintInfo();
                break;
            case R.id.tv_project_logistics_name:
                pop.showAsDropDown(logisticsName);
                break;
            case R.id.tv_project_scan:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    public void onItemClick(int position, Object o) {
        LogisticEntity entity = (LogisticEntity) o;
        logisticsName.setText(entity.wl_name);
        logisticsId = entity.wl_id;
        pop.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && !StringUtil.isEmpty(data.getStringExtra("code"))) {
            scanedCode = data.getStringExtra("code");
            getResultCode();
        }
    }
}
