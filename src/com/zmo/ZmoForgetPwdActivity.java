package com.zmo;

import com.ad.util.ToastUtil;
import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.view.ZmoEditText;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ZmoForgetPwdActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView mEmailTabBtn;
	private TextView mMobileTabBtn;

	private ZmoEditText mNameEditView;
	private ZmoEditText mCodeEditView;

	private CircularProgressButton mGetCodeBtn;
	private CircularProgressButton mOkBtn;
	private boolean bEmailFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: // 失败
				mOkBtn.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
				break;
			case 1: // 成功
				mOkBtn.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
				finish();
				break;
			case 2: // 验证码获取失败
				mGetCodeBtn.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
				break;
			case 3: // 验证码获取成功
				mGetCodeBtn.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.zmo_forget_pwd_activity);
		setTitle("忘记密码");
		initView();
	}

	private void initView() {
		mEmailTabBtn = (TextView) findViewById(R.id.email_tab_id);
		mMobileTabBtn = (TextView) findViewById(R.id.mobile_tab_id);
		mNameEditView = (ZmoEditText) findViewById(R.id.account_edit_id);
		mCodeEditView = (ZmoEditText) findViewById(R.id.code_edit_id);
		mGetCodeBtn = (CircularProgressButton) findViewById(R.id.get_code_btn_id);
		mGetCodeBtn.setIndeterminateProgressMode(true);
		mOkBtn = (CircularProgressButton) findViewById(R.id.ok_btn_id);
		mOkBtn.setIndeterminateProgressMode(true);

		mEmailTabBtn.setOnClickListener(this);
		mMobileTabBtn.setOnClickListener(this);
		mGetCodeBtn.setOnClickListener(this);
		mOkBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ok_btn_id:
			onSubmit();
			break;
		case R.id.email_tab_id:
			onSetState(true);
			break;
		case R.id.mobile_tab_id:
			onSetState(false);
			break;
		case R.id.get_code_btn_id:
			onGetVerifyCode();
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

	@Override
	public void onRightClick() {

	}

	private void onGetVerifyCode() {
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		mGetCodeBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoForgetPwdActivity.class) {

				}
			};
		}.start();
	}

	private void onSubmit() {

		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		if (bEmailFlag) {
			onEmail();
		} else {
			onMobile();
		}
	}

	private void onEmail() {
		String mobileString = mNameEditView.onGetEditText();
		String codeString = mCodeEditView.onGetEditText();

		if (TextUtils.isEmpty(mobileString)) {
			ToastUtil.onShowToast(this, "请输入邮箱！");
			return;
		}

		if (TextUtils.isEmpty(codeString)) {
			ToastUtil.onShowToast(this, "请输入验证码！");
			return;
		}

		mOkBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoForgetPwdActivity.class) {

				}
			};
		}.start();
	}

	private void onMobile() {

		String mobileString = mNameEditView.onGetEditText();
		String codeString = mCodeEditView.onGetEditText();

		if (TextUtils.isEmpty(mobileString)) {
			ToastUtil.onShowToast(this, "请输入手机号！");
			return;
		}

		if (TextUtils.isEmpty(codeString)) {
			ToastUtil.onShowToast(this, "请输入验证码！");
			return;
		}

		mOkBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoForgetPwdActivity.class) {

				}
			};
		}.start();
	}

}
