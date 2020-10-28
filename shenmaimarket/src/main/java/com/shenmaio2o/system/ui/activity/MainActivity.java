package com.shenmaio2o.system.ui.activity;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shenmaio2o.system.R;
import com.shenmaio2o.system.utlis.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.test_id)
    TextView test;
    @Bind(R.id.drawee)
    SimpleDraweeView image;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.test_id)
    void click(){
        String url = "https://gd2.alicdn.com/imgextra/i2/380101244/TB2HHzZdNmJ.eBjy0FhXXbBdFXa_!!380101244.jpg";
        ImageLoaderUtils.getImage(url,image);
    }


}
