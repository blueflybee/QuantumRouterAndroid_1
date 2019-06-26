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
package com.fernandocejas.android10.sample.presentation.interactor;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.exception.LoginInvalidException;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultSubscriber;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;

import java.util.List;

import rx.exceptions.CompositeException;

/**
 * Default subscriber base class to be used whenever you want default error handling.
 */
public abstract class AppSubscriber<T> extends DefaultSubscriber<T> {

  private LoadDataView loadDataView;

  public AppSubscriber(LoadDataView loadDataView) {
    this.loadDataView = loadDataView;
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onCompleted() {
    super.onCompleted();
  }

  @Override
  public void onError(Throwable e) {
    super.onError(e);

    handleExceptionOnUI((Exception) handleCompositeException(e));

    handleLoginInvalid(e);

  }

  protected void handleExceptionOnUI(Exception singleThrowable) {
    if (loadDataView != null) {
      loadDataView.hideLoading();
      String errorMessage = ErrorMessageFactory.create(loadDataView.getContext(),
          new DefaultErrorBundle(singleThrowable).getException());
      System.out.println("errorMessage = " + errorMessage);
      loadDataView.onError(errorMessage);
    }
  }

  protected Throwable handleCompositeException(Throwable e) {
    Throwable singleThrowable;
    if (e instanceof CompositeException) {
      CompositeException compositeException = (CompositeException) e;
      singleThrowable = compositeException.getExceptions().get(0);
    } else {
      singleThrowable = e;
    }
    return singleThrowable;
  }

  protected void handleLoginInvalid(Throwable e) {
    if (loadDataView == null) return;
    loadDataView.hideLoading();
    if (e instanceof CompositeException) {
      CompositeException compositeException = (CompositeException) e;
      List<Throwable> exceptions = compositeException.getExceptions();
      for (Throwable exception : exceptions) {
        showLoginInvalidAndClearToken(exception);
      }
    } else {
      showLoginInvalidAndClearToken(e);
    }

  }

  private void showLoginInvalidAndClearToken(Throwable e) {
    if (e.getClass() == LoginInvalidException.class) {
      PrefConstant.putAppToken("");
      ((AndroidApplication) loadDataView.getContext().getApplicationContext()).unbindBleService();
      loadDataView.showLoginInvalid();
    }
  }

  @Override
  public void onNext(T t) {
    super.onNext(t);
    if (loadDataView != null) {
      loadDataView.hideLoading();
    }
    doNext(t);
  }

  protected abstract void doNext(T t);

}
