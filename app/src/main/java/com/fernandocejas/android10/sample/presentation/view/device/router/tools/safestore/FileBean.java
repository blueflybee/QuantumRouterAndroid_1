package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.net.Uri;

import java.io.Serializable;

/**
 * 一个消息的JavaBean
 *
 * @author way
 *
 */
public class FileBean implements Serializable {
	private String name;//
	private Uri key;
	private String path;
	private String count;
	private String size;
	private long longSize;
	private String date;//
	private String lastModifyTime;
	private String transitSpeed;
	private boolean isFloder = true;// true代表文件夹 false 代表文件
	private long longDate; //记录long型的日期

	public FileBean() {
	}

	public FileBean(String name, Uri key, String path, String count, String size, long longSize, String date, String lastModifyTime, String transitSpeed, boolean isFloder, long longDate) {
		this.name = name;
		this.key = key;
		this.path = path;
		this.count = count;
		this.size = size;
		this.longSize = longSize;
		this.date = date;
		this.lastModifyTime = lastModifyTime;
		this.transitSpeed = transitSpeed;
		this.isFloder = isFloder;
		this.longDate = longDate;
	}

	public long getLongSize() {
		return longSize;
	}

	public void setLongSize(long longSize) {
		this.longSize = longSize;
	}

	public Uri getKey() {
		return key;
	}

	public void setKey(Uri key) {
		this.key = key;
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

	@Override
	public String toString() {
		return "FileBean{" +
				"name='" + name + '\'' +
				", key=" + key +
				", path='" + path + '\'' +
				", count='" + count + '\'' +
				", size='" + size + '\'' +
				", date='" + date + '\'' +
				", lastModifyTime='" + lastModifyTime + '\'' +
				", transitSpeed='" + transitSpeed + '\'' +
				", isFloder=" + isFloder +
				", longDate=" + longDate +
				'}';
	}
}
