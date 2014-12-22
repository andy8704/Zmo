package com.zmo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ad.util.ToastUtil;
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

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mLoginBtn.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
				break;
			case 1:
				mLoginBtn.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
				finish();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_login_activity);
		setTitle("登录");
		onSetRightLable("注册");
		onRightVisible(true);
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
			onLogIn();
			break;
		case R.id.email_tab_id:
			onSetState(true);
			break;
		case R.id.mobile_tab_id:
			onSetState(false);
			break;
		case R.id.forget_view_id:
			startActivity(new Intent(ZmoLogInActivity.this, ZmoForgetPwdActivity.class));
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

	private void onSetState(boolean bEmailFlag) {
		if (bEmailFlag) {
			// email模式登录
			mNameEditView.onSetTextType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

			mEmailTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mEmailTabBtn.setTextColor(Color.WHITE);
			mMobileTabBtn.setBackgroundColor(Color.WHITE);
			mMobileTabBtn.setTextColor(Color.BLACK);
		} else {
			// mobile模式登录
			mNameEditView.onSetTextType(InputType.TYPE_CLASS_NUMBER);

			mMobileTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mMobileTabBtn.setTextColor(Color.WHITE);
			mEmailTabBtn.setBackgroundColor(Color.WHITE);
			mEmailTabBtn.setTextColor(Color.BLACK);
		}
	}

	private void onLogIn() {

		String mobileString = mNameEditView.onGetEditText();
		String pwdString = mPwdEditView.onGetEditText();

		if (TextUtils.isEmpty(mobileString)) {
			ToastUtil.onShowToast(this, "请输入手机号！");
			return;
		}

		if (TextUtils.isEmpty(pwdString)) {
			ToastUtil.onShowToast(this, "请输入密码！");
			return;
		}

		mLoginBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoLogInActivity.class) {

					mHandler.sendEmptyMessage(1);
				}
			};
		}.start();
	}

	@Override
	public void onRightClick() {

		startActivity(new Intent(this, ZmoRegisterActivity.class));
		finish();
	}
}
