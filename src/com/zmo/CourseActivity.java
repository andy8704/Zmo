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
import com.zmo.view.CourseHotFragment;
import com.zmo.view.CourseOfflineFragment;

/**
 * 
 * 
 * @类名称: CourseActivity
 * @描述: 课程展示
 * @时间: 2014-12-23 下午12:17:18
 * 
 */
public class CourseActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView mHotTabBtn;
	private TextView mVideoTabBtn;
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
	}

	private void initView() {
		mHotTabBtn = (TextView) findViewById(R.id.hot_tab_id);
		mVideoTabBtn = (TextView) findViewById(R.id.video_tab_id);
		mOfflineTabBtn = (TextView) findViewById(R.id.offline_tab_id);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_id);

		List<Fragment> viewList = new ArrayList<Fragment>(3);
		viewList.add(new CourseHotFragment());
		viewList.add(new CourseOfflineFragment());
		mPagerAdapter = new ZmoFragmentAdaper(getSupportFragmentManager(), viewList);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					onSetState(R.id.hot_tab_id);
				} else if (1 == position) {
					onSetState(R.id.video_tab_id);
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

		mHotTabBtn.setBackgroundColor(Color.WHITE);
		mHotTabBtn.setTextColor(Color.BLACK);

		mVideoTabBtn.setBackgroundColor(Color.WHITE);
		mVideoTabBtn.setTextColor(Color.BLACK);

		mOfflineTabBtn.setBackgroundColor(Color.WHITE);
		mOfflineTabBtn.setTextColor(Color.BLACK);

		switch (nId) {
		case R.id.hot_tab_id:
			mHotTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mHotTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.video_tab_id:
			mVideoTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			mVideoTabBtn.setTextColor(Color.WHITE);
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
		switch (v.getId()) {
		default:
			break;
		case R.id.hot_tab_id:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.video_tab_id:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.offline_tab_id:
			mViewPager.setCurrentItem(2);
			break;
		}
	}

}
