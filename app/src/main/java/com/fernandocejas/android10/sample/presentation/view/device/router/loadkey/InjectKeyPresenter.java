package com.fernandocejas.android10.sample.presentation.view.device.router.loadkey;

import android.content.Intent;

import com.blueflybee.keystore.IKeystoreRepertory;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.callback.SaveCallback;
import com.blueflybee.keystore.data.KeyConfig;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.Util;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo.KeyBean;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import sun.misc.BASE64Decoder;

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
public class InjectKeyPresenter implements Presenter {

  private InjectKeyView mInjectKeyView;

  @Inject
  public InjectKeyPresenter() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
  }

  public void setView(InjectKeyView matchPinView) {
    this.mInjectKeyView = matchPinView;
  }


  public void injectKey(Intent intent) {
    try {
      List<LZKey> lzKeys = new ArrayList<>();

      LZKeyInfo<KeyBean> data = (LZKeyInfo<KeyBean>) intent.getSerializableExtra(Navigator.EXTRA_LZKEYINFO);
      List<KeyBean> keylist = data.getKeylist();
      BASE64Decoder base64Decoder = new BASE64Decoder();
      for (KeyBean keyBean : keylist) {
        LZKey lzKey = new LZKey();
        lzKey.setId(keyBean.getKeyid());
        byte[] bytes = base64Decoder.decodeBuffer(keyBean.getKey());
        String key = Util.encodeHexString(bytes);
        lzKey.setKey(key);
        System.out.println("lzKey = " + lzKey);
        lzKeys.add(lzKey);
      }

      if (lzKeys.isEmpty()) {
        mInjectKeyView.onKeyInjectFail();
        return;
      }

      String serialnum = GlobleConstant.getgDeviceId();
//      System.out.println("serialnum = " + serialnum);
      String keyRepoId = serialnum + "_" + PrefConstant.getUserUniqueKey("0");

      KeyConfig keyConfig = new KeyConfig();
      keyConfig.setCapacity(200);
      keyConfig.setKeyLowValue(50);
      keyConfig.setKeyTooLowValue(10);
      keyConfig.setDevType(IKeystoreRepertory.TYPE_ROUTER);
      KeystoreRepertory.getInstance().clear(keyRepoId);
      KeystoreRepertory.getInstance().save(keyRepoId, keyConfig, lzKeys, new SaveCallback() {
        @Override
        public void onStart() {
          mInjectKeyView.onKeyInjectStart();
        }

        @Override
        public void onSuccess(String routerId) {
          mInjectKeyView.onKeyInjectSuccess(routerId);
        }

        @Override
        public void onFail() {
          mInjectKeyView.onKeyInjectFail();
        }

        @Override
        public void onProgress(int progress) {
          mInjectKeyView.onKeyInjectProgress(progress);
        }

        @Override
        public void onCancel(String routerId) {
          mInjectKeyView.onKeyInjectCancel(routerId);
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
      mInjectKeyView.onKeyInjectFail();
    }
  }

}
