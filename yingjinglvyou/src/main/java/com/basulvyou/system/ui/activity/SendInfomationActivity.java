package com.basulvyou.system.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.ImgGridAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.util.BitMapUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.photo.Bimp;
import com.basulvyou.system.util.photo.ImageItem;
import com.basulvyou.system.wibget.datepicker.NumericWheelAdapter;
import com.basulvyou.system.wibget.datepicker.OnWheelChangedListener;
import com.basulvyou.system.wibget.datepicker.WheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 发布拼车信息
 */
public class SendInfomationActivity extends BaseActivity implements View.OnClickListener{

    private String type;//拼车类型
    private EditText date,startAds,endAds,tel,num,prices,other;
    private Button sendInfo;
    private String editStartDate;
    // 添加大小月月份并将其转换为list,方便之后的判断
    String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
    String[] months_little = { "4", "6", "9", "11" };
    List<String> list_big;
    List<String> list_little;
    private static int START_YEAR = 1895, END_YEAR = 2100;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int startYear =0;
    private int startMonth =0;
    private int startDay =0;
    private int startHour =0;
    private int startMin =0;
    private TextView popTip;
    private Button ok, cancel;
    private Calendar c;
    WheelView wv_year, wv_month, wv_day, wv_hour, wv_min;
    private AlertDialog dialog;
    private LocationClient mLocationClient;
    private String address;
    private ImageView getLocal;
    private ProgressBar proLocal;
    private String lnglatString;
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button imgCancel;//取消
    private GridView noScrollgridview;
    public static Bitmap bimap ;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取
    private View parentView;
    private String imgString;
    private ImgGridAdapter imgAdapter;
    private int maxNum = 1;//图片最大选取数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_send_infomation,null);
        setContentView(parentView);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        type = getIntent().getStringExtra("type");
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("信息发布");
        setLeftBackButton();
        date = (EditText) findViewById(R.id.edt_send_info_date);
        startAds = (EditText) findViewById(R.id.edt_send_info_startads);
        endAds = (EditText) findViewById(R.id.edt_send_info_endads);
        tel = (EditText) findViewById(R.id.edt_send_info_tel);
        num = (EditText) findViewById(R.id.edt_send_info_num);
        prices = (EditText) findViewById(R.id.edt_send_info_prices);
        other = (EditText) findViewById(R.id.edt_send_info_other);
        if(null != type && "102".equals(type)){
            num.setHint("请填写您的乘车人数");
            prices.setHint("请填写您的车费预算");
        }
        sendInfo = (Button) findViewById(R.id.btn_send_info_send);
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        initDatePickerDialog();
        getLocal = (ImageView) findViewById(R.id.img_send_info_getstartads);
        proLocal = (ProgressBar) findViewById(R.id.pro_send_info_getstartads);
        getLocation();

