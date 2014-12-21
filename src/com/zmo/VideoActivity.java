package com.zmo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ad.util.ToastUtil;
import com.ad.view.PullListView;
import com.ad.view.PullListView.IXListViewListener;
import com.zmo.adapter.VideoItemAdapter;
import com.zmo.model.VideoModel;
import com.zmo.utils.CommonDefine;
import com.zmo.utils.NetWorkMonitor;

public class VideoActivity extends ZmoBasicActivity implements IXListViewListener, OnItemClickListener {

	private PullListView mPullListView = null;
	private List<VideoModel> mDataList = null;
	private VideoItemAdapter mAdapter = null;
	private boolean bNextPageFlag = true;
	private Handler mHanlder = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mAdapter.notifyDataSetChanged();
				setRefreshTime();
				mPullListView.stopRefresh();
				if (bNextPageFlag)
					mPullListView.setPullLoadEnable(true);
				else
					mPullListView.setPullLoadEnable(false);
				break;

			case 1:
				mAdapter.notifyDataSetChanged();
				mPullListView.stopLoadMore();
				if (bNextPageFlag)
					mPullListView.setPullLoadEnable(true);
				else
					mPullListView.setPullLoadEnable(false);
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 设置上次更新时间
	 */
	private void setRefreshTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		format.format(date);
		mPullListView.setRefreshTime(format.format(date));
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.video_activity);
		setTitle(R.string.video_title_str);

		mPullListView = (PullListView) findViewById(R.id.pull_listview_id);
		mPullListView.setPullRefreshEnable(true);
		mPullListView.setPullLoadEnable(false);
		mPullListView.setXListViewListener(this);
		mPullListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {

		if(null != mDataList && !mDataList.isEmpty()){
			if(mDataList.size() > postion){
				VideoModel model = mDataList.get(postion);
				if(null != model){
					Intent intent = new Intent(VideoActivity.this, VideoDetailActivity.class);
					intent.putExtra(CommonDefine.INTENT_DATA, model);
					startActivity(intent);
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		onGetData();
	}

	@Override
	public void onLoadMore() {
		onGetNextPage();
	}

	private void onGetData() {
		if (!NetWorkMonitor.isConnect(this)) {
			mPullListView.stopRefresh();
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (VideoActivity.this) {

				}
			};
		}.start();

	}

	private void onGetNextPage() {
		if (!bNextPageFlag) {
			mPullListView.stopLoadMore();
			return;
		}

		if (!NetWorkMonitor.isConnect(this)) {
			mPullListView.stopLoadMore();
			ToastUtil.onShowToast(this, "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (VideoActivity.this) {

				}
			};
		}.start();
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		
	}
}
