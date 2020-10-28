package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.CommentApi;
import com.yishangshuma.bangelvyou.util.photo.Bimp;
import com.yishangshuma.bangelvyou.util.photo.ImageItem;
import com.yishangshuma.bangelvyou.util.StringUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

import java.util.HashMap;

/**写评论界面**/
public class EditCommentActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener,View.OnClickListener{

    private EditText commentEd;
    private TextView commentTip, viewDes, locDes, serDes;
    private RatingBar totalLevel, viewLevel, locationLevel, serLevel;
    private StringBuffer commentContent;//评论的内容
    private String id;//商品id
    private View parentView;
    private PopupWindow pop = null;// 选择图片来源
    private View use_camera;//使用相机
    private View use_local;//使用本地照片
    private Button cancel;//取消
    private GridAdapter adapter;
    private GridView noScrollgridview;
    public static Bitmap bimap ;
    private static final int TAKE_PICTURE_Ca = 1;//相机获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView=getLayoutInflater().inflate(R.layout.activity_edit_comment,null);
        setContentView(parentView);
        id = getIntent().getExtras().getString("id");
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        initView();
        initListener();
        setData();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("写评论");
        setTopRightTitle("发布", TopClickUtil.TOP_COM);
        totalLevel = (RatingBar) findViewById(R.id.rating_editcomment_totallevel);
        viewLevel = (RatingBar) findViewById(R.id.rating_editcomment_viewlevel);
        viewDes = (TextView) findViewById(R.id.tv_editcomment_viewdes);
        locationLevel = (RatingBar) findViewById(R.id.rating_editcomment_loclevel);
        locDes = (TextView) findViewById(R.id.tv_editcomment_locdes);
        serLevel = (RatingBar) findViewById(R.id.rating_editcomment_serlevel);
        serDes = (TextView) findViewById(R.id.tv_editcomment_serdes);
        commentEd = (EditText) findViewById(R.id.ed_comment);
        commentTip = (TextView) findViewById(R.id.tv_comment_tip);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);

        pop = new PopupWindow(EditCommentActivity.this);
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
        cancel = (Button) view.findViewById(R.id.item_cancel);
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        viewLevel.setOnRatingBarChangeListener(this);
        locationLevel.setOnRatingBarChangeListener(this);
        serLevel.setOnRatingBarChangeListener(this);
        use_camera.setOnClickListener(this);
        use_local.setOnClickListener(this);
        cancel.setOnClickListener(this);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Bimp.tempSelectBitmap.size()) {
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                }else{
                    Intent intent = new Intent(EditCommentActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
    }

    private void setData() {
        StringUtil.lengthFilter(this, commentEd, commentTip, 1000);

    }

    /**
     * 整理评论内容
     */
    private void setCommentContent(){

    }

    @Override
    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    /**
     * 保存评论星级
     */
    private void setLevel(int level){
        if(level == 0f){
            commentContent.append("5");//默认五星
        }else{
            commentContent.append(level + "");
        }
    }

    /**
     * 提交评论
     */
    public void commitComment(){
        commentContent = new StringBuffer();
        setLevel((int)totalLevel.getRating());//保存总的评价星级
        setLevel((int)viewLevel.getRating());//保存风光环境星级
        setLevel((int) locationLevel.getRating());//保存当地服务星级
        setLevel((int) serLevel.getRating());//保存公共服务星级
        if(StringUtil.isEmpty(commentEd.getText().toString())){
            Toast.makeText(this,"请输入评论内容",Toast.LENGTH_SHORT).show();
            return;
        }else{
            commentContent.append("*#" + commentEd.getText().toString());
            httpPostRequest(CommentApi.saveVirtualCommentUrl(), requestParams(), CommentApi.API_VIRTUAL_COMMENT);
        }
    }

    /**
     *整理评论参数
     */
     private HashMap requestParams(){
         HashMap<String,String > params = new HashMap<>();
         params.put("key", configEntity.key);
         params.put("comm_id", id);
         params.put("goods", commentContent.toString());
         return params;
     }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case CommentApi.API_VIRTUAL_COMMENT:
                Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()){
            case R.id.rating_editcomment_viewlevel:
                setTextLevelDes(viewDes, (int) rating);
                break;
            case R.id.rating_editcomment_loclevel:
                setTextLevelDes(locDes, (int) rating);
                break;
            case R.id.rating_editcomment_serlevel:
                setTextLevelDes(serDes, (int) rating);
                break;

        }
    }

    /**
     * 设置评价描述
     */
    private void setTextLevelDes(TextView view,int rating){
        if(rating == 1){
            view.setText("特别差");
        }else if(rating == 2){
            view.setText("不太好");
        }else if(rating == 3){
            view.setText("一般般");
        }else if(rating == 4){
            view.setText("比较好");
        }else if(rating == 5){
            view.setText("非常好");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.use_camera://打开照相机
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE_Ca);
                pop.dismiss();
                break;
            case R.id.use_local://打开本地图库
                Intent intent = new Intent(EditCommentActivity.this, AlbumActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                pop.dismiss();
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
//                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == 5){
                return 5;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 5) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
}
