package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TranslateUtil;

/**
 * Created by jianzhang.
 * on 2017/8/23.
 * copyright easybiz.
 */

public class TranslateActivity extends BaseActivity implements View.OnClickListener, TranslateUtil.TranslateInterface {
    private View mTranslateView, mBackView;
    private RadioButton mZangyuRb, mHanyuRb;
    private EditText mInputEt, mOutputEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initView();
    }

    private void initView() {
        mTranslateView = findViewById(R.id.translate_tv);
        mZangyuRb = (RadioButton) findViewById(R.id.zangyu_rb);
        mHanyuRb = (RadioButton) findViewById(R.id.hanyu_rb);
        mInputEt = (EditText) findViewById(R.id.input_et);
        mOutputEt = (EditText) findViewById(R.id.output_et);
        mBackView = findViewById(R.id.back);
        mTranslateView.setOnClickListener(this);
        mZangyuRb.setOnClickListener(this);
        mHanyuRb.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translate_tv:
                goTranslate();
                break;
            case R.id.hanyu_rb:
                mZangyuRb.setChecked(false);
                mHanyuRb.setChecked(true);
                goTranslate();
                break;
            case R.id.zangyu_rb:
                mZangyuRb.setChecked(true);
                mHanyuRb.setChecked(false);
                goTranslate();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private boolean checkInput() {
        if (StringUtil.isEmpty(mInputEt.getText().toString())) {
            Toast.makeText(this, "请输入翻译内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getSearchType() {
        if (mZangyuRb.isChecked()) {
            return TranslateUtil.SEARCH_TYPE_TI_TO_ZH;
        } else {
            return TranslateUtil.SEARCH_TYPE_ZH_TO_TI;
        }
    }

    @Override
    public void onTranslateResult(final boolean success, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    try {
                        if (!StringUtil.isEmpty(result)) {
                            String res = result.replace("<", "");
                            res = res.replace(">", "");
                            mOutputEt.setText(res);
                        } else {
                            mOutputEt.setText("");
                        }
                    } catch (Exception e) {
                        mOutputEt.setText("");
                        e.printStackTrace();
                    }
                } else {
                    mOutputEt.setText("");
                    Toast.makeText(TranslateActivity.this, "翻译失败，请重试!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goTranslate() {
        if (checkInput()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TranslateUtil.translate(mInputEt.getText().toString(), getSearchType(), TranslateActivity.this);
                }
            }).start();
        }
    }
}
