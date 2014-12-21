package com.zmo;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.view.ZmoEditText;

public class ZmoRegisterActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView mEmailTabBtn;
	private TextView mMobileTabBtn;

	private ZmoEditText mNameEditView;
	private ZmoEditText mPwdEditView;
	private ZmoEditText mCodeEditView;

	private CircularProgressButton mRegisterBtn;
	private CircularProgressButton mGetCodeBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_register_activity);
		initView();
		addListener();
	}

	private void initView() {
		mEmailTabBtn = (TextView) findViewById(R.id.email_tab_id);
		mMobileTabBtn = (TextView) findViewById(R.id.mobile_tab_id);

		mNameEditView = (ZmoEditText) findViewById(R.id.login_edit_id);
		mPwdEditView = (ZmoEditText) findViewById(R.id.pwd_edit_id);
		mCodeEditView = (ZmoEditText) findViewById(R.id.code_edit_id);
		mPwdEditView.onSetTextPwd(true);

		mRegisterBtn = (CircularProgressButton) findViewById(R.id.register_btn_id);
		mRegisterBtn.setIndeterminateProgressMode(true);
		mGetCodeBtn = (CircularProgressButton) findViewById(R.id.get_code_btn_id);
		mGetCodeBtn.setIndeterminateProgressMode(true);

		onSetState(false);
	}

	private void addListener() {

		mEmailTabBtn.setOnClickListener(this);
		mMobileTabBtn.setOnClickListener(this);
		mRegisterBtn.setOnClickListener(this);
		mGetCodeBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register_btn_id:
			break;

		case R.id.email_tab_id:
			break;
		case R.id.mobile_tab_id:
			break;
		case R.id.get_code_btn_id:
			break;
		default:
			break;
		}
	}

	private void onSetState(boolean bEmailFlag) {
		if (bEmailFlag) {
			// email模式登录
			mNameEditView.onSetTextType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else {
			// mobile模式登录
			mNameEditView.onSetTextType(InputType.TYPE_CLASS_NUMBER);
		}
	}
}
