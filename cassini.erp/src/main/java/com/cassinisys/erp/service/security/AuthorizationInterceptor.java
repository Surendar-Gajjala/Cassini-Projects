package com.cassinisys.erp.service.security;

import com.cassinisys.erp.api.exceptions.UnauthorizedAccessException;
import com.cassinisys.erp.config.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String uri = request.getRequestURI();
        if(uri.startsWith("/api/security/login") ||
                uri.startsWith("/api/security/session/current") ||
                uri.startsWith("/api/security/session/isactive")) {
            return true;
        }

        if(TenantManager.get().getTenantId() == null) {
            return false;
        }

        if(sessionWrapper == null || sessionWrapper.getSession() == null) {
          /*  if(o instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) o;
                if(!hm.getMethod().getName().equalsIgnoreCase("getProductPicture")) {
                    throw new UnauthorizedAccessException("Login is required to access this information. Resource: " + uri);
                }
            }*/
        	return true;
        }

        if(o instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod)o;
            PermissionCheck permissionCheck = hm.getMethodAnnotation(PermissionCheck.class);

            if(permissionCheck != null) {
                String[] permissions = permissionCheck.permissions();

                if(permissions != null && permissions.length > 0) {
                    if(!sessionWrapper.getAuthorization().hasOneOfPermissions(Arrays.asList(permissions))) {
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
