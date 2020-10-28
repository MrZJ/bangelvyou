package com.zycreview.system.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zycreview.system.R;
import com.zycreview.system.entity.TradingEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 交易单适配器
 * 正在生成
 */
public class TradingAdapter extends  AbsBaseAdapter<TradingEntity>{

    private List<TradingEntity> mData;
    private LayoutInflater inflater;
    private OnCheckListener listener;
    private HashMap<Integer, String> priceText;//保存价格
    private HashMap<Integer, String> weightText;//出库重量
    private HashMap<Integer, Boolean> checkState;//保存状态
    private Context context;

    public TradingAdapter(List<TradingEntity> data,Context context){
        mData = data;
        this.context = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public void setData(List<TradingEntity> infos) {
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
    public TradingEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_trading, null);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_item_trading_name);
        TextView tv_surplusweight = (TextView) convertView.findViewById(R.id.tv_item_trading_surplusweight);
        TextView tv_weight = (TextView) convertView.findViewById(R.id.tv_item_trading_weight);
        final EditText edt_outweight = (EditText) convertView.findViewById(R.id.edt_item_outweight);
        final EditText edt_price = (EditText) convertView.findViewById(R.id.edt_item_trading_price);
        final CheckBox addCheck = (CheckBox) convertView.findViewById(R.id.check_item_trading_add);
        final TradingEntity entity = getItem(position);
        tv_name.setText("药材名称: " + entity.drugs_name);
        tv_weight.setText("药材总量: " + entity.drugs_number +entity.drugs_unit);
        tv_surplusweight.setText("剩余重量: " + entity.surplus_drugs_number + entity.drugs_unit);
        edt_outweight.setText(weightText.get(position));
        edt_price.setText(priceText.get(position));
        if(checkState.get(position) == null){
            addCheck.setChecked(false);
        }else{
            addCheck.setChecked(checkState.get(position));
        }
        addCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkState.put(position,b);
                if (b) {
                    if(StringUtil.isEmpty(edt_price.getText().toString().trim()) || StringUtil.isEmpty(edt_outweight.getText().toString().trim())){
                        addCheck.setChecked(false);
                    }else if(!StringUtil.dataAboveZero(edt_price.getText().toString().trim())){
                        addCheck.setChecked(false);
                        Toast.makeText(context,"单价不符合规则",Toast.LENGTH_SHORT).show();
                        edt_price.setText("");
                    } else if(!StringUtil.dataAboveZero(edt_outweight.getText().toString().trim())){
                        addCheck.setChecked(false);
                        Toast.makeText(context,"出库重量不符合规则",Toast.LENGTH_SHORT).show();
                        edt_outweight.setText("");
                    }else{
                        listener.addTrading(entity.fb_id, edt_price.getText().toString().trim(),edt_outweight.getText().toString().trim());
                    }
                } else {
                    listener.removeTrading(entity.fb_id);
                }
            }
        });
        edt_outweight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isEmpty(editable.toString())) {
                    if(editable.toString().startsWith(".")){
                        edt_outweight.setText("");
                    }else{
                        // 当EditText数据发生改变的时候存到map中
                        float a = Float.parseFloat(editable.toString());
                        float b = Float.parseFloat(entity.surplus_drugs_number);
                        if(a > b){
                            edt_outweight.setText("");
                            Toast.makeText(context,"出库重量不符合规则",Toast.LENGTH_SHORT).show();
                        }else{
                            weightText.put(position, editable.toString());
                        }
                    }
                } else {
                    String oldWeight = weightText.get(position);
                    if(!StringUtil.isEmpty(oldWeight)){
                        listener.removeTrading(entity.fb_id);
                    }
                    addCheck.setChecked(false);
                    weightText.put(position, "");
                }
            }
        });
        edt_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    if(s.toString().startsWith(".")){
                        edt_price.setText("");
                    }else{
                        // 当EditText数据发生改变的时候存到map中
                        priceText.put(position, s.toString());
                    }
                } else {
                    String oldPrice = priceText.get(position);
                    if(!StringUtil.isEmpty(oldPrice)){
                        listener.removeTrading(entity.fb_id);
                    }
                    addCheck.setChecked(false);
                    priceText.put(position, "");
                }
            }
        });
        return convertView;
    }

    public interface OnCheckListener{
        public void addTrading(String id,String price,String outWeight);
        public void removeTrading(String id);
    }

    public void setOnCheckListener(OnCheckListener listener){
        this.listener = listener;
    }

    public void clearMap(){
        priceText = new HashMap<>();
        checkState = new HashMap<>();
        weightText = new HashMap<>();
    }
}
