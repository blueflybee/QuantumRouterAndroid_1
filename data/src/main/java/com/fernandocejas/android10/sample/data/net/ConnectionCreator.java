package com.fernandocejas.android10.sample.data.net;

/**
 * @author shaojun
 * @name ConnectionCreator
 * @package com.fernandocejas.android10.sample.data.net
 * @date 15-12-9
 */
public class ConnectionCreator {

  public static final String CLOUD_CONNECTION = "cloud_connection";
  public static final String ROUTER_CONNECTION = "router_connection";
  public static final String MOCK_CONNECTION = "mock_connection";

  private static IPostConnection mConnection;

  public static IPostConnection create(String which, String url) {
    if (which.equalsIgnoreCase(CLOUD_CONNECTION)) {
      return new CloudApiConnection(url);
    }

    else if (which.equalsIgnoreCase(ROUTER_CONNECTION)) {
      return new RouterApiConnection(url);
    }

    else if (which.equalsIgnoreCase(MOCK_CONNECTION)) {
      return new MockApiPostConnection();
    }

    else {
      return new MockApiPostConnection();
    }
  }
}
