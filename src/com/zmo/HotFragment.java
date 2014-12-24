package com.zmo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ad.util.DisplayUtil;
import com.ad.util.ToastUtil;
import com.zmo.model.ActivityModel;
import com.zmo.model.CourseModel;
import com.zmo.model.TutorModel;
import com.zmo.model.VideoModel;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.view.ActivityItemView;
import com.zmo.view.CourseItemView;
import com.zmo.view.FunctionRegionView;
import com.zmo.view.TutorItemView;
import com.zmo.view.VideoItemView;

public class HotFragment extends BaseFragment {

	private LinearLayout mGroupViewLayout;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				onSetData();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected View setView() {
		return View.inflate(getActivity(), R.layout.hot_fragment, null);
	}

	@Override
	protected void initView() {
		mGroupViewLayout = (LinearLayout) findViewById(R.id.hot_fragment_id);
	}

	@Override
	public void onStart() {
		super.onStart();

		// onGetData();
		onSetData();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void onGetData() {

		if (!NetWorkMonitor.isConnect(getActivity())) {
			ToastUtil.onShowToast(getActivity(), "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (HotFragment.class) {

				}
			};
		}.start();
	}

	private void onSetData() {

		CourseModel courseModel1 = new CourseModel();
		courseModel1.title = "一起创业的小伙伴";
		courseModel1.lable = "创业，小伙伴";
		courseModel1.courseType = "视频课程";
		courseModel1.desp = "教你怎样解决创业过程中可能遇到的弯路和解决的思路及方法";

		List<CourseModel> courseModels = new ArrayList<CourseModel>(2);
		courseModels.add(courseModel1);
		courseModels.add(courseModel1);
		onSetCourseData(28, courseModels);

		ActivityModel activityModel1 = new ActivityModel();
		activityModel1.title = "新能源汽车";
		activityModel1.courseType = "线上";
		activityModel1.desp = "新能源汽车的发展前景";
		List<ActivityModel> activityModels = new ArrayList<ActivityModel>(2);
		activityModels.add(activityModel1);
		activityModels.add(activityModel1);
		onSetActivityData(22, activityModels);

		VideoModel videoModel1 = new VideoModel();
		videoModel1.title = "一个小孩独自生活的故事";
		videoModel1.playTime = "12:31";
		List<VideoModel> videoModels = new ArrayList<VideoModel>(2);
		videoModels.add(videoModel1);
		videoModels.add(videoModel1);
		onSetVideoData(21, videoModels);

		TutorModel tutorModel1 = new TutorModel();
		tutorModel1.title = "刘邓明";
		tutorModel1.desp = "清华大学总裁班营销专家";
		List<TutorModel> tutorModels = new ArrayList<TutorModel>(2);
		tutorModels.add(tutorModel1);
		tutorModels.add(tutorModel1);
		onSetTutorData(20, tutorModels);
	}

	private void onSetCourseData(final int nCount, final List<CourseModel> data) {
		FunctionRegionView courseView = new FunctionRegionView(getActivity());
		courseView.onSetBasicInfo(getString(R.string.save_course_lable), nCount);
		if (null != data && !data.isEmpty()) {
			for (CourseModel ele : data) {
				if (null != ele) {
					CourseItemView course = new CourseItemView(getActivity());
					course.onSetData(ele);
					course.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {

						}
					});
					courseView.onAddOneItemView(course);
				}
			}
		}

		courseView.addDetailClick(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), CourseActivity.class));
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = DisplayUtil.dip2px(getActivity(), 20);
		mGroupViewLayout.addView(courseView, params);
	}

	private void onSetActivityData(final int nCount, final List<ActivityModel> data) {

		FunctionRegionView activityView = new FunctionRegionView(getActivity());
		activityView.onSetBasicInfo(getString(R.string.save_activity_lable), nCount);
		if (null != data && !data.isEmpty()) {
			for (ActivityModel ele : data) {
				if (null != ele) {
					ActivityItemView activity = new ActivityItemView(getActivity());
					activity.onSetData(ele);
					activity.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {

						}
					});
					activityView.onAddOneItemView(activity);
				}
			}
		}

		activityView.addDetailClick(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), ActivityActivity.class));
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = DisplayUtil.dip2px(getActivity(), 20);
		mGroupViewLayout.addView(activityView, params);
	}

	private void onSetVideoData(final int nCount, final List<VideoModel> data) {

		FunctionRegionView videoView = new FunctionRegionView(getActivity());
		videoView.onSetBasicInfo(getString(R.string.video_title_str), nCount);
		if (null != data && !data.isEmpty()) {
			for (VideoModel ele : data) {
				if (null != ele) {
					VideoItemView video = new VideoItemView(getActivity());
					video.onSetData(ele);
					video.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {

						}
					});
					videoView.onAddOneItemView(video);
				}
			}
		}

		videoView.addDetailClick(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), VideoActivity.class));
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = DisplayUtil.dip2px(getActivity(), 20);
		mGroupViewLayout.addView(videoView, params);
	}

	private void onSetTutorData(final int nCount, final List<TutorModel> data) {
		FunctionRegionView tutorView = new FunctionRegionView(getActivity());
		tutorView.onSetBasicInfo(getString(R.string.save_tutor_lable), nCount);
		if (null != data && !data.isEmpty()) {
			for (TutorModel ele : data) {
				if (null != ele) {
					TutorItemView tutor = new TutorItemView(getActivity());
					tutor.onSetData(ele);
					tutor.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {

						}
					});
					tutorView.onAddOneItemView(tutor);
				}
			}
		}

		tutorView.addDetailClick(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), TourteActivity.class));
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = DisplayUtil.dip2px(getActivity(), 20);
		mGroupViewLayout.addView(tutorView, params);
	}
}
