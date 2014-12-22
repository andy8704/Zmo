package com.zmo.model;

public class ActivityDetailModel extends ActivityModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034492426635352721L;

	public String address;
	public String activityTime;
	
	/** 课程价格 **/
	public long price;
	/** 导师名 **/
	public String tutorName;
	/** 导师头像 **/
	public String tutorIconUrl;
	/** 导师职业 **/
	public String tutorOccupation;
	/** 导师描述 **/
	public String tutorDesp;
	
}
