package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.core.SubBlePkg;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.RemoteLockOperationRequest;
import com.qtec.router.model.rsp.RemoteLockOperationResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
public class RemoteLockOperationPresenter implements Presenter {

  private final UseCase remoteLockOperationUseCase;
  private RemoteLockOperationView mRemoteLockOperationView;

  @Inject
  public RemoteLockOperationPresenter(@Named(RouterUseCaseComm.REMOTE_LOCK_OPERATION) UseCase remoteLockOperationUseCase) {
    this.remoteLockOperationUseCase = checkNotNull(remoteLockOperationUseCase, "remoteLockOperationUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    remoteLockOperationUseCase.unsubscribe();
  }

  public void setView(RemoteLockOperationView remoteLockOperationView) {
    this.mRemoteLockOperationView = remoteLockOperationView;
  }


  /**
   * 远程操作门锁方法
   *
   * @param routerId 转发网关id
   * @param lockId   门锁设备id
   * @param pkg      操作指令数据包
   */
  public void remoteOpt(String routerId, String lockId, BlePkg pkg) {
//    mRemoteLockOperationView.showLoading();

    RemoteLockOperationRequest request = new RemoteLockOperationRequest();
    request.setCmdType(pkg.getBody().getCmd());
    BleLock bleLock = LockManager.getLockById(mRemoteLockOperationView.getContext(), lockId);
    List<SubBlePkg> subBlePkgs = new BleMapper().blePkgToSubPkgs(pkg, bleLock == null ? "" : bleLock.getKeyRepoId());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < subBlePkgs.size(); i++) {
      SubBlePkg subPkg = subBlePkgs.get(i);
      byte[] bytes = subPkg.toBytes();
      String hex = BleHexUtil.encodeHexStr(bytes);
      sb.append(hex);
    }
    String hexString = sb.toString();
    System.out.println("hexString req= " + hexString);
    byte[] bytes = BleHexUtil.hexStringToBytes(hexString);
    request.setDevid(lockId);
    request.setLen(bytes.length);
    request.setEncrypdata(new BASE64Encoder().encode(bytes));


    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_REMOTE_LOCK_OPERATION, RouterUrlPath.METHOD_POST);

    remoteLockOperationUseCase.execute(multiEncryptInfo.getCloudEncryptInfo(), new AppSubscriber<RemoteLockOperationResponse>(mRemoteLockOperationView) {


      @Override
      public void onStart() {
        super.onStart();
        if (BleMapper.BLE_CMD_GET_LOCK_STATUS.equals(request.getCmdType())) {
          mRemoteLockOperationView.onRefreshStart();
        }

      }

      @Override
      public void onError(Throwable e) {
//        super.onError(e);
        if (BleMapper.BLE_CMD_GET_LOCK_STATUS.equals(request.getCmdType())) {
          mRemoteLockOperationView.onRefreshError();
        } else if (BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE.equals(request.getCmdType())
            || BleMapper.BLE_CMD_UNLOCK_REMOTE.equals(request.getCmdType())) {
          mRemoteLockOperationView.showRemoteLockOptFailed(request.getCmdType());
        }
      }

      @Override
      protected void doNext(RemoteLockOperationResponse response) {
        String encrypdata = response.getEncrypdata();
        if (TextUtils.isEmpty(encrypdata)) {
          mRemoteLockOperationView.showRemoteLockOptFailed(request.getCmdType());
          return;
        }
        try {
          byte[] decodeBytes = new BASE64Decoder().decodeBuffer(encrypdata);
          String hexStr = BleHexUtil.encodeHexStr(decodeBytes);

          if (TextUtils.isEmpty(hexStr)) {
            mRemoteLockOperationView.showRemoteLockOptFailed(request.getCmdType());
            return;
          }
          hexStr = hexStr.replace(" ", "");
          System.out.println("hexStr = " + hexStr);
          BleMapper bleMapper = new BleMapper();
          int length = hexStr.length();
          System.out.println("length = " + length);
          int pkgCount = length % 40 == 0 ? length / 40 : length / 40 + 1;
          System.out.println("pkgCount = " + pkgCount);
          for (int i = 0; i < pkgCount; i++) {
            int endIndex = (i == pkgCount - 1) ? length : (i + 1) * 40;
            String substring = hexStr.substring(i * 40, endIndex);
            System.out.println("substring = " + substring);
            if (substring.startsWith("00")) continue;
            bleMapper.addSubPkg(BleHexUtil.hexStringToBytes(substring));
          }
          BleLock bleLock = LockManager.getLockById(mRemoteLockOperationView.getContext(), lockId);
          BlePkg blePkg = bleMapper.subPkgsToBlePkg(bleLock == null ? "" : bleLock.getKeyRepoId());
          System.out.println("blePkg = " + blePkg);
          BleBody body = blePkg.getBody();
          if (body == null) {
            mRemoteLockOperationView.showRemoteLockOptFailed(request.getCmdType());
            return;
          }
          mRemoteLockOperationView.showRemoteLockOptSuccess(blePkg, request.getCmdType());

          if (BleMapper.BLE_CMD_GET_LOCK_STATUS.equals(request.getCmdType())) {
            mRemoteLockOperationView.onRefreshComplete();
          }
        } catch (Exception e) {
          e.printStackTrace();
          mRemoteLockOperationView.showRemoteLockOptFailed(request.getCmdType());
        }

      }
    });
  }

}
