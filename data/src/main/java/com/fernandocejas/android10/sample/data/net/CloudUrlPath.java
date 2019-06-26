package com.fernandocejas.android10.sample.data.net;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 云端requestUrl
 *     version: 1.0
 * </pre>
 */
public class CloudUrlPath {

  public static final String PATH_MODIFY_USER_INFO = "/routerservice/user/save";
  public static final String PATH_LOGOUT = "/routerservice/login/logout";
  public static final String PATH_MODIFY_PWD = "/routerservice/user/modifypassword";

  public static final String PATH_GET_INTEL_DEVICE_LIST = "/routerservice/device/list";
  public static final String PATH_GET_INTEL_DEVICE_DETAIL = "/routerservice/device/detail";
  //
  public static final String PATH_GET_EXTRACT_NET_PORT = "/securitystorage/gettunnel";

  public static final String PATH_UNBIND_INTEL_DEV = "/routerservice/device/unbindTrans";
  public static final String PATH_GET_DEV_TREE = "/routerservice/device/list";
  public static final String PATH_COMMIT_ADD_ROUTER_INFO = "/routerservice/device/adddevice";
  public static final String PATH_MODIFY_DEV = "/routerservice/device/modify";
  //
  public static final String PATH_TRANSMIT = "/routerservice/router/dispatch";
  public static final String PATH_GET_STS_TOKEN = "/routerservice/external/sts/gettoken";
  public static final String PATH_GET_ROUTER_INFO_CLOUD = "/routerservice/router/detail";
  public static final String PATH_MODIFY_ROUTER_DESC = "/routerservice/router/updaterouterdescription";
  public static final String PATH_MODIFY_DEV_NAME = "/routerservice/device/update";
  public static final String PATH_GET_ROUTER_GROUPS = "/routerservice/router/grouplist";
  public static final String PATH_MODIFY_ROUTER_GROUP = "/routerservice/router/updateroutergroup";
  public static final String PATH_CREATE_ROUTER_GROUP = "/routerservice/router/addgroup";
  public static final String PATH_UNBIND_ROUTER = "/routerservice/device/unbind";
  public static final String PATH_GET_FINGERPRINTS = "/routerservice/device/fingerprint/list";
  public static final String PATH_GET_PASSWORDS = "/routerservice/device/pass/list";
  public static final String PATH_MODIFY_LOCK_PWD_NAME = "/routerservice/device/pass/update";
  public static final String PATH_DELETE_LOCK_PWD = "/routerservice/device/pass/del";
  public static final String PATH_MODIFY_LOCK_FP_NAME = "/routerservice/device/fingerprint/update";
  public static final String PATH_DELETE_LOCK_FP = "/routerservice/device/fingerprint/del";
  public static final String PATH_ADD_LOCK_PWD = "/routerservice/device/pass/add";
  public static final String PATH_ADD_LOCK_FP = "/routerservice/device/fingerprint/add";
  public static final String PATH_CHECK_APP_VERSION = "/routerservice/version/queryversion";
  public static final String PATH_CHECK_LOCK_VERSION = "/routerservice/version/querylockversion";
  public static final String GET_USERS_OF_LOCK = "/routerservice/device/getdeviceusersid";
  public static final String PATH_GET_UNLOCK_MODE = "/routerservice/device/getopenconfig";
  public static final String PATH_MODIFY_UNLOCK_MODE = "/routerservice/device/modifyopenconfig_sprint2";
  public static final String PATH_MODIFY_UNLOCK_MODE_V15 = "/routerservice/device/modifyopenconfig";
  public static final String PATH_UPLOAD_LOGCAT = "/routerservice/test/addandroidlog";
  public static final String PATH_GET_DOOR_CARDS = "/routerservice/device/doorcard/list";
  public static final String PATH_DELETE_LOCK_DOOR_CARD = "/routerservice/device/doorcard/del";
  public static final String PATH_MODIFY_LOCK_DOOR_CARD_NAME = "/routerservice/device/doorcard/update";
  public static final String PATH_ADD_LOCK_DOOR_CARD = "/routerservice/device/doorcard/add";
  public static final String PATH_LOCK_FACTORY_RESET = "/routerservice/device/reset";
  public static final String PATH_GET_USER_ROLE = "/routerservice/device/getrole";
  public static final String PATH_GET_LOCK_USERS = "/routerservice/device/admin/binduserlist";
  public static final String PATH_DELETE_LOCK_USER = "/routerservice/device/admin/delbinduser";
  public static final String PATH_UNBIND_LOCK_OF_ADMIN = "/routerservice/device/admin/unbind";
  public static final String PATH_GET_PASSAGEWAY_MODE = "/routerservice/device/getchannelconfig";
  public static final String PATH_MODIFY_PASSAGEWAY = "/routerservice/device/modifychannelconfig";
  public static final String PATH_UPDATE_LOCK_VERSION = "/routerservice/device/updateversion";
  public static final String PATH_ADD_TEMP_PWD = "/routerservice/device/pass/addtemp";
  public static final String PATH_QUERY_TEMP_PWD = "/routerservice/device/pass/counttemp";
  public static final String PATH_GET_TEMP_PWD = "/routerservice/device/pass/gettemp";
  public static final String PATH_GET_LOCK_VOLUME = "/routerservice/device/getvolume";
  public static final String PATH_ADJUST_LOCK_VOLUME = "/routerservice/device/setvolume";
  public static final String PATH_CHECK_LITE_VERSION = "/routerservice/version/queryliteversion";
  public static final String PATH_UPDATE_LITE = "/routerservice/version/liteupgrade";
  public static final String PATH_GET_LITE_UPDATE = "/routerservice/device/getupgradestatus";
  public static final String PATH_CLOUD_UNBIND_ROUTER_LOCK = "/routerservice/device/unbindlock";


