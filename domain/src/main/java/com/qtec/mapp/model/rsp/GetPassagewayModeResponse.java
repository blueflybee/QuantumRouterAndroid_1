package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetPassagewayModeResponse {

  public static final String CHANNEL_OPEN = "1";
  public static final String CHANNEL_CLOSED = "0";

  /**
   * channelConfig : 0，关闭（默认）；1，开启
   */
  private String channelConfig;

  public String getChannelConfig() {
    return channelConfig;
  }

  public void setChannelConfig(String channelConfig) {
    this.channelConfig = channelConfig;
  }
}
