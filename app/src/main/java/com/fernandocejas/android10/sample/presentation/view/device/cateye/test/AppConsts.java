package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.os.Environment;

import java.io.File;

/**
 * Created by juyang on 16/3/23.
 */
public class AppConsts {

    /***************************************
     * 路径
     *************************************************/
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    public static final String APP_PATH = SDCARD_PATH + "Qtec" + File.separator;
    public static final String LOG_PATH = APP_PATH + "CatLog" + File.separator;
    public static final String CAPTURE_PATH = APP_PATH + "QtecCapture" + File.separator;
    public static final String VIDEO_PATH = APP_PATH + "QtecVideo" + File.separator;
    public static final String DOWNLOAD_PATH = APP_PATH + "QtecDownload" + File.separator;


    public static final String IMAGE_JPG_KIND = ".jpg";
    public static final String VIDEO_MP4_KIND = ".mp4";



    //设备端文件存放路径
    public static final String DEV_VIDEO_PATH = "/rec/00/VIDEO/";
    public static final String DEV_IMAGE_PATH = "/rec/00/IMAGE/";

    /* String formatter */
    //发送声波数据格式
    public static final String SOUND_WAVE_FORMATTER = "%s;%s;";
    //远程回放数据查询格式
    public static final String REMOTE_SEARCH_FORMATTER = "%04d%02d%02d000000%04d%02d%02d000000";

    /******************************
     * 设备设置
     ********************************/
    public static final String TAG_MSG = "msg";
    public static final String TAG_FLAG = "flag";

    public static final String TAG_PACKET_TYPE = "packet_type";
    public static final String TAG_EXTEND_TYPE = "extend_type";
    public static final String TAG_EXTEND_MSG = "extend_msg";
    public static final String TAG_EXTEND_ARG1 = "extend_arg1";
    public static final String TAG_EXTEND_ARG2 = "extend_arg2";

    // 添加第三方报警设备
    public static final String TAG_ADD_THIRD_ALRAM_DEV = "addThirdAlarmDev";
    public static final int RC_GPIN_BIND_PTZ = 0x00; // 外设报警联动报警设置回调
    public static final int RC_GPIN_ADD = 0x10; // 外设报警添加
    public static final int RC_GPIN_SET = 0x11; // 外设报警设置
    public static final int RC_GPIN_SECLECT = 0x12; // 外设报警查询
    public static final int RC_GPIN_DEL = 0x13; // 外设报警查询
    public static final int RC_GPIN_SET_SWITCH = 0x14; // 外设报警设置开关(只内部使用)
    public static final int RC_GPIN_SET_SWITCH_TIMEOUT = 0x15; // 外设报警设置开关
    public static final int RC_GPIN_BIND_PTZPRE = 0X18; // 门磁关联预置点
    // 录像存储模式 0: 停止录像 1: 手动录像 2. 报警录像
    public static final String TAG_STORAGEMODE = "storageMode";
    public static final String FORMATTER_STORAGE_MODE = "storageMode=%d;";
    public static final int STORAGEMODE_NULL = 0;// 停止录像
    public static final int STORAGEMODE_NORMAL = 1;// 手动录像
    public static final int STORAGEMODE_ALARM = 2;// 报警录像
    public static final int STORAGEMODE_CHFRAME = 3;// 抽帧录像
    // 视频方向(小维之前的老协议)
    public static final String FORMATTER_EFFECT = "effect_flag=%d;";
    public static final String TAG_EFFECT = "effect_flag";
    public static final int SCREEN_NORMAL = 0;// 0(正),4(反)
    public static final int SCREEN_OVERTURN = 4;// 0(正),4(反)
    public static final int SCREEN_MIRROR = 2;// 0(正),2(镜像)
    // 视频方向(小维之后的新协议)图像反转模式：0（未反转），1（90度反转），2（180度反转），3（270度反转）
    public static final String TAG_ROTATE = "rotate";
    public static final String FORMATTER_ROTATE = "rotate=%d;";
    public static final int TAG_ROTATE_0 = 0;
    public static final int TAG_ROTATE_90 = 1;
    public static final int TAG_ROTATE_180 = 2;
    public static final int TAG_ROTATE_270 = 3;
    // 网络校时 通用开关 0：未开启， 1：已开启
    public static final String TAG_BSNTP = "bSntp";
    public static final String FORMATTER_BSNTP = "bSntp=%d;";
    // 时间格式
    public static final String TAG_TIME_FORMAT = "nTimeFormat";
    // MM/DD/YYYY
    public static final int TIME_TYPE_0 = 0;
    // YYYY-MM-DD
    public static final int TIME_TYPE_1 = 1;
    // DD/MM/YYYY
    public static final int TIME_TYPE_2 = 2;
    // 时区字段
    public static final String TIME_ZONE = "timezone";
    public static final String FORMAT_TIME_ZONE = "timezone=%d";
    public static final String FORMATTER_TIME_ZONE = "timezone=%d;bSntp=1";
    // 对讲模式：1（单向）2（双向）3（单双向）
    public static final String TAG_CHATMODE = "chatMode";
    public static final int TAG_CHATMODE_S = 1;//单向
    public static final int TAG_CHATMODE_D = 2;//双向
    public static final int TAG_CHATMODE_SAD = 3;//单双向切换
    // 对讲标志位
    public static final String TAG_MOVESPEED = "moveSpeed";
    // 设置码流
    public static final int TYPE_SET_PARAM = 0x03;
    public static final String TAG_STREAM = "MobileQuality";
    public static final String FORMATTER_CHANGE_STREAM = "MobileQuality=%d;";
    // 设置主控发不发音频
    public static final String FORMATTER_CHANGE_AUDIO_STATE = "MoRecordOrMonitor=%d;";
    // 手动录像 bRecEnable
    public static final String TAG_BRECENABLE = "bRecEnable";

