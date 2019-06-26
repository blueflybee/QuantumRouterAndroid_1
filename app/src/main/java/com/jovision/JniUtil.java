package com.jovision;

import android.app.Application;
import android.util.Log;
import android.view.Surface;

import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.mediatek.elian.ElianNative;

import java.io.IOException;

/**
 * Created by juyang on 16/3/23.
 */
public class JniUtil {

    private static final String TAG = "JniUtil";
    private static ElianNative elian;// 智联路由





    /****************************************     SDK初始化与释放      ******************************/

    /**
     * 1.初始化网络库（程序开启后必须调用此方法）
     *
     * @param app     Application
     * @param logPath 日志的路径
     * @param localIp 手机当前外网ip
     * @return
     */
    public static boolean initSDK(Application app, String logPath, String localIp) throws IOException {
        boolean initSdkRes = false;

        try {
            boolean initRes = Jni.init(app, 9200, logPath, localIp, 0,"");//初始化网络库
            String version = Jni.getVersion();
            Log.v(TAG, "initSDK:version=" + version);
            boolean enableHelper = false;
            if (initRes) {
                enableHelper = Jni.startLinkHelper(3, 10);//开启小助手
                Jni.enableLog(true);//打开底层log打印
            }
            Jni.enableStatistics(true);//显示统计
            Log.v(TAG, "initSDK:initSdkRes=" + initSdkRes + ";initRes=" + initRes + ";enableHelper=" + enableHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return initSdkRes;
    }

    /**
     * 2.退出软件时调用此方法
     *
     * @return
     */
    public static void deInitSDK() {
        Jni.deInit();
    }



    /****************************************     声波智联      ***********************************/


    /**
     * 发送声波(老)
     *
     * @param params 格式:wifiName;wifiPass
     * @param times  播放完成的回调是 AppConsts.CALL_GEN_VOICE
     */
    public static void oldSendSoundWave(String params, int times) {
        Jni.genVoice(params, times);
        Log.v(TAG, "old sendSoundWave:params=" + params);
    }

    /**
     * 发送声波(新)
     *
     * @param params 格式:wifiName;wifiPass
     * @param times  播放次数
     *
     */
    public static void newSendSoundWave(String params, int times) {
        Log.v(TAG, "new sendSoundWave-E:params=" + params);
        Jni.genSound(params, times);
        Log.v(TAG, "new sendSoundWave-X:params=" + params);
    }

    /**
     * 初始化智联路由
     */
    public static boolean initElian() {
        boolean result = false;
        if (!"x86".equalsIgnoreCase(android.os.Build.CPU_ABI)) {
            // 智联路由
            result = ElianNative.LoadLib();
            if (!result) {
                Log.e(TAG, "initElian:can't load elianjni lib");
            }
            elian = new ElianNative();
        }
        return result;
    }

    /**
     * 发送智联路由命令
     *
     * @param wifiName
     * @param wifiPass
     * @param authMode
     */
    public static void sendElian(String wifiName, String wifiPass, byte authMode) {
        if (!"x86".equalsIgnoreCase(android.os.Build.CPU_ABI)) {
            Log.v(TAG, "start zhilian...StartSmartConnection");
            elian.InitSmartConnection(null, 1, 1);// V1&V4
            elian.StartSmartConnection(wifiName, wifiPass,
                    "android smart custom", authMode);
        }
    }


    /**
     * 停止智联路由命令
     */
    public static void stopElian() {
        if (!"x86".equalsIgnoreCase(android.os.Build.CPU_ABI) && null !=
                elian) {
            int res = elian.StopSmartConnection();
            Log.v(TAG, "stop zhilian is open elianState=" + res);
        } else {
            Log.v(TAG, "stop zhilian is already closed");
        }
    }




    /************************************    以下功能云视通和流媒体协议共用      ***********************************/

    /************************************     视频显示与暂停      ***********************************/

    /**
     * 暂停底层显示
     *
     * @param index 窗口索引
     * @return true：成功，false：失败
     */
    public static boolean pauseSurface(int index) {
        return Jni.pauseSurface(index);
    }

    /**
     * 恢复底层显示
     *
     * @param index   窗口索引
     * @param surface
     * @return true：成功，false：失败
     */
    public static boolean resumeSurface(int index, Object surface) {
        return Jni.resumeSurface(index, surface);
    }

    /**
     * 暂停实时视频播放
     *
     * @param index
     */
    public static boolean pauseVideo(int index) {
        pauseSurface(index);
        return Jni.sendBytes(index,
                JVNetConst.JVN_CMD_VIDEOPAUSE, new byte[0], 8);
    }


    /**
     * 继续实时视频播放
     *
     * @param index
     */
    public static boolean resumeVideo(int index, Surface surface) {
        boolean resume = resumeSurface(index, surface);
        return Jni.sendBytes(index,
                JVNetConst.JVN_CMD_VIDEO, new byte[0], 8);
    }



    /****************************************     音频监听      ***********************************/

    /**
     * 开启监听
     *
     * @param window
     */
    public static boolean openSound(int window) {
        Jni.setAecDenoise(window, false, true);
        resumeAudio(window);
        return Jni.playAudio(window, true);
    }

    /**
     * 关闭监听
     *
     * @param window
     */
    public static boolean closeSound(int window) {
        pauseAudio(window);
        return Jni.playAudio(window, false);
    }



    /**
     * 开始：录本地录音并发送
     */
    public static boolean startRecordSendAudio(int index) {
        return Jni.recordAndsendAudioData(index);
    }

    /**
     * 停止：录本地录音并发送
     */
    public static boolean stopRecordSendAudio(int index) {
        return Jni.stopRecordAudioData(index);
    }

    /**
     * 设置aec和降噪启用与否
     */
    public static void resetAecDenoise(int index, boolean aec, boolean denoise) {
        Jni.setAecDenoise(index, aec, denoise);
    }

    /**
     * 恢复音频
     */
    public static boolean resumeAudio(int index) {
        return Jni.resumeAudio(index);
    }

    /**
     * 暂停音频
     */
    public static boolean pauseAudio(int index) {
        return Jni.pauseAudio(index);
    }





    /****************************************     录像      ***********************************/

    /**
     * 开始录像
     */
    public static boolean startRecord(int index) {
        String fileName = System.currentTimeMillis()
                + AppConsts.VIDEO_MP4_KIND;
        boolean recordRes = Jni.startRecord(index, AppConsts.VIDEO_PATH + fileName, true, true, 0);
        return recordRes;
    }

    /**
     * 停止录像
     */
    public static boolean stopRecord(int index) {
        return Jni.stopRecord(index);
    }


    /****************************************     抓拍      ***********************************/
    /**
     * 抓拍
     *
     * @param index
     */
    public static boolean capture(int index) {
        String fileName = System.currentTimeMillis()
                + AppConsts.IMAGE_JPG_KIND;

        boolean res = Jni.screenShot(index, AppConsts.CAPTURE_PATH + fileName, 100);
        return res;
    }


    /**
     * 设置下载路径
     * @param index
     * @param fileName
     * @return
     */
    public static boolean setDownloadFileName(int index,String fileName){
        return Jni.setDownloadFileName(index,fileName);
    }

    /************************************    以上功能云视通和流媒体协议共用      ***********************************/






}
