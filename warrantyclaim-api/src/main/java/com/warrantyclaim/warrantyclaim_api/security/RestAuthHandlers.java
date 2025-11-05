package com.warrantyclaim.warrantyclaim_api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAuthHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
            org.springframework.security.core.AuthenticationException ex) {
        try {
            String auth = req.getHeader("Authorization");
            if (auth == null || auth.trim().length() == 0) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.getWriter().write("{\"success\":false,\"message\":\"Missing access token\"}");
            } else {
                res.setStatus(401);
                res.setContentType("application/json");
                res.getWriter().write(
                        "{\"success\":false,\"message\":\"Unauthorized – Please login to access this resource\"}");
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res,
            org.springframework.security.access.AccessDeniedException ex) {
        try {
            res.setStatus(403);
            res.setContentType("application/json");
            res.getWriter().write(
                    "{\"success\":false,\"message\":\"Forbidden – You do not have permission to perform this action\"}");
        } catch (Exception ignored) {
        }
    }
}
