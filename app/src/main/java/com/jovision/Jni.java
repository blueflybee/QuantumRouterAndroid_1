package com.jovision;

/**
 * Created by juyang on 16/3/22.
 */
public class Jni {

    /**
     * 初始化
     *
     * @param handle   回调句柄，要传 MainApplication 的实例对象，因为回调方式是：
     *                 {@link MainApplication#onJniNotify(int, int, int, Object)}
     * @param port     本地端口(大部分国家都用9200,已知的 突尼斯，墨西哥，越南 端口用0)
     * @param logPath  日志路径(一定要有读写本地存储的权限)
     * @param mobileIP 手机当前IP，如果没有传空字符串即可，此IP用于全网段广播
     * @param nOEMID   OEM编号(0 是公司产品 1 是泰国定制，以后再有OEM直接编号累加)
     * @param countryId   国家代码
     * @return true：成功，false：失败
     *
     * @see 调用时间：App开启后调用此方法
     */
    public static native boolean init(Object handle, int port, String logPath, String mobileIP, int nOEMID, String countryId);

    /**
     * 卸载播放库（原：deinit）
     *
     * @see 调用时间：App退出时调用此方法
     */
    public static native void deInit();

    /**
     * 获取播放库和网络库版本号,为了防止库用错，建议将该版本号打印出来
     *
     * @return json: {"jni":"xx","net":"xx"}
     *
     * @see 调用时间：加载上播放库之后即可调用
     */
    public static native String getVersion();

    /**
     * 设置MTU(最大传输单元)（原：SetMTU）
     * nMtu可以设置的值只有700,1400（七百，默认1千4百）
     *
     * @param mtu
     * @return true:成功 false：失败
     * @see 调用时间：App初始化完网络库之后调用此方法，设置MTU时也需要调用此方法
     * 设置mtu的调用步骤:
     * 1、调用停止小助手函数 -> stopLinkHelper();
     * 2、设置MTU -> setMTU(int mtu);
     * 3、打开小助手函数 -> enableLinkHelper;
     * 4、把所有设备设置到小助手函数中 -> setLinkHelper;
     * 以上步骤操作后,要记住设置mut状态,下次重启软件,初始化网络sdk后 ,立即设置mtu,然后在打开小助手,设置小助手.
     */
    public static native boolean setMTU(int mtu);


    /**
     * 启用底层日志打印
     *
     * @param enable 是否启用log打印（true：启用；false：不启用）
     * @see 调用时间：调用完接口1，初始化完网络库即可调用，日志保存在初始化网络库时传递的路径里
     */
    public static native void enableLog(boolean enable);

    /**
     * 是否显示统计（原：setStat）
     *
     * @param enable true:开启统计 false：关闭统计（精简回调信息）
     * @see 调用时间：App初始化完网络库之后可以调用此接口设置视频连接是否返回统计信息
     */
    public static native void enableStatistics(boolean enable);


    /**
     * 设置缩略图信息（原：setThumb）
     *
     * @param width   缩略图宽（根据需要设置，常见352，640，960，1280）
     * @param quality 图像质量（根据需要设置，由低到高，范围 1-100）
     * @see 调用时间：App初始化完网络库之后调用此方法设置视频截图的宽度和图片质量
     */
    public static native void setThumbnailSize(int width, int quality);


    /**
     * 删除底层保存的错误日志
     *
     * @see 调用时间：调用完接口1，初始化完网络库即可调用，根据需要调用此接口，生活和CloudSEE均未调用此接口
     */
    public static native void deleteLog();


    /**
     * 清理小助手缓存（原：clearCache）
     *
     * @see 调用时间：监听到手机端网络切换后，需要删除设备的小助手，删除后需要调用此方法清除缓存
     */
    public static native void clearHelperCache();


//    /**
//     * 视频连接
//     *
//     * @param window      窗口索引，从 0 开始
//     * @param nType       新添加的连接类型,0 IP  1 号码 2 昵称 3只TCP 4 号码 + 端口
//     * @param channel     设备通道，从 1 开始
//     * @param ip          设备IP
//     * @param port        设备断开
//     * @param username    设备用户名
//     * @param password    设备密码
//     * @param cloudSeeId  设备云视通号码，去掉编组
//     * @param groupId     设备编组
//     * @param turnType    转发模式（0：云视通方式时禁用转发；1：云视通方式时启用转发；2：云视通方式时仅用转发；现在App中都用1）
//     * @param connectType 连接方式（5：手机身份连接手机码流；6：主控身份连接第一码流；现在App都用5）
//     * @param surface     显示视频的surface
//     * @param isAp        是否Ap直连（此方法暂时无用，传false即可）
//     * @param thumbName   场景图完整路径+名字+后缀
//     * @param netLibType  区分76库和80库（0:80, 1:76）
//     * @param deviceType  云视通连接  0x01  全转发连接  0x02
//     * @return 连接结果
//     * //大于0 成功
//     * //-1 失败
//     * //-2 :已连接
//     * //-3 :社备类型发送错误
//     *
//     * @see 调用时间：所有需要视频连接的地方调用此接口
//     */
//    public static native int connect(int window,
//                                     int nType,
//                                     int channel,
//                                     String ip,
//                                     int port,
//                                     String username,
//                                     String password,
//                                     int cloudSeeId,
//                                     String groupId,
//                                     int turnType,
//                                     int connectType,
//                                     Object surface,
//                                     boolean isAp,
//                                     String thumbName,
//                                     int netLibType,
//                                     int deviceType,
//                                     boolean isSupportVIFrame,//2017/11/06 added
//                                     String eid,//2017/11/06 added
//                                     int subStream//2017/11/06 added
//    );


    /**
     * 视频连接
     *
     * @param window      窗口索引，从 0 开始
     * @param nType       0优先使用TCP协议 1优先使用UDP协议  3关闭穿透    新添加的连接类型,0 IP  1 号码 2 昵称 3只TCP 4 号码 + 端口
     * @param channel     设备通道，从 1 开始
     * @param ip          设备IP
     * @param port        设备断开
     * @param username    设备用户名
     * @param password    设备密码
     * @param cloudSeeId  设备云视通号码，去掉编组
     * @param groupId     设备编组
     * @param turnType    转发模式（0：云视通方式时禁用转发；1：云视通方式时启用转发；2：云视通方式时仅用转发；现在App中都用1）
     * @param connectType 连接方式（5：手机身份连接手机码流；6：主控身份连接第一码流；现在App都用5）
     * @param surface     显示视频的surface
     * @param isAp        是否Ap直连（此方法暂时无用，传false即可）
     * @param thumbName   场景图完整路径+名字+后缀
     * @param netLibType  区分76库和80库（0:80, 1:76）
     * @param deviceType  云视通连接  0x01  全转发连接  0x02  云视通2.0视频连接  0x03
     * @return 连接结果
     * //大于0 成功
     * //-1 失败
     * //-2 :已连接
     * //-3 :社备类型发送错误
     * @see 调用时间：所有需要视频连接的地方调用此接口
     */
    public static native int connect(int window,
                                     int nType,
                                     int channel,
                                     String ip,
                                     int port,
                                     String username,
                                     String password,
                                     int cloudSeeId,
                                     String groupId,
                                     int turnType,
                                     int connectType,
                                     Object surface,
                                     boolean isAp,
                                     String thumbName,
                                     int netLibType,
                                     int deviceType,
                                     boolean supportVIFrame,
                                     String deviceId,
                                     int subStream);


