package com.zmo.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmo.R;
import com.zmo.ZmoApplication;
import com.zmo.model.TutorModel;

public class TourteItemAdapter extends BaseAdapter {
	private Context mContext;
	private List<TutorModel> mDataList;

	public TourteItemAdapter(final Context context, final List<TutorModel> data) {
		mContext = context;
		mDataList = data;
	}

	@Override
	public int getCount() {
		return null == mDataList ? 0 : mDataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null == mDataList ? null : mDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (null == mDataList || mDataList.isEmpty())
			return null;

		Holder holder = null;
		if (null == arg1) {
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.zmo_tourte_item_adapter, null);
			holder = new Holder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}

		TutorModel model = mDataList.get(arg0);
		if (null != model) {
			holder.tv_NameView.setText(model.userName);
			holder.tv_DespView.setText(model.desp);
			holder.tv_FansView.setText(String.format(mContext.getString(R.string.tourte_fans_format_str), model.fansCount));
			if (!TextUtils.isEmpty(model.userIconUrl))
				ZmoApplication.onGetInstance().onGetFinalBitmap().display(holder.iv_imgView, model.userIconUrl);
			else {
				holder.iv_imgView.setImageResource(R.drawable.default_user_icon);
			}
		}

		return arg1;
	}

	public class Holder {
		public ImageView iv_imgView;
		public TextView tv_NameView;
		public TextView tv_FansView;
		public TextView tv_DespView;

		public Holder(final View view) {
			iv_imgView = (ImageView) view.findViewById(R.id.tutor_img_id);
			tv_NameView = (TextView) view.findViewById(R.id.tutor_name_id);
			tv_FansView = (TextView) view.findViewById(R.id.tutor_fans_id);
			tv_DespView = (TextView) view.findViewById(R.id.tutor_desp_id);

		}
	}
}
