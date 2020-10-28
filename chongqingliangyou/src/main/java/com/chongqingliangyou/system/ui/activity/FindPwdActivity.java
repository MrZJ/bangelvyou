package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.api.CodeApi;
import com.chongqingliangyou.system.entity.CodeEntity;
import com.chongqingliangyou.system.entity.FieldErrors;
import com.chongqingliangyou.system.util.checkMobile;

import java.util.HashMap;

/**
 * 找回密码，请求验证码页面
 *
 * @author Administrator
 *
 */
public class FindPwdActivity extends BaseActivity implements OnClickListener {

	private EditText editFindPwdPhone;
	private Button btnFindPwdGetCode;
	private String phoneTemp;
	private String phoneIMEI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pwd);
		initView();
		initListener();
	}

	@Override
	public void initListener() {
		super.initListener();
		initSideBarListener();
		btnFindPwdGetCode.setOnClickListener(this);
	}

	private void initView() {
		initTopView();
		setLeftBackButton();
		setTitle("找回密码");
		editFindPwdPhone = (EditText) findViewById(R.id.edit_find_pwd_phone);
		btnFindPwdGetCode = (Button) findViewById(R.id.btn_find_pwd_getCode);
	}

	@Override
	public void onResume() {
		super.onResume();
		setBtnClickable();
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		phoneIMEI = TelephonyMgr.getDeviceId();//获取手机唯一识别码
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_find_pwd_getCode:
				String phoneStr = editFindPwdPhone.getText().toString().trim();
				if(phoneStr != null && phoneStr.length() > 0){
					if (!checkMobile.isMobileNO(phoneStr)) {
						Toast.makeText(this, "您输入的手机号码不正确", Toast.LENGTH_SHORT).show();
						return;
					}
					btnFindPwdGetCode.setClickable(false);
					CodeEntity codeEntity=new CodeEntity();
					codeEntity.username=phoneStr;
					codeEntity.veriCode="123456";
					Intent intent = new Intent(this, InputRegisterCodeActivity.class);
					intent.putExtra("phone", phoneStr);
					intent.putExtra("code", codeEntity);
					startActivity(intent);
//					findPwd(phoneStr);
				} else {
					Toast.makeText(this, "请先输入手机号", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	/**
	 * 请求获取验证码
	 * @param phone
	 */
	private void findPwd(String phone){
		phoneTemp = phone;
		httpPostRequest(CodeApi.getCodeUrl(),
				getRequestParams(phone), CodeApi.API_GET_CODE);
	}

	/**
	 * 获取验证码参数
	 *
	 * @return
	 */
	private HashMap<String,String> getRequestParams(String phoneStr){
		HashMap<String,String> params = new HashMap<>();
		params.put("mobile", phoneStr);
		params.put("unique_id",phoneIMEI);
		return params;
	}

	@Override
	public void httpResponse(String json, int action){
		super.httpResponse(json, action);
		switch (action)
		{
			case CodeApi.API_GET_CODE:
				codeHander(json);
				break;
			default:
				break;
		}
	}

	@Override
	protected void httpError(FieldErrors error, int action) {
		super.httpError(error, action);
		setBtnClickable();
	}

	/**
	 * 处理验证码信息
	 * @param json
	 */
	private void codeHander(String json){
		setBtnClickable();
		CodeEntity codeEntity = JSON.parseObject(json, CodeEntity.class);
		if(codeEntity != null){
			codeEntity.username = phoneTemp;
			Intent intent = new Intent(this, InputRegisterCodeActivity.class);
			intent.putExtra("phone", phoneTemp);
			intent.putExtra("code", codeEntity);
			startActivity(intent);
		}
	}


	/**
	 * 设置按钮可以点击
	 */
	private void setBtnClickable() {
		btnFindPwdGetCode.setClickable(true);
	}
}
