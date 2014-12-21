package com.zmo.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zmo.R;
import com.zmo.view.MediaPlayerView.VideoStarte;



public class MyMediaController {

	private MediaPlayerControl mPlayer;
	private Context mContext;
 	private Window mWindow;
	private View mDecor;
	private ProgressBar mProgress;
	private TextView mEndTime, mCurrentTime;
	private boolean mShowing = true;
	private boolean mDragging;
	private static final int sDefaultTimeout = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private static final String TAG = "MyMediaController";
	private boolean mUseFastForward;
	private boolean mFromXml;
	private boolean mListenersSet;
	private View.OnClickListener mNextListener, mPrevListener;
	StringBuilder mFormatBuilder;
	Formatter mFormatter;
	private ImageButton mPauseButton;
	private ImageButton mFfwdButton;
	private ImageButton mRewButton;
	private ImageButton mNextButton;
	private ImageButton mPrevButton;
	private View mRoot;
	private TextView mMediaTitle;
	private TextView mCurrentSystemTime;
	private ImageView mBatteryIV;
 	private int mSreenWidth;
	private int mSreenHeight;
	 public static boolean isCompletion = false;
	private MyVideoView videoView;
	private LinearLayout ll_controller_show;
	private boolean isPlayer = false;;
	public MyMediaController(Context context) {
		 
		
		mContext = context;
		mUseFastForward = true;
		//initFloatingWindow();
		
	}
 
