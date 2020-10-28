package com.zycreview.system.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.RegisterApi;
import com.zycreview.system.entity.EntpTypeEntity;
import com.zycreview.system.ui.activity.DotSearchResultActivity;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.OnWheelClickedListener;
import com.zycreview.system.wibget.address.WheelView;

import java.util.List;

/**
 * 查询网点页面
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener, OnWheelChangedListener, OnWheelClickedListener {

    private View mView, typeView, areaView, pop_rl_title, pop_ll_context;
    private EditText editKeyWord;
    private TextView tv_search_area, tv_search_type;
    private Button btnCommint;

    // 选择地区相关
    private PopupWindow pop = null;
    private WheelView mViewProvince;
    private TextView tv_confirm, tv_close;
    private String[] type;
    private String[] searchType = {"网点查询","企业查询"};
    private int popType = 0;
    private int pCurrent;
    private int tCurrent;

    /**
     * 当前查询类型
     */
    private String mCurrentSearchType;
    private String typeIndex;
    private String typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        initListener();
        getTypeData();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        setTitle("查询企业");
        hideBackBtn();
        editKeyWord = (EditText) mView.findViewById(R.id.ed_search_keyword);
        typeView = mView.findViewById(R.id.rel_search_type);
        areaView = mView.findViewById(R.id.rel_search_area);
        typeIndex = "0";
        tv_search_type = (TextView) mView.findViewById(R.id.tv_search_type);
        tv_search_area = (TextView) mView.findViewById(R.id.tv_search_area);
        btnCommint = (Button) mView.findViewById(R.id.btn_search_commint);

        //选择地区相关
        pop = new PopupWindow(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.pop_address_item, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        view.findViewById(R.id.id_city).setVisibility(View.GONE);
        view.findViewById(R.id.id_district).setVisibility(View.GONE);
        tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        tv_close = (TextView) view.findViewById(R.id.tv_close);
        pop_ll_context = view.findViewById(R.id.pop_ll_context);
        pop_rl_title = view.findViewById(R.id.pop_rl_title);
        pop_rl_title.setVisibility(View.GONE);
        LinearLayout.LayoutParams llParames = (LinearLayout.LayoutParams) pop_ll_context.getLayoutParams();
        llParames.rightMargin = 30;
        llParames.leftMargin = 30;
        pop_ll_context.setLayoutParams(llParames);
    }

    private void initListener() {
        typeView.setOnClickListener(this);
//        areaView.setOnClickListener(this);
        btnCommint.setOnClickListener(this);

        //选择地区相关
        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewProvince.addClickingListener(this);
        // 添加onclick事件
        tv_confirm.setOnClickListener(this);
        tv_close.setOnClickListener(this);
    }

    /**
     * 设置地域数据
     */
    private void setAddressData() {
        //选择地区相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(getActivity(), searchType);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(pCurrent);
        updateSearchType();
    }

    private void getTypeData(){
        httpGetRequest(RegisterApi.getEntpTypeUrl(), RegisterApi.API_GET_ENTPTYPE);
    }

    private void setTypeData() {
        //选择类型相关
        ArrayWheelAdapter wheelAdapter = new ArrayWheelAdapter(getActivity(), type);
        mViewProvince.setViewAdapter(wheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(3);
        mViewProvince.setCurrentItem(tCurrent);
        updateTypes();
    }

    /**
     * 选择查询类型
     */
    private void updateSearchType() {
        pCurrent = mViewProvince.getCurrentItem();
        mCurrentSearchType = searchType[pCurrent];
    }

    private void updateTypes() {
        tCurrent = mViewProvince.getCurrentItem();
        typeName = type[tCurrent];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_search_type:
                popType = 0;
                if(type != null && type.length >=0){
                    setTypeData();
                    pop.showAsDropDown(typeView);
                }else{
                    Toast.makeText(getActivity(),"暂无企业类型",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rel_search_area:
//                popType = 1;
//                setAddressData();
//                pop.showAsDropDown(areaView);
                break;
            case R.id.btn_search_commint:
                if(StringUtil.isEmpty(typeIndex)){
                    Toast.makeText(getActivity(),"请选择查询类型",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), DotSearchResultActivity.class);
                    intent.putExtra("typeIndex", typeIndex);
                    if(typeIndex.equals("1")){//网点查询
                        if (StringUtil.isEmpty(editKeyWord.getText().toString())) {
                            Toast.makeText(getActivity(), "关键字不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            intent.putExtra("key", editKeyWord.getText().toString());
                        }
                    } else{//企业查询
                        if (StringUtil.isEmpty(editKeyWord.getText().toString()) && StringUtil.isEmpty(typeName)) {
                            Toast.makeText(getActivity(), "关键字，企业类型至少有一个不为空！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            intent.putExtra("key", editKeyWord.getText().toString());
                            intent.putExtra("type_name", typeName);
                        }
                    }
                    startActivity(intent);
                }
                break;
            case R.id.tv_close:
                pop.dismiss();
                break;
            case R.id.tv_confirm:
                pop.dismiss();
                tv_search_area.setText(mCurrentSearchType);
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (popType == 1) {
            updateSearchType();
        } else {
            updateTypes();
        }
    }


    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        if (mViewProvince.getCurrentItem() == itemIndex) {
            pop.dismiss();
            if (popType == 1) {
                if (mCurrentSearchType.equals("网点查询")) {
                    typeIndex = "1";
                    typeView.setVisibility(View.GONE);
                } else {
                    typeIndex = "0";
                    typeView.setVisibility(View.VISIBLE);
                }
                tv_search_area.setText(mCurrentSearchType);
            } else {
                tv_search_type.setText(typeName);
            }
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case RegisterApi.API_GET_ENTPTYPE:
                entpHander(json);
                break;
        }
    }

    /**
     * 处理企业类型
     * @param str
     */
    private void entpHander(String str){
        if(!StringUtil.isEmpty(str)) {
            EntpTypeEntity entpTypeEntity = JSON.parseObject(str, EntpTypeEntity.class);
            if(entpTypeEntity != null){
                List<EntpTypeEntity.EntpType> entpType = entpTypeEntity.dataList;
                if(!ListUtils.isEmpty(entpType)){
                    type = new String[entpType.size()];
                    for (int i = 0; i < entpType.size(); i++) {
                        type[i] = entpType.get(i).base_name;
                    }
                }
            }
        }
    }

}
