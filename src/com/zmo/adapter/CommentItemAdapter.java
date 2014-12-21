package com.zmo.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.zmo.model.CommentModel;
import com.zmo.utils.ForumUtil;

public class CommentItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<CommentModel> mDataList;

	public CommentItemAdapter(final Context context, final List<CommentModel> data) {
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
		if(null == mDataList || mDataList.isEmpty())
			return null;
		
		Holder holder = null;
		if(null == arg1){
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.comment_item_adapter, null);
			holder = new Holder();
			holder.iv_imgView = (ImageView) arg1.findViewById(R.id.img_view_id);
			holder.tv_timeView = (TextView) arg1.findViewById(R.id.time_id);
			holder.tv_nameView = (TextView) arg1.findViewById(R.id.name_id);
			holder.tv_contentView = (TextView) arg1.findViewById(R.id.content_id);
			
			arg1.setTag(holder);
		}else {
			holder = (Holder) arg1.getTag();
		}
		
		CommentModel model = mDataList.get(arg0);
		if (null != model) {
			holder.tv_nameView.setText(model.userName);
			holder.tv_contentView.setText(model.content);
			holder.tv_timeView.setText(onGetTime(model.time));
			if(!TextUtils.isEmpty(model.userIconUrl))
				ZmoApplication.onGetInstance().onGetFinalBitmap().display(holder.iv_imgView, model.userIconUrl);
			else {
				holder.iv_imgView.setImageResource(R.drawable.default_user_icon);
			}
		}
		
		return arg1;
	}
	
	@SuppressLint("SimpleDateFormat") private String onGetTime(final long timeMill){
		return new SimpleDateFormat(com.zmo.utils.DateFormat.DEFAULT_DATETIME_FORMAT_SEC).format(new Date(timeMill));
	}

	public class Holder {
		public ImageView iv_imgView;
		public TextView tv_timeView;
		public TextView tv_nameView;
		public TextView tv_contentView;
	}
}
