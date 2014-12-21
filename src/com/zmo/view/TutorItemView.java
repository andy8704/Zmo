package com.zmo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmo.R;
import com.zmo.ZmoApplication;
import com.zmo.model.CourseModel;
import com.zmo.model.TutorModel;

public class TutorItemView extends LinearLayout {

	private Context mContext;
	private ImageView mImageView;
	private TextView mTitleView;
	private TextView mDespView;

	public TutorItemView(Context context) {
		super(context);

		initUI(context);
	}

	public TutorItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}

	@SuppressLint("NewApi")
	public TutorItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI(context);
	}

	private void initUI(Context context) {
		mContext = context;

		inflate(context, R.layout.tutor_item_view, this);
		mImageView = (ImageView) findViewById(R.id.img_view_id);
		mTitleView = (TextView) findViewById(R.id.title_view_id);
		mDespView = (TextView) findViewById(R.id.desp_view_id);
	}

	public void onSetData(final TutorModel data) {
		if (null == data)
			return;

		if (!TextUtils.isEmpty(data.picUrl))
			ZmoApplication.onGetInstance().onGetFinalBitmap().display(mImageView, data.picUrl);

		mTitleView.setText(data.title);
		mDespView.setText(data.despStr);
	}
}
