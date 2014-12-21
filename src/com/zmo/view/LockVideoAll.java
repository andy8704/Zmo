package com.zmo.view;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zmo.R;

public class LockVideoAll extends Activity implements OnPreparedListener, OnErrorListener, OnClickListener, OnCompletionListener {
	private View mLoadingView;
	private MyVideoView videoView;
	private Window window;
	private MyMediaController c;
	private RelativeLayout playerContent;
	// private BMediaController mVVCtl = null;
	public static boolean isFullScreen = false;
	private ImageView mVideoBg;
	public static boolean isPrepared = false;
	private int currentPosition;
	private FrameLayout frameLayout1;
	private String mVideoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_media_all);
		Intent intent = getIntent();
		mVideoPath = intent.getStringExtra("videoPath");
		currentPosition = intent.getIntExtra("currentTime", 0);
		if (TextUtils.isEmpty(mVideoPath)) {
			return;
		}
		frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout1);
		mVideoBg = (ImageView) findViewById(R.id.iv_videobg);
		// final int videoHeight = getWindowManager().getDefaultDisplay()
		// .getHeight() / 2;//
		// getResources().getDimensionPixelSize(R.dimen.video_height);
		// playerContent = (RelativeLayout) findViewById(R.id.playerContent);
		// playerContent.getLayoutParams().height = videoHeight;
		mLoadingView = findViewById(R.id.player_load);
		videoView = (MyVideoView) findViewById(R.id.player_myVideoView);
		View root = findViewById(R.id.ll_video_controller);
		window = getWindow();
		c = new MyMediaController(this);
		videoView.setMediaController(c);
		c.makeControllerView(root);

		c.setOnclick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				if (isFullScreen) {// 已经全屏，变成远原来大小
				// FrameLayout.LayoutParams layoutParams =
				// (FrameLayout.LayoutParams) playerContent
				// .getLayoutParams();
				// layoutParams.height = videoHeight;
				// playerContent.setLayoutParams(layoutParams);
				// videoView.setLayoutParams(layoutParams);
				// final WindowManager.LayoutParams attrs = window
				// .getAttributes();
				// attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
				// window.setAttributes(attrs);
				// window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
				// isFullScreen = false;

				} else { // 从普通大小变成全屏
				// FrameLayout.LayoutParams layoutParams =
				// (FrameLayout.LayoutParams) playerContent
				// .getLayoutParams();
				// layoutParams.height = LayoutParams.MATCH_PARENT;
				// playerContent.setLayoutParams(layoutParams);
				// videoView.setLayoutParams(layoutParams);
				// window.setFlags(
				// WindowManager.LayoutParams.FLAG_FULLSCREEN,
				// WindowManager.LayoutParams.FLAG_FULLSCREEN);
				// mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

					// isFullScreen = true;
				}
			}
		});
		c.setPrevNextListeners(this, this);
		videoView.setOnPreparedListener(this);
		videoView.setOnErrorListener(this);
		videoView.setOnCompletionListener(this);
		videoView.setVideoPath(mVideoPath);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		videoView.stopPlayback();
		videoView = null;
		c = null;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		videoView.stopPlayback();
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mLoadingView.setVisibility(View.GONE);
		mVideoBg.setVisibility(View.GONE);
		isPrepared = true;
		findViewById(R.id.ll_video_controller).setVisibility(View.VISIBLE);
		if (currentPosition != 0) {
			videoView.seekTo(currentPosition);
		}
		videoView.start();
	}
}
