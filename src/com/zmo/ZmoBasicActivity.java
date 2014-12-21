package com.zmo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zmo.data.AccountData;
import com.zmo.utils.CommonUtil;

public abstract class ZmoBasicActivity extends FragmentActivity {

	private ImageView iv_backBtnView;
	private TextView tv_TitleView;
	private TextView tv_RightBtnView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.zmo_basic_activity);

		CommonUtil.addActivity(this);

		initView();
	}

	private void initView() {
		iv_backBtnView = (ImageView) findViewById(R.id.back_btn_id);
		tv_TitleView = (TextView) findViewById(R.id.title_view_id);
		tv_RightBtnView = (TextView) findViewById(R.id.right_btn_id);

		iv_backBtnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tv_RightBtnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onRightClick();
			}
		});
	}

	@Override
	public void setContentView(int layoutResID) {
		FrameLayout layout = ((FrameLayout) findViewById(R.id.groupview_id));
		View view = getLayoutInflater().inflate(layoutResID, null);
		layout.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public void setTitle(CharSequence title) {
		tv_TitleView.setText(title);
	}

	public void setTitle(int resouceId) {
		tv_TitleView.setText(resouceId);
	}

	public void onRightVisible(boolean bShow) {
		if (bShow)
			tv_RightBtnView.setVisibility(View.VISIBLE);
		else
			tv_RightBtnView.setVisibility(View.INVISIBLE);
	}
	
	public void onSetRightLable(final String lable){
		tv_RightBtnView.setText(lable);
	}
	
	public void onSetRightLable(final int nResourceId){
		tv_RightBtnView.setText(nResourceId);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		CommonUtil.popActivity(this);
	}

	protected boolean islogIn() {
		AccountData accountData = ZmoApplication.onGetInstance().onGetAccountData();
		if (null != accountData)
			return accountData.bIsLogIn;

		return false;
	}

	public abstract void onRightClick();
}
