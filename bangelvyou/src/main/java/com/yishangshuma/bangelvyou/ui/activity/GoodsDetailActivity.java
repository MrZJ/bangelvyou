package com.yishangshuma.bangelvyou.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.TopOneAdapter;
import com.yishangshuma.bangelvyou.api.CartApi;
import com.yishangshuma.bangelvyou.api.FocusApi;
import com.yishangshuma.bangelvyou.api.ShopDetailApi;
import com.yishangshuma.bangelvyou.entity.CanBuyEntity;
import com.yishangshuma.bangelvyou.entity.CommentInfoEntity;
import com.yishangshuma.bangelvyou.entity.EntpInfoEntity;
import com.yishangshuma.bangelvyou.entity.GoodsCanBuyEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailAttrEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailAttrInfoEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailImgEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailInfoEntity;
import com.yishangshuma.bangelvyou.entity.RegionAddressEntity;
import com.yishangshuma.bangelvyou.entity.TopInfo;
import com.yishangshuma.bangelvyou.listener.PageChangeListener;
import com.yishangshuma.bangelvyou.listener.ShareClickListener;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.util.StringUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;
import com.yishangshuma.bangelvyou.wibget.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 商品详情界面
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener,TopOneAdapter.OnModelClickListener,ViewPager.OnPageChangeListener {

    private View tab_1, goodInfo,goodComment, chosenColorClass,commentLine, lin_into_class, lin_into_shop;
    private TextView tab_main_text_1, btn_buyNow, btn_cart, goodPrice,
            goodType, commentedNum,shopName;
    private ImageView tab_main_img_1,shopIcon;
    private AutoScrollViewPager pager;
    private TopOneAdapter mAdAdapter;
    private PageChangeListener pageChangeListener;
    private ShareClickListener shareClickListener;
    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.heart_grey,R.mipmap.heart_red };
    // Tab选项卡的文字
    private String mTextviewArray[] = { "收藏" ,"已收藏"};
    private String goods_id, imageUrl, goods_name;
    private boolean ifOrderGoods, finishThis, finishNow;// 是否是订单商品详情
    private View choseView;//选择商品规格视图
    private PopupWindow pop;
    private View parentView,LinearLayoutAddress;
    private ImageView closePop,subNum,addNum,imgChosePop;//取消弹窗的按钮|减少商品数量|增加商品数量|商品第一张图片
    private TextView userAddress,goodNum,goodFreight,popGoodPrice,popGoodStock,popBuyNow,popAddCart;
    private int cartNum=1;
    private static final int SET_ADDRESS=0;
    private static final int USER_LOGIN=1;
    private List<RegionAddressEntity> regionAddresslist=new ArrayList<RegionAddressEntity>();
    private LinearLayout indicatorLayout;//图片位置指示
    private LinearLayout LinearLayoutGoodAttribute;//商品属性动态添加布局
    private ImageView[] indicators = null;
    private GoodsDetailInfoEntity detail;//商品详情实体
    private List<TopInfo> topInfos;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private HashMap<String, String> attributeCodeMap;//存储用户选择的商品属性code
    private CanBuyEntity canBuyEnity;//是否能购买实体类
    private String buyType;//购买动作类型（立即购买|加入购物车）
    private StringBuffer attriString;//所购商品的属性
    private boolean realInCart=false;
    private int width;
    private LinearLayout commentLinearLayout;//评论列表
    private String addressCode;//用户送货区域编码
    private boolean realBuy = false;//判断运费获取是否来自立即购买和加入购物车按钮的点击事件
    private HashMap<Integer,Boolean> isShowCommentAll;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_goods_detail,null);
        setContentView(parentView);
