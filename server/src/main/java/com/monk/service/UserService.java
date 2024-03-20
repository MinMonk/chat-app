package com.monk.service;

import com.monk.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static Map<String, User> userMap = new HashMap<>();

    static {
        userMap.put("tom", new User("tom", "tom"));
        userMap.put("jerry", new User("jerry", "jerry"));
        userMap.put("jack", new User("jack", "jack"));
        userMap.put("rose", new User("rose", "rose"));
    }

    public boolean verifyUser(User user) {
        final User cacheUser = userMap.get(user.getAccount());
        if (null == cacheUser) {
            return false;
        }
        if (cacheUser.getAccount().equals(user.getAccount())
                && cacheUser.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }

}
