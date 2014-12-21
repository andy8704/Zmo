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

public class ZmoSaveListActivity extends ZmoBasicActivity implements OnClickListener {

	private TextView tv_CourseTabBtn;
	private TextView tv_ActivityTabBtn;
	private TextView tv_TutorTabBtn;

	private ViewPager mViewPager;
	private ZmoFragmentAdaper mPagerAdapter = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.zmo_save_list_activity);
		setTitle("收藏");
		initView();
		onSetState(R.id.course_tab_id);
	}

	private void initView() {
		tv_CourseTabBtn = (TextView) findViewById(R.id.course_tab_id);
		tv_ActivityTabBtn = (TextView) findViewById(R.id.activity_tab_id);
		tv_TutorTabBtn = (TextView) findViewById(R.id.tutor_tab_id);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_id);

		tv_CourseTabBtn.setOnClickListener(this);
		tv_ActivityTabBtn.setOnClickListener(this);
		tv_TutorTabBtn.setOnClickListener(this);

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
					onSetState(R.id.course_tab_id);
				} else if (1 == position) {
					onSetState(R.id.activity_tab_id);
				} else if (2 == position) {
					onSetState(R.id.tutor_tab_id);
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

		tv_CourseTabBtn.setBackgroundColor(Color.WHITE);
		tv_CourseTabBtn.setTextColor(Color.BLACK);

		tv_ActivityTabBtn.setBackgroundColor(Color.WHITE);
		tv_ActivityTabBtn.setTextColor(Color.BLACK);

		tv_TutorTabBtn.setBackgroundColor(Color.WHITE);
		tv_TutorTabBtn.setTextColor(Color.BLACK);

		switch (nId) {
		case R.id.course_tab_id:
			tv_CourseTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_CourseTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.activity_tab_id:
			tv_ActivityTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_ActivityTabBtn.setTextColor(Color.WHITE);
			break;
		case R.id.tutor_tab_id:
			tv_TutorTabBtn.setBackgroundColor(getResources().getColor(R.color.color_535353));
			tv_TutorTabBtn.setTextColor(Color.WHITE);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.course_tab_id:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.activity_tab_id:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.tutor_tab_id:
			mViewPager.setCurrentItem(2);
			break;
		}

	}
}
