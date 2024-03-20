# 该项目是一个基于TCP/IP实现的一个BIO程序
该Demo是在跟着韩顺平老师的《TCP UDP Socket编程》视频学习，在学习的过程中，基于韩老师的思路，结合自己的开发经验，写出来的一个demo，仅供自己学习使用。
该工程分为三个Module
- server：服务端
- common：公共包
- client：客户端

## server服务端
这个简易的服务端，具备了监听了本地的**9999**端口外，还具备在控制台发布公告的功能

## client客户端
客户端具备一些简单的功能
1. 登录
2. 获取在线用户列表
3. 群聊
4. 私聊
5. 发送文件
6. 离线发送消息及文件

## common公共包
里边定义了一些通用的常量、枚举类，已经一些工具方法，供服务端、客户端使用
