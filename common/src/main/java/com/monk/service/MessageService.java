package com.monk.service;

import com.monk.entity.Message;

public interface MessageService {

    int DONE = 0;
    int OFFLINE = 1;
    int ERROR = 2;

    /**
     * 发送消息
     * @param message  消息
     * @return
     */
    int sendMessage(Message message);
}
