package com.chongqingliangyou.system.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.api.RegisterApi;
import com.chongqingliangyou.system.entity.FieldErrors;
import com.chongqingliangyou.system.util.ConfigUtil;
import com.chongqingliangyou.system.util.MD5Util;
import com.chongqingliangyou.system.util.ToastUtil;

import java.util.HashMap;

/**
 * 修改密码页面
 * @author Administrator
 *
 */
public class ModifyPassWordActivity extends BaseActivity implements OnClickListener {


	private EditText etOldPwd;
	private EditText etNewPwd;
	private Button btnOK;
	private String newPwd;
	//密码错误次数
	private int errorCount;
	private final static int MSG_IS_FINISH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pas);
		initView();
		initListener();
	}

	@Override
	public void initListener() {
		super.initListener();
		initSideBarListener();
		btnOK.setOnClickListener(this);
	}

	private void initView() {
		initTopView();
		setLeftBackButton();
		setTitle("修改密码");
		etOldPwd = (EditText) findViewById(R.id.edit_setPassword_oldPassword);
		etNewPwd = (EditText) findViewById(R.id.edit_setPassword_newPassword);
		btnOK = (Button) findViewById(R.id.btn_setPassword_confirm);
	}

	@Override
	public void onResume() {
		super.onResume();
		setBtnClickable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_setPassword_confirm:
				String oldPwd = etOldPwd.getText().toString().trim();
				String newPwd = etNewPwd.getText().toString().trim();
				if(oldPwd == null || oldPwd.length() == 0){
					Toast.makeText(this, "请先输入原始密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if(newPwd == null || newPwd.length() == 0){
					Toast.makeText(this, "请先输入新密码", Toast.LENGTH_SHORT).show();
					return;
				}
				this.newPwd = newPwd;
				btnOK.setClickable(false);
				modifyPwd(oldPwd);
		}
	}

	/**
	 * 修改密码
	 * @param oldPwd
	 */
	private void modifyPwd(String oldPwd) {
		if (!comparePwd(oldPwd)) {
			ToastUtil.showToast("原密码错误!", this, ToastUtil.DELAY_SHORT);
			setBtnClickable();
			errorCount++;
			if(errorCount==3){
				ToastUtil.showToast("密码输错三次,即将关闭应用", this, ToastUtil.DELAY_SHORT);
				errorCount=0;
				mHandler.sendEmptyMessageDelayed(MSG_IS_FINISH, 2000);
			}
			return;
		}
		httpPostRequest(RegisterApi.getUpdatePwdUrl(), getRequestParams(), RegisterApi.API_UPDATE_PWD);
	}

	protected void processMessage(Message msg) {
		super.processMessage(msg);
		switch (msg.what) {
			case MSG_IS_FINISH:
				appManager.finishActivity();
				break;
		}
	}

	/**
	 * 对比密码是否一致
	 *
	 * @param oldPwd
	 */
	private boolean comparePwd(String oldPwd) {
		String beginPwdMD5 = ConfigUtil.loadConfig(this).passwordMD5;
		String oldPwdMD5 = MD5Util.getMD5Str(oldPwd);
		return beginPwdMD5.equals(oldPwdMD5);
	}

	/**
	 * 获取修改密码参数
	 *
	 * @return
	 */
	private HashMap<String,String> getRequestParams(){
		HashMap<String,String> params = new HashMap<>();
		params.put("key", configEntity.key);
		params.put("password", newPwd);
		return params;
	}

	@Override
	public void httpResponse(String json, int action){
		super.httpResponse(json, action);
		switch (action)
		{
			case RegisterApi.API_UPDATE_PWD:
				setBtnClickable();
				Toast.makeText(this,"密码修改成功,请重新登录!",Toast.LENGTH_SHORT).show();
				configEntity = ConfigUtil.loadConfig(this);
				configEntity.isLogin = false;
				configEntity.passwordMD5 = "";
				configEntity.key = "";
				configEntity.username = "";
				ConfigUtil.saveConfig(this, configEntity);
				IntentOtherActivity(LoginActivity.class);
				appManager.finishLoginOtherActivity();
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
	 * 设置按钮可以点击
	 */
	private void setBtnClickable() {
		btnOK.setClickable(true);
	}



}
