package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;

/**
 * 拼车下单界面
 */
public class OrderCarpoolingActivity extends BaseActivity implements View.OnClickListener{

    private TextView budget,titleNum,seatNum,startTime,startAda,endAds,tel,notice;
    private Button sure;
    private CarpoolingInfoEntity carpoolingInfoEntity;
    private ImageView topBanner;
    private View startTimeLine;
    private String locationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_carpooling);
        carpoolingInfoEntity = (CarpoolingInfoEntity) getIntent().getSerializableExtra("carpoolingInfo");
        locationType = getIntent().getStringExtra("locationType");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("详情页");
        topBanner = (ImageView) findViewById(R.id.order_carpooling_banner);//顶部banner
        budget = (TextView) findViewById(R.id.tv_order_carpooling_budget);//预算
        titleNum = (TextView) findViewById(R.id.order_carpooling_num);//人数标题
        seatNum = (TextView) findViewById(R.id.tv_order_carpooling_num);//人数
        startTimeLine = findViewById(R.id.order_carpooling_budgetline);//分割线
        startTime = (TextView) findViewById(R.id.tv_order_carpooling_time);//距离开始时间
        startAda = (TextView) findViewById(R.id.tv_order_carpooling_startads);//出发地
        endAds = (TextView) findViewById(R.id.tv_order_carpooling_endads);//目的地
        tel = (TextView) findViewById(R.id.tv_order_carpooling_tel);//电话
        notice = (TextView) findViewById(R.id.tv_order_carpooling_notice);//其他留言
        sure = (Button) findViewById(R.id.btn_order_carpooling_sure);//我要下单
        if(null != carpoolingInfoEntity){
            if(carpoolingInfoEntity.comm_type.equals("104")){
                topBanner.setVisibility(View.GONE);
                titleNum.setVisibility(View.GONE);
                seatNum.setVisibility(View.GONE);
                startTimeLine.setVisibility(View.GONE);
                startTime.setVisibility(View.GONE);
            }
            budget.setText("￥"+carpoolingInfoEntity.goods_price);
            if(carpoolingInfoEntity.comm_type.equals("101")){
                seatNum.setText(carpoolingInfoEntity.pd_stock+"/"+carpoolingInfoEntity.inventory);
            }else{
                seatNum.setText(carpoolingInfoEntity.inventory);
            }
            startTime.setText(carpoolingInfoEntity.fctime);
            startAda.setText(carpoolingInfoEntity.pcqd);
            endAds.setText(carpoolingInfoEntity.pczd);
            tel.setText(carpoolingInfoEntity.goods_content);
            notice.setText(carpoolingInfoEntity.goods_qtcontent);
        }
        if (!TextUtils.isEmpty(carpoolingInfoEntity.add_user_id) &&
                configEntity.key.equals(carpoolingInfoEntity.add_user_id)) {
            sure.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        if ("appointment".equals(locationType)){
            sure.setText("我要预约·");
        }
        // TODO: 2017/6/9 处理点击事件
        sure.setOnClickListener(this);
        tel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_order_carpooling_sure:
                Intent takeIntent = new Intent(this,TakeCarpoolingActivity.class);
                takeIntent.putExtra("id",carpoolingInfoEntity.goods_id);
                takeIntent.putExtra("maxNum", carpoolingInfoEntity.pd_stock);
                takeIntent.putExtra("commType", carpoolingInfoEntity.comm_type);
                if(carpoolingInfoEntity.comm_type.equals("104")){
                    takeIntent.putExtra("date",carpoolingInfoEntity.up_date);
                }else{
                    takeIntent.putExtra("date",carpoolingInfoEntity.down_date);
                }
                startActivity(takeIntent);
                break;
            case R.id.tv_order_carpooling_tel:
                callTel(tel.getText().toString().trim());
                break;
        }
    }

    /**
     * 拨打电话
     * @param str
     */
    private void callTel(String str){
        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + str));
        startActivity(phoneIntent);
    }
}
