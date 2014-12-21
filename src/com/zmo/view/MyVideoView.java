package com.zmo.view;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.zmo.view.MyMediaController.MediaPlayerControl;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;


/**
 * Displays a video file. The VideoView class can load images from various
 * sources (such as resources or content providers), takes care of computing its
 * measurement from the video so that it can be used in any layout manager, and
 * provides various display options such as scaling and tinting.
 */
public class MyVideoView extends SurfaceView implements MediaPlayerControl {
	private Context mContext;
//	private String TAG = "VideoView";
	// settable by the client
	private Uri mUri;
//	private Map<String, String> mHeaders;
	private int mDuration;
	public static final int MODE_SYSTEM = 1;
	public static final int MODE_SOFT = 2;
	// all possible internal states
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;
	private static final int STATE_SUSPEND = 6;
	private static final int STATE_RESUME = 7;
	private static final int STATE_SUSPEND_UNSUPPORTED = 8;

	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	// All the stuff we need for playing and showing a video
	private SurfaceHolder mSurfaceHolder = null;
	private MediaPlayer mMediaPlayer = null;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private MyMediaController mMediaController;
	private OnCompletionListener mOnCompletionListener;
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	private int mCurrentBufferPercentage;
	private OnErrorListener mOnErrorListener;
	private OnInfoListener mOnInfoListener;
	private int mSeekWhenPrepared; // recording the seek position while
									// preparing
	private boolean mCanPause;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;
	private int mStateWhenSuspended; // state before calling suspend()
	
//	private int mySurfacewidth;
//	private int mySurfaceheight;

	/** 是否是系统解码 */
	private boolean isSystemMediaPlayer;

	/** 是否切换 */
	private boolean isSwitch;

	/** 是否是全屏 */
	private boolean isFullScreen;
	
	private boolean isLive;

	public static final int VITAMIO_NOT_FOUND = 8008;

