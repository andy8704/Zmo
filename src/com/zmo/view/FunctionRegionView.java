package com.zmo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmo.R;

public class FunctionRegionView extends LinearLayout {

	private TextView mFuncTextView;
	private TextView mCountView;
	private LinearLayout mGroupViewLayout;
	private Context mContext;

	public FunctionRegionView(Context context) {
		super(context);

		initUI(context);
	}

	public FunctionRegionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}

	@SuppressLint("NewApi")
	public FunctionRegionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI(context);
	}

	private void initUI(final Context context) {
		mContext = context;
		inflate(context, R.layout.function_region_view, this);
		mFuncTextView = (TextView) findViewById(R.id.function_lable_id);
		mCountView = (TextView) findViewById(R.id.count_view_id);
		mGroupViewLayout = (LinearLayout) findViewById(R.id.groupview_id);
	}

	public void onSetBasicInfo(final String labStr, final int nCount) {
		mFuncTextView.setText(labStr);
		mCountView.setText(String.valueOf(nCount));
	}

	public void onAddOneItemView(final View view) {
		if (null == view)
			return;

		mGroupViewLayout.addView(view);
	}

}
