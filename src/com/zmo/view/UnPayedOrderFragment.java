package com.zmo.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ad.util.ToastUtil;
import com.ad.view.PullListView;
import com.ad.view.PullListView.IXListViewListener;
import com.zmo.BaseFragment;
import com.zmo.R;
import com.zmo.adapter.ZmoUnOrderAdapter;
import com.zmo.model.OrderInfo;
import com.zmo.utils.NetWorkMonitor;

public class UnPayedOrderFragment extends BaseFragment implements IXListViewListener, OnItemClickListener {

	private PullListView mPullListView;
	private ZmoUnOrderAdapter mAdapter = null;
	private List<OrderInfo> mDataList;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View setView() {
		return View.inflate(getActivity(), R.layout.listview_view, null);
	}

	@Override
	protected void initView() {
		mPullListView = (PullListView) findViewById(R.id.pull_listview_id);
		mPullListView.setPullRefreshEnable(true);
		mPullListView.setPullLoadEnable(false);
		mPullListView.setXListViewListener(this);
		mPullListView.setOnItemClickListener(this);

		mDataList = new ArrayList<OrderInfo>();
		mAdapter = new ZmoUnOrderAdapter(getActivity(), mDataList);
		mPullListView.setAdapter(mAdapter);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser)
			onGetData();
	}

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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
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
		if (!NetWorkMonitor.isConnect(getActivity())) {
			mPullListView.stopRefresh();
			ToastUtil.onShowToast(getActivity(), "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (UnPayedOrderFragment.this) {

				}
			};
		}.start();

	}

	private void onGetNextPage() {
		if (!bNextPageFlag) {
			mPullListView.stopLoadMore();
			return;
		}

		if (!NetWorkMonitor.isConnect(getActivity())) {
			mPullListView.stopLoadMore();
			ToastUtil.onShowToast(getActivity(), "请开启网络");
			return;
		}

		new Thread() {
			public void run() {
				synchronized (UnPayedOrderFragment.this) {

				}
			};
		}.start();
	}

}
