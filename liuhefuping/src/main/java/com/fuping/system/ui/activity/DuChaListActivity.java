package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.DuChaListAdapter;
import com.fuping.system.entity.CountryDuChaAllEntity;
import com.fuping.system.entity.CountryDuChaEntity;
import com.fuping.system.request.CountryDuChaListRequest;
import com.fuping.system.request.PeopleDuChaListRequest;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.StringUtil;

/**
 * Created by jianzhang.
 * on 2017/10/19.
 * copyright easybiz.
 */

public class DuChaListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int TYPE_PEOPLE = 0;
    public static final int TYPE_COUNTRY = 1;
    public static final int REQUEST_CODE_SAVE = 100;
    private ListView mListView;
    private String village_id;
    private int mType;

    public static void startActivity(Context context, int type, String village_id) {
        Intent intent = new Intent(context, DuChaListActivity.class);
        intent.putExtra("village_id", village_id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ducha_list);
        mType = getIntent().getIntExtra("type", -1);
        village_id = getIntent().getStringExtra("village_id");
        if (StringUtil.isEmpty(village_id)) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("脱贫攻坚查看");
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setText("新建");
        top_right_title.setOnClickListener(this);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        if (v == top_right_title) {
            if (mType == TYPE_COUNTRY) {
                CountryDuChaActivity.startActivityForResult(this, village_id, CountryDuChaActivity.TYPE_DETAIL, REQUEST_CODE_SAVE);
            } else {
                PeopleDuChaActivity.startActivityForResult(this, village_id, CountryDuChaActivity.TYPE_DETAIL, REQUEST_CODE_SAVE);
            }
        }
    }

    private void loadData() {
        if (mType == TYPE_COUNTRY) {
            CountryDuChaListRequest request = new CountryDuChaListRequest(configEntity.key, village_id);
            httpGetRequest(request.getRequestUrl(), CountryDuChaListRequest.COUNTRY_DUCHA_LIST_REQUEST);
        } else {
            PeopleDuChaListRequest request = new PeopleDuChaListRequest(configEntity.key, village_id);
            httpGetRequest(request.getRequestUrl(), CountryDuChaListRequest.COUNTRY_DUCHA_LIST_REQUEST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CountryDuChaListRequest.COUNTRY_DUCHA_LIST_REQUEST:
                handleRequest(json);
                break;
        }
    }

    private CountryDuChaAllEntity countryDetailAllEntity;

    private void handleRequest(String json) {
        try {
            countryDetailAllEntity = JSONObject.parseObject(json, CountryDuChaAllEntity.class);
            if (countryDetailAllEntity != null) {
                if (countryDetailAllEntity.is_complete_each != null && countryDetailAllEntity.is_complete_each.is_complete == 1) {
                    top_right_title.setVisibility(View.GONE);
                } else {
                    top_right_title.setVisibility(View.VISIBLE);
                }
                if (mType == TYPE_COUNTRY) {
                    if (!ListUtils.isEmpty(countryDetailAllEntity.inspectionVillageHistoryList)) {
                        mListView.setAdapter(new DuChaListAdapter(this, countryDetailAllEntity.inspectionVillageHistoryList));
                    } else {
                        showToast("没有督查历史，请新建");
                        CountryDuChaActivity.startActivityForResult(this, village_id, CountryDuChaActivity.TYPE_DETAIL, REQUEST_CODE_SAVE);
                    }
                } else {
                    if (!ListUtils.isEmpty(countryDetailAllEntity.inspectionPoorerHistoryList)) {
                        mListView.setAdapter(new DuChaListAdapter(this, countryDetailAllEntity.inspectionPoorerHistoryList));
                    } else {
                        showToast("没有督查历史，请新建");
                        PeopleDuChaActivity.startActivityForResult(this, village_id, CountryDuChaActivity.TYPE_DETAIL, REQUEST_CODE_SAVE);
                    }
                }
            } else {
                countryDetailAllEntity = null;
            }
        } catch (Exception e) {
            countryDetailAllEntity = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CountryDuChaEntity entity = (CountryDuChaEntity) mListView.getAdapter().getItem(position);
            if (entity.is_my == 1) {
                showToast("您没有权限查看");
            } else {
                if (mType == TYPE_COUNTRY) {
                    CountryDuChaActivity.startActivity(this, entity.inspection_village_id, CountryDuChaActivity.TYPE_LIST);
                } else {
                    PeopleDuChaActivity.startActivity(this, entity.inspection_poor_id, CountryDuChaActivity.TYPE_LIST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SAVE) {
                loadData();
            }
        }
    }
}
