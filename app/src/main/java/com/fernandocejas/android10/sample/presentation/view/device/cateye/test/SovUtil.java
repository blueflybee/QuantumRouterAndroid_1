package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.util.Log;
import android.view.Surface;

import com.jovision.JVNetConst;
import com.jovision.Jni;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 流媒体协议相关JNI接口
 * Created by juyang on 17/6/23.
 */

public class SovUtil {

    private static final String TAG = "SovUtil";


    /******************* 流媒体协议 **********************/

    /**
     * 流媒体服务器上增加设备号码（进App获取到设备列表立即调用此方法把设备号添加到服务器上，同时新添加设备也需调用此接口）
     *
     * @param ystNoArray
     */
    public static void addYSTNOS(String[] ystNoArray) {
        Jni.sovAddYSTNOS(ystNoArray);
    }

    /**
     * 流媒体服务器上删除设备号码（删除设备的时候需要调用此方法，删除服务器上保存的设备信息）
     *
     * @param ystNoArray
     */
    public static void delYSTNOS(String[] ystNoArray) {
        Jni.sovDelYSTNOS(ystNoArray);
    }


    /**
     * 流媒体协议局域网广播
     */
    public static void searchLanDev() {
        Jni.sovSearchLocalLanDevice();
        Log.v(TAG, "sovSearchLanDev-局域网广播");
    }

    /**
     * 流媒体协议视频连接，需要显示视频
     *
     * @param group
     * @param ystNo
     * @param devUser
     * @param devPwd
     * @param surface
     * @return
     */
    public static int connect(String group, int ystNo, String devUser, String devPwd, boolean apConnect, Surface surface) {
        int result = -1;

        Log.e(TAG, "connected By Sov:devNum=" + ystNo
                + ";user=" + devUser
                + ";pwd=" + devPwd
                + ";group=" + group
                + ";num=" + ystNo + ";\nresult=" + result);

        result = Jni.connect(0, 1, 1,
                "", 0, devUser,
                devPwd, ystNo, group,
                JVNetConst.JVN_TRYTURN, JVNetConst.TYPE_3GMO_UDP, surface,
                apConnect, "", 1, JVNetConst.CONNECT_BY_SOV,false,"",0);


        return result;
    }

    /**
     * 断开指定窗口视频
     *
     * @param index
     * @return
     */
    public static boolean disconnect(int index) {
        return Jni.disConnect(index);
    }


    /**
     * @param status 上（1）、下（2）、左（3）、右（4）、停止（5）
     * @param data   灵敏度 ptz_speed=30
     * @see 云台设置：开始、结束、方向 2017/04/11添加
     *
     *               0x01 开启，0x02 关闭
     */

