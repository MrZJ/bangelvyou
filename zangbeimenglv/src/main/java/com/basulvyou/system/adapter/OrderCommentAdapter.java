package com.basulvyou.system.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.OrderGoodsList;
import com.basulvyou.system.util.AsynImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;


/**
 * 订单商品评价适配器
 */
public class OrderCommentAdapter extends BaseAdapter{
	
	private List<OrderGoodsList> mData;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private HashMap<Integer, String> commentText;//保存用户对商品文字评论
    private HashMap<Integer, String> ratNum;//保存用户对商品评论星级

	
	public OrderCommentAdapter(List<OrderGoodsList> branchInfos, Context context,HashMap<Integer, String> commentMap,HashMap<Integer, String> ratMap) {
		mData = branchInfos;
		commentText=commentMap;
		ratNum=ratMap;
	    inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		if (null == mData) {
			return 0;
		}
		return mData.size();
	}

	public OrderGoodsList getItem(int position) {

		return mData.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView( final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_comment_order_goods, null);
		TextView tv_shop_name = (TextView) convertView.findViewById(R.id.comment_shop_name);
		TextView tv_shop_price = (TextView) convertView.findViewById(R.id.comment_shop_price);
		TextView tv_shop_comment = (TextView) convertView.findViewById(R.id.comment_shop_comment);
		ImageView img_shop = (ImageView) convertView.findViewById(R.id.comment_img_shop);
		EditText edit_comment=(EditText) convertView.findViewById(R.id.edit_comment);
		RatingBar rat_comment=(RatingBar) convertView.findViewById(R.id.rat_comment);
		OrderGoodsList orderGoods = getItem(position);
		tv_shop_name.setText(orderGoods.goods_name);
		tv_shop_price.setText("￥" + orderGoods.goods_price);
		tv_shop_comment.setText("已购买" + orderGoods.goods_num + "件");
		rat_comment.setRating(Float.parseFloat(ratNum.get(position)));
		edit_comment.setText(commentText.get(position));
        rat_comment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				
				ratNum.put(position, String.valueOf((int)arg1));
			}
		});
        edit_comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
			
			@Override
			public void afterTextChanged(Editable s) {
				 if (s != null && !"".equals(s.toString())) {
					 // 当EditText数据发生改变的时候存到map中  
	            	 commentText.put(position, s.toString());
	             }else {
	            	 commentText.put(position, "");
	             }
			}
		});
		imageLoader.displayImage(orderGoods.goods_image_url, img_shop,
				AsynImageUtil.getImageOptions(), null);
		
		return convertView;
	}
	
	/**
	 * 提供评论MAP
	 * @return
	 */
	public HashMap<Integer, String> getCommentMap()
	{
		return commentText;
	}
	
	/**
	 * 提供星级MAP
	 * @return
	 */
	public HashMap<Integer, String> getRatMap()
	{
		return ratNum;
	}
}
