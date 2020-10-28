package com.fuping.system.wibget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuping.system.R;
import com.fuping.system.adapter.AutoCompleteAdapter;
import com.fuping.system.adapter.AutoCompleteCountryAdapter;
import com.fuping.system.adapter.AutoCompleteGovermentAdapter;

/**
 * Created by jianzhang.
 * on 2017/10/27.
 * copyright easybiz.
 */

public class AdvancedAutoCompleteTextView extends RelativeLayout implements TextWatcher {

    private Context context;
    private AutoCompleteTextView tv;


    public AdvancedAutoCompleteTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public AdvancedAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv = new AutoCompleteTextView(context);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(params);
        tv.setBackgroundResource(R.color.white);
        tv.setPadding(10, 0, 40, 0);
        tv.setSingleLine();

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.rightMargin = 10;
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(p);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setImageResource(R.mipmap.del);
        iv.setClickable(true);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tv.setText("");
                tv.setHint("");
            }
        });
        tv.addTextChangedListener(this);
        this.addView(tv);
        this.addView(iv);
    }

    public void setAdapter(AutoCompleteAdapter adapter) {
        tv.setAdapter(adapter);
    }

    public void setAdapter(AutoCompleteCountryAdapter adapter) {
        tv.setAdapter(adapter);
    }

    public void setAdapter(AutoCompleteGovermentAdapter adapter) {
        tv.setAdapter(adapter);
    }

    public void setThreshold(int threshold) {
        tv.setThreshold(threshold);
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return tv;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void hidden(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
    }

    public void hideList() {
        try {
            tv.dismissDropDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText(String string) {
        try {
            tv.setText(null);
            tv.setHint(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText2(String string) {
        try {
            tv.setText(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTextSieze(float size) {
        try {
            tv.setTextSize(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

