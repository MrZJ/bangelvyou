package com.shayangfupin.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shayangfupin.system.R;
import com.shayangfupin.system.api.MemberApi;
import com.shayangfupin.system.entity.FieldErrors;
import com.shayangfupin.system.entity.MemberEntity;
import com.shayangfupin.system.ui.activity.InfoAndHelperActivity;
import com.shayangfupin.system.ui.activity.LoginActivity;
import com.shayangfupin.system.ui.activity.MainActivity;
import com.shayangfupin.system.ui.activity.RevisePasswordActivity;
import com.shayangfupin.system.utlis.AsynImageUtil;
import com.shayangfupin.system.utlis.CacheImgUtil;
import com.shayangfupin.system.utlis.ConfigUtil;
import com.shayangfupin.system.utlis.ToastUtil;
import com.shayangfupin.system.wibget.CircleImageView;

import java.io.File;
import java.util.HashMap;

/**
 * 我的界面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View mView,rlUser;
    private View rl_mine_info,line_mine_info, rl_mine_help, line_mine_help,rl_my_modify_password, rl_about_us;
    private CircleImageView userIcon;
    private TextView tv_user, tv_user_register;
    private Bitmap icon;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Button btn_login_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        initListener();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        setTitle("我的");
        hideBackBtn();
//        setTopRightImg(R.mipmap.setting_icon, TopClickUtil.TOP_SET, R.mipmap.hint_icon, TopClickUtil.TOP_HINT);
        rlUser = mView.findViewById(R.id.rel_user);
        userIcon = (CircleImageView) mView.findViewById(R.id.img_user_icon);
        rl_mine_info = mView.findViewById(R.id.rl_mine_info);
        line_mine_info = mView.findViewById(R.id.lin_mine_info);
        rl_mine_help = mView.findViewById(R.id.rl_mine_help);
        line_mine_help = mView.findViewById(R.id.lin_mine_help);
        rl_my_modify_password = mView.findViewById(R.id.rl_my_modify_password);
        rl_about_us = mView.findViewById(R.id.rl_about_us);
        tv_user = (TextView) mView.findViewById(R.id.tv_user);
        /*tv_user_register = (TextView) mView.findViewById(R.id.tv_user_register);*/
        btn_login_out = (Button) mView.findViewById(R.id.btn_login_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    /**
     * 验证用户是否登录然后显示相关信息
     */
    private void setData() {
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (configEntity.isLogin) {
            btn_login_out.setVisibility(View.VISIBLE);
            File iconFile = new File(CacheImgUtil.user_icon);
            if (iconFile.exists() && configEntity.isLogin) {
                String path = iconFile.getAbsolutePath();
                icon = BitmapFactory.decodeFile(path);
                userIcon.setImageBitmap(icon);
            } else {
                userIcon.setImageResource(R.mipmap.mine_toux);
            }
            tv_user.setText(configEntity.username);
        } else {
            btn_login_out.setVisibility(View.GONE);
        }
        if(configEntity.usertype.equals("2")){
            rl_mine_info.setVisibility(View.VISIBLE);
            line_mine_info.setVisibility(View.VISIBLE);
            rl_mine_help.setVisibility(View.VISIBLE);
            line_mine_help.setVisibility(View.VISIBLE);
        } else {
            rl_mine_info.setVisibility(View.GONE);
            line_mine_info.setVisibility(View.GONE);
            rl_mine_help.setVisibility(View.GONE);
            line_mine_help.setVisibility(View.GONE);
        }
    }


    private void initListener() {
        initTopListener();
        userIcon.setOnClickListener(this);
        rl_mine_info.setOnClickListener(this);
        rl_mine_help.setOnClickListener(this);
        rl_my_modify_password.setOnClickListener(this);
        tv_user.setOnClickListener(this);
       /* tv_user_register.setOnClickListener(this);*/
        rl_about_us.setOnClickListener(this);
        btn_login_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent infoIntent = new Intent(getActivity(), InfoAndHelperActivity.class);
        switch (v.getId()) {
            /*case R.id.img_user_icon:
                Intent accountIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(accountIntent);
                break;*/
            case R.id.rl_mine_info:
                infoIntent.putExtra("type","个人信息");
                startActivity(infoIntent);
                break;
            case R.id.rl_mine_help:
                infoIntent.putExtra("type","我的帮扶人");
                startActivity(infoIntent);
                break;
            case R.id.rl_my_modify_password:
                if(configEntity.isLogin) {
                    startActivity(new Intent(getActivity(), RevisePasswordActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                    Intent loginAndRegisterIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            case R.id.tv_user:
                if (!configEntity.isLogin) {
                    Intent loginAndRegisterIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            /*case R.id.tv_user_register:
               *//* if (!configEntity.isLogin) {
                    Intent loginAndRegisterIntent = new Intent(getActivity(), RegisterUserActivity.class);
                    startActivity(loginAndRegisterIntent);
                }*//*
                break;*/
            case R.id.rl_about_us:
                infoIntent.putExtra("type","关于我们");
                startActivity(infoIntent);
                break;
            case R.id.btn_login_out:
                configEntity.isLogin = false;
                configEntity.key = "";
                configEntity.usertype = "";
                configEntity.username = "";
                ConfigUtil.saveConfig(getActivity(), configEntity);
                tv_user.setText("登录");
                ToastUtil.showToast("退出账户成功", getActivity(), ToastUtil.DELAY_SHORT);
                setData();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.checkPerson();
                if (mainActivity.homeFragment != null) {
                    mainActivity.homeFragment.checkPerson();
                }
                break;
        }
    }

    /**
     * 获取会员信息
     */
    private void getMemberInfo() {
        httpPostRequest(MemberApi.getMemberInfoUrl(), getRequestParams(),
                MemberApi.API_MEMBER);
    }

    /**
     * 获取会员参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
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
            default:
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case MemberApi.API_MEMBER:
                if (getActivity() == null) {
                    return;
                }
                configEntity.isLogin = false;
                ConfigUtil.saveConfig(getActivity(), configEntity);
                setData();
                break;
            default:
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
            tv_user.setText(memberEntity.username);
            if (getActivity() != null) {
                configEntity.username = memberEntity.username;
                ConfigUtil.saveConfig(getActivity(), configEntity);
            }
            if (!TextUtils.isEmpty(memberEntity.avator) && configEntity.isLogin) {
                imageLoader.displayImage(memberEntity.avator, userIcon,
                        AsynImageUtil.getImageOptions(),
                        new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.user_icon));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != icon) {
            icon.recycle();
            icon = null;
        }
    }

}
