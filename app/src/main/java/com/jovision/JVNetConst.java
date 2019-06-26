package com.jovision;

public final class JVNetConst {

    /* 云视通协议 连接结果 */
    public static final int CONNECT_OK = 0x01;// 连接成功（全转发 云视通）
    public static final int DISCONNECT_OK = 0x02;// 断开连接成功
    public static final int NO_RECONNECT = 0x03;// 不必要重复连接
    public static final int CONNECT_FAILED = 0x04;// 连接失败
    public static final int NO_CONNECT = 0x05;// 没有连接
    public static final int ABNORMAL_DISCONNECT = 0x06;// 连接异常断开（全转发 云视通）
    public static final int SERVICE_STOP = 0x07;// 服务停止连接，连接断开
    public static final int DISCONNECT_FAILED = 0x08;// 断开连接失败

//    public static final int CCONNECTTYPE_CONNOK = 0x01;//连接成功（全转发 云视通）
//    public static final int CCONNECTTYPE_DISOK = 0x02;//断开连接成功
//    public static final int CCONNECTTYPE_RECONN = 0x03;//不必重复连接
//    public static final int CCONNECTTYPE_CONNERR = 0x04;//连接失败
//    public static final int CCONNECTTYPE_NOCONN = 0x05;//没连接
//    public static final int CCONNECTTYPE_DISCONNE = 0x06;//连接异常断开（全转发 云视通）
//    public static final int CCONNECTTYPE_SSTOP = 0x07;//服务停止，连接断开
//    public static final int CCONNECTTYPE_DISF = 0x08;//断开连接失败

    /* 云视通协议 实时监控数据类型 */
    public final static int JVN_DATA_I = 0x01;// 视频I帧
    public final static int JVN_DATA_B = 0x02;// 视频B帧
    public final static int JVN_DATA_P = 0x03;// 视频P帧
    public final static int JVN_DATA_A = 0x04;// 音频
    public final static int JVN_DATA_S = 0x05;// 帧尺寸
    public final static int JVN_DATA_OK = 0x06;// 视频帧收到确认
    public final static int JVN_DATA_DANDP = 0x07;// 下载或回放收到确认
    public final static int JVN_DATA_O = 0x08;// 其他自定义数据
    public final static int JVN_DATA_SKIP = 0x09;// 视频S帧

    /* 云视通协议 解码器startcode的数值 */
    public final static int JVN_DSC_CARD = 0x0453564A;// 板卡解码类型04版解码器
    public final static int JVN_DSC_9800CARD = 0x0953564A;// 9800板卡解码类型04版解码器
    public final static int JVN_DSC_960CARD = 0x0A53564A;// 新版标准解码器05版解码器
    public final static int JVN_DSC_DVR = 0x0553563A;// DVR的解码器类型
    public final static int JVN_DSC_952CARD = 0x0153564A;// 952
    public final static int JVN_DVR_8004CARD = 0x0253564A;// 宝宝在线
    public final static int DVR8004_STARTCODE = 0x0553564A;//
    public final static int JVSC951_STARTCODE = 0x0753564A;//
    public final static int IPC3507_STARTCODE = 0x1053564A;//
    public final static int IPC_DEC_STARTCODE = 0x1153564A;//
    public final static int JVN_NVR_STARTCODE = 0x2053564A;// nvr

    /* 云视通协议 请求类型 */
    public final static byte JVN_REQ_CHECK = (byte) 0x10;// 请求录像检索
    public final static byte JVN_REQ_DOWNLOAD = (byte) 0x20;// 请求录像下载
    public final static byte JVN_REQ_PLAY = (byte) 0x30;// 请求远程回放
    public final static byte JVN_REQ_CHAT = (byte) 0x40;// 请求语音聊天
    public final static byte JVN_REQ_TEXT = (byte) 0x50;// 请求文本聊天
    public final static byte JVN_REQ_CHECKPASS = (byte) 0x71;// 请求身份验证

