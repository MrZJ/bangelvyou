package com.yishangshuma.bangelvyou.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.FocusApi;
import com.yishangshuma.bangelvyou.api.ShopDetailApi;
import com.yishangshuma.bangelvyou.entity.ShopDetailAttrEntity;
import com.yishangshuma.bangelvyou.entity.ShopDetailAttrInfoEntity;
import com.yishangshuma.bangelvyou.listener.ShareClickListener;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**景点、酒店、体验、美食预定界面*/
public class AttractionReserveActivity extends BaseActivity implements View.OnClickListener{

    private static final int TIME_REQUEST_CODE = 1;
    private View parentView, reserveTop, reserveCombo, reserveComboSed, rseerveKnow, reserveKnowSed,relLookCombo, popReserveCombo,popTypeItem,downOutTimeLine;
    private WebView comboOrKnowWeb;
    private Button btnLookCombo,btnPopReserve;
    private PopupWindow pop,typePop;
    private TextView choseName,chosePrice,inTimeTitle, choseInTime, outTimeTitle, choseOutTime, inType;
    private ImageView closePop;
    private String detailType, id, imgUrl, price, oldprice, name, inDate, outDate,contentUrl,noticeUrl;
    private List<ShopDetailAttrEntity> attrsList;//商品属性
    private ImageView reserveImg,popReserveImg;
    private TextView reserveName, reservePirce, reserveOldPrice;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout addTypeLine;//显示类型
    private String select_attr;//属性id
    private LinearLayout linAddAttrs;//显示属性的布局
    private HashMap<String, String> attributeCodeMap;//存储用户选择的商品属性code
    private StringBuffer attriString;//所购商品的属性
    private ShareClickListener shareClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_attraction_reserve, null);
        attrsList = (List<ShopDetailAttrEntity>) getIntent().getExtras().getSerializable("attrs");
        detailType = getIntent().getExtras().getString("type");//类型区分酒店、美食、体验、景点
        id = getIntent().getExtras().getString("id");//商品id
        imgUrl = getIntent().getExtras().getString("img_url");
        name = getIntent().getExtras().getString("name");
        price = getIntent().getExtras().getString("price");
        oldprice = getIntent().getExtras().getString("oldprice");
        contentUrl = getIntent().getExtras().getString("content");
        noticeUrl = getIntent().getExtras().getString("notice");
        setContentView(parentView);
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTopRightImg(R.mipmap.top_collect, TopClickUtil.TOP_COL, R.mipmap.top_share);
        if("view".equals(detailType)){
            setTitle("景点");
        }else if("sleep".equals(detailType)){
            setTitle("酒店");
        }else if("food".equals(detailType)){
            setTitle("美食");
        }else if("live".equals(detailType)){
            setTitle("体验");
        }else{
            setTitle("预定");
        }
        reserveTop = findViewById(R.id.reserve_top);
        reserveCombo = findViewById(R.id.rel_reserve_combo);
        rseerveKnow = findViewById(R.id.rel_reserve_know);
        reserveComboSed = findViewById(R.id.view_reserve_combo_select);
        reserveKnowSed = findViewById(R.id.view_reserve_know_select);
        reserveImg = (ImageView) findViewById(R.id.img_reserve_goodspic);
        reserveName = (TextView) findViewById(R.id.tv_reserve_name);
        reservePirce = (TextView) findViewById(R.id.tv_reserve_price);
        reserveOldPrice = (TextView) findViewById(R.id.tv_reserve_old_price);
        reserveOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        comboOrKnowWeb = (WebView) findViewById(R.id.webview_reserve);
        WebSettings webSettings = comboOrKnowWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(false);
        relLookCombo = findViewById(R.id.rel_reserve_lookcombo);
        btnLookCombo = (Button) findViewById(R.id.btn_reserve_lookcombo);
        pop = new PopupWindow(this);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        popReserveCombo = getLayoutInflater().inflate(R.layout.pop_reserve_combo,null);
        popReserveImg = (ImageView) popReserveCombo.findViewById(R.id.img_pop_reserve);
        choseName = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_name);
        choseName.setText(name);
        chosePrice = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_price);
        chosePrice.setText("￥"+price);
        inTimeTitle = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_intime);//入住时间标题
        choseInTime = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_choseintime);//选择入住时间
        outTimeTitle = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_outtime);//离店时间标题
        choseOutTime = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_choseouttime);//选择离店时间
        downOutTimeLine = popReserveCombo.findViewById(R.id.view_pop_reserve_bottomouttime);//选择离店底部的线
        linAddAttrs = (LinearLayout) popReserveCombo.findViewById(R.id.lin_pop_reserve_addattr);//显示属性的布局
        closePop = (ImageView) popReserveCombo.findViewById(R.id.img_pop_reserve_close);
