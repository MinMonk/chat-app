package com.monk.service;

import com.monk.entity.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public abstract class BasicMessageService implements MessageService{

    @Override
    public abstract int sendMessage(Message message);

    protected int doSend(Socket socket, Message msg) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
            return DONE;
        } catch (IOException e) {
            log.error("", e);
            return ERROR;
        }
    }
}
