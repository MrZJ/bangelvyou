package com.zycreview.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zycreview.system.R;
import com.zycreview.system.adapter.UrlListAdapter;
import com.zycreview.system.api.MemberApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.MemberEntity;
import com.zycreview.system.entity.UrlListBean;
import com.zycreview.system.ui.activity.BccScanActivity;
import com.zycreview.system.ui.activity.InspectManagerActivity;
import com.zycreview.system.ui.activity.InstockManagerActivity;
import com.zycreview.system.ui.activity.LoginActivity;
import com.zycreview.system.ui.activity.MineWebActivity;
import com.zycreview.system.ui.activity.OutstockManagerActivity;
import com.zycreview.system.ui.activity.PlantManageActivity;
import com.zycreview.system.ui.activity.RegisterUserActivity;
import com.zycreview.system.ui.activity.RevisePasswordActivity;
import com.zycreview.system.utils.AsynImageUtil;
import com.zycreview.system.utils.CacheImgUtil;
import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.wibget.CircleImageView;

import java.io.File;
import java.util.HashMap;

/**
 * 我的界面
 * Created by Administrator on 2016/1/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View mView, loginView, unLoginView, plantType;
    private View tab_plant_manage, tab_checkout_manage, tab_storage_in, tab_tab_storage_out, rl_firm_info, rl_my_modify_password, rl_about_us, rl_scan, rl_login_out;
    private CircleImageView userIcon;
    private TextView tab_plant_manage_text, tab_checkout_manage_text, tab_storage_in_text, tab_tab_storage_out_text;
    private TextView tv_user_login, tv_user_register, tv_user_type, tv_user_name;
    private ImageView tab_plant_manage_img, tab_checkout_manage_img, tab_storage_in_img, tab_tab_storage_out_img;
    private Bitmap icon;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ListView listview;
    //Tab选项卡的文字
    private String mTextviewArray[] = {"种植管理", "检验管理", "入库管理", "库存管理"};
    private int mImageviewArray[] = {R.mipmap.icon_m_02, R.mipmap.icon_m_03, R.mipmap.icon_m_04, R.mipmap.icon_m_05};

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
        loginView = mView.findViewById(R.id.rel_user_login);
        unLoginView = mView.findViewById(R.id.rel_user_unlogin);
        userIcon = (CircleImageView) mView.findViewById(R.id.img_user_icon_login);
        tv_user_type = (TextView) mView.findViewById(R.id.tv_user_type);
        tv_user_name = (TextView) mView.findViewById(R.id.tv_user_name);
        plantType = mView.findViewById(R.id.lin_plant_type);//种植企业才有
        tab_plant_manage = mView.findViewById(R.id.tab_plant_manage);
        tab_plant_manage_text = (TextView) tab_plant_manage.findViewById(R.id.textview);
        tab_plant_manage_img = (ImageView) tab_plant_manage.findViewById(R.id.imageview);
        tab_checkout_manage = mView.findViewById(R.id.tab_checkout_manage);
        tab_checkout_manage_text = (TextView) tab_checkout_manage.findViewById(R.id.textview);
        tab_checkout_manage_img = (ImageView) tab_checkout_manage.findViewById(R.id.imageview);
        tab_storage_in = mView.findViewById(R.id.tab_storage_in);
        tab_storage_in_text = (TextView) tab_storage_in.findViewById(R.id.textview);
        tab_storage_in_img = (ImageView) tab_storage_in.findViewById(R.id.imageview);
        tab_tab_storage_out = mView.findViewById(R.id.tab_tab_storage_out);
        tab_tab_storage_out_text = (TextView) tab_tab_storage_out.findViewById(R.id.textview);
        tab_tab_storage_out_img = (ImageView) tab_tab_storage_out.findViewById(R.id.imageview);
        setTab();
        rl_firm_info = mView.findViewById(R.id.rl_firm_info);
        rl_my_modify_password = mView.findViewById(R.id.rl_my_modify_password);
        rl_about_us = mView.findViewById(R.id.rl_about_us);
        rl_scan = mView.findViewById(R.id.rl_scan);
//        rl_feedback = mView.findViewById(R.id.rl_feedback);
        rl_login_out = mView.findViewById(R.id.rl_login_out);
        tv_user_login = (TextView) mView.findViewById(R.id.tv_user_login);
        tv_user_register = (TextView) mView.findViewById(R.id.tv_user_register);
        listview = (ListView) mView.findViewById(R.id.listview);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        if (!StringUtil.isEmpty(configEntity.key)) {
//            getMemberInfo();
        }
    }

    private void setData() {
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (configEntity.isLogin) {
            loginView.setVisibility(View.VISIBLE);
            unLoginView.setVisibility(View.GONE);
            File iconFile = new File(CacheImgUtil.user_icon);
            if (iconFile.exists() && configEntity.isLogin) {
                String path = iconFile.getAbsolutePath();
                icon = BitmapFactory.decodeFile(path);
                userIcon.setImageBitmap(icon);
            } else {
                userIcon.setImageResource(R.mipmap.mine_toux);
            }
            tv_user_type.setText(configEntity.usertype);
            tv_user_name.setText(configEntity.username);
            rl_login_out.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(new UrlListAdapter(getActivity(), configEntity.urlList));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        UrlListBean bean = configEntity.urlList.get(position);
                        Intent detailIntent = new Intent(getActivity(), MineWebActivity.class);
                        detailIntent.putExtra("title", bean.name);
                        detailIntent.putExtra("url", bean.url + "&mobileLogin=true&key=" + configEntity.key);
                        startActivity(detailIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//            if(configEntity.usertype.contains("种植企业")) {
//                plantType.setVisibility(View.VISIBLE);
//            }else{
//                plantType.setVisibility(View.GONE);
//            }
        } else {
            loginView.setVisibility(View.GONE);
            unLoginView.setVisibility(View.VISIBLE);
            rl_login_out.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
//            plantType.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置Tab
     */
    private void setTab() {
        tab_plant_manage_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_checkout_manage_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_storage_in_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_tab_storage_out_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_plant_manage_text.setText(mTextviewArray[0]);
        tab_checkout_manage_text.setText(mTextviewArray[1]);
        tab_storage_in_text.setText(mTextviewArray[2]);
        tab_tab_storage_out_text.setText(mTextviewArray[3]);
        tab_plant_manage_img.setImageResource(mImageviewArray[0]);
        tab_checkout_manage_img.setImageResource(mImageviewArray[1]);
        tab_storage_in_img.setImageResource(mImageviewArray[2]);
        tab_tab_storage_out_img.setImageResource(mImageviewArray[3]);
    }

    private void initListener() {
        initTopListener();
        userIcon.setOnClickListener(this);
        tab_plant_manage.setOnClickListener(this);
        tab_checkout_manage.setOnClickListener(this);
        tab_storage_in.setOnClickListener(this);
        tab_tab_storage_out.setOnClickListener(this);
        rl_firm_info.setOnClickListener(this);
        rl_my_modify_password.setOnClickListener(this);
        tv_user_login.setOnClickListener(this);
        tv_user_register.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
//        rl_feedback.setOnClickListener(this);
        rl_login_out.setOnClickListener(this);
        rl_scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.img_user_icon:
                /*Intent accountIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(accountIntent);
                break;*/
            case R.id.tab_plant_manage:
                if (configEntity.isLogin) {
                    Intent plantIntent = new Intent(getActivity(), PlantManageActivity.class);
                    startActivity(plantIntent);
                } else {
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_checkout_manage:
                if (configEntity.isLogin) {
                    startActivity(new Intent(getActivity(), InspectManagerActivity.class));
                } else {
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_storage_in:
                if (configEntity.isLogin) {
                    startActivity(new Intent(getActivity(), InstockManagerActivity.class));
                } else {
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_tab_storage_out:
                if (configEntity.isLogin) {
                    startActivity(new Intent(getActivity(), OutstockManagerActivity.class));
                } else {
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_firm_info:
                if (configEntity.isLogin) {
                    Intent detailIntent = new Intent(getActivity(), MineWebActivity.class);
                    detailIntent.putExtra("title", "企业信息");
                    detailIntent.putExtra("url", ConfigUtil.HTTP_URL_ENTP_INFO + configEntity.entpId.trim());
                    startActivity(detailIntent);
                } else {
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_my_modify_password:
                if (configEntity.isLogin) {
                    startActivity(new Intent(getActivity(), RevisePasswordActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent loginAndRegisterIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            case R.id.tv_user_login:
                if (!configEntity.isLogin) {
                    Intent loginAndRegisterIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            case R.id.tv_user_register:
                if (!configEntity.isLogin) {
                    Intent loginAndRegisterIntent = new Intent(getActivity(), RegisterUserActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            case R.id.rl_about_us:
                Intent detailIntent = new Intent(getActivity(), MineWebActivity.class);
                detailIntent.putExtra("title", "关于我们");
                detailIntent.putExtra("url", ConfigUtil.HTTP_URL_ABOUT_US);
                startActivity(detailIntent);
                break;
            case R.id.rl_scan:
                if (configEntity.isLogin) {
                    //判断设备是否是扫描枪
                    String deviceModel = Build.MANUFACTURER;
                    if (deviceModel.contains("UBX")) {
                        startActivity(new Intent(getActivity(), BccScanActivity.class));
                    } else {
                        Toast.makeText(getActivity(), "当前设备不是扫码枪", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                    Intent loginAndRegisterIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginAndRegisterIntent);
                }
                break;
            case R.id.rl_login_out:
                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                configEntity.isLogin = false;
                configEntity.key = "";
                ConfigUtil.saveConfig(getActivity(), configEntity);
                setData();
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
            tv_user_login.setText(memberEntity.username);
            if (getActivity() != null) {
                configEntity.username = memberEntity.username;
                /*configEntity.level = memberEntity.memberlevel;
                configEntity.point = memberEntity.point;*/
                ConfigUtil.saveConfig(getActivity(), configEntity);
            }
            /*userLevel.setText("Lv" + memberEntity.memberlevel);*/
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
