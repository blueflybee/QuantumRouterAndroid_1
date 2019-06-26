package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.qtec.mapp.model.rsp.GetStsTokenResponse.CredentialsBean;

/**
 * @author shaojun
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-27
 */
public class OssUtil {
//    public static final String STS_SERVER = "http://qtec-route-headimg.oss-cn-shanghai.aliyuncs.com/";
  public static final String STS_SERVER = "http://headimg-3caretec.oss-cn-hangzhou.aliyuncs.com/";

//    private static final String END_POINT = "oss-cn-shanghai.aliyuncs.com";
  private static final String END_POINT = "oss-cn-hangzhou.aliyuncs.com";
//    private static final String bucket = "qtec-route-headimg";
  private static final String bucket = "headimg-3caretec";

  private static final String ACCESS_KEY_ID = "LTAICfqX2k1uGE96";
  private static final String ACCESS_KEY_SECRET = "zasHc1NhP9xVeC7lRn7IYcv7bkslRc";


  public static void upload(Context context,
                            CredentialsBean credentialsBean,
                            String remoteFilePath,
                            String localFilePath,
                            OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback,
                            OSSProgressCallback<PutObjectRequest> progressCallback) {
    OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
        credentialsBean.getAccessKeyId(), credentialsBean.getAccessKeySecret(), credentialsBean.getSecurityToken());
//    OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    OSS oss = new OSSClient(context, END_POINT, credentialProvider);

    // 构造上传请求
    PutObjectRequest put = new PutObjectRequest(bucket, remoteFilePath, localFilePath);
    // 异步上传时可以设置进度回调
    if (progressCallback != null) {
      put.setProgressCallback(progressCallback);
    }
    if (completedCallback != null) {
      OSSAsyncTask task = oss.asyncPutObject(put, completedCallback);
    }
    // task.cancel(); // 可以取消任务
    // task.waitUntilFinished(); // 可以等待任务完成

  }





}