    // 手动录像 bRecEnable
    public static final String FORMATTER_BRECENABLE = "bRecEnable=%d;";

    // 报警录像 bRecAlarmEnable
    public static final String TAG_BRECALARMENABLE = "bRecAlarmEnable";
    // 老家用字段MobileCH 2：家用 其他非家用
    public static final String TAG_MOBILECH = "MobileCH";
    public static final int MOBILECH_HOME = 2;
    // ModeByMicStatus插耳机变双向对讲，拔掉耳机单向对讲（但是此逻辑不用了） 1是可以，0是不行
    public static final String TAG_MODEBYMICSTATUS = "ModeByMicStatus";
    // sd卡管理 1:有SD卡 0：没有SD卡
    public static final String TAG_NSTORAGE = "nStorage";

    // new sd卡管理 1:有SD卡 0：没有SD卡
    public static final String TAG_DISK = "disk_exist";

    public static final int NSTORAGE_HAS_SDCARD = 1;
    public static final int NSTORAGE_NO_SDCARD = 0;
    // SD卡总容量
    public static final String TAG_NTOTALSIZE = "nTotalSize";
    // SD卡剩余容量
    public static final String TAG_NUSEDSIZE = "nUsedSize";
    // SD卡状态 nStatus: 0:未发现SD卡 1：未格式化 2：存储卡已满 3：录像中... 4：准备就绪
    public static final String TAG_NSTATUS = "nStatus";
    // 移动侦测灵敏度
    public static final String TAG_NMDSENSITIVITY = "nMDSensitivity";
    public static final String FORMATTER_NMDSENSITIVITY = "nMDSensitivity=%d;";
    // 设置设备语言 0:中文 1：英文
    public static final String FORMATTER_SET_DEV_LANGUAGE = "nLanguage=%d;";

