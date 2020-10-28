package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.AddressAdapter;
import com.basulvyou.system.api.AddressApi;
import com.basulvyou.system.entity.AddressEntity;
import com.basulvyou.system.entity.AddressList;
import com.basulvyou.system.util.ListUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 地址管理界面
 */
public class AddressManageActivity extends BaseActivity implements View.OnClickListener, AddressAdapter.OnClickAddressListener {

    private Button addNewAddress;
    private ListView addressList;
    private AddressAdapter addressAdapter;
    private View noAddressTip, empty_tv, progressBar;
    private String addressId;//地址ID
    private List<AddressEntity> addresslist;
    private boolean isDel;//是否删除了选中的地址
    private int delPosition;//要删除的下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
//        addressId = getIntent().getExtras().getString("addressId");
        initView();
        setData();
        initListener();

    }

    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("地址管理");
        addNewAddress = (Button) findViewById(R.id.btn_address_manage_add);
        addressList = (ListView) findViewById(R.id.list_address_manage_adslist);
        noAddressTip = findViewById(R.id.rel_address_manage_tip);
        empty_tv = findViewById(R.id.empty_tv);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        addNewAddress.setOnClickListener(this);
        addressAdapter.setOnClickAddress(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_address_manage_add:
                Intent editIntent = new Intent(this, EditAddressActivity.class);
                editIntent.putExtra("type", 1);
                startActivity(editIntent);
                break;
        }
    }

    /**
     * 设置列表数据
     */
    private void setData() {
        addressAdapter = new AddressAdapter(this, null);
        addressList.setAdapter(addressAdapter);
    }

    /**
     * 获取地址数据信息
     */
    private void getAddressList() {
        httpPostRequest(AddressApi.getAddressUrl(),
                getRequestParams(), AddressApi.API_GET_ADDRESS);
    }

    /**
     * 获地址参数
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        return params;
    }

    /**
     * 返回数据
     *
     * @param json
     * @param action
     */
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AddressApi.API_GET_DELETE_ADDRESS:
                if (addresslist == null || addresslist.size() <= delPosition) {
                    return;
                }
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
//                if(addresslist.get(delPosition).address_id.endsWith(addressId)){
//                    isDel = true;
//                }
                addresslist.removeAll(addresslist);
                addressAdapter.notifyDataSetChanged();
                getAddressList();
                break;
            case AddressApi.API_GET_ADDRESS:
                addressHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理地址信息
     *
     * @param json
     */
    private void addressHander(String json) {
        AddressList addList = JSON.parseObject(json, AddressList.class);
        if (addList == null || ListUtils.isEmpty(addList.list)) {
            showEmptyTip();
        } else {
            addresslist = addList.list;
            if (!ListUtils.isEmpty(addresslist)) {
                noAddressTip.setVisibility(View.GONE);
                addressList.setVisibility(View.VISIBLE);
                addressAdapter.setData(addresslist);
                addressAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void editAddress(int position) {
        if (ListUtils.isEmpty(addresslist) || addresslist.size() <= position) {
            return;
        }
        Intent intent = new Intent(AddressManageActivity.this, EditAddressActivity.class);
        AddressEntity address = addresslist.get(position);
        intent.putExtra("type", 2);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void deleteAddress(int position) {
//         删除地址
        if (ListUtils.isEmpty(addresslist) || addresslist.size() <= position) {
            return;
        }
        delPosition = position;
        delAddress(position);
    }

    /**
     * 删除地址
     */
    private void delAddress(int position) {
        httpPostRequest(AddressApi.getDeleteAddressUrl(),
                getDeleteRequestParams(position), AddressApi.API_GET_DELETE_ADDRESS);
    }

    /**
     * 删除地址参数
     *
     * @return
     */
    private HashMap<String, String> getDeleteRequestParams(int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("address_id", addresslist.get(position).address_id);
        return params;
    }

    private void showEmptyTip() {
        if (noAddressTip != null) {
            noAddressTip.setVisibility(View.VISIBLE);
            empty_tv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
