package com.basulvyou.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.MemberApi;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.entity.MemberEntity;
import com.basulvyou.system.ui.activity.AccountActivity;
import com.basulvyou.system.ui.activity.AddressManageActivity;
import com.basulvyou.system.ui.activity.BrowsingHistoryActivity;
import com.basulvyou.system.ui.activity.CartActivity;
import com.basulvyou.system.ui.activity.CollectActivity;
import com.basulvyou.system.ui.activity.CouponActivity;
import com.basulvyou.system.ui.activity.LoginActivity;
import com.basulvyou.system.ui.activity.MyCarpoolingActivity;
import com.basulvyou.system.ui.activity.MyCreditActivity;
import com.basulvyou.system.ui.activity.MyEvaluateActivity;
import com.basulvyou.system.ui.activity.MyPointActivity;
import com.basulvyou.system.ui.activity.OrderListActivity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.wibget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;

/**
 * 我的界面
 * Created by Administrator on 2016/1/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private View cart, order, collect, coupon, point, comment, history, address, carpool, logistic, rl_my_credit;
    private CircleImageView userIcon;
    private TextView userName, userLevel, userUnLogin;
    private Bitmap icon;
    private ImageLoader imageLoader = ImageLoader.getInstance();

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
        setLogoShow(R.mipmap.mine_set, TopClickUtil.TOP_SET);
        setTitle("我的");
//        setTopRightImg(R.mipmap.mine_notice, TopClickUtil.TOP_MES);
        userIcon = (CircleImageView) mView.findViewById(R.id.img_user_icon);
        userName = (TextView) mView.findViewById(R.id.tv_user_name);
        userLevel = (TextView) mView.findViewById(R.id.tv_user_level_num);
        userUnLogin = (TextView) mView.findViewById(R.id.tv_user_unlogin);
//        cart = mView.findViewById(R.id.rl_my_cart);
//        order = mView.findViewById(R.id.rl_my_order);
        collect = mView.findViewById(R.id.rl_my_collect);
        coupon = mView.findViewById(R.id.rl_my_coupon);
        point = mView.findViewById(R.id.rl_my_point);
        comment = mView.findViewById(R.id.rl_my_comment);
        history = mView.findViewById(R.id.rl_my_history);
        address = mView.findViewById(R.id.rl_my_address);
        carpool = mView.findViewById(R.id.rl_my_carpool);
        logistic = mView.findViewById(R.id.rl_my_logistic);
        rl_my_credit = mView.findViewById(R.id.rl_my_credit);

    }

    @Override
    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (!StringUtil.isEmpty(configEntity.key)) {
            getMemberInfo();
        }
        setData();
    }

    private void setData() {
        if (configEntity.isLogin) {
            userName.setVisibility(View.VISIBLE);
            userLevel.setVisibility(View.VISIBLE);
            userUnLogin.setVisibility(View.GONE);
            if (configEntity.username != null) {
                userName.setText(configEntity.username);
            } else {
                userName.setText("");
            }
            userLevel.setText(configEntity.level);
            File iconFile = new File(CacheImgUtil.user_icon);
            if (iconFile.exists() && configEntity.isLogin) {
                String path = iconFile.getAbsolutePath();
                icon = BitmapFactory.decodeFile(path);
                userIcon.setImageBitmap(icon);
            } else {
                userIcon.setImageResource(R.mipmap.mine_toux);
            }
        } else {
            userName.setVisibility(View.GONE);
            userLevel.setVisibility(View.GONE);
            userUnLogin.setVisibility(View.VISIBLE);
            userIcon.setImageResource(R.mipmap.mine_toux);
            String unloginTip = "您还没有登录,请先登录";
            SpannableStringBuilder style = new SpannableStringBuilder(unloginTip);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB933")), unloginTip.length() - 2, unloginTip.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            userUnLogin.setText(style);
        }
    }

    private void intiListener() {
        initTopListener();
        userIcon.setOnClickListener(this);
        userUnLogin.setOnClickListener(this);
//        cart.setOnClickListener(this);
//        order.setOnClickListener(this);
        collect.setOnClickListener(this);
        coupon.setOnClickListener(this);
        point.setOnClickListener(this);
        comment.setOnClickListener(this);
        history.setOnClickListener(this);
        address.setOnClickListener(this);
        carpool.setOnClickListener(this);
        logistic.setOnClickListener(this);
        rl_my_credit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != getActivity()) {
            if (!configEntity.isLogin) {
                if (v.getId() != R.id.tv_user_unlogin) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                return;
            }
        } else {
            return;
        }
        switch (v.getId()) {
            case R.id.img_user_icon:
                Intent accountIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(accountIntent);
                break;
            case R.id.rl_my_cart:
                Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(cartIntent);
                /*Intent alarmIntent = new Intent(getActivity(), AlarmRescueActivity.class);
                startActivity(alarmIntent);*/
                /*Intent sendIntent = new Intent(getActivity(), SendCarpoolingActivity.class);
                startActivity(sendIntent);*/
                /*Intent carIntent = new Intent(getActivity(), CarpoolingListActivity.class);
                startActivity(carIntent);*/
                break;
            case R.id.rl_my_order:
                Intent orderIntent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.rl_my_collect:
                Intent collectIntent = new Intent(getActivity(), CollectActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.rl_my_coupon:
                Intent couponIntent = new Intent(getActivity(), CouponActivity.class);
                startActivity(couponIntent);
                break;
            case R.id.rl_my_point:
                Intent pointIntent = new Intent(getActivity(), MyPointActivity.class);
                startActivity(pointIntent);
                break;
            case R.id.rl_my_comment:
                Intent intent = new Intent(getActivity(), MyEvaluateActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_history:
                Intent historyIntent = new Intent(getActivity(), BrowsingHistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.rl_my_address:
                Intent addressIntent = new Intent(getActivity(), AddressManageActivity.class);
                startActivity(addressIntent);
                break;
            case R.id.rl_my_carpool:
                Intent carpoolIntent = new Intent(getActivity(), MyCarpoolingActivity.class);
                carpoolIntent.putExtra("type", "101");
                startActivity(carpoolIntent);
                break;
            case R.id.rl_my_logistic:
                Intent logisticIntent = new Intent(getActivity(), MyCarpoolingActivity.class);
                logisticIntent.putExtra("type", "104");
                startActivity(logisticIntent);
                break;
            case R.id.rl_my_credit:
                Intent intent1 = new Intent(getActivity(), MyCreditActivity.class);
                startActivity(intent1);
                break;
            default:
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
                /*configEntity.isLogin = false;
                ConfigUtil.saveConfig(getActivity(), configEntity);*/
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
            if (getActivity() != null) {
                configEntity.username = memberEntity.username;
                configEntity.level = memberEntity.memberlevel;
                configEntity.point = memberEntity.point;
                configEntity.mobile = memberEntity.mobile;
                configEntity.birthday = memberEntity.birthday;
                configEntity.sex = memberEntity.sex;
                configEntity.credit_score = memberEntity.credit_score;
                configEntity.real_name = memberEntity.real_name;
                ConfigUtil.saveConfig(getActivity(), configEntity);
            }
            userLevel.setText(memberEntity.memberlevel);
            if (configEntity.username != null) {
                userName.setText(configEntity.username);
            } else {
                userName.setText("");
            }
            if (configEntity.isLogin) {
                if (!TextUtils.isEmpty(memberEntity.avator)) {
                    imageLoader.displayImage(memberEntity.avator, userIcon,
                            AsynImageUtil.getImageOptions(R.mipmap.mine_toux),
                            null);
                } else {
                    userIcon.setImageResource(R.mipmap.mine_toux);
                }
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
