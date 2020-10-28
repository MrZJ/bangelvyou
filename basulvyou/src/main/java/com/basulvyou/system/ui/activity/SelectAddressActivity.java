package com.basulvyou.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.SelectAddressAdapter;
import com.basulvyou.system.api.AddressApi;
import com.basulvyou.system.entity.RegionAddressEntity;
import com.basulvyou.system.entity.RegionAddressList;
import com.basulvyou.system.util.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectAddressActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView address_list;
    private SelectAddressAdapter adapter;
    private int type = 1;//定义当前选择的类型:  1:省份直辖市   2:地级市 3:区县
    private String area_id = "";
    private List<RegionAddressEntity> regionAddresslist;
    private ArrayList<RegionAddressEntity> selectAddresslist = new ArrayList<RegionAddressEntity>();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initView();
        initListener();
        setAdapter();
        getAddressRegion();
    }

    protected void onResume(){
        super.onResume();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("选择地址");
        address_list = (ListView) findViewById(R.id.address_list);
    }

    /**
     * 设置tab数据
     */
    private void setAdapter(){
        adapter = new SelectAddressAdapter(this);
        address_list.setAdapter(adapter);
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        address_list.setOnItemClickListener(this);
    }

    /**
     * 获取省份信息
     */
    private void getAddressRegion(){
        httpPostRequest(AddressApi.getAddressRegionUrl(),
                getRequestParams(), AddressApi.API_GET_ADDRESS_REGION);
    }

    /**
     * 获取省份参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("area_id", area_id);
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action){
            case AddressApi.API_GET_ADDRESS_REGION:
                addressRegionHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理地理信息
     * @param json
     */
    private void addressRegionHander(String json){
//        hiddenLoading();
        RegionAddressList regionAddress = JSON.parseObject(json, RegionAddressList.class);
        if (null == regionAddress) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }else{
            if(!ListUtils.isEmpty(regionAddress.list)){
                regionAddresslist = regionAddress.list;
                adapter.setData(regionAddresslist);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if (null != regionAddresslist && position < regionAddresslist.size()) {
//            showLoading(getString(R.string.tip_loading), true);
            RegionAddressEntity address = regionAddresslist.get(position);
            if (address !=null) {
                selectAddresslist.add(address);
                if(type == 1){
                    type = 2;
                } else if(type == 2){
                    type = 3;
                } else if(type == 3){
                    Intent intent = new Intent();
                    intent.putExtra("address_list", selectAddresslist);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return;
                }
                area_id = address.area_id;
                getAddressRegion();
            }
        }

    }

}

