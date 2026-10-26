package com.logistics.common;

/**
 * 登录用户上下文（JWT 拦截器解析后放入 ThreadLocal）
 */
public class UserContext {

    public record LoginUser(Long id, String role) {}

    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static Long getUserId() {
        return HOLDER.get() == null ? null : HOLDER.get().id();
    }

    public static String getRole() {
        return HOLDER.get() == null ? null : HOLDER.get().role();
    }

    public static void remove() {
        HOLDER.remove();
    }
}
