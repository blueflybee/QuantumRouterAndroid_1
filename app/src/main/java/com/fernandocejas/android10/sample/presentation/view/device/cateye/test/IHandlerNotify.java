package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

/**
 * 活动内部Handler事件通知接口
 * Created by juyang on 16/3/22.
 */
public interface IHandlerNotify {

    /**
     * 消息通知
     *
     * @param what 分类
     * @param arg1 参数1
     * @param arg2 参数2
     * @param obj  附加对象
     */
    public void onHandler(int what, int arg1, int arg2, Object obj);
}