    /**
     * 断开（原：disconnect）
     *
     * @param window 窗口索引（从0开始）
     * @return true：方法调用成功，false：方法调用失败
     *
     * @see 调用时间：滑屏时划回来的窗口调用此方法；onResume时调用此方法
     */
    public static native boolean disConnect(int window);


    /**
     * 暂停底层显示（数据仍然传输，但是不显示）(同时暂停音频)（原：pause）
     *
     * @param window 窗口索引（从0开始）
     * @return true：暂停成功；false：暂停失败
     *
     * @see 调用时间：滑屏时划过去的窗口调用此方法；onPause时调用此方法
     */
    public static native boolean pauseSurface(int window);

    /**
     * 恢复底层显示（将视频显示到surface上）(同时恢复音频)（原：resume）
     *
     * @param window  窗口索引（从0开始）
     * @param surface
     * @return true：恢复成功；false：恢复失败
     *
     * @see 调用时间：滑屏时划回来的窗口调用此方法；onResume时调用此方法
     */
    public static native boolean resumeSurface(int window, Object surface);


    /**
     * 暂停底层音频播放
     *
     * @param window 窗口索引
     * @return true：暂停成功；false：暂停失败
     *
     * @see 调用时间：关闭声音时需要调用此方法，暂停音频播放
     */
    public static native boolean pauseAudio(int window);

    /**
     * 恢复底层音频播放
     *
     * @param window 窗口索引
     * @return true：恢复成功；false：恢复失败
     *
     * @see 调用时间：开启监听时需要调用此方法恢复音频播放
     */
    public static native boolean resumeAudio(int window);


    /**
     * 截图，抓拍（原：screenshot）
     *
     * @param window  窗口索引
     * @param name    待保存的文件名 图片完整路径+图片名称+图片格式后缀
     * @param quality 画面质量（根据需要设置，由低到高，范围 1-100）
     * @return true：抓拍成功；false：抓拍失败
     *
     * @see 调用时间：视频连接成功后，可调用此方法抓拍
     */
    public static native boolean screenShot(int window, String name, int quality);


    /**
     * 开始录像
     *
     * @param window       窗口索引（从0开始）
     * @param path         保存的视频文件完整路径加文件后缀名.mp4
     * @param enableVideo  是否录制视频（true：录制视频；false：不录制视频）
     * @param enableAudio  是否录制音频（true：录制音频；false：不录制音频）
     * @param audioCodecID 0为arm音频格式封装 1原格式封装 接口新增参数
     * @return true：开始录像成功；false：开始录像失败
     *
     * @see 调用时间：视频连接成功后，可调用此方法录像
     */
    public static native boolean startRecord(int window, String path,
                                             boolean enableVideo, boolean enableAudio, int audioCodecID);


    /**
     * 停止录像
     *
     * @param window
     * @return true：停止成功；false：停止失败
     *
     * @see 调用时间：视频连接成功后，开启录像后可以调用此方法停止录像
     */
    public static native boolean stopRecord(int window);


    /**
     * 修改指定窗口音频标识位（监听功能），将音频数据放入音频队列，（原:enablePlayAudio）
     * true :音频入队  false：音频不入队  音频数据一直传输
     *
     * @param window 窗口索引（从0开始）
     * @param enable 是否播放 Normaldata 的音频数据（true：开启音频；false：不开启音频）
     * @return true：开启成功；false：开启失败
     *
     * @see 调用时间：视频连接成功后，开启关闭音频
     */
    public static native boolean playAudio(int window, boolean enable);


    /**
     * 开始录音接口（对讲功能，手机端录音接口）
     *
     * @param window
     * @return true：开始录音成功；false：开始录音失败
     *
     * @see 调用时间：发送开启对讲命令，收到同意对讲后，调用此方法开启手机端录音
     */
    public static native boolean recordAndsendAudioData(int window);

    /**
     * 停止录音接口（对讲功能，手机端停止录音接口）
     *
     * @param window
     * @return true：停止录音成功；false：停止录音失败
     *
     * @see 调用时间：对讲开启后，关闭对讲时调用此接口停止手机端录音
     */
    public static native boolean stopRecordAudioData(int window);


    /**
     * 默认播放器是开启aec和降噪，如不需要 调用此接口重置player 在recordAndsendAudioData之前（原:resetAecDenoise）
     *
     * @param window
     * @param isAec     是否开启回音抵消  true：开启；false：关闭
     * @param isDenoise 是否开启降噪功能  true：开启；false：关闭
     *
     * @see 调用时间：监听和单向对讲时需要开启降噪，不开启回音抵消；双向对讲时需要开启降噪和回音抵消
     */
    public static native void setAecDenoise(int window, boolean isAec, boolean isDenoise);


    /**
     * 设置显示图像的顶点坐标(坐标系原点在 Surface 左下顶点)和长宽（手势缩放视频画面，重绘视频画面用）
     *
     * @param window 窗口索引
     * @param left   图像左坐标
     * @param bottom 图像底坐标
     * @param width  图像宽
     * @param height 图像高
     * @return true：调用成功；false：调用失败
     *
     * @see 调用时间：手势缩放时需要调用此方法
     */
    public static native boolean setViewPort(int window, int left, int bottom,
                                             int width, int height);


    /**
     * 标志是否双向对讲，控制offeraudiodata，当callback是true时使用chatdata（原:enableTalkback）
     *
     * @param window
     * @param enableTalkBack true：使用对讲音频；false：使用监听音频
     * @return true：设置成功；false：设置失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：双向对讲时使用此方法控制音频来源
     */
    public static native boolean setTalkBackDataSource(int window, boolean enableTalkBack);


