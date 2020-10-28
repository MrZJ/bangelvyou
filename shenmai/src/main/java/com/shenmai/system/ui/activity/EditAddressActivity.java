package com.shenmai.system.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenmai.system.R;
import com.shenmai.system.api.AddressApi;
import com.shenmai.system.entity.AddressEntity;
import com.shenmai.system.entity.RegionAddressEntity;
import com.shenmai.system.utlis.CheckMobile;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.TopClickUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新增地址界面
 *
 * @author KevinLi
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class EditAddressActivity extends BaseActivity implements OnClickListener {

    private static final int CITY_REQUEST_CODE = 1;
    private View ll_next;
    private EditText edt_name, edt_phone, edt_address;
    private TextView tv_region;
    private List<RegionAddressEntity> regionAddresslist;
    private AddressEntity address;
    private int type;//1：添加  2：编辑修改

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        type = getIntent().getExtras().getInt("type", 1);
        if (type == 2) {
            address = (AddressEntity) getIntent().getExtras().getSerializable("address");
        }
        initView();
        initListener();
    }

    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTopView();
        setLeftBackButton();
        setTopRightTitle("保存", TopClickUtil.TOP_SAVE_ADDRESS);
        ll_next = findViewById(R.id.ll_next);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_address = (EditText) findViewById(R.id.edt_address);
        tv_region = (TextView) findViewById(R.id.tv_region);
        if (type == 2) {
            setTitle("修改收货地址");
            regionAddresslist = new ArrayList();
            for (int i = 0; i < 3; i++) {
                RegionAddressEntity addressEntity = new RegionAddressEntity();
                if (i == 0) {
                    addressEntity.area_id = address.area_id.substring(0, 2) + "0000";
                } else if (i == 1) {
                    addressEntity.area_id = address.city_id;
                } else if (i == 2) {
                    addressEntity.area_id = address.area_id;
                }
                regionAddresslist.add(addressEntity);
            }
            edt_name.setText(address.true_name);
            edt_phone.setText(address.mob_phone);
            tv_region.setText(address.area_info);
            edt_address.setText(address.address);
        } else {
            setTitle("新建收货地址");
        }
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        ll_next.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_next://选择地址
                startActivityForResult(new Intent(EditAddressActivity.this,
                        SelectAddressActivity.class), CITY_REQUEST_CODE);
                break;
        }
    }

    public void saveAddress() {
        String name = edt_name.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String region = tv_region.getText().toString().trim();
        if (name == null || name.isEmpty()) {
            Toast.makeText(this, "请先输入收货人名字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone == null || phone.isEmpty()) {
            Toast.makeText(this, "请先输入联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CheckMobile.isMobileNO(phone)) {
            Toast.makeText(this, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address == null || address.isEmpty()) {
            Toast.makeText(this, "请先输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (region == null || region.isEmpty()) {
            Toast.makeText(this, "请先输入所属地区", Toast.LENGTH_SHORT).show();
            return;
        }
        addAddress(name, phone, address, region);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    ContentResolver reContentResolverol = getContentResolver();
                    Uri contactData = data.getData();
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null, null);
                    while (phone.moveToNext()) {
                        String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        edt_phone.setText(usernumber);
                    }
                    edt_name.setText(username);
                    break;
                case CITY_REQUEST_CODE:
                    regionAddresslist = (ArrayList<RegionAddressEntity>) data.getExtras()
                            .getSerializable("address_list");
                    if (!ListUtils.isEmpty(regionAddresslist)) {
                        StringBuffer address = new StringBuffer();
                        address.append(regionAddresslist.get(0).area_name)
                                .append(regionAddresslist.get(1).area_name)
                                .append(regionAddresslist.get(2).area_name);
                        tv_region.setText(address);
                    }
                    break;
            }
        }
    }

    /**
     * 增加或保存地址
     */
    private void addAddress(String name, String phone, String address, String region) {
        if (type == 2) {
            httpPostRequest(AddressApi.getEditAddressUrl(),
                    getRequestParams(name, phone, address, region), AddressApi.API_GET_EDIT_ADDRESS);

        } else {
            httpPostRequest(AddressApi.getAddAddressUrl(),
                    getRequestParams(name, phone, address, region), AddressApi.API_GET_ADD_ADDRESS);
        }
    }

    /**
     * 增加或保存地址参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams(String name, String phone, String addressStr, String region) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("true_name", name);
        params.put("pro_id", regionAddresslist.get(0).area_id);
        params.put("city_id", regionAddresslist.get(1).area_id);
        params.put("area_id", regionAddresslist.get(2).area_id);
        params.put("area_info", region);
        params.put("address", addressStr);
        params.put("tel_phone", "");
        params.put("mob_phone", phone);
        if (type == 2) {
            params.put("address_id", address.address_id);
        }
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AddressApi.API_GET_ADD_ADDRESS:
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case AddressApi.API_GET_EDIT_ADDRESS:
                Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
                finish();
            default:
                break;
        }
    }

}

