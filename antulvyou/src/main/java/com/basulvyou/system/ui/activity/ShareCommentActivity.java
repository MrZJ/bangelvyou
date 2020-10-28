package com.basulvyou.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.ShareCommentAdapter;
import com.basulvyou.system.api.ShareTextApi;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.entity.ShareCommentEntity;
import com.basulvyou.system.entity.ShareCommentList;
import com.basulvyou.system.util.ListUtils;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

/**
 * 分享评论显示页面
 */
public class ShareCommentActivity extends AbsLoadMoreActivity<ListView, ShareCommentEntity> implements ShareCommentAdapter.CommentClickListenter, View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private EditText etComment;
    private TextView tvSendCom;
    private String shareId;
    private String commentId;
    private String toId;
    private String type;
    private List<ShareCommentEntity> shareCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_comment);
        shareId = getIntent().getStringExtra("shareId");
        initView();
        initListener();
        setData();
        showLoading(getResources().getString(R.string.load_text), true);
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("评论");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.share_comment_list);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSendCom = (TextView) findViewById(R.id.btn_send_comment);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        tvSendCom.setOnClickListener(this);
        etComment.setOnClickListener(this);
    }

    private void setData() {
        type = "1";
        mAdapter = new ShareCommentAdapter(null, this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
        ((ShareCommentAdapter) mAdapter).setCommentClickListener(this);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(ShareTextApi.getHotShareCommentListlUrl(shareId, mPager.rows + "", mPager.getPage() + ""), ShareTextApi.API_GET_SHARE_COMMENT_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShareTextApi.API_GET_SHARE_COMMENT_LIST:
                commentHandler(json);
                break;
            case ShareTextApi.API_GET_SHARE_SEND_COMMENT:
                Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
                etComment.setClickable(true);
                tvSendCom.setClickable(true);
                etComment.setText("");
                shareCommentList.clear();
                shareCommentList = null;
                clearData();
                loadData();
                break;
        }
    }

    private void commentHandler(String json) {
        final long start = System.currentTimeMillis();
        ShareCommentList shareList = JSON.parseObject(json, ShareCommentList.class);
        if (null != shareList ) {
            shareCommentList = shareList.comment_list;
            if(!ListUtils.isEmpty(shareCommentList)){
                appendData(shareCommentList, start);
            }else{
                mPullToRefreshListView.onRefreshComplete();
            }
        }
    }


    @Override
    public void onClickComment(String commentId, String toId) {
        this.commentId = commentId;
        this.toId = toId;
        type = "2";
        etComment.requestFocus();
        showAndHideSoft();
    }

    /**
     * 显示或者隐藏输入法
     */
    private void showAndHideSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_comment:
                if(!configEntity.isLogin) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = etComment.getText().toString();
                if(TextUtils.isEmpty(comment)) {
                    Toast.makeText(ShareCommentActivity.this, "请输入要评论的话", Toast.LENGTH_SHORT).show();
                    return;
                }
                etComment.setClickable(false);
                tvSendCom.setClickable(false);
                httpPostRequest(ShareTextApi.getHotShareSendCommentUrl(),getParams(),ShareTextApi.API_GET_SHARE_SEND_COMMENT);
                break;
            case R.id.et_comment:
                type = "1";
                break;
        }
    }

    /**
     * 评论需要的参数
     *
     * @return
     */
    public HashMap<String, String> getParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        if(type.equals("2")){
            params.put("id", commentId);
            params.put("type", "2");
            params.put("to", toId);
        } else {
            params.put("id", shareId);
            params.put("type", "1");
        }
        params.put("comment_content", etComment.getText().toString());
        return params;
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case ShareTextApi.API_GET_SHARE_SEND_COMMENT:
                Toast.makeText(this, "发表评论失败", Toast.LENGTH_SHORT).show();
                etComment.setClickable(true);
                tvSendCom.setClickable(true);
                break;
        }
    }
}
