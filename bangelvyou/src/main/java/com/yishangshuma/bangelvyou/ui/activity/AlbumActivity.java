package com.yishangshuma.bangelvyou.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.AlbumGridViewAdapter;
import com.yishangshuma.bangelvyou.util.photo.AlbumHelper;
import com.yishangshuma.bangelvyou.util.photo.Bimp;
import com.yishangshuma.bangelvyou.util.photo.ImageBucket;
import com.yishangshuma.bangelvyou.util.photo.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 进入相册显示所有图片
 */
public class AlbumActivity extends Activity {
    //显示手机里的所有图片的列表控件
    private GridView gridView;
    //当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    //完成按钮
    private Button okButton;
    // 返回按钮
    private Button back;
    private Intent intent;

    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;
    private String id;//要评论的商品id

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_camera_album);
        id = getIntent().getExtras().getString("id");
        mContext = this;
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plugin_camera_no_pictures);
        init();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //mContext.unregisterReceiver(this);
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
            intent.putExtra("id",id);
            intent.setClass(mContext, EditCommentActivity.class);
            startActivity(intent);
            finish();
        }

    }

    // 返回按钮监听
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    // 初始化，给一些对象赋值
    private void init() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new BackListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
                Bimp.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size()
                + "/" + 5 + ")");
    }

    private void initListener() {
        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton,
                                    int position, boolean isChecked, Button chooseBt) {
                if (Bimp.tempSelectBitmap.size() >= 5) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeOneData(dataList.get(position))) {
                        Toast.makeText(AlbumActivity.this, "超出可选张数", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (isChecked) {
                    chooseBt.setVisibility(View.VISIBLE);
                    Bimp.tempSelectBitmap.add(dataList.get(position));
                    okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size()
                            + "/" + 5 + ")");
                } else {
                    Bimp.tempSelectBitmap.remove(dataList.get(position));
                    chooseBt.setVisibility(View.GONE);
                    okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + 5 + ")");
                }
                isShowOkBt();
            }
        });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(ImageItem imageItem) {
        if (Bimp.tempSelectBitmap.contains(imageItem)) {
            Bimp.tempSelectBitmap.remove(imageItem);
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + 5 + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (Bimp.tempSelectBitmap.size() > 0) {
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size()
                    + "/" + 5 + ")");
            okButton.setPressed(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
        } else {
            okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + 5 + ")");
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }
}
