package com.zmo;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.view.ZmoEditText;

public class ZmoLogInActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView mEmailTabBtn;
	private TextView mMobileTabBtn;

	private ZmoEditText mNameEditView;
	private ZmoEditText mPwdEditView;

	private CircularProgressButton mLoginBtn;
	private TextView mForgetPWDBtn;

	private TextView mWeiBoBtn;
	private TextView mQQBtn;
	private TextView mWeiXinBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_login_activity);
		initView();
		addListener();
	}

	private void initView() {
		mEmailTabBtn = (TextView) findViewById(R.id.email_tab_id);
		mMobileTabBtn = (TextView) findViewById(R.id.mobile_tab_id);

		mNameEditView = (ZmoEditText) findViewById(R.id.login_edit_id);
		mPwdEditView = (ZmoEditText) findViewById(R.id.pwd_edit_id);
		mPwdEditView.onSetTextPwd(true);

		mLoginBtn = (CircularProgressButton) findViewById(R.id.login_btn_id);
		mLoginBtn.setIndeterminateProgressMode(true);
		mForgetPWDBtn = (TextView) findViewById(R.id.forget_view_id);
		mWeiBoBtn = (TextView) findViewById(R.id.weibo_login_btn_id);
		mQQBtn = (TextView) findViewById(R.id.qq_login_btn_id);
		mWeiXinBtn = (TextView) findViewById(R.id.weixin_login_btn_id);
		
		onSetState(false);
	}

	private void addListener() {

		mEmailTabBtn.setOnClickListener(this);
		mMobileTabBtn.setOnClickListener(this);
		mForgetPWDBtn.setOnClickListener(this);
		mQQBtn.setOnClickListener(this);
		mWeiBoBtn.setOnClickListener(this);
		mWeiXinBtn.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn_id:
			break;

		case R.id.email_tab_id:
			break;
		case R.id.mobile_tab_id:
			break;
		case R.id.weibo_login_btn_id:
			break;
		case R.id.weixin_login_btn_id:
			break;
		case R.id.qq_login_btn_id:
			break;

		default:
			break;
		}
	}
	
	private void onSetState(boolean bEmailFlag){
		if(bEmailFlag){
			// email模式登录
			mNameEditView.onSetTextType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		}else{
			// mobile模式登录
			mNameEditView.onSetTextType(InputType.TYPE_CLASS_NUMBER);
		}
	}
}
