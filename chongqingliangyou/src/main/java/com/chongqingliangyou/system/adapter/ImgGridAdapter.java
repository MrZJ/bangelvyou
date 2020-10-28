package com.chongqingliangyou.system.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.util.photo.Bimp;

/**
 * 选着图片适配器
 */
public class ImgGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    private Context ctx;
    private int maxNum;

    public boolean isShape() {
        return shape;
    }
    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public ImgGridAdapter(Context context, int num) {
        inflater = LayoutInflater.from(context);
        ctx = context;
        maxNum = num;
    }

    public void update() {
        loading();
    }

    public int getCount() {
        if(Bimp.tempSelectBitmap.size() == maxNum){
            return maxNum;
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
            holder.image.setImageBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.icon_addpic_unfocused));
            if (position == maxNum) {
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
                    handler.removeMessages(msg.what);
                    notifyDataSetChanged();
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
