package com.zmo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ad.util.ToastUtil;
import com.ad.view.progressbutton.CircularProgressButton;
import com.zmo.view.ZmoEditText;

public class ZmoAddInfoActivity extends ZmoBasicActivity{

	private ZmoEditText mNameEditText;
	private ZmoEditText mOccupationText;
	private CircularProgressButton mOkBtn;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.zmo_userinfo_edit_activity);
		
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
		
		new Thread(){
			public void run() {
				synchronized (ZmoAddInfoActivity.class) {
					// 
				}
			};
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