    // 设备安全防护状态
    public static final String TAG_SET_DEV_SAFE_STATE = "bAlarmEnable";
    // 设备安全防护状态 通用开关 0：关闭 1：打开
    public static final String FORMATTER_SET_DEV_SAFE_STATE = "bAlarmEnable=%d;";
    // 移动侦测开关
    public static final String TAG_SET_MDENABLE_STATE = "bMDEnable";
    // 移动侦测开关 通用开关 0：关闭 1：打开
    public static final String FORMATTER_SET_MDENABLE_STATE = "bMDEnable=%d;";
    // 婴儿啼哭报警功能
    public static final String TAG_BBCENABLE = "bBCEnable";
    // 设备报警声音开关
    public static final String TAG_SET_ALARM_SOUND = "bAlarmSound";
    // 设备报警声音开关 通用开关 0：关闭 1：打开
    public static final String FORMATTER_SET_ALARM_SOUND = "bAlarmSound=%d;";
    // 邮件报警开关
    public static final String TAG_SEND_ALARM_EMAIL = "nMDOutEMail";
    // 邮件报警参数设置(手机端自定义)
    public static final String TAG_PARAM_ALARM_EMAIL = "paramAlarmEMail";
    public static final String FORMATTER_ALARM_SEND_TEST_EMAIL = "acMailSender=%s;"// 邮件发送者
        + "acSMTPServer=%s;"// 邮件服务器地址
        + "acSMTPUser=%s;"// 用户名
        + "acSMTPPasswd=%s;"// 密码
        + "acSMTPort=%d;"// 发送邮件端口
        + "acSMTPCrypto=%s;"// 邮件加密方式（none/ssl/tls）
        + "acReceiver0=%s;"// 收件人1
        + "acReceiver1=%s;"// 收件人2
        + "acReceicer2=%s;"// 收件人3
        + "acReciever3=%s;";// 收件人4
    public static final String FORMATTER_ALARM_EMAIL_SET = "alarmTime1=%s;"// 报警时间段-%s
        + "nAlarmDelay=%d;"// 报警间隔
        + "bAlarmSound=%d;"// 报警声音开关
        + "acMailSender=%s;"// 邮件发送者
        + "acSMTPServer=%s;"// 邮件服务器地址
        + "acSMTPUser=%s;"// 用户名
        + "acSMTPPasswd=%s;"// 密码
        + "vmsServerIp=%s;"// vms服务器IP地址
        + "vmsServerPort=%d;"// vms服务器端口
        + "acSMTPort=%d;"// 发送邮件端口
        + "acSMTPCrypto=%s;"// 邮件加密方式（none/ssl/tls）
        + "acReceiver0=%s;"// 收件人1
        + "acReceiver1=%s;"// 收件人2
        + "acReceicer2=%s;"// 收件人3
        + "acReciever3=%s;";// 收件人4
    // 报警时间段
    public static final String TAG_ALARM_TIME = "alarmTime1";
    // 报警间隔
    public static final String TAG_ALARM_DELAY = "nAlarmDelay";
    // vms服务器IP地址
    public static final String TAG_VMS_SERVER_IP = "vmsServerIp";
    // vms服务器端口
    public static final String TAG_VMS_SERVER_PORT = "vmsServerPort";
    // 邮件发送者
    public static final String TAG_ACMAILSENDER = "acMailSender";
    // 邮件服务器地址
    public static final String TAG_ACSMTPSERVER = "acSMTPServer";
    // 用户名
    public static final String TAG_ACSMTPUSER = "acSMTPUser";
    // 密码
    public static final String TAG_ACSMTPPASSWD = "acSMTPPasswd";
    // 发送邮件端口
    public static final String TAG_ACSMTPORT = "acSMTPPort";
    // 邮件加密方式（none/ssl/tls）
    public static final String TAG_ACSMTPCRYPTO = "acSMTPCrypto";
    // 收件人0
    public static final String TAG_ACRECEIVER0 = "acReceiver0";
    // 收件人1
    public static final String TAG_ACRECEIVER1 = "acReceiver1";
    // 收件人2
    public static final String TAG_ACRECEIVER2 = "acReceicer2";
    // 收件人3
    public static final String TAG_ACRECEIVER3 = "acReciever3";
    // 设备邮件报警开关 通用开关 0：关闭 1：打开
    public static final String FORMATTER_SEND_MAIL = "nMDOutEMail=%d;";
    // 报警时间段字段
    public static final String TAG_SET_ALARM_TIME = "alarmTime0";
    // 报警时间段设置
    public static final String FORMATTER_SET_ALARM_TIME = "alarmTime0=%s;";
    // 全天报警
    public static final String ALARM_TIME_ALL_DAY = "00:00:00-23:59:59";
    // 报警时间段时间格式化
    public static final String FORMATTER_ALARM_TIME = "%s:00-%s:00";
    // 通用开关
    public static final int SWITCH_CLOSE = 0;
    public static final int SWITCH_OPEN = 1;

    /* 日志前缀 */
    public static final String LOG_PREFIX_ACTIVITY = "xiaowei_activity_";
    public static final String LOG_PREFIX_FRAGMENT = "xiaowei_fragment_";
    /* 平台标识 */
    public static final int BIZ_ACC_ANDROID = 0x10;
    /******************************
     * 添加第三方报警设备
     ********************************/
    // 报警设备昵称设置,报警开关设置
    public static final String FORMATTER_SET_THIRD_ALARM_DEV = "type=%d;guid=%d;name=%s;enable=%d;";
    // 添加第三方报警设备
    public static final String FORMATTER_ADD_THIRD_ALARM_DEV = "type=%d;";
    // 删除第三方报警设备
    public static final String FORMATTER_DELETE_THIRD_ALARM_DEV = "type=%d;guid=%d;";


    /******************************
     * 飞行器新增
     ********************************/

