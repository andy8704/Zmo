package com.zmo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ad.util.ToastUtil;
import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.utils.StringUtil;
import com.zmo.view.ZmoEditText;

public class ZmoPwdReSetActivity extends ZmoBasicActivity {

	private ZmoEditText mPwd1View;
	private ZmoEditText mPwd2View;
	private CircularProgressButton mOkBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.zmo_set_new_pwd_activity);
		initView();
	}

	private void initView() {
		mPwd1View = (ZmoEditText) findViewById(R.id.pwd1_edit_id);
		mPwd2View = (ZmoEditText) findViewById(R.id.pwd2_edit_id);
		mOkBtn = (CircularProgressButton) findViewById(R.id.ok_btn_id);
		mOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onSubmit();
			}
		});
	}

	private void onSubmit() {

		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}
		
		String pwd1 = mPwd1View.onGetEditText();
		String pwd2 = mPwd2View.onGetEditText();
		
		if(TextUtils.isEmpty(pwd1) || StringUtil.isBlank(pwd1)){
			ToastUtil.onShowToast(this, "密码不能为空");
			return;
		}
		
		if(TextUtils.isEmpty(pwd2) || StringUtil.isBlank(pwd2)){
			ToastUtil.onShowToast(this, "确认密码不能为空");
			return;
		}
		
		if(!TextUtils.equals(pwd1, pwd2)){
			ToastUtil.onShowToast(this, "两次输入的密码不同");
			return;
		}
		

		new Thread() {
			public void run() {
				synchronized (ZmoPwdReSetActivity.this) {
					
				}
			};
		}.start();
	}
}
