package com.fernandocejas.android10.sample.presentation.view.safe;

/**
 * 一个消息的JavaBean
 * 
 * @author way
 * 
 */
public class DeviceEntity<T> {
	private String key;
	private String name;
	private int type;//0 表示lock;1 表示 router
	public Boolean isItemExpanded; //标志每个item是否展开
	private int state;//0：等待检测 1：检测中 2：检测完成 3:设备没有密钥
	private Boolean hasKey; // 判断是否有有密钥

	T chilBeans;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHasKey() {
		return hasKey;
	}

	public void setHasKey(Boolean hasKey) {
		this.hasKey = hasKey;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setItemExpanded(Boolean itemExpanded) {
		isItemExpanded = itemExpanded;
	}

	public Boolean getItemExpanded() {
		return isItemExpanded;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public T getChilBeans() {
		return chilBeans;
	}

	public void setChilBeans(T chilBeans) {
		this.chilBeans = chilBeans;
	}

	@Override
	public String toString() {
		return "DeviceEntity{" +
				"key='" + key + '\'' +
				", name='" + name + '\'' +
				", type=" + type +
				", isItemExpanded=" + isItemExpanded +
				'}';
	}

	public static class ChildEntity{
		private String content;
		private int testState;//0:不可见 1：可见并检测中 2：检测完成
		private Boolean isOpened; //是否开启

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getTestState() {
			return testState;
		}

		public void setTestState(int testState) {
			this.testState = testState;
		}

		public void setOpened(Boolean opened) {
			isOpened = opened;
		}

		public Boolean getOpened() {
			return isOpened;
		}

		@Override
		public String toString() {
			return "ChildEntity{" +
					"content='" + content + '\'' +
					", testState=" + testState +
					'}';
		}
	}
}
