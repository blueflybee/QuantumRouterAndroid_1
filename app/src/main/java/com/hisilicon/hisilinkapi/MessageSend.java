package com.hisilicon.hisilinkapi;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


public class MessageSend {
	private static final String TAG = "MessageSend";
	private Handler mHandler;

    static {
        System.loadLibrary("HisiLink");
    }

    public MessageSend(Context context){
    }

    public void multiCastThread(){
    	HandlerThread handlerThread = new HandlerThread("MultiSocketA");
        handlerThread.start();
        mHandler =  new Handler(handlerThread.getLooper());
        mHandler.post(mRunnable);


    }
    public void stopMultiCast(){
    	HisiLibApi.stopMulticast();
    }
    private Runnable mRunnable = new Runnable() {
        public void run() {
            Log.d(TAG, "Multicast run...");
            try {
            	HisiLibApi.startMulticast();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