    /* 云视通协议 请求返回结果类型 */
    public final static byte JVN_RSP_CHECKDATA = (byte) 0x11;// 检索结果
    public final static byte JVN_RSP_CHECKOVER = (byte) 0x12;// 检索完成
    public final static byte JVN_RSP_DOWNLOADDATA = (byte) 0x21;// 下载数据
    public final static byte JVN_RSP_DOWNLOADOVER = (byte) 0x22;// 下载数据完成
    public final static byte JVN_RSP_DOWNLOADE = (byte) 0x23;// 下载数据失败
    public final static byte JVN_RSP_PLAYDATA = (byte) 0x31;// 回放数据
    public final static byte JVN_RSP_PLAYOVER = (byte) 0x32;// 回放完成
    public final static byte JVN_RSP_PLAYE = (byte) 0x39;// 回放失败
    public final static byte JVN_RSP_CHATDATA = (byte) 0x41;// 语音数据
    public final static byte JVN_RSP_CHATACCEPT = (byte) 0x42;// 同意语音请求
    public final static byte JVN_RSP_TEXTDATA = (byte) 0x51;// 文本数据
    public final static byte JVN_RSP_TEXTACCEPT = (byte) 0x52;// 同意文本请求
    public final static byte JVN_RSP_CHECKPASST = (byte) 0x72;// 身份验证成功
    public final static byte JVN_RSP_CHECKPASSF = (byte) 0x73;// 身份验证失败
    public final static byte JVN_RSP_NOSERVER = (byte) 0x74;// 无该通道服务
    public final static byte JVN_RSP_INVALIDTYPE = (byte) 0x7A;// 连接类型无效
    public final static byte JVN_RSP_OVERLIMIT = (byte) 0x7B;// 连接超过主控允许的最大数目
    public final static byte JVN_RSP_DLTIMEOUT = (byte) 0x76;// 下载超时
    public final static byte JVN_RSP_PLTIMEOUT = (byte) 0x77;// 回放超时
    public final static byte JVN_RSP_DISCONN = (byte) 0x7C;// 断开连接确认
    public final static byte JVN_CMD_PLAYSEEK = (byte) 0x44;// 播放定位 按帧定位 需要参数：帧数(1~最大帧)

    /* 云视通协议 命令类型 Play 开头都是远程回放 */
    public final static byte JVN_CMD_DOWNLOADSTOP = (byte) 0x24;// 停止下载数据
    public final static byte JVN_CMD_PLAYUP = (byte) 0x33;// 快进
    public final static byte JVN_CMD_PLAYDOWN = (byte) 0x34;// 慢放
    public final static byte JVN_CMD_PLAYDEF = (byte) 0x35;// 原速播放
    public final static byte JVN_CMD_PLAYSTOP = (byte) 0x36;// 停止播放
    public final static byte JVN_CMD_PLAYPAUSE = (byte) 0x37;// 暂停播放
    public final static byte JVN_CMD_PLAYGOON = (byte) 0x38;// 继续播放
    public final static byte JVN_CMD_CHATSTOP = (byte) 0x43;// 停止语音聊天
    public final static byte JVN_CMD_TEXTSTOP = (byte) 0x53;// 停止文本聊天
    public final static byte JVN_CMD_YTCTRL = (byte) 0x60;// 云台控制
    public final static byte JVN_CMD_VIDEO = (byte) 0x70;// 实时监控
    public final static byte JVN_CMD_VIDEOPAUSE = (byte) 0x75;// 暂停实时监控
    public final static byte JVN_CMD_TRYTOUCH = (byte) 0x78;// 打洞包
    public final static byte JVN_CMD_FRAMETIME = (byte) 0x79;// 帧发送时间间隔(单位ms)
    public final static byte JVN_CMD_DISCONN = (byte) 0x80;// 断开连接

    /* 云视通协议 与云视通服务器的交互消息 */
    public final static byte JVN_CMD_TOUCH = (byte) 0x81;// 探测包
    public final static byte JVN_REQ_ACTIVEYSTNO = (byte) 0x82;// 主控请求激活YST号码
    public final static byte JVN_RSP_YSTNO = (byte) 0x82;// 服务器返回YST号码
    public final static byte JVN_REQ_ONLINE = (byte) 0x83;// 主控请求上线
    public final static byte JVN_RSP_ONLINE = (byte) 0x84;// 服务器返回上线令牌
    public final static byte JVN_CMD_ONLINE = (byte) 0x84;// 主控地址更新
    public final static byte JVN_CMD_OFFLINE = (byte) 0x85;// 主控下线
    public final static byte JVN_CMD_KEEP = (byte) 0x86;// 主控保活
    public final static byte JVN_REQ_CONNA = (byte) 0x87;// 分控请求主控地址
    public final static byte JVN_RSP_CONNA = (byte) 0x87;// 服务器向分控返回主控地址
    public final static byte JVN_CMD_CONNB = (byte) 0x87;// 服务器命令主控向分控穿透
    public final static byte JVN_RSP_CONNAF = (byte) 0x88;// 服务器向分控返回 主控未上线
    public final static byte JVN_CMD_RELOGIN = (byte) 0x89;// 通知主控重新登陆
    public final static byte JVN_CMD_CLEAR = (byte) 0x8A;// 通知主控下线并清除网络信息包括云视通号码
    public final static byte JVN_CMD_REGCARD = (byte) 0x8B;// 主控注册板卡信息到服务器
    public final static byte JVN_CMD_ONLINES2 = (byte) 0x8C;// 服务器命令主控向转发服务器上线/主控向转发服务器上线
    public final static byte JVN_CMD_CONNS2 = (byte) 0x8D;// 服务器命令分控向转发服务器发起连接
    public final static byte JVN_REQ_S2 = (byte) 0x8E;// 分控向服务器请求转发
    public final static byte JVN_TDATA_CONN = (byte) 0x8F;// 分控向转发服务器发起连接
    public final static byte JVN_TDATA_NORMAL = (byte) 0x90;// 分控/主控向转发服务器发送普通数据
    public final static byte JVN_CMD_CARDCHECK = (byte) 0x91;// 板卡验证
    public final static byte JVN_CMD_ONLINEEX = (byte) 0x92;// 主控地址更新扩展
    public final static byte JVN_CMD_TCPONLINES2 = (byte) 0x93;// 服务器命令主控TCP向转发服务器上线
    public final static byte JVN_CMD_ONLYI = (byte) 0x61;// 该通道只发关键帧
    public final static byte JVN_CMD_FULL = (byte) 0x62;// 该通道恢复满帧

