package com.zmo.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.util.DateFormat;
import com.zmo.R;
import com.zmo.ZmoApplication;
import com.zmo.model.OrderInfo;

public class ZmoOrderAdapter extends BaseAdapter {

	private Context mContext;
	private List<OrderInfo> mData;

	public ZmoOrderAdapter(Context context, final List<OrderInfo> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return null == mData ? 0 : mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null == mData ? null : mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {

		if (null == mData || mData.isEmpty())
			return null;

		Holder holder = null;
		if (null == contentView) {
			contentView = LayoutInflater.from(mContext).inflate(R.layout.zmo_ordered_adapter, null);
			holder = new Holder(contentView);
			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}

		OrderInfo model = mData.get(position);
		if (null != model) {
			ZmoApplication.onGetInstance().onGetFinalBitmap().display(holder.iv_ImgView, model.orderPicUrl);
			holder.tv_OrderIdView.setText(String.format(mContext.getString(R.string.order_id_format_str), model.orderId));
			holder.tv_OrderTitleView.setText(model.orderTitle);
			holder.tv_OrderPriceView.setText(String.format(mContext.getString(R.string.order_price_format_str), model.orderPrice));
			holder.tv_TimeView.setText(onFormatTime(model.orderTime));
		}
		return contentView;
	}
	
	@SuppressLint("SimpleDateFormat") private String onFormatTime(final long time){
		return new SimpleDateFormat(DateFormat.DEFAULT_DATE_FORMAT).format(new Date(time));
	}

	public class Holder {
		public ImageView iv_ImgView;
		public TextView tv_OrderIdView;
		public TextView tv_OrderTitleView;
		public TextView tv_TimeView;
		public TextView tv_OrderPriceView;

		public Holder(final View view) {
			if (null == view)
				return;

			iv_ImgView = (ImageView) view.findViewById(R.id.order_img_id);
			tv_OrderIdView = (TextView) view.findViewById(R.id.order_id);
			tv_OrderTitleView = (TextView) view.findViewById(R.id.order_title_id);
			tv_TimeView = (TextView) view.findViewById(R.id.time_view_id);
			tv_OrderPriceView = (TextView) view.findViewById(R.id.order_price_id);
		}
	}

}
