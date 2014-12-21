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

	public AccountData() {
		accountName = "admin";
		accountPwd = StringUtil.MD5("123456");
		bIsLogIn = false;
	}

	public AccountData getAccountFromPre(final Context context) {
		if (null == context)
			return null;

		SharedPreferences share = context.getSharedPreferences("account", 0);
		this.accountName = share.getString("accountName", "");
		this.accountMail = share.getString("accountMail", "");
		this.bIsLogIn = share.getBoolean("bIsLogIn", false);
		this.userId = share.getString("userId", "");
		this.occupation = share.getString("occupation", "");

		return this;
	}

	public void onSaveAccountToPre(final Context context) {

		if (null == context)
			return;

		SharedPreferences.Editor edit = context.getSharedPreferences("account", 0).edit();
		edit.putString("accountName", this.accountName);
		edit.putString("accountMail", this.accountMail);
		edit.putBoolean("bIsLogIn", this.bIsLogIn);
		edit.putString("userId", this.userId);
		edit.putString("occupation", this.occupation);
		edit.commit();
	}

	public void onClearData(final Context context) {
		if (null == context)
			return;

		SharedPreferences.Editor editor = context.getSharedPreferences("account", 0).edit();
		editor.clear();
		editor.commit();
	}
}
