package com.zmo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.util.NetWorkMonitor;
import com.ad.util.ToastUtil;
import com.ad.view.MyFullGridView;
import com.zmo.adapter.TutorGridViewAdapter;
import com.zmo.model.ActivityDetailModel;
import com.zmo.model.TutorModel;

public class ActivityOfflineDetailActivity extends ZmoBasicActivity implements OnClickListener {

	private ImageView iv_ActivityImageView;
	private TextView tv_TitleView;
	private TextView tv_AddressView;
	private TextView tv_TimeView;
	private TextView tv_PriceView;
	private Button b_SignBtn;
	private Button b_SaveBtn;

	private TextView tv_ActivityDespView;
	private MyFullGridView mGridView;
	private List<TutorModel> mTutorList;
	private TutorGridViewAdapter mAdapter = null;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				onSetData(null);
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
		setContentView(R.layout.activity_offline_activity);

		initView();
		onGetActivityDetailInfo();
	}

	private void initView() {

		iv_ActivityImageView = (ImageView) findViewById(R.id.img_view_id);
		tv_TitleView = (TextView) findViewById(R.id.subject_view_id);
		tv_AddressView = (TextView) findViewById(R.id.addr_view_id);
		tv_TimeView = (TextView) findViewById(R.id.time_view_id);
		tv_PriceView = (TextView) findViewById(R.id.price_view_id);
		b_SignBtn = (Button) findViewById(R.id.signup_btn_id);
		b_SaveBtn = (Button) findViewById(R.id.save_btn_id);

		mGridView = (MyFullGridView) findViewById(R.id.listview_id);

		mTutorList = new ArrayList<TutorModel>(4);
		mAdapter = new TutorGridViewAdapter(this, mTutorList);
		mGridView.setAdapter(mAdapter);

		b_SignBtn.setOnClickListener(this);
		b_SaveBtn.setOnClickListener(this);
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
		}
	}

	private void onGetActivityDetailInfo() {
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请链接网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (ActivityOfflineDetailActivity.class) {

				}
			};
		}.start();
	}

	private void onSetData(final ActivityDetailModel model) {
		if (null == model)
			return;

		tv_TitleView.setText(String.format(getString(R.string.activity_theme_format_str), model.title));
		tv_AddressView.setText(String.format(getString(R.string.activity_address_format_str), model.address));
		tv_TimeView.setText(String.format(getString(R.string.activity_time_format_str), model.activityTime));
		tv_PriceView.setText(String.format(getString(R.string.course_price_format_str), model.price));

		if (!TextUtils.isEmpty(model.picUrl))
			ZmoApplication.onGetInstance().onGetFinalBitmap().display(iv_ActivityImageView, model.picUrl);

		tv_ActivityDespView.setText(model.desp);
	}
}
