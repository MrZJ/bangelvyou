package com.shishoureport.system.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.BusinessTravel;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BusinessTravelAdapter extends BaseAdapter implements MyDataPickerDialog.OnButtonClick {


    private Context mContext;
    private List<BusinessTravel> mData;
    private String mTip;

    public BusinessTravelAdapter(Context context, List<BusinessTravel> data) {
        mContext = context;
        mData = data;
        mTip = mContext.getResources().getString(R.string.travel_detail);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int mData) {
        return mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bus_travel_item, null);
            holder = new ViewHolder();
            holder.travel_detail_time_tv = (TextView) convertView.findViewById(R.id.travel_detail_time_tv);
            holder.delet_travel_tv = (TextView) convertView.findViewById(R.id.delet_travel_tv);
            holder.travel_place_et = (EditText) convertView.findViewById(R.id.travel_place_et);
            holder.start_time_tv = (TextView) convertView.findViewById(R.id.start_time_tv);
            holder.end_time_tv = (TextView) convertView.findViewById(R.id.end_time_tv);
            holder.time_lengh_et = (EditText) convertView.findViewById(R.id.time_lengh_et);
            holder.start_time_layout = convertView.findViewById(R.id.start_time_layout);
            holder.end_time_layout = convertView.findViewById(R.id.end_time_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.delet_travel_tv.setVisibility(View.GONE);
        } else {
            holder.delet_travel_tv.setVisibility(View.VISIBLE);
        }
        holder.delet_travel_tv.setOnClickListener(new MyDeletClickListener(position));
        holder.start_time_layout.setOnClickListener(new MyTimeClickDialog("开始时间", position, MyDataPickerDialog.TYPE_START_TIME));
        holder.end_time_layout.setOnClickListener(new MyTimeClickDialog("结束时间", position, MyDataPickerDialog.TYPE_END_TIME));
        holder.travel_place_et.addTextChangedListener(new MyOnTextChangeListener(position, MyOnTextChangeListener.PLACE_CHAGE, holder.travel_place_et));
        holder.time_lengh_et.addTextChangedListener(new MyOnTextChangeListener(position, MyOnTextChangeListener.DAY_CHANGE, holder.time_lengh_et));
        BusinessTravel travel = mData.get(position);
        holder.travel_detail_time_tv.setText(String.format(mTip, "" + (position + 1)));
        initHolder(travel, holder);
        return convertView;
    }

    private void initHolder(BusinessTravel travel, ViewHolder holder) {
        if (travel != null && holder != null) {
            if (travel.time_length != null) {
                holder.time_lengh_et.setText(travel.time_length);
            } else {
                holder.time_lengh_et.setText("");
            }
            if (travel.place != null) {
                holder.travel_place_et.setText(travel.place);
            } else {
                holder.travel_place_et.setText("");
            }
            if (travel.start_time != null) {
                holder.start_time_tv.setText(travel.start_time);
            } else {
                holder.start_time_tv.setText("");
            }
            if (travel.end_time != null) {
                holder.end_time_tv.setText(travel.end_time);
            } else {
                holder.end_time_tv.setText("");
            }
        }
    }

    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            mData.get(pos).start_time = date;
            notifyDataSetChanged();
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            mData.get(pos).end_time = date;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    class MyDeletClickListener implements View.OnClickListener {
        private int pos;

        public MyDeletClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            try {
                mData.remove(pos);
                notifyDataSetChanged();
            } catch (Exception e) {

            }
        }
    }

    class MyTimeClickDialog implements View.OnClickListener {
        private String title;
        private int pos;
        private int type;

        public MyTimeClickDialog(String title, int pos, int type) {
            this.title = title;
            this.pos = pos;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            showTimeDialog(title, type, pos);
        }
    }

    class MyOnTextChangeListener implements TextWatcher {
        public static final int PLACE_CHAGE = 1;
        public static final int DAY_CHANGE = 2;
        private int pos;
        private int change_type;
        private EditText editText;

        public MyOnTextChangeListener(int pos, int type, EditText editText) {
            this.pos = pos;
            this.change_type = type;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (editText.isFocused()) {
                if (change_type == PLACE_CHAGE) {
                    mData.get(pos).place = s.toString();
                } else if (change_type == DAY_CHANGE) {
                    mData.get(pos).time_length = s.toString();
                }
            }
        }
    }


    class ViewHolder {
        public TextView travel_detail_time_tv, delet_travel_tv, start_time_tv, end_time_tv;
        public EditText travel_place_et, time_lengh_et;
        private View start_time_layout, end_time_layout;
    }

    private void showTimeDialog(String titlt, int type, int pos) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(mContext, titlt, this, type, pos);
        dataPickerDialog.show();
    }
}
