package com.zmo;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zmo.adapter.ZmoFragmentAdaper;
import com.zmo.view.CloseOrderFragment;
import com.zmo.view.PayedOrderFragment;
import com.zmo.view.UnPayedOrderFragment;

public class ZmoOrderListActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView tv_PayedTabBtn;
	private TextView tv_UnPayedTabBtn;
	private TextView tv_CloseTabBtn;

	private ViewPager mViewPager;
	private ZmoFragmentAdaper mPagerAdapter = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_order_list_activity);
		setTitle("订单");
		initView();
		onSetState(R.id.payed_tab_id);
	}

	private void initView() {
		tv_PayedTabBtn = (TextView) findViewById(R.id.payed_tab_id);
		tv_UnPayedTabBtn = (TextView) findViewById(R.id.unpay_tab_id);
		tv_CloseTabBtn = (TextView) findViewById(R.id.close_tab_id);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_id);

		tv_PayedTabBtn.setOnClickListener(this);
		tv_UnPayedTabBtn.setOnClickListener(this);
		tv_CloseTabBtn.setOnClickListener(this);

		List<Fragment> viewList = new ArrayList<Fragment>(3);
		viewList.add(new PayedOrderFragment());
		viewList.add(new UnPayedOrderFragment());
		viewList.add(new CloseOrderFragment());
		mPagerAdapter = new ZmoFragmentAdaper(getSupportFragmentManager(), viewList);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					onSetState(R.id.payed_tab_id);
				} else if (1 == position) {
					onSetState(R.id.unpay_tab_id);
				} else if (2 == position) {
					onSetState(R.id.close_tab_id);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}

	private void onSetState(int nId) {

		tv_PayedTabBtn.setBackgroundColor(Color.WHITE);
		tv_PayedTabBtn.setTextColor(Color.BLACK);

		tv_UnPayedTabBtn.setBackgroundColor(Color.WHITE);
		tv_UnPayedTabBtn.setTextColor(Color.BLACK);

		tv_CloseTabBtn.setBackgroundColor(Color.WHITE);
		tv_CloseTabBtn.setTextColor(Color.BLACK);

		switch (nId) {
		case R.id.payed_tab_id:
			tv_PayedTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_PayedTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.unpay_tab_id:
			tv_UnPayedTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_UnPayedTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.close_tab_id:
			tv_CloseTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_CloseTabBtn.setTextColor(Color.WHITE);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.payed_tab_id:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.unpay_tab_id:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.close_tab_id:
			mViewPager.setCurrentItem(2);
			break;
		}

	}
}
