package com.fuping.system.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.TongJiAdapter;
import com.fuping.system.entity.Pie;
import com.fuping.system.entity.TongJiDetailEntity;
import com.fuping.system.entity.TongJiEntity;
import com.fuping.system.request.CountryTongJiRequest;
import com.fuping.system.request.PeopleTongJiRequest;
import com.fuping.system.ui.activity.MyFragmentActivity;
import com.fuping.system.wibget.PieView;

import java.util.ArrayList;
import java.util.List;

import static com.fuping.system.ui.activity.MyFragmentActivity.TYPE_TONGJI_COUNTRY_LIST;
import static com.fuping.system.ui.activity.MyFragmentActivity.TYPE_TONGJI_PEOPLE_COUNTRY_LIST;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class TongJiFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final int TYPE_COUNTRY = 1;
    public static final int TYPE_PEOPLE = 2;
    private GridView gridview;
    private PieView pieview;
    private ArrayList<Pie> pieArrayList = new ArrayList<>();
    private String[] arr = {"未督查户数", "未达脱贫标准", "达到脱贫标准"};
    private float[] pre = {0, 100, 0};

    private int mType;

    public static TongJiFragment getInstance(int type) {
        TongJiFragment fragment = new TongJiFragment();
        fragment.setmType(type);
        return fragment;
    }

    private void setmType(int type) {
        this.mType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tongji, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridview = (GridView) view.findViewById(R.id.gridview);
        pieview = (PieView) view.findViewById(R.id.pieview);
        gridview.setOnItemClickListener(this);
        loadData();
    }

    private List<TongJiEntity> getData(TongJiEntity tongJiEntity) {
        List<TongJiEntity> list = new ArrayList();
        TongJiEntity entity = new TongJiEntity();
        if (TYPE_COUNTRY == mType) {
            entity.title = "未督查村数";
        } else {
            entity.title = "未督查户数";
        }
        entity.count = tongJiEntity.all_no_inspection_count;
        entity.percent = tongJiEntity.all_no_inspection_count_ratio;
        list.add(entity);
        TongJiEntity entity1 = new TongJiEntity();
        entity1.title = "未达脱贫标准";
        entity1.count = tongJiEntity.some_no_inspection_count;
        entity1.percent = tongJiEntity.some_no_inspection_count_ratio;
        list.add(entity1);
        TongJiEntity entity2 = new TongJiEntity();
        entity2.title = "达到脱贫标准";
        entity2.count = tongJiEntity.yes_inspection_count;
        entity2.percent = tongJiEntity.yes_inspection_count_ratio;
        list.add(entity2);
        TongJiEntity entity3 = new TongJiEntity();
        entity3.title = "总贫困数";
        entity3.count = tongJiEntity.all_count;
        entity3.percent = tongJiEntity.all_count_ratio;
        list.add(entity3);
        return list;
    }

    private void loadData() {
        if (mType == TYPE_PEOPLE) {
            PeopleTongJiRequest request = new PeopleTongJiRequest();
            httpGetRequest(request.getRequestUrl(), PeopleTongJiRequest.PEOPLE_TONG_JI_REQUEST);
        } else {
            CountryTongJiRequest request = new CountryTongJiRequest();
            httpGetRequest(request.getRequestUrl(), CountryTongJiRequest.COUNTRY_TONG_JI_REQUEST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleTongJiRequest.PEOPLE_TONG_JI_REQUEST:
            case CountryTongJiRequest.COUNTRY_TONG_JI_REQUEST:
                handRequest(json);
                break;
        }
    }

    private void handRequest(String json) {
        try {
            TongJiDetailEntity entity = JSONObject.parseObject(json, TongJiDetailEntity.class);
            if (entity != null && entity.count_each != null) {
                gridview.setAdapter(new TongJiAdapter(getData(entity.count_each), getActivity()));
                try {
                    pre[0] = Float.valueOf(entity.count_each.all_no_inspection_count_ratio);
                    pre[1] = Float.valueOf(entity.count_each.some_no_inspection_count_ratio);
                    pre[2] = Float.valueOf(entity.count_each.yes_inspection_count_ratio);
                } catch (Exception e) {
                }
                int[] pieColor = {getResources().getColor(R.color.blue),
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.deep_blue)
                };
                for (int i = 0; i < arr.length; i++) {
                    Pie pie = new Pie(pre[i], arr[i], pieColor[i]);
                    pieArrayList.add(pie);
                }
                pieview.SetPie(pieArrayList);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mType == TYPE_COUNTRY) {
            MyFragmentActivity.startActivity(getActivity(), TYPE_TONGJI_COUNTRY_LIST, position + "", null, null);
        } else if (mType == TYPE_PEOPLE) {
            MyFragmentActivity.startActivity(getActivity(), TYPE_TONGJI_PEOPLE_COUNTRY_LIST, position + "", null, null);
        }
    }
}
