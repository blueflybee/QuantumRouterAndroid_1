package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.req.UploadLogcatRequest;
import com.qtec.mapp.model.rsp.UploadLogcatResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * 登录用例
 *
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class UploadLogcatUseCase extends UseCase {

  private final CloudRepository cloudRepository;
  private BufferedReader mBufferedReader;
  private SimpleDateFormat mDateFormat;

  @Inject
  public UploadLogcatUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
    this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    createLogCollector();
  }

  @Override
  protected Observable<UploadLogcatResponse> buildUseCaseObservable(final IRequest request) {
    return Observable
        .interval(1200, TimeUnit.MILLISECONDS)
        .flatMap(new Func1<Long, Observable<UploadLogcatResponse>>() {
          @Override
          public Observable<UploadLogcatResponse> call(Long aLong) {

            QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
            UploadLogcatRequest logcatRequest = (UploadLogcatRequest) encryptInfo.getData();
            logcatRequest.setTime(getCurrentTime());

            String line;
            try {
              if ((line = mBufferedReader.readLine()) != null) {
                //读出每行log信息
//                System.out.println("log==== " + line);
                clearLogCache();
                logcatRequest.setAndroidlog(line);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
            return cloudRepository.uploadLogcat(request);
          }
        });
  }

  private String getCurrentTime() {
    return mDateFormat.format(new Date(System.currentTimeMillis()));
  }

  /**
   * 开始收集日志信息
   */
  public void createLogCollector() {
//    String logFileName = sdf.format(new Date()) + ".log";// 日志文件名称
    List<String> commandList = new ArrayList<>();
    commandList.add("logcat");
//    commandList.add("-d");
    //commandList.add(LOG_PATH_INSTALL_DIR + File.separator + logFileName);
//    commandList.add(getLogPath());
    commandList.add("-v");
    commandList.add("time");
    commandList.add("*:D");

    //commandList.add("*:E");// 过滤所有的错误信息

    // 过滤指定TAG的信息
    // commandList.add("MyAPP:V");
    // commandList.add("*:S");
    try {
      Process process = Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));
      mBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//      recordLogServiceLog("start collecting the log,and log name is:"+logFileName);
      // process.waitFor();
    } catch (Exception e) {
//      Log.e(TAG, "CollectorThread == >" + e.getMessage(), e);
//      recordLogServiceLog("CollectorThread == >" + e.getMessage());
    }
  }

  /**
   * 每次记录日志之前先清除日志的缓存, 不然会在两个日志文件中记录重复的日志
   */
  private void clearLogCache() {
    List<String> commandList = new ArrayList<>();
    commandList.add("logcat");
    commandList.add("-c");
    try {
      Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));

    } catch (Exception e) {

    }
  }

}
