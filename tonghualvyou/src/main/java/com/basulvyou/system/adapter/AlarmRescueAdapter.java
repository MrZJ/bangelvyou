package com.basulvyou.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.ListUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 报警救援列表适配器
 */
public class AlarmRescueAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private LayoutInflater mInflater;
    private List<CarpoolingInfoEntity> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public AlarmRescueAdapter(List<CarpoolingInfoEntity> list,Context ctx){
        mInflater = LayoutInflater.from(ctx);
        mList = list;
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(int postion);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_alarm_rescue,null);
        AlarmRescueHolder holder = new AlarmRescueHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlarmRescueHolder alarmRescueHolder = (AlarmRescueHolder) holder;
        if(position > 0){
            //当前字母和前一个位置的字母相等则隐藏字母显示
            if(mList.get(position).seo_title.equals(mList.get(position-1).seo_title)){
                alarmRescueHolder.alpLinear.setVisibility(View.GONE);
            }else{
                alarmRescueHolder.rescueAlp.setText(mList.get(position).seo_title);
                alarmRescueHolder.alpLinear.setVisibility(View.VISIBLE);
            }
            alarmRescueHolder.rescueName.setText(mList.get(position).goods_name);
            alarmRescueHolder.rescueLocal.setText(mList.get(position).p_name);
        }else{
            alarmRescueHolder.rescueAlp.setText(mList.get(position).seo_title);
            alarmRescueHolder.rescueName.setText(mList.get(position).goods_name);
            alarmRescueHolder.rescueLocal.setText(mList.get(position).p_name);
        }
        if (!ListUtils.isEmpty(mList.get(position).main_pics)) {
            imageLoader.displayImage(mList.get(position).main_pics.get(0).main_pic, alarmRescueHolder.rescueIcon,
                    AsynImageUtil.getImageOptions(), null);
        } else {
            alarmRescueHolder.rescueIcon.setBackgroundResource(R.mipmap.alarm_icon);
        }
        alarmRescueHolder.clickRel.setTag(position);
        alarmRescueHolder.clickRel.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick((Integer)v.getTag());
        }
    }

    class AlarmRescueHolder extends RecyclerView.ViewHolder {

        public TextView rescueAlp,rescueName,rescueLocal;
        public ImageView rescueIcon;
        public LinearLayout alpLinear;
        public RelativeLayout clickRel;

        public AlarmRescueHolder(View itemView) {
            super(itemView);
            alpLinear = (LinearLayout) itemView.findViewById(R.id.lin_item_alarm_rescue_alp);
            rescueAlp = (TextView) itemView.findViewById(R.id.tv_item_alarm_rescue_alp);//字母
            rescueIcon = (ImageView) itemView.findViewById(R.id.img_item_alarm_rescue_icon);
            rescueName = (TextView) itemView.findViewById(R.id.tv_item_alarm_rescue_name);
            rescueLocal = (TextView) itemView.findViewById(R.id.tv_item_alarm_rescue_ads);
            clickRel = (RelativeLayout) itemView.findViewById(R.id.rel_item_alarm_rescue);
            rescueName.setTextColor(Color.parseColor("#666666"));
        }

    }
}
