package com.monk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CommandLevelTwo {

    ONLINE_USER_REQUEST(1, "获取在线用户列表请求"),
    GROUP_MSG(2, "群发消息"),
    SINGLE_MSG(3, "私聊"),
    SEND_FILE(4, "发送文件"),
    NEWS(5, "新闻"),
    LOGOUT(9, "退出"),
    ONLINE_USER_RESPONSE(11, "在线用户列表请求响应"),
    UNKNOWN(999, "未知指令"),
    ;
    private Integer type;
    private String meaning;

    public static CommandLevelTwo resolveByType(Integer type) {
        return Arrays.stream(CommandLevelTwo.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElse(UNKNOWN);

    }

}