	public MyVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
//		mContext = context;
//		initVideoView();
		
	}
	
	public MyVideoView(Context context) {
		super(context);
		mContext = context;
		initVideoView();
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initVideoView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Log.i("@@@@", "onMeasure");
		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		
//		DLog.i("width = " + width + "height = " + height);
		
		if (!isFullScreen) {
			if (mVideoWidth > 0 && mVideoHeight > 0) {
				if (mVideoWidth * height > width * mVideoHeight) {
					// Log.i("@@@", "image too tall, correcting");
					height = width * mVideoHeight / mVideoWidth;
				} else if (mVideoWidth * height < width * mVideoHeight) {
					// Log.i("@@@", "image too wide, correcting");
					width = height * mVideoWidth / mVideoHeight;
				} else {
					// Log.i("@@@", "aspect ratio is correct: " +
					// width+"/"+height+"="+
					// mVideoWidth+"/"+mVideoHeight);
				}
			}
		}
		// Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
		setMeasuredDimension(width, height);
	}

	public int resolveAdjustedSize(int desiredSize, int measureSpec) {
		int result = desiredSize;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			/*
			 * Parent says we can be as big as we want. Just don't be larger
			 * than max size imposed on ourselves.
			 */
			result = desiredSize;
			break;

		case MeasureSpec.AT_MOST:
			/*
			 * Parent says we can be as big as we want, up to specSize. Don't be
			 * larger than specSize, and don't be larger than the max size
			 * imposed on ourselves.
			 */
			result = Math.min(desiredSize, specSize);
			break;

		case MeasureSpec.EXACTLY:
			// No choice. Do what we are told.
			result = specSize;
			break;
		}
		return result;
	}

	private void initVideoView() {
		mVideoWidth = 0;
		mVideoHeight = 0;

		getHolder().addCallback(mSHCallback);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}

	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}

	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}

	public void setVideoURI(Uri uri, Map<String, String> headers) {
		mUri = uri;
		if (uri != null) {
			String path = mUri.toString();
			if ((path.contains("mp4") || path.contains("3gp") || path.contains("3g2") || path.contains("http://") || path.contains("rtsp://"))
					&& !isSwitch) {
				getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				isSystemMediaPlayer = true;
			} else {
				getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
				isSystemMediaPlayer = false;
				isSwitch = false;
			}
		}
		//Logger.d(TAG, "setVideoURI........................");
//		mHeaders = headers;
		mSeekWhenPrepared = 0;
		openVideo();
		requestLayout();
		invalidate();
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}
	public void staopMedia(){
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
	}
	private void openVideo() {
		if (mUri == null || mSurfaceHolder == null) {
			// not ready for playback just yet, will try again later
			return;
		}
		// Tell the music playback service to pause
		// TODO: these constants need to be published somewhere in the
		// framework.    
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		mContext.sendBroadcast(i);

		// we shouldn't clear the target state, because somebody might have
		// called start() previously
		release(false);
		try {
			if (isSystemMediaPlayer) {
			//	mMediaPlayer = new SystemMediaPlayer(new MediaPlayer());
				mMediaPlayer = new MediaPlayer();
				//Logger.d(TAG, "new SystemMediPlayer");
			} else {
//				mMediaPlayer = new SoftMediaPlayer(new io.vov.vitamio.MediaPlayer(mContext));
				Toast.makeText(mContext, "无法播放该视频", Toast.LENGTH_SHORT).show();
				return;
				//Logger.d(TAG, "new SoftMediPlayer");
			}
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mDuration = -1;
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mMediaPlayer.setOnInfoListener(MyOnInfoListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDataSource(mContext, mUri);
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			Log.i("mediaplayer", mUri + "");
			// we don't set the target state here either, but preserve the
			// target state that was there before.
			mCurrentState = STATE_PREPARING;
			attachMediaController();
		}  catch (FileNotFoundException e) {
			mErrorListener.onError(null, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			//Logger.e(TAG, e);
		} catch (IOException ex) {
			//Logger.d(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(null, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException ex) {
		//	Logger.d(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(null, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} 
	}

	public void setMediaController(MyMediaController controller) {
		if (mMediaController != null) {
			mMediaController.hide();
		}
		mMediaController = controller;
		attachMediaController();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			View anchorView = this.getParent() instanceof View ? (View) this.getParent() : this;
		}
	}

	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			mVideoWidth = width;
			mVideoHeight = height;
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
			}
		}
	};

	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mCurrentState = STATE_PREPARED;

			// Get the capabilities of the player for this stream
			//
			if (mp != null) {
				/*Metadata data = null;
				if (data != null) {
					mCanPause = !data.has(Metadata.PAUSE_AVAILABLE) || data.getBoolean(Metadata.PAUSE_AVAILABLE);
					mCanSeekBack = !data.has(Metadata.SEEK_BACKWARD_AVAILABLE)
							|| data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
					mCanSeekForward = !data.has(Metadata.SEEK_FORWARD_AVAILABLE)
							|| data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
				} else {*/

					mCanPause = mCanSeekBack = mCanSeekForward = true;
				//}
				
			} else {
				mCanPause = mCanSeekBack = mCanSeekForward = true;
			}
			

			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(null);
			}
			if (mMediaController != null) {
			}
			mVideoWidth = mMediaPlayer.getVideoWidth();
			mVideoHeight = mMediaPlayer.getVideoHeight();

			int seekToPosition = mSeekWhenPrepared; // mSeekWhenPrepared may be
													// changed after seekTo()
													// call
			if (seekToPosition != 0) {
				seekTo(seekToPosition);
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				// Log.i("@@@@", "video size: " + mVideoWidth +"/"+
				// mVideoHeight);
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
					// We didn't actually change the size (it was already at the
					// size
					// we need), so we won't get a "surface changed" callback,
					// so
					// start the video here instead of in the callback.
					if (mTargetState == STATE_PLAYING) {
						start();
						if (mMediaController != null) {
							mMediaController.show();
						}
					} else if (!isPlaying() && (seekToPosition != 0 || getCurrentPosition() > 0)) {
						if (mMediaController != null) {
							// Show the media controls when we're paused into a
							// video and make 'em stick.
							mMediaController.show(0);
						}
					}
				}
			} else {
				// We don't know the video size yet, but should start anyway.
				// The video size might be reported to us later.
				if (mTargetState == STATE_PLAYING) {
					start();
				}
			}
		}
	};

	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null) {
				mMediaController.isCompletion =true;
				mMediaController.hide();
			}
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(null);
			}
		}
	};

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			//Logger.d(TAG, "Error: " + framework_err + "," + impl_err);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null) {
				mMediaController.hide();
			}

			/* If an error handler has been supplied, use it and finish. */
			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(null, framework_err, impl_err)) {
					return true;
				}
			}

			/*
			 * Otherwise, pop up an error dialog so the user knows that
			 * something bad has happened. Only try and pop up the dialog if
			 * we're attached to a window. When we're going away and no longer
			 * have a window, don't bother showing the user an error.
			 */

			if (getWindowToken() != null) {
				int messageId;
				if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
					messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
				} else {
					messageId = android.R.string.VideoView_error_text_unknown;
				}

				new AlertDialog.Builder(mContext)
						.setTitle(android.R.string.VideoView_error_title)
						.setMessage(messageId)
						.setPositiveButton(android.R.string.VideoView_error_button,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {

										if (mOnCompletionListener != null) {
											mOnCompletionListener.onCompletion(null);
										}
									}
								}).setCancelable(false).show();
			}

			return true;
		}
	};

	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
		}
	};
	private MediaPlayer.OnInfoListener MyOnInfoListener = new OnInfoListener() {
		
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if(mOnInfoListener != null){
				mOnInfoListener.onInfo(mp, what, extra);
				return true;
			}
			return false;
		}
	};
	/**
	 * Register a callback to be invoked when the media file is loaded and ready
	 * to go.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
		mOnPreparedListener = l;
	}

	/**
	 * Register a callback to be invoked when the end of a media file has been
	 * reached during playback.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
	}
	
	/**
	 * Register a callback to be invoked when an error occurs during playback or
	 * setup. If no listener is specified, or if the listener returned false,
	 * VideoView will inform the user of any errors.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}
	  /**
     * Register a callback to be invoked when an info/warning is available.
     *
     * @param listener the callback that will be run
     */
	public void setOnInfoListener(OnInfoListener l){
		mOnInfoListener = l;
	}
	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0) {
					seekTo(mSeekWhenPrepared);
				}
				start();
				if (mMediaController != null) {
					mMediaController.show();
				}
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
//			mySurfacewidth = getWidth();
//			mySurfaceheight = getHeight();
			Log.d("yangqiang", "surfaceCreated");
			// resume() was called before surfaceCreated()
			if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND && mTargetState == STATE_RESUME) {
				mMediaPlayer.setDisplay(mSurfaceHolder);
				resume();

			} else {
				openVideo();
				//Logger.d(TAG, "surfaceCreated openVideo");
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			if (mMediaController != null)
				mMediaController.hide();
			if (mCurrentState != STATE_SUSPEND) {
				release(true);
			}
		}
	};

	/*
	 * release the media player in any state
	 */
	private void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();

			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
		}
		//Logger.d(TAG, "release ... end ");
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
 		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}
	public void showOrHide(){
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL && keyCode != KeyEvent.KEYCODE_ENDCALL;
		if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				} else {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP && mMediaPlayer.isPlaying()) {
				pause();
				mMediaController.show();
			} else {
				toggleMediaControlsVisiblity();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void toggleMediaControlsVisiblity() {
		if (mMediaController.isShowing()) {
			mMediaController.hide();
		} else {
			mMediaController.show();
		}
	}

	public void start() {
		if (isInPlaybackState()) {
			mMediaController.isCompletion = false;
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
	}

	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	public void suspend() {
		if (isInPlaybackState()) {
			/*
			 * if (mMediaPlayer.suspend()) { mStateWhenSuspended =
			 * mCurrentState; mCurrentState = STATE_SUSPEND; mTargetState =
			 * STATE_SUSPEND; } else { release(false); mCurrentState =
			 * STATE_SUSPEND_UNSUPPORTED; Log.w(TAG,
			 * "Unable to suspend video. Release MediaPlayer."); }
			 */
		}
	}

	public void resume() {
		if (mSurfaceHolder == null && mCurrentState == STATE_SUSPEND) {
			mTargetState = STATE_RESUME;
			return;
		}
		if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND) {
			if (mMediaPlayer.isPlaying()) {
				mCurrentState = mStateWhenSuspended;
				mTargetState = mStateWhenSuspended;
			} else {
				//Logger.d(TAG, "Unable to resume video");
			}
			return;
		}
		if (mCurrentState == STATE_SUSPEND_UNSUPPORTED) {
			openVideo();
		}
	}

	// cache duration as mDuration for faster access
	// if getDuration() > 10h then return 0
	public int getDuration() {
		if (isInPlaybackState()) {
			//isLive && mMediaPlayer instanceof SoftMediaPlayer ||
			if ( mMediaPlayer.getDuration() > 36000000) {
				return 0;
			}
			
			if (mDuration > 0) {
				return mDuration;
			}
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}

	public int getCurrentPosition() {
		boolean issoft = false;//mMediaPlayer instanceof SoftMediaPlayer;
		if ((isLive && issoft) || (issoft && mMediaPlayer.getCurrentPosition() > getDuration()) )
			return 0;
		if (isInPlaybackState()) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
//		return ( mCurrentState != STATE_ERROR && mCurrentState != STATE_PREPARING);
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	public boolean canPause() {
		return mCanPause;
	}

	public boolean canSeekBackward() {
		return mCanSeekBack && canSeek();
	}

	public boolean canSeekForward() {
		return mCanSeekForward && canSeek();
	}

	public boolean isSystemMediaPlayer() {
		return isSystemMediaPlayer;
	}

	public boolean isSwitch() {
		return isSwitch;
	}

	public void setSwitch(boolean isSwitch) {
		this.isSwitch = isSwitch;
	}

	public void setUri(Uri mUri) {
		this.mUri = mUri;
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public void fullScreen(int width, int height) {
		isFullScreen = true;
		//isFullScreen = false;
		android.widget.RelativeLayout.LayoutParams lp =  (android.widget.RelativeLayout.LayoutParams)getLayoutParams();
		
		lp.height = height;
		lp.width = width;
		
		setLayoutParams(lp);
	}

	public void normalScreen() {
		isFullScreen = false;
		//isFullScreen = true;
		LayoutParams lp = getLayoutParams();
		/*lp.width = mVideoWidth;
		lp.height = mVideoHeight;*/
		setLayoutParams(lp);
	}
	
	@Override
	public boolean canSeek() {
 		return !isLive && getDuration() > 0;
	}
}
