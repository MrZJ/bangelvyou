package com.basulvyou.system.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.CollectAdapter;
import com.basulvyou.system.api.FocusApi;
import com.basulvyou.system.entity.CollectEntity;
import com.basulvyou.system.entity.CollectList;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 我的收藏列表页
 */
public class CollectActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView collect_list;
    private CollectAdapter adapter;
    private List<CollectEntity> collectlist;
    private int delPosition;//要删除的收藏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
        initListener();
        setAdapter();
        getCollectList();
    }

   private void initView(){
       initTopView();
       setTitle("我的收藏");
       setLeftBackButton();
       collect_list = (ListView) findViewById(R.id.collect_list);
   }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        collect_list.setOnItemClickListener(this);
        collect_list.setOnItemLongClickListener(this);
    }

    /**
     * 获取收藏列表信息
     */
    private void getCollectList(){
        httpPostRequest(FocusApi.getCollectListUrl(),
                getRequestParams(), FocusApi.API_GET_COLLECT_LIST);
    }

    /**
     * 获取收藏列表参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        return params;
    }

    /**
     * 删除收藏信息
     */
    private void deleteCollect(int position){
        httpPostRequest(FocusApi.getDeleteCollectUrl(), getDeleteRequestParams(position), FocusApi.API_GET_DELETE_COLLECT);
    }

    /**
     * 获取删除收藏参数
     *
     * @return
     */
    private HashMap<String,String> getDeleteRequestParams(int position){
        CollectEntity collectEntity = new CollectEntity();
        if(!ListUtils.isEmpty(collectlist) && collectlist.size() > position){
            collectEntity = collectlist.get(position);
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("fav_id", collectEntity.fav_id);
        return params;
    }

    /**
     * 设置adapter数据
     */
    private void setAdapter(){
        adapter = new CollectAdapter(null, this);
        collect_list.setAdapter(adapter);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action){
            case FocusApi.API_GET_COLLECT_LIST:
                collectHander(json);
                break;
            case FocusApi.API_GET_DELETE_COLLECT:
                if(collectlist == null || collectlist.size() <= delPosition){
                    return;
                }
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                collectlist.remove(delPosition);
                adapter.notifyDataSetChanged();
//                getCollectList();
                break;
            default:
                break;
        }
    }

    /**
     * 处理收藏列表信息
     * @param json
     */
    private void collectHander(String json){
        CollectList collect = JSON.parseObject(json, CollectList.class);
        if(collect != null){
            collectlist = collect.list;
            if(!ListUtils.isEmpty(collectlist)){
                adapter.setData(collectlist);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!ListUtils.isEmpty(collectlist) && collectlist.size() > position){
            CollectEntity collectEntity = collectlist.get(position);
            String rule = collectEntity.cls_type;
            divide(rule);
        }
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     * @param keyStr
     */
    private void divide(String keyStr) {
        if (!StringUtil.isEmpty(keyStr)) {
            String detailStr = keyStr.substring(0, 2);
            String goodsId = keyStr.substring(2);
            //详情规则
            Intent detailIntent = new Intent(this, LocationDetailActivity.class);
            detailIntent.putExtra("goods_id", goodsId);
            if ("**".equals(detailStr)) {//景点详情
                detailIntent.putExtra("type", "view");
            } else if ("##".equals(detailStr)) {//美食详情
                detailIntent.putExtra("type", "food");
            } else if ("!!".equals(detailStr)) {//酒店详情
                detailIntent.putExtra("type", "sleep");
            } else if ("~~".equals(detailStr)) {//体验详情
                detailIntent.putExtra("type", "live");
            } else if ("$$".equals(detailStr)) {//实物详情
                Intent goodsDetailIntent = new Intent(this, GoodsDetailActivity.class);
                goodsDetailIntent.putExtra("goods_id", goodsId);
                startActivity(goodsDetailIntent);
                return;
            } else {
                return;
            }
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(this).setTitle("").setMessage("确认删除该收藏商品？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delPosition = position;
                        deleteCollect(position);
                    }})
                .setNegativeButton("取消",null)
                .show();
        return true;
    }
}
