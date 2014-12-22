package com.zmo.data;

import java.io.Serializable;

import com.zmo.utils.StringUtil;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @ClassName: AccountData
 * @Description: 账号的相关信息
 * @author andy.xu
 * @date 2014-3-12 下午7:15:20
 * 
 */
public class AccountData implements Serializable {

	
	public static final String KEY_ACCOUNT_SHAPE = "account";
	public static final String KEY_ACCOUNT_NAME = "accountName";
	public static final String KEY_ACCOUNT_PWD = "accountPwd";
	public static final String KEY_ACCOUNT_MAIL = "accountMail";
	public static final String KEY_NICK_NAME = "nickName";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_OCCUPATION = "occupation";
	public static final String KEY_IS_LOGIN = "bIsLogIn";
	public static final String KEY_USER_ICON = "userIcon";
	public static final String KEY_USER_ID = "userId";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 844937276445285331L;
	/**
	 * 账户名称
	 */
	public String accountName;
	/**
	 * 账户的密码
	 */
	public String accountPwd;

	/**
	 * 账户的邮箱
	 */
	public String accountMail;

	/**
	 * 注册时候分配的唯一标识
	 */
	public String userId;

	/**
	 * 昵称
	 */
	public String nickName;

	/**
	 * 手机
	 */
	public String mobile;

	/**
	 * 账户是否登录
	 */
	public boolean bIsLogIn;

	/** 职业 **/
	public String occupation;
	
	/**用户的头像url**/
	public String userIcon;

	public AccountData() {
		accountName = "admin";
		accountPwd = StringUtil.MD5("123456");
		bIsLogIn = false;
	}

	public AccountData getAccountFromPre(final Context context) {
		if (null == context)
			return null;

		SharedPreferences share = context.getSharedPreferences(KEY_ACCOUNT_SHAPE, 0);
		this.accountName = share.getString(KEY_ACCOUNT_NAME, "");
		this.accountMail = share.getString(KEY_ACCOUNT_MAIL, "");
		this.userIcon = share.getString(KEY_USER_ICON, "");
		this.bIsLogIn = share.getBoolean(KEY_IS_LOGIN, false);
		this.userId = share.getString(KEY_USER_ID, "");
		this.occupation = share.getString(KEY_OCCUPATION, "");

		return this;
	}

	public void onSaveAccountToPre(final Context context) {

		if (null == context)
			return;

		SharedPreferences.Editor edit = context.getSharedPreferences(KEY_ACCOUNT_SHAPE, 0).edit();
		edit.putString(KEY_ACCOUNT_NAME, this.accountName);
		edit.putString(KEY_ACCOUNT_MAIL, this.accountMail);
		edit.putBoolean(KEY_IS_LOGIN, this.bIsLogIn);
		edit.putString(KEY_USER_ID, this.userId);
		edit.putString(KEY_USER_ICON, this.userIcon);
		edit.putString(KEY_OCCUPATION, this.occupation);
		edit.commit();
	}

	public void onClearData(final Context context) {
		if (null == context)
			return;

		SharedPreferences.Editor editor = context.getSharedPreferences(KEY_ACCOUNT_SHAPE, 0).edit();
		editor.clear();
		editor.commit();
	}
}
