package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

/**
 * 类似Handler的时间通知接口
 * Created by juyang on 16/3/22.
 */
public interface IHandlerLikeNotify {
    /**
     * 消息通知
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    public void onNotify(int what, int arg1, int arg2, Object obj);
}
