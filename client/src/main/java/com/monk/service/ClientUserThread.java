package com.monk.service;

import com.monk.entity.Message;
import com.monk.enums.CommandLevelTwo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Scanner;

@Slf4j
public class ClientUserThread extends Thread {

    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public ClientUserThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean loop = true;
        while (loop) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message msgObj = (Message) ois.readObject();
                final String senderAccount = msgObj.getSender().getAccount();
                final String receiverAccount = msgObj.getReceiver().getAccount();
                final String msg = msgObj.getMsg();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sendDateStr = sdf.format(msgObj.getSendDate());

                switch (CommandLevelTwo.resolveByType(msgObj.getCommandType())) {
                    case ONLINE_USER_RESPONSE:
                        System.out.println("=============在线用户列表=============");
                        Arrays.stream(msg.split(","))
                                .forEach(e -> System.out.println(e));
                        break;
                    case GROUP_MSG:
                        System.out.println(sendDateStr + " - [" + senderAccount + "]发送了群消息: " + msg);
                        break;
                    case SINGLE_MSG:
                        System.out.println(sendDateStr + " - [" + senderAccount + "]对[" + receiverAccount + "]说: " + msg);
                        break;
                    case SEND_FILE:
                        this.saveFile(msgObj);
                        break;
                    case NEWS:
                        System.out.println("服务器发布了公告:" + msgObj.getMsg());
                        break;
                    default:
                        System.out.println("用户已退出");
                        break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    void saveFile(Message msgObj){
        try {
            final String senderAccount = msgObj.getSender().getAccount();
            System.out.print("[" + senderAccount + "]给你发送了一个文件,请选择文件的保存地址:");
            Scanner scanner = new Scanner(System.in);
            String destFile = scanner.nextLine();
            System.out.println("destFile--->" + destFile);
            FileUtils.writeByteArrayToFile(new File(destFile), msgObj.getBytes(), false);
            System.out.println("收到[" + senderAccount + "]发送的文件(" + msgObj.getMsg() + "),文件保存成功,保存地址:" + destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
