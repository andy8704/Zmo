package com.zmo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zmo.R;
import com.zmo.listener.PicMenuListener;
import com.zmo.utils.ForumUtil;

public class PicMenuDlg extends Dialog implements android.view.View.OnClickListener {

	private TextView tv_PicPhotoBtn;
	private TextView tv_CameraBtn;
	private TextView tv_CancelBtn;
	private PicMenuListener mListener;

	public PicMenuDlg(Context context) {
		this(context, R.style.transparent);
	}

	public PicMenuDlg(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window dlgWindow = getWindow();
		WindowManager.LayoutParams params = dlgWindow.getAttributes();
		params.width = ForumUtil.getScreenWidth(getContext());
		dlgWindow.setAttributes(params);
		dlgWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

		setContentView(R.layout.pic_menu_dlg);
		initView();
	}

	private void initView() {
		tv_PicPhotoBtn = (TextView) findViewById(R.id.photo_btn_id);
		tv_CameraBtn = (TextView) findViewById(R.id.camera_btn_id);
		tv_CancelBtn = (TextView) findViewById(R.id.cancel_btn_id);

		tv_PicPhotoBtn.setOnClickListener(this);
		tv_CameraBtn.setOnClickListener(this);
		tv_CancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.photo_btn_id:
			dismiss();
			if (null != mListener)
				mListener.onPhotograph();
			break;
		case R.id.camera_btn_id:
			dismiss();
			if (null != mListener)
				mListener.onCamera();
			break;
		case R.id.cancel_btn_id:
			dismiss();
			break;

		default:
			break;
		}
	}

	public void setPicMenuListener(final PicMenuListener listener) {
		mListener = listener;
	}

}
