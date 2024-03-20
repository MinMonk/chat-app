package com.monk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户实体
 *
 * @author monk
 */
@Data
@AllArgsConstructor
public class User implements Serializable {


    private static final long serialVersionUID = 79897340888451170L;
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    public static User server = new User("server");

    public User(String account) {
        this.account = account;
    }
}
