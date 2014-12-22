package com.zmo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.util.NetWorkMonitor;
import com.ad.util.ToastUtil;
import com.ad.view.MyFullListView;
import com.zmo.adapter.CommentItemAdapter;
import com.zmo.model.CommentModel;
import com.zmo.model.CourseDetailModel;

public class CourseDetailActivity extends ZmoBasicActivity implements OnClickListener {

	private ImageView iv_CourseImageView;
	private TextView tv_CourseTypeView;
	private TextView tv_TutorView;
	private TextView tv_TagView;
	private TextView tv_CourseDespView;
	private TextView tv_PriceView;
	private Button b_SignBtn;
	private Button b_SaveBtn;

	private ImageView iv_TutorImageView;
	private TextView tv_TutorNameView;
	private TextView tv_TutorOccupationView;
	private TextView tv_TutorDespView;
	private TextView tv_CommentBtn;
	private MyFullListView mListView;
	private List<CommentModel> mCommentList;
	private CommentItemAdapter mAdapter = null;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
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
		setContentView(R.layout.course_detail_activity);

		initView();
		onGetCourseDetailInfo();
	}

	private void initView() {

		iv_CourseImageView = (ImageView) findViewById(R.id.img_view_id);
		tv_CourseTypeView = (TextView) findViewById(R.id.img_mark_id);
		tv_TutorView = (TextView) findViewById(R.id.course_tutor_name_id);
		tv_TagView = (TextView) findViewById(R.id.course_tag_view_id);
		tv_CourseDespView = (TextView) findViewById(R.id.course_desp_view_id);
		tv_PriceView = (TextView) findViewById(R.id.course_price_view_id);
		b_SignBtn = (Button) findViewById(R.id.signup_btn_id);
		b_SaveBtn = (Button) findViewById(R.id.save_btn_id);

		iv_TutorImageView = (ImageView) findViewById(R.id.tutor_imgview_id);
		tv_TutorNameView = (TextView) findViewById(R.id.tutor_name_id);
		tv_TutorOccupationView = (TextView) findViewById(R.id.tutor_occupation_id);
		tv_TutorDespView = (TextView) findViewById(R.id.tutor_desp_id);

		tv_CommentBtn = (TextView) findViewById(R.id.comment_btn_id);
		mListView = (MyFullListView) findViewById(R.id.listview_id);
		mCommentList = new ArrayList<CommentModel>(10);
		mAdapter = new CommentItemAdapter(this, mCommentList);
		mListView.setAdapter(mAdapter);

		b_SignBtn.setOnClickListener(this);
		b_SaveBtn.setOnClickListener(this);
		tv_CommentBtn.setOnClickListener(this);
	}

	@Override
	public void onRightClick() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.signup_btn_id:
			// 报名
			break;
		case R.id.save_btn_id:
			// 收藏
			break;
		case R.id.comment_btn_id:
			// 评论
			break;
		}
	}

	private void onGetCourseDetailInfo() {
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请链接网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (CourseDetailActivity.class) {

				}
			};
		}.start();
	}
	
	private void onSetData(final CourseDetailModel model){
		if(null == model)
			return;
		
		
	}
}
