package com.fernandocejas.android10.sample.data.net;

import android.support.annotation.Nullable;

import com.qtec.model.core.QtecEncryptInfo;

import java.io.IOException;

/**
 * @author shaojun
 * @name IPostConnection
 * @package com.fernandocejas.android10.sample.data.net
 * @date 15-12-9
 */
public interface IPostConnection {

  String getSessionId();

  void setSessionId(String sessionId);

  @Nullable String requestSyncCall(String requestMsg, String bizCode, QtecEncryptInfo encryptInfo) throws IOException;

  String getUrl();
}
