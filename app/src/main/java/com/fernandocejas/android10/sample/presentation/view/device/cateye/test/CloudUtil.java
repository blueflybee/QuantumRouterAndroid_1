package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.util.Log;
import android.view.Surface;

import com.jovision.JVNetConst;
import com.jovision.Jni;
import com.jovision.JniUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 云视通协议相关JNI接口
 * Created by juyang on 17/6/23.
 */

public class CloudUtil {

    private static final String TAG = "CloudUtil";
    private static byte[] acFLBuffer = new byte[2048];


    /**
     * 1.开启广播
     *
     * @param localIp
     * @return
     */
    public static int startLanSearchServer(String localIp) {
        Log.v(TAG, "startSearchLan-开启广播");
        return Jni.startLanSearchServer(9400, 6666, localIp, 1);
    }

    /**
     * 2.停止广播
     */
    public static void stopLanSearchServer() {
        Log.v(TAG, "stopSearchLan-停止广播");
        Jni.stopLanSearchServer();
    }


    /**
     * 3.全网段广播 广播回调：AppConsts.CALL_LAN_SEARCH
     */
    public static void searchAllLanDev() {
        Jni.searchAllLanDevice("", 0, 0, 0, "", 2000, 2);
        Log.v(TAG, "searchAllLanDev-全网段广播");
    }

    /**
     * 4.纯局域网广播 广播回调：AppConsts.CALL_LAN_SEARCH
     */
    public static void searchLanDev() {
        Jni.searchLocalLanDevice(2000);
        Log.v(TAG, "searchLanDev-纯局域网广播");
    }


    /**
     * 1.正常视频连接，需要显示视频
     *
     * @param channel
     * @param surface
     * @param scenePath
     * @return
     */
    public static int connect(Channel channel, Surface surface,
                              String scenePath) {
        int result = -1;

        if (null == channel) {
            return result;
        }
        Device device = channel.getParent();
        if (null != device && null != channel) {
            result = Jni.connect(channel.getIndex(), 1, channel.getChannel(),
                    device.getIp(), device.getPort(), device.getUser(),
                    device.getPwd(), device.getNo(), device.getGid(),
                    JVNetConst.JVN_TRYTURN, JVNetConst.TYPE_3GMO_UDP, surface,
                    false, scenePath, 1, JVNetConst.CONNECT_BY_CLOUD,true,"",2);
            Log.e(TAG, "connected By Yst:devNum=" + device.getFullNo()
                    + ";user=" + device.getUser()
                    + ";pwd=" + device.getPwd()
                    + ";group=" + device.getGid()
                    + ";num=" + device.getNo() + ";channel=" + channel.getChannel());
        } else {
            Log.e(TAG, "connected error,device or channel is null");
        }
        return result;
    }

    /**
     * 2.断开指定窗口视频
     *
     * @param index
     * @return
     */
    public static boolean disconnect(int index) {
        return Jni.disConnect(index);
    }


    /****************************************     对讲      ***********************************/

    /**
     * 开始语音对讲
     *
     * @param index
     * @param singleVoiceCall true 单向对讲 false 双向对讲
     * @return
     */
    public static boolean startVoiceCall(int index, boolean singleVoiceCall) {
        JniUtil.resetAecDenoise(index, singleVoiceCall ? false : true, true);
        return Jni.sendBytes(index, JVNetConst.JVN_REQ_CHAT, new byte[0], 8);
    }

    /**
     * 停止语音对讲
     */
    public static boolean stopVoiceCall(int index) {
        return Jni.sendBytes(index, JVNetConst.JVN_CMD_CHATSTOP, new byte[0], 8);
    }


    /****************************************     切换码流      ***********************************/

