package com.yishangshuma.bangelvyou.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.AddressApi;
import com.yishangshuma.bangelvyou.entity.AddressEntity;
import com.yishangshuma.bangelvyou.entity.RegionAddressEntity;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.util.checkMobile;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新增地址界面
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener{

    private static final int CITY_REQUEST_CODE = 1;
    private View rl_user, rl_next;
    private EditText edt_name, edt_phone, edt_address;
    private TextView tv_region;
    private Button btn_save;
    private List<RegionAddressEntity> regionAddresslist;
    private AddressEntity address;
    private int type;//1：添加  2：编辑修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        type = getIntent().getExtras().getInt("type", 1);
        if(type == 2){
            address = (AddressEntity) getIntent().getExtras().getSerializable("address");
        }
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("新增收货人");
        rl_user = (View) findViewById(R.id.rel_edit_address_user);
        rl_next = (View) findViewById(R.id.rl_next);
        edt_name = (EditText) findViewById(R.id.edit_address_consignee_name);
        edt_phone = (EditText) findViewById(R.id.edit_address_consignee_phone);
        edt_address = (EditText) findViewById(R.id.edit_address_consignee_detailads);
        tv_region = (TextView) findViewById(R.id.tv_address_consignee_region);
        btn_save = (Button) findViewById(R.id.btn_edit_address_save);
        if(type == 2){
//            regionAddresslist = new ArrayList<RegionAddressEntity>();
//            for(int i = 0; i < 3; i++){
//                RegionAddressEntity addressEntity = new RegionAddressEntity();
//                if(i == 1){
//                    addressEntity.area_id = address.city_id;
//                } else if(i == 2){
//                    addressEntity.area_id = address.area_id;
//                }
//                regionAddresslist.add(addressEntity);
//            }
            edt_name.setText(address.true_name);
            edt_phone.setText(address.mob_phone);
            tv_region.setText(address.area_info);
            edt_address.setText(address.address);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        btn_save.setOnClickListener(this);
        rl_user.setOnClickListener(this);
        tv_region.setOnClickListener(this);
        rl_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_address_save://保存
                String name = edt_name.getText().toString().trim();
                String phone = edt_phone.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String region = tv_region.getText().toString().trim();
                if(name == null || "".equals(name)){
                    Toast.makeText(this, "请先输入收货人名字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone == null || "".equals(phone)){
                    Toast.makeText(this, "请先输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkMobile.isMobileNO(phone)) {
                    Toast.makeText(this, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(address == null || "".equals(address)){
                    Toast.makeText(this, "请先输入详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(region == null || "".equals(region)){
                    Toast.makeText(this, "请先输入所属地区", Toast.LENGTH_SHORT).show();
                    return;
                }
                addAddress(name, phone, address, region);
                break;
            case R.id.rel_edit_address_user://选择联系人
                startActivityForResult(new Intent(
                        Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
                break;
            case R.id.tv_address_consignee_region://选择地址
                startActivityForResult(new Intent(EditAddressActivity.this,
                        SelectAddressActivity.class), CITY_REQUEST_CODE);
                break;
            case R.id.rl_next://选择地址
                startActivityForResult(new Intent(EditAddressActivity.this,
                        SelectAddressActivity.class), CITY_REQUEST_CODE);
                break;
          }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case 0:
                    ContentResolver reContentResolverol = getContentResolver();
                    Uri contactData = data.getData();
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
                    if(!ListUtils.isEmpty(regionAddresslist)){
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
     * 增加地址
     */
    private void addAddress(String name, String phone, String address, String region){
        if(type == 2){
            httpPostRequest(AddressApi.getEditAddressUrl(),
                    getRequestParams(name, phone, address, region), AddressApi.API_GET_EDIT_ADDRESS);

        } else {
            httpPostRequest(AddressApi.getAddAddressUrl(),
                    getRequestParams(name, phone, address, region), AddressApi.API_GET_ADD_ADDRESS);
        }
    }

    /**
     * 增加地址
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(String name, String phone, String addressStr, String region){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("true_name", name);
        if(null != regionAddresslist){
            if(null != regionAddresslist.get(0).area_id){
                params.put("pro_id", regionAddresslist.get(0).area_id);
            }
            if(null != regionAddresslist.get(1).area_id){
                params.put("city_id", regionAddresslist.get(1).area_id);
            }
            if(null != regionAddresslist.get(2).area_id){
                params.put("area_id", regionAddresslist.get(2).area_id);
            }
        }
        params.put("area_info", region);
        params.put("address", addressStr);
        params.put("tel_phone", "");
        params.put("mob_phone", phone);
        if(type == 2){
            params.put("address_id", address.address_id);
        }
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        String flag = JSON.parseObject(json).getString("flag").toString();
        if(null == flag && "1".equals(flag)){
            return;
        }
        switch (action){
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
