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
import com.zmo.model.CourseModel;

public class CourseItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<CourseModel> mDataList;

	public CourseItemAdapter(Context context, List<CourseModel> data) {
		mContext = context;
		mDataList = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == mDataList ? 0 : mDataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null == mDataList ? null : mDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (null == mDataList || mDataList.isEmpty())
			return null;

		Holder holder = null;
		if (null == contentView) {
			contentView = LayoutInflater.from(mContext).inflate(R.layout.course_item_view, null);
			holder = new Holder(contentView);
			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}

		CourseModel model = mDataList.get(arg0);
		if (null != model) {
			if (!TextUtils.isEmpty(model.picUrl))
				ZmoApplication.onGetInstance().onGetFinalBitmap().display(holder.iv_ImgView, model.picUrl);

			holder.tv_TypeView.setText(model.courseType);
			holder.tv_TitleView.setText(model.title);
			holder.tv_PriceView.setText(String.format(mContext.getString(R.string.activity_price_format_str), model.price > 0 ? String.valueOf(model.price)
					: "免费"));
			holder.tv_TagView.setText(model.lable);
		}
		return contentView;
	}

	public class Holder {
		public ImageView iv_ImgView;
		public TextView tv_TypeView;
		public TextView tv_TitleView;
		public TextView tv_PriceView;
		public TextView tv_TagView;

		public Holder(final View view) {
			if (null == view)
				return;

			iv_ImgView = (ImageView) view.findViewById(R.id.img_view_id);
			tv_TypeView = (TextView) view.findViewById(R.id.img_mark_id);
			tv_TitleView = (TextView) view.findViewById(R.id.title_view_id);
			tv_TagView =(TextView) view.findViewById(R.id.label_view_id);
			tv_PriceView = (TextView) view.findViewById(R.id.desp_view_id);
		}
	}
}
