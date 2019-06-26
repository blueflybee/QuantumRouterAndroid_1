package com.fernandocejas.android10.sample.presentation.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

public class MyProgressDialog extends ProgressDialog
{

  public MyProgressDialog(Context context)
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
        return true;
      }
      return false;
    });
  }

}
