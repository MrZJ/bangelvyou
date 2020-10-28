package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishoureport.system.R;

/**
 * Created by jianzhang.
 * on 2017/8/29.
 * copyright easybiz.
 */

public class WorkAdapter extends BaseAdapter {
    public interface WorkItemClick {
        void onItemClick(int pos);
    }

    private Context mContext;
    private WorkItemClick itemClick;

    public WorkAdapter(Context context, WorkItemClick itemClick) {
        mContext = context;
        this.itemClick = itemClick;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.work_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.pic_iv);
            holder.layout = convertView.findViewById(R.id.layout);
            holder.textView = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initHoder(holder, position);
        return convertView;
    }

    private void initHoder(ViewHolder holder, final int pos) {
        if (holder != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(pos);
                }
            });
            switch (pos) {
                case 0:
                    holder.imageView.setImageResource(R.mipmap.attandence);
                    holder.textView.setText("考勤打卡");
                    break;
                case 1:
                    holder.imageView.setImageResource(R.mipmap.leave_aplication_pic);
                    holder.textView.setText("请假");
                    break;
                case 2:
                    holder.imageView.setImageResource(R.mipmap.business_pic);
                    holder.textView.setText("出差");
                    break;
                case 3:
                    holder.imageView.setImageResource(R.mipmap.approve_pic);
                    holder.textView.setText("已审批");
                    break;
                case 4:
                    holder.imageView.setImageResource(R.mipmap.work_over_time);
                    holder.textView.setText("集体加班");
                    break;
                case 5:
                    holder.imageView.setImageResource(R.mipmap.personal_work_over_time);
                    holder.textView.setText("个人加班");
                    break;
                case 6:
                    holder.imageView.setImageResource(R.mipmap.send_car);
                    holder.textView.setText("派车管理");
                    break;
                case 7:
                    holder.imageView.setImageResource(R.mipmap.san);
                    holder.textView.setText("扫码管理");
                    break;
            }
        }
    }

    public class ViewHolder {
        public ImageView imageView;
        public TextView textView;
        private View layout;
    }
}