	private OnTouchListener mTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (mShowing) {
					hide();
					return true;
				}
			}
			return false;
		}
	};
	private ImageButton mPlayModeButton;
	private RelativeLayout mContollerContentLL;

	public void setMediaPlayer(MyVideoView videoView) {
		mPlayer = videoView;
		updatePausePlay();
		updatePlayMode();
	}


	/**
	 * Create the view that holds the widgets that control playback. Derived
	 * classes can override this to create their own.
	 * 
	 * @return The controller view.
	 * @hide This doesn't work as advertised
	 */
	public View makeControllerView(View root) {
	
		mRoot = root;

		initControllerView(mRoot);
		root.setOnTouchListener(mTouchListener); 
		return mRoot;
	}

	private void initControllerView(View v) {
		mPauseButton = (ImageButton) v.findViewById(R.id.pause);
		if (mPauseButton != null) {
			mPauseButton.requestFocus();
			mPauseButton.setOnClickListener(mPauseListener);
		}
		
		ll_controller_show = (LinearLayout) v.findViewById(R.id.ll_controller_show);
	/*
		if(ll_controller_show != null){
			ll_controller_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				
			}
		});
		}*/
	/*	mMediaTitle = (TextView) v.findViewById(R.id.media_title);
		mCurrentSystemTime = (TextView) v.findViewById(R.id.media_current_system_time);
		mBatteryIV = (ImageView) v.findViewById(R.id.media_battery);*/
		/*
		 * PowerManager powerManager = (PowerManager)
		 * mContext.getSystemService(Context.); powerManager.
		 */

		/*mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
		if (mFfwdButton != null) {
			mFfwdButton.setOnClickListener(mFfwdListener);
			if (!mFromXml) {
				mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
			}
		}
*/
		/*mRewButton = (ImageButton) v.findViewById(R.id.rew);
		if (mRewButton != null) {
			mRewButton.setOnClickListener(mRewListener);
			if (!mFromXml) {
				mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
			}
		}*/

		// By default these are hidden. They will be enabled when
		// setPrevNextListeners() is called
		/*mNextButton = (ImageButton) v.findViewById(R.id.next);
		if (mNextButton != null && !mFromXml && !mListenersSet) {
			mNextButton.setVisibility(View.GONE);
		}*/
		/*mPrevButton = (ImageButton) v.findViewById(R.id.prev);
		if (mPrevButton != null && !mFromXml && !mListenersSet) {
			mPrevButton.setVisibility(View.GONE);
		}*/
		mPlayModeButton = (ImageButton) v.findViewById(R.id.diaplay_mode);
		if (mPlayModeButton != null) {
			mPlayModeButton.setOnClickListener(mChangePalyMode);
		}
		mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = (SeekBar) mProgress;
				seeker.setOnSeekBarChangeListener(mSeekListener);
			}
			mProgress.setMax(1000);
		}
	

		mEndTime = (TextView) v.findViewById(R.id.time);
		mCurrentTime = (TextView) v.findViewById(R.id.time_current);
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		installPrevNextListeners();
	}

	public void setMediaTitle(String title) {
		if (mMediaTitle != null)
			mMediaTitle.setText(title);
	}

	/**
	 * Show the controller on screen. It will go away automatically after 3
	 * seconds of inactivity.
	 */
	public void show() {
		show(sDefaultTimeout);
	}

	/**
	 * Disable pause or seek buttons if the stream cannot be paused or seeked.
	 * This requires the control interface to be a MediaPlayerControlExt
	 */
	private void disableUnsupportedButtons() {
		try {
			if (mPauseButton != null && !mPlayer.canPause()) {
				mPauseButton.setEnabled(false);
			}
			if (mRewButton != null && !mPlayer.canSeekBackward()) {
				mRewButton.setEnabled(false);
			}
			if (mFfwdButton != null && !mPlayer.canSeekForward()) {
				mFfwdButton.setEnabled(false);
			}
		} catch (IncompatibleClassChangeError ex) {
			// We were given an old version of the interface, that doesn't have
			// the canPause/canSeekXYZ methods. This is OK, it just means we
			// assume the media can be paused and seeked, and so we don't
			// disable
			// the buttons.
		}
	}

	/**
	 * Show the controller on screen. It will go away automatically after
	 * 'timeout' milliseconds of inactivity.
	 * 
	 * @param timeout
	 *            The timeout in milliseconds. Use 0 to show the controller
	 *            until hide() is called.
	 */
	public void show(int timeout) {
		Log.d("show", "show");
		if (!mShowing && mRoot != null) {
			setProgress();
			setSystemTime();
			if (mPauseButton != null) {
				mPauseButton.requestFocus();
			}
			disableUnsupportedButtons();
			ll_controller_show.setVisibility(View.VISIBLE);
			mShowing = true;
		}
		updatePausePlay();

		// cause the progress bar to be updated even if mShowing
		// was already true. This happens, for example, if we're
		// paused with the progress bar showing the user hits play.
		mHandler.sendEmptyMessage(SHOW_PROGRESS);

		Message msg = mHandler.obtainMessage(FADE_OUT);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}

	public boolean isShowing() {
		return mShowing;
	}

	/**
	 * Remove the controller from the screen.
	 */
	public void hide() {
		if (mRoot == null)
			return;

		if (mShowing) {
			try {
				mHandler.removeMessages(SHOW_PROGRESS);
				ll_controller_show.setVisibility(View.GONE);
			} catch (IllegalArgumentException ex) {
				Log.w("MediaController", "already removed");
			}
			mShowing = false;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int pos;
			switch (msg.what) {
			case FADE_OUT:
				hide();
				break;
			case SHOW_PROGRESS:
				pos = setProgress();
				if (!mDragging && mShowing && mPlayer.isPlaying()) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
		}
	};

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	private void setSystemTime() {
		if (mCurrentSystemTime != null) {
			mCurrentSystemTime.setText(mDateFormat.format(new Date(System.currentTimeMillis())));
		}
	}
	private int setProgress() {
		if (mPlayer == null || mDragging) {
			return 0;
		}
		int position = mPlayer.getCurrentPosition();
		int duration = mPlayer.getDuration();
//		if(position == 0 && isPlayer){     //记录播放次数
//			if(mContext instanceof PlayerActivity)
//			((PlayerActivity) mContext).playNum();
//			isPlayer = false;
//		}
		if(position != 0){
			isPlayer = true;
		}
		if (mProgress != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = 1000L * position / duration;
				if(isCompletion){
					pos = 1000;
					position = duration;
				}
				mProgress.setProgress((int) pos);
			}
			int percent = mPlayer.getBufferPercentage();
			mProgress.setSecondaryProgress(percent * 10);
		}

		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mCurrentTime != null)
			mCurrentTime.setText(stringForTime(position));
		
		return position;
	}

	 
	public boolean onTouchEvent(MotionEvent event) {
		show(sDefaultTimeout);
		return true;
	}

 	public boolean onTrackballEvent(MotionEvent ev) {
		show(sDefaultTimeout);
		return false;
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (event.getRepeatCount() == 0
				&& (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
			doPauseResume();
			show(sDefaultTimeout);
			if (mPauseButton != null) {
				mPauseButton.requestFocus();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				updatePausePlay();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			// don't show the controls for volume adjustment
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
			hide();

			return true;
		} else {
			show(sDefaultTimeout);
			
		}
		return false;
	}
	
	private View.OnClickListener mPauseListener = new View.OnClickListener() {
		public void onClick(View v) {
			doPauseResume();
			show(sDefaultTimeout);
		}
	};
	public void setImage(ImageView mImage){
		this.mImageView = mImage;
		if(mImageView != null){
			mImageView.requestFocus();
			mImageView.setOnClickListener(mPauseListener);
		}
	}
	public ImageView mImageView;
	private void updatePausePlay() {
		if (mRoot == null || mPauseButton == null)
			return;
		if (mPlayer.isPlaying()) {
			if(mImageView != null)
			mImageView.setVisibility(View.GONE);
			mPauseButton.setImageResource(R.drawable.ic_media_pause);
		} else {
			mPauseButton.setImageResource(R.drawable.ic_media_play);
			if(MediaPlayerView.isPrepared)
				if(mImageView != null)
				mImageView.setVisibility(View.VISIBLE);
			
		}
	}
	public void setStartButtonClick(VideoStarte startClick){
		mClickStart = startClick;
	}
	private VideoStarte mClickStart;
	/**
	 * 播放暂停按钮调用
	 */
	public void doPauseResume() {
		Log.i(TAG, "doPauseResume doPauseResume");
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		} else {
			if(MediaPlayerView.isPrepared){
				mPlayer.start();
			}else{
				if(mClickStart != null){
					mClickStart.startLoad();
				}
			}
		}
		updatePausePlay();
	}
	
	
	private  View.OnClickListener mOnclick;
	
	
	public void setOnclick(View.OnClickListener mOnclick) {
		this.mOnclick = mOnclick;
	}

	private View.OnClickListener mChangePalyMode = new View.OnClickListener() {
		public void onClick(View v) {
			/*if (mPlayer.isFullScreen()) {
				mPlayer.normalScreen();
			} else {
				mPlayer.fullScreen(mSreenWidth, mSreenHeight);
			}
			updatePlayMode();*/
			show(sDefaultTimeout);
			mOnclick.onClick(v);
			updatePlayMode();
			 
		}
	};
	

	private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			show(3600000);

			mDragging = true;

			mHandler.removeMessages(SHOW_PROGRESS);
		}

		public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
			if (!fromuser) {
				return;
			}
			isCompletion = false;
			long duration = mPlayer.getDuration();
			long newposition = (duration * progress) / 1000L;
			if (mCurrentTime != null)
				mCurrentTime.setText(stringForTime((int) newposition));
		}

		public void onStopTrackingTouch(SeekBar bar) {
			mDragging = false;
			if (!mPlayer.canSeek()) {
				bar.setProgress(0);
			} else {
				long duration = mPlayer.getDuration();
				long newposition = (duration * bar.getProgress()) / 1000L;
				mPlayer.seekTo((int) newposition);
			}

			setProgress();
			updatePausePlay();
			show(sDefaultTimeout);

			mHandler.sendEmptyMessage(SHOW_PROGRESS);
		}
	};

	private void updatePlayMode() {
		if (mRoot == null || mPlayModeButton == null)
			return;

		if (MediaPlayerView.isFullScreen) {
			mPlayModeButton.setBackgroundResource(R.drawable.btn_full_screen_normal);
		} else {
			mPlayModeButton.setBackgroundResource(R.drawable.btn_original_size_normal);
		}
	}

	private View.OnClickListener mRewListener = new View.OnClickListener() {
		public void onClick(View v) {
			int pos = mPlayer.getCurrentPosition();
			pos -= 5000; // milliseconds
			mPlayer.seekTo(pos);
			setProgress();

			show(sDefaultTimeout);
		}
	};




	private SimpleDateFormat mDateFormat;

	private void installPrevNextListeners() {
		if (mNextButton != null) {
			mNextButton.setOnClickListener(mNextListener);
			mNextButton.setEnabled(mNextListener != null);
		}

		if (mPrevButton != null) {
			mPrevButton.setOnClickListener(mPrevListener);
			mPrevButton.setEnabled(mPrevListener != null);
		}
	}

	public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
		mNextListener = next;
		mPrevListener = prev;
		mListenersSet = true;

		if (mRoot != null) {
			installPrevNextListeners();

			if (mNextButton != null && !mFromXml) {
				mNextButton.setVisibility(View.VISIBLE);
			}
			if (mPrevButton != null && !mFromXml) {
				mPrevButton.setVisibility(View.VISIBLE);
			}
		}
	}
 
	public interface MediaPlayerControl {
		void start();

		void pause();

		int getDuration();

		int getCurrentPosition();

		void seekTo(int pos);

		boolean isPlaying();

		int getBufferPercentage();

		boolean canPause();
		
		boolean canSeek();

		boolean canSeekBackward();

		boolean canSeekForward();
		
		boolean isFullScreen();
		
		void normalScreen();
		
		void fullScreen(int width, int height);
	}
}
