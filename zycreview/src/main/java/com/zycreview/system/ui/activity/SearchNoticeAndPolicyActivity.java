package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zycreview.system.R;
import com.zycreview.system.utils.StringUtil;

/**
 * 搜索公告或政策
 */
public class SearchNoticeAndPolicyActivity extends BaseActivity implements View.OnClickListener {

    private String type;
    private EditText keyWord;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notice_and_policy);
        type = getIntent().getStringExtra("type");
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
                    Intent intent;
                    if (type.equals("notice")) {
                        intent = new Intent(this, NoticeSearchResultActivity.class);
                    } else {
                        intent = new Intent(this, PolicySearchResultActivity.class);
                        intent.putExtra("searchType", "2");
                    }
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
