package com.zmo.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ZmoFragmentAdaper extends FragmentPagerAdapter {

	private List<Fragment> mFragmentList = null;

	public ZmoFragmentAdaper(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public ZmoFragmentAdaper(FragmentManager fm, List<Fragment> data) {
		super(fm);

		mFragmentList = data;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return null != mFragmentList && !mFragmentList.isEmpty() ? mFragmentList.get(arg0) : null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null != mFragmentList && !mFragmentList.isEmpty() ? mFragmentList.size() : 0;
	}

}