        noScrollgridview = (GridView) findViewById(R.id.gri_send_info_img);
        pop = new PopupWindow(SendInfomationActivity.this);
        View view = getLayoutInflater().inflate(R.layout.pop_user_choseicon,null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout pop_layout=(RelativeLayout) view.findViewById(R.id.pop_parent);
        pop_layout.getBackground().setAlpha(60);//设置背景透明度
        use_camera = view.findViewById(R.id.use_camera);
        use_local = view.findViewById(R.id.use_local);
        imgCancel = (Button) view.findViewById(R.id.item_cancel);
        imgAdapter = new ImgGridAdapter(this,maxNum);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview.setAdapter(imgAdapter);

    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        sendInfo.setOnClickListener(this);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.show();
                return true;
            }
        });
        getLocal.setOnClickListener(this);
        use_camera.setOnClickListener(this);
        use_local.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Bimp.tempSelectBitmap.size()) {
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(SendInfomationActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("num", maxNum);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_info_send://发送信息
                checkEdti();
                break;
            case R.id.img_send_info_getstartads://定位
                getLocation();
                break;
            case R.id.use_camera://打开照相机
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                Intent intent = new Intent(this, AlbumActivity.class);
                intent.putExtra("id","1");
                intent.putExtra("num",maxNum);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.item_cancel://关闭图片选择
                pop.dismiss();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgAdapter.update();
    }

    /**
     * 获取地理位置和经纬度
     */
    private void getLocation(){
        getLocal.setVisibility(View.GONE);
        proLocal.setVisibility(View.VISIBLE);
        initLocation();
    }

    /**
     * 判断字段是否为空
     */
    private void checkEdti(){
        if(StringUtil.isEmpty(date.getText().toString().trim())){
            Toast.makeText(this,"请输入时间日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(startAds.getText().toString().trim())){
            Toast.makeText(this,"请输入出发地",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(endAds.getText().toString().trim())){
            Toast.makeText(this,"请输入目的地",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(tel.getText().toString().trim())){
            Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(num.getText().toString().trim())){
            Toast.makeText(this,"请输入座位数",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isEmpty(prices.getText().toString().trim())){
            Toast.makeText(this,"请输入价格",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!ListUtils.isEmpty(Bimp.tempSelectBitmap)){
            imgString = BitMapUtil.convertIconToString(Bimp.tempSelectBitmap.get(0).getBitmap());
        }
        sendInfo();
    }

    /**
     * 发布信息
     */
    private void sendInfo(){
        httpPostRequest(CarpoolingApi.sendCarpoolingInfo(), getRequsetParams(), CarpoolingApi.API_SEND_CARPOOLING_INFO);
    }

    private HashMap<String,String> getRequsetParams(){
        HashMap<String,String> requestParams = new HashMap<>();
        requestParams.put("key",configEntity.key);
        requestParams.put("comm_type",type);
        requestParams.put("price_ref",prices.getText().toString().trim());
        requestParams.put("pcqd",startAds.getText().toString().trim());
        requestParams.put("pczd",endAds.getText().toString().trim());
        requestParams.put("comm_content",tel.getText().toString().trim());
        requestParams.put("inventory",num.getText().toString().trim());
        if(!StringUtil.isEmpty(other.getText().toString().trim())){
            requestParams.put("comm_asrc_content", other.getText().toString().trim());
        }
        if(!StringUtil.isEmpty(lnglatString)){
            requestParams.put("lnglat", lnglatString);
        }
        requestParams.put("down_date", date.getText().toString());
        if(!StringUtil.isEmpty(imgString)){
            requestParams.put("main_pic",imgString);
        }
        return requestParams;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CarpoolingApi.API_SEND_CARPOOLING_INFO:
                Toast.makeText(this,"发布成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 显示日期选择框
     */
    @SuppressLint("NewApi")
    public void initDatePickerDialog() {
        dialog = new AlertDialog.Builder(this).create();
        View popView = View.inflate(this, R.layout.pop_datepicker, null);
        dialog.setView(popView, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        popTip = (TextView) popView.findViewById(R.id.date_tip);
        wv_year = (WheelView) popView.findViewById(R.id.year);
        wv_month = (WheelView) popView.findViewById(R.id.month);
        wv_day = (WheelView) popView.findViewById(R.id.day);
        wv_hour = (WheelView) popView.findViewById(R.id.hour);
        wv_min = (WheelView) popView.findViewById(R.id.min);
        ok = (Button) popView.findViewById(R.id.date_sure);
        cancel = (Button) popView.findViewById(R.id.date_cancel);
        popTip.setText("请选择用车时间");
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        int textSize = 0;
        textSize = adjustFontSize(getWindow().getWindowManager());
        // 年
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(mYear - START_YEAR);// 初始化时显示的数据
        // 月
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(mMonth);
        // 日
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(mMonth + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(mMonth + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((mYear % 4 == 0 && mYear % 100 != 0) || mYear % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(mDay - 1);
        //时
        wv_hour.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hour.setLabel("时");
        wv_hour.setCyclic(true);
        wv_hour.setCurrentItem(c.get(Calendar.HOUR_OF_DAY));
        //分
        wv_min.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_min.setLabel("分");
        wv_min.setCyclic(true);
        wv_min.setCurrentItem(c.get(Calendar.MINUTE));
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hour.TEXT_SIZE = textSize;
        wv_min.TEXT_SIZE = textSize;
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkStartDate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 判断开始日期是否符合要求
     */
    public void checkStartDate(){
        //判断选择年是否晚早于当前年
        if (((wv_year.getCurrentItem() + START_YEAR) >= mYear)) {
                //判断选择月是否早于当前月
                if (((wv_month.getCurrentItem()) >= mMonth)) {
                        //判断选择日是否晚于当前日
                        if (((wv_day.getCurrentItem() + 1) >= mDay)) {
                            buffStartDateString();
                        }else{
                            Toast.makeText(SendInfomationActivity.this, "日期选择不合法", Toast.LENGTH_SHORT).show();
                            return;
                        }
                }else{
                    Toast.makeText(SendInfomationActivity.this,"日期选择不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
        }else{
            Toast.makeText(SendInfomationActivity.this,"日期选择不合法", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 整理拼接开始日期字符串
     */
    public void buffStartDateString(){
        StringBuffer startBuff = new StringBuffer();
        startYear = wv_year.getCurrentItem() + START_YEAR;
        startMonth = wv_month.getCurrentItem() + 1;
        startDay = wv_day.getCurrentItem() + 1;
        startHour = wv_hour.getCurrentItem();
        startMin = wv_min.getCurrentItem();
        startBuff.append(startYear+"-");
        if (wv_month.getCurrentItem() + 1<10) {
            startBuff.append("0"+startMonth+"-");
        }else{
            startBuff.append(startMonth+"-");
        }
        if (wv_day.getCurrentItem() + 1<10) {
            startBuff.append("0"+startDay+" ");
        }else{
            startBuff.append(startDay+" ");
        }
        if (wv_hour.getCurrentItem() <10) {
            startBuff.append("0"+startHour+":");
        }else{
            startBuff.append(startHour+":");
        }
        if (wv_min.getCurrentItem() <10) {
            startBuff.append("0"+startMin);
        }else{
            startBuff.append(startMin);
        }
        editStartDate = startBuff.toString();
        date.setText(startBuff.toString());
        dialog.dismiss();
    }

    /**
     * 日期选择添加年监听
     */
    OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year_num = newValue + START_YEAR;
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                if ((year_num % 4 == 0 && year_num % 100 != 0)|| year_num % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
        }
    };

    /**
     *日期选择监听月大小
     */
    OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int month_num = newValue + 1;
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                        .getCurrentItem() + START_YEAR) % 100 != 0)
                        || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
        }
    };

    /**
     * 日期选择框判断适配字体大小
     * @param windowmanager
     * @return
     */
    public static int adjustFontSize(WindowManager windowmanager) {
        int screenWidth = windowmanager.getDefaultDisplay().getWidth();
        int screenHeight = windowmanager.getDefaultDisplay().getHeight();
        if (screenWidth <= 240) { // 240X320 屏幕
            return 10;
        } else if (screenWidth <= 320) { // 320X480 屏幕
            return 14;
        } else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
            return 18;
        } else if (screenWidth <= 540) { // 540X960 屏幕
            return 21;
        } else if (screenWidth <= 800) { // 800X1280 屏幕
            return 24;
        } else { // 大于 800X1280
            return 24;
        }
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(SendInfomationActivity.this);
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 百度地图监听
     * @author Administrator
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if(arg0.getLocType() != BDLocation.TypeNetWorkLocation ){
                Toast.makeText(SendInfomationActivity.this,"获取位置信息失败",Toast.LENGTH_SHORT).show();
            }else{
                lnglatString = String.valueOf(arg0.getLongitude()) +","+ String.valueOf(arg0.getLatitude());
                address = arg0.getAddrStr();
                startAds.setText(address);
            }
            getLocal.setVisibility(View.VISIBLE);
            proLocal.setVisibility(View.GONE);
            mLocationClient.stop();
        }
    }

    /**
     * 返回方法获取到头像
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_Ca://相机获取图片
                if (Bimp.tempSelectBitmap.size() < 5 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocationClient && mLocationClient.isStarted()){
            mLocationClient.stop();
        }
        Bimp.tempSelectBitmap.clear();
        imgAdapter = null;
    }

}