//        showLoadingDialog();
        goods_id = getIntent().getExtras().getString("goods_id");
        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        width = dm.widthPixels;//宽度
        initView();
        initListener();
        getGoodDetail();
    }

    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("商品详情");
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHA);
        tab_1 = (View) findViewById(R.id.view_goods_detail_collect);
        tab_main_text_1 = (TextView) tab_1.findViewById(R.id.textview);
        tab_main_img_1 = (ImageView) tab_1.findViewById(R.id.imageview);
        btn_buyNow = (TextView) findViewById(R.id.tv_goods_detail_buynow);
        btn_cart = (TextView) findViewById(R.id.tv_goods_detail_addcart);
        pager = (AutoScrollViewPager) findViewById(R.id.viewpager_goods_detail_pager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pager.getLayoutParams();
        params.height = width;
        indicatorLayout = (LinearLayout) findViewById(R.id.line_goods_detail_indicator);
        goodPrice = (TextView) findViewById(R.id.tv_goods_detail_goodsprice);
        goodType = (TextView) findViewById(R.id.tv_goods_detail_goodsname);
        goodInfo = (View) findViewById(R.id.rel_goods_detail_goodsdetail);
        chosenColorClass = (View) findViewById(R.id.rel_goods_detail_goodsclass);
        goodComment = (View) findViewById(R.id.rel_goods_detail_comments);
        commentedNum = (TextView) findViewById(R.id.tv_goods_detail_commentsnum);
        commentLine = (View) findViewById(R.id.view_goods_detail_havecomment);
        commentLinearLayout = (LinearLayout) findViewById(R.id.line_goods_detail_commentlist);
        shopIcon = (ImageView) findViewById(R.id.img_shop_icon);
        shopName = (TextView) findViewById(R.id.tv_shop_name);
        lin_into_class = (View) findViewById(R.id.lin_into_class);
        lin_into_shop = (View) findViewById(R.id.lin_into_shop);

        //商品选择规格界面
        pop = new PopupWindow(this);
        choseView = getLayoutInflater().inflate(R.layout.pop_chose_goods_attrs,null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.style.Animation_pop);
        pop.setContentView(choseView);
        imgChosePop = (ImageView) choseView.findViewById(R.id.img_chose_GoodImage);
        popGoodPrice= (TextView) choseView.findViewById(R.id.tv_pop_goodprice);
        popGoodStock= (TextView) choseView.findViewById(R.id.tv_pop_goodstock);
        goodFreight = (TextView) choseView.findViewById(R.id.tv_pop_goodfreight);
        closePop = (ImageView) choseView.findViewById(R.id.close_chose);
        LinearLayoutAddress = (View) choseView.findViewById(R.id.ll_chose_address);
        userAddress = (TextView) choseView.findViewById(R.id.tv_pop_choseaddress);
        LinearLayoutGoodAttribute = (LinearLayout) choseView.findViewById(R.id.ll_add_good_attribute);
        subNum = (ImageView) choseView.findViewById(R.id.chose_cartMinus);
        goodNum = (TextView) choseView.findViewById(R.id.chose_cartNum);
        addNum = (ImageView) choseView.findViewById(R.id.chose_cartPlus);
        popBuyNow = (TextView) choseView.findViewById(R.id.pop_btn_buynow);
        popAddCart = (TextView) choseView.findViewById(R.id.pop_btn_cart);
        //修改tab数据
        tab_main_text_1.setText(mTextviewArray[0]);
        tab_main_text_1.setTextColor(this.getResources().getColor(
                R.color.main_text_color));
        tab_main_img_1.setBackgroundResource(mImageViewArray[0]);
    }

    /**
     * 设置商品数据数据
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setData() {
        if (detail!=null) {
            shareClickListener = new ShareClickListener(this);
            setShareNameAndImg();
            rightOne.setOnClickListener(shareClickListener);
            goodPrice.setText("￥"+detail.comm_price);
            goodType.setText(detail.comm_name);
            goods_name = detail.comm_name;
            commentedNum.setText("晒单评价("+detail.commentCount+")");
            List<GoodsDetailImgEntity> imgEntity = detail.comm_image_url;
            EntpInfoEntity entpInfo = detail.entpInfo;
            imageLoader.displayImage(entpInfo.entp_logo, shopIcon);
            shopName.setText(entpInfo.entp_name);
            attributeCodeMap = new HashMap<String, String>();
            if (!ListUtils.isEmpty(imgEntity)) {
                imageUrl = imgEntity.get(0).comm_image_url;
                topInfos = new ArrayList<TopInfo>();
                for (int i = 0; i < imgEntity.size(); i++) {
                    TopInfo topInfo = new TopInfo();
                    topInfo.pciture = imgEntity.get(i).comm_image_url;
                    topInfos.add(topInfo);
                }
                indicators = new ImageView[topInfos.size()]; // 定义指示器数组大小
                initIndicator();
                mAdAdapter = new TopOneAdapter(this, "");
                mAdAdapter.setData(topInfos);
                mAdAdapter.setInfiniteLoop(true);
                pager.setAdapter(mAdAdapter);
                pager.setAutoScrollDurationFactor(10);
                pager.setInterval(2000);
                pager.startAutoScroll();
                mAdAdapter.setOnModelClickListener(this);
                if(topInfos !=null){
                    pager.setCurrentItem(topInfos.size());
                }
                imageLoader.displayImage(imgEntity.get(0).comm_image_url, imgChosePop);
            }
            List<CommentInfoEntity> commentInfoList = detail.commentInfoList;
            if (ListUtils.isEmpty(commentInfoList)) {
                addCommentToView(commentInfoList);
            }
            popGoodPrice.setText("￥"+detail.comm_price);
            popGoodStock.setText(detail.pd_stock);
            if (configEntity.kuaiAddress != null && !"".equals(configEntity.kuaiAddress)) {
                userAddress.setText(configEntity.kuaiAddress);
                goodFreight.setText("暂无运费信息");
            } else {
                goodFreight.setText("请选择配送地址");
            }
            if (configEntity.kuaiAddressCode != null && !"".equals(configEntity.kuaiAddressCode)) {
                addressCode = configEntity.kuaiAddressCode;
            }
            List<GoodsDetailAttrEntity> attrList=detail.attr_arrs;
            showGoodSonAttr(attrList);
        }

    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        tab_1.setOnClickListener(this);
        btn_buyNow.setOnClickListener(this);
        btn_cart.setOnClickListener(this);
        pager.setOnPageChangeListener(this);
        goodInfo.setOnClickListener(this);
        goodComment.setOnClickListener(this);
        chosenColorClass.setOnClickListener(this);
        closePop.setOnClickListener(this);
        LinearLayoutAddress.setOnClickListener(this);
        subNum.setOnClickListener(this);
        addNum.setOnClickListener(this);
        popBuyNow.setOnClickListener(this);
        popAddCart.setOnClickListener(this);
        lin_into_class.setOnClickListener(this);
        lin_into_shop.setOnClickListener(this);
    }

    /**
     * 显示商品评论
     * @param commentInfoList
     */
    private void addCommentToView(List<CommentInfoEntity> commentInfoList){
        isShowCommentAll = new HashMap<Integer,Boolean>();
        for(int i = 0; i < detail.commentInfoList.size(); i++){
            CommentInfoEntity commentInfoEntity = detail.commentInfoList.get(i);
            isShowCommentAll.put(i,true);
            View commentItem = LayoutInflater.from(this).inflate(R.layout.item_tourist_comment, null);
            View commentShowAll = commentItem.findViewById(R.id.item_comment_tourist_more);
            final TextView commentContent = (TextView) commentItem.findViewById(R.id.item_comment_tourist_content);
            final TextView tvCommentLookMore = (TextView) commentItem.findViewById(R.id.item_comment_tourist_more_tv);
            final ImageView imgCommentLookMore = (ImageView) commentItem.findViewById(R.id.item_comment_tourist_more_img);
            TextView item_comment_tourist_name = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_name);
            TextView item_comment_tourist_level = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_level);
            TextView item_comment_tourist_date = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_date);
            RatingBar item_comment_tourist_rating = (RatingBar) commentItem
                    .findViewById(R.id.item_comment_tourist_rating);
            ImageView item_comment_tourist_icon = (ImageView) commentItem.findViewById(R.id.item_comment_tourist_icon);
            final int finalI = i;
            commentShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentShowOrHideText(finalI,commentContent,tvCommentLookMore,imgCommentLookMore);
                }
            });
            commentContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentShowOrHideText(finalI,commentContent,tvCommentLookMore,imgCommentLookMore);
                }
            });
            imageLoader.displayImage(commentInfoEntity.comment_user_icon, item_comment_tourist_icon,
                    AsynImageUtil.getImageOptions(R.mipmap.tourist_icon), null);
            commentContent.setText(commentInfoEntity.comment_content);
            item_comment_tourist_name.setText(commentInfoEntity.comment_user_name);
            item_comment_tourist_level.setText(commentInfoEntity.comment_user_level);
            item_comment_tourist_date.setText(commentInfoEntity.comment_date);
            item_comment_tourist_rating.setRating(StringUtil.stringToInt(commentInfoEntity.comment_level));
            commentLinearLayout.addView(commentItem);
        }
    }

    /**TextView展开或只显示二行文字*/
    private void commentShowOrHideText(int indexIsShow,TextView text,TextView tvMore,ImageView imgMore){
        if (isShowCommentAll.get(indexIsShow)){
            isShowCommentAll.put(indexIsShow,false);
            text.setEllipsize(null);
            text.setSingleLine(false);
            tvMore.setText("收起");
            imgMore.setBackgroundResource(R.mipmap.comment_lookmore_up);
        }else{
            isShowCommentAll.put(indexIsShow,true);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setMaxLines(2);
            tvMore.setText("全部");
            imgMore.setBackgroundResource(R.mipmap.comment_lookmore);
        }
    }

    /**
     * 动态添加商品子属性
     * 并进行选择
     */
    private void showGoodSonAttr(List<GoodsDetailAttrEntity> attrList){
        if (attrList != null && !ListUtils.isEmpty(attrList)) {
            for (int i = 0; i < attrList.size(); i++) {
                View attriView = LayoutInflater.from(this).inflate(R.layout.item_good_attribute, null);
                TextView attributeName = (TextView) attriView.findViewById(R.id.tv_good_attribute_name);
                final TextView chosedAttri = (TextView) attriView.findViewById(R.id.chose_attribute);
                LinearLayout choseLinear = (LinearLayout) attriView.findViewById(R.id.ll_chose_attribute);
                LinearLayoutGoodAttribute.addView(attriView);
                final String attriName = attrList.get(i).attr_name;
                attributeName.setText(attriName);
                chosedAttri.setText("请选择"+attriName);
                attributeCodeMap.put(attriName, null);
                List<GoodsDetailAttrInfoEntity> sonAttriLis = attrList.get(i).son_attr;
                final String[] sonAttriName = new String[sonAttriLis.size()];
                final String[] sonAttriCode = new String[sonAttriLis.size()];
                for (int j = 0; j < sonAttriLis.size(); j++) {
                    sonAttriName[j] = sonAttriLis.get(j).son_name;
                    sonAttriCode[j] = sonAttriLis.get(j).son_id;
                }
                choseLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsDetailActivity.this);
                        builder.setItems(sonAttriName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                chosedAttri.setText(sonAttriName[arg1]);
                                attributeCodeMap.put(attriName,sonAttriCode[arg1]);
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
//            showLoadingDialog();
            getGoodInvAndPrice(attrSonId);
            if (addressCode != null && !"".equals(addressCode)) {
                realBuy = false;
                getGoodYFInfo(attrSonId);
            }
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
     * 请求商品运费信息
     */
    private void getGoodYFInfo(String attrSonId) {
        httpPostRequest(ShopDetailApi.getGoodCanBuyUrl(), getRequestParams(detail.comm_id,addressCode, String.valueOf(cartNum), attrSonId), ShopDetailApi.API_POST_GOOD_CAN_BUY_LIST);
    }

    /**
     * 请求商品库存和价格
     */
    private void getGoodInvAndPrice(String attrs)
    {
        httpPostRequest(ShopDetailApi.getGoodInvAndPrice(), getRequestParams(attrs), ShopDetailApi.API_POST_GOOD_INV_PRICE);
    }

    /**
     * 设置分享文字和图片
     */
    private void setShareNameAndImg(){
        if(shareClickListener != null){
            if(null != detail){
                shareClickListener.setShareImgUrl(detail.comm_image_url.get(0).comm_image_url);
                shareClickListener.setContent(detail.comm_name);
            }
        }
    }

    /**
     * 请求商品详情
     */
    private void getGoodDetail()
    {
        httpGetRequest(ShopDetailApi.getPushOrderListUrl(goods_id, ConfigUtil.phoneIMEI), ShopDetailApi.API_GET_MY_ORDER_LIST);
    }

    /**
     * 检查是否商品能购买
     * 0：立即购买|1：加入购物车
     */
    private void allAttriRealChosed()
    {
        if (addressCode == null || "".equals(addressCode)) {
            Toast.makeText(this, "请选择配送地址", Toast.LENGTH_SHORT).show();
            return;
        }
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
        getGoodYFInfo(attrSonId);
    }

    /**
     * 整理参数
     * 商品ID|地区编码|购买数量|套餐ID
     */
    private HashMap getRequestParams(String comm_id,String p_index,String buy_count,String select_attr){
        HashMap<String,String> params=new HashMap<>();
        params.put("comm_id", comm_id);
        params.put("p_index", p_index);
        params.put("buy_count", buy_count);
        params.put("select_attr", select_attr);
        return params;
    }

    /**
     * 整理参数
     * 商品ID|套餐ID
     */
    private HashMap getRequestParams(String attrs){
        HashMap<String,String> params=new HashMap<>();
        params.put("comm_id", goods_id);
        params.put("select_attr",attrs);
        return params;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.rel_goods_detail_goodsdetail://商品描述
                if (detail != null) {
                    Intent describeIntent = new Intent(GoodsDetailActivity.this,GoodsDesribieActivity.class);
                    describeIntent.putExtra("comm_id", detail.comm_id);
                    startActivity(describeIntent);
                }
                break;
            case R.id.rel_goods_detail_comments://商品评论
                if (detail != null) {
                    Intent commentIntent = new Intent(GoodsDetailActivity.this,CommentWebActivity.class);
                    commentIntent.putExtra("comm_id", detail.comm_id);
                    startActivity(commentIntent);
                }
                break;
            case R.id.view_goods_detail_collect:// 收藏
                if (configEntity.isLogin) {
                    addFocus();
                } else {
                    startActivityForResult(intent, USER_LOGIN);
                }
                break;
            case R.id.tv_goods_detail_buynow:// 立即购买
                if (configEntity.isLogin) {
                    if (detail != null) {
                        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    }else{
                        Toast.makeText(this, "立即购买失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivityForResult(intent, USER_LOGIN);
                }
                break;
            case R.id.tv_goods_detail_addcart:// 加入购物车
                if (configEntity.isLogin) {
                    if (detail != null) {
                        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    }else{
                        Toast.makeText(this, "加入购物车失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivityForResult(intent, USER_LOGIN);
                }
                break;
            case R.id.rel_goods_detail_goodsclass://选择套餐
                if (configEntity.isLogin) {
                    if (detail!= null) {
                        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    }
                } else {
                    startActivityForResult(intent, USER_LOGIN);
                }
                break;
            case R.id.close_chose:
                pop.dismiss();
                break;
            case R.id.ll_chose_address://选择地址
                startActivityForResult(new Intent(GoodsDetailActivity.this, SelectAddressActivity.class), SET_ADDRESS);
                break;
            case R.id.chose_cartMinus://减少商品数量
                if (cartNum>1) {
                    cartNum -= 1;
                    goodNum.setText(String.valueOf(cartNum));
                }
                break;
            case R.id.chose_cartPlus://增减商品数量
                cartNum += 1;
                goodNum.setText(String.valueOf(cartNum));
                break;
            case R.id.pop_btn_buynow://立即购买
                buyType = "buyNow";
                realBuy = true;
                allAttriRealChosed();
                break;
            case R.id.pop_btn_cart://加入购物车
                buyType = "addCart";
                realBuy = true;
                allAttriRealChosed();
                break;
            case R.id.lin_into_shop://店铺
//                if (detail != null) {
//                    if (detail.entpInfo != null) {
//                        Intent storeIntent = new Intent(ShopDetailActivity.this, StoreActivity.class);
//                        EntpInfoEntity entpInfo = detail.entpInfo;
//                        storeIntent.putExtra("storeId", entpInfo.entp_id);
//                        storeIntent.putExtra("storeName", entpInfo.entp_name);
//				   /* if (finishNow) {
//			    		startActivity(storeIntent);
//						ShopDetailActivity.this.finish();
//						return;
//					}
//			    	if (finishThis) {
//						ShopDetailActivity.this.finish();
//						return;
//					}*/
//                        startActivity(storeIntent);
//                    }
//                }
                break;
            case R.id.lin_into_class://店铺分类
//                if (detail != null) {
//                    if (detail.entpInfo != null) {
//                        Intent classIntent = new Intent(ShopDetailActivity.this, StoreClassActivity.class);
//                        EntpInfoEntity entpInf = detail.entpInfo;
//                        classIntent.putExtra("storeId", entpInf.entp_id);
//		    	    /*if (finishNow) {
//						ShopDetailActivity.this.finish();
//						return;
//					}
//			    	if (finishThis) {
//			    		startActivity(classIntent);
//						return;
//					}*/
//                        startActivity(classIntent);
//                    }
//                }
                break;
        }
    }

    /**
     * 初始化指示器图片
     */
    private void initIndicator(){
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            if(i == 0){
                indicators[i].setBackgroundResource(R.mipmap.indicators_right_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
            indicatorLayout.addView(indicators[i]);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicators[i].getLayoutParams();
            if(i!= 0){
                layoutParams.leftMargin = 20;
            }
            layoutParams.bottomMargin = 5;
            layoutParams.height = 18;
            layoutParams.width = 14;
            indicators[i].setLayoutParams(layoutParams);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case SET_ADDRESS:
                    regionAddresslist = (ArrayList<RegionAddressEntity>) data.getExtras()
                            .getSerializable("address_list");
                    if(!ListUtils.isEmpty(regionAddresslist)){
                        StringBuffer address = new StringBuffer();
                        address.append(regionAddresslist.get(0).area_name)
                                .append(regionAddresslist.get(1).area_name)
                                .append(regionAddresslist.get(2).area_name);
                        userAddress.setText(address);
                        configEntity.kuaiAddress = address.toString();
                        addressCode = regionAddresslist.get(1).area_id;
                        configEntity.kuaiAddressCode = addressCode;
                        ConfigUtil.saveConfig(this, configEntity);
                        goodInvAndPrice();
                    }
                    break;
                case USER_LOGIN:

                    break;
            }
        }

    }

    /**
     * 获取添加购物车数据信息
     *
     */
    private void addCart() {
        httpPostRequest(CartApi.getAddCartUrl(), getRequestParams(),
                CartApi.API_GET_ADD_CART);
    }

    /**
     * 获取添加购物车参数
     *
     */
    private HashMap getRequestParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("pd_count", String.valueOf(cartNum));
        params.put("comm_id",detail.comm_id);
        params.put("pd_price",detail.comm_price);
        params.put("select_attr",attriString.toString());
        params.put("fre_id", detail.fre_id);
        return params;
    }

    /**
     * 获取添加收藏数据信息
     *
     */
    private void addFocus() {
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
        params.put("goods_id", goods_id);
        return params;
    }

    /**
     * 显示收藏完成的信息
     * @param json
     */
    public void addCollectHander(String json){
        String msg = JSON.parseObject(json).getString("msg");
        if(null != msg && !"".equals(msg)){
            Toast.makeText(GoodsDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            tab_main_img_1.setBackgroundResource(mImageViewArray[1]);
            tab_1.setClickable(false);
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShopDetailApi.API_GET_MY_ORDER_LIST:
                goodDetailHander(json);
                break;
            case ShopDetailApi.API_POST_GOOD_CAN_BUY_LIST:
                goodCanBuyHander(json);
                break;
            case FocusApi.API_GET_ADD_FOCUS://收藏
                addCollectHander(json);
                break;
            case ShopDetailApi.API_POST_GOOD_INV_PRICE://商品库存和价格
                goodInvAndPriceHander(json);
                break;
            case CartApi.API_GET_ADD_CART:
                Toast.makeText(this, "添加购物车成功", Toast.LENGTH_SHORT).show();
                realInCart = true;
                if ("buyNow".equals(buyType)) {
                    realInCart = false;
                    startActivity(new Intent(this,CartActivity.class));
                    pop.dismiss();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理商品详情信息
     * @param json
     */
    private void goodDetailHander(String json){
        GoodsDetailEntity shopDetailEntity = JSON.parseObject(json, GoodsDetailEntity.class);
        if (null == shopDetailEntity) {
            Toast.makeText(this, "获取数据失败",  Toast.LENGTH_SHORT).show();
        }else{
            if (null == shopDetailEntity.comm_info) {
                Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }else{
                detail = shopDetailEntity.comm_info;
                setData();
            }
        }

    }

    /**
     * 处理是否能买信息
     */
    private void goodCanBuyHander(String json){
        GoodsCanBuyEntity scEntity = JSON.parseObject(json, GoodsCanBuyEntity.class);
        if (null != scEntity) {
            canBuyEnity = scEntity.canBuyInfo;
            if (null == canBuyEnity) {
                Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }else{
                if ("0".equals(canBuyEnity.fre_flag) && "0".equals(canBuyEnity.kc_flag)) {
                    goodFreight.setText(canBuyEnity.fre_tip);
                    if (realBuy) {
                        if (realInCart) {
                            if ("buyNow".equals(buyType)) {
                                realInCart = false;
                                startActivity(new Intent(GoodsDetailActivity.this,CartActivity.class));
                                pop.dismiss();
                            }else if (buyType.equals("addCart")) {
                                addCart();
                            }
                        }else{
                            addCart();
                        }
                    }
                }else{
                    Toast.makeText(this, "商品当前不支持购买", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 处理商品套餐价格和库存
     */
    private void goodInvAndPriceHander(String json){
        if (null != json && !"".equals(json) && !"{}".equals(json.trim())) {
            if (null != JSON.parseObject(json)) {
                String pd_stock=JSON.parseObject(json).getString("pd_stock");
                String pd_price=JSON.parseObject(json).getString("pd_price");
                popGoodPrice.setText("￥"+pd_price);
                popGoodStock.setText(pd_stock);
            }
        }
    }

    @Override
    public void gotoShop(String key) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        int pos = (position) % ListUtils.getSize(topInfos);
        for (int i = 0; i < indicators.length; i++) {
            if(pos == i){
                indicators[pos].setBackgroundResource(R.mipmap.indicators_right_now);
            } else{
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
        }

    }

}

