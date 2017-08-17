package com.lsjr.zizi.chat.inter;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/31 9:58
 * <p>
 * 聊天的点击对应的事件操作
 */

public interface OnChatEventListener {
    void stopVoicePlay();

    void sendText(String text);

    void sendGif(String text);

    void sendVoice(String filePath, int timeLen);

}
