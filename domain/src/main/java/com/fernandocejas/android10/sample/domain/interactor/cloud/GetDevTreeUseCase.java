package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class GetDevTreeUseCase extends UseCase {

  private final CloudRepository cloudRepository;
  private String deviceType = "";

  @Inject
  public GetDevTreeUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override
  protected Observable<List<GetDevTreeResponse<List<DeviceBean>>>> buildUseCaseObservable(IRequest request) {
    return cloudRepository.getDevTree(request)
        .map(new Func1<List<GetDevTreeResponse<List<DeviceBean>>>, List<GetDevTreeResponse<List<DeviceBean>>>>() {
          @Override
          public List<GetDevTreeResponse<List<DeviceBean>>> call(List<GetDevTreeResponse<List<DeviceBean>>> devices) {
            if (deviceType.isEmpty()) return devices;
            ArrayList<GetDevTreeResponse<List<DeviceBean>>> devTreeResponses = new ArrayList<>();
            for (GetDevTreeResponse<List<DeviceBean>> device : devices) {
              String devType = device.getDeviceType();
              if (!deviceType.contains(devType)) continue;
              devTreeResponses.add(device);
            }
            return devTreeResponses;
          }
        });
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }
}
