package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.TradedEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 交易管理适配器
 * 已生成
 */
public class TradedAdapter extends  AbsBaseAdapter<TradedEntity>{

    private List<TradedEntity> mData;
    private LayoutInflater inflater;
    private OnTradedBottomCheckListener listener;
    private HashMap<Integer,String> isShowBottom;
    private boolean isInflate;

    public TradedAdapter(List<TradedEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<TradedEntity> infos) {
        mData = infos;
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public TradedEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_traded, null);
            holder = new ViewHolder();
            holder.linear_traded = convertView.findViewById(R.id.liner_item_tarded);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_traded_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_item_traded_date);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_traded_price);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_item_traded_state);
            holder.vs_bottom = convertView.findViewById(R.id.linear_traded_bottom);
            holder.btn_detail = (Button) convertView.findViewById(R.id.btn_check_detail);
            holder.btn_drugs = (Button) convertView.findViewById(R.id.btn_traded_durgs);
            holder.btn_update = (Button) convertView.findViewById(R.id.btn_traded_update);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TradedEntity entity = getItem(position);
        holder.tv_name.setText("采购方企业名称: " + entity.caigou_entp_name);
        holder.tv_date.setText("交易时间: " + entity.trade_date);
        holder.tv_price.setText("交易金额: " + entity.trade_money);
        String state;
        if(entity.trade_state.equals("1")){
            state = "已完成";
        } else if(entity.trade_state.equals("0")){
            state = "未完成";
        } else {
            state = "已废除";
        }
        holder.tv_state.setText("交易状态: " + state);
        if(null != isShowBottom.get(position) && isShowBottom.get(position).equals("show")){
            holder.vs_bottom.setVisibility(View.VISIBLE);
            holder.vs_bottom.setTag("visible");
        } else {
            holder.vs_bottom.setVisibility(View.GONE);
            holder.vs_bottom.setTag("gone");
        }
        final ViewHolder finalHolder = holder;
        holder.linear_traded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalHolder.vs_bottom.getTag().equals("visible")) {
                    finalHolder.vs_bottom.setVisibility(View.GONE);
                    finalHolder.vs_bottom.setTag("gone");
                    isShowBottom.put(position,"unShow");
                } else {
                    isShowBottom.put(position,"show");
                    finalHolder.btn_detail.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            listener.checkTradedDetail(entity);
                        }
                    });
                    finalHolder.btn_drugs.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            listener.tradedDurgs(entity.trade_id);
                        }
                    });
                    finalHolder.btn_update.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            listener.updateTraded(entity);
                        }
                    });
                    finalHolder.vs_bottom.setVisibility(View.VISIBLE);
                    finalHolder.vs_bottom.setTag("visible");
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        View linear_traded;
        TextView tv_name, tv_date, tv_price, tv_state;
        View vs_bottom;
        Button btn_detail,btn_drugs,btn_update;
    }

    public interface OnTradedBottomCheckListener{
        public void checkTradedDetail(TradedEntity entity);
        public void updateTraded(TradedEntity entity);
        public void tradedDurgs(String id);
    }

    public void setOnTradedBottomClickListener(OnTradedBottomCheckListener listener){
        this.listener = listener;
    }

    public void clearMap(){
        isShowBottom = new HashMap<>();
    }
}
