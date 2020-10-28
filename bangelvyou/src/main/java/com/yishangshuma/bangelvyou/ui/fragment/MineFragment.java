package com.yishangshuma.bangelvyou.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.MemberApi;
import com.yishangshuma.bangelvyou.entity.FieldErrors;
import com.yishangshuma.bangelvyou.entity.MemberEntity;
import com.yishangshuma.bangelvyou.ui.activity.AccountActivity;
import com.yishangshuma.bangelvyou.ui.activity.AddressManageActivity;
import com.yishangshuma.bangelvyou.ui.activity.BrowsingHistoryActivity;
import com.yishangshuma.bangelvyou.ui.activity.CartActivity;
import com.yishangshuma.bangelvyou.ui.activity.CollectActivity;
import com.yishangshuma.bangelvyou.ui.activity.CouponActivity;
import com.yishangshuma.bangelvyou.ui.activity.LoginActivity;
import com.yishangshuma.bangelvyou.ui.activity.MyCommentActivity;
import com.yishangshuma.bangelvyou.ui.activity.MyPointActivity;
import com.yishangshuma.bangelvyou.ui.activity.OrderListActivity;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;
import com.yishangshuma.bangelvyou.wibget.CircleImageView;

import java.io.File;
import java.util.HashMap;

/**
 * 我的界面
 * Created by Administrator on 2016/1/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    private View cart, order ,collect, coupon , point , comment, history, address;
    private CircleImageView userIcon;
    private TextView userName, userLevel, userUnLogin;
    private Bitmap icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        intiView();
        intiListener();
        return mView;
    }

    private void intiView(){
        initTopView(mView);
        setLogoShow(R.mipmap.mine_set, TopClickUtil.TOP_SET);
        setTitle("我的");
//        setTopRightImg(R.mipmap.mine_notice, TopClickUtil.TOP_MES);
        userIcon = (CircleImageView) mView.findViewById(R.id.img_user_icon);
        userName = (TextView) mView.findViewById(R.id.tv_user_name);
        userLevel = (TextView) mView.findViewById(R.id.tv_user_level_num);
        userUnLogin = (TextView) mView.findViewById(R.id.tv_user_unlogin);
        cart = mView.findViewById(R.id.rl_my_cart);
        order = mView.findViewById(R.id.rl_my_order);
        collect = mView.findViewById(R.id.rl_my_collect);
        coupon = mView.findViewById(R.id.rl_my_coupon);
        point = mView.findViewById(R.id.rl_my_point);
        comment = mView.findViewById(R.id.rl_my_comment);
        history = mView.findViewById(R.id.rl_my_history);
        address = mView.findViewById(R.id.rl_my_address);
    }

    @Override
    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (configEntity.isLogin) {
            getMemberInfo();
        }
        setData();
    }

    private void setData(){
        if(configEntity.isLogin){
            userName.setVisibility(View.VISIBLE);
            userLevel.setVisibility(View.VISIBLE);
            userUnLogin.setVisibility(View.GONE);
            File iconFile = new File(CacheImgUtil.user_icon);
            if (iconFile.exists()) {
                String path=iconFile.getAbsolutePath();
                icon = BitmapFactory.decodeFile(path);
                userIcon.setImageBitmap(icon);
            }else{
                userIcon.setImageResource(R.mipmap.touxiang);
            }
        }else{
            userName.setVisibility(View.GONE);
            userLevel.setVisibility(View.GONE);
            userUnLogin.setVisibility(View.VISIBLE);
            userIcon.setImageResource(R.mipmap.touxiang);
            String unloginTip = "您还没有登录,请先登录";
            SpannableStringBuilder style = new SpannableStringBuilder(unloginTip);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB933")), unloginTip.length()-2, unloginTip.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            userUnLogin.setText(style);
        }
    }

    private void intiListener(){
        initTopListener();
        userIcon.setOnClickListener(this);
        userUnLogin.setOnClickListener(this);
        cart.setOnClickListener(this);
        order.setOnClickListener(this);
        collect.setOnClickListener(this);
        coupon.setOnClickListener(this);
        point.setOnClickListener(this);
        comment.setOnClickListener(this);
        history.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(null != getActivity()){
            if(!configEntity.isLogin ){
                if(v.getId() != R.id.tv_user_unlogin){
                    Toast.makeText(getActivity(), "请先登录",Toast.LENGTH_SHORT).show();
                }
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                return;
            }
        }else{
            return;
        }
        switch (v.getId()){
            case R.id.img_user_icon:
                Intent accountIntent = new Intent(getActivity(), AccountActivity.class);
                startActivity(accountIntent);
                break;
            case R.id.rl_my_cart:
                Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(cartIntent);
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
                Intent commentIntent = new Intent(getActivity(), MyCommentActivity.class);
                startActivity(commentIntent);
                break;
            case R.id.rl_my_history:
                Intent historyIntent = new Intent(getActivity(), BrowsingHistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.rl_my_address:
                Intent addressIntent = new Intent(getActivity(), AddressManageActivity.class);
                startActivity(addressIntent);
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
    private HashMap<String,String> getRequestParams() {
        HashMap<String,String> params = new HashMap<>();
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
        if(memberEntity != null){
            userName.setText(memberEntity.username);
            userLevel.setText("Lv"+memberEntity.memberlevel);
            if (getActivity() != null) {
                configEntity.username = memberEntity.username;
                configEntity.level = memberEntity.memberlevel;
                configEntity.point = memberEntity.point;
                ConfigUtil.saveConfig(getActivity(), configEntity);
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
