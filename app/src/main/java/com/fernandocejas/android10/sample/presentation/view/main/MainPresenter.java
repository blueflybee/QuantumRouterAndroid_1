package com.fernandocejas.android10.sample.presentation.view.main;

import android.os.Build;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.IKeystoreRepertory;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.callback.SaveCallback;
import com.blueflybee.keystore.data.KeyConfig;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.Util;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockInjectKeyView;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetKeyRequest;
import com.qtec.router.model.rsp.GetKeyResponse;
import com.qtec.router.model.rsp.GetKeyResponse.KeyBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import sun.misc.BASE64Decoder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
class MainPresenter implements Presenter {

  private final UseCase getKeyUseCase;
  private MainView mMainView;

  private boolean isGettingRouterKey = false;

  @Inject
  public MainPresenter(@Named(RouterUseCaseComm.GET_KEY) UseCase getKeyUseCase) {
    this.getKeyUseCase = checkNotNull(getKeyUseCase, "getKeyUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getKeyUseCase.unsubscribe();
  }

  public void setView(MainView mainView) {
    this.mMainView = mainView;
  }

  void getRouterKeys(String repoId, int number) {
    isGettingRouterKey = true;
    GetKeyRequest request = new GetKeyRequest();
    request.setKeynumber(number);
    request.setDevicename(Build.MODEL);
    request.setKeytype(1);
    request.setRequestid(10);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_GET_KEY, RouterUrlPath.METHOD_GET);

//    mMainView.showLoading();
    getKeyUseCase.execute(multiEncryptInfo, new AppSubscriber<GetKeyResponse<List<KeyBean>>>(mMainView) {

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        isGettingRouterKey = false;

      }

      @Override
      protected void doNext(GetKeyResponse<List<KeyBean>> response) {
        saveRouterKeys(repoId, response);
        isGettingRouterKey = false;
      }
    });

  }

  private void saveRouterKeys(String repoId, GetKeyResponse<List<KeyBean>> response) {
    try {
      List<LZKey> lzKeys = new ArrayList<>();

      List<KeyBean> keylist = response.getKeylist();
      if (keylist == null) return;

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

      KeyConfig keyConfig = new KeyConfig();
      keyConfig.setCapacity(200);
      keyConfig.setKeyLowValue(50);
      keyConfig.setKeyTooLowValue(10);
      keyConfig.setDevType(IKeystoreRepertory.TYPE_ROUTER);
      KeystoreRepertory.getInstance().save(repoId, keyConfig, lzKeys, new SaveCallback() {
        @Override
        public void onStart() {
//          ToastUtils.showShort("补充密钥中...");
        }

        @Override
        public void onSuccess(String routerId) {
//          ToastUtils.showShort("密钥补充完成");
        }

        @Override
        public void onFail() {
//          ToastUtils.showShort("密钥补充失败");
        }

        @Override
        public void onProgress(int progress) {
        }

        @Override
        public void onCancel(String routerId) {
//          ToastUtils.showShort("密钥补充取消");
        }
      });

    } catch (Exception e) {
      e.printStackTrace();

    }

  }

  boolean isGettingRouterKey() {
    return isGettingRouterKey;
  }

  void injectLockKey(List<LZKey> keys, String lockMac) {
    BleLock bleLock = LockManager.getLock(mMainView.getContext(), lockMac);
    if (bleLock == null) return;
    KeystoreRepertory.getInstance().clearTmpKeys(bleLock.getKeyRepoId());

    KeyConfig keyConfig = new KeyConfig();
    keyConfig.setCapacity(55);
    keyConfig.setKeyLowValue(6);
    keyConfig.setKeyTooLowValue(6);
    keyConfig.setDevType(IKeystoreRepertory.TYPE_LOCK);

    KeystoreRepertory.getInstance().save(bleLock.getKeyRepoId(), keyConfig, keys, new SaveCallback() {
      @Override
      public void onStart() {
        ((LockInjectKeyView) mMainView).onKeyInjectStart();

      }

      @Override
      public void onSuccess(String lockId) {
        ((LockInjectKeyView) mMainView).onKeyInjectSuccess(lockId);
      }

      @Override
      public void onFail() {
        ((LockInjectKeyView) mMainView).onKeyInjectFail();
      }

      @Override
      public void onProgress(int progress) {
        ((LockInjectKeyView) mMainView).onKeyInjectProgress(progress);
      }

      @Override
      public void onCancel(String lockId) {
        ((LockInjectKeyView) mMainView).onKeyInjectCancel(lockId);
      }
    });
  }

}
