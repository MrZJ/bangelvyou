package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.ShareImgEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 选着图片适配器
 */
public class MyWordsGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context ctx;
    private List<ShareImgEntity> urls;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public MyWordsGridAdapter(Context context,
                              List<ShareImgEntity> data) {
        inflater = LayoutInflater.from(context);
        ctx = context;
        urls = data;
    }

    public int getCount() {
        return urls.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_my_gri,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_my_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String url = urls.get(position).friend_image_url;
        if (url != null) {
            imageLoader.displayImage(url, holder.image,
                    AsynImageUtil.getImageOptions(), null);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

}
