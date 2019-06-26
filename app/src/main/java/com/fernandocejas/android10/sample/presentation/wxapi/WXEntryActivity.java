package com.fernandocejas.android10.sample.presentation.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.utils.ToastUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/09/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

  private final int REQUEST_PAY = 100;
  private static final String APP_ID = "wxdf42c23e01fbccd7";    //这个APP_ID就是注册APP的时候生成的
  private static final String APP_SECRET = "cf23e7ff72350ea51ce56f187ba1e94b";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    share = ShareKey.getShare(this);
    //如果没回调onResp，八成是这句没有写
    WXAPIFactory.createWXAPI(getContext(), APP_ID, true).handleIntent(getIntent(), this);
  }

  // 微信发送请求到第三方应用时，会回调到该方法
  @Override
  public void onReq(BaseReq req) {
  }

  // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
  //app发送消息给微信，处理返回消息的回调
//  @Override
//  public void onResp(BaseResp resp) {
//    switch (resp.errCode) {
//      case BaseResp.ErrCode.ERR_AUTH_DENIED:
//      case BaseResp.ErrCode.ERR_USER_CANCEL:
//        switch (resp.getType()) {
//          case RETURN_MSG_TYPE_SHARE:
//            ToastUtils.showShort("微信分享取消");
//            finish();
//            break;
//
//        }
//        break;
//
//      case BaseResp.ErrCode.ERR_OK:
//        switch (resp.getType()) {
//          case RETURN_MSG_TYPE_SHARE:
//            finish();
//            break;
//        }
//        break;
//    }
//  }

  @Override
  public void onResp(BaseResp resp) { //在这个方法中处理微信传回的数据
    //形参resp 有下面两个个属性比较重要
    //1.resp.errCode
    //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
    switch (resp.errCode) { //根据需要的情况进行处理
      case BaseResp.ErrCode.ERR_OK:
        //正确返回
        ToastUtils.showShort("微信分享成功");
        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        ToastUtils.showShort("微信分享取消");
        break;
      case BaseResp.ErrCode.ERR_AUTH_DENIED:
        //认证被否决
        ToastUtils.showShort("微信分享失败");
        break;
      case BaseResp.ErrCode.ERR_SENT_FAILED:
        //发送失败
        ToastUtils.showShort("微信分享失败");
        break;
      case BaseResp.ErrCode.ERR_UNSUPPORT:
        //不支持错误
        break;
      case BaseResp.ErrCode.ERR_COMM:
        //一般错误
        ToastUtils.showShort("微信分享失败");
        break;
      default:
        //其他不可名状的情况
        ToastUtils.showShort("微信分享失败");
        break;
    }

    finish();
  }

}
