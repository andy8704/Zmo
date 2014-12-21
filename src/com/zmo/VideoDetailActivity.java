package com.zmo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ad.util.ToastUtil;
import com.zmo.adapter.CommentItemAdapter;
import com.zmo.model.CommentModel;
import com.zmo.utils.DisplayUtil;
import com.zmo.utils.NetWorkMonitor;
import com.zmo.view.MediaPlayerView;
import com.zmo.view.MyListView;

public class VideoDetailActivity extends ZmoBasicActivity {

	private MediaPlayerView mMediaView;
	private TextView tv_commentCountView;
	private Button b_commentBtn;
	private MyListView mListView;
	private List<CommentModel> mDataList;
	private CommentItemAdapter mAdapter;
	private TextView mMoreBtnView;
	private boolean bNextPageFlag = true;
	private final int PAGE_SIZE = 20;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			default:
				break;
			case 0:
				if (bNextPageFlag)
					mListView.addFooterView(mMoreBtnView);
				else
					mListView.removeFooterView(mMoreBtnView);
				mAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.video_detail_activity);
		initView();
		onGetData();
	}

	private void initView() {
		mMediaView = (MediaPlayerView) findViewById(R.id.media_view_id);
		tv_commentCountView = (TextView) findViewById(R.id.comment_count_id);
		b_commentBtn = (Button) findViewById(R.id.comment_btn_id);
		mListView = (MyListView) findViewById(R.id.listview_id);

		mDataList = new ArrayList<CommentModel>();
		mAdapter = new CommentItemAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);

		mMoreBtnView = new TextView(this);
		mMoreBtnView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(this, 60)));
		mMoreBtnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onGetNextPage();				
			}
		});
	}

	private void onGetData() {
		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (VideoDetailActivity.this) {

				}
			};
		}.start();

	}

	private void onGetNextPage() {
		if (!bNextPageFlag)
			return;

		if (!NetWorkMonitor.isConnect(this)) {
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (VideoDetailActivity.this) {

				}
			};
		}.start();
	}
}
