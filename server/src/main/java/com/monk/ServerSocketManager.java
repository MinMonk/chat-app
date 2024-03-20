package com.monk;

import com.monk.service.SocketManager;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServerSocketManager implements SocketManager {

    private static Map<String, ServerUserThread> onlineSocket = new ConcurrentHashMap<>();

    public static void add(String userId, ServerUserThread thread) {
        onlineSocket.put(userId, thread);
    }

    public static ServerUserThread get(String userId) {
        return onlineSocket.get(userId);
    }

    @Override
    public Socket getSocketByAccount(String userId) {
        final ServerUserThread userThread = get(userId);
        if(null == userThread){
            return null;
        }
        return userThread.getSocket();
    }

    public static void remove(String userId) {
        onlineSocket.remove(userId);
    }

    public static String listOnlineUser(){
        return onlineSocket.keySet().stream().collect(Collectors.joining(","));
    }

    public static Map<String, ServerUserThread> getOnlineSocket() {
        return onlineSocket;
    }
}
