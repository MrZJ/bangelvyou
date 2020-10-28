package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.basulvyou.system.R;
import com.basulvyou.system.util.ListUtils;

import java.util.ArrayList;

/**
 * 选择地点
 * <p/>
 * Created by Administrator on 2016/7/13 0013.
 */
public class ChooserAddress extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    private ListView listView;
    private ArrayList<String> pois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser_address);
        pois = getIntent().getExtras().getStringArrayList("pois");
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        top_title_search.setVisibility(View.GONE);
        setTitle("地点");
        listView = (ListView) findViewById(R.id.listView);
        if (!ListUtils.isEmpty(pois)) {
            ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.list_item, pois);
            listView.setAdapter(adapter);
        }
    }

    public void initListener() {
        super.initListener();
        listView.setOnItemClickListener(this);
        btn_top_goback.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!ListUtils.isEmpty(pois)) {
            ShareTextActivity.backAddress = pois.get(position);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_top_goback:
                finish();
                break;
        }
    }
}
