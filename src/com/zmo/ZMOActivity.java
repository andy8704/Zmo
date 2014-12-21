package com.zmo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ZMOActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout mFragmentViewLayout;
	private TextView mFindBtn;
	private TextView mImBtn;
	private TextView mMyBtn;
	private HotFragment mHotFragment;
	private ZmoOwnerFragment mOwnerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zmo);
		initUI();
		addListener();
		
		onSetState(R.id.my_btn_id);
		onShowOwner();
	}

	private void initUI() {
		mFragmentViewLayout = (LinearLayout) findViewById(R.id.frament_view_id);
		mFindBtn = (TextView) findViewById(R.id.find_btn_id);
		mImBtn = (TextView) findViewById(R.id.im_btn_id);
		mMyBtn = (TextView) findViewById(R.id.my_btn_id);
	}

	private void addListener() {
		mFindBtn.setOnClickListener(this);
		mImBtn.setOnClickListener(this);
		mMyBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.find_btn_id:
			onShowFind();
			break;
		case R.id.im_btn_id:
			onShowIm();
			break;
		case R.id.my_btn_id:
			onShowOwner();
			break;
		default:
			break;
		}
		onSetState(view.getId());
	}

	private void onSetState(final int nId) {

		mFindBtn.setTextColor(getResources().getColor(R.color.color_tool_text));
		mImBtn.setTextColor(getResources().getColor(R.color.color_tool_text));
		mMyBtn.setTextColor(getResources().getColor(R.color.color_tool_text));

		switch (nId) {
		case R.id.find_btn_id:
			mFindBtn.setTextColor(getResources().getColor(R.color.color_tool_text_select));
			break;
		case R.id.im_btn_id:
			mImBtn.setTextColor(getResources().getColor(R.color.color_tool_text_select));
			break;
		case R.id.my_btn_id:
			mMyBtn.setTextColor(getResources().getColor(R.color.color_tool_text_select));
			break;
		default:
			break;
		}
	}

	private void onShowFind() {
		if (null == mHotFragment)
			mHotFragment = new HotFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.frament_view_id, mHotFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void onShowIm() {

	}

	private void onShowOwner() {

		if (null == mOwnerFragment)
			mOwnerFragment = new ZmoOwnerFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.frament_view_id, mOwnerFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