    /* 云视通协议 视频连接方式参数 */
    public final static byte JVN_ALLSERVER = 0;// 所有服务
    public final static byte JVN_ONLYNET = 1;// 只局域网服务
    public final static byte JVN_NOTURN = 0;// 云视通方式时禁用转发
    public final static byte JVN_TRYTURN = 1;// 云视通方式时启用转发
    public final static byte JVN_ONLYTURN = 2;// 云视通方式时仅用转发

    /* 云视通协议 视频连接身份参数 */
    public final static byte TYPE_PC_UDP = 1;// 连接类型 UDP 支持UDP收发完整数据
    public final static byte TYPE_PC_TCP = 2;// 连接类型 TCP 支持TCP收发完整数据
    public final static byte TYPE_MO_TCP = 3;// 连接类型 TCP 支持TCP收发简单数据,普通视频帧等不再发送，只能采用专用接口收发数据(适用于手机监控)
    public final static byte TYPE_MO_UDP = 4;// 连接类型 TCP 支持TCP收发简单数据,普通视频帧等不再发送，只能采用专用接口收发数据(适用于手机监控)
    public final static byte TYPE_3GMO_UDP = 5;// 手机身份连接，手机使用此身份
    public final static byte TYPE_3GMOHOME_UDP = 6;// 主控cv身份连接第一码流

    /* 云视通协议 语言参数 */
    public final static byte JVN_LANGUAGE_ENGLISH = 1;
    public final static byte JVN_LANGUAGE_CHINESE = 2;

    /* 云视通协议 云台控制类型 */
    public final static byte JVN_YTCTRL_U = 1;// 上
    public final static byte JVN_YTCTRL_D = 2;// 下
    public final static byte JVN_YTCTRL_L = 3;// 左
    public final static byte JVN_YTCTRL_R = 4;// 右
    public final static byte JVN_YTCTRL_A = 5;// 自动
    public final static byte JVN_YTCTRL_GQD = 6;// 光圈大
    public final static byte JVN_YTCTRL_GQX = 7;// 光圈小
    public final static byte JVN_YTCTRL_BJD = 8;// 变焦大
    public final static byte JVN_YTCTRL_BJX = 9;// 变焦小
    public final static byte JVN_YTCTRL_BBD = 10;// 变倍大
    public final static byte JVN_YTCTRL_BBX = 11;// 变倍小
    public final static byte JVN_YTCTRL_UT = 21;// 上停止
    public final static byte JVN_YTCTRL_DT = 22;// 下停止
    public final static byte JVN_YTCTRL_LT = 23;// 左停止
    public final static byte JVN_YTCTRL_RT = 24;// 右停止
    public final static byte JVN_YTCTRL_AT = 25;// 自动停止
    public final static byte JVN_YTCTRL_GQDT = 26;// 光圈大停止
    public final static byte JVN_YTCTRL_GQXT = 27;// 光圈小停止
    public final static byte JVN_YTCTRL_BJDT = 28;// 变焦大停止
    public final static byte JVN_YTCTRL_BJXT = 29;// 变焦小停止
    public final static byte JVN_YTCTRL_BBDT = 30;// 变倍大停止
    public final static byte JVN_YTCTRL_BBXT = 31;// 变倍小停止

    /* 云视通协议 回调what值 */
    public final static byte JVN_DEVICE_UPDATE = 0;// 设备升级
    public final static byte JVN_REMOTE_SETTING = 1;// 远程配置请求
    public final static byte JVN_WIFI_INFO = 2;// AP,WIFI热点请求
    public final static byte JVN_STREAM_INFO = 3;// 码流配置请求
    public final static byte JVN_WIFI_SETTING_SUCCESS = 4;// WIFI配置成功
    public final static byte JVN_WIFI_SETTING_FAILED = 5;// WIFI配置失败
    public final static byte JVN_WIFI_IS_SETTING = 6;// 格式化成功回调
    public final static byte JVN_DEVICE_SDCARD_STATE = 7;// 获取SD卡存储容量状态 2015.5.4
    public final static byte JVN_GET_USERINFO = 20;// 获得用户名密码请求
    public final static byte JVN_RC_GETPARAM = 80;// 获取配置信息全的回调

