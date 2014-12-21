package com.zmo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.widget.Toast;

import com.zmo.R;

/**
 * 
 * @ClassName: CommonUtil
 * @Description: 工具类
 * @author andy.xu
 * @date 2014-2-26 上午9:37:14
 * 
 */
public class CommonUtil {

	/**
	 * 当前的农历时间
	 */
	public static Calendar mCurCalendar = null;

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
		// add for test
		options.inScaled = false;
//		options.inDensity = CommonDefine.IN_DENSITY;
//		options.inTargetDensity = CommonDefine.IN_DENSITY;

		// try {
		// // [Neo] 可能会有安全异常
		// // inNativeAlloc 是一个隐藏变量，需要使用特殊的方法设置。
		// BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options,
		// true);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// bitmap = BitmapFactory.decodeFile(filePath, options);
		bitmap = decodeStreamFile(filePath, options);
		return bitmap;
	}

	/**
	 * 根绝手机的分辨率进行图片的解析
	 * 
	 * @param filePath
	 * @param context
	 * @return
	 */
	public static Bitmap decodeFile(final String filePath, final Context context) {
		if (TextUtils.isEmpty(filePath) || null == context)
			return null;

		DisplayMetrics display = context.getResources().getDisplayMetrics();
		if (null != display) {
			return decodeCurFile(filePath, display.widthPixels, display.heightPixels);
		} else
			return decodeFile(filePath);
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
	 * @方法: onGetThumeBitmap
	 * @描述: 获取缩略图
	 * @参数 @param filePath
	 * @参数 @param nWidth
	 * @参数 @param nHeight
	 * @参数 @return
	 * @返回值类型 Bitmap
	 * @捕获异常
	 */
	public static Bitmap onGetThumeBitmap(final String filePath, int nWidth, int nHeight) {
		if (TextUtils.isEmpty(filePath))
			return null;

		Bitmap bmp = CommonUtil.decodeFile(filePath);
		if (null == bmp)
			return null;

		return onGetThumeBitmap(bmp, nWidth, nHeight);
	}

	/**
	 * 
	 * @方法: onGetThumeBitmap
	 * @描述: 创建缩略图
	 * @参数 @param orgiBmp[in] 原图
	 * @参数 @param nWidth[in] 目标图片宽度
	 * @参数 @param nHeight[in] 目标图片高度
	 * @参数 @return
	 * @返回值类型 Bitmap
	 * @捕获异常
	 */
	public static Bitmap onGetThumeBitmap(final Bitmap orgiBmp, int nWidth, int nHeight) {

		if (null == orgiBmp)
			return null;

		int nW = orgiBmp.getWidth();
		int nH = orgiBmp.getHeight();
		float fScale = (float) nW / nWidth;
		if (fScale < 1)
			return orgiBmp;

		nHeight = (int) (nH / fScale);
		Bitmap bmp = Bitmap.createScaledBitmap(orgiBmp, nWidth, nHeight, false);
		return bmp;
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

	public static Bitmap decodeCurFile(final String filePath, final int nWidth, final int nHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;// 设置图片可以被回收
		BitmapFactory.decodeFile(filePath, options);
		// 调用上面定义的方法计算inSampleSize值
		final int height = options.outHeight;
		final int width = options.outWidth;
		int scaleX = width / nWidth;
		int scaleY = height / nHeight;
		int scale = 1;
		// 采样率依照最大的方向为准
		if (scaleX >= scaleY && scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleX <= scaleY && scaleX >= 1) {
			scale = scaleY;
		}

		if (height == width)
			scale = scaleX;

		options.inSampleSize = scale;
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return decodeStreamFile(filePath, options);
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
	 * @描述: 圆角矩形
	 * @param bitmap
	 * @param roundPx
	 * @return Bitmap
	 * @异常
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		if (null == bitmap)
			return null;

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
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
	 * 截取特定长度的字符串
	 * 
	 * @param content
	 * @param nCount
	 * @return
	 */
	public static String getCurLengthStr(String content, int nCount) {

		if (TextUtils.isEmpty(content))
			return "";

		if (content.length() <= nCount)
			return content;
		else
			return TextUtils.substring(content, 0, nCount);
	}

	/**
	 * 显示Toast
	 * 
	 * @param title
	 * @param context
	 */
	public static void showToast(final String title, final Context context) {
		if (TextUtils.isEmpty(title) || null == context)
			return;
		Toast toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的路径
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {

		if (TextUtils.isEmpty(sPath))
			return true;

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
	 * 文件重新命名
	 * 
	 * @param sourcePath
	 *            [in]源文件路径
	 * @param destPath
	 *            [in]目的文件路径
	 */
	public static void renameFile(final String sourcePath, final String destPath) {
		if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(destPath))
			return;

		File file = new File(sourcePath);
		if (file.isFile() && file.exists()) {

			File destFile = new File(destPath);
			if (!destFile.exists()) {
				try {
					destFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			file.renameTo(destFile);
			file.delete();
		}
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
	 * 获取当前的日期
	 * 
	 * @param date
	 * @param context
	 * @return
	 */
	public static String onGetWeek(final Date date, final Context context) {
		if (null == date || null == context)
			return null;
		String curDay = "";
		switch (date.getDay()) {
		default:
			break;
		case 0:
			curDay = context.getString(R.string.sun);
			break;
		case 1:
			curDay = context.getString(R.string.mon);
			break;
		case 2:
			curDay = context.getString(R.string.tue);
			break;
		case 3:
			curDay = context.getString(R.string.wen);
			break;
		case 4:
			curDay = context.getString(R.string.thi);
			break;
		case 5:
			curDay = context.getString(R.string.fri);
			break;
		case 6:
			curDay = context.getString(R.string.sar);
			break;
		}

		return curDay;
	}

	/**
	 * 获取当前的星期（英文版）
	 * 
	 * @param date
	 * @param context
	 * @return
	 */
	public static String onGetEnWeek(final Date date, final Context context) {
		if (null == date || null == context)
			return null;
		String curDay = "";
		switch (date.getDay()) {
		default:
			break;
		case 0:
			curDay = context.getString(R.string.en_sun);
			break;
		case 1:
			curDay = context.getString(R.string.en_mon);
			break;
		case 2:
			curDay = context.getString(R.string.en_tue);
			break;
		case 3:
			curDay = context.getString(R.string.en_wen);
			break;
		case 4:
			curDay = context.getString(R.string.en_thi);
			break;
		case 5:
			curDay = context.getString(R.string.en_fri);
			break;
		case 6:
			curDay = context.getString(R.string.en_sar);
			break;
		}

		return curDay;
	}

	/**
	 * 根据文件名称取得文件类型
	 * 
	 * @param fileHandler
	 * @return
	 */
	public static String getMIMEType(final File fileHandler) {
		if (null == fileHandler)
			return null;
		String type = null;
		String fName = fileHandler.getName();
		if (TextUtils.isEmpty(fName))
			return null;
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (TextUtils.isEmpty(end))
			return null;
		if (end.equals("apk"))
			type = "application/vnd.android.package-archive";
		else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("wav") || end.equals("xmf") || end.equals("ogg"))
			type = "autio/*";
		else if (end.equals("3gp") || end.equals("mp4") || end.equals("wmv") || end.equals("rmvb") || end.equals("avi"))
			type = "video/*";
		else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("gif") || end.equals("bmp"))
			type = "image/*";
		else
			type = null;

		return type;
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

	/**
	 * 打开一个网页
	 * 
	 * @param addr
	 *            链接地址
	 * @param context
	 *            设备上下文链接
	 */
	public static void onGotoWeb(final String addr, final Context context) {
		if (TextUtils.isEmpty(addr) || null == context)
			return;

		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri content_url = null;
			content_url = Uri.parse(addr);
			intent.setData(content_url);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 */
	public static void onCreateDir(final String dirPath) {
		if (TextUtils.isEmpty(dirPath))
			return;

		File file = new File(dirPath);
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * 保存图片到本地
	 * 
	 * @param bmp
	 * @param destPath
	 */
	public static void onSaveBmpToFile(final Bitmap bmp, final String destPath) {
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
			bmp.compress(CompressFormat.JPEG, 100, outStream);
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			bmp.compress(CompressFormat.JPEG, nQuality, outStream);
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 验证手机号的合法性
	 * 
	 * @param mail
	 *            手机号码
	 * @return boolean true:合法 false:不合法
	 */
	public static boolean isValidMobile(String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			return false;
		}

		// Pattern p =
		// Pattern.compile("^0{0,1}(13[0-9]|15[0-9]|18[0-9])[0-9]{8}$");
		Pattern p = Pattern.compile("^0{0,1}(1)[0-9]{10}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	// 检查Email地址是否合法

	public static boolean isValidEmail(final String emailAddr) {

		if (TextUtils.isEmpty(emailAddr))
			return false;

		// \\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*

		// Pattern pattern =
		// Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+",
		// Pattern.CASE_INSENSITIVE);
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(emailAddr);
		return matcher.matches();
	}

	/**
	 * 过滤掉特殊字符
	 * 
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String stringFilter(String str) {
		// 清除掉所有特殊字符
		String regEx = "[`~@#$%^&*?？!！\"\\\\()+=|.:;{}''\\[\\]<>/~@#￥%……&*（）——+|{}：；。【】‘”“’《》]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 过滤文件名称中的特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String fileNameFilter(String str) {
		String regEx = "[`~@#$%^&*?？!！\"\\\\()+=|:;{}''\\[\\]<>/~@#￥%……&*（）——+|{}：；。【】‘”“’《》]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	// 获得当前月--开始日期
	public static long getMinMonthDate(final Calendar date) {
		if (null == date)
			return 0;

		Calendar calendar = Calendar.getInstance();
		calendar.set(date.get(Calendar.YEAR), date.get(Calendar.MONDAY), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.DAY_OF_MONTH, date.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MILLISECOND, 0); // 必须设置毫秒才能保证为每个月的开始日期，时间是按照毫秒为单位的。
		return calendar.getTime().getTime();
	}

	// 获得当前月--结束日期
	public static long getMaxMonthDate(final Calendar date) {
		if (null == date)
			return 0;

		Calendar cal = Calendar.getInstance();
		cal.set(date.get(Calendar.YEAR), date.get(Calendar.MONDAY), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MILLISECOND, 0);// 必须设置毫秒才能保证为每个月的开始日期，时间是按照毫秒为单位的。
		return cal.getTime().getTime();
	}

	public static Bitmap onCreateNewBmp(final List<Bitmap> bmpList) {
		if (null == bmpList || bmpList.isEmpty())
			return null;

		int nWidth = 0;
		int nHeight = 0;
		for (Bitmap ele : bmpList) {
			if (null != ele) {
				if (ele.getWidth() > nWidth)
					nWidth = ele.getWidth();

				nHeight += ele.getHeight();
			}
		}
		// 创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
		Bitmap bitmap = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ARGB_8888);
		// 将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
		Canvas canvas = new Canvas(bitmap);
		boolean bFirst = true;
		int nCurHeight = 0;
		for (int i = 0; i < bmpList.size(); i++) {
			Bitmap bmp = bmpList.get(i);
			if (null != bmp) {
				if (bFirst) {
					bFirst = false;
					canvas.drawBitmap(bmp, 0, 0, null);
					nCurHeight += bmp.getHeight();
				} else {
					canvas.drawBitmap(bmp, 0, nCurHeight, null);
					nCurHeight += bmp.getHeight();
				}
			}
		}
		return bitmap;
	}

	public static Activity mStartActivity = null;

	public static void DeleteDir(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteDir(f);
				}
				file.delete();
			}
		}
	}

	/**
	 * 保存涂鸦的大图
	 * 
	 * @param filePath
	 *            [in] 保存路径
	 */
	public static void saveCurSKetchBmp(final String filePath, Bitmap bitmap) {
		if (TextUtils.isEmpty(filePath) || null == bitmap)
			return;

		if (null != bitmap) {
			File file = new File(filePath);
			if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(filePath);
				bitmap.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @方法: onGotoActivity
	 * @描述: 打开特定的页面
	 * @参数 @param context[in] 设备上下文
	 * @参数 @param activity[in] 跳转的activity
	 * @返回值类型 void
	 * @捕获异常
	 */
	public static void onGotoActivity(final Context context, final Class<?> activity) {

		if (null == context || null == activity)
			return;

		Intent intent = new Intent(context, activity);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @描述: 调用系统的发送数据
	 * @param context
	 * @param despStr
	 *            void
	 * @异常
	 */
	public static void onSendText(final Context context, final String despStr) {
		if (null == context || TextUtils.isEmpty(despStr))
			return;

		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		intent.setType("text/plain"); // 分享发送的数据类型
		intent.putExtra(Intent.EXTRA_TEXT, despStr); // 分享的内容
		context.startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
	}

	/**
	 * 
	 * @方法: isSameDate
	 * @描述: 是否在同一天
	 * @参数 @param first
	 * @参数 @param second
	 * @参数 @return
	 * @返回值类型 boolean
	 * @捕获异常
	 */
	public static boolean isSameDate(final Date first, final Date second) {
		if (null == first || null == second)
			return false;
		if (first.getYear() == second.getYear() && first.getMonth() == second.getMonth() && first.getDate() == second.getDate()) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @描述: 根据包名启动一个activity
	 * @param context
	 * @param packageName
	 *            void
	 * @异常
	 */
	public static void onLaunchIntentFromPackage(final Context context, final String packageName) {
		if (null == context || TextUtils.isEmpty(packageName))
			return;

		try {
			PackageManager pckManager = context.getPackageManager();
			Intent intent = pckManager.getLaunchIntentForPackage(packageName);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @描述: 获取文字大小的缩放比例（480）
	 * @param context
	 * @return float
	 * @异常
	 */
	public static float onGetTextFontSizeScale(final Context context) {
		if (null == context)
			return 1;

		DisplayMetrics dis = context.getResources().getDisplayMetrics();
		int nDis = dis.densityDpi;
		return nDis / 480.0f;
	}
}
