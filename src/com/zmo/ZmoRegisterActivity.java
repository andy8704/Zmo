package com.zmo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.util.ToastUtil;
import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.view.ZmoEditText;

public class ZmoRegisterActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView mEmailTabBtn;
	private TextView mMobileTabBtn;

	private ZmoEditText mNameEditView;
	private ZmoEditText mPwdEditView;
	private ZmoEditText mCodeEditView;

	private LinearLayout mGetMobileCodeLinearLayout;

	private CircularProgressButton mRegisterBtn;
	private CircularProgressButton mGetCodeBtn;
	private boolean bEmailFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: // 失败
				mRegisterBtn.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
				break;
			case 1: // 成功
				mRegisterBtn.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);

				startActivity(new Intent(ZmoRegisterActivity.this, ZmoAddInfoActivity.class));
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
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_register_activity);
		setTitle("注册");
		onRightVisible(true);
		onSetRightLable("登录");
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

		mGetMobileCodeLinearLayout = (LinearLayout) findViewById(R.id.get_code_linearlayout_id);

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
			onRegister();
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

			mGetMobileCodeLinearLayout.setVisibility(View.GONE);

			mEmailTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mEmailTabBtn.setTextColor(Color.WHITE);
			mMobileTabBtn.setBackgroundColor(Color.WHITE);
			mMobileTabBtn.setTextColor(Color.BLACK);

		} else {
			// mobile模式登录
			mNameEditView.onSetTextType(InputType.TYPE_CLASS_NUMBER);

			mGetMobileCodeLinearLayout.setVisibility(View.VISIBLE);

			mMobileTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mMobileTabBtn.setTextColor(Color.WHITE);
			mEmailTabBtn.setBackgroundColor(Color.WHITE);
			mEmailTabBtn.setTextColor(Color.BLACK);
		}
	}

	private void onGetVerifyCode() {
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		mGetCodeBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoRegisterActivity.class) {

				}
			};
		}.start();
	}

	private void onRegister() {

		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		if (bEmailFlag) {
			onEmailRegister();
		} else {
			onMobileRegister();
		}
	}

	private void onEmailRegister() {
		String mobileString = mNameEditView.onGetEditText();
		String pwdString = mPwdEditView.onGetEditText();

		if (TextUtils.isEmpty(mobileString)) {
			ToastUtil.onShowToast(this, "请输入邮箱！");
			return;
		}

		if (TextUtils.isEmpty(pwdString)) {
			ToastUtil.onShowToast(this, "请输入密码！");
			return;
		}

		// if(TextUtils.isEmpty(codeString)){
		// ToastUtil.onShowToast(this, "请输入验证码！");
		// return;
		// }

		mRegisterBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoRegisterActivity.class) {

				}
			};
		}.start();
	}

	private void onMobileRegister() {
		
		
		String mobileString = mNameEditView.onGetEditText();
		String pwdString = mPwdEditView.onGetEditText();
		String codeString = mCodeEditView.onGetEditText();

		if (TextUtils.isEmpty(mobileString)) {
			ToastUtil.onShowToast(this, "请输入手机号！");
			return;
		}

		if (TextUtils.isEmpty(pwdString)) {
			ToastUtil.onShowToast(this, "请输入密码！");
			return;
		}

		if (TextUtils.isEmpty(codeString)) {
			ToastUtil.onShowToast(this, "请输入验证码！");
			return;
		}

		mRegisterBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);

		new Thread() {
			public void run() {
				synchronized (ZmoRegisterActivity.class) {

				}
			};
		}.start();
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, ZmoLogInActivity.class));
		finish();
	}
}
