package com.core;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 10:14 上午
 * @description: 存放用户信息
 */
public final class UserContext {
    /**
     * 存放访问token
     */
    private static ThreadLocal<String> AUTHORIZATION = new InheritableThreadLocal<>();

    /**
     * 邮箱
     */
    private static final ThreadLocal<String> EMAIL_ADDRESS = new InheritableThreadLocal<String>();

    /**
     * 绑定当前用户token
     *
     * @param authorization 用户票据信息
     */
    public static void setAuthorization(String authorization) {
        if (authorization != null) {
            AUTHORIZATION.set(authorization);
        }
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public static String getAuthorization() {
        return AUTHORIZATION.get();
    }

    /**
     * 设置邮箱
     *
     * @param emailAddress
     */
    public static void setEmailAddress(String emailAddress) {
        EMAIL_ADDRESS.set(emailAddress);
    }

    /**
     * 获取邮箱
     *
     * @return
     */
    public static String getEmailAddress() {
        return EMAIL_ADDRESS.get();
    }

    /**
     * 资源清理，避免内存增长，一般在请求完成后执行该方法
     */
    public static void clean() {
        EMAIL_ADDRESS.remove();
    }

}
