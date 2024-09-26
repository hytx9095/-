package com.wrbi.springbootinit.common;

public class UserContext {
    //为每一个线程都提供了一个独立的变量副本。
    //用于在多线程环境中存储线程局部变量，每个线程可以独立访问自己的副本，不会相互影响
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     * @param userId 用户id
     */
    public static void setUserId(long userId) {
        tl.set(userId);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户id
     */
    public static long getUserId() {
        return tl.get();
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUserId(){
        tl.remove();
    }
}
