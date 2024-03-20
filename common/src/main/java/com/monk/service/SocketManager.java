package com.monk.service;

import java.net.Socket;

public interface SocketManager {

    Socket getSocketByAccount(String account);
}
