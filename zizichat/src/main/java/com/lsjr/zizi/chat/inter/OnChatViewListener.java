package com.lsjr.zizi.chat.inter;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/31 9:57
 *
 *  聊天View对应的类型点击事件
 *
 */

public interface OnChatViewListener {

    void clickPhoto();

    void clickCamera();

    void clickVideo();

    void clickAudio();

    void clickFile();

    void clickLocation();

    void clickCard();
}
