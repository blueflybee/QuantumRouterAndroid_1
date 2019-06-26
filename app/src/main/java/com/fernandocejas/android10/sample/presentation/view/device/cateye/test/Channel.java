package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.view.Surface;
import android.view.SurfaceView;

/**
 * 云视通协议：简单的通道集合类
 *
 * @author juyang
 */
public class Channel {
    //所属的设备
    private Device parent;
    //窗口索引，从0开始
    private int index;
    //设备通道，从 1 开始
    private int channel;
    //昵称
    private String nickName;
    //连接状态
    private boolean isConnecting;
    private boolean isConnected;
    private boolean isPaused;

    //对讲状态
    private boolean isSingleVoice = false;//是否单向对讲
    private boolean isVoiceCalling = false;//是否正在对讲
    private boolean isSendingVoice = false;//是否正在发送语音

    //只发关键帧
    private boolean sendCMD = false;

    //同意文本聊天
    private boolean agreeTextChat;

    //码流index
    private int streamIndex;

    //自动巡航功能是否开启
    private boolean isAuto;

    private boolean isRemotePlay;

    private Surface surface;
    private SurfaceView surfaceView;

    public Channel(Device device, int index, int channel, boolean isConnected,
                   boolean isRemotePlay, String nickName) {
        this.parent = device;
        this.index = index;
        this.channel = channel;
        this.isConnected = isConnected;
        this.isRemotePlay = isRemotePlay;
        this.nickName = nickName;

        isPaused = true;
    }

    public Channel() {
        this.index = 0;
        this.channel = 0;

        this.isPaused = true;
        this.isConnected = false;
        this.isRemotePlay = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isRemotePlay() {
        return isRemotePlay;
    }

    public void setRemotePlay(boolean isRemotePlay) {
        this.isRemotePlay = isRemotePlay;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Device getParent() {
        return parent;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    public void setIsConnecting(boolean isConnecting) {
        this.isConnecting = isConnecting;
    }

    public boolean isVoiceCalling() {
        return isVoiceCalling;
    }

    public void setVoiceCalling(boolean voiceCalling) {
        isVoiceCalling = voiceCalling;
    }

    public boolean isSendingVoice() {
        return isSendingVoice;
    }

    public void setSendingVoice(boolean sendingVoice) {
        isSendingVoice = sendingVoice;
    }

    public boolean isSendCMD() {
        return sendCMD;
    }

    public void setSendCMD(boolean sendCMD) {
        this.sendCMD = sendCMD;
    }

    public boolean isAgreeTextChat() {
        return agreeTextChat;
    }

    public void setAgreeTextChat(boolean agreeTextChat) {
        this.agreeTextChat = agreeTextChat;
    }

    public int getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(int streamIndex) {
        this.streamIndex = streamIndex;
    }

    public boolean isSingleVoice() {
        return isSingleVoice;
    }

    public void setSingleVoice(boolean singleVoice) {
        isSingleVoice = singleVoice;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder(1024);
        sBuilder.append("Channel-").append((channel < 0) ? "X" : channel)
                .append(", window = ").append(index).append(": isConnected = ")
                .append(isConnected).append(", isPaused: ").append(isPaused)
                .append(", surface = ")
                .append((null != surface) ? surface.hashCode() : "null")
                .append(", hashcode = ").append(hashCode());
        return sBuilder.toString();
    }

}