//        inType = (TextView) popReserveCombo.findViewById(R.id.tv_pop_reserve_chosetype);
        btnPopReserve = (Button) popReserveCombo.findViewById(R.id.btn_pop_reserve);
        if(!"sleep".equals(detailType)){
            if("live".equals(detailType) || "view".equals(detailType)){
                inTimeTitle.setText("到达时间");
            }
            outTimeTitle.setVisibility(View.GONE);
            choseOutTime.setVisibility(View.GONE);
            downOutTimeLine.setVisibility(View.GONE);
        }
        pop.setContentView(popReserveCombo);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        reserveCombo.setOnClickListener(this);
        rseerveKnow.setOnClickListener(this);
        comboOrKnowWeb.setWebViewClient(new WebViewClient() {
            //点击网页中按钮时，让其还在原页面打开
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        relLookCombo.setOnClickListener(this);
        btnLookCombo.setOnClickListener(this);
        btnPopReserve.setOnClickListener(this);
        closePop.setOnClickListener(this);
//        inType.setOnClickListener(this);
        choseInTime.setOnClickListener(this);
        choseOutTime.setOnClickListener(this);
        shareClickListener = new ShareClickListener(this);
        setShareNameAndImg();
        rightTwo.setOnClickListener(shareClickListener);
    }

    /**
     * 设置分享文字和图片
     */
    private void setShareNameAndImg(){
        if(shareClickListener != null){
            if(null != name){
                shareClickListener.setShareImgUrl(imgUrl);
                shareClickListener.setContent(name);
            }
        }
    }

    private void setData(){
        Bitmap imageRes = imageLoader.loadImageSync(imgUrl);
        if(null != imageRes){
            reserveImg.setImageBitmap(imageRes);
            popReserveImg.setImageBitmap(imageRes);
        }
//        imageLoader.displayImage(imgUrl, reserveImg, AsynImageUtil.getImageOptions(), null);
//        imageLoader.displayImage(imgUrl, popReserveImg, AsynImageUtil.getImageOptions(), null);
        reserveName.setText(name);
        reservePirce.setText("￥" + price);
        if(null == oldprice || "0.00".equals(oldprice)){
            reserveOldPrice.setVisibility(View.GONE);
        }else{
            reserveOldPrice.setText(oldprice);
        }
        selectItemAndWeb("combo", contentUrl);
        if(!ListUtils.isEmpty(attrsList)){
            attributeCodeMap = new HashMap<String, String>();
            showGoodSonAttr(attrsList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_reserve_combo:
                selectItemAndWeb("combo",contentUrl);//套餐内容
                break;
            case R.id.rel_reserve_know:
                selectItemAndWeb("know",noticeUrl);//消费需知
                break;
            case R.id.rel_reserve_lookcombo:
                pop.showAsDropDown(reserveTop);
                break;
            case R.id.btn_reserve_lookcombo:
                pop.showAsDropDown(reserveTop);
                break;
            case R.id.btn_pop_reserve:
                buyGoodNow();
                break;
            case R.id.img_pop_reserve_close:
                pop.dismiss();
                break;
//            case R.id.tv_pop_reserve_chosetype:
//                showTypePop();
//                break;
            case R.id.tv_pop_reserve_choseintime:
            case R.id.tv_pop_reserve_choseouttime:
                Intent timeIntent = new Intent(AttractionReserveActivity.this, SelectTimeActivity.class);
                if("sleep".equals(detailType)){
                    timeIntent.putExtra("type","2");//酒店选择两个时间
                }else{
                    timeIntent.putExtra("type","1");//只选择一个时间
                }
                startActivityForResult(timeIntent, TIME_REQUEST_CODE);
                break;

        }
    }

    /**设置套餐和需知选择背景*/
    private void selectItemAndWeb(String item,String webHttp){
        if("combo".equals(item)){
            reserveComboSed.setVisibility(View.VISIBLE);
            reserveKnowSed.setVisibility(View.INVISIBLE);
        }else{
            reserveComboSed.setVisibility(View.INVISIBLE);
            reserveKnowSed.setVisibility(View.VISIBLE);
        }
        comboOrKnowWeb.loadUrl(webHttp);
    }

    /**
     * 动态添加商品子属性
     * 并进行选择
     */
    private void showGoodSonAttr(List<ShopDetailAttrEntity> attrList){
        if (attrList != null && !ListUtils.isEmpty(attrList)) {
            for (int i = 0; i < attrList.size(); i++) {
                View attriView = LayoutInflater.from(this).inflate(R.layout.pop_type_chose, null);
                TextView attributeName = (TextView) attriView.findViewById(R.id.tv_pop_type_chose_typename);
                final TextView chosedAttri = (TextView) attriView.findViewById(R.id.tv_pop_type_chose_type);
                linAddAttrs.addView(attriView);
                final String attriName = attrList.get(i).attr_name;
                attributeName.setText(attriName);
                chosedAttri.setText("请选择"+attriName);
                attributeCodeMap.put(attriName, null);
                List<ShopDetailAttrInfoEntity> sonAttriLis = attrList.get(i).son_attr;
                final String[] sonAttriName = new String[sonAttriLis.size()];
                final String[] sonAttriCode = new String[sonAttriLis.size()];
                for (int j = 0; j < sonAttriLis.size(); j++) {
                    sonAttriName[j] = sonAttriLis.get(j).son_name;
                    sonAttriCode[j] = sonAttriLis.get(j).son_id;
                }
                chosedAttri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AttractionReserveActivity.this);
                        builder.setItems(sonAttriName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                chosedAttri.setText(sonAttriName[arg1]);
                                attributeCodeMap.put(attriName, sonAttriCode[arg1]);
                                goodInvAndPrice();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
    }

    /**
     * 商品立即购买
     */
    private void buyGoodNow()
    {
        Intent buyGoodIntent = new Intent(AttractionReserveActivity.this,BuyLocationGoodWebActivity.class);
        buyGoodIntent.putExtra("id",id);
        if(null == inDate || "".equals(inDate)){
            Toast.makeText(this,"请选时间",Toast.LENGTH_SHORT).show();
            return;
        }else {
            buyGoodIntent.putExtra("inDate",inDate);
            if(null != outDate && "".equals(outDate)){
                buyGoodIntent.putExtra("outDate",outDate);
            }
        }
        if(null != attributeCodeMap){
            Iterator attriCodeIte = attributeCodeMap.entrySet().iterator();
            List<String> codeList = new ArrayList<String>();
            while (attriCodeIte.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) attriCodeIte.next();
                if (entry.getValue()==null) {
                    Toast.makeText(this, "请选择"+entry.getKey(), Toast.LENGTH_SHORT).show();
                    codeList.clear();
                    return;
                }else{
                    codeList.add(entry.getValue());
                }
            }
            String attrSonId = getAttrSonIdString(codeList);
            buyGoodIntent.putExtra("select_attr",attrSonId);
        }
        pop.dismiss();
        startActivity(buyGoodIntent);
    }

    /**
     * 请求商品库存和套餐价格
     * 以及运费
     */
    private void goodInvAndPrice()
    {
        boolean getInvPrice = true;
        Iterator attriCodeIte = attributeCodeMap.entrySet().iterator();
        List<String> codeList = new ArrayList<String>();
        while (attriCodeIte.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) attriCodeIte.next();
            if (entry.getValue() == null) {
                getInvPrice = false;
            } else {
                codeList.add(entry.getValue());
            }
        }
        if (getInvPrice) {
            String attrSonId = getAttrSonIdString(codeList);
            getGoodInvAndPrice(attrSonId);
        }
    }

    /**
     * 整理商品子属性字符串
     *
     */
    private String getAttrSonIdString(List<String> codeList){
        attriString = new StringBuffer();
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.size() == 0) {
                attriString.append(codeList.get(i));
            } else if (i == codeList.size()-1){
                attriString.append(codeList.get(i));
            } else {
                attriString.append(codeList.get(i)+",");
            }
        }
        return attriString.toString();
    }

    /**
     * 请求商品库存和价格
     */
    private void getGoodInvAndPrice(String attrs)
    {
        httpPostRequest(ShopDetailApi.getGoodInvAndPrice(), getRequestParams(attrs), ShopDetailApi.API_POST_GOOD_INV_PRICE);
    }

    /**
     * 整理参数
     * 商品ID|套餐ID
     */
    private HashMap getRequestParams(String attrs){
        HashMap<String,String> params=new HashMap<>();
        params.put("comm_id", id);
        params.put("select_attr", attrs);
        return params;
    }

    /**
     * 显示类别弹出框
     */
    private void showTypePop(){
//        typePop = new PopupWindow(this);
//        typePop.setFocusable(true);
//        typePop.setOutsideTouchable(true);
//        typePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        typePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        typePop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
//        popTypeItem = getLayoutInflater().inflate(R.layout.pop_type_chose,null);
//        addTypeLine = (LinearLayout) popTypeItem.findViewById(R.id.line_type_chose);
//        if(!ListUtils.isEmpty(attrsList)){
//            showTypeAttr(attrsList.get(0).son_attr);
//        }
    }

    /**
     * 动态添加显示类别
     */
    private void showTypeAttr(List<ShopDetailAttrInfoEntity> attrList){
        if (attrList != null && !ListUtils.isEmpty(attrList)) {
            final HashMap<String,String> tagMap = new HashMap<>();
            for (int i = 0; i < attrList.size(); i++) {
                View attriView = LayoutInflater.from(this).inflate(R.layout.item_type_chose, null);
                TextView attributeName = (TextView) attriView.findViewById(R.id.tv_type_name);
                addTypeLine.addView(attriView);
                String attriName = attrList.get(i).son_name;
                attributeName.setText(attriName);
                attributeName.setTag(attrList.get(i).son_id);
                tagMap.put(attrList.get(i).son_id,attrList.get(i).son_name);
                attributeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_attr = v.getTag().toString();
                        inType.setText(tagMap.get(v.getTag().toString()));
                        typePop.dismiss();
                    }
                });
            }
            typePop.setContentView(popTypeItem);
            typePop.showAsDropDown(reserveTop);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TIME_REQUEST_CODE:
                    inDate = data.getExtras().getString("inDate");
                    choseInTime.setText(inDate);
                    if("sleep".equals(detailType)){
                        outDate = data.getExtras().getString("outDate");
                        choseOutTime.setText(outDate);
                    }
                    break;
            }
        }
    }
    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case FocusApi.API_GET_ADD_FOCUS://收藏
                addCollectHander(json);
                break;
            case ShopDetailApi.API_POST_GOOD_INV_PRICE://商品库存和价格
                goodInvAndPriceHander(json);
                break;
            default:
                break;
        }
    }
    /**
     * 收藏
     */
    public void collect(){
        if(configEntity.isLogin){
            addCollect();
        }else{
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    /**
     * 获取添加收藏数据信息
     *
     */
    private void addCollect() {
        httpPostRequest(FocusApi.getAddFocusUrl(), getFocusRequestParams(),
                FocusApi.API_GET_ADD_FOCUS);
    }

    /**
     * 获取添加收藏参数
     *
     */
    private HashMap<String,String> getFocusRequestParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("goods_id", id);
        return params;
    }

    /**
     * 显示收藏完成的信息
     * @param json
     */
    public void addCollectHander(String json){
        String msg = JSON.parseObject(json).getString("msg");
        if(null != msg && !"".equals(msg)){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理商品套餐价格和库存
     */
    private void goodInvAndPriceHander(String json){
        if (null != json && !"".equals(json) && !"{}".equals(json.trim())) {
            if (null != JSON.parseObject(json)) {
                String pd_price=JSON.parseObject(json).getString("pd_price");
                chosePrice.setText("￥"+pd_price);
            }
        }
    }

}
