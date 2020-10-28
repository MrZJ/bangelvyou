package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.basulvyou.system.R;

/**
 * 发起拼车界面
 */
public class SendCarpoolingActivity extends BaseActivity implements View.OnClickListener{

    private View owner,passenger;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_carpooling);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("发布拼车");
        owner = findViewById(R.id.lin_send_carpooling_owner);
        passenger = findViewById(R.id.lin_send_carpooling_passenger);
        close = (ImageView) findViewById(R.id.img_send_carpooling_close);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        owner.setOnClickListener(this);
        passenger.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_send_carpooling_close){
            SendCarpoolingActivity.this.finish();
            return;
        }
        if (!configEntity.isLogin) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }
        Intent intent = new Intent(this,CarpoolingListActivity.class);
        switch (v.getId()){
            case R.id.lin_send_carpooling_owner://我是车主
                intent.putExtra(CarpoolingListActivity.TYPE, CarpoolingListActivity.CAR_OWNER);
                startActivity(intent);
                break;
            case R.id.lin_send_carpooling_passenger://我的乘客
                intent.putExtra(CarpoolingListActivity.TYPE, CarpoolingListActivity.PASSENGER);
                startActivity(intent);
                break;
        }
    }

}
