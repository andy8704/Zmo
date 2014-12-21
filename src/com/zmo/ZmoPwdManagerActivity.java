package com.zmo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ad.util.ToastUtil;
import com.zmo.data.AccountData;
import com.zmo.view.ZmoEditText;

public class ZmoPwdManagerActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView tv_mobileTabBtn;
	private TextView tv_emailTabBtn;

	private TextView tv_PwdView;
	private ZmoEditText codeEditText;
	private Button b_CodeBtn;
	private Button b_NextBtn;
	private AccountData mAccountData;

	private boolean bMobileType = true;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.zmo_pwd_manager_activity);
		setTitle(R.string.pwd_manager_title);

		mAccountData = ZmoApplication.onGetInstance().onGetAccountData();
		initView();
		onSetState(true);
	}

	private void initView() {
		tv_mobileTabBtn = (TextView) findViewById(R.id.mobile_tab_id);
		tv_emailTabBtn = (TextView) findViewById(R.id.email_tab_id);

		tv_PwdView = (TextView) findViewById(R.id.pwd_view_id);
		codeEditText = (ZmoEditText) findViewById(R.id.code_edit_id);
		b_CodeBtn = (Button) findViewById(R.id.get_code_btn_id);
		b_NextBtn = (Button) findViewById(R.id.next_step_btn_id);

		tv_mobileTabBtn.setOnClickListener(this);
		tv_emailTabBtn.setOnClickListener(this);
		b_CodeBtn.setOnClickListener(this);
		b_NextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.mobile_tab_id:
			onSetState(true);
			break;
		case R.id.email_tab_id:
			onSetState(false);
			break;
		case R.id.get_code_btn_id:
			onGetVerfineCode();
			break;
		case R.id.next_step_btn_id:
			onNextStep();
			break;
		default:
			break;
		}
	}

	private void onSetState(boolean bMobile) {

		bMobileType = bMobile;

		if (bMobile) {
			tv_mobileTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_mobileTabBtn.setTextColor(Color.WHITE);
			tv_PwdView.setText(String.format(getString(R.string.tel_format_lable), onGetMobileHideString(mAccountData.mobile)));
			tv_emailTabBtn.setBackgroundColor(Color.WHITE);
			tv_emailTabBtn.setTextColor(Color.BLACK);
		} else {
			tv_emailTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_emailTabBtn.setTextColor(Color.WHITE);
			tv_PwdView.setText(String.format(getString(R.string.email_format_lable), onGetEmailHideString(mAccountData.mobile)));
			tv_mobileTabBtn.setBackgroundColor(Color.WHITE);
			tv_mobileTabBtn.setTextColor(Color.BLACK);
		}
	}

	private String onGetMobileHideString(final String mobile) {
		if (TextUtils.isEmpty(mobile))
			return null;

		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}

	private String onGetEmailHideString(final String emailStr) {
		if (TextUtils.isEmpty(emailStr))
			return null;

		int nIndex = emailStr.indexOf("@");
		return emailStr.substring(0, nIndex - 3) + "***" + emailStr.substring(nIndex);
	}

	private void onGetVerfineCode() {

	}

	private void onNextStep() {
		if (bMobileType) {
			// 手机号模式
			startActivityForResult(new Intent(this, ZmoPwdReSetActivity.class), 1);
		} else {
			// 直接把重置的密码发到邮箱中
			ToastUtil.onShowToast(this, "密码重置成功，请到邮箱中查收");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub

		if (resultCode == RESULT_OK && requestCode == 1) {
			// 设置新密码成功
		}
		super.onActivityResult(requestCode, resultCode, arg2);
	}
}
