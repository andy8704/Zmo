package com.zmo.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class PopMenuDlg extends PopupWindow {

	private Context mContext;

	protected final int LIST_PADDING = 10;

	private Rect mRect = new Rect();
	private int mCurBottomPos = 0;

	private final int[] mLocation = new int[2];

	private int mScreenWidth, mScreenHeight;

	private boolean mIsDirty;

	private int popupGravity = Gravity.NO_GRAVITY;

	public PopMenuDlg(Context context, int width, int height) {
		super(context); // 必须加上这句话，否则在2.3及以下系统会空指针异常

		this.mContext = context;

		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);

		setWidth(width);
		setHeight(height);

		setBackgroundDrawable(new BitmapDrawable());
	}

	public PopMenuDlg(Context context) {
		super(context); // 必须加上这句话，否则在2.3及以下系统会空指针异常

		this.mContext = context;

		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);

		setBackgroundDrawable(new BitmapDrawable());
	}

	public void onSetView(final View view) {
		if (null == view)
			return;

		setContentView(view);
	}

	/**
	 * 显示view的位置
	 * 
	 * @param bUpShow
	 *            [in] 向上显示标记 true:显示在当前view的上方， false:显示在当前View的下方
	 */
	public void show(View view, boolean bUpShow) {
		view.getLocationOnScreen(mLocation);

		if (bUpShow) {
			// 向上显示
			mCurBottomPos = mScreenHeight - mLocation[1];
			showAtLocation(view, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, mLocation[1] - getHeight() / 2);
		} else {
			// 向下显示
			// mCurBottomPos = mLocation[1] + view.getHeight();
			// popupGravity = Gravity.NO_GRAVITY;
			// showAtLocation(view, Gravity.NO_GRAVITY, mLocation[0],
			// mLocation[1]);
			showAsDropDown(view);
		}

		if (mIsDirty) {
			populateActions();
		}

		setFocusable(true);
	}

	/**
	 */
	private void populateActions() {
		mIsDirty = false;
	}

}