    public static boolean setPtz(int window, int status, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_PTZ,
                0, (byte) status,
                data == null ? null : data.getBytes(),
                data == null ? 0 : data.length());
        Log.e(TAG, "setPtz:status=" + status + ";window=" + window + ";data=" + data);
        return sendRes;
    }


    /**
     * 开启对讲
     *
     * @param window
     * @return
     */
    public static boolean startStreamVoiceCall(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_CHAT,
                0, JVNetConst.SRC_EX_START_CHAT, null, 0);
        Log.e(TAG, "startStreamVoiceCall_res=" + sendRes + ";window=" + window);
        return sendRes;
    }

    /**
     * 12.关启对讲
     *
     * @param window
     * @return
     */
    public static boolean stopStreamVoiceCall(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_CHAT,
                0, JVNetConst.SRC_EX_STOP_CHAT, null, 0);
        Log.e(TAG, "stopStreamVoiceCall_res=" + sendRes + ";window=" + window);
        return sendRes;
    }

    /**
     * 17.猫眼流媒体切换码流
     *
     * @param index
     * @param stream 当前码流获取是从所有参数里面获取，参考JVPlayActivity  623-626行
     *               切换完成后有回调：回调如下，亦可参考JVPlayActivity  357-370行 case JVNetConst.CALL_CATEYE_SENDDATA: {
     *               //流媒体猫眼，设置协议回调
     *               case JVNetConst.CALL_CATEYE_SENDDATA: {
     *               try {
     *               org.json.JSONObject object = new org.json.JSONObject(obj.toString());
     *               int nType = object.getInt("nPacketType");
     *               int nCmd = object.getInt("nCmd");
     *               switch (nCmd) {
     *               //码流切换回调
     *               case JVNetConst.SRC_REMOTE_CMD: {
     *               int result = object.getInt("result");
     *               if (1 == result) {
     *               Toast.makeText(JVCloudPlayActivity.this, "码流切换成功了！", Toast.LENGTH_LONG).show();
     *               }
     *               break;
     *               }
     */
    public static boolean streamCatChangeStream(int index, int stream) {
        String changeStr = String.format(AppConsts.FORMATTER_STREAM_CAT_CHANGE_STREAM, stream);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_CMD,
                0, JVNetConst.SRC_EX_CMD_RESOLUTION, changeStr.getBytes(), changeStr.length());
        Log.e(TAG, "streamCatChangeStream=" + sendRes + ";window=" + index + ";changeStr=" + changeStr);
        return sendRes;
    }



    /**
     * 20.检索远程回放视频列表
     *
     * @param index
     * @param year
     * @param month
     * @param day
     */
    public static boolean checkStreamRemoteVideo(int index, int year, int month, int day) {
        String date = String.format(AppConsts.FORMATTER_STREAM_CAT_REMOTE_CHECK_DATE, year,
                month, day, year, month, day);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_CHECK,
                0, JVNetConst.SRC_EX_CHECK_VIDEO, date.getBytes(), date.length());
        Log.e(TAG, "checkStreamRemoteVideo=" + sendRes + ";window=" + index + ";date=" + date);
        return sendRes;
    }

    /**
     * 21.检索远程回放图片列表
     *
     * @param index
     * @param year
     * @param month
     * @param day
     */
    public static boolean checkStreamRemoteImage(int index, int year, int month, int day) {

        String date = String.format(AppConsts.FORMATTER_STREAM_CAT_REMOTE_CHECK_DATE, year,
                month, day, year, month, day);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_CHECK,
                0, JVNetConst.SRC_EX_CHECK_PCITURE, date.getBytes(), date.length());
        Log.e(TAG, "checkStreamRemoteImage=" + sendRes + ";window=" + index);
        return sendRes;
    }


    /**
     * 22.流媒体猫眼取消下载
     *
     * @param index
     */
    public static boolean cancelStreamCatDownload(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_DOWNLOAD,
                0, JVNetConst.SRC_EX_RD_CMD_UPLOADBREAK, null, 0);
        Log.e(TAG, "cancelStreamCatDownload=" + sendRes + ";window=" + index);
        return sendRes;
    }

    /**
     * 23.流媒体猫眼请求下载
     *
     * @param index
     */
    public static boolean startStreamCatDownload(int index, String filePath) {
        String param = String.format(AppConsts.FORMATTER_STREAM_CAT_DOWNLOAD_PATH, filePath);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_DOWNLOAD,
                0, JVNetConst.SRC_EX_RD_REQ_DOWNLOAD, param.getBytes(), param.length());
        Log.e(TAG, "startStreamCatDownload=" + sendRes + ";window=" + index + ";param=" + param);
        return sendRes;
    }



    /**
     * 25.流媒体猫眼请求停止远程回放
     *
     * @param index
     * @return
     */
    public static boolean stopStreamCatRemotePlay(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYSTOP, null, 0);
        Log.e(TAG, "stopStreamCatRemotePlay=" + sendRes + ";window=" + index);
        return sendRes;
    }


    /**
     * 26.流媒体猫眼 暂停 远程回放
     *
     * @param index
     * @return
     */
    public static boolean pauseStreamCatRemotePlay(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYPAUSE, null, 0);
        Log.e(TAG, "pauseStreamCatRemotePlay=" + sendRes + ";window=" + index);
        return sendRes;
    }

    /**
     * 27.流媒体猫眼 继续 远程回放
     *
     * @param index
     * @return
     */
    public static boolean goonStreamCatRemotePlay(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYGOON, null, 0);
        Log.e(TAG, "goonStreamCatRemotePlay=" + sendRes + ";window=" + index);
        return sendRes;
    }




    /**
     * 29.流媒体猫眼远程回放调节播放进度
     *
     * @param index
     * @param seekProgress
     */
    public static boolean streamCatSeekTo(int index, int seekProgress) {
        String param = String.format(AppConsts.FORMATTER_STREAM_CAT_SEEKPOS, seekProgress);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYSEEK, param.getBytes(), param.length());
        Log.e(TAG, "streamCatSeekTo=" + sendRes + ";seekProgress=" + seekProgress);
        return sendRes;
    }

    /**
     * 30.设置下载路径
     *
     * @param fileFullPath
     * @return
     */
    public static void setDownloadFilePath(int index, String fileFullPath) {
        Jni.setDownloadFileName(index, fileFullPath);
    }


    /**
     * 33.获取流媒体猫眼文件列表
     *
     * @param fileStr
     * @param dateStr
     * @return
     */
    public static ArrayList<StreamFile> getStreamCatFileList(String fileStr, String dateStr) {
        ArrayList<StreamFile> dataList = new ArrayList<StreamFile>();

        try {
            dateStr = dateStr.replace("-", "");
            if (null == fileStr || "".equalsIgnoreCase(fileStr)) {
                return dataList;
            }
            String[] fileArray = fileStr.split(";");
            for (int i = 0; i < fileArray.length; i++) {
                StreamFile cf = new StreamFile();
                String fileString = fileArray[i];
                String thumbnailKind = String.valueOf(fileString.charAt(0));//T or X
                String meidaKind = String.valueOf(fileString.charAt(1));//P or V
                String alarmKind = String.valueOf(fileString.charAt(2));//N or A

                cf.setName(String.format("%c%c:%c%c:%c%c",
                        fileString.charAt(5), fileString.charAt(6), fileString.charAt(7),
                        fileString.charAt(8), fileString.charAt(9), fileString.charAt(10)));

                Log.e("fullList-" + i, "name=" + cf.getName());
                cf.setFileDate(String.format("%c%c:%c%c:%c%c",
                        fileString.charAt(5), fileString.charAt(6), fileString.charAt(7),
                        fileString.charAt(8), fileString.charAt(9), fileString.charAt(10)));
                Log.e("fullList-" + i, "date=" + cf.getFileDate());

                //文件类型
                if ("P".equalsIgnoreCase(meidaKind)) {
                    cf.setMediaKind(StreamFile.MEDIA_PICTURE);
                    cf.setFilePath(File.separator + "mnt" + File.separator + "misc" + File.separator + dateStr + File.separator + String.format("%c%c%c%c%c%c%c%c%c",
                            fileString.charAt(2), fileString.charAt(3), fileString.charAt(4), fileString.charAt(5), fileString.charAt(6),
                            fileString.charAt(7), fileString.charAt(8), fileString.charAt(9),
                            fileString.charAt(10)) + ".jpg");
                } else if ("V".equalsIgnoreCase(meidaKind)) {
                    cf.setMediaKind(StreamFile.MEDIA_VIDEO);
                    cf.setFilePath(File.separator + "mnt" + File.separator + "misc" + File.separator + dateStr + File.separator + String.format("%c%c%c%c%c%c%c%c%c",
                            fileString.charAt(2), fileString.charAt(3), fileString.charAt(4), fileString.charAt(5), fileString.charAt(6),
                            fileString.charAt(7), fileString.charAt(8), fileString.charAt(9),
                            fileString.charAt(10)) + ".mp4");
                }

                //正常文件，报警文件
                if ("N".equalsIgnoreCase(alarmKind)) {
                    cf.setFileKind(StreamFile.FILE_NORMAL);
                } else if ("A".equalsIgnoreCase(alarmKind)) {
                    cf.setFileKind(StreamFile.FILE_ALARM);
                }

                //
                if ("T".equalsIgnoreCase(thumbnailKind)) {
                    cf.setThumbnailPath(File.separator + "mnt" + File.separator + "misc" + File.separator + dateStr + File.separator + String.format("%c%c%c%c%c%c%c%c%c%c%c",
                            fileString.charAt(0), fileString.charAt(1), fileString.charAt(2), fileString.charAt(3), fileString.charAt(4), fileString.charAt(5), fileString.charAt(6),
                            fileString.charAt(7), fileString.charAt(8), fileString.charAt(9),
                            fileString.charAt(10)) + ".jpg");
                } else {
                    cf.setThumbnailPath("");
                }

                Log.e("fullList-" + i, "ThumbnailPath=" + cf.getThumbnailPath());
                cf.setIndex(dataList.size());
                dataList.add(cf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    //获取全部字段信息
    public static boolean getStreamCatDataAll(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_PARAM_ALL,
                0, JVNetConst.SRC_EX_GETPARAM, null, 0);
        Log.e(TAG, "streamCatSendData_res=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //设置感应门铃按键灯开关：0关，1开
    public static boolean setStreamBellLightStatus(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_DISPLAY,
                0, JVNetConst.SRC_EX_DISPLAY_BELLLIGHT, data.getBytes(), data.length());
        Log.e(TAG, "setBellLightStatus=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }


    //设置报警类型：0图片，1视频
    public static boolean setStreamAlarmType(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_ALARMTYPE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmType=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    //设置红外感应开关
    public static boolean setStreamPirEnable(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_PIR, data.getBytes(), data.length());
        Log.e(TAG, "setStreamPirEnable=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    //设置重力感应
    public static boolean setStreamGSensorEnable(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_GSENSOR, data.getBytes(), data.length());
        Log.e(TAG, "setStreamGSensorEnable=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    //设置移动侦测开关
    public static boolean setStreamMDetect(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_MDETECT, data.getBytes(), data.length());
        Log.e(TAG, "setStreamMDetect=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    /**
     * 设置门外报警音开关（2017.2.14新增）
     */
    public static boolean setStreamFriendAlarmEnable(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_FRIEND_ALARM, data.getBytes(), data.length());
        Log.e(TAG, "setStreamFriendAlarmEnable=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }


    //设备重启
    public static boolean streamRestart(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_ABOUTEYE,
                0, JVNetConst.SRC_EX_ABOUT_REBOOT, null, 0);
        Log.e(TAG, "streamRestart=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //设备恢复出厂设置
    public static boolean streamReset(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_ABOUTEYE,
                0, JVNetConst.SRC_EX_ABOUT_FORMAT, null, 0);
        Log.e(TAG, "streamReset=" + sendRes + ";window=" + window);
        return sendRes;
    }

    /**
     * 猫眼流媒体 获取设备当前连接所用IP
     *
     * @param index
     * @return
     */
    public static boolean getStreamCatDevIP(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_CMD,
                0, JVNetConst.SRC_EX_CMD_GET_IP, null, 0);
        Log.e(TAG, "getStreamCatDevIP=" + sendRes + ";window=" + index);
        return sendRes;
    }

    //获取设备SD卡信息
    public static boolean getStreamCatSDinfo(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_REFRESH, null, 0);
        Log.e(TAG, "getStreamCatSDinfo=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //SD卡设置存储分辨率
    public static boolean setStreamSDResolution(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_RESOLUTION, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDResolution=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //SD卡设置录像时间
    public static boolean setStreamSDRecordTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_RECORDTIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDRecordTime=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //SD卡设置自动覆盖开关
    public static boolean setStreamSDAutoSwitch(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_AUTOSWITCH, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDAutoSwitch=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //SD卡格式化
    public static boolean setStreamSDFormat(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_CMD_FORMAT, null, 0);
        Log.e(TAG, "setStreamSDAutoSwitch=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //获取设备时间信息
    public static boolean getStreamCatTime(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_GETSYSTIME, null, 0);
        Log.e(TAG, "getStreamCatTime=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //修改猫眼所在时区
    public static boolean setStreamCatZone(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_ZONE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDResolution=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //修改时间格式
    public static boolean setStreamCatTimeFormat(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_FORMAT, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDResolution=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //修改猫眼网络校对开关
    public static boolean setStreamCatSntp(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_SNTP, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSDResolution=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }

    //设置设备时间
    public static boolean setStreamCatTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETSYSTIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamCatTime=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //向设备请求升级
    public static boolean cmdStreamCatUpdateInfo(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_FIRMUP_REQ, null, 0);
        Log.e(TAG, "getStreamCatUpdateInfo=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //发送命令给设备开始下载升级文件
    public static boolean cmdStreamCatUpdateDownload(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_UPLOAD_START_STREAM, null, 0);
        Log.e(TAG, "getStreamCatUpdateDownload=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //发送命令给设备取消下载升级文件
    public static boolean cmdStreamCatUpdateCancel(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_UPLOAD_CANCEL_STREAM, null, 0);
        Log.e(TAG, "getStreamCatUpdateCacel=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //发送命令给设备获取下载升级文件进度
    public static boolean cmdStreamCatUpdateProgress(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_UPLOAD_PROGRESS, null, 0);
        Log.e(TAG, "getStreamCatUpdateProgress=" + sendRes + ";window=" + window);
        return sendRes;
    }

    //发送命令给设备烧写程序
    public static boolean cmdStreamCatUpdateFirmUp(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_FIRMUP_START, null, 0);
        Log.e(TAG, "getStreamCatUpdateProgress=" + sendRes + ";window=" + window);
        return sendRes;
    }

    /**
     * 发送命令给设备烧写程序
     */
    public static boolean cmdStreamCatUpdateFirmUpStart(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_FIRMUP,
                0, JVNetConst.SRC_EX_FIRMUP_START, null, 0);
        Log.e(TAG, "cmdStreamCatUpdateFirmUpStart=" + sendRes + ";window=" + window);
        return sendRes;
    }


    /**
     * 获取设备SD卡使用信息
     *
     * @param index
     * @return
     */
    public static boolean getStreamCatStorage(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_CMD,
                0, JVNetConst.SRC_EX_CMD_GET_STORAGE, null, 0);
        Log.e(TAG, "getStreamCatStorage=" + sendRes + ";window=" + index);
        return sendRes;
    }


    /**
     * 设置按门铃亮屏开关：0关，1开
     */
    public static boolean setStreamRingAndLCD(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_DISPLAY,
                0, JVNetConst.SRC_EX_DISPLAY_RINGANDLCD, data.getBytes(), data.length());
        Log.e(TAG, "setStreamRingAndLCD=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    /**
     * 设置宽动态开关：0关闭，1开启
     * */
    public static boolean setStreamWDR(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_DISPLAY,
                0, JVNetConst.SRC_EX_DISPLAY_WDR, data.getBytes(), data.length());
        Log.e(TAG, "setStreamWDR=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    /**
     * 设置门铃遮挡报警开关（2017.9.27新增 by yinpeng）
     * */
    public static boolean setStreamCoverAlarmEnable(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_COVER_ALARM, data.getBytes(), data.length());
        Log.e(TAG, "setStreamCoverAlarmEnable=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }


    /**
     * 设置设备语言类型：0中文，1英文
     */
    public static boolean setStreamLanguage(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_DISPLAY,
                0, JVNetConst.SRC_EX_DISPLAY_LANGUAGE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamLanguage=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    /**
     * 设置红外感应检测时间PirTime=? 0/1/2 -- 5/10/15 s
     */
    public static boolean setStreamPirTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_PIR_TIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamPirTime=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }

    /**
     * 流媒体设置猫眼休眠时间
     */
    public static boolean setStreamSuspentTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_DISPLAY,
                0, JVNetConst.SRC_EX_DISPLAY_SUSPENDTIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSuspentTime=" + sendRes + ";window=" + window + ";type:" + data);
        return sendRes;
    }


    /**
     * <--连接设备获取-->
     * V8(全转发)
     * 设置安全防护时间段
     */
    public static boolean setStreamAlarmTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_ALARM_TIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmTime=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }

    /**
     * <--连接设备获取-->
     * V8(全转发)
     * 设置移动侦测灵敏度
     */
    public static boolean setStreamAlarmMDSens(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_MDETECT_SENS, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmMDSens=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }


    /**
     * V8(全转发)<--连接设备获取-->
     * 获取报警推送相关：推送开关、声音开关、起始时间段，都走本接口
     */
    public static boolean getStreamAlarm(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_REFRESH, null, 0);
        Log.e(TAG, "getStreamAlarm=" + sendRes + ";window=" + window);
        return sendRes;
    }



    /**
     * V8(全转发)<--连接设备获取-->
     * 获取存储卡信息和录像相关设置
     */
    public static boolean getStreamSDCard(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.EX_STORAGE_REFRESH, null, 0);
        Log.e(TAG, "getStreamSDCard=" + sendRes + ";window=" + window);
        return sendRes;
    }

    /**
     * V8(全转发)<--连接设备获取-->
     * 设置录像模式
     */
    public static boolean setStreamRecordMode(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_RECORDMODE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamRecordMode=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }



    /**
     * V8(全转发)<--连接设备获取-->
     * 设置抽帧录像时间
     */
    public static boolean setStreamRecordChframe(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_STORAGE,
                0, JVNetConst.SRC_EX_STORAGE_RECORDMODE_CHFRAME_TIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamRecordChframe=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }



    /**
     * V8 全转发协议
     * 现在使用的视频翻转
     *
     * @param window
     * @param screenTag   当前视频镜像正反， 0 和 2
     * @param effect_flag 码流数据获取的字段 回调：码流数据返回effect_flag
     */
    public static boolean streamRotateVideo(int window, int screenTag, int effect_flag) {
        int send = 0;
        if (AppConsts.SCREEN_NORMAL == screenTag) {
            send = 0x04 | effect_flag | 0x02;
        } else if (AppConsts.SCREEN_OVERTURN == screenTag) {
            send = (~0x04) & effect_flag & (~0x02);
        }
        String data = String.format(AppConsts.FORMAT_NEFFECT_FLAG, send);

        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_EFFECT_FLAG, data.getBytes(), data.length());
        Log.e(TAG, "streamRotateVideo=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }



    /**
     * V8 全转发协议<--不用连接设备直接获取-->
     * 现在使用的视频镜像
     *
     * @param window
     * @param screenTag   当前视频正反， 0 和 4
     * @param effect_flag 码流数据获取的字段 回调：码流数据返回effect_flag
     */
    public static boolean streamMirrorVideo(int window, int screenTag, int effect_flag) {
        int send = 0;
        if (AppConsts.SCREEN_NORMAL == screenTag) {
            send = 0x04 | effect_flag;
        } else if (AppConsts.SCREEN_OVERTURN == screenTag) {
            send = (~0x04) & effect_flag;
        }
        String data = String.format(AppConsts.FORMAT_NEFFECT_FLAG, send);

        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_EFFECT_FLAG, data.getBytes(), data.length());
        Log.e(TAG, "streamRotateVideo=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }



    /**
     * V8(全转发)<--连接设备获取-->
     * 设置LDC 畸变校正
     */
    public static boolean setStreamDistortion(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_LDC, data.getBytes(), data.length());
        Log.e(TAG, "setStreamDistortion=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }



    /**
     * <--连接设备获取-->
     * 修改网络校对开关
     */
    public static boolean setStreamSntp(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_SNTP, data.getBytes(), data.length());
        Log.e(TAG, "setStreamSntp=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }



    /**
     * <--连接设备获取-->
     * 设置设备时间
     */
    public static boolean setStreamTime(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETSYSTIME, data.getBytes(), data.length());
        Log.e(TAG, "setStreamTime=" + sendRes + ";window=" + window + "; data:" + data);
        return sendRes;
    }



    /**
     * <--不用连接设备直接获取-->
     * 修改时间格式
     */
    public static boolean setStreamTimeFormat(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_FORMAT, data.getBytes(), data.length());
        Log.e(TAG, "setStreamTimeFormat=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }



    /**
     * <--连接设备获取-->
     * 修改设备所在时区
     */
    public static boolean setStreamZone(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_TIME,
                0, JVNetConst.SRC_EX_SETTIME_ZONE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamZone=" + sendRes + ";window=" + window + ";data=" + data);
        return sendRes;
    }


    /**
     * <--连接设备获取-->
     * 获取设备账户信息
     */
    public static boolean getStreamUserInfo(int window) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_REMOTE_ACCOUNT,
                0, JVNetConst.SRC_EX_REMOTE_REFRESH, null, 0);
        Log.e(TAG, "getStreamUserInfo=" + sendRes + ";window=" + window);
        return sendRes;
    }



    /**
     * <--连接设备获取-->
     * 修改设备账户信息 管理员密码
     */
    public static boolean modifyStreamUserInfo(int window, String userName, String userPwd) {
        String sendData = String.format(Locale.CHINA, AppConsts.REMOTE_USER_INFO, userName, userPwd);

        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_REMOTE_ACCOUNT,
                0, JVNetConst.SRC_EX_REMOTE_MODIFY, sendData.getBytes(), sendData.length());
        Log.e(TAG, "modifyStreamUserInfo=" + sendRes + ";window=" + window + "; data=" + sendData);
        return sendRes;
    }




    /**
     * <--连接设备获取-->
     * V8(全转发)
     * 设置报警推送开关
     */
    public static boolean setStreamAlarmEnable(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_ALARM_ENABLE, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmEnable=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }

    /**
     * <--连接设备获取-->
     * V8(全转发)
     * 设置移动侦测开关
     */
    public static boolean setStreamAlarmMdetectSwicth(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_MDETECT_SWITCH, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmMdetectSwicth=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }


    /**
     * <--连接设备获取-->
     * V8(全转发)
     * 设置移动侦测开关
     */
    public static boolean setStreamAlarmSound(int window, String data) {
        boolean sendRes = Jni.sovSendData(window, JVNetConst.SRC_INTELLIGENCE,
                0, JVNetConst.SRC_EX_INTELLIGENCE_ALARM_SOUND, data.getBytes(), data.length());
        Log.e(TAG, "setStreamAlarmSound=" + sendRes + ";window=" + window + ";data:" + data);
        return sendRes;
    }




    /**
     * 获取流媒体IPC(V8)文件列表
     *
     * @param fileStr
     * @param dateStr
     * @return
     */
    public static ArrayList<StreamFile> getStreamIPCFileList(String fileStr, String dateStr) {
        ArrayList<StreamFile> dataList = new ArrayList<StreamFile>();

        try {
            dateStr = dateStr.replace("-", "");
            if (null == fileStr || "".equalsIgnoreCase(fileStr)) {
                return dataList;
            }
            String[] fileArray = fileStr.split(";");
            for (int i = 0; i < fileArray.length; i++) {
                StreamFile cf = new StreamFile();
                String fileString = fileArray[i];
                String thumbnailKind = String.valueOf(fileString.charAt(0));//T or X
                String meidaKind = String.valueOf(fileString.charAt(1));//P or V
                char alarmKind = fileString.charAt(2);//N or A 录像类型

                cf.setName(String.format("%c%c:%c%c:%c%c",
                        fileString.charAt(5), fileString.charAt(6), fileString.charAt(7),
                        fileString.charAt(8), fileString.charAt(9), fileString.charAt(10)));

                cf.setFileDate(String.format("%c%c:%c%c:%c%c",
                        fileString.charAt(5), fileString.charAt(6), fileString.charAt(7),
                        fileString.charAt(8), fileString.charAt(9), fileString.charAt(10)));

                //文件类型
                if ("V".equalsIgnoreCase(meidaKind)) {
                    cf.setMediaKind(StreamFile.MEDIA_VIDEO);
                    cf.setFilePath(File.separator + "progs" + File.separator + "rec" + File.separator
                            + "00" + File.separator + dateStr + File.separator + String.format("%c%c%c%c%c%c%c%c%c",
                            fileString.charAt(2), fileString.charAt(3), fileString.charAt(4), fileString.charAt(5), fileString.charAt(6),
                            fileString.charAt(7), fileString.charAt(8), fileString.charAt(9),
                            fileString.charAt(10)) + AppConsts.VIDEO_MP4_KIND);
                }

                Log.e(TAG, "DOWNLOAD_FILE_,cf.filePath:"+cf.getFilePath());
                cf.setVideoKind(alarmKind);
                cf.setThumbnailPath("");

                cf.setIndex(dataList.size());
                dataList.add(cf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * 是否启用远程回放
     *
     * @param enable
     */
    public static boolean enableRemotePlay(int index, boolean enable) {
        boolean enableRes = Jni.enablePlayback(index, enable);
        Log.e(TAG, "enableRemotePlay:enable=" + enable + ";enableRes=" + enableRes);
        return enableRes;
    }


    /**
     * Cat-IPC 共用
     * 流媒体请求停止远程回放
     *
     * @param index
     * @return
     */
    public static boolean stopStreamRemotePlay(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYSTOP, null, 0);
        Log.e(TAG, "stopStreamCatRemotePlay=" + sendRes + ";window=" + index);
        return sendRes;
    }

    /**
     * Cat-IPC 共用
     * 流媒体请求开始远程回放
     *
     * @param index
     * @return
     */
    public static boolean startStreamRemotePlay(int index, String filePath) {
        String param = String.format(AppConsts.FORMATTER_STREAM_CAT_DOWNLOAD_PATH, filePath);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_REQ_PLAY, param.getBytes(), param.length());
        Log.e(TAG, "startStreamRemotePlay=" + sendRes + ";window=" + index + ";param=" + param);
        return sendRes;
    }

    /**
     * Cat-IPC 共用
     * 流媒体远程回放调节播放进度
     *
     * @param index
     * @param seekProgress
     */
    public static boolean streamSeekTo(int index, int seekProgress) {
        String param = String.format(AppConsts.FORMATTER_STREAM_CAT_SEEKPOS, seekProgress);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_PLAY,
                0, JVNetConst.SRC_EX_RP_CMD_PLAYSEEK, param.getBytes(), param.length());
        Log.e(TAG, "streamSeekTo=" + sendRes + ";seekProgress=" + seekProgress);
        return sendRes;
    }




    /**
     * Cat-IPC 共用
     * 流媒体请求下载
     *
     * @param index
     */
    public static boolean startStreamDownload(int index, String filePath) {
        String param = String.format(AppConsts.FORMATTER_STREAM_CAT_DOWNLOAD_PATH, filePath);
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_DOWNLOAD,
                0, JVNetConst.SRC_EX_RD_REQ_DOWNLOAD, param.getBytes(), param.length());
        Log.e(TAG, "startStreamDownload=" + sendRes + ";window=" + index + ";param=" + param);
        return sendRes;
    }


    /**
     * Cat-IPC 共用
     * 流媒体取消下载
     *
     * @param index
     */
    public static boolean cancelStreamDownload(int index) {
        boolean sendRes = Jni.sovSendData(index, JVNetConst.SRC_REMOTE_DOWNLOAD,
                0, JVNetConst.SRC_EX_RD_CMD_LOADSTOP, null, 0);
        Log.e(TAG, "cancelStreamDownload=" + sendRes + ";window=" + index);
        return sendRes;
    }

    /**
     * 流媒体猫眼是否传输实时视频
     *
     * @param index
     * @param enable true:启用   false:不启用
     * @return
     */
    public static boolean enableStreamCatVideoData(int index, boolean enable) {
        boolean enableRes = Jni.sovEnableRealTimeData(index, enable);
        Log.e(TAG, "enableStreamCatVideoData:enable=" + enable + ";enableRes=" + enableRes);
        return enableRes;
    }


    public static void yyy(String ystNum, String devUser, String devPwd, String key) {
        String streamKey = "StreamKey="+key;
        Jni.setAESKey(ystNum,devUser,devPwd,streamKey.getBytes(),streamKey.length());
        Log.e(TAG, "setAESKey:streamKey=" + streamKey);
    }


    /**
     * 流媒体协议Ap配置功能
     * @param devNum  设备云视通号码
     * @param params  wifi名和wifi密码
     */
    public static void sovApSet(String devNum, String params){
        Jni.sovConfigWifiByAP(devNum,"admin","", JVNetConst.SRC_NETWORK,0, JVNetConst.SRC_EX_NETWORK_AP_CONFIG,params.getBytes(),params.length());
    }

}


