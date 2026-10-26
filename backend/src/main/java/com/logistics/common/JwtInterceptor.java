package com.logistics.common;

import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * JWT 登录拦截器：校验请求头中的 token，解析出用户ID与角色放入 UserContext
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    public static final byte[] SECRET = "logistics-jwt-secret-key".getBytes(StandardCharsets.UTF_8);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null || token.isBlank() || !JWTUtil.verify(token, SECRET)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"登录已过期，请重新登录\",\"data\":null}");
            return false;
        }
        Object id = JWTUtil.parseToken(token).getPayload("id");
        Object role = JWTUtil.parseToken(token).getPayload("role");
        UserContext.set(new UserContext.LoginUser(
                id == null ? null : ((Number) id).longValue(),
                role == null ? null : role.toString()));
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        UserContext.remove();
    }
}
