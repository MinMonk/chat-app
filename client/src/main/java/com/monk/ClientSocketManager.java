package com.monk;

import com.monk.service.ClientUserThread;
import com.monk.service.SocketManager;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSocketManager implements SocketManager {

    private static ConcurrentHashMap<String, ClientUserThread> onlineSocket = new ConcurrentHashMap<>();

    public static void add(String userId, ClientUserThread thread) {
        onlineSocket.put(userId, thread);
    }

    public static ClientUserThread get(String userId) {
        return onlineSocket.get(userId);
    }

    public static void remove(String userId) {
        onlineSocket.remove(userId);
    }

    @Override
    public Socket getSocketByAccount(String userId) {
        final ClientUserThread clientUserThread = get(userId);
        if (null == clientUserThread) {
            return null;
        }
        return clientUserThread.getSocket();
    }
}
