package com.monk;

import com.monk.enums.CommandLevelOne;
import com.monk.service.impl.LoginServiceImpl;
import com.monk.util.SystemInUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 *
 * @author monk
 */
@Slf4j
public class ChatClient {

    public void mainMenu() {
        while (true) {
            System.out.println("=================欢迎使用聊天程序小Demo=================");
            System.out.println("1. 登录系统 ");
            System.out.println("9. 退出系统 ");
            System.out.println("请输入对应的指令:");
            final CommandLevelOne command = CommandLevelOne.resolveByType(SystemInUtils.readInt());
            if(CommandLevelOne.LOGIN.equals(command)){
                new LoginServiceImpl().execute();
            }else{
                System.exit(0);
                System.out.println("系统即将退出,欢迎下次使用...");
                break;
            }

        }
    }

    public static void main(String[] args) {
        new ChatClient().mainMenu();

    }
}
