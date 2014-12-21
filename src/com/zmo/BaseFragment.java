package com.zmo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	protected View mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = setView();
		initView();
		return mContentView;
	}

	protected abstract View setView();

	protected abstract void initView();

//	public abstract boolean onBackPressed();

	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	protected Activity getBaseActivity() {
		return getActivity();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// super.startActivityForResult(intent, requestCode);
		getActivity().startActivityForResult(intent, requestCode);// fragment的startActivityForResult方法会修改请求码
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
}
