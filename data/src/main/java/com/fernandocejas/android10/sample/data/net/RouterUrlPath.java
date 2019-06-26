package com.fernandocejas.android10.sample.data.net;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 网关requestUrl
 *     version: 1.0
 * </pre>
 */
public class RouterUrlPath {

  public static final String PATH_SEARCH_ROUTER = "routerinfo";
  public static final String PATH_ADD_ROUTER_VERIFY = "routeradd";
  public static final String PATH_SEARCH_INTEL_DEV_NOTIFY = "devsearch";
  public static final String PATH_SEARCH_INTEL_DEV = "getdevsearch";
  public static final String PATH_ADD_INTEL_DEV_VERIFY = "devadd";
  public static final String PATH_UNBIND_INTEL_DEV = "devunbound";
  public static final String PATH_FIRST_GET_KEY = "firstkeyrequest";
  public static final String PATH_GET_ROUTER_INFO = "get_routerbasicinfo";
  public static final String PATH_SET_DHCP = "set_basicwan_cfg";
  public static final String PATH_SET_PPPOE = "set_basicwan_cfg";
  public static final String PATH_SET_STATIC_IP = "set_basicwan_cfg";
  public static final String PATH_GET_NET_MODE = "get_wantype_cfg";
  public static final String PATH_RESTART_ROUTER = "routerrestart";
  public static final String PATH_FACTORY_RESET = "cfgrestore";
  public static final String PATH_UPDATE_FIRMWARE = "upgrade";
  public static final String PATH_GET_WIFI_CONFIG = "get_wireless_cfg";
  public static final String PATH_CONFIG_WIFI = "set_wireless_cfg";
  public static final String PATH_GET_TIMER_TASK = "get_timertask_cfg";
  public static final String PATH_SET_ROUTER_TIMER = "set_timertask_cfg";
  public static final String PATH_CHECK_ADMIN_PWD = "check_password_cfg";
  public static final String PATH_SET_ADMIN_PWD = "set_password_cfg";
  public static final String PATH_GET_KEY = "keyrequest";
  public static final String PATH_CHECK_FIRMWARE = "get_update_version";
  public static final String PATH_GET_FIRMWARE_UPDATE_STATUS = "get_update_rate";
  public static final String PATH_FIRST_CONFIG = "set_firstconfigure_cfg";
  public static final String PATH_BIND_ROUTER_TO_LOCK = "devadd";
  public static final String PATH_QUERY_BIND_ROUTER_TO_LOCK = "devcheck";
  public static final String PATH_UNBIND_ROUTER_TO_LOCK = "devunbound";
  public static final String PATH_GET_ROUTER_FIRST_CONFIG = "get_systemconfigure_cfg";


  private static String sToken;

  public static String getToken() {
    return sToken;
  }

  public static void setToken(String token) {
    RouterUrlPath.sToken = token;
  }

  public static final String METHOD_GET = "get";
  public static final String METHOD_POST = "post";
  public static final String METHOD_PUT = "put";
  public static final String METHOD_DELETE = "delete";

  public static final String PATH_GET_ROUTER_STATUS = "routerstatus";
  public static final String PATH_POST_SPECIAL_ATTENTION = "proc_specialcare_cfg";
  public static final String PATH_GET_SPECIAL_ATTENTION = "proc_specialcare_cfg";
  public static final String PATH_GET_QOS_INFO = "proc_qos_cfg";
  public static final String PATH_GET_CHILD_CARE_LIST = "proc_childrule_cfg";
  public static final String PATH_ADD_VPN = "add_vpn_cfg";
  public static final String PATH_DELETE_VPN = "del_vpn_cfg";
  public static final String PATH_MODIFY_VPN = "set_vpn_cfg";
  public static final String PATH_GET_VPN_LIST = "get_vpn_cfg";
  public static final String PATH_SET_VPN = "set_vpn_sw";
  public static final String PATH_POST_SAFE_INSPECTION = "quicklycheck";
  public static final String PATH_GET_WIRELESS_LIST = "get_wds_wifi_scan";
  public static final String PATH_POST_CONNECT_WIRELESS = "set_up_wds";
  public static final String PATH_GET_CONNECTED_WIFI = "get_wds_cfg";
  public static final String PATH_GET_BLACK_LIST = "proc_macblock_cfg";
  public static final String PATH_REMOVE_BLACK_MEM = "proc_macblock_cfg";
  public static final String PATH_SET_WIFI_SWITCH = "set_wds_cfg";
  public static final String PATH_GET_WIFI_DETAIL = "get_wds_status";
  public static final String PATH_GET_LOCK_STATUS = "lockstatus";
  public static final String PATH_ADD_FINGER = "addfingerprint";
  public static final String PATH_MODIFY_FINGER_NAME = "renamefingerprint";
  public static final String PATH_DELETE_FINGER = "delfingerprint";
  public static final String PATH_REMOTE_LOCK_OPERATION = "devaction";
  public static final String PATH_QUERY_FINGER_INFO = "getfingerprint";
  public static final String PATH_GET_SAM_PWD = "get_smb_pwd";
  public static final String PATH_QUERY_DISK_STATE = "proc_qtec_disk_cfg";
  public static final String PATH_SET_SIGNAL_REGULATION = "set_wifi_txpower";
  public static final String PATH_GET_SIGNAL_MODE = "get_wifi_txpower";
  public static final String PATH_GET_AUTH_DEVICE_LIST = "get_antiwifi_dev_list";
  public static final String PATH_GET_BAND_SPEED = "proc_wan_speedtest_cfg";
  public static final String PATH_GET_GUEST_SWITCH = "get_guest_wifi";
  public static final String PATH_GET_WIFI_TIME_CONFIG = "get_wifi_timer";
  public static final String PATH_DELETE_WIFI_SWITCH = "del_wifi_timer_rule";
  public static final String PATH_SET_WIFI_DATA = "set_wifi_timer_rule";
  public static final String PATH_SET_WIFI_ALL_SWITCH = "set_wifi_timer";
  public static final String PATH_SET_GUEST_SWITCH = "set_guest_wifi";
  public static final String PATH_ANTI_FRIT_NET = "get_antiwifi_status";
  public static final String PATH_ANTI_FRIT_SWITCH = "set_antiwifi_admin_forbidden";
  public static final String PATH_ENABLE_ANTI_FRIT_NET = "set_antiwifi";
  public static final String PATH_SET_ANTI_QUESTION = "set_antiwifi_question";
  public static final String PATH_GET_ANTI_FRIT_QUESTION = "get_antiwifi_authinfo";
  public static final String PATH_GET_WAITING_LIST = "get_antiwifi_dev_list";
  public static final String PATH_ALLOW_AUTH_DEVICE = "set_authed_antiwifi_dev";
  public static final String PATH_BLOCK_AUTHED = "block_authed_antiwifi_dev";


}
