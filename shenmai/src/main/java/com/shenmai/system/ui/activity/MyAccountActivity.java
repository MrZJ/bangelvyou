package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.api.MemberApi;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.entity.MemberEntity;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.CacheImgUtil;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;

import java.io.File;
import java.util.HashMap;

/**
 * 我的详细信息界面
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_my_name_1, tv_my_name_2,tvUserPhone;
    private View rl_name, rl_mod_pwd, relUserPhone;
    private ImageView img_user_icon;
    private Button btn_back_login;
    private Bitmap icon;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String userPhone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initView();
        initListener();
        setData();
    }

    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(this);
        if (configEntity.isLogin) {
            getMember();
        }
        setData();
    }

    private void setData() {
        if (!StringUtil.isEmpty(configEntity.username) && configEntity.isLogin) {
            tv_my_name_1.setText(configEntity.username);
            tv_my_name_2.setText(configEntity.username);
        }
        File iconFile = new File(CacheImgUtil.user_icon);
        if (iconFile.exists() && configEntity.isLogin) {
            String path=iconFile.getAbsolutePath();
            icon = BitmapFactory.decodeFile(path);
            img_user_icon.setImageBitmap(icon);
        }else{
            img_user_icon.setImageResource(R.mipmap.mine_toux);
        }
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("我的信息");
        img_user_icon = (ImageView) findViewById(R.id.img_user_icon);
        tv_my_name_1 = (TextView) findViewById(R.id.tv_my_name_1);
        rl_name = findViewById(R.id.rl_name);
        tv_my_name_2 = (TextView) findViewById(R.id.tv_my_name_2);
        rl_mod_pwd = findViewById(R.id.rl_mod_pwd);
        btn_back_login = (Button) findViewById(R.id.btn_back_login);
        relUserPhone = findViewById(R.id.rel_my_account_phone);
        tvUserPhone = (TextView) findViewById(R.id.tv_my_accout_phone);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        img_user_icon.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_mod_pwd.setOnClickListener(this);
        btn_back_login.setOnClickListener(this);
        relUserPhone.setOnClickListener(this);
    }

    /**
     * 获取会员信息
     *
     * @param
     */
    private void getMember() {
        httpPostRequest(MemberApi.getMemberUrl(), getRequestParams(),
                MemberApi.API_MEMBER);
    }

    /**
     * 获取会员参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap();
        params.put("key", configEntity.key);
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case MemberApi.API_MEMBER:
                MemberHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case MemberApi.API_MEMBER:
                setData();
                break;
        }
    }

    /**
     * 处理会员信息
     *
     * @param json
     */
    private void MemberHander(String json) {
        MemberEntity memberEntity = JSON.parseObject(json, MemberEntity.class);
        if (memberEntity != null) {
            tv_my_name_1.setText(memberEntity.username);
            tv_my_name_2.setText(memberEntity.username);
            if(!StringUtil.isEmpty(memberEntity.mobile)){
                userPhone = memberEntity.mobile;
                tvUserPhone.setText(userPhone);
            }
            configEntity.username = memberEntity.username;
            ConfigUtil.saveConfig(this, configEntity);
            if(!StringUtil.isEmpty(memberEntity.user_logo)) {
                imageLoader.displayImage(memberEntity.user_logo, img_user_icon,
                        AsynImageUtil.getImageOptions(),
                        new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.user_icon));
            } else {
                img_user_icon.setImageResource(R.mipmap.mine_toux);
                CacheImgUtil.clearUserIcon();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_user_icon://头像
            case R.id.rl_name://昵称
                startActivity(new Intent(this, TouxiangActivity.class));
                break;
            case R.id.rl_mod_pwd://修改密码
                startActivity(new Intent(this,UpdatePasswordActivity.class));
                break;
            case R.id.rel_my_account_phone://绑定手机号
                if(StringUtil.isEmpty(userPhone)){
                    startActivity(new Intent(this,BingPhoneActivity.class));
                }else{
                    Toast.makeText(this,"手机号已绑定,如需修改请前往PC端",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back_login://退出登录
                ConfigUtil.clearConfig(this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                appManager.finishLoginOtherActivity();
                break;
        }
    }
}
