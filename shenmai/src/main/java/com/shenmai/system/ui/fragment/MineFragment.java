package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.shenmai.system.R;
import com.shenmai.system.ui.activity.BankInfoActivity;
import com.shenmai.system.ui.activity.CollectActivity;
import com.shenmai.system.ui.activity.ManagerAddressActivity;
import com.shenmai.system.ui.activity.MineWebActivity;
import com.shenmai.system.ui.activity.MyAccountActivity;
import com.shenmai.system.ui.activity.OrderWebActivity;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;

/**
 * 我的界面
 *
 * Created by Administrator on 2016/1/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View mView, rl_account, rl_withdraw, rl_shop, rl_my_address,
            rl_my_collect, rl_help, rl_message, rl_about_us, rl_grade_us,rl_my_order;
    private TextView tv_my_name, tv_is_auth, tv_mass_num, tv_app_version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        intiView();
        intiListener();
        return mView;
    }

    private void intiView() {
        initTopView(mView);
        setTitle("我的");
        hideBackBtn();
        rl_account = mView.findViewById(R.id.rl_account);
        rl_withdraw = mView.findViewById(R.id.rl_withdraw);
        rl_shop = mView.findViewById(R.id.rl_shop);
        rl_my_order = mView.findViewById(R.id.rl_my_order);
        rl_my_address = mView.findViewById(R.id.rl_my_address);
        rl_my_collect = mView.findViewById(R.id.rl_my_collect);
        rl_help = mView.findViewById(R.id.rl_help);
        rl_message = mView.findViewById(R.id.rl_message);
        rl_about_us = mView.findViewById(R.id.rl_about_us);
        rl_grade_us = mView.findViewById(R.id.rl_grade_us);
        tv_my_name = (TextView) mView.findViewById(R.id.tv_my_name);//用户昵称
        tv_is_auth = (TextView) mView.findViewById(R.id.tv_is_auth);//是否认证
        tv_mass_num = (TextView) mView.findViewById(R.id.tv_mass_num);//消息个数
        tv_app_version = (TextView) mView.findViewById(R.id.tv_mine_appversion);
        if(!StringUtil.isEmpty(getAppVersionName())){
            tv_app_version.setText(getAppVersionName());
        }
        tv_my_name.setText(configEntity.username);
    }

    private void intiListener() {
        rl_account.setOnClickListener(this);
        rl_withdraw.setOnClickListener(this);
        rl_shop.setOnClickListener(this);
        rl_my_order.setOnClickListener(this);
        rl_my_address.setOnClickListener(this);
        rl_my_collect.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        rl_grade_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(getActivity() == null){
            return;
        }
        Intent mineIntent = new Intent(getActivity(), MineWebActivity.class);
        switch (v.getId()) {
            case R.id.rl_account://账号信息
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.rl_withdraw://提现信息
                startActivity(new Intent(getActivity(), BankInfoActivity.class));
                break;
            case R.id.rl_shop://店铺信息

                break;
            case R.id.rl_my_order://我的订单
                startActivity(new Intent(getActivity(), OrderWebActivity.class));
                break;
            case R.id.rl_my_address://我的收货地址
                Intent addressIntent = new Intent(getActivity(),
                        ManagerAddressActivity.class);
                addressIntent.putExtra("addressId", "my");
                startActivity(addressIntent);
                break;
            case R.id.rl_my_collect://我的收藏
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.rl_help://帮助
                mineIntent.putExtra("title","帮助中心");
                mineIntent.putExtra("url",ConfigUtil.HTTP_MINE_HELP);
                startActivity(mineIntent);
                break;
            case R.id.rl_message://消息中心
                mineIntent.putExtra("title","消息中心");
                mineIntent.putExtra("url",ConfigUtil.HTTP_MINE_NOTICE);
                startActivity(mineIntent);
                break;
            case R.id.rl_about_us://关于我们
                mineIntent.putExtra("title","关于我们");
                mineIntent.putExtra("url",ConfigUtil.HTTP_MINE_ABOUTS);
                startActivity(mineIntent);
                break;
            case R.id.rl_grade_us://给我们评分
                BDAutoUpdateSDK.cpUpdateCheck(getActivity(), new MyCPCheckUpdateCallback());
                break;
        }
    }

    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
            if(infoForInstall == null && info == null){
                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
            } else {
                BDAutoUpdateSDK.uiUpdateAction(getActivity(), new MyUICheckUpdateCallback());
            }
        }

    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {

        @Override
        public void onCheckComplete() {

        }

    }

    /**
     * 获取软件当前版本信息
     */
    private String getAppVersionName() {
        try {
            String pkName = getActivity().getPackageName();
            String versionName = getActivity().getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