    /**
     * 切换码流（依赖文本聊天）
     *
     * @param index
     * @param stream 1:高清   2:标清   3:流畅
     */
    public static boolean changeStream(int index, int stream) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                AppConsts.TYPE_SET_PARAM, String.format(AppConsts.FORMATTER_CHANGE_STREAM, stream));
    }


    /****************************************     云台      ***********************************/

    /**
     * 云台自动巡航命令
     *
     * @param index
     * @param cmd
     * @param stop
     * @param speed 3-255
     */
    public static void sendCtrlCMDAuto(final int index, final int cmd,
                                       final boolean stop, final int speed) {

        new Thread() {
            @Override
            public void run() {
                byte[] data = new byte[4];
                data[0] = (byte) cmd;
                data[1] = (byte) 0;
                data[2] = (byte) 0;
                data[3] = (byte) speed;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data, 4);
                if (cmd == JVNetConst.JVN_YTCTRL_A)
                    return;

                byte[] data1 = new byte[4];
                data1[0] = (byte) (cmd + 20);
                data1[1] = (byte) 0;
                data1[2] = (byte) 0;
                data1[3] = (byte) speed;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data1, 4);
            }
        }.start();

    }


    /**
     * 长按给云台发命令
     *
     * @param index
     * @param cmd
     * @param stop
     * @param speed 3-255
     */
    public static void sendCtrlCMDLongPush(final int index, final int cmd,
                                           final boolean stop, final int speed) {
        new Thread() {
            @Override
            public void run() {
                byte[] data = new byte[4];
                data[0] = (byte) cmd;
                data[1] = (byte) 0;
                data[2] = (byte) 0;
                data[3] = (byte) speed;
                // 云台命令
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data, 4);
                if (stop)
                    return;
                // 如果不是自动命令 发完云台命令接着发一条停止
                byte[] data1 = new byte[4];
                data1[0] = (byte) (cmd + 20);
                data1[1] = (byte) 0;
                data1[2] = (byte) 0;
                data1[3] = (byte) speed;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data1, 4);
            }
        }.start();

    }


    /****************************** 云视通协议：获取设备所有配置信息 **********************************/


    /**
     * 请求文本聊天（文本聊天按通道发请求）
     *
     * @param index
     * @return
     */
    public static boolean requestTextChat(int index) {
        return Jni.sendBytes(index, JVNetConst.JVN_REQ_TEXT, new byte[0], 8);
    }

    /**
     * 云视通协议配置信息（全）请求
     *
     * @param index
     * @return
     */
    public static boolean requestAllSetData(int index) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                JVNetConst.RC_GETPARAM, "");
    }


    /****************************** 云视通协议：设备设置，1.报警设置 **********************************/
    /**
     * 1.设置安全防护时间段
     *
     * @param index
     * @param startTime
     * @param endTime   回调：码流数据里面取alarmTime0判断是否成功
     */
    public static void setAlarmTime(int index, String startTime, String endTime) {
        String alarmTime = "";
        Log.e(TAG, "setAlarmTime-start time=" + startTime + ";end time=" + endTime);
        if (startTime.equals("00:00") && endTime.equals("23:59")) {
            // 全天
            alarmTime = AppConsts.ALARM_TIME_ALL_DAY;
            Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA, true,
                    JVNetConst.RC_EX_ALARM, JVNetConst.EX_MDRGN_SUBMIT, String.format(
                            AppConsts.FORMATTER_SET_ALARM_TIME,
                            alarmTime));
        } else {
            alarmTime = String.format(AppConsts.FORMATTER_ALARM_TIME, startTime,
                    endTime);
            Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA, true,
                    JVNetConst.RC_EX_ALARM, JVNetConst.EX_MDRGN_SUBMIT, String.format(
                            AppConsts.FORMATTER_SET_ALARM_TIME,
                            alarmTime));
        }
    }

    /**
     * 2.报警推送开关
     *
     * @param index
     * @param switchState 通用开关
     * @return 回调：码流数据里面返回bAlarmEnable
     */
    public static boolean setDevSafeState(int index, int switchState) {
        Log.e(TAG, "setDevSafeState-param=" + String.format(Locale.CHINA,
                AppConsts.FORMATTER_SET_DEV_SAFE_STATE, switchState));
        return Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_ALARM, JVNetConst.EX_MDRGN_SUBMIT, String.format(Locale.CHINA,
                        AppConsts.FORMATTER_SET_DEV_SAFE_STATE, switchState));
    }

    /**
     * 3.设置移动侦测开关
     *
     * @param index
     * @param switchState 通用开关
     * @return 回调：码流数据里面返回bMDEnable
     */
    public static boolean setMotionDetection(int index, int switchState) {
        Log.e(TAG, "setMotionDetection-param=" + String.format(Locale.CHINA, AppConsts.FORMATTER_SET_MDENABLE_STATE, switchState));
        return Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_MDRGN, JVNetConst.EX_MDRGN_SUBMIT,
                String.format(Locale.CHINA, AppConsts.FORMATTER_SET_MDENABLE_STATE, switchState));
    }


    /**
     * 4.设置移动侦测灵敏度
     *
     * @param index
     * @param sensitivity param = "nMDSensitivity=10"; FORMATTER_NMDSENSITIVITY
     * @return 回调：JVNetConst.JVN_MOTION_DETECT_GET_CALLBACK 17
     */

    public static boolean setMDSensitivity(int index, int sensitivity) {
        String param = String.format(Locale.CHINA, AppConsts.FORMATTER_NMDSENSITIVITY, sensitivity);
        Log.e(TAG, "setMotionDetection-param=" + param);

        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_MDRGN, JVNetConst.EX_MDRGN_SUBMIT,
                param);
    }

    /**
     * 5.设置设备报警声音开关
     *
     * @param index
     * @param switchState 通用开关
     * @return 回调：码流数据里面返回bAlarmSound
     */
    public static boolean setDevAlarmSound(int index, int switchState) {
        Log.e(TAG, "setDevAlarmSound-param=" + String.format(
                AppConsts.FORMATTER_SET_ALARM_SOUND, switchState));
        return Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_ALARM, JVNetConst.EX_MDRGN_SUBMIT, String.format(
                        AppConsts.FORMATTER_SET_ALARM_SOUND, switchState));
    }

    /****************************** 云视通协议：设备设置，2.时间时区 **********************************/
    /**
     * 1.设置网络校时开关 通用开关
     *
     * @param index
     * @param switchState 回调：码流数据返回bSntp
     */
    public static boolean setBSntp(int index, int switchState) {
        Log.e(TAG, "setBSntp-param=" + String.format(AppConsts.FORMATTER_BSNTP, switchState));
        return Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA, false,
                0, AppConsts.TYPE_SET_PARAM, String.format(AppConsts.FORMATTER_BSNTP, switchState));


    }

    /**
     * 2.设置设备时间
     *
     * @param index
     * @param timeType 回调：没有回调？
     */
    public static void setDeviceTime(int index, int timeType, String timeStr) {
        String time = timeType + ":" + timeStr;
        Log.e(TAG, "setDeviceTime-param=" + time);
        Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA, false,
                time.getBytes().length, JVNetConst.RC_SETSYSTEMTIME, 0, 0, 0,
                time.getBytes(), time.getBytes().length);
    }


    /**
     * 3.设置设备时区
     *
     * @param index
     * @param timeZone 回调：码流数据返回timezone字段判断是否设置成功
     */
    public static void setDeviceTimeZone(int index, int timeZone) {
        Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA,
                false, 0, JVNetConst.RC_SETPARAM,
                String.format(AppConsts.FORMATTER_TIME_ZONE, timeZone));
    }


    /****************************** 云视通协议：设备设置，3.系统管理 **********************************/
    /**
     * 设备重启
     *
     * @param index 无回调
     */
    public static boolean restartDev(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_FIRMUP, JVNetConst.EX_FIRMUP_REBOOT,
                JVNetConst.FIRMUP_HTTP, 0, 0, new byte[0], 0);
    }

    /**
     * 设备重置
     *
     * @param index 无回调
     */
    public static boolean resetDev(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_FIRMUP_RESTORE, JVNetConst.FIRMUP_HTTP, 0, 0,
                new byte[0], 0);
    }


    /**
     * 获取设备管理员用户信息
     *
     * @param index
     * @return
     */
    public static boolean getUserInfo(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_ACCOUNT, JVNetConst.EX_ACCOUNT_REFRESH,
                JVNetConst.POWER_ADMIN, 0, 0, new byte[0], 0);
    }

    /**
     * 修改设备的用户名密码
     *
     * @param index
     * @param userName
     * @param userPwd
     * @param descript
     * @param power    回调：case JVNetConst.JVN_GET_USERINFO: { int extend_type =
     *                 dataObj.getInt("extend_type"); if (Consts.EX_ACCOUNT_MODIFY ==
     *                 extend_type) { // --修改设备的用户名密码，只要走回调就修改成功了 } break; }
     */
    public static void modifyDevPass(int index, String userName, String userPwd, String descript,
                                     int power) {
        try {
            byte[] paramByte = new byte[JVNetConst.SIZE_ID
                    + JVNetConst.SIZE_PW + JVNetConst.SIZE_DESCRIPT];
            byte[] userNameByte = userName.getBytes();
            byte[] userPwdByte = userPwd.getBytes();
            byte[] desByte;


            if (null == descript) {
                desByte = new byte[0];
            } else {
                desByte = descript.getBytes("GBK");
            }

            // 设备端是GBK编码
            System.arraycopy(userNameByte, 0, paramByte, 0,
                    userNameByte.length);
            System.arraycopy(userPwdByte, 0, paramByte,
                    JVNetConst.SIZE_ID, userPwdByte.length);
            System.arraycopy(desByte, 0, paramByte, JVNetConst.SIZE_ID
                    + JVNetConst.SIZE_PW, desByte.length);

            Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                    true, JVNetConst.RC_EX_ACCOUNT,
                    JVNetConst.EX_ACCOUNT_MODIFY, power, 0, 0,
                    paramByte, paramByte.length);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /****************************** 云视通协议：设备设置，4.存储管理 **********************************/

    /**
     * 1.存储管理,获取设备的存储状态 回调：JVNetConst.JVN_DEVICE_SDCARD_STATE
     *
     * @param index
     * @return 回调：JVNetConst.JVN_DEVICE_SDCARD_STATE获取sd卡状态
     */

    public static boolean requestSDCardState(int index) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_STORAGE, JVNetConst.EX_STORAGE_REFRESH,
                null);


    }

    /**
     * 2.格式化存储卡
     *
     * @param index
     * @return 回调：同1
     */

    public static boolean formatSDCard(int index) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_STORAGE, JVNetConst.EX_STORAGE_FORMAT,
                null);
    }

    /**
     * 3.sd卡停止录像
     *
     * @param index
     * @return 回调：同1
     */

    public static boolean stopSDCardRecord(int index) {
        String params = "bRecEnable=0;RecFileLength=600;bRecDisconEnable=0;bRecTimingEnable=0;RecTimingStart=0;RecTimingStop=0;bRecAlarmEnable=0;";
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_STORAGE, JVNetConst.EX_STORAGE_REC,
                params);
    }


    /**
     * 4.获取录像模式
     *
     * @param index
     * @return 回调：
     */

    public static boolean requestRCMode(int index) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_STORAGE, JVNetConst.EX_STORAGE_GETRECMODE,
                null);
    }

    /**
     * 4.修改录像模式
     *
     * @param index
     * @param recMode AppConsts.STORAGEMODE_NORMAL AppConsts.STORAGEMODE_ALARM
     * @param time    抽帧间隔
     * @return 回调：码流数据里面获取storageMode
     */
    public static boolean changeSDCardRecordMode(int index, int recMode, int time) {
        String param = "";
        if (time < 0) {
            param = String.format(AppConsts.FORMATTER_STORAGE_MODE_OLD, recMode);
        } else {
            param = String.format(AppConsts.FORMATTER_STORAGE_MODE_NEW, recMode, time);
        }

        return Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.COUNT_EX_STORAGE,
                JVNetConst.TYPE_EX_STORAGE_SWITCH,
                param);
    }


    /****************************** 云视通协议：设备设置，5.版本信息 **********************************/


    /**
     * 1.设备升级之前需要先发送取消升级命令
     *
     * @param index
     * @return
     */
    public static boolean sendCancelUpdateCmd(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_FIRMUP, JVNetConst.EX_UPLOAD_CANCEL,
                JVNetConst.FIRMUP_HTTP, 0, 0, new byte[0], 0);
    }

    /**
     * 2.发送升级命令
     *
     * @param index
     * @return
     */
    public static boolean sendUpdateCmd(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_UPLOAD_START, JVNetConst.FIRMUP_HTTP,
                0, 0, null, 0);
    }

    /**
     * 3.创建计时器每隔一段时间获取下载进度：
     *
     * @param index
     * @return
     */
    public static boolean getDownloadProcess(int index) {
        return Jni.sendSuperBytes(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_UPLOAD_DATA,
                JVNetConst.FIRMUP_HTTP, 0, 0, new byte[0],
                0);
    }

    /**
     * 4.处理升级进度命令，进度为100时，表示下载完毕，并发送EX_UPLOAD_OK命令：
     *
     * @param index
     * @return
     */
    public static boolean sendUploadOKCmd(int index) {
        return Jni.sendSuperBytes(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_UPLOAD_OK,
                JVNetConst.FIRMUP_HTTP, 0, 0, new byte[0],
                0);
    }

    /**
     * 5.收到EX_UPLOAD_OK命令反馈，发送烧写命令：
     *
     * @param index
     * @return
     */
    public static boolean sendWriteCmd(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_FIRMUP_START, JVNetConst.FIRMUP_HTTP,
                0, 0, new byte[0], 0);
    }

    /**
     * 6.收到EX_FIRMUP_START命令反馈，发送获取烧写进度命令，创建计时器，一直发送获取烧写进度命令：
     *
     * @param index
     * @return
     */
    public static boolean getWriteProcess(int index) {
        return Jni.sendSuperBytes(index, JVNetConst.JVN_RSP_TEXTDATA,
                true, JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_FIRMUP_STEP, JVNetConst.FIRMUP_HTTP,
                0, 0, new byte[0], 0);
    }


    /**
     * 7.设备重启命令（烧写完成后提示用户重启设备）
     *
     * @param index
     * @return
     */
    public static boolean reBootDevice(int index) {
        return Jni.sendSuperBytes(index,
                JVNetConst.JVN_RSP_TEXTDATA, true,
                JVNetConst.RC_EX_FIRMUP,
                JVNetConst.EX_FIRMUP_REBOOT,
                JVNetConst.FIRMUP_HTTP, 0, 0,
                new byte[0], 0);

    }


    /****************************** 云视通协议：设备设置，6.其他设置 **********************************/

    /**
     * 现在使用的视频翻转(老协议)
     *
     * @param index
     * @param screenTag   当前视频正反， 0 和 4
     * @param effect_flag 码流数据获取的字段 回调：码流数据返回effect_flag
     */
    public static void rotateVideo(int index, int screenTag, int effect_flag) {
        int send = 0;
        if (AppConsts.SCREEN_NORMAL == screenTag) {
            send = 0x04 | effect_flag;
        } else if (AppConsts.SCREEN_OVERTURN == screenTag) {
            send = (~0x04) & effect_flag;
        }
        Log.v(TAG, "rotateVideo=" + String.format(AppConsts.FORMATTER_EFFECT, send));
        Jni.rotateVideo(index, JVNetConst.JVN_RSP_TEXTDATA,
                String.format(AppConsts.FORMATTER_EFFECT, send));
        Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true, JVNetConst.RC_EX_SENSOR,
                JVNetConst.EX_SENSOR_SAVE, String.format(AppConsts.FORMATTER_EFFECT, send));
    }

    /**
     * 现在使用的视频镜像(老协议)
     *
     * @param index
     * @param screenTag   当前视频正反， 0 和 2
     * @param effect_flag 码流数据获取的字段 回调：码流数据返回effect_flag
     */
    public static void mirrorVideo(int index, int screenTag, int effect_flag) {
        int send = 0;
        if (AppConsts.SCREEN_NORMAL == screenTag) {
            send = 0x02 | effect_flag;
        } else if (AppConsts.SCREEN_MIRROR == screenTag) {
            send = (~0x02) & effect_flag;
        }
        Log.v(TAG, "mirrorVideo=" + String.format(AppConsts.FORMATTER_EFFECT, send));
        Jni.rotateVideo(index, JVNetConst.JVN_RSP_TEXTDATA,
                String.format(AppConsts.FORMATTER_EFFECT, send));
        Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true, JVNetConst.RC_EX_SENSOR,
                JVNetConst.EX_SENSOR_SAVE, String.format(AppConsts.FORMATTER_EFFECT, send));
    }


