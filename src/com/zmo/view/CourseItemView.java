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

public class CourseItemView extends LinearLayout {

	private Context mContext;
	private ImageView mImageView;
	private TextView mImgMarkView;
	private TextView mTitleView;
	private TextView mLabelView;
	private TextView mDespView;

	public CourseItemView(Context context) {
		super(context);

		initUI(context);
	}

	public CourseItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}

	@SuppressLint("NewApi")
	public CourseItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI(context);
	}

	private void initUI(Context context) {
		mContext = context;
		inflate(context, R.layout.course_item_view, this);
		mImageView = (ImageView) findViewById(R.id.img_view_id);
		mImgMarkView = (TextView) findViewById(R.id.img_mark_id);
		mTitleView = (TextView) findViewById(R.id.title_view_id);
		mLabelView = (TextView) findViewById(R.id.label_view_id);
		mDespView = (TextView) findViewById(R.id.desp_view_id);
	}

	public void onSetData(final CourseModel data) {
		if (null == data)
			return;

		if (!TextUtils.isEmpty(data.picUrl))
			ZmoApplication.onGetInstance().onGetFinalBitmap().display(mImageView, data.picUrl);

		mImgMarkView.setText(data.courseType);
		mTitleView.setText(data.title);
		mLabelView.setText(String.format(mContext.getString(R.string.course_lable_format_str), data.lable));
		mDespView.setText(String.format(mContext.getString(R.string.course_desp_format_str), data.desp));
	}

}
