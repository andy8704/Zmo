package com.zmo;

import java.util.ArrayList;
import java.util.List;

import com.zmo.adapter.ZmoFragmentAdaper;
import com.zmo.view.CloseOrderFragment;
import com.zmo.view.OfflineActivityFragment;
import com.zmo.view.OnlineActivityFragment;
import com.zmo.view.PayedOrderFragment;
import com.zmo.view.UnPayedOrderFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * 
 * @类名称: ActivityActivity
 * @描述: 活动展示
 * @时间: 2014-12-23 下午12:17:18
 * 
 */
public class ActivityActivity extends ZmoBasicActivity implements OnClickListener{
	
	private TextView mOnlineTabBtn;
	private TextView mOfflineTabBtn;
	
	private ViewPager mViewPager;
	private ZmoFragmentAdaper mPagerAdapter = null; 
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_activity);
		setTitle(R.string.save_activity_lable);
		
		initView();
		onSetState(R.id.online_tab_id);
	}
	
	private void initView(){
		mOnlineTabBtn = (TextView) findViewById(R.id.online_tab_id);
		mOfflineTabBtn = (TextView) findViewById(R.id.offline_tab_id);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_id);
		
		mOnlineTabBtn.setOnClickListener(this);
		mOfflineTabBtn.setOnClickListener(this);
		
		List<Fragment> viewList = new ArrayList<Fragment>(2);
		viewList.add(new OnlineActivityFragment());
		viewList.add(new OfflineActivityFragment());
		mPagerAdapter = new ZmoFragmentAdaper(getSupportFragmentManager(), viewList);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					onSetState(R.id.online_tab_id);
				} else if (1 == position) {
					onSetState(R.id.offline_tab_id);
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

		mOnlineTabBtn.setBackgroundColor(Color.WHITE);
		mOnlineTabBtn.setTextColor(Color.BLACK);

		mOfflineTabBtn.setBackgroundColor(Color.WHITE);
		mOfflineTabBtn.setTextColor(Color.BLACK);

		switch (nId) {
		case R.id.online_tab_id:
			mOnlineTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mOnlineTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.offline_tab_id:
			mOfflineTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mOfflineTabBtn.setTextColor(Color.WHITE);
			break;
		}
	}

	@Override
	public void onRightClick() {
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		default:
			break;
		case R.id.online_tab_id:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.offline_tab_id:
			mViewPager.setCurrentItem(1);
			break;
		}
	}

}
