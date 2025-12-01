package com.cassinisys.platform.service.security;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by reddy on 9/11/15.
 */
@Component
@Order(2)
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String uri = request.getRequestURI();
        if (uri.startsWith("/api/security/logout") ||
                uri.startsWith("/api/security/checkportal") ||
                uri.startsWith("/api/security/login/validate") ||
                uri.startsWith("/api/security/login/resetpwd") ||
                uri.startsWith("/api/security/login/resetpwd/verify") ||
                uri.startsWith("/api/security/login/newpassword") ||
                uri.startsWith("/api/security/language/de") ||
                uri.startsWith("/api/security/language/en") ||
                uri.startsWith("/api/security/session/current") ||
                uri.startsWith("/api/core/currencies") ||
                uri.startsWith("/api/security/session/isactive") ||
                uri.startsWith("/api/app/details") ||
                uri.startsWith("/api/common/persons/preferences") ||
                uri.startsWith("/api/common/persons/getSessionTime") ||
                uri.startsWith("/api/common/persons/getPreferenceByContext/SYSTEM")) {
            return true;
        }
        if (TenantManager.get().getTenantId() == null) {
            throw new UnauthorizedAccessException("You do not have authorization to perform this operation");
        }
        if (sessionWrapper == null || sessionWrapper.getSession() == null) {
            if (o instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) o;
                if (!hm.getMethod().getName().equalsIgnoreCase("getProductPicture")) {
                    throw new UnauthorizedAccessException("Login is required to access this information. Resource: " + uri);
                }
            }
            throw new UnauthorizedAccessException("You do not have authorization to perform this operation");
        }

        if (o instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) o;
            PermissionCheck permissionCheck = hm.getMethodAnnotation(PermissionCheck.class);

            if (permissionCheck != null) {
                String[] permissions = permissionCheck.permissions();

                if (permissions.length > 0) {
                    if (!sessionWrapper.getAuthorization().hasOneOfPermissions(Arrays.asList(permissions))) {
                        throw new UnauthorizedAccessException("You do not have authorization to perform this operation (url: " + uri + ")");
                    }
                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
