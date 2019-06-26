package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;

public class MyProgressDialogForPreview extends ProgressDialog
{

  public MyProgressDialogForPreview(Context context, SambaUtils.DownloadTaskForPreview filePreviewTask)
  {
    super(context);
    this.setCancelable(false);

    this.setOnKeyListener((dialog, keyCode, event) -> {
      if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)
      {
        return true;
      }
      else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
      {
        dialog.dismiss();
        System.out.println("dialog 取消了异步任务");
        filePreviewTask.cancel(true);
        Toast.makeText(context, "预览已取消", Toast.LENGTH_SHORT).show();
        return true;
      }
      return false;
    });
  }



}
