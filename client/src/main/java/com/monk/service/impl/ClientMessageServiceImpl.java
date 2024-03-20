package com.monk.service.impl;

import com.monk.ClientSocketManager;
import com.monk.entity.Message;
import com.monk.service.BasicMessageService;
import com.monk.service.SocketManager;

import java.net.Socket;

public class ClientMessageServiceImpl extends BasicMessageService {

    private SocketManager socketManager;

    public ClientMessageServiceImpl() {
        this.socketManager = new ClientSocketManager();
    }

    @Override
    public int sendMessage(Message message) {
        final Socket socket = socketManager.getSocketByAccount(message.getSender().getAccount());
        if (null == socket) {
            return ERROR;
        }
        return super.doSend(socket, message);
    }
}
