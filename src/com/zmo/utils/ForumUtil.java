package com.zmo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.zmo.listener.BitmapDownloadListener;

/**
 * 
 * @类名称: ForumUtil
 * @描述: 社区工具类
 * @开发者: andy.xu
 * @时间: 2014-7-23 上午10:40:09
 * 
 */
public class ForumUtil {

	public static final String MINA_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mina/";

	/**
	 * 社区缓存文件的路径
	 */
	public static final String MINA_FORUM_DIR = MINA_ROOT_DIR + "/forum/";

	public static final String MINA_FORUM_SAVE_DIR = MINA_ROOT_DIR + "/forum/favorite/";

	/**
	 * 对图片进行管理的工具类
	 */
	private static ImageLoader imageLoader;

	/**
	 * 获取图片的本地存储路径(文件名为url进行md5加密，后缀名是.jpg)。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	public static String getImagePath(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl))
			return null;

		String imageName = StringUtil.MD5(imageUrl) + ".jpg";
		String imageDir = MINA_FORUM_DIR;
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		String imagePath = imageDir + imageName;
		return imagePath;
	}

	/**
	 * 
	 * @Description: 获取保存本地图片的路径
	 * @param
	 * @return
	 * @throws
	 */
	public static String onGetPicSavePath(String imgUrl) {
		if (TextUtils.isEmpty(imgUrl))
			return null;

		String imageName = StringUtil.MD5(imgUrl) + ".jpg";
		String imageDir = MINA_FORUM_SAVE_DIR;
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		String imagePath = imageDir + imageName;
		return imagePath;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的路径
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param sourcePath
	 *            [in]源文件路径
	 * @param destPath
	 *            [in]目的文件路径
	 */
	public static void copyFile(final String sourcePath, final String destPath) {
		if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(destPath))
			return;

		File file = new File(sourcePath);
		if (file.isFile() && file.exists()) {

			try {
				FileChannel sourceChannel = new FileInputStream(file).getChannel();
				FileChannel destChannel = new FileOutputStream(destPath).getChannel();
				// 采取文件管道的方式进行拷贝
				destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
				sourceChannel.close();
				destChannel.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @方法: onDisplayBitmap
	 * @描述: 显示图片到Imageview
	 * @参数 @param view
	 * @参数 @param imgUrl
	 * @参数 @return
	 * @返回值类型 Bitmap
	 * @捕获异常
	 */
	public void onDisplayBitmap(ImageView view, String imgUrl) {
		if (null == view || TextUtils.isEmpty(imgUrl))
			return;

		if (null == imageLoader)
			imageLoader = ImageLoader.getInstance();

		LoadImageTask task = new LoadImageTask(view);
		task.execute(imgUrl);
	}

	/**
	 * 
	 * @Description: 释放所有的内存资源
	 * @param
	 * @return void
	 * @throws
	 */
	public static void onReleaseBmpCache() {
		if (null != imageLoader)
			imageLoader.onReleaseMemoryCache();
	}
	
	public static void onRemoveBmp(final String key){
		if(TextUtils.isEmpty(key))
			return;
		
		if(null == imageLoader)
			return;
		
		imageLoader.onRemoveBitmapFromMemoryCache(key);
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author andy.xu
	 */
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		// private String mImageUrl;

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {

		}

		/**
		 * 将可重复使用的ImageView传入
		 * 
		 * @param imageView
		 */
		public LoadImageTask(ImageView imageView) {
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String mImageUrl = params[0];
			if (TextUtils.isEmpty(mImageUrl))
				return null;

			Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				if (null != mImageView)
					mImageView.setImageBitmap(bitmap);
			}
		}

		/**
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		private Bitmap loadImage(String imageUrl) {
			if (TextUtils.isEmpty(imageUrl))
				return null;

			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				downloadImage(imageUrl);
			}
			if (imageUrl != null) {
				Bitmap bitmap = ImageLoader.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
		}

		/**
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 */
		private void downloadImage(String imageUrl) {
			if (TextUtils.isEmpty(imageUrl))
				return;

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Log.d("TAG", "monted sdcard");
			} else {
				Log.d("TAG", "has no sdcard");
			}
			HttpURLConnection con = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(15 * 1000);
//				con.setDoInput(true);
//				con.setDoOutput(true);
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(getImagePath(imageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageFile != null) {
				Bitmap bitmap = ImageLoader.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
				}
			}
		}
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author andy.xu
	 */
	public class LoadBitmapTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		// private String mImageUrl;

		private BitmapDownloadListener mListener = null;
		private int mWidth = 0, mHeight = 0;

		public LoadBitmapTask() {
			if (null == imageLoader)
				imageLoader = ImageLoader.getInstance();
		}

		public LoadBitmapTask(final BitmapDownloadListener listen) {
			mListener = listen;
			if (null == imageLoader)
				imageLoader = ImageLoader.getInstance();
		}

		public LoadBitmapTask(final BitmapDownloadListener listen, final int nWidth, final int nHeight) {
			if (null == imageLoader)
				imageLoader = ImageLoader.getInstance();
			onSetListener(listen, nWidth, nHeight);
		}

		public void onSetListener(final BitmapDownloadListener listen) {
			mListener = listen;
		}

		public void onSetListener(final BitmapDownloadListener listen, final int nWidth, final int nHeight) {
			mListener = listen;
			mWidth = nWidth;
			mHeight = nHeight;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String mImageUrl = params[0];
			if (TextUtils.isEmpty(mImageUrl))
				return null;

			if (null != mListener)
				mListener.onDownloading();

			Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl, mWidth, mHeight);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (null != mListener)
				mListener.onDownloadComplete(bitmap);
		}

		/**
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		private Bitmap loadImage(String imageUrl, final int nWidth, final int nHeight) {
			if (TextUtils.isEmpty(imageUrl))
				return null;

			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				downloadImage(imageUrl);
			}
			if (imageUrl != null) {
				Bitmap bitmap = null;
				if (nWidth > 0 && nHeight > 0) {
					bitmap = ImageLoader.decodeFile(imageFile.getPath(), nWidth, nHeight);
				} else
					bitmap = ImageLoader.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
		}

		/**
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 */
		private void downloadImage(String imageUrl) {
			if (TextUtils.isEmpty(imageUrl))
				return;

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Log.d("TAG", "monted sdcard");
			} else {
				Log.d("TAG", "has no sdcard");
			}
			HttpURLConnection con = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(15 * 1000);
//				con.setDoInput(true);
//				con.setDoOutput(true);
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(getImagePath(imageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageFile != null) {
				Bitmap bitmap = ImageLoader.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
				}
			}
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {

		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;

				left = 0;
				top = 0;
				right = width;
				bottom = width;

				height = width;

				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;

				float clip = (width - height) / 2;

				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;

				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}

			Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);// 设置画笔无锯齿

			canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

			// 以下有两种方法画圆,drawRounRect和drawCircle
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
			// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
			canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @Description: 解析图片文件资源
	 * @param filePath
	 *            [in] 图片的路径
	 * @return Bitmap 图片资源
	 * @throws
	 */
	public static Bitmap decodeFile(String filePath) {

		if (TextUtils.isEmpty(filePath))
			return null;

		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// API level > 3 才能用 BitmapFactory.Options.inPurgeable 这个变量；
		options.inPurgeable = true;
		bitmap = decodeStreamFile(filePath, options);
		return bitmap;
	}

	public static Bitmap decodeFile(final String filePath, final int nWidth, final int nHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;// 设置图片可以被回收
		BitmapFactory.decodeFile(filePath, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, nWidth, nHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		// return BitmapFactory.decodeFile(filePath, options);
		return decodeStreamFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	private static Bitmap decodeStreamFile(final String filePath, final BitmapFactory.Options opt) {

		if (TextUtils.isEmpty(filePath) || null == opt)
			return null;

		File file = new File(filePath);
		if (!file.exists())
			return null;

		InputStream is;
		try {
			is = new FileInputStream(file);

			return BitmapFactory.decodeStream(is, null, opt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError error) {
			Log.e("decodeBitmap", error.toString());
			return null;
		}
		return null;
	}

	public static int convertDipToPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	// 转换px为dip
	public static int convertPxToDip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(final Context context) {
		if (null == context)
			return 0;

		DisplayMetrics display = context.getResources().getDisplayMetrics();
		if (null != display)
			return display.widthPixels;
		return 0;
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(final Context context) {
		if (null == context)
			return 0;

		DisplayMetrics display = context.getResources().getDisplayMetrics();
		if (null != display)
			return display.heightPixels;
		return 0;
	}

	/**
	 * 
	 * @方法: onFormatVisiterTime
	 * @描述: 格式化访问的时间字符串
	 * @参数 @param time
	 * @参数 @return
	 * @返回值类型 String
	 * @捕获异常
	 */
	public static String onFormatVisiterTime(final long time) {

		if (time <= 0)
			return null;

		String timeFormat = null;

		long millTime = Long.valueOf(time);
		Date todayDate = new Date();
		Date visitDate = new Date(millTime);

		if (todayDate.getYear() > visitDate.getYear()) {
			timeFormat = todayDate.getYear() - visitDate.getYear() + "年前";
		} else if (todayDate.getYear() == visitDate.getYear()) {
			if (todayDate.getMonth() > visitDate.getMonth()) {
				timeFormat = todayDate.getMonth() - visitDate.getMonth() + "月前";
			} else if (todayDate.getMonth() == visitDate.getMonth()) {
				if (todayDate.getDate() > visitDate.getDate()) {
					timeFormat = todayDate.getDate() - visitDate.getDate() + "天前";
				} else if (todayDate.getDate() == visitDate.getDate()) {
					if (todayDate.getHours() > visitDate.getHours()) {
						timeFormat = todayDate.getHours() - visitDate.getHours() + "小时前";
					} else if (todayDate.getHours() == visitDate.getHours()) {
						int nMinitus = todayDate.getMinutes() - visitDate.getMinutes();
						if (nMinitus >= 1)
							timeFormat = nMinitus + "分钟前";
						else
							timeFormat = "刚刚";
					}
				}
			}
		}
		return timeFormat;
	}

	/**
	 * 保存图片到本地
	 * 
	 * @param bmp
	 * @param destPath
	 */
	public static void onSaveBmpToFile(final Bitmap bmp, final String destPath, final int nQuality) {
		if (null == bmp || TextUtils.isEmpty(destPath))
			return;

		File file = new File(destPath);

		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(destPath);
			bmp.compress(CompressFormat.PNG, nQuality, outStream);
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图片下载到SD卡缓存起来。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 */
	public static void downloadImage(String imageUrl, final String savePath) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.d("TAG", "monted sdcard");
		} else {
			Log.d("TAG", "has no sdcard");
		}
		HttpURLConnection con = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		File imageFile = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(15 * 1000);
//			con.setDoInput(true);
//			con.setDoOutput(true);
			bis = new BufferedInputStream(con.getInputStream());
			imageFile = new File(getImagePath(imageUrl));
			fos = new FileOutputStream(imageFile);
			bos = new BufferedOutputStream(fos);
			byte[] b = new byte[1024];
			int length;
			while ((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private static Stack<Pair<String, Activity>> mActivityList = null;

	/**
	 * 添加一个到堆栈
	 * 
	 * @param activity
	 */
	public static void addActivity(final Activity activity) {
		if (null == mActivityList)
			mActivityList = new Stack<Pair<String, Activity>>();
		Pair<String, Activity> data = new Pair<String, Activity>(activity.getClass().getName(), activity);
		mActivityList.add(data);
	}

	public static void popActivity(final Activity activity) {
		if (null != mActivityList && !mActivityList.isEmpty()) {
			for (int i = mActivityList.size() - 1; i >= 0; i--) {
				Pair<String, Activity> ele = mActivityList.get(i);
				if (null != ele) {
					if (TextUtils.equals(ele.first, activity.getClass().getName())) {
						mActivityList.remove(i);
						return;
					}
				}
			}
		}
	}

	/**
	 * 销毁所有存在的activity
	 */
	public static void destoryAllActivity() {
		if (null != mActivityList && mActivityList.isEmpty() == false) {
			for (int i = 0; i < mActivityList.size(); i++) {
				Pair<String, Activity> ele = mActivityList.get(i);
				if (null != ele) {
					ele.second.finish();
				}
			}
			mActivityList.clear();
			// System.exit(0);
		}
	}
}
