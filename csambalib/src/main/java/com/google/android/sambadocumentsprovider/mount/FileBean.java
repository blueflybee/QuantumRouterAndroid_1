package com.google.android.sambadocumentsprovider.mount;

import java.io.Serializable;

/**
 * 一个消息的JavaBean
 * 
 * @author way
 * 
 */
public class FileBean implements Serializable {
	private String name;//
	private String path;
	private String count;
	private String size;
	private String date;//
	private String lastModifyTime;
	private String transitSpeed;
	private boolean isFloder = true;// true代表文件夹 false 代表文件
	private long longDate; //记录long型的日期

	public FileBean() {
	}

	public FileBean(String name, String count, String size, String date, String lastModifyTime, String transitSpeed, boolean isFloder) {
		this.name = name;
		this.count = count;
		this.size = size;
		this.date = date;
		this.lastModifyTime = lastModifyTime;
		this.transitSpeed = transitSpeed;
		this.isFloder = isFloder;
	}

	public boolean getFileType() {
		return isFloder;
	}

	public void setFileType(boolean floder) {
		isFloder = floder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getTransitSpeed() {
		return transitSpeed;
	}

	public void setTransitSpeed(String transitSpeed) {
		this.transitSpeed = transitSpeed;
	}

	public void setLongDate(long longDate) {
		this.longDate = longDate;
	}

	public long getLongDate() {
		return longDate;
	}

}
