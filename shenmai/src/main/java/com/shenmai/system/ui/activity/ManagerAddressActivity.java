package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenmai.system.R;
import com.shenmai.system.adapter.AddressAdapter;
import com.shenmai.system.adapter.AddressAdapter.onClickAddressListener;
import com.shenmai.system.api.AddressApi;
import com.shenmai.system.entity.AddressEntity;
import com.shenmai.system.entity.AddressList;
import com.shenmai.system.utlis.ListUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 地址管理界面
 *
 * @author KevinLi
 */
public class ManagerAddressActivity extends AbsScrollLoadMoreActivity<AddressEntity> implements OnClickListener, onClickAddressListener {

    private View rl_no_address;
    private TextView tv_order;
    private ListView address_list;
    private List<AddressEntity> addresslist;
    private int position;
    private PullToRefreshScrollView mPullToRefreshScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_address);
        initView();
        setData();
        initListener();
    }

    protected void onResume() {
        super.onResume();
        clearData();
        loadData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("管理收货地址");
        tv_order = (TextView) findViewById(R.id.tv_order);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.collect_list_refer);
        address_list = (ListView) findViewById(R.id.address_list);
        rl_no_address = findViewById(R.id.rl_no_address);
    }

    /**
     * 设置列表数据
     */
    private void setData() {
        mAdapter = new AddressAdapter(this, null);
        address_list.setAdapter(mAdapter);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setScrollingWhileRefreshingEnabled(true);
        tv_order.setOnClickListener(this);
        ((AddressAdapter)mAdapter).setOnClickAddress(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order://新增地址
                Intent intent = new Intent(ManagerAddressActivity.this, EditAddressActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
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
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap();
        params.put("key", configEntity.key);
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AddressApi.API_GET_ADDRESS:
                addressHander(json);
                break;
            case AddressApi.API_GET_DELETE_ADDRESS:
                if (addresslist == null || addresslist.size() <= position) {
                    return;
                }
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                clearData();
                loadData();
                if (mAdapter.getCount() == 0) {
                    rl_no_address.setVisibility(View.VISIBLE);
                    mPullToRefreshScrollView.setVisibility(View.GONE);
                }
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
        if (null == addList) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        } else {
            final long start = System.currentTimeMillis();
            addresslist = addList.dataList;
            if (!ListUtils.isEmpty(addresslist)) {
                rl_no_address.setVisibility(View.GONE);
                mPullToRefreshScrollView.setVisibility(View.VISIBLE);
            }
            appendData(addresslist, start);
        }
    }

    @Override
    public void editAddress(int position) {
        if (ListUtils.isEmpty(addresslist) || addresslist.size() <= position) {
            return;
        }
        Intent intent = new Intent(this, EditAddressActivity.class);
        AddressEntity address = addresslist.get(position);
        intent.putExtra("type", 2);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void deleteAddress(int position) {
        this.position = position;
        if (ListUtils.isEmpty(addresslist) || addresslist.size() <= position) {
            return;
        }
        delAddress();
    }

    /**
     * 获取删除地址信息
     */
    private void delAddress() {
        httpPostRequest(AddressApi.getDeleteAddressUrl(),
                getDeleteRequestParams(position), AddressApi.API_GET_DELETE_ADDRESS);
    }

    /**
     * 获取删除地址参数
     *
     * @return
     */
    private HashMap<String, String> getDeleteRequestParams(int position) {
        HashMap<String, String> params = new HashMap();
        params.put("key", configEntity.key);
        params.put("address_id", addresslist.get(position).address_id);
        return params;
    }

    @Override
    protected PullToRefreshScrollView getRefreshView() {
        return mPullToRefreshScrollView;
    }

    @Override
    protected void loadData() {
        getAddressList();
    }
}
