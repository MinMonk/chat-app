package com.monk.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体
 *
 * @author monk
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -6499375086702698095L;
    /**
     * 发件人
     */
    private User sender;

    /**
     * 收件人
     */
    private User receiver;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 文件字节数组
     */
    private byte[] bytes;

    /**
     * 指令类型
     */
    private Integer commandType;

    /**
     * 发送时间
     */
    private Date sendDate = new Date();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(sender=").append(sender.getAccount()).append(", ");
        sb.append("receiver=").append(receiver.getAccount()).append(", ");
        sb.append("msg=").append(msg).append(", ");
        sb.append("commandType=").append(commandType).append(", ");
        sb.append("sendDate=").append(sendDate).append(")");
        return sb.toString();
    }
}
