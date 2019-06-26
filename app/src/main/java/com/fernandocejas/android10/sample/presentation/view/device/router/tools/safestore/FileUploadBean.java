package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.CustomCircleProgress;

import java.io.Serializable;

/**
 * 一个消息的JavaBean
 *
 * @author way
 *
 */
public class FileUploadBean implements Serializable{
	private int id;
	private String name;//
	private String num;
	private long sumSize;
	private long finishedSize;
	private String netSpeed;
	private String circleProgress;
	private String date;
	private String downloadsKey;//源地址
	private String path;//目的地址
	private int state;
	private long lastTime;
	private long lastCompleteSize;
	private long nowTime;
	private long nowSize;
	private String loadingSpeed;
	Boolean isLastTime = true; //用来记录网速
	private CustomCircleProgress customCircleProgress;

	//定义三种下载的状态：初始化状态，正在下载状态，暂停状态 INIT = 1; DOWNLOADING = 2; PAUSE = 3;

	public FileUploadBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public long getSumSize() {
		return sumSize;
	}

	public void setSumSize(long sumSize) {
		this.sumSize = sumSize;
	}

	public long getFinishedSize() {
		return finishedSize;
	}

	public void setFinishedSize(long finishedSize) {
		this.finishedSize = finishedSize;
	}

	public String getNetSpeed() {
		return netSpeed;
	}

	public void setNetSpeed(String netSpeed) {
		this.netSpeed = netSpeed;
	}

	public String getCircleProgress() {
		return circleProgress;
	}

	public void setCircleProgress(String circleProgress) {
		this.circleProgress = circleProgress;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDownloadsKey() {
		return downloadsKey;
	}

	public void setDownloadsKey(String downloadsKey) {
		this.downloadsKey = downloadsKey;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Boolean lastTime) {
		isLastTime = lastTime;
	}

	public Boolean getIsLastTime() {
		return isLastTime;
	}



	public CustomCircleProgress getCustomCircleProgress() {
		return customCircleProgress;
	}

	public void setCustomCircleProgress(CustomCircleProgress customCircleProgress) {
		this.customCircleProgress = customCircleProgress;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getLastCompleteSize() {
		return lastCompleteSize;
	}

	public void setLastCompleteSize(long lastCompleteSize) {
		this.lastCompleteSize = lastCompleteSize;
	}

	public long getNowTime() {
		return nowTime;
	}

	public void setNowTime(long nowTime) {
		this.nowTime = nowTime;
	}

	public long getNowSize() {
		return nowSize;
	}

	public void setNowSize(long nowSize) {
		this.nowSize = nowSize;
	}

	public String getLoadingSpeed() {
		return loadingSpeed;
	}

	public void setLoadingSpeed(String loadingSpeed) {
		this.loadingSpeed = loadingSpeed;
	}
}