    /**
     * 翻转视频（老家用IPC接口，摄像机翻转）
     *
     * @param window  窗口索引
     * @param uchType 0x51
     * @param cmd     effect_flag=%d; 0(正),4(反)
     * @return true：翻转成功；false：翻转失败
     */
    public static native boolean rotateVideo(int window, byte uchType,
                                             String cmd);


    /**
     * 发送字节数据
     *
     * @param window  窗口索引（从0开始）
     * @param uchType
     * @param data
     * @param size
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：设备设置用此方法
     */
    public static native boolean sendBytes(int window, byte uchType,
                                           byte[] data, int size);


    /**
     * 发送字符串数据（设置接口命令）
     *
     * @param window   窗口索引（从0开始）
     * @param uchType  发送类型
     * @param isExtend 是否扩展消息
     * @param count    扩展包数量
     * @param type     扩展消息类型
     * @param data     数据
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：设备设置的一些设置接口，例如：切码流，获取SD卡信息
     */
    public static native boolean sendString(int window, byte uchType,
                                            boolean isExtend, int count, int type, String data);


    /**
     * 发送超级字节数组（发送字符串数据接口的升级版）
     *
     * @param window   窗口索引（从0开始）
     * @param uchType
     * @param isExtend
     * @param count
     * @param type
     * @param p1
     * @param p2
     * @param p3
     * @param data
     * @param size
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：设备设置的一些设置接口，例如：设备重置，设备重启
     */
    public static native boolean sendSuperBytes(int window, byte uchType,
                                                boolean isExtend, int count, int type, int p1, int p2, int p3,
                                                byte[] data, int size);


    /**
     * 发送原始数据
     *
     * @param window       窗口索引（从0开始）
     * @param data_type
     * @param packet_type
     * @param packet_count
     * @param extend_type
     * @param extend_p1
     * @param extend_p2
     * @param extend_p3
     * @param data
     * @param size
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：设备设置的一些设置接口
     */
    public static native boolean sendPrimaryBytes(int window, byte data_type,
                                                  int packet_type, int packet_count, int extend_type, int extend_p1,
                                                  int extend_p2, int extend_p3, byte[] data, int size);


    /**
     * 发送整数数据（仅用于远程回放进度调节一个接口）
     *
     * @param window  窗口索引（从0开始）
     * @param uchType 0x44
     * @param data    快进到的帧数
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：远程回放调节播放进度
     */
    public static native boolean sendInteger(int window, byte uchType, int data);


    /**
     * 发送命令
     *
     * @param window  窗口索引
     * @param uchType 0x61：仅发关键帧；0x62：发全帧
     * @param data
     * @param size    0
     * @return 0：发送失败  1：发送成功  2：对方不支持该命令
     *
     * @see 调用时间：此方法仅用于设置是否只发送关键帧还是全帧时使用
     */
    public static native int sendCmd(int window, byte uchType, byte[] data,
                                     int size);


    /**
     * 发送聊天命令
     *
     * @param window  窗口索引（从0开始）
     * @param uchType
     * @param size
     * @param flag
     * @return true：成功；false：失败（失败原因只有可能是窗口不对）
     *
     * @see 调用时间：视频连接成功后，文本聊天同意后，通过此文本聊天请求命令，获取一些设备信息，比如获取码流信息
     */
    public static native boolean sendTextData(int window, byte uchType,
                                              int size, int flag);


    /**
     * 开启快速链接服务（原：enableLinkHelper）
     *
     * @param typeId   enable == true 时（APP中用3）
     *                 1: 使用者是开启独立进程的云视通小助手
     *                 2: 使用者是云视通客户端，支持独立进程的云视通小助手
     *                 3: 使用者是云视通客户端，不支持独立进程的云视通小助手
     * @param maxLimit 允许最大限制（APP中使用10）
     * @return true:开启成功；false：开启失败
     *
     * @see 调用时间：APP开启后，初始化完网络库即可调用此方法开启小助手
     */
    public static native boolean startLinkHelper(int typeId,
                                                 int maxLimit);


    /**
     * 停止小助手(原：StopHelp)
     *
     * @return true:停止成功；false：停止失败
     */
    public static native boolean stopLinkHelper();


    /**
     * 获取已设置小助手的云视通列表(原：getAllDeviceStatus)
     *
     * @return json [{cno: "A361", enable: false},{no: "A362", enable: false}]
     *
     * @see 调用时间：给设备设置小助手时，需要先获取一下设备小助手状态，调用此方法可以获取小助手状态
     */
    public static native String getHelperYSTNO();


    /**
     * 给设备设置连接小助手
     *
     * @param json       [{gid: "A", no: 361, channel: 1, name: "abc", pwd:
     *                   "123"},{gid: "A", no: 362, channel: 1, name: "abc", pwd:
     *                   "123"}]
     * @param netLibType 区分76库和80库,传0,1（0:80, 1:76），CloudSEE传1
     * @return true：设置成功；false：设置失败
     *
     * @see 调用时间：获取设备列表后，给在线的设备设置小助手
     */
    public static native boolean setLinkHelper(String json, int netLibType);


    /**
     * 移除小助手(原：HelperRemove)
     *
     * @param group 编组号，编组号+nYSTNO可确定唯一设备
     * @param yst   搜索具有某云视通号码的设备，>0有效
     *
     * @see 调用时间：给设备设置小助手时，发现设备离线了，但是小助手也没起作用，就移除小助手
     */
    public static native void removeLinkHelper(String group, int yst);


    /**
     * 开启广播(原：searchLanServer)
     *
     * @param localPort  默认 9400
     * @param serverPort 默认 6666
     * @param strIP      手机当前ip
     * @param nType      0 只搜索本网段 1 启用ping 可以跨网段（App中均传1）
     * @return 1：成功  其他失败
     *
     * @see 调用时间：App开启后，初始化完网络库，可调用此方法开启广播
     */
    public static native int startLanSearchServer(int localPort, int serverPort, String strIP, int nType);

    /**
     * 停止搜索局域网服务端(原：stopSearchLanServer)
     *
     * @see 调用时间：App退出时调用此方法停止广播
     */
    public static native void stopLanSearchServer();

    /**
     * 搜索局域网设备，参考 搜索本局域网（全网段广播）(原：searchLanDevice)
     * 跨网段广播
     *
     * @param group      传空（""）即可
     * @param cloudSeeId 传0即可
     * @param cardType   传0即可
     * @param variety    传0即可
     * @param deviceName 传空（""）即可
     * @param timeout    单位是毫秒 App传2000即可
     * @param frequency  频率，传2即可
     * @return 1：成功   其他失败
     *
     * @see 调用是否成功，等回调 AppConsts.CALL_LAN_SEARCH_SERVER = 0xA8
     * @see 调用时间：三分钟广播，下拉刷新广播，添加设备获取设备通道数量时的广播
     */
    public static native int searchAllLanDevice(String group, int cloudSeeId,
                                                int cardType, int variety, String deviceName, int timeout,
                                                int frequency);

