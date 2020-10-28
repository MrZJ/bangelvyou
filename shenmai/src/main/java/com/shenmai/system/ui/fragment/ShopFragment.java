package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.adapter.MyShopGridAdapter;
import com.shenmai.system.api.MemberApi;
import com.shenmai.system.entity.MemberEntity;
import com.shenmai.system.entity.ShopTypes;
import com.shenmai.system.ui.activity.BaseActivity;
import com.shenmai.system.ui.activity.MainActivity;
import com.shenmai.system.ui.activity.MyAccountActivity;
import com.shenmai.system.ui.activity.OrderWebActivity;
import com.shenmai.system.ui.activity.ShopSeeWebActivity;
import com.shenmai.system.ui.activity.ShopWebActivity;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.CacheImgUtil;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.widget.CircleImageView;
import com.shenmai.system.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的小店界面
 */
public class ShopFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener{

    private View mView,llShopFrozenMoney,llShopWithdrawMoney;
    private TextView tvShopFrozenMoney, tvShopWithdrawMoney, tvGoStore;
    private CircleImageView userImg;
    private MyGridView gridItem;
    private ArrayList<ShopTypes> list = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Bitmap icon;
    private ShopTypes type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shop, container, false);
        initView();
        initListener();
        return mView;
    }

    private void initView(){
        userImg = (CircleImageView) mView.findViewById(R.id.img_shop_usericon);
        gridItem = (MyGridView) mView.findViewById(R.id.grid_shop_item);
        llShopFrozenMoney = mView.findViewById(R.id.ll_shop_frozen_money);
        llShopWithdrawMoney = mView.findViewById(R.id.ll_shop_withdraw_money);
        tvShopFrozenMoney = (TextView) mView.findViewById(R.id.tv_shop_frozen_money);
        tvShopWithdrawMoney = (TextView) mView.findViewById(R.id.tv_shop_withdraw_money);
        tvGoStore = (TextView) mView.findViewById(R.id.tv_shop_gostore);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = new ArrayList<>();
        type = new ShopTypes( "营收", R.mipmap.shop_money);
        list.add(type);
        type = new ShopTypes( "订单", R.mipmap.shop_order);
        list.add(type);
        type = new ShopTypes( "微学堂", R.mipmap.shop_school);
        list.add(type);
        type = new ShopTypes( "二级", R.mipmap.shop_level);
        list.add(type);
        type = new ShopTypes( "分享", R.mipmap.shop_share);
        list.add(type);
        type = new ShopTypes( "店铺预览", R.mipmap.shop_view);
        list.add(type);
        MyShopGridAdapter myShopGridAdapter = new MyShopGridAdapter(getActivity(), list);
        gridItem.setAdapter(myShopGridAdapter);
        getMember();
    }

    private void initListener(){
        gridItem.setOnItemClickListener(this);
        userImg.setOnClickListener(this);
        llShopFrozenMoney.setOnClickListener(this);
        llShopWithdrawMoney.setOnClickListener(this);
        tvGoStore.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(getActivity() == null){
            return;
        }
        Intent shopWenIntnet = new Intent(getActivity(), ShopWebActivity.class);
        switch (i) {
            case 1:
                startActivity(new Intent(getActivity(), OrderWebActivity.class));
                break;
            case 0:
                shopWenIntnet.putExtra("title", "营收");
                shopWenIntnet.putExtra("url", ConfigUtil.HTTP_SHOP_MENOY);
                break;
            case 2:
                shopWenIntnet.putExtra("title", "微学堂");
                shopWenIntnet.putExtra("url", ConfigUtil.HTTP_SHOP_LEARN);
                break;
            case 3:
                shopWenIntnet.putExtra("title", "我的二级");
                shopWenIntnet.putExtra("url", ConfigUtil.HTTP_SHOP_LEVEL);
                break;
            case 4:
                ((BaseActivity) getActivity()).shareAction(configEntity.username + "的小店", ConfigUtil.HTTP_SHOP_SEE + configEntity.key);
                break;
            case 5:
                startActivity(new Intent(getActivity(), ShopSeeWebActivity.class));
                break;
        }
        if(!StringUtil.isEmpty(shopWenIntnet.getStringExtra("title"))){
            startActivity(shopWenIntnet);
        }
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

    /**
     * 处理会员信息
     *
     * @param json
     */
    private void MemberHander(String json) {
        MemberEntity memberEntity = JSON.parseObject(json, MemberEntity.class);
        if (memberEntity != null) {
            configEntity.username = memberEntity.username;
            ConfigUtil.saveConfig(getActivity(), configEntity);
            if(!StringUtil.isEmpty(memberEntity.user_logo)) {
                imageLoader.displayImage(memberEntity.user_logo, userImg,
                        AsynImageUtil.getImageOptions(),
                        new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.user_icon));
            } else {
                icon = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.mine_toux);
                userImg.setImageBitmap(icon);
                CacheImgUtil.clearUserIcon();
            }
            if (!StringUtil.isEmpty(memberEntity.leiji_dongjie_money)) {
                tvShopFrozenMoney.setText("¥: " + memberEntity.leiji_dongjie_money + "元");
            } else {
                tvShopFrozenMoney.setText("¥: 暂无数据");
            }
            if (!StringUtil.isEmpty(memberEntity.leiji_tixian_money)) {
                tvShopWithdrawMoney.setText("¥: " + memberEntity.leiji_tixian_money + "元");
            } else {
                tvShopWithdrawMoney.setText("¥: 暂无数据");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!ListUtils.isEmpty(list)){
            for (int i = 0; i <list.size() ; i++) {
                list.remove(list.get(i));
            }
            list.clear();
            list = null;
        }
        gridItem.setAdapter(null);
        if(icon != null){
            icon.recycle();
        }
        userImg.setImageBitmap(null);
    }

    @Override
    public void onClick(View v) {
        if(getActivity() == null){
            return;
        }
        Intent shopWenIntnet = new Intent(getActivity(), ShopWebActivity.class);
        switch (v.getId()){
            case R.id.img_shop_usericon:
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.ll_shop_frozen_money:
                shopWenIntnet.putExtra("title", "营收");
                shopWenIntnet.putExtra("url", ConfigUtil.HTTP_SHOP_MENOY);
                startActivity(shopWenIntnet);
                break;
            case R.id.ll_shop_withdraw_money:
                shopWenIntnet.putExtra("title", "营收");
                shopWenIntnet.putExtra("url", ConfigUtil.HTTP_SHOP_MENOY+"?bi_type=500");
                startActivity(shopWenIntnet);
                break;
            case R.id.tv_shop_gostore:
                if(getActivity() != null){
                    ((MainActivity) getActivity()).setTabSelection(1);
                    ((MainActivity) getActivity()).state = 1;
                }
                break;
        }
    }
}
