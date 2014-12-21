package com.zmo;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.ad.util.ToastUtil;
import com.zmo.utils.NetWorkMonitor;

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

	}

}
