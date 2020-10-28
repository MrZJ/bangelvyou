package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.basulvyou.system.R;

/**
 * 拼车规则界面
 */
public class CarpoolingRuleActivity extends BaseActivity {

    private String type;
    private ImageView ruleImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_rule);
        type = getIntent().getExtras().getString("type");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        ruleImg = (ImageView) findViewById(R.id.img_rule_img);
        if(type.equals("carpooling")){
            setTitle("拼车规则");
            ruleImg.setBackgroundResource(R.mipmap.carpooling_rule);
        }else{
            setTitle("物流规则");
            ruleImg.setBackgroundResource(R.mipmap.logistic_rule);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }
}
