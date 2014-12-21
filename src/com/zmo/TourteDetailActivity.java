package com.zmo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TourteDetailActivity extends ZmoBasicActivity implements OnClickListener {

	private ImageView iv_UserImgView;
	private TextView tv_NameView;
	private TextView tv_DespView;
	private Button b_SaveBtn;

	private TextView tv_courseBtn;
	private TextView tv_RelatedBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.zmo_tutor_detail_activity);
		initUI();
	}

	private void initUI() {
		iv_UserImgView = (ImageView) findViewById(R.id.tutor_img_id);
		tv_NameView = (TextView) findViewById(R.id.tutor_name_id);
		tv_DespView = (TextView) findViewById(R.id.tutor_desp_id);
		b_SaveBtn = (Button) findViewById(R.id.store_btn_id);

		tv_courseBtn = (TextView) findViewById(R.id.course_tab_id);
		tv_RelatedBtn = (TextView) findViewById(R.id.related_tab_id);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		default:
			break;
		case R.id.save_btn_id:
			break;
		case R.id.course_tab_id:
			onSetTabState(R.id.course_tab_id);
			break;
		case R.id.related_tab_id:
			onSetTabState(R.id.related_tab_id);
			break;
		}
	}
	
	private void onSaveTourte(){
		
	}
	
	private void onSetTabState(final int viewId){
		
		if(R.id.course_tab_id == viewId){
			tv_courseBtn.setTextColor(getResources().getColor(R.color.color_tool_text_select));
			tv_RelatedBtn.setTextColor(getResources().getColor(R.color.color_tool_text));
		}else if(R.id.related_tab_id == viewId){
			tv_courseBtn.setTextColor(getResources().getColor(R.color.color_tool_text));
			tv_RelatedBtn.setTextColor(getResources().getColor(R.color.color_tool_text_select));
		}
	}

}
