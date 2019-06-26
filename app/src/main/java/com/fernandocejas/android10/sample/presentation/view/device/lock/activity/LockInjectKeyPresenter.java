package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.blueflybee.keystore.IKeystoreRepertory;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.callback.SaveCallback;
import com.blueflybee.keystore.data.KeyConfig;
import com.blueflybee.keystore.data.LZKey;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;

import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class LockInjectKeyPresenter implements Presenter {

  private LockInjectKeyView mLockInjectKeyView;
  @Inject
  public LockInjectKeyPresenter() {}

  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {}

  public void setView(LockInjectKeyView matchPinView) {
    this.mLockInjectKeyView = matchPinView;
  }


  public void injectKey(List<LZKey> keys, String keyRepoId) {

    KeyConfig keyConfig = new KeyConfig();
    keyConfig.setCapacity(50);
    keyConfig.setKeyLowValue(6);
    keyConfig.setKeyTooLowValue(6);
    keyConfig.setDevType(IKeystoreRepertory.TYPE_LOCK);
    KeystoreRepertory.getInstance().clear(keyRepoId);
    KeystoreRepertory.getInstance().save(keyRepoId, keyConfig, keys, new SaveCallback() {
      @Override
      public void onStart() {
        mLockInjectKeyView.onKeyInjectStart();
      }

      @Override
      public void onSuccess(String routerId) {
        mLockInjectKeyView.onKeyInjectSuccess(routerId);
      }

      @Override
      public void onFail() {
        mLockInjectKeyView.onKeyInjectFail();
      }

      @Override
      public void onProgress(int progress) {
        mLockInjectKeyView.onKeyInjectProgress(progress);
      }

      @Override
      public void onCancel(String routerId) {
        mLockInjectKeyView.onKeyInjectCancel(routerId);
      }
    });
  }

}