//    public static boolean yyy(int index){
//        return Jni.sendString(index,
//                JVNetConst.JVN_RSP_TEXTDATA, true, 0x17,
//                3, "StreamKey=1234567890123456");
//    }
    /**
     * 现在使用的获取 视频翻转、镜像状态（新协议）
     *
     * @param index
     */
    public static boolean getCurrentVideoDirection(int index) {
        return Jni.sendString(index,
                JVNetConst.JVN_RSP_TEXTDATA, true, JVNetConst.RC_EX_SENSOR,
                JVNetConst.EX_SENSOR_GETPARAM, "");
    }

    /**
     * 根据effect_flag获取视频当前方向值
     *
     * @param effect_flag
     * @return
     */
    public static int getVideoDirection(int effect_flag) {
        int direction = -1;
        if (AppConsts.SCREEN_NORMAL == (0x04 & effect_flag)) {
            direction = AppConsts.SCREEN_NORMAL;
        } else {
            direction = AppConsts.SCREEN_OVERTURN;
        }
        Log.e(TAG, "getVideoDirection=" + direction);

        return direction;
    }


    /**
     * 根据effect_flag获取视频镜像方向值
     *
     * @param effect_flag
     * @return
     */
    public static int getVideoMirror(int effect_flag) {
        int direction = -1;
        if (AppConsts.SCREEN_NORMAL == (0x02 & effect_flag)) {
            direction = AppConsts.SCREEN_NORMAL;
        } else {
            direction = AppConsts.SCREEN_MIRROR;
        }
        Log.e(TAG, "getVideoMirror=" + direction);
        return direction;
    }


    /***************** 以下为远程回放所有功能 ***************************/

    /**
     * 播放远程文件
     *
     * @param index
     */
    public static void playRemoteFile(int index, String acBuffStr) {
        Log.v(TAG, "playRemoteFile:acBuffStr=" + acBuffStr);
        Jni.sendBytes(index,
                JVNetConst.JVN_REQ_PLAY,
                acBuffStr.getBytes(),
                acBuffStr.getBytes().length);
    }

    /**
     * 停止远程回放视频
     *
     * @param index
     */
    public static boolean stopRemoteFile(int index) {
        return Jni.sendBytes(index, JVNetConst.JVN_CMD_PLAYSTOP, new byte[0],
                0);
    }

    /**
     * 暂停远程播放
     *
     * @param index
     */
    public static boolean pausePlay(int index) {
        // 暂停视频
        return Jni.sendBytes(index, JVNetConst.JVN_CMD_PLAYPAUSE,
                new byte[0], 0);
    }

    /**
     * 继续远程播放
     *
     * @param index
     */
    public static boolean goonPlay(int index) {
        // 继续播放视频
        return Jni.sendBytes(index, JVNetConst.JVN_CMD_PLAYGOON,
                new byte[0], 0);
    }

    /**
     * 是否启用远程回放
     *
     * @param index
     * @param enable
     * @return
     */
    public static boolean enableRemotePlay(int index, boolean enable) {
        boolean enableRes = Jni.enablePlayback(index, enable);
        Log.e(TAG, "enable=" + enable + ";enableRes=" + enableRes);
        return enableRes;
    }

    /**
     * 远程回放调节播放进度
     *
     * @param index
     * @param seekProgress
     */
    public static void seekTo(int index, int seekProgress) {
        Jni.sendInteger(index, JVNetConst.JVN_CMD_PLAYSEEK,
                seekProgress);
    }

    /**
     * 远程回放开始下载
     *
     * @param index
     * @param dataByte
     * @param downFileFullName
     */
    public static void startRemoteDownload(int index, byte[] dataByte, String downFileFullName) {
        // 下载之前必须先调用此方法设置文件名
        Jni.setDownloadFileName(index, downFileFullName);
        Jni.sendBytes(index,
                JVNetConst.JVN_CMD_DOWNLOADSTOP,
                new byte[0], 8);
        Jni.sendBytes(index,
                JVNetConst.JVN_REQ_DOWNLOAD, dataByte,
                dataByte.length);
    }

    /**
     * 远程回放取消下载
     *
     * @param index
     */
    public static void cancelRemoteDownload(int index) {
        Jni.sendBytes(index,
                JVNetConst.JVN_CMD_DOWNLOADSTOP,
                new byte[0], 8);
        Jni.cancelDownload(index);
    }

    /**
     * 查询远程回放数据方法
     *
     * @param index
     * @param date  数据从回调中返回
     */
    public static void checkRemoteData(int index, String date) {
        Jni.sendBytes(index, (byte) JVNetConst.JVN_REQ_CHECK, date.getBytes(),
                date.length());
    }

    /**
     * 远程检索回调获取到码流数据list
     *
     * @param pBuffer
     * @param deviceType
     * @param channelOfChannel
     * @return
     */
    public static ArrayList<RemoteVideo> getRemoteList(byte[] pBuffer,
                                                       int deviceType, int channelOfChannel) {

        ArrayList<RemoteVideo> datalist = new ArrayList<RemoteVideo>();

        try {
            String textString1 = new String(pBuffer);
            Log.v(TAG, "getRemoteList:deviceType=" + deviceType + ";pBuffer="
                    + textString1);

            int nSize = pBuffer.length;
            // 无数据
            if (nSize == 0) {
                return datalist;
            }

            if (deviceType == 0) {
                for (int i = 0; i <= nSize - 7; i += 7) {
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%02d", channelOfChannel);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteDisk = String.format("%c", pBuffer[i]);
                    datalist.add(rv);
                }
            } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
                int nIndex = 0;
                for (int i = 0; i <= nSize - 10; i += 10) {
                    acFLBuffer[nIndex++] = pBuffer[i];// 录像所在盘
                    acFLBuffer[nIndex++] = pBuffer[i + 7];// 录像类型
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%c%c", pBuffer[i + 8],
                            pBuffer[i + 9]);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteKind = String.format("%c", pBuffer[i + 7]);
                    rv.remoteDisk = String.format("%s%d", "",
                            (pBuffer[i] - 'C') / 10 + 1);
                    datalist.add(rv);
                }

            } else if (deviceType == 2 || deviceType == 3) {
                for (int i = 0; i <= nSize - 7; i += 7) {
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%02d", channelOfChannel);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteDisk = String.format("%c", pBuffer[i]);
                    datalist.add(rv);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datalist;
    }

    /**
     * 拼接远程回放视频参数
     *
     * @param videoBean
     * @param isJFH
     * @param deviceType
     * @param year
     * @param month
     * @param day
     * @param listIndex
     * @return
     */
    public static String getPlayFileString(RemoteVideo videoBean,
                                           boolean isJFH, int deviceType, int year, int month, int day,
                                           int listIndex) {
        byte acChn[] = new byte[3];
        byte acTime[] = new byte[10];
        byte acDisk[] = new byte[2];
        String acBuffStr = "";
        if (null == videoBean) {
            return acBuffStr;
        }

        Log.v(TAG, "getPlayFileString:deviceType=" + deviceType + ";isJFH=" + isJFH);
        if (isJFH) {
            if (deviceType == 0) {

                // sprintf(acChn, "%s",videoBean.remoteChannel);
                String channelStr = String
                        .format("%s", videoBean.remoteChannel);
                Log.e("channelStr", channelStr);
                System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                        channelStr.length());

                // sprintf(acTime, "%s",videoBean.remoteDate);
                String acTimeStr = String.format("%s", videoBean.remoteDate);
                System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                        acTimeStr.length());

                // sprintf(acDisk, "%s",videoBean.remoteDisk);
                String acDiskStr = String.format("%s", videoBean.remoteDisk);
                System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                        acDiskStr.length());
                acBuffStr = String.format(
                        "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.mp4",
                        acDisk[0], year, month, day, acChn[0], acChn[1],
                        acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                        acTime[7]);

            } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
                String channelStr = String
                        .format("%s", videoBean.remoteChannel);
                System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                        channelStr.length());

                // sprintf(acTime, "%s",videoBean.remoteDate);
                String acTimeStr = String.format("%s", videoBean.remoteDate);
                System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                        acTimeStr.length());

