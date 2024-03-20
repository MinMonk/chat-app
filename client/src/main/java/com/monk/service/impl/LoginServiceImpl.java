package com.monk.service.impl;

import com.monk.ClientSocketManager;
import com.monk.constant.ChatConstant;
import com.monk.entity.Message;
import com.monk.entity.User;
import com.monk.enums.CommandLevelOne;
import com.monk.enums.CommandLevelTwo;
import com.monk.service.ClientUserThread;
import com.monk.service.CommandService;
import com.monk.service.MessageService;
import com.monk.util.SystemInUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 登录指令的实现类
 *
 * @author monk
 */
@Slf4j
public class LoginServiceImpl implements CommandService {

    private MessageService messageService = new ClientMessageServiceImpl();

    @Override
    public void execute() {
        System.out.println("请输入账号:");
        final String account = SystemInUtils.readString();
        System.out.println("请输入密码:");
        final String password = SystemInUtils.readString();

        try {
            // 将用户名/密码发送到服务端验证
            User user = new User(account, password);
            Socket socket = new Socket(InetAddress.getLocalHost(), ChatConstant.SERVER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new User(account, password));

            // 读取服务端的验证结果
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msgObj = (Message) ois.readObject();
            if (CommandLevelOne.LOGIN_SUCCESS.getType().equals(msgObj.getCommandType())) {
                // 登录成功
                System.out.println("欢迎" + account);

                // 启动客户端用户线程,让其在后端监听服务端传递的消息
                ClientUserThread userThread = new ClientUserThread(socket);
                ClientSocketManager.add(account, userThread);
                userThread.start();

                // 显示二级菜单
                boolean loop = true;
                while (loop) {
                    System.out.println("\n===============二级菜单  用户[" + account + "]===================");
                    System.out.println("1. 获取在线用户列表");
                    System.out.println("2. 群发消息");
                    System.out.println("3. 私聊");
                    System.out.println("4. 发送文件");
                    System.out.println("9. 退出系统");
                    System.out.println("请输入对应的指令:");
                    final CommandLevelTwo command = CommandLevelTwo.resolveByType(SystemInUtils.readInt());

                    // 构造消息对象
                    Message message = new Message();
                    message.setSender(user);
                    message.setReceiver(User.server);
                    String receiver = null;
                    String msg = null;
                    switch (command) {
                        case ONLINE_USER_REQUEST:
                            message.setCommandType(CommandLevelTwo.ONLINE_USER_REQUEST.getType());
                            messageService.sendMessage(message);
                            break;
                        case GROUP_MSG:
                            System.out.println("请输入要群发的消息:");
                            msg = SystemInUtils.readString();
                            message.setCommandType(CommandLevelTwo.GROUP_MSG.getType());
                            message.setMsg(msg);
                            messageService.sendMessage(message);
                            break;
                        case SINGLE_MSG:
                            System.out.println("请输入私聊对象的账号:");
                            receiver = SystemInUtils.readString();
                            System.out.println("请输入要发送的消息内容:");
                            msg = SystemInUtils.readString();
                            message.setCommandType(CommandLevelTwo.SINGLE_MSG.getType());
                            message.setMsg(msg);
                            message.setReceiver(new User(receiver));

                            messageService.sendMessage(message);
                            break;
                        case SEND_FILE:
                            System.out.println("请输入对方账号:");
                            receiver = SystemInUtils.readString();
                            System.out.println("请选择要发送的文件:");
                            String srcFile = SystemInUtils.readString();
                            message.setCommandType(CommandLevelTwo.SEND_FILE.getType());
                            message.setMsg(srcFile);
                            message.setReceiver(new User(receiver));

                            // 将文件读成字节流
                            final byte[] bytes = FileUtils.readFileToByteArray(new File(srcFile));
                            message.setBytes(bytes);
                            messageService.sendMessage(message);
                            break;
                        case LOGOUT:
                            message.setCommandType(CommandLevelTwo.LOGOUT.getType());
                            messageService.sendMessage(message);
                            ClientSocketManager.remove(account);

                            System.exit(0);
                            System.out.println("系统即将退出,欢迎下次使用...");
                            loop = false;
                            break;
                        default:
                            System.out.println("二级指令:" + command);
                            break;
                    }
                }

            } else {
                log.info("用户[" + account + "]登录失败");
                socket.close();
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
