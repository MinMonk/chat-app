package com.monk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CommandLevelOne {
    LOGIN(1, "登录"),
    LOGOUT(9, "退出"),
    LOGIN_SUCCESS(2, "登录成功"),
    LOGIN_FAILED(3, "登录失败"),
    UNKNOWN(999, "未知指令"),
    ;

    private Integer type;
    private String meaning;

    public static CommandLevelOne resolveByType(Integer type) {
        return Arrays.stream(CommandLevelOne.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElse(UNKNOWN);

    }
}
