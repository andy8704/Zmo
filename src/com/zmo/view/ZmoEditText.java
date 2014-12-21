package com.zmo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmo.R;

public class ZmoEditText extends LinearLayout implements TextWatcher {

	private LinearLayout mGroupView = null;
	private EditText mEditText;
	private ImageView mDelBtn;

	public ZmoEditText(Context context) {
		super(context);
		initUI();
	}

	public ZmoEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI();
	}

	@SuppressLint("NewApi")
	public ZmoEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI();
	}

	private void initUI() {
		inflate(getContext(), R.layout.zmo_edittext_del, this);
		mGroupView = (LinearLayout) findViewById(R.id.edit_linearlayout_id);
		mEditText = (EditText) findViewById(R.id.editview_id);
		mDelBtn = (ImageView) findViewById(R.id.del_btn_id);
		mDelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mEditText.setText("");
			}
		});
		mEditText.addTextChangedListener(this);
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (TextUtils.isEmpty(arg0)) {
			mDelBtn.setVisibility(View.INVISIBLE);
		} else {
			mDelBtn.setVisibility(View.VISIBLE);
		}
	}

	public void setTextSize(final int nTextSize) {
		if (null != mEditText)
			mEditText.setTextSize(nTextSize);
	}

	public void onSetTextColor(final int nColor) {
		if (null != mEditText)
			mEditText.setTextColor(nColor);
	}

	public void onSetTextType(final int nInputType) {
		if (null != mEditText)
			mEditText.setInputType(nInputType);
	}

	public void onSetTextPwd(boolean bPwd) {
		if (null != mEditText) {
			if (bPwd) {
				mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
		}
	}

	public void onSetHintText(final String hintString) {
		if (!TextUtils.isEmpty(hintString) && null != mEditText)
			mEditText.setHint(hintString);
	}

	public void onSetHintText(final int stringId) {
		if (null != mEditText)
			mEditText.setText(stringId);
	}

	public void onSetHintColor(final int nColor) {
		if (null != mEditText)
			mEditText.setHintTextColor(nColor);
	}

	public void onSetEditBg(final int nColor) {
		if (null != mGroupView)
			mGroupView.setBackgroundColor(nColor);
	}

	public void onSetEditBgDrawable(final int nResourceId) {
		if (null != mGroupView)
			mGroupView.setBackgroundResource(nResourceId);
	}

	public String onGetEditText() {
		if (null != mEditText)
			return mEditText.getText().toString();
		return null;
	}
}
