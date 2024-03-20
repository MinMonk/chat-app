package com.monk.manager;

import com.monk.ServerSocketManager;
import com.monk.entity.Message;
import com.monk.service.BasicMessageService;
import com.monk.service.MessageService;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerMessageServiceImpl extends BasicMessageService {

    private ServerSocketManager socketManager;

    public ServerMessageServiceImpl() {
        this.socketManager = new ServerSocketManager();
    }

    private static Map<String, List<Message>> offlineMessageCache = new ConcurrentHashMap<>();


    @Override
    public int sendMessage(Message message) {
        final Socket socket = socketManager.getSocketByAccount(message.getReceiver().getAccount());
        if (null == socket) {
            addOfflineMessage(message);
            return OFFLINE;

        }

        int sendResult = super.doSend(socket, message);
        if (sendResult == MessageService.ERROR) {
            log.info("发送消息失败, {}", message);
        }

        return sendResult;
    }

    public static void addOfflineMessage(Message msg) {
        final String receiverAccount = msg.getReceiver().getAccount();
        List<Message> messageList = offlineMessageCache.get(receiverAccount);
        if (null == messageList || messageList.size() <= 0) {
            messageList = new ArrayList<>();
        }
        messageList.add(msg);
        offlineMessageCache.put(receiverAccount, messageList);
    }


    public static List<Message> readOfflineMessage(String account) {
        return offlineMessageCache.get(account);
    }
}
