package com.fernandocejas.android10.sample.presentation.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;

/**
 * 该类用于显示提示信息
 */
public class ToastUtil {

	public static void dismiss() {
		if (mToast == null)
			return;
		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 14)
			mToast.cancel();
	}

	public static void show(String msg) {
		showToast(msg, Gravity.CENTER_VERTICAL);
	}

	public static void showBottom(String msg) {
		showToast(msg, Gravity.BOTTOM);
	}

	public static void show(int msg) {
		showToast(msg, Gravity.CENTER_VERTICAL);
	}

	public static void showBottom(int msg) {
		showToast(msg, Gravity.BOTTOM);
	}

	private static void showToast(final String msg, final int gravity) {
		startShow(msg, gravity);
	}

	private static void showToast(final int msg, final int gravity) {
		startShow(getString(msg), gravity);
	}

	private static String getString(final int msg) {
		return AndroidApplication.mApplicationContext.getString(msg);
	}

	private static void startShow(final String msg, final int gravity) {
		new Thread(new Runnable() {
			public void run() {
				postShow(msg, gravity);
			}

		}).start();
	}

	private static void postShow(final String msg, final int gravity) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (mSynObj) {
					if (mToast == null) {
						mToast = Toast.makeText(
								AndroidApplication.mApplicationContext, msg,
								Toast.LENGTH_SHORT);
					}
					if (Integer.valueOf(android.os.Build.VERSION.SDK) < 12)
						mToast.cancel();
					mToast.setText(msg);
					mToast.setDuration(Toast.LENGTH_SHORT);
					mToast.setGravity(gravity, 0, 0);
					mToast.show();
				}
			}
		});
	}

	private static Toast mToast = null;
	private static final Handler mHandler = new Handler(Looper.getMainLooper());
	private static final Object mSynObj = new Object();

}
