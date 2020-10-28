package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.OrderCommentAdapter;
import com.basulvyou.system.api.CommentApi;
import com.basulvyou.system.entity.OrderGoodsList;

import java.util.HashMap;
import java.util.List;

/**
 * 订单评价界面
 */
public class OrderCommentActivity extends BaseActivity implements View.OnClickListener {

    private List<OrderGoodsList> orderGoodsList;
    private ListView order_comment_list;
    private HashMap<Integer, String> commentText;//保存用户对商品文字评论
    private HashMap<Integer, String> ratNum;//保存用户对商品评论星级
    private TextView sure_comment;//提交评论
    private OrderCommentAdapter adapter;//评论晒单商品展示适配器
    private String orderId;//订单编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment);
        orderGoodsList = (List<OrderGoodsList>) getIntent().getExtras().getSerializable("orderGoodsList");
        orderId = getIntent().getExtras().getString("orderID");
        initView();
        setAdapter();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("评论晒单");
        order_comment_list = (ListView) findViewById(R.id.order_comment_list);
        sure_comment = (TextView) findViewById(R.id.sure_comment);
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        sure_comment.setOnClickListener(this);
    }

    /**
     * 设置适配器
     */
    private void setAdapter(){
        commentText = new HashMap<Integer, String>();
        ratNum = new HashMap<Integer, String>();
        for (int i = 0; i < orderGoodsList.size(); i++) {
            OrderGoodsList orderGoods =orderGoodsList.get(i);
            ratNum.put(i, "5");//设置初始星级为5
            commentText.put(i, "");//初始评论为空
        }
        adapter = new OrderCommentAdapter(orderGoodsList, this,commentText,ratNum);
        order_comment_list.setAdapter(adapter);

    }

    /**
     * 点击事件
     */
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sure_comment://点击编辑or完成
                commentText = adapter.getCommentMap();
                ratNum = adapter.getRatMap();
                StringBuffer commitString = new StringBuffer();
                boolean sureCommit = false;
                for (int i = 0; i < orderGoodsList.size(); i++) {
                    OrderGoodsList orderGoods = orderGoodsList.get(i);
                    String commentStr = commentText.get(i);
                    String ratStr = ratNum.get(i);
                    if (commentStr != null && !"".equals(commentStr)) {
                        sureCommit = true;
                    }
                    if (orderGoodsList.size() == 0) {
                        commitString.append(orderGoods.goods_id+"|"+ratStr+"|"+commentStr);
                    }else if(i == orderGoodsList.size()-1){
                        commitString.append(orderGoods.goods_id+"|"+ratStr+"|"+commentStr);
                    }else{
                        commitString.append(orderGoods.goods_id+"|"+ratStr+"|"+commentStr+",");
                    }
                }
                if (sureCommit) {
                    commentCommit(configEntity.key, orderId, commitString.toString());
                }else{
                    Toast.makeText(this, "评论不能都为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 提交评论到服务器
     * @param key
     * @param orderId
     * @param goods
     */
    private void commentCommit(String key, String orderId,String goods){
        httpPostRequest(CommentApi.getCommentUrl(),
                getRequestParams(key, orderId, goods), CommentApi.API_COMMENT);
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap getRequestParams(String key, String orderId, String goods){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", key);
        params.put("order_id", orderId);
        params.put("goods", goods);
        return params;
    }

    /**
     * 数据处理
     * @param json
     * @param action
     */
    @Override
    public void httpOnResponse(String json, int action) {
        super.httpOnResponse(json, action);
        switch (action) {
            case CommentApi.API_COMMENT:
                Toast.makeText(this, "评价成功", Toast.LENGTH_SHORT).show();
                Intent backIntent = new Intent();
                backIntent.putExtra("commentSucc", "1");
                this.setResult(0, backIntent);
                this.finish();
                break;
            default:
                break;
        }
    }

}