    /**
     * 查询某个设备是否被搜索出来 局域网（本网段广播）(原：queryDevice)
     *
     * @param timeout 超时时间，毫秒
     * @return 1：成功   其他失败
     *
     * @see 调用是否成功，等回调 AppConsts.CALL_LOCAL_LAN_DEVICE = 0xAE
     * @see 调用时间：声波或智联路由配置时最后一步搜索用此方法广播，仅此一处用此广播
     */
    public static native boolean searchLocalLanDevice(int timeout);


    /**
     * 修改指定窗口播放标识位，是否启用远程回放
     *
     * @param window 窗口索引（从0开始）
     * @param enable true：启用远程回放；false：不启用远程回放
     * @return true：启用成功；false：启用失败
     *
     * @see 调用时间：远程回放播放视频时，需要先调用此方法
     */
    public static native boolean enablePlayback(int window, boolean enable);


    /**
     * 取消下载，删除正在下载的文件（远程回放取消下载命令）
     *
     * @param window
     * @return true：取消成功；false：取消失败
     *
     * @see 调用时间：远程回放开始下载之前先调用此方法，因为下载不支持多人同时下载，所以需要先把别人的下载断开
     */
    public static native boolean cancelDownload(int window);


    /**
     * 设置下载文件路径+文件名+文件后缀
     *
     * @param window
     * @param fileName 文件完整路径，包括下载文件路径+文件名+文件后缀
     * @return true：取消成功；false：取消失败
     *
     * @see 调用时间：调用下载远程回放文件接口之前，需要调用此接口设置下载路径
     */
    public static native boolean setDownloadFileName(int window, String fileName);


    /**
     * 新生成声波配置语音数据 生成声波配置数据，重复多次会阻塞执行(原：genSoundConfig)
     *
     * @param data：WiFi名称和密码格式为：wifiname;password
     * @param times：声波响的次数,App中设置为3
     *
     * @see 调用时间：声波配置时使用，云视通生活App上用此接口发声波，此方法没有回调，阻塞调用，需写到线程里
     */
    public static native void genSound(String data, int times);


    /**
     * 生成声波配置语音数据 生成声波配置数据，重复多次会阻塞执行
     *
     * @param data：WiFi名称和密码（格式：wifiname;wifipass）
     * @param times：声波响的次数
     * @see 调用时间：老声波，仅限于非H号段的家用机无线配置功能
     */
    public static native void genVoice(String data, int times);


    /**
     * 手机改变网络状态时更换绑定的IP地址 2015年12月4日(原：ChangeMobileIP)
     *
     * @param newIP 手机端当前新IP
     * @return 0:原基础上绑定成功 1:重新绑定成功 2:两次IP相同 -1:失败 -2:未初始化
     *
     * @see 调用时间：监听到手机网络切换时，手机端ip更换时调用此方法设置新IP
     */
    public static native int setNewIP(String newIP);

    /**
     * 获取通道个数
     *
     * @param group      云视通号编组
     * @param cloudSeeId 云视通号码
     * @param timeout    注意：单位是秒；设置为2即可
     * @return 通道数量
     *
     * @see 调用时间：添加设备时，获取设备通道数量调用此接口。
     */
    public static native int getChannelCount(String group, int cloudSeeId,
                                             int timeout);


    /****************************************************************************
     * 猫眼设备唤醒接口
     * 名称 :JVC_StartBroadcastSelfServer
     * 功能 : 开启自定义广播服务 回调使用之前方式 类型是0xB7
     * @param localPort 本地服务端口，<0时为默认9700
     * @param serverPort 设备端服务端口，<=0时为默认9108,建议统一用默认值与服务端匹配
     * @return 返回值:TRUE/ FALSE 其他 :
     *****************************************************************************/

    public static native int startBCSelfServer(int localPort, int serverPort);


    /****************************************************************************
     * 停止猫眼唤醒接口
     * 名称 : JVC_StopBroadcastSelfServer
     * 功能 : 停止自定义广播服务
     * 参数 : 无
     * 返回值: 无
     * 其他 : 无
     *****************************************************************************/
    public static native void stopBCSelfServer();


    /****************************************************************************
     * 猫眼回调需要调用接口
     * 名称 : JVC_SendSelfDataOnceFromBC
     * 功能 : 此方法要在StartBroadcastSelfServer回调返回时调用 从自定义广播套接字发送一次UDP消息
     * 参数 :
     * [IN] pBuffer 净载数据 类型（4字节）+云视通号码（4字节）
     * [IN] nSize 净载数据长度
     * [IN] pchDeviceIP 目的IP地址
     * [IN] nLocalPort 目的端口
     * 返回值: 无
     * 其他 :
     *****************************************************************************/
    public static native void sendSelfDataOnceFromBC(byte[] buffer, int size, String ip, int port);


    /**
     * 查询猫眼是否在线(原：strMedOnline)
     *
     * @param ystNum 猫眼云视通号码
     * @return 0：成功   -1：失败
     * 成功需要等待 CALL_CATEYE_ONLINE 0xD2 回调
     * arg2 -1：与服务器通讯失败
     * 0：设备不在线
     * 1：正常
     */
    public static native int sovCheckOnlineState(String ystNum);


    /**
     * 流媒体IPC设置协议，所有设置接口均通过此方法设置，直接发送，不需要连接设备(原：strMedDirectTransmit)
     *
     * 2017/03/10 添加
     *
     * @param ystNo
     * @param cmd
     * @param param
     * @param nPacketType
     * @param data
     * @param size
     * @return true:成功   false:失败
     */
    public static native boolean sovDirectSendData(String ystNo, String userName, String password, int cmd, int param, byte nPacketType, byte[] data, int size);


    /**
     * 流媒体猫眼设置协议，所有设置接口均通过此方法设置，不需要先发送文本聊天请求(原：strMedSendData)
     *
     * @param window
     * @param cmd
     * @param param
     * @param nPacketType
     * @param data
     * @param size
     * @return true:成功   false:失败
     */
    public static native boolean sovSendData(int window, int cmd, int param, byte nPacketType, byte[] data, int size);


