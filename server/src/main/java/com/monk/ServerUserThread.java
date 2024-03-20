package com.monk;

import com.monk.entity.Message;
import com.monk.entity.User;
import com.monk.enums.CommandLevelTwo;
import com.monk.manager.ServerMessageServiceImpl;
import com.monk.service.MessageService;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.net.Socket;

@Slf4j
public class ServerUserThread extends Thread {

    private Socket socket;
    private MessageService messageService;


    public Socket getSocket() {
        return socket;
    }

    public ServerUserThread(Socket socket) {
        this.socket = socket;
        this.messageService = new ServerMessageServiceImpl();
    }

    @Override
    public void run() {
        boolean loop = true;
        while (loop) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message msgObj = (Message) ois.readObject();
                final String senderAccount = msgObj.getSender().getAccount();
                log.info("收到的消息:{}", msgObj);

                // 初始化消息对象
                Message message = new Message();
                message.setReceiver(msgObj.getSender());
                message.setSender(User.server);

                switch (CommandLevelTwo.resolveByType(msgObj.getCommandType())) {
                    case ONLINE_USER_REQUEST:
                        String onlineUser = ServerSocketManager.listOnlineUser();
                        message.setMsg(onlineUser);
                        message.setCommandType(CommandLevelTwo.ONLINE_USER_RESPONSE.getType());
                        messageService.sendMessage(message);
                        break;
                    case GROUP_MSG:
                        // 将消息发送所有的在线用户(非自己)
                        ServerSocketManager.getOnlineSocket().entrySet().forEach(entry -> {
                            if (!entry.getKey().equals(senderAccount)) {
                                message.setCommandType(CommandLevelTwo.GROUP_MSG.getType());
                                message.setMsg(msgObj.getMsg());
                                message.setReceiver(new User(entry.getKey()));
                                message.setSender(new User(senderAccount));
                                messageService.sendMessage(message);
                            }
                        });
                        break;
                    case SINGLE_MSG:
                        // 将消息转发给对应的用户
                        message.setCommandType(CommandLevelTwo.SINGLE_MSG.getType());
                        message.setSender(msgObj.getSender());
                        message.setReceiver(msgObj.getReceiver());
                        message.setMsg(msgObj.getMsg());
                        messageService.sendMessage(message);
                        break;
                    case SEND_FILE:
                        // 将文件字节流转发给用户
                        message.setCommandType(CommandLevelTwo.SEND_FILE.getType());
                        message.setSender(msgObj.getSender());
                        message.setReceiver(msgObj.getReceiver());
                        message.setMsg(msgObj.getMsg());
                        message.setBytes(msgObj.getBytes());
                        messageService.sendMessage(message);
                        break;
                    case LOGOUT:
                        System.out.println("用户[" + senderAccount + "]已退出");
                        ServerSocketManager.remove(senderAccount);
                        socket.close();
                        loop = false;
                        break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
