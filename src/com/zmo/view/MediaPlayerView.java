package com.zmo.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zmo.R;

public class MediaPlayerView extends LinearLayout implements OnPreparedListener, OnErrorListener, OnClickListener, OnCompletionListener,
		OnInfoListener {

	// private LinearLayout mMediaViewGroup = null;
	private String threadName;
	// private LinearLayout mMediaControlViewGroup = null;
	private Context mContext = null;
	private View mLoadingView;
	private MyVideoView videoView;
	private Window window;
	private Activity mActivity;
	private MyMediaController c;
	private RelativeLayout playerContent;
	// private BMediaController mVVCtl = null;
	public static boolean isFullScreen = false;
	private ImageView mVideoBg;
	public static boolean isPrepared = false;
	private int currentPosition;
	private FrameLayout frameLayout1;
	private ImageView iv_videobg_start;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	private final Object SYNC_Playing = new Object();
	private final int EVENT_PLAY = 0;
	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;
	private String mVideoPath = null;

	private boolean bPauseFlag = false;
	private Handler mHandler = new Handler();
	private boolean isPlayed;

	public MediaPlayerView(Context context) {
		super(context);

		initUI(context);
	}

	public MediaPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}

	@SuppressLint("NewApi")
	public MediaPlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI(context);
	}

	private void initUI(final Context context) {
		mActivity = (Activity) context;
		mContext = context;
		inflate(context, R.layout.zmo_video_view, this);
		// mMediaViewGroup = (LinearLayout) findViewById(R.id.view_holder);
		frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout1);
		mVideoBg = (ImageView) findViewById(R.id.iv_videobg);
		// final int windowWidth = DensityUtil.getWindowWH()[0] -
		// DensityUtil.dip2px(40);//
		// getResources().getDimensionPixelSize(R.dimen.video_height);
		playerContent.getLayoutParams().height = 11 * 720 / 17;
		playerContent = (RelativeLayout) findViewById(R.id.playerContent);
		// playerContent.getLayoutParams().height = 11 * windowWidth / 17;
		mLoadingView = findViewById(R.id.player_load);
		videoView = (MyVideoView) findViewById(R.id.player_myVideoView);
		iv_videobg_start = (ImageView) findViewById(R.id.iv_videobg_start);
		View root = findViewById(R.id.ll_video_controller);
		window = mActivity.getWindow();
		c = new MyMediaController(mActivity);
		videoView.setMediaController(c);
		c.makeControllerView(root);
		c.setStartButtonClick(new VideoStarte() {

			@Override
			public void startLoad() {
				// TODO Auto-generated method stub
				mLoadingView.setVisibility(View.VISIBLE);
				mListener.onPlay();
				videoView.setVideoPath(mVideoPath);
			}
		});
		c.setOnclick(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
					videoView.setUri(null);
					mVideoBg.setVisibility(View.VISIBLE);
					onStop();
					Intent intent = new Intent(mActivity, LockVideoAll.class);
					intent.putExtra("videoPath", mVideoPath);
					intent.putExtra("currentTime", currentPosition);
					mActivity.startActivity(intent);
					isFullScreen = true;
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
		videoView.setOnInfoListener(this);
		iv_videobg_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onPlay();
				mLoadingView.setVisibility(View.VISIBLE);
				iv_videobg_start.setVisibility(View.GONE);
				videoView.setVideoPath(mVideoPath);
			}
		});
	}

	// public void onSetVideoPath(final String videoPath) {
	// mVideoPath = videoPath;
	// // onPlay();
	// }

	public void onSetVideoInfo(final String videoUrl) {

		if (TextUtils.isEmpty(videoUrl))
			return;

		 mVideoPath = videoUrl;
		// mVV.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// if (mVV.isPlaying())
		// onStop();
		// }
		// });
	}

	public void onDestroy() {
		isPrepared = false;
		// mLastPos = mVV.getCurrentPosition();

		// if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
		// mLastPos = mVV.getCurrentPosition();
		// mVV.stopPlayback();
		// }
		videoView.stopPlayback();
		videoView = null;
		c = null;

	}

	public void onStop() {
		iv_videobg_start.setVisibility(View.VISIBLE);
		isPrepared = false;
		videoView.stopPlayback();
		mLoadingView.setVisibility(View.GONE);
		// videoView = null;
		// c = null;
		/**
		 * 结束后台事件处理线程
		 */
	}

	public interface VideoStarte {
		public void startLoad();
	}

	private VideoPlayListener mListener = null;

	public interface VideoPlayListener {
		public void onPlay();

		public void onGotoDetail();
	}

	public void onSetPlayListener(final VideoPlayListener listener) {
		mListener = listener;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		onStop();
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	}

	public void onpause() {
		isFullScreen = false;
		if (videoView != null) {
			currentPosition = videoView.getCurrentPosition();
		}
	}

	public void onresume() {
		isPrepared = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		onStop();
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.e("yangqiang", "onPrepared onPrepared");
		mLoadingView.setVisibility(View.GONE);
		mVideoBg.setVisibility(View.GONE);
		isPrepared = true;
		findViewById(R.id.ll_video_controller).setVisibility(View.VISIBLE);
		if (currentPosition != 0) {
			videoView.seekTo(currentPosition);
		}
		videoView.start();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			mLoadingView.setVisibility(View.VISIBLE);
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mLoadingView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		return true;
	}
}
