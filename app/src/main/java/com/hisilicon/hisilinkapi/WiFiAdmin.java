package com.hisilicon.hisilinkapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class WiFiAdmin {
    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;
    static final int SECURITY_ERR = 4;

    private WifiManager mWifiManager;

    private WifiInfo mWifiInfo;

    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfigurations;
    private ConnectivityManager mConnectivityManager;
    private static final String TAG = "WifiAdmin";
    public WiFiAdmin(Context context){

        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        mWifiInfo=mWifiManager.getConnectionInfo();

        mWifiConfigurations=mWifiManager.getConfiguredNetworks();
        mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void startScan(){
        mWifiManager.startScan();

        mWifiList=mWifiManager.getScanResults();
    }

    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }


    public List<ScanResult> getWifiList(){
        return mWifiList;
    }


    public String getWifiSSID(){
        mWifiInfo=mWifiManager.getConnectionInfo();
        String str = (mWifiInfo==null)?"NULL":mWifiInfo.getSSID();

        str = str.substring(1,str.length()-1);
        return str;
    }


    public int getWifiIPAdress(){
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();
    }


    public String getWifiGWAdress(){
        mWifiInfo=mWifiManager.getConnectionInfo();
        int ipAddress = (mWifiInfo==null) ? 0 : mWifiInfo.getIpAddress();
        return String.format(Locale.getDefault(), "%d.%d.%d.1",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff));
    }


    public int getSecurity() {
        for (WifiConfiguration mWifiConfiguration : mWifiConfigurations) {

            String mCurrentSSID = getWifiSSID();


            String mConfigSSid = mWifiConfiguration.SSID;

            mConfigSSid = mConfigSSid.substring(1,mConfigSSid.length()-1);


            if (mCurrentSSID.equals(mConfigSSid)&&mWifiInfo.getNetworkId()==mWifiConfiguration.networkId) {
                if (mWifiConfiguration.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
                    return SECURITY_PSK;
                }
                if (mWifiConfiguration.allowedKeyManagement.get(KeyMgmt.WPA_EAP) || mWifiConfiguration.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
                    return SECURITY_EAP;
                }
                return (mWifiConfiguration.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
            }
        }

        return SECURITY_ERR;
    }


    public void addNetWork(WifiConfiguration configuration){
        int wcgId=mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
        mWifiManager.reconnect();
        //mWifiManager.saveConfiguration();
    }

    public void reconnect(){
        mWifiManager.reconnect();
    }

    public void enableNetWork(int wcgId){
        mWifiManager.enableNetwork(wcgId, true);
    }

    public boolean isWifiConnected()
    {
        boolean flag = false;
        if (null != mConnectivityManager) {
            NetworkInfo nif = mConnectivityManager.getActiveNetworkInfo();
            if (null != nif && nif.isConnected()) {
                if (nif.getState() == NetworkInfo.State.CONNECTED) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    public int getNetWorkId(){
        return (mWifiInfo==null)?-1:mWifiInfo.getNetworkId();
    }


    public void disConnectionWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs == null)
            return null;
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID!=null && existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }


    public void forgetWifi(String SSID){
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            Log.d(TAG,"tempConfig.networkId="+tempConfig.networkId);
            mWifiManager.removeNetwork(tempConfig.networkId);
            //mWifiManager.saveConfiguration();
        }
    }


    public WifiConfiguration createWifiInfo(String SSID, String Password, int Type){
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if(Type == 1) //WIFICIPHER_NOPASS
        {
            config.hiddenSSID = true;
            config.allowedKeyManagement.set(KeyMgmt.NONE);
        }

        if(Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }
}
