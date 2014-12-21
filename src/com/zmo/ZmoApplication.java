package com.zmo;

import com.zmo.data.AccountData;
import com.zmo.utils.CommonUtil;

import net.tsz.afinal.FinalBitmap;
import android.app.Application;
import android.os.Environment;

public class ZmoApplication extends Application {

	private FinalBitmap mFinalBitmap = null;
	private static ZmoApplication instanceApplication;
	private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zmo/cache";

	@Override
	public void onCreate() {
		super.onCreate();
		instanceApplication = this;
	}

	public static ZmoApplication onGetInstance() {
		return instanceApplication;
	}

	public FinalBitmap onGetFinalBitmap() {
		if (null == mFinalBitmap) {
			mFinalBitmap = FinalBitmap.create(this);
			CommonUtil.onCreateDir(CACHE_PATH);
			mFinalBitmap.configDiskCachePath(CACHE_PATH);
			mFinalBitmap.configMemoryCacheSize(8 * 1024 * 1024);
		}

		return mFinalBitmap;
	}

	private AccountData mAccountData = null;

	public AccountData onGetAccountData() {
		if (null == mAccountData) {
			mAccountData = new AccountData().getAccountFromPre(this);
		}

		return mAccountData;
	}

	public void onSetAccountData(final AccountData data) {
		if (null == data)
			return;

		mAccountData = data;
		data.onSaveAccountToPre(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		if (null != mFinalBitmap)
			mFinalBitmap.clearMemoryCache();
	}
}
