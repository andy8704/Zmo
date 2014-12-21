package com.zmo.utils;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * 
 * @ClassName: CommonDefine
 * @Description: 常量字符串定义
 * @author andy.xu
 * @date 2014-2-26 上午10:13:36
 * 
 */
public class CommonDefine {

	public static final String INTENT_DATA = "intent_data";

	public static final String NOTE_SUFFIX_AMR = ".amr";

	public static final String MINA_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mina/";

	// 运营商标识
	public static final String USER_OPERATOR_CHINA_MOBILE = "中国移动";
	public static final String USER_OPERATOR_CHINA_UNICOM = "中国联通";
	public static final String USER_OPERATOR_CHINA_TELECOM = "中国电信";
	public static final String USER_OPERATOR_CHINA_TIETONG = "中国铁通";

	public static final String WEIXIN = "com.tencent.mm";
	public static final String QQ = "com.tencent.mobileqq";

}