    /* 云视通协议 设置命令 */
    public final static byte RC_GETPARAM = (byte) 0x02;
    public final static byte RC_SETPARAM = (byte) 0x03;
    public final static byte RC_SETSYSTEMTIME = (byte) 0x0A;
    public final static byte EX_STA_GET_APINFO = (byte) 0x0F;
    public final static byte EX_WIFI_AP_CONFIG = (byte) 0x0B; // 针对新AP配置方式，获取到手机端配置的AP信息，便立即返回
    public final static byte RC_EX_FlashJpeg = (byte) 0x0a;
    public final static byte EX_MD_REFRESH = (byte) 0x01;
    public final static byte EX_MD_SUBMIT = (byte) 0x02;
    public final static byte EX_MD_UPDATE = (byte) 0x03;
    public final static byte EX_STORAGE_REC = (byte) 0x02;
    public final static byte RC_EX_SNAPSHOT = (byte) 0x18;
    public final static byte RC_EX_CHECK_FILE = (byte) 0x19;
    public final static byte EX_SNAPSHOT_SET = (byte) 0x01;
    public final static byte EX_DQP_CHECK_FILE = (byte) 0x01;
    public final static byte RC_EX_MD = (byte) 0x06;
    public final static byte RC_EXTEND = (byte) 0x06;
    public final static byte EX_STORAGE_REFRESH = (byte) 0x01;
    public final static byte EX_STORAGE_REC_ON = (byte) 0x02;
    public final static byte EX_STORAGE_REC_OFF = (byte) 0x03;
    public final static byte EX_STORAGE_OK = (byte) 0x04;
    public final static byte EX_STORAGE_ERR = (byte) 0x05;
    public final static byte EX_STORAGE_FORMAT = (byte) 0x06;
    public final static byte EX_FILE_IN_SDCARD = (byte) 0x07;
    public final static byte EX_STORAGE_ACCESS = (byte) 0x09;
    public final static byte RC_EX_COMTRANS = 0x12;
    public final static byte EX_COMTRANS_SET = 0x2A;
    public final static byte EX_COMTRANS_GET = 0x2B;
    public final static int EX_MDRGN_REFRESH = 0X01;
    public final static int EX_MDRGN_UPDATE = 0X03;
    public final static int EX_SENSOR_SUBMIT = 0x02;//设置视频翻转角度
    public final static int EX_SENSOR_SAVE = 0x04;//保存视频翻转角度
    public final static int EX_SENSOR_GETPARAM = 0x05;//获取视频翻转角度
    public final static int EX_STORAGE_GETRECMODE = 0x08;//获取录像模式

    /* 云视通协议 客户定制需求参数 */
    public final static byte JVN_YTCTRL_RECSTART = 41;// 远程录像开始
    public final static byte JVN_YTCTRL_RECSTOP = 42;// 远程录像开始
    public final static byte JVN_ABFRAMERET = 35; // 帧序列中每个多少帧一个回复



    /*****************************************  流媒体相关接口  **********************************/

    /* 流媒体协议 连接结果 */
    public static final int CCONNECTTYPE_CONNOK = 1;//连接成功（全转发 云视通）
    public static final int CCONNECTTYPE_DISCONNE = 6;//连接异常断开（全转发 云视通）
    public static final int CCONNECTTYPE_NET_TIMEOUT = 9;//连接超时(全转发协议)
    public static final int CCONNECTTYPE_PWDERR = 10;//用户名密码错误(全转发协议)
    public static final int CCONNECTTYPE_UNKONW_CONNECT = 11;//没有告诉全转发服务器设备号(全转发协议)
    public static final int CCONNECTTYPE_NO_ONLINE = 12;//服务器通知手机:设备不在线(全转发协议)
    public static final int CCONNECTTYPE_OFFLINE = 13;//服务器通知手机:设备掉线了(全转发协议)
    public static final int CCONNECTTYPE_WAKEUP_DEVICE_TIMEOUT = 14;//唤醒设备超时(全转发协议)
    public static final int CCONNECTTYPE_DEVICE_REJECT_CONNECT = 15;//设备拒绝连接(全转发协议)


    /* 流媒体协议 设置协议 gSRemoteCfg->nCmd */
    public final static int SRC_DATA_CUSTOM = 0x01;
    public final static int SRC_PARAM_ALL = 0x02;//配置信息全
    public final static int SRC_TIME = 0x03;//时间
    public final static int SRC_CHAT = 0x04;//对讲命令
    public final static int SRC_FIRMUP = 0x05;//设备升级
    public final static int SRC_STORAGE = 0x06;//存储管理
    public final static int SRC_DISPLAY = 0x07;//显示设置
    public final static int SRC_INTELLIGENCE = 0x08;//智能设置
    public final static int SRC_ABOUTEYE = 0x09;//关于猫眼
    public final static int SRC_REMOTE_PLAY = 0x10;//远程回放
    public final static int SRC_REMOTE_DOWNLOAD = 0x11;//远程下载
    public final static int SRC_REMOTE_CHECK = 0x12;//远程查询
    public final static int SRC_REMOTE_CMD = 0x13;//远程命令
    public static final int SRC_NETWORK = 0x14;//网络管理
    public static final int SRC_REMOTE_ACCOUNT = 0x15;//用户设置
    public final static int SRC_PTZ = 0x16;//云台

