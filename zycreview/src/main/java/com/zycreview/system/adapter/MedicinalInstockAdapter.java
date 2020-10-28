package com.zycreview.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.MedicInstockEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 药材入库适配器
 */
public class MedicinalInstockAdapter extends  AbsBaseAdapter<MedicInstockEntity>{

    private List<MedicInstockEntity> mData;
    private LayoutInflater inflater;
    private OnInstockClickListener listener;
    private String showType;

    public MedicinalInstockAdapter(List<MedicInstockEntity> data,Context context){
        mData = data;
        inflater = LayoutInflater.from(context.getApplicationContext());

    }

    @Override
    public void setData(List<MedicInstockEntity> infos) {
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
    public MedicInstockEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_instock_info, null);
            holder = new ViewHolder();
            holder.tv_medic_name = (TextView) convertView.findViewById(R.id.tv_item_instock_info_medicname);
            holder.tv_receive = (TextView) convertView.findViewById(R.id.tv_item_instock_info_receiveuser);
            holder.tv_instock_date = (TextView) convertView.findViewById(R.id.tv_item_instock_info_instockdate);
            holder.tv_instock_weight = (TextView) convertView.findViewById(R.id.tv_item_instock_info_medicweight);
            holder.btn_instock = (Button) convertView.findViewById(R.id.btn_item_instock_info_instock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MedicInstockEntity entity = getItem(position);
        if(!StringUtil.isEmpty(showType) && showType.equals("record")){
            holder.btn_instock.setText("查看");
            holder.tv_medic_name.setText("药材名称: "+entity.ingred_name);
            holder.tv_receive.setText("仓库名称: "+ entity.store_name);
            holder.tv_instock_weight.setText("入库人员: "+entity.input_name);
            holder.tv_instock_date.setText("入库时间: "+entity.pjr_date);
        }else{
            holder.tv_medic_name.setText("药材名称: "+entity.pjr_i_name);
            holder.tv_receive.setText("采收人员: "+ entity.pjr_u_name);
            holder.tv_instock_weight.setText("采收重量: "+entity.pjr_weight+entity.pjr_unit);
            holder.tv_instock_date.setText("采收时间: "+entity.pjr_date);
        }
        holder.btn_instock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id;
                if(!StringUtil.isEmpty(showType) && showType.equals("record")){
                    id =entity.input_id;
                }else{
                    id =entity.pjr_no_in;
                }
                listener.ClickInstock(id);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tv_medic_name, tv_instock_date , tv_instock_weight,tv_receive;
        Button btn_instock;
    }

    public interface OnInstockClickListener{
        public void ClickInstock(String id);
    }

    public void setOnInstockClickListener(OnInstockClickListener listener){
        this.listener = listener;
    }

    public void setShowType(String type){
        this.showType = type;
    }

}