package com.hisilicon.hisilinkapi;

public class HisiLibApi {
	static public native int setNetworkInfo(int security, int port, int onlineProtocal, int ipAdress, String ssid, String password, String deviceName);

	static public native byte []getMessageToSend();

	static public native String getPassword(String ssid);

	static public native int startMulticast();

	static public native int stopMulticast();
}