    /* 流媒体协议 设置协议 gSRemotecfg->packet.nPacketType */
    public final static byte SRC_EX_GETPARAM = 0x01;
    public final static byte SRC_EX_SETPARAM = 0x02;
    public final static byte SRC_EX_GETSYSTIME = 0x01;
    public final static byte SRC_EX_SETSYSTIME = 0x02;
    public final static byte SRC_EX_SETTIME_ZONE = 0x03;
    public final static byte SRC_EX_SETTIME_FORMAT = 0x04;
    public final static byte SRC_EX_SETTIME_SNTP = 0x05;
    public final static byte SRC_EX_START_CHAT = 0x01;
    public final static byte SRC_EX_STOP_CHAT = 0x02;
    public final static int EX_ABOUT_REBOOT = 0x02;//重启设备
    public final static int EX_ABOUT_FORMAT = 0x03;//恢复出厂
    public final static byte SRC_EX_FIRMUP_UPDINFO_REQ = 0x11;
    public final static byte SRC_EX_FIRMUP_REQ = 0x01;
    public final static byte SRC_EX_UPLOAD_START = 0x01;
    public final static byte SRC_EX_UPLOAD_START_STREAM = 0x02;
    public final static byte SRC_EX_UPLOAD_DATA = 0x04;
    public final static byte SRC_EX_UPLOAD_CANCEL = 0x02;
    public final static byte SRC_EX_UPLOAD_CANCEL_STREAM = 0x03;
    public final static byte SRC_EX_UPLOAD_PROGRESS = 0x04;
    public final static byte SRC_EX_UPLOAD_OK = 0x03;
    public final static byte SRC_EX_FIRMUP_OK= 0x05;
    public final static byte SRC_EX_FIRMUP_START= 0x06;
    public final static byte SRC_EX_CMD_GET_STORAGE = 0x05;//获取设备存储状况，20170111添加
    public final static byte SRC_PTZ_UP = 0x01;//云台 上
    public final static byte SRC_PTZ_DOWN = 0x02;//云台 下
    public final static byte SRC_PTZ_LEFT = 0x03;//云台 左
    public final static byte SRC_PTZ_RIGHT = 0x04;//云台 右
    public final static byte SRC_PTZ_STOP = 0x05;//云台 停止
    public final static byte SRC_EX_FIRMUP_STEP = 0x06;
    public final static byte SRC_EX_FIRMUP_REBOOT = (byte) 0xa0;
    public final static byte SRC_EX_FIRMUP_RESTORE = (byte) 0xa1;
    public final static byte SRC_EX_MT_ONEKEY_DIAG = 0x30;
    public final static byte SRC_EX_FIRMUP_RET = 0x08;
    public final static byte SRC_EX_STORAGE_REFRESH = 0x01;
    public final static byte SRC_EX_STORAGE_RESOLUTION = 0x10;
    public final static byte SRC_EX_STORAGE_RECORDTIME = 0x11;
    public final static byte SRC_EX_STORAGE_AUTOSWITCH = 0x12;
    public final static byte SRC_EX_STORAGE_RESOLUTION_STREAM = 0x02;
    public final static byte SRC_EX_STORAGE_RECORDTIME_STREAM = 0x03;
    public final static byte SRC_EX_STORAGE_AUTOSWITCH_STREAM = 0x04;
    public final static byte SRC_EX_STORAGE_GETRECMODE = 0x08;
    public final static byte SRC_EX_STORAGE_REC = 0x02;
    public final static byte SRC_EX_STORAGE_FORMAT = 0x06;
    public final static byte SRC_EX_STORAGE_SWITCH = 0x07;
    public final static byte SRC_EX_DISPLAY_BELLLIGHT = 0x02;
    public final static byte SRC_EX_INTELLIGENCE_ALARMTYPE = 0x02;
    public final static byte SRC_EX_INTELLIGENCE_PIR = 0x03;
    public final static byte SRC_EX_INTELLIGENCE_GSENSOR = 0x04;
    public final static byte SRC_EX_INTELLIGENCE_MDETECT = 0x05;
    public final static byte SRC_EX_INTELLIGENCE_REFRESH = 0x01;
    public final static byte SRC_EX_ABOUT_REBOOT = 0x02;
    public final static byte SRC_EX_ABOUT_FORMAT = 0x03;
    public final static byte SRC_EX_ABOUT_SHUTDOWN = 0x04;
    public final static byte SRC_EX_ABOUT_REFRESH = 0x01;
    public final static byte SRC_EX_CHECK_VIDEO = 0x01;
    public final static byte SRC_EX_CHECK_PCITURE = 0x02;
    public final static byte SRC_EX_CMD_RESOLUTION = 0x01;//更改实时码流
    public final static byte SRC_EX_CMD_MANUAL_ALARM_START = 0x02;//开始手动（主动）报警
    public final static byte SRC_EX_CMD_MANUAL_ALARM_STOP = 0x03;//停止手动（主动）报警
    public final static byte SRC_EX_RP_REQ_PLAY = 0x01;
    public final static byte SRC_EX_RP_CHECK_FILE = 0x02;
    public final static byte SRC_EX_RP_CMD_PLAYUP = 0x03;
    public final static byte SRC_EX_RP_CMD_PLAYDOWN = 0x04;
    public final static byte SRC_EX_RP_CMD_PLAYDEF = 0x05;
    public final static byte SRC_EX_RP_CMD_PLAYSTOP = 0x06;
    public final static byte SRC_EX_RP_CMD_PLAYPAUSE = 0x07;
    public final static byte SRC_EX_RP_CMD_PLAYGOON = 0x08;
    public final static byte SRC_EX_RP_CMD_PLAYSEEK = 0x09;
    public final static byte SRC_EX_RD_REQ_DOWNLOAD = 0x01;
    public final static byte SRC_EX_RD_CMD_LOADGOON = 0x02;
    public final static byte SRC_EX_RD_CMD_LOADPAUSE = 0x03;
    public final static byte SRC_EX_RD_CMD_LOADSTOP = 0x04;
    public final static byte SRC_EX_RD_CMD_UPLOADBREAK = 0x05;
    public final static byte SRC_EX_DISPLAY_SUSPENDTIME = 0x03;
    public final static byte SRC_EX_DISPLAY_RINGANDLCD = 0x05;
    public final static byte SRC_EX_DISPLAY_LANGUAGE = 0x06;
    public final static byte SRC_EX_DISPLAY_WDR = 0x07;//宽动态 2017/10/17
    public final static byte SRC_EX_INTELLIGENCE_COVER_ALARM = 0x0A;//人体感应模式 2018.2.9
    public final static byte SRC_EX_CMD_GET_IP = 0x04;//获取设备IP地址，20170111添加
    public final static byte SRC_EX_INTELLIGENCE_FRIEND_ALARM = 0x08;
    public final static byte SRC_EX_INTELLIGENCE_PIR_TIME = 0x09;//红外感应时间
    public final static byte SRC_EX_INTELLIGENCE_MDETECT_SENS = 0x08;//移动侦测灵敏度
    public final static byte SRC_EX_INTELLIGENCE_ALARM_ENABLE = 0x09;//报警开关
    public final static byte SRC_EX_INTELLIGENCE_ALARM_SOUND = 0x0A;//报警声音开关
    public final static byte SRC_EX_INTELLIGENCE_ALARM_TIME = 0x0B;//安全防护时间段
    public final static byte SRC_EX_INTELLIGENCE_EFFECT_FLAG = 0x0C;//视频翻转控制
    public final static byte SRC_EX_INTELLIGENCE_LDC = 0x0D;//LDC 畸变校正 状态设置
    public final static byte SRC_EX_INTELLIGENCE_MDETECT_SWITCH = 0x05;//移动侦测开关
    public final static byte SRC_EX_STORAGE_CMD_FORMAT = 0x05;//流媒体协议格式化SD卡
    public final static byte SRC_EX_STORAGE_RECORDMODE = 0x06;//流媒体协议录像类型
    public final static byte SRC_EX_STORAGE_RECORDMODE_CHFRAME_TIME = 0x07;//抽帧录像时间
    public static final byte SRC_EX_REMOTE_REFRESH = 0x01;//获取用户信息
    public static final byte SRC_EX_REMOTE_ADD = 0x02;//添加用户
    public static final byte SRC_EX_REMOTE_DEL = 0x03;//删除用户
    public static final byte SRC_EX_REMOTE_MODIFY = 0x04;//修改用户信息
    public static final byte SRC_EX_NETWORK_AP_CONFIG = 0x03;//AP配置
    public final static byte SRC_EX_INTELLIGENCE_NIGHT_MODE = 0x0E;//






