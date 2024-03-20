package com.monk;

import com.monk.constant.ChatConstant;
import com.monk.entity.Message;
import com.monk.entity.User;
import com.monk.enums.CommandLevelOne;
import com.monk.enums.CommandLevelTwo;
import com.monk.manager.ServerMessageServiceImpl;
import com.monk.service.MessageService;
import com.monk.service.UserService;
import com.monk.util.SystemInUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Hello world!
 */
@Slf4j
public class ChatServer {


    public static void main(String[] args) {
        try {

            UserService userService = new UserService();
            MessageService messageService = new ServerMessageServiceImpl();

            final ServerSocket server = new ServerSocket(ChatConstant.SERVER_PORT);
            log.info("服务器启动成功,正在监听" + ChatConstant.SERVER_PORT + "端口....");

            // 推送新闻线程
            new Thread(() -> {
                while (true) {
                    System.out.println("请输入想推送的新闻(输入[exit]退出推送):");
                    final String content = SystemInUtils.readString();
                    if ("exit".equals(content)) {
                        break;
                    }
                    ServerSocketManager.getOnlineSocket().entrySet().forEach(entry -> {
                        Message message = new Message();
                        message.setSender(User.server);
                        message.setReceiver(new User(entry.getKey()));
                        message.setMsg(content);
                        message.setCommandType(CommandLevelTwo.NEWS.getType());
                        messageService.sendMessage(message);
                    });
                }

            }).start();


            while (true) {
                Socket socket = server.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                log.info("用户正在请求登录, {}", user);

                // 校验用户密码是否正确
                Message message = new Message();
                message.setReceiver(user);
                message.setSender(User.server);
                if (userService.verifyUser(user)) {
                    // 启动一个会话线程
                    ServerUserThread serverThread = new ServerUserThread(socket);
                    ServerSocketManager.add(user.getAccount(), serverThread);
                    serverThread.start();

                    // 向客户端发送消息,登录成功
                    message.setCommandType(CommandLevelOne.LOGIN_SUCCESS.getType());
                    messageService.sendMessage(message);

                    // 读取离线信息
                    final List<Message> offlineMessage = ServerMessageServiceImpl.readOfflineMessage(user.getAccount());
                    if(null != offlineMessage && offlineMessage.size() > 0){
                        offlineMessage.stream().forEach(msg -> {
                            messageService.sendMessage(msg);
                        });
                    }
                } else {
                    // 向客户端发送消息,登录失败
                    message.setCommandType(CommandLevelOne.LOGIN_FAILED.getType());
                    messageService.sendMessage(message);

                    // 关闭socket资源
                    ois.close();
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


}
