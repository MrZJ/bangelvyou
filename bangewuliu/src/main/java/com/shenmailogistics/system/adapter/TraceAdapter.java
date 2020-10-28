package com.shenmailogistics.system.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenmailogistics.system.R;
import com.shenmailogistics.system.bean.TraceEntity;
import com.shenmailogistics.system.utils.StringUtil;

/**
 * Created by Administrator on 2017/3/31.
 */

public class TraceAdapter extends BaseQuickAdapter<TraceEntity.TraceItemEntity,BaseViewHolder>{

    public TraceAdapter() {
        super(R.layout.item_location_info);
    }


    @Override
    protected void convert(BaseViewHolder helper, TraceEntity.TraceItemEntity item) {
        helper.setText(R.id.item_trace_date,item.start_date);
        if(!StringUtil.isEmpty(item.mileage)){
            helper.setText(R.id.item_trace_mil,"公里数: "+item.mileage);
        }else{
            helper.setText(R.id.item_trace_mil,"公里数: 无");
        }

    }
}