//                acBuffStr = String.format(
//                        "./rec/%02d/%04d%02d%02d/%c%c%c%c%c%c%c%c%c.mp4",
//                        acFLBuffer[listIndex * 2] - 'C', year, month, day,
//                        acFLBuffer[listIndex * 2 + 1], acChn[0], acChn[1],
//                        acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
//                        acTime[7]);

                acBuffStr = String.format(
                        "./rec/%02d/%04d%02d%02d/%c%c%c%c%c%c%c%c%c.mp4",
                        acFLBuffer[listIndex * 2] - 'C', year, month, day,
                        acFLBuffer[listIndex * 2 + 1], acChn[0], acChn[1],
                        acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                        acTime[7]);

            }

            Log.v(TAG, "getPlayFileString:acBuffStr=" + acBuffStr);
        } else if (deviceType == 0) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());

            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());

            // sprintf(acDisk, "%s",videoBean.remoteDisk);
            String acDiskStr = String.format("%s", videoBean.remoteDisk);
            System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                    acDiskStr.length());

            acBuffStr = String.format(
                    "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.sv4",
                    acDisk[0], year, month, day, acChn[0], acChn[1], acTime[0],
                    acTime[1], acTime[3], acTime[4], acTime[6], acTime[7]);

        } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());
            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());
            acBuffStr = String.format(
                    "./rec/%02d/%04d%02d%02d/%c%c%c%c%c%c%c%c%c.sv5",
                    acFLBuffer[listIndex * 2] - 'C', year, month, day,
                    acFLBuffer[listIndex * 2 + 1], acChn[0], acChn[1],
                    acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                    acTime[7]);
            Log.v(TAG, "getPlayFileString:acBuffStr=" + acBuffStr);
        } else if (deviceType == 2 || deviceType == 3) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());

            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());

            // sprintf(acDisk, "%s",videoBean.remoteDisk);
            String acDiskStr = String.format("%s", videoBean.remoteDisk);
            System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                    acDiskStr.length());
            acBuffStr = String.format(
                    "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.sv6",
                    acDisk[0], year, month, day, acChn[0], acChn[1], acTime[0],
                    acTime[1], acTime[3], acTime[4], acTime[6], acTime[7]);
            Log.v(TAG, "getPlayFileString:acBuffStr=" + acBuffStr);

        }
        Log.v(TAG, "getPlayFileString:bytesize: " + acBuffStr.getBytes().length + ", url:"
                + acBuffStr);
        acChn = null;
        acTime = null;
        acDisk = null;

        return acBuffStr;
    }
}
