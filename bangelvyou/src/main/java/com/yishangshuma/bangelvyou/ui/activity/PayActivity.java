package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.BuyApi;
import com.yishangshuma.bangelvyou.entity.Pay;
import com.yishangshuma.bangelvyou.util.Constants;
import com.yishangshuma.bangelvyou.util.PayResult;
import com.yishangshuma.bangelvyou.util.StringUtil;
import com.yishangshuma.bangelvyou.util.WeiXinPayUtil;

/**
 * 支付宝和微信支付页面
 */
public class PayActivity extends BaseActivity implements View.OnClickListener{

    private TextView allPriceID, tv_order;
    private View rl_zhifubao, rl_weixin;
    private ImageView check_pay_zhifubao, check_pay_weixin;
    private String cart_sum;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    /**支付方式状态*/
    private static final int PAY_ZHIFUBAO = 1;
    private static final int PAY_WEIXIN = 2;
    private int pay_way;//支付方式

    String tradeOutId = "";//支付编号
    String price = "";
    String subject = "测试的商品2";
    String body = "该测试商品的详细描述1";
    String id = "";
    private Pay payInfo;

    /**微信支付参数 */
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult resultObj = new PayResult((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT)
                                .show();

                        PayActivity.this.finish();

                        // Intent intent = new Intent(PayActivity.this,
                        // BillDetailActivity.class);
                        // intent.putExtra("type", 2);
					/*Intent intent = new Intent(PayActivity.this,
							OrderHistoryActivity.class);
					intent.putExtra("tag", 1);// 转到我的订单点餐记录

					startActivity(intent);*/

                        appManager.finishOtherActivity();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(PayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "支付环境良好", Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        msgApi.registerApp(Constants.APP_ID);
        id = getIntent().getExtras().getString("id");
//		cart_sum = getIntent().getExtras().getString("cart_sum");
//		tradeOutId = getIntent().getExtras().getString("tradeoutid");
//		subject = getIntent().getExtras().getString("shopName");
//		body = subject;
        initView();
        initListener();
//        showLoadingDialog();
        getWeiXinPayInfo(id);
//		setData();
    }

    protected void onResume(){
        super.onResume();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("等待支付");
        allPriceID = (TextView) findViewById(R.id.allPriceID);
        tv_order = (TextView) findViewById(R.id.tv_order);
        rl_zhifubao = (View) findViewById(R.id.rl_zhifubao);
        rl_weixin = (View) findViewById(R.id.rl_weixin);
        check_pay_zhifubao = (ImageView) findViewById(R.id.check_pay_zhifubao);
        check_pay_weixin = (ImageView) findViewById(R.id.check_pay_weixin);
//        setCheckBoxCheck(PAY_WEIXIN);
    }

    /**
     * 请求微信支付参数
     */
    private void getWeiXinPayInfo(String id){
        httpGetRequest(BuyApi.getWeiXinPayInfo(id), BuyApi.API_GET_WEIXIN_PAY_INFO);
    }


    @Override
    public void httpResponse(String json, int action) {
        switch (action) {
            case BuyApi.API_GET_WEIXIN_PAY_INFO:
                handlerWeiXin(json);
                break;

            default:
                break;
        }
    }

    private void  handlerWeiXin(String json){
        if(null != json && !"".equals(json)){
            //调用微信支付
            PayReq req = new PayReq();
            req.appId = JSON.parseObject(json).getString("appid");
            req.nonceStr = JSON.parseObject(json).getString("noncestr");
            req.packageValue = JSON.parseObject(json).getString("package");
            req.partnerId = JSON.parseObject(json).getString("partnerid");
            req.prepayId = JSON.parseObject(json).getString("prepayid");
            req.sign = JSON.parseObject(json).getString("sign");
            req.timeStamp = JSON.parseObject(json).getString("timestamp");
            msgApi.sendReq(req);
        }else{
            Toast.makeText(this, "在线支付失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置tab数据
     */
    private void setData(){
        allPriceID.setText("实付款:￥" + (StringUtil.stringToDouble(payInfo.order_money)));
        price = payInfo.order_money;
//		price = "0.01";//测试数据
        tradeOutId = payInfo.out_trade_no;//订单id
        subject = payInfo.order_name;
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        tv_order.setOnClickListener(this);
        rl_zhifubao.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_order://立即支付
//			payByWay();
                if(null != payInfo){
                    payByWeixin();
                }else{
                    Toast.makeText(this, "立即支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_zhifubao://选择支付宝支付
//                setCheckBoxCheck(PAY_ZHIFUBAO);
                break;
            case R.id.rl_weixin://选择微信支付
//                setCheckBoxCheck(PAY_WEIXIN);
                break;
        }
    }

    /**
     * 选择支付方式功能按钮显示
     */
    /*private void setCheckBoxCheck(int select_pay){
        pay_way = select_pay;
        switch(select_pay){
            case PAY_ZHIFUBAO:
                check_pay_zhifubao.setBackgroundResource(R.drawable.radiogreen);
                check_pay_weixin.setBackgroundResource(R.drawable.radiogray);
                break;
            case PAY_WEIXIN:
                check_pay_zhifubao.setBackgroundResource(R.drawable.radiogray);
                check_pay_weixin.setBackgroundResource(R.drawable.radiogreen);
                break;
        }
    }*/

    /**
     * 按照选择的支付方式进行支付
     */
    /*private void payByWay(){
        switch(pay_way){
            case PAY_ZHIFUBAO:
                payByZhifubao();
                break;
            case PAY_WEIXIN:
                payByWeixin();
                break;
        }
    }*/

    /**
     * 支付宝支付
     * call alipay sdk pay. 调用SDK支付
     *
     */
   /* public void payByZhifubao() {
        if(StringUtil.stringToDouble(price) <= 0){
            Toast.makeText(PayActivity.this, "付款金额最少为0.01！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (body == null || tradeOutId == null) {
            Toast.makeText(PayActivity.this, "支付账单有误，请返回重试！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String orderInfo = PayUtil.getOrderInfo(subject, body, price,
                tradeOutId);
        String sign = PayUtil.sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + PayUtil.getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }*/

    /**
     * 调用微信支付
     */
    public void payByWeixin(){
        WeiXinPayUtil weixin = new WeiXinPayUtil(this, msgApi, subject, tradeOutId, price);
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    /*public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask payTask = new PayTask(PayActivity.this);
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }*/

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    /*public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }*/

}