    public static final String FORMATTER_DBALARM = "bEnableDBDetect=%d;"+// 分贝报警使能 0,1
        "DBThreahold=%d;"+// 绝对分贝报警门限 0~100
        "DBRealtiveThreahold=%d;"+// 相对分贝报警门限 0~100
        "bAsdEnableRecord=%d;"+// 使能报警录像 0,1
        "bAsdOutClient=%d;"+// 使能发送至分控 0,1
        "bAsdOutEMail=%d;";// 使能报警邮件 0,1

    public static final String TAG_RECFILELENGTH = "RecFileLength";
    public static final String FORMATTER_RECFILELENGTH = "RecFileLength=%d;";

    public static final String TAG_UARTBAUT = "uartbaut";
    public static final String FORMATTER_UARTBAUT = "uartbaut=%d;";

    //产品类型
    public static final String TAG_PRODUCT_TYPE = "ProductType";
    //产品版本号
    public static final String TAG_VERSION = "Version";

    public static final String FORMATTER_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMATTER_STORAGE_MODE_OLD = "storageMode=%d;";
    public static final String FORMATTER_STORAGE_MODE_NEW = "storageMode=%d;chFrameSec=%d;";
    //抽帧录像功能开关
    public static final String TAG_BRECCHFRAMEENABLE = "bRecChFrameEnable";
    //抽帧录像时间间隔
    public static final String TAG_CHFRAMESEC = "chFrameSec";

    // TODO 2017/03/03新增 V8 全转发协议下字段定义
    public static final String TAG_ALARM_ALARMTIME0SE = "alarmTime0SE";//防护时间段
    public static final String TAG_TIME_FORMATTER = "timeFormat";//时间格式
    public static final String FORMAT_ALARM_ALARMTIME0SE = "alarmTime0SE=%s";//防护时间段
    public static final String FORMAT_TIME_FORMAT = "timeFormat=%d";//设置时间格式

    // 录像存储模式 0: 停止录像 1: 手动录像 2. 报警录像 3.抽帧录像
    public static final String N_RECORD_TYPE = "nRecordType";
    public static final String FORMAT_STORAGETYPE = "nRecordType=%d";
    public static final String FORMAT_STORAGETYPE_FRAME = "nRecordType=%d;chFrameSec=%d;";

    public static final String TAG_NEFFECT_FLAG = "nEffectFlag";
    public static final String FORMAT_NEFFECT_FLAG = "nEffectFlag=%d";

    public static final String TAG_BSUPPORT_CLOUD = "bSupportXWCloud";

    /* 流媒体 修改管理员密码*/
    public static final String REMOTE_USER_INFO = "user_name=%s;user_pwd=%s;";//用户名、密码

    public static final String TAG_NPACKETTYPE = "nPacketType";//
    public static final String TAG_NCMD = "nCmd";//
    public static final String TAG_DATA = "data";//
    public static final String TAG_PARAM = "nParam";//
    public static final String TAG_RESULT = "result";//
    public static final String TAG_REASON = "reason";//

    // 对讲模式：1（单向）2（双向）3（单双向）
    public static final String TAG_STREAM_CAT_CHATMODE = "nChatMode";

    //是否支持云台
    public static final String TAG_PTZ_SUPPORT = "softptzsupport";
    //云台灵敏度
    public static final String TAG_PTZ_SPEED = "PtzSpeed";
    public static final String FORMAT_PTZ_SPEED = "PtzSpeed=%d";

    // 码流 1:高清 2:标清 3:流畅
    public static final String TAG_STREAM_CAT_MOBILEQUALITY = "nMobileQuality";

    public static final String FORMATTER_STREAM_CAT_CHANGE_STREAM = "nMobileQuality=%d;";

    public static final String TAG_STREAM_BSUPPORTTHUMBNAIL = "bSupportThumbnail";


    // 猫眼流媒体 远程回放查询播放列表时用的时间格式
    public static final String FORMATTER_STREAM_CAT_REMOTE_CHECK_DATE = "TimeRange=%04d%02d%02d000000%04d%02d%02d000000";
    public static final String FORMATTER_STREAM_CAT_DOWNLOAD_PATH = "filePath=%s;";
    public static final String FORMATTER_STREAM_CAT_SEEKPOS = "nSeekPos=%d;";


    //门铃按键灯开关
    public static final String TAG_BBELLLIGHT = "bBellLight";
    public static final String FORMATTER_BBELLLIGHT = "bBellLight=%d;";
    //休眠时间--流媒体协议
    public static final String TAG_NLCDSHOWTIME = "nLCDShowTime";
    public static final String FORMATTER_NLCDSHOWTIME = "nLCDShowTime=%d;";
    //休眠时间
    public static final String TAG_NSUSPENDTIME = "nSuspendTime";
    public static final String FORMATTER_NSUSPENDTIME = "nSuspendTime=%d;";
    //开机向导
    public static final String TAG_BSTARTGUIDE = "bStartGuide";
    public static final String FORMATTER_BSTARTGUIDE = "bStartGuide=%d;";

