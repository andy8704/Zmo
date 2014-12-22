package com.zmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.view.shapeimageview.CircularImageView;
import com.zmo.data.AccountData;
import com.zmo.view.ZmoOrderView;

public class ZmoOwnerFragment extends BaseFragment implements OnClickListener {

	private CircularImageView iv_UserImageView;
	private TextView tv_UserNameView;
	private TextView tv_UserOccupationView;
	private TextView tv_UserLevelView;
	private TextView tv_OrderCountView;
	private LinearLayout ll_OrderBtn;
	private TextView tv_SaveCountView;
	private LinearLayout ll_SaveBtn;
	private ZmoOrderView mOrderedView;
	private ZmoOrderView mUnOrderView;
	private ZmoOrderView mCloseOrderView;

	private ZmoOrderView mSaveActivityView;
	private ZmoOrderView mSaveCourseView;
	private ZmoOrderView mSaveTutorView;

	private TextView tv_SetBtn;
	private TextView tv_AboutBtn;

	@Override
	protected View setView() {
		return View.inflate(getActivity(), R.layout.zmo_owner_fragment, null);
	}

	@Override
	protected void initView() {

		iv_UserImageView = (CircularImageView) findViewById(R.id.user_img_id);
		tv_UserNameView = (TextView) findViewById(R.id.user_name_id);
		tv_UserOccupationView = (TextView) findViewById(R.id.user_occupation_id);
		tv_UserLevelView = (TextView) findViewById(R.id.user_level_id);

		ll_OrderBtn = (LinearLayout) findViewById(R.id.order_linearlayout_id);
		tv_OrderCountView = (TextView) findViewById(R.id.order_count_id);
		mOrderedView = (ZmoOrderView) findViewById(R.id.ordered_view_id);
		mUnOrderView = (ZmoOrderView) findViewById(R.id.un_order_view_id);
		mCloseOrderView = (ZmoOrderView) findViewById(R.id.close_order_view_id);

		ll_SaveBtn = (LinearLayout) findViewById(R.id.save_linearlayout_id);
		tv_SaveCountView = (TextView) findViewById(R.id.save_count_id);
		mSaveActivityView = (ZmoOrderView) findViewById(R.id.save_activity_view_id);
		mSaveCourseView = (ZmoOrderView) findViewById(R.id.save_course_view_id);
		mSaveTutorView = (ZmoOrderView) findViewById(R.id.save_tutor_view_id);

		tv_SetBtn = (TextView) findViewById(R.id.set_btn_id);
		tv_AboutBtn = (TextView) findViewById(R.id.about_btn_id);

		ll_OrderBtn.setOnClickListener(this);
		ll_SaveBtn.setOnClickListener(this);
		tv_SetBtn.setOnClickListener(this);
		tv_AboutBtn.setOnClickListener(this);

		onRegisterAccountChangeListener();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		unRegisterAccountChangeListener();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.order_linearlayout_id:
			startActivity(new Intent(getActivity(), ZmoOrderListActivity.class));
			break;
		case R.id.save_linearlayout_id:
			startActivity(new Intent(getActivity(), ZmoSaveListActivity.class));
			break;
		case R.id.set_btn_id:
			startActivity(new Intent(getActivity(), ZmoSetActivity.class));
			break;
		case R.id.about_btn_id:
			break;

		default:
			break;
		}

	}

	private SharedPreferences mAccountSharePreferences = null;

	private void onRegisterAccountChangeListener() {

		mAccountSharePreferences = getActivity().getSharedPreferences(AccountData.KEY_ACCOUNT_SHAPE, 0);
		mAccountSharePreferences.registerOnSharedPreferenceChangeListener(mShareListener);
	}

	private OnSharedPreferenceChangeListener mShareListener = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (!TextUtils.isEmpty(key)) {
				if (TextUtils.equals(key, AccountData.KEY_NICK_NAME)) {
					String nickName = sharedPreferences.getString(key, "");
					tv_UserNameView.setText(nickName);
				} else if (TextUtils.equals(key, AccountData.KEY_OCCUPATION)) {
					String occupation = sharedPreferences.getString(key, "");
					tv_UserOccupationView.setText(occupation);
				} else if (TextUtils.equals(key, AccountData.KEY_USER_ICON)) {
					String userIcon = sharedPreferences.getString(key, "");
					if (!TextUtils.isEmpty(userIcon))
						ZmoApplication.onGetInstance().onGetFinalBitmap().display(iv_UserImageView, userIcon);
				}

			}
		}
	};

	private void unRegisterAccountChangeListener() {

		if (null != mAccountSharePreferences && null != mShareListener)
			mAccountSharePreferences.unregisterOnSharedPreferenceChangeListener(mShareListener);
	}

}