    /**
     * 流媒体猫眼局域网搜索功能(原：strMedSearchDevice)
     *
     * @see 调用是否成功，等回调 AppConsts.CALL_CAT_BROAD_CALLBACK = 0xB7
     *
     * @return 如果有设备，会有此回调，每次回调返回一个设备号
     * //    env->CallVoidMethod(g_handle, g_notifyid, CALL_CATEYE_SEARCH_DEVICE,
     * //                        (jint) is_end, (jint) 0, jmsg);
     * //    is_end:1   最后一个设备
     * //    is_end:0  不是最后一个设备
     * //    #define CALL_CATEYE_SEARCH_DEVICE  0xD9
     * //    云视通号在jmsg，values["ystno"] = pystNo; values["identify"] = pidentify;
     */
    public static native void sovSearchLocalLanDevice();


    /**
     * 流媒体是否传实时视频数据(原：strMedRealTimeData)
     *
     * @param window
     * @param enable true 暂停实时视频数据;enable： false 请求实时视频数据
     * @return true:成功  false:失败
     *
     * @see 远程回放流程：调用strMedRealTimeData（原：strMedPlayback） 暂停实时视频  ->    发送命令请求回放数据 ->
     */
    public static native boolean sovEnableRealTimeData(int window, boolean enable);


    /**
     * 视频连接前设置一下用户名，让服务器知道是谁连接的视频，容易排查问题(原：strMedSetUserName)
     *
     * @param userName
     */
    public static native void sovSetUserName(String userName);


    /**
     * ap配置wifi(原：strMedConfigWifiInAP)
     *
     * @param ystNo
     * @param userName
     * @param password
     * @param cmd
     * @param param
     * @param nPacketType
     * @param data
     * @param size
     */
    public static native void sovConfigWifiByAP(String ystNo, String userName, String password, int cmd, int param, byte nPacketType, byte[] data, int size);


    /**
     * 流媒体IPC设备号上传到服务器，服务器会维护云视通号所对应的IP地址，保证流媒体连接。(原：strMedAddYstnos)
     *
     * @param ystNos 设备号数组
     */
    public static native void sovAddYSTNOS(String[] ystNos);


    /**
     * 解绑设备时调用，取消服务器中需要维护的设备号(原：strMedDelYstnos)
     *
     * @param ystNos 设备号数组
     */
    public static native void sovDelYSTNOS(String[] ystNos);



//
//    Java_com_jovision_Jni_setAESKey(JNIEnv *env, jclass clazz,
//                                    jstring sYst_No,
//                                    jstring userName,
//                                    jstring passWord,
//                                    jbyteArray data,
//                                    jint size)

    public static native void setAESKey(String ystNum, String devUser, String devPwd, byte[] keyArray, int length);

}


