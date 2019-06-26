package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.AndroidPopupActivity;

import java.util.Map;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/30
 *      desc: 首页接受邀请弹窗
 *      version: 1.0
 * </PRE>
 */

public class HwPopupPushActivity extends AndroidPopupActivity {
  static final String TAG = HwPopupPushActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /**
   * 实现通知打开回调方法，获取通知相关信息
   *
   * @param title   标题
   * @param summary 内容
   * @param extMap  额外参数
   */
  @Override
  protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
    Log.d(TAG,"onSysNoticeOpened, title: " + title + ", summary: " + summary + ", extMap: " + extMap);
  }
}
