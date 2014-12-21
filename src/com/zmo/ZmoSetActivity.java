package com.zmo;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.view.shapeimageview.CircularImageView;
import com.zmo.data.AccountData;
import com.zmo.listener.PicMenuListener;
import com.zmo.view.PicMenuDlg;

public class ZmoSetActivity extends ZmoBasicActivity implements OnClickListener {

	private RelativeLayout ll_PicBtn;
	private LinearLayout ll_NameBtn;
	private LinearLayout ll_OccupationBtn;
	private CircularImageView iv_UserIconView;
	private TextView tv_UserNameView;
	private TextView tv_UserOccupationView;
	private LinearLayout ll_ModifyPwdBtn;
	private LinearLayout ll_ExitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zmo_set_activity);
		setTitle(R.string.set_title_str);
		initView();
	}

	private void initView() {
		ll_PicBtn = (RelativeLayout) findViewById(R.id.pic_btn_id);
		iv_UserIconView = (CircularImageView) findViewById(R.id.img_view_id);
		ll_NameBtn = (LinearLayout) findViewById(R.id.name_btn_id);
		tv_UserNameView = (TextView) findViewById(R.id.name_view_id);
		ll_OccupationBtn = (LinearLayout) findViewById(R.id.occupation_btn_id);
		tv_UserOccupationView = (TextView) findViewById(R.id.occupation_view_id);
		ll_ModifyPwdBtn = (LinearLayout) findViewById(R.id.modify_pwd_btn_id);
		ll_ExitBtn = (LinearLayout) findViewById(R.id.exit_btn_id);

		ll_PicBtn.setOnClickListener(this);
		ll_NameBtn.setOnClickListener(this);
		ll_OccupationBtn.setOnClickListener(this);
		ll_ModifyPwdBtn.setOnClickListener(this);
		ll_ExitBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		default:
			break;
		case R.id.pic_btn_id:
			onShowMenu();
			break;
		case R.id.name_btn_id:
			onShowEditDlg(tv_UserNameView.getText().toString(), tv_UserNameView);
			break;
		case R.id.occupation_btn_id:
			onShowEditDlg(tv_UserOccupationView.getText().toString(), tv_UserOccupationView);
			break;
		case R.id.modify_pwd_btn_id:
			startActivity(new Intent(ZmoSetActivity.this, ZmoPwdManagerActivity.class));
			break;
		case R.id.exit_btn_id:
			onExit();
			break;
		}
	}

	private void onExit() {
		AccountData accountData = ZmoApplication.onGetInstance().onGetAccountData();
		accountData.bIsLogIn = false;
		accountData.onClearData(this);
		ZmoApplication.onGetInstance().onSetAccountData(accountData);
	}

	private void onShowMenu() {
		PicMenuDlg dlg = new PicMenuDlg(this);
		dlg.setPicMenuListener(new PicMenuListener() {
			@Override
			public void onPhotograph() {
				onGetPicFromContent();
			}

			@Override
			public void onCamera() {
				onGetPic();
			}
		});
		dlg.show();
	}

	private void onShowEditDlg(final String defaultStr, final TextView textView) {
		final Dialog dlg = new Dialog(this, R.style.transparent);
		View view = View.inflate(this, R.layout.edit_text_dlg, null);
		final EditText editText = (EditText) view.findViewById(R.id.edit_id);
		TextView cancelView = (TextView) view.findViewById(R.id.cancel_btn_id);
		TextView okView = (TextView) view.findViewById(R.id.ok_btn_id);
		editText.setText(defaultStr);
		cancelView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
			}
		});

		okView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
				if (null != textView)
					textView.setText(editText.getText().toString());
			}
		});
		dlg.setContentView(view);
		dlg.show();
	}

	private void onGetPicFromContent() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 1);
	}

	private String mTempPath;

	/**
	 * 
	 * @Description: 进行拍照采集
	 * @param
	 * @return void
	 * @throws
	 */
	private void onGetPic() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Intent intent_camera = getPackageManager().getLaunchIntentForPackage("com.android.camera");
			if (intent_camera != null) {
				getImageByCamera.setPackage("com.android.camera");
			}
			mTempPath = getSeedFileName(this, "temp.jpg");
			File out = new File(mTempPath);
			Uri uri = Uri.fromFile(out);
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(getImageByCamera, 2);
		}
	}

	private String getSeedFileName(final Context context, String seedName) {
		// 修改存放路径
		String myDir = Environment.getExternalStorageDirectory().toString() + "/data/" + getPackageName() + "/camera";
		String myFile = myDir + "/" + seedName;
		File dir = new File(myDir);
		File file = new File(myFile);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 修改权限
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("chmod 755 " + myDir);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		try {
			runtime.exec("chmod 755 " + myFile);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return myFile;
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) { // 图库返回
			Uri uri = data.getData();
			if (uri == null) {
				return;
			}
			startPhotoZoom(uri);
		} else if (requestCode == 2 && resultCode == Activity.RESULT_OK) { // 拍照返回
			startPhotoZoom(Uri.fromFile(new File(mTempPath)));
		} else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
			if (data == null) {
				return;
			}
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap parcelable = extras.getParcelable("data");
				if (null != parcelable) {
					// mPhotoBmp = ForumUtil.toRoundBitmap(mPhotoBmp);
					if (null != parcelable)
						iv_UserIconView.setImageBitmap(parcelable);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		
	}
}