    //猫眼门铃是否点亮屏幕：0（不亮），1（亮）
    public static final String TAG_RINGANDLCD = "nRingAndLCD";
    public static final String FORMATTER_RINGANDLCD = "nRingAndLCD=%d;";

    //猫眼语言显示类型：0（中文），1（英文）
    public static final String TAG_LANGUAGE = "nLanguage";
    public static final String FORMATTER_LANGUAGE = "nLanguage=%d;";

    //报警文件类型：0（图片），1（视频）
    public static final String TAG_BALARMTYPE = "bAlarmType";
    public static final String FORMATTER_BALARMTYPE = "bAlarmType=%d;";
    //红外感应开关：0（关），1（开）
    public static final String TAG_BPIRENABLE = "bPirEnable";
    public static final String FORMATTER_BPIRENABLE = "bPirEnable=%d;";

    //猫眼宽动态：0（关闭），1（开启）
    public static final String TAG_WDR = "bEnableWdr";
    public static final String FORMATTER_WDR = "bEnableWdr=%d;";

    //红外感应时间：0-3s,1-10s,2-15s;
    public static final String TAG_BPIRTIME = "PirTime";
    public static final String FORMATTER_BPIRTIME = "PirTime=%d;";

    //门前报警音开关：0（关），1（开）
    public static final String TAG_BFRIENDALARMENABLE = "bFriendAlarmEnable";
    public static final String FORMATTER_BFRIENDALARMENABLE = "bFriendAlarmEnable=%d;";

    //门铃遮挡开关：0（关），1（开）
    public static final String TAG_BCOVERALARMENABLE = "bCoverEn";
    public static final String FORMATTER_BCOVERALARMENABLE = "bCoverEn=%d;";

    //重力感应开关：0（关），1（开）
    public static final String TAG_BGSENSORENABLE = "bGsensorEnable";
    public static final String FORMATTER_BGSENSORENABLE = "bGsensorEnable=%d;";
    //移动侦测开关：0（关），1（开）
    public static final String TAG_BMDETECT = "bMDetect";
    public static final String FORMATTER_BMDETECT = "bMDetect=%d;";
    //设备模式：0：高性能   1：普通   2：省电
    public static final String TAG_NSCENEMODE = "nSceneMode";
    public static final String FORMATTER_NSCENEMODE = "nSceneMode=%d;";
    //	存储分辨率	0：高清（1280x720） 1：标清（960x540） 2：普通（512x288）
    public static final String TAG_NSTORAGERESOLUTION = "nStorageResolution";
    public static final String FORMATTER_NSTORAGERESOLUTION = "nStorageResolution=%d;";
    //录像时长	3秒、6秒、9秒、12秒
    public static final String TAG_NRECORDTIME = "nRecordTime";
    public static final String FORMATTER_NRECORDTIME = "nRecordTime=%d;";
    //自动覆盖开关	0：关 1：开
    public static final String TAG_BAUTOSWITCH = "bAutoSwitch";
    public static final String FORMATTER_BAUTOSWITCH = "bAutoSwitch=%d;";

    //剩余电量
    public static final String TAG_BATTERY_VAL = "nBatteryVal";
    //客服电话
    public static final String TAG_CUSTOMERSERVICE = "CustomerService";
    //网站
    public static final String TAG_WEBSITE = "WebSite";

    public static final String FORMATTER_TIME_ZONE_STREAM = "nTimeZone=%d";

    public static final String TAG_TIME_ZONE_STREAM = "nTimeZone";

    /**************** 云视通协议，用户信息字段 ********************/
    //用户id即用户名
    public static final String TAG_ID = "ID";
    //用户权限
    public static final String TAG_POWER = "POWER";
    //用户描述
    public static final String TAG_DESCRIPT = "DESCRIPT";
    //管理员用户名
    public static final String TAG_ADMIN = "admin";
    //日期formatter
    public static final String FORMAT_DATE = "%04d-%02d-%02d";


    //流媒体设备Ap信号的start head
    public static final String SOV_AP_HEAD = "IPC-V-";
    //流媒体App配置wifi名字和密码拼接规则
    public static final String FORMATTER_AP_WIFI_SET = "wifi_ssid=%s;wifi_pwd=%s;";
}
