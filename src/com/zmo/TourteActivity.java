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
import com.zmo.adapter.TourteItemAdapter;
import com.zmo.model.TutorModel;
import com.zmo.utils.CommonDefine;
import com.zmo.utils.NetWorkMonitor;

/**
 * 
 * 
 * @类名称: TourteActivity
 * @描述: 导师列表展示
 * @时间: 2014-12-23 下午12:02:46
 * 
 */
public class TourteActivity extends ZmoBasicActivity implements IXListViewListener, OnItemClickListener {

	private PullListView mPullListView;
	private List<TutorModel> mDataList;
	private TourteItemAdapter mAdapter;

	private boolean bNextPageFlag = true;
	private Handler mHanlder = new Handler() {
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
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.tourte_activity);
		setTitle(R.string.tourte_title_str);
		initView();
	}

	private void initView() {
		mPullListView = (PullListView) findViewById(R.id.listview_id);
		mPullListView.setPullRefreshEnable(true);
		mPullListView.setPullLoadEnable(false);
		mPullListView.setXListViewListener(this);
		mPullListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
		if (null != mDataList && !mDataList.isEmpty()) {
			if (mDataList.size() > postion) {
				TutorModel model = mDataList.get(postion);
				if (null != model) {
					Intent intent = new Intent(TourteActivity.this, TourteDetailActivity.class);
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
				synchronized (TourteActivity.this) {

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
				synchronized (TourteActivity.this) {

				}
			};
		}.start();
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub

	}
}