    //流媒体下载回调what = CALL_DOWNLOAD = 0xA6;,arg2定义如下
    public static final int DT_FILE_HEAD = 6;
    public static final int DT_FILE_DATA = 7;
    public static final int DT_FILE_TAIL = 8;


    //回调what，包括云视通流媒体
    public static final int CALL_CATEYE_CONNECTED = 0xD3;//流媒体 视频连接回调
    public static final int CALL_CATEYE_SENDDATA = 0xD6;//流媒体控制命令协议回调
    public static final int CALL_CATEYE_PLAY_END = 0xDB;//猫眼流媒体远程回放播放结束回调
    public static final int CALL_CATEYE_PLAY = 0xDA;//猫眼流媒体 远程回放播放数据回调
    public static final int CALL_LOCAL_LAN_DEVICE = 0xAE;// 云视通纯局域网广播回调
    public static final int CALL_LAN_SEARCH_SERVER = 0xA8;// 云视通全网段广播回调
    public static final int CALL_CATEYE_SEARCH_DEVICE = 0xD9;//流媒体广播回调
    public final static int REMOTE_PLAY_CMD_OVER = 0x32;// 流媒体远程回放结束
    public final static int REMOTE_PLAY_CMD_PLAYERROR = 0x39;// 流媒体远程回放出错
    public final static int REMOTE_PLAY_CMD_PLTIMEOUT = 0x77;// 流媒体远程回放超时
    public static final int CALL_CONNECT_CHANGE = 0xA1;//云视通 视频连接回调
    public static final int CALL_NORMAL_DATA = 0xA2;
    public static final int CALL_CHECK_RESULT = 0xA3;
    public static final int CALL_CHAT_DATA = 0xA4;
    public static final int CALL_TEXT_DATA = 0xA5;
    public static final int CALL_DOWNLOAD = 0xA6;
    public static final int CALL_PLAY_DATA = 0xA7;
    public static final int CALL_LAN_SEARCH = 0xA8;
    public static final int CALL_NEW_PICTURE = 0xA9;//I帧
    public static final int CALL_STAT_REPORT = 0xAA;
    public static final int CALL_GOT_SCREENSHOT = 0xAB;
    public static final int CALL_PLAY_DOOMED = 0xAC;
    public static final int CALL_PLAY_AUDIO = 0xAD;
    public static final int CALL_QUERY_DEVICE = 0xAE;
    public static final int CALL_HDEC_TYPE = 0xAF;
    public static final int DEVICE_TYPE_UNKOWN = -1;
    public static final int DEVICE_TYPE_DVR = 0x01;
    public static final int DEVICE_TYPE_950 = 0x02;
    public static final int DEVICE_TYPE_951 = 0x03;
    public static final int DEVICE_TYPE_IPC = 0x04;
    public static final int DEVICE_TYPE_NVR = 0x05;
    public static final int JAE_ENCODER_SAMR = 0x00;
    public static final int JAE_ENCODER_ALAW = 0x01;
    public static final int JAE_ENCODER_ULAW = 0x02;
    public static final int TEXT_REMOTE_CONFIG = 0x01;
    public static final int TEXT_AP = 0x02;
    public static final int TEXT_GET_STREAM = 0x03;
    public static final int FLAG_WIFI_CONFIG = 0x01;
    public static final int FLAG_WIFI_AP = 0x02;
    public static final int FLAG_BPS_CONFIG = 0x03;
    public static final int FLAG_CONFIG_SCCUESS = 0x04;
    public static final int FLAG_CONFIG_FAILED = 0x05;
    public static final int FLAG_CONFIG_ING = 0x06;
    public static final int FLAG_SET_PARAM = 0x07;
    public static final int FLAG_GPIN_ADD = 0x10;
    public static final int FLAG_GPIN_SET = 0x11;
    public static final int FLAG_GPIN_SELECT = 0x12;
    public static final int FLAG_GPIN_DEL = 0x13;
    public static final int EX_WIFI_CONFIG = 0x0A;
    public static final int ARG1_PLAY_BAD = 0x01;
    public static final int DOWNLOAD_REQUEST = 0x20;
    public static final int DOWNLOAD_START = 0x21;
    public static final int DOWNLOAD_FINISHED = 0x22;
    public static final int DOWNLOAD_ERROR = 0x23;
    public static final int DOWNLOAD_STOP = 0x24;
    public static final int DOWNLOAD_TIMEOUT = 0x76;
    public static final int BAD_STATUS_NOOP = 0x00;
    public static final int BAD_STATUS_OMX = 0x01;
    public static final int BAD_STATUS_FFMPEG = 0x02;
    public static final int BAD_STATUS_OPENGL = 0x03;
    public static final int BAD_STATUS_AUDIO = 0x04;
    public static final int BAD_STATUS_DECODE = 0x05;
    public static final int PLAYBACK_DONE = 0x06;
    public static final int HDEC_BUFFERING = 0x07;
    public static final int BAD_SCREENSHOT_NOOP = 0x00;
    public static final int BAD_SCREENSHOT_INIT = 0x01;
    public static final int BAD_SCREENSHOT_CONV = 0x02;
    public static final int BAD_SCREENSHOT_OPEN = 0x03;


