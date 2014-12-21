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
import com.zmo.model.VideoModel;
import com.zmo.utils.ForumUtil;

public class VideoItemAdapter extends BaseAdapter{
	private Context mContext;
	private List<VideoModel> mDataList;

	public VideoItemAdapter(final Context context, final List<VideoModel> data) {
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.zmo_video_item_adapter, null);
			holder = new Holder();
			holder.iv_imgView = (ImageView) arg1.findViewById(R.id.video_img_id);
			holder.tv_titleView = (TextView) arg1.findViewById(R.id.video_title_id);
			
			arg1.setTag(holder);
		}else {
			holder = (Holder) arg1.getTag();
		}
		
		VideoModel model = mDataList.get(arg0);
		if (null != model) {
			holder.tv_titleView.setText(model.title);
			if(!TextUtils.isEmpty(model.videoPicUrl))
				ZmoApplication.onGetInstance().onGetFinalBitmap().display(holder.iv_imgView, model.videoPicUrl);
			else {
				holder.iv_imgView.setImageResource(R.drawable.default_user_icon);
			}
		}
		
		return arg1;
	}
	
	public class Holder {
		public ImageView iv_imgView;
		public TextView tv_titleView;
	}
}
