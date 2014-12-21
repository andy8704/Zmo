package com.zmo;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.ad.util.ToastUtil;
import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.view.ZmoEditText;

public class ZmoAddInfoActivity extends ZmoBasicActivity{

	private ZmoEditText mNameEditText;
	private ZmoEditText mOccupationText;
	private CircularProgressButton mOkBtn;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mOkBtn.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
				break;
			case 1:
				mOkBtn.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
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
		setContentView(R.layout.zmo_userinfo_edit_activity);
		setTitle("完善资料");
		initView();
	}
	
	private void initView(){
		mNameEditText = (ZmoEditText) findViewById(R.id.name_edit_id);
		mOccupationText = (ZmoEditText) findViewById(R.id.work_edit_id);
		mOkBtn = (CircularProgressButton) findViewById(R.id.ok_btn_id);
		
		mOkBtn.setIndeterminateProgressMode(true);
		mOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onSubmit();
			}
		});
	}
	
	private void onSubmit(){
		
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}
		
		String nameString = mNameEditText.onGetEditText();
		String occupationString = mOccupationText.onGetEditText();
		
		if(TextUtils.isEmpty(nameString)){
			ToastUtil.onShowToast(this, "名称不能为空!");
			return;
		}
		
		if(TextUtils.isEmpty(occupationString)){
			ToastUtil.onShowToast(this, "职业不能为空!");
			return;
		}
		
		mOkBtn.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);
		
		new Thread(){
			public void run() {
				synchronized (ZmoAddInfoActivity.class) {
					// 
					
					mHandler.sendEmptyMessage(1);
				}
			};
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRightClick() {
		
	}
}
