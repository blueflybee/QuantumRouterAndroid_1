package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

/**
*进入子文件夹取消的时候 恢复 remoteCurrentSharePath
*
* @param
* @return
*/
public class MyProgressDialogForFetch extends ProgressDialog
{

  public MyProgressDialogForFetch(Context context,String remoteCurrentSharePath)
  {
    super(context);
    this.setCancelable(false);
    this.setOnKeyListener((dialog, keyCode, event) -> {
      if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)
      {
        return true;
      }
      /*else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
      {
        dialog.dismiss();
        return true;
      }*/
      return false;
    });
  }

}