//
//package com.jovision;
//
///**
// * Created by juyang on 16/3/22.
// */
//public class Jni {
//
//    /**
//     * 1.初始化，参考 {@link JVSUDT#JVC_InitSDK(int, Object)}
//     *
//     * @param handle 回调句柄，要传 MainApplication 的实例对象哦，因为回调方式是：<br />
//     *               {@link MainApplication#onJniNotify(int, int, int, Object)}
//     * @param port   本地端口
//     * @param path   日志路径
//     * @param strIP  手机外网ip
//     * @return true：成功，false：失败
//     */
//    public static native boolean init(Object handle, int port, String path, String strIP);
//
//    /**
//     * 2.卸载，参考 {@link JVSUDT#JVC_ReleaseSDK()}
//     */
//    public static native void deinit();
//
//    /**
//     * 3.（新增接口）获取底层库版本,为了防止库用错，建议将该版本号打印出来
//     *
//     * @return json: {"jni":"xx","net":"xx"}
//     */
//    public static native String getVersion();
//
//    /**
//     * 4.启用底层日志打印，参考 {@link JVSUDT#JVC_EnableLog(boolean)}
//     *
//     * @param enable
//     */
//    public static native void enableLog(boolean enable);
//
//    /**
//     * 5.（新增接口）删除底层保存的错误日志
//     */
//    public static native void deleteLog();
//
//    /**
//     * 6.新 连接，参考
//     * {@link JVSUDT#JVC_Connect(int, int, String, int, String, String, int, String, boolean, int, boolean, int, Object)}
//     *
//     * @param window        窗口索引，从 0 开始
//     * @param channel       设备通道，从 1 开始
//     * @param ip            设备IP
//     * @param port          设备端口
//     * @param username      设备用户名
//     * @param password      设备密码
//     * @param cloudSeeId    设备云视通号码（例如：A361）
//     * @param groupId       设备编组（例如：A）
//     * @param isLocalDetect
//     * @param turnType
//     * @param isPhone
//     * @param connectType
//     * @param surface
//     * @param isVip
//     * @param isTcp
//     * @param isAp
//     * @param isTryOmx
//     * @param thumbName
//     * @return 连接结果，成功时返回窗口索引，失败时返回原因值，以下是返回的错误信息
//     * #define BAD_HAS_CONNECTED	-1 （未初始化网络库也报此错误）
//     * #define BAD_CONN_OVERFLOW	-2
//     *  #define BAD_NOT_CONNECT		-3
//     *  #define BAD_ARRAY_OVERFLOW	-4
//     *  #define BAD_CONN_UNKOWN		-5
//     */
//    public static native int connect(int window,
//                                     int channel,
//                                     String ip,
//                                     int port,
//                                     String username,
//                                     String password,
//                                     int cloudSeeId,
//                                     String groupId,
//                                     boolean isLocalDetect,
//                                     int turnType,
//                                     boolean isPhone,
//                                     int connectType,
//                                     Object surface,
//                                     boolean isVip,
//                                     boolean isTcp,
//                                     boolean isAp,
//                                     boolean isTryOmx,
//                                     String thumbName);
//
//    /**
//     * 7.暂停底层显示
//     *
//     * @param window 窗口索引
//     * @return true：成功，false：失败
//     */
//    public static native boolean pause(int window);
//
//    /**
//     * 8.恢复底层显示
//     *
//     * @param window  窗口索引
//     * @param surface
//     * @return true：成功，false：失败
//     */
//    public static native boolean resume(int window, Object surface);
//
//    /**
//     * 9.断开视频连接，参考 {@link JVSUDT#JVC_DisConnect(int)}
//     *
//     * @param window 窗口索引
//     * @return true：成功，false：失败
//     */
//    public static native boolean disconnect(int window);
//
//    /**
//     * 10.发送字节数据，参考 {@link JVSUDT#JVC_SendData(int, byte, byte[], int)}
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param data
//     * @param size
//     * @return true：成功，false：失败
//     */
//    public static native boolean sendBytes(int window, byte uchType,
//                                           byte[] data, int size);
//
//    /**
//     * 11.发送整数数据（远程回放进度调节），参考
//     * {@link JVSUDT#JVC_SendPlaybackData(int, byte, int, int)} 实际调用
//     * {@link #sendCmd(int, byte, byte[], int)}
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param data
//     */
//    public static native boolean sendInteger(int window, byte uchType, int data);
//
//    /**
//     * 12.修改指定窗口播放标识位，是否启用远程回放，参考 {@link JVSUDT#ChangePlayFalg(int, int)}
//     *
//     * @param window 窗口索引
//     * @param enable true：启用远程回放，false：不启用远程回放
//     * @return
//     */
//    public static native boolean enablePlayback(int window, boolean enable);
//
//    /**************** 2015-02-09 V2.2.0 新增功能 ********************/
//
//    /**
//     * 13.初始化音频编码（弃用）
//     *
//     * @param type         类型，amr/alaw/ulaw，参考 {@link AppConsts#JAE_ENCODER_SAMR},
//     *                     {@link AppConsts#JAE_ENCODER_ALAW},
//     *                     {@link AppConsts#JAE_ENCODER_ULAW}
//     * @param sampleRate
//     * @param channelCount
//     * @param bitCount
//     * @param block        PCM 640
//     * @return
//     */
//    public static native boolean initAudioEncoder(int type, int sampleRate,
//                                                  int channelCount, int bitCount, int block);
//
//    /**
//     * 14.编码一帧（弃用）
//     *
//     * @param data
//     * @return 失败的话返回 null
//     */
//    public static native byte[] encodeAudio(byte[] data);
//
//    /**
//     * 15.销毁音频编码，如果要切换编码参数，必须销毁的重新创建（弃用）
//     *
//     * @return
//     */
//    public static native boolean deinitAudioEncoder();
//
//    /**
//     * 16.发送字符串数据
//     *
//     * @param window   窗口索引
//     * @param uchType  发送类型
//     * @param isExtend 是否扩展消息
//     * @param count    扩展包数量
//     * @param type     扩展消息类型
//     * @param data     数据
//     */
//    public static native boolean sendString(int window, byte uchType,
//                                            boolean isExtend, int count, int type, String data);
//
//    /**
//     * 17.发送超级字节数组
//     *
//     * @param window
//     * @param uchType
//     * @param isExtend
//     * @param count
//     * @param type
//     * @param p1
//     * @param p2
//     * @param p3
//     * @param data
//     * @param size
//     * @return
//     */
//    public static native boolean sendSuperBytes(int window, byte uchType,
//                                                boolean isExtend, int count, int type, int p1, int p2, int p3,
//                                                byte[] data, int size);
//
//    /**
//     * 18. 发送原始数据
//     *
//     * @param window
//     * @param data_type
//     * @param packet_type
//     * @param packet_count
//     * @param extend_type
//     * @param extend_p1
//     * @param extend_p2
//     * @param extend_p3
//     * @param data
//     * @param size
//     * @return
//     */
//    public static native boolean sendPrimaryBytes(int window, byte data_type,
//                                                  int packet_type, int packet_count, int extend_type, int extend_p1,
//                                                  int extend_p2, int extend_p3, byte[] data, int size);
//
//    /**
//     * 19.老声波：生成声波配置语音数据 生成声波配置数据，重复多次会阻塞执行
//     *
//     * @param data：WiFi名称和密码
//     * @param times：声波响的次数
//     */
//    public static native void genVoice(String data, int times);
//
//    /**
//     * 20.发送聊天命令，参考 {@link JVSUDT#JVC_SendTextData(int, byte, int, int)}
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param size
//     * @param flag
//     */
//    public static native boolean sendTextData(int window, byte uchType,
//                                              int size, int flag);
//
//    /**
//     * 21.开始录制（本地录像），参考
//     * {@link JVSUDT#StartRecordMP4(String, int, int, int, int, int, double, int)}
//     *
//     * @param window       窗口索引
//     * @param path         文件保存路径
//     * @param enableVideo  是否录制视频
//     * @param enableAudio  是否录制音频
//     * @param audioCodecID 0为amr音频格式封装 1原格式封装 接口新增参数；amr大部分手机播放器均可以播放此格式音频，原格式封装则支持的较少
//     * @return
//     */
//    public static native boolean startRecord(int window, String path,
//                                             boolean enableVideo, boolean enableAudio, int audioCodecID,boolean isTwoRecord);
//
//
//    /**
//     * 22.检查对应窗口是否处于录像状态， 现在只有单路可用
//     *
//     * @param window 窗口索引
//     * @return true 正在录像，false 没在录像
//     */
//    public static native boolean checkRecord(int window);
//
//    /**
//     * 23.停止录制，参考 {@link JVSUDT#StopRecordMP4(int)}
//     *
//     * @return
//     */
//    public static native boolean stopRecord();
//
//    /**
//     * 24.修改指定窗口音频标识位
//     *
//     * @param window 窗口索引
//     * @param enable 是否播放（ Normaldata 的）音频数据 true：开启声音  false：关闭声音
//     * @return
//     */
//    public static native boolean enablePlayAudio(int window, boolean enable);
//
//    /**
//     * 25.设置 AP，参考 {@link JVSUDT#JVC_ManageAP(int, byte, String)}
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param json
//     */
//    public static native boolean setAccessPoint(int window, byte uchType,
//                                                String json);
//
//    /**
//     * 26.获取指定窗口是否正在播放音频
//     *
//     * @param window 窗口索引
//     * @return true:正在监听，false：没有正在监听
//     */
//    public static native boolean isPlayAudio(int window);
//
//    /**
//     * 27.开启小助手（快速链接服务），参考 {@link JVSUDT#JVC_EnableHelp(boolean, int)}
//     *
//     * @param enable
//     * @param typeId   enable == true 时
//     *                 <ul>
//     *                 <li>1: 使用者是开启独立进程的云视通小助手</li>
//     *                 <li>2: 使用者是云视通客户端，支持独立进程的云视通小助手</li>
//     *                 <li>3: 使用者是云视通客户端，不支持独立进程的云视通小助手</li>
//     *                 </ul>
//     * @param maxLimit 允许最大限制
//     * @return
//     */
//    public static native boolean enableLinkHelper(boolean enable, int typeId,
//                                                  int maxLimit);
//
//    /**
//     * 28.给设备设置连接小助手，参考 {@link JVSUDT#JVC_SetHelpYSTNO(byte[], int)}
//     *
//     * @param json [{gid: "A", no: 361, channel: 1, name: "abc", pwd:
//     *             "123"},{gid: "A", no: 362, channel: 1, name: "abc", pwd:
//     *             "123"}]
//     * @return
//     */
//    public static native boolean setLinkHelper(String json);
//
//    /**
//     * 29.开启广播，参考 {@link JVSUDT#JVC_StartLANSerchServer(int, int)}
//     *
//     * @param localPort  默认 9400
//     * @param serverPort 默认 6666
//     * @param strIP      手机的ip地址
//     * @return
//     */
//    public static native int searchLanServer(int localPort, int serverPort, String strIP);
//
//    /**
//     * 30.停止搜索局域网服务端，参考 {@link JVSUDT#JVC_StopLANSerchServer()}
//     */
//    public static native void stopSearchLanServer();
//
//    /**
//     * 31.搜索局域网设备，参考 搜索本局域网
//     * {@link JVSUDT#JVC_MOLANSerchDevice(String, int, int, int, String, int)}
//     * 跨网段广播
//     *
//     * @param group
//     * @param cloudSeeId
//     * @param cardType
//     * @param variety
//     * @param deviceName
//     * @param timeout    单位是毫秒
//     * @param frequence
//     * @return
//     */
//    public static native int searchLanDevice(String group, int cloudSeeId,
//                                             int cardType, int variety, String deviceName, int timeout,
//                                             int frequence);
//
//    /**
//     * 32.查询某个设备是否被搜索出来 局域网本网段广播
//     *
//     * @param groudId    组标识
//     * @param cloudSeeId 云视通编号
//     * @param timeout    超时时间，毫秒
//     * @return 调用是否成功，等回调
//     */
//    public static native boolean queryDevice(String groudId, int cloudSeeId,
//                                             int timeout);
//
//    /**
//     * 33.恢复底层音频播放
//     *
//     * @param window 窗口索引
//     * @return
//     */
//    public static native boolean resumeAudio(int window);
//
//    /**
//     * 34.暂停底层音频播放
//     *
//     * @param window 窗口索引
//     * @return
//     */
//    public static native boolean pauseAudio(int window);
//
//    /**
//     * 35.底层MP4播放初始化功能
//     *
//     * @param
//     * @return
//     */
//    public static native int Mp4Init();
//
//    /**
//     * 36.底层MP4播放初始化功能
//     *
//     * @param uri
//     * @return
//     */
//    public static native int SetMP4Uri(String uri);
//
//    /**
//     * 37.底层MP4播放准备接口，主要解析MP4文件信息，并且通过回调返回给应用层
//     *
//     * @return 0：OK ;1:正在播放 ;2：底层播放线程正在退出，需要等待完全退出才能继续播放
//     */
//    public static native int Mp4Prepare();
//
//    /**
//     * 38.底层MP4开始播放接口
//     *
//     * @param surface
//     * @return 0：OK 其他失败
//     */
//    public static native int Mp4Start(Object surface);
//
//    /**
//     * 39.底层MP4停止播放接口
//     *
//     * @param StopSeconds 停止播放的时间点(秒)
//     * @return
//     */
//    public static native int Mp4Stop(int StopSeconds);
//
//    /**
//     * 40.底层MP4播放库销毁释放资源
//     *
//     * @return
//     */
//    public static native int Mp4Release();
//
//    /**
//     * 41.底层MP4播放暂停接口，与MP4Resume对应
//     *
//     * @return
//     */
//    public static native int Mp4Pause();
//
//    /**
//     * 42.底层MP4继续播放接口，与Mp4Pause对应
//     *
//     * @return
//     */
//    public static native int Mp4Resume();
//
//    /**
//     * 43.停止广播
//     * <p/>
//     * STOP lansearch return：1:success 0:failed
//     */
//    public static native int StopMobLansearch();
//
//    /**
//     * 44.截图抓拍
//     *
//     * @param window  窗口索引
//     * @param name    待保存的文件名
//     * @param quality 画面质量
//     * @return
//     */
//    public static native boolean screenshot(int window, String name, int quality);
//
//    /**
//     * 45.设置本地的服务器
//     *
//     * @param pGroup
//     * @param pServer
//     * @return 0:成功 其他：失败
//     */
//    public static native int SetSelfServer(
//            String pGroup, String pServer);
//
//    /**
//     * 46.设置缩略图信息
//     *
//     * @param width   缩略图宽
//     * @param quality 图像质量
//     */
//    public static native void setThumb(int width, int quality);
//
//    /**
//     * 47.是否显示统计
//     *
//     * @param on true:开启统计 false：关闭统计
//     */
//    public static native void setStat(boolean on);
//
//    /**
//     * 48.取消下载，删除正在下载的文件
//     */
//    public static native void cancelDownload();
//
//    /**
//     * 49.设置窗口颜色
//     *
//     * @param window 窗口索引
//     * @param red    红，0~1
//     * @param green  绿，0~1
//     * @param blue   蓝，0~1
//     * @param alpha  透明，0~1
//     * @return
//     */
//    public static native boolean setColor(int window, float red, float green,
//                                          float blue, float alpha);
//
//    /**
//     * 51.获取下载文件路径
//     *
//     * @return
//     */
//    public static native String getDownloadFileName();
//
//    /**
//     * 50.设置下载文件路径
//     *
//     * @param fileName
//     */
//    public static native void setDownloadFileName(String fileName);
//
//    /**
//     * 52.翻转视频
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param cmd
//     */
//    public static native boolean rotateVideo(int window, byte uchType,
//                                             String cmd);
//
//    /**
//     * 设置mtu的调用步骤:<br/>
//     * 1、调用停止小助手函数 -> StopHelp();<br/>
//     * 2、设置MTU -> SetMtu(int mtu);<br/>
//     * 3、打开小助手函数 -> enableLinkHelper;<br/>
//     * 4、把所有设备设置到小助手函数中 -> setLinkHelper;<br/>
//     * 以上步骤操作后,要记住设置mut状态,下次重启软件,初始化网络sdk后 ,立即设置mtu,然后在打开小助手,设置小助手.
//     */
//
//    /**
//     * 53.停止小助手<br/>
//     */
//    public static native int StopHelp();
//
//    /**
//     * 54.设置MTU<br/>
//     * nMtu可以设置的值只有700,1400（七百，1千4百）
//     *
//     * @param nMtu
//     * @return 1:成功 0：失败
//     */
//    public static native int SetMTU(int nMtu);
//
//    /**
//     * 55.获取已设置的云视通列表<br/>
//     * 参考 {@link JVSUDT#JVC_GetHelpYSTNO(byte[], int)}
//     *
//     * @return json [{cno: "A361", enable: false},{no: "A362", enable: false}]
//     */
//    public static native String getAllDeviceStatus();
//
//    // --------------------------------------------------------
//    // ## libjvpush.so 开始
//    // --------------------------------------------------------
//    /**
//     * 初始化并且连接离线推送SDK接口 平台参数<br/>
//     * #define BIZ_PUSH_IOS 0x00<br/>
//     * #define BIZ_PUSH_ANDROID 0x10<br/>
//     * #define BIZ_PUSH_PC_WIN 0x21<br/>
//     * #define BIZ_PUSH_PC_LINUX 0x22<br/>
//     * #define BIZ_PUSH_PC_MAX 0x23
//     */
//
//    /**
//     * 56. 初始化离线推送SDK<br/>
//     * init sdk in:上下文 object
//     */
//    public static native int initSdk(Object CallbackHandle);
//
//    /**
//     * 57.开始<br/>
//     * return :0:success -1:failed
//     */
//    public static native int start(Byte platform, int appid, String token, String url);
//
//    /**
//     * 58.停止<br/>
//     * return :0:success -1:failed
//     */
//    public static native int stop();
//
//    // --------------------------------------------------------
//    // ## libjvpush.so 结束
//    // --------------------------------------------------------
//
//    /**
//     * 59.设置显示图像的顶点坐标(坐标系原点在 Surface 左下顶点)和长宽
//     *
//     * @param window 窗口索引
//     * @param left   图像左坐标
//     * @param bottom 图像底坐标
//     * @param width  图像宽
//     * @param height 图像高
//     * @return
//     */
//    public static native boolean setViewPort(int window, int left, int bottom,
//                                             int width, int height);
//
//    /**
//     * 60.新声波：生成声波配置语音数据 生成声波配置数据，重复多次会阻塞执行
//     *
//     * @param data：WiFi名称和密码
//     * @param times：声波响的次数
//     */
//    public static native void genSoundConfig(String data, int times);
//
//    /****************************************************************************
//     * 62.猫眼设备唤醒接口 名称 : JVC_StartBroadcastSelfServer 功能 : 开启自定义广播服务 回调使用之前方式
//     * 类型是0xB7 参数 : [IN] nLPort 本地服务端口，<0时为默认9700 [IN] nServerPort
//     * 设备端服务端口，<=0时为默认9108,建议统一用默认值与服务端匹配 返回值: TRUE/FALSE 其他 :
//     *****************************************************************************/
//    public static native int startBCSelfServer(int localPort, int serverPort);
//
//    /****************************************************************************
//     * 62.停止猫眼唤醒接口 名称 : JVC_StopBroadcastSelfServer 功能 : 停止自定义广播服务 参数 : 无 返回值: 无
//     * 其他 : 无
//     *****************************************************************************/
//    public static native void stopBCSelfServer();
//
//    /****************************************************************************
//     * 63.猫眼回调需要调用接口 名称 : JVC_SendSelfDataOnceFromBC 功能 :
//     * 此方法要在StartBroadcastSelfServer回调返回时调用 从自定义广播套接字发送一次UDP消息 参数 : [IN] pBuffer
//     * 净载数据 类型（4字节）+云视通号码（4字节） [IN] nSize 净载数据长度 [IN] pchDeviceIP 目的IP地址 [IN]
//     * nLocalPort 目的端口 返回值: 无 其他 :
//     *****************************************************************************/
//    public static native void sendSelfDataOnceFromBC(byte[] buffer, int size, String ip, int port);
//
//    /**
//     * 64.连接流媒体
//     *
//     * @param window
//     * @param url
//     * @param surface
//     * @param isTryOmx
//     * @param thumbName
//     * @param timeOut   超时时间：为毫秒：比如10秒超时，需填写：10*1000
//     * @return
//     */
//    public static native int connectRTMP(int window, String url,
//                                         Object surface, boolean isTryOmx, String thumbName, int timeOut);
//
//    /**
//     * 65.关闭流媒体
//     *
//     * @param window
//     * @return
//     */
//    public static native boolean shutdownRTMP(int window);
//
//    /**
//     * 66.开始录音接口
//     *
//     * @param window
//     * @return
//     */
//    public static native boolean recordAndsendAudioData(int window);
//
//    /**
//     * 67.停止录音接口
//     *
//     * @param window
//     * @return
//     */
//    public static native boolean stopRecordAudioData(int window);
//
//    /**
//     * 68.清理小助手缓存
//     */
//    public static native void clearCache();
//
//    /**
//     * 69.默认播放器是开启aec和降噪，如不需要 调用此接口重置player 在recordAndsendAudioData之前
//     *
//     * @param window
//     * @param isAec
//     * @param isDenoise
//     */
//    public static native void resetAecDenoise(int window, boolean isAec, boolean isDenoise);
//
//    /**
//     * 69.手机改变网络状态时更换绑定的IP地址 2015年12月4日
//     *
//     * @param strNewIP
//     * @return
//     */
//    public static native int ChangeMobileIP(String strNewIP);
//
//    /**
//     * 70.移除小助手
//     * [METHOD] HelperRemove
//     * [IN] pGroup 编组号，编组号+nYSTNO可确定唯一设备
//     * [IN] NYST 搜索具有某云视通号码的设备，>0有效
//     *  * [RETURN] no
//     */
//    public static native int HelperRemove(String group, int yst);
//
//
//    /**
//     * 71.播歌曲  新增网络接口
//     *
//     * @param path     文件路径
//     * @param fileName 文件名
//     * @return
//     */
//    public static native int SendFile(String path, String fileName);
//
//
//    /**
//     * 72.发送命令，参考 {@link JVSUDT#JVC_SendCMD(int, byte, byte[], int)}
//     * // [Neo] TODO 未验证
//     *
//     * @param window  窗口索引
//     * @param uchType
//     * @param data
//     * @param size
//     * @return
//     */
//    public static native int sendCmd(int window, byte uchType, byte[] data,
//                                     int size);
//
//
//
////    JNIEXPORT jboolean JNICALL Java_com_jovision_Jni_setSurfaceStat(JNIEnv* env,
////                                                                    jclass clazz, jint window, jint windowNum, jint isHorizontal)
////    两个窗口
////    JNIEXPORT jboolean JNICALL Java_com_jovision_Jni_setSurfaceStat(JNIEnv* env,
////                                                                    jclass clazz, jint window,  2, 0)
////    一个窗口
////    JNIEXPORT jboolean JNICALL Java_com_jovision_Jni_setSurfaceStat(JNIEnv* env,
////                                                                    jclass clazz, jint window,  2, 0)
//
//
//    /**
//     * 73.设置视频播放显示几个窗口
//     * @param window
//     * @param windowNum
//     * @param isHorizontal
//     * @return
//     */
//    public static native boolean setSurfaceStat(int window,int windowNum,int isHorizontal);
//
//
//
//
//}
