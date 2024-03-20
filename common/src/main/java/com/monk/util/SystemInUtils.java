package com.monk.util;

import com.monk.enums.CommandLevelOne;

import java.util.Scanner;


public class SystemInUtils {

    public static Integer readInt() {
        Scanner scanner = new Scanner(System.in);
        try {
            return Integer.valueOf(scanner.next());
        } catch (Exception e) {
            System.out.println("未知输入指令.");
        }
        return CommandLevelOne.UNKNOWN.getType();
    }

    public static String readString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