  private static String sToken;

  public static String getToken() {
    return sToken;
  }

  public static void setToken(String token) {
    CloudUrlPath.sToken = token;
  }

  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";

  public static final String PATH_LOGIN = "/routerservice/login/login";
  public static final String PATH_GET_ID_CODE = "/routerservice/external/sms/register";
  public static final String PATH_RESET_PWD_GET_ID_CODE = "/routerservice/external/sms/modifypassword";
  public static final String PATH_REGISTER = "/routerservice/user/register";
  public static final String PATH_RESET_PWD_FORGET = "/routerservice/user/forgetpassword";


  public static final String PATH_LOGIN_LIST = "/routerservice/loginlist/pwdlogin";

  public static final String PATH_GET_AGREEMENT = "/routerservice/statute/querystatute";  //用户协议

  public static final String PATH_GET_MEM_REMARK_NAME = "/routerservice/share/updateremark";

  public static final String PATH_GET_QUESTION_LIST = "/routerservice/faq/list";  //问题列表

  public static final String PATH_GET_QUESTION_DETAIL = "/routerservice/faq/detail";  //问题详情

  public static final String PATH_GET_MY_ADVICE_LIST = "/routerservice/feedback/list";  //我的反馈列表

  public static final String PATH_GET_ADVICE_DETAIL = "/routerservice/feedback/detail";  //反馈 详情

  public static final String PATH_POST_FEED_BACK = "/routerservice/feedback/save";  //新增反馈

  public static final String PATH_UPDATE_FEED_BACK = "/routerservice/feedback/update";  //更新反馈

  public static final String PATH_DEVICE_COUNT = "/routerservice/device/devicenum";  //网关数量

  public static final String PATH_UPLOAD_MSG_ID = "/routerservice/user/uploaddeviceid";

  public static final String PATH_SHARED_MEM_LIST = "/routerservice/share/list";  //设备共享成员

  public static final String PATH_INVITATION = "/routerservice/share/share";  //邀请

  public static final String PATH_GET_LOCK_LIST = "/routerservice/cat/locklist";

  public static final String PATH_CONNECT_LOCK = "/routerservice/cat/pairinglock";

  public static final String PATH_QUERY_LOCKED_CAT = "/routerservice/device/isbinded";

  public static final String PATH_UNBIND_CAT_LOCK = "/routerservice/cat/unpairinglock";

  public static final String PATH_UPLOAD_DEVICE_PWD = "/routerservice/device/modifypass";

  public static final String PATH_CAT_LOCK = "/routerservice/cat/pairedlock";

  public static final String PATH_GET_DOOR_BEER_MSG_LIST = "/routerservice/cat/bellrecordlist";

  public static final String PATH_GET_DOOR_BEER_MSG_DETAIL = "/routerservice/cat/bellrecorddetail";

  public static final String PATH_SET_DOOR_BEER_MSG_IS_READ= "/routerservice/cat/readallbellrecord";

  public static final String PATH_DELETE_DOOR_BEER_MSG= "/routerservice/cat/deletebellrecord";

  public static final String PATH_MSG_DETAIL = "/routerservice/message/detail";  //消息详情

  public static final String PATH_MSG_LIST = "/routerservice/message/list";  //消息列表

  public static final String DEAL_INVITATION = "/routerservice/share/handle";  //处理邀请

  public static final String DELETE_MSG = "/routerservice/message/delete";  // 消息删除

  public static final String GET_MSG_COUNT = "/routerservice/message/msgtip";  // 消息数量统计

  public static final String SET_MSG_READ = "/routerservice/message/readmsg";  // 消息已读

  public static final String PATH_UNLOCK_INFO_LIST = "/routerservice/device/recordlist";  // 开门信息/异常信息列表

}
