/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.net;

import android.support.annotation.Nullable;

import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.google.gson.JsonIOException;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.mapp.model.rsp.LoginResponse;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link Callable} so when executed asynchronously can
 * return a value.
 */
public class MockApiPostConnection implements IPostConnection {

  public static final String JSON_LOGIN_LIST_RSP = "{\"code\":0,\"msg\":null,\"data\":[{\"phone\":\"13344450\",\"password\":\"0\"},{\"phone\":\"13344451\",\"password\":\"1\"},{\"phone\":\"13344452\",\"password\":\"2\"},{\"phone\":\"13344453\",\"password\":\"3\"},{\"phone\":\"13344454\",\"password\":\"4\"}]}";
  private String sessionId = "";

  protected MockApiPostConnection() {
  }


  @Override
  public String getSessionId() {
    return sessionId;
  }

  @Override
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Nullable
  @Override
  public String requestSyncCall(String requestMsg, String bizCode, QtecEncryptInfo encryptInfo) throws IOException {
    JsonMapper jsonMapper = new JsonMapper();
    try {
      if (requestMsg.contains(CloudUrlPath.PATH_LOGIN_LIST)) {

        return JSON_LOGIN_LIST_RSP;

      }

      if (requestMsg.contains(CloudUrlPath.PATH_LOGIN)) {

        QtecResult qtecResult = new QtecResult();
        qtecResult.setCode(0);
        qtecResult.setMsg("ok");
        LoginResponse data = new LoginResponse();
        data.setUserPhone("666666666");
        data.setUserNickName("dddd");
        qtecResult.setData(data);
        return jsonMapper.toJson(qtecResult);

      }
    } catch (JsonIOException e) {
      e.printStackTrace();
    }
    throw new JsonIOException("");
//        return "";
  }

  @Override
  public String getUrl() {
    return "";
  }
}
