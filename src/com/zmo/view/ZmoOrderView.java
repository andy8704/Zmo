package com.zmo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmo.R;

public class ZmoOrderView extends LinearLayout {

	private TextView tv_CountView;
	private TextView tv_LableView;

	public ZmoOrderView(Context context) {
		super(context);
		initView();
	}

	public ZmoOrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	@SuppressLint("NewApi")
	public ZmoOrderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		inflate(getContext(), R.layout.zmo_order_view, this);
		tv_CountView = (TextView) findViewById(R.id.order_count_id);
		tv_LableView = (TextView) findViewById(R.id.order_lable_id);
	}

	public void onSetLableInfo(final String lableStr, final int lableColor) {
		tv_LableView.setText(lableStr);
		tv_LableView.setTextColor(lableColor);
	}

	public void onSetLableInfo(final int lableStrId, final int lableColor) {
		onSetLableInfo(getContext().getString(lableStrId), lableColor);
	}

	public void onSetCountColor(final int nColor) {
		tv_CountView.setTextColor(nColor);
	}

	public void onSetCountText(final String count) {
		tv_CountView.setText(count);
	}
}
