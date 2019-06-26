/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation.exception;

import android.content.Context;

import com.fernandocejas.android10.sample.data.exception.KeyInvalidException;
import com.fernandocejas.android10.sample.data.exception.LoginInvalidException;
import com.fernandocejas.android10.sample.data.exception.NetworkConnectionException;
import com.fernandocejas.android10.sample.presentation.R;
import com.orhanobut.logger.Logger;
import java.io.IOException;

/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {



  private ErrorMessageFactory() {
    //empty
  }

  /**
   * Creates a String representing an error message.
   *
   * @param context Context needed to retrieve string resources.
   * @param exception An exception used as a condition to retrieve the correct error message.
   * @return {@link String} an error message.
   */
  public static String create(Context context, Exception exception) {
    String message = "";

    if (exception.getClass() == NetworkConnectionException.class) {
      message = "没有网络连接，请先连接网络";
    }

    else if (exception.getClass() == IOException.class) {
      message = exception.getMessage();
    }

    else if (exception.getClass() == LoginInvalidException.class) {
      message = exception.getMessage();
    }

    else if (exception.getClass() == KeyInvalidException.class) {
      message = exception.getMessage();
    }

    return message;
  }
}
