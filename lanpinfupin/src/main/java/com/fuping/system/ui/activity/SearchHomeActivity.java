package com.fuping.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fuping.system.R;
import com.fuping.system.utils.StringUtil;


/**
 * 搜索
 */
public class SearchHomeActivity extends BaseActivity implements View.OnClickListener {

    private EditText keyWord;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notice);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setTitle("搜索");
        keyWord = (EditText) findViewById(R.id.edit_seach_keyword);
        search = (Button) findViewById(R.id.btn_commint_search);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commint_search:
                if (!StringUtil.isEmpty(keyWord.getText().toString().trim())) {
                    Intent intent = new Intent(this, NoticeSearchResultActivity.class);
                    intent.putExtra("key", keyWord.getText().toString().trim());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "请填写需要搜索的关键字！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