    // 远程回放播放失败的回调
    public static final int ARG2_REMOTE_PLAY_OVER = 0x32;
    public static final int ARG2_REMOTE_PLAY_ERROR = 0x39;
    public static final int ARG2_REMOTE_PLAY_TIMEOUT = 0x77;


    // 扩展类型，用于指定哪个模块去处理,lck20120206
    public static final int RC_EX_FIRMUP = 0x01;
    public static final int RC_EX_NETWORK = 0x02;
    public static final int RC_EX_STORAGE = 0x03;
    public static final int RC_EX_ACCOUNT = 0x04;
    public static final int RC_EX_COVERRGN = 0x05;
    public static final int RC_EX_MDRGN = 0X06;
    public static final int EX_MDRGN_SUBMIT = 0x02;
    public static final int RC_EX_ALARM = 0x07;
    public static final int RC_EX_SENSOR = 0x08;
    public static final int RC_EX_PTZ = 0x09;
    public static final int RC_EX_AUDIO = 0x0a;
    public static final int RC_EX_ALARMIN = 0x0b;
    public static final int RC_EX_REGISTER = 0x0c;
    public static final int RC_EX_ROI = 0x0d;
    public static final int RC_EX_QRCODE = 0x0e;
    public static final int RC_EX_IVP = 0x0f;
    public static final int EX_IVP_ASD_SUBMIT = 0x22;
    // 升级过程中用到的宏定义：
    // 系统升级指令,lck20120207
    public static final int EX_UPLOAD_START = 0x01;
    public static final int EX_UPLOAD_CANCEL = 0x02;
    public static final int EX_UPLOAD_OK = 0x03;
    public static final int EX_UPLOAD_DATA = 0x04;
    public static final int EX_FIRMUP_START = 0x05;
    public static final int EX_FIRMUP_STEP = 0x06;
    public static final int EX_FIRMUP_OK = 0x07;
    public static final int EX_FIRMUP_RET = 0x08;
    public static final int EX_FIRMUP_REBOOT = 0xA0;// 设备重启
    public static final int EX_FIRMUP_RESTORE = 0xA1;// 设备重置
    public static final int EX_FIRMUP_UPDINFO_REQ = 0x11; // 获取升级所需信息
    public static final int EX_FIRMUP_UPDINFO_RESP = 0x21;
    // 升级结果定义
    public static final int FIRMUP_SUCCESS = 0x01;
    public static final int FIRMUP_FAILED = 0x02;
    public static final int FIRMUP_LATEST = 0x03;
    public static final int FIRMUP_INVALID = 0x04;
    public static final int FIRMUP_ERROR = 0x05;
    public static final int FIRMUP_NOTFIT = 0x06;
    // 升级方法
    public static final int FIRMUP_HTTP = 0x00;
    public static final int FIRMUP_FILE = 0x01;
    public static final int FIRMUP_FTP = 0x02;// 已废弃
    /*********************************
     * 　以下修改设备用户名密码需要的宏定义
     ***************************************/
    public static final int SECRET_KEY = 0x1053564A;
    public static final int MAX_ACCOUNT = 13;
    public static final int SIZE_ID = 20;
    public static final int SIZE_PW = 20;
    public static final int SIZE_DESCRIPT = 32;
    // 用户组定义
    public static final int POWER_GUEST = 0x0001;
    public static final int POWER_USER = 0x0002;
    public static final int POWER_ADMIN = 0x0004;
    public static final int POWER_FIXED = 0x0010;
    // 帐户管理指令,lck20120308
    public static final int EX_ACCOUNT_OK = 0x01;
    public static final int EX_ACCOUNT_ERR = 0x02;
    public static final int EX_ACCOUNT_REFRESH = 0x03;
    public static final int EX_ACCOUNT_ADD = 0x04;
    public static final int EX_ACCOUNT_DEL = 0x05;
    public static final int EX_ACCOUNT_MODIFY = 0x06;
    // 操作状态
    public static final int ERR_OK = 0x0; // 修改成功 
    public static final int ERR_EXISTED = 0x1; // 用户已存在 
    public static final int ERR_LIMITED = 0x2; // 用户太多，超出了限制 
    public static final int ERR_NOTEXIST = 0x3; // 指定的用户不存在
    public static final int ERR_PASSWD = 0x4; // 密码错误
    public static final int ERR_PERMISION_DENIED = 0x5;// 无权限
    public static final int TYPE_EX_UPDATE = 0x01;
    public static final int TYPE_EX_SENSOR = 0x02;
    public static final int TYPE_EX_STORAGE_SWITCH = 0x07;
    public static final int TYPE_EX_SET_DHCP = 0x09;
    public static final int COUNT_EX_UPDATE = 0x01;
    public static final int COUNT_EX_NETWORK = 0x02;
    public static final int COUNT_EX_STORAGE = 0x03;
    public static final int COUNT_EX_SENSOR = 0x08;
    /* MTU设置 */
    public static final int MTU_700 = 700;
    public static final int MTU_1400 = 1400;// 路由器可以设置的最大值是1472

    /* 新播放库协议 */
    public static final int CONNECT_BY_CLOUD = 0x01;//云视通连接  0x01
    public static final int CONNECT_BY_SOV = 0x02;//全转发连接  0x02
}
