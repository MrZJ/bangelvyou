package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.entity.CreditScore;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.util.ConfigUtil;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 * 我的信用积分界面
 */

public class MyCreditActivity extends BaseActivity implements View.OnClickListener {
    public static final int HTTP_CREDIT = 11;
    private View img_top_goback;
    private RatingBar ratingBar;
    private TextView my_credit_count;
    private View progressBar, credit_layout, empty_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit);
        initView();
        loadDate();
    }

    private void initView() {
        img_top_goback = findViewById(R.id.img_top_goback);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        img_top_goback.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        credit_layout = findViewById(R.id.credit_layout);
        empty_tv = findViewById(R.id.empty_tv);
        my_credit_count = (TextView) findViewById(R.id.my_credit_count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_goback:
                finish();
                break;
        }
    }

    private void loadDate() {
        StringBuilder urlBuilder = new StringBuilder(ConfigUtil.HTTP_URL);
        urlBuilder.append("/Service.do?method=credit_score&op=credit_score").append("&key=").append(configEntity.key);
        httpGetRequest(urlBuilder.toString(), HTTP_CREDIT);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        if (HTTP_CREDIT == action) {
            try {
                CreditScore creditScore = JSON.parseObject(json, CreditScore.class);
                if (creditScore != null) {
                    float score = 0;
                    float rating = 0;
                    try {
                        score = Float.valueOf(creditScore.credit_score);
                        rating = score / 20;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ratingBar.setRating(rating);
                    progressBar.setVisibility(View.GONE);
                    empty_tv.setVisibility(View.GONE);
                    credit_layout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        progressBar.setVisibility(View.GONE);
        empty_tv.setVisibility(View.VISIBLE);
        credit_layout.setVisibility(View.GONE);
    }
}
