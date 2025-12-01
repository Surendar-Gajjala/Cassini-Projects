package com.cassinisys.platform.service.security;

import com.cassinisys.platform.config.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by reddy on 9/24/15.
 */
@Component
@Order(1)
public class TenantInterceptor implements HandlerInterceptor {
    @Autowired
    private SecurityService securityService;


    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession session = request.getSession();
        String tenantId = (String) session.getAttribute("CASSINI_TENANT_IDENTIFIER");
        if(tenantId != null) {
            TenantManager.get().setTenantId(tenantId);
        }
        else {
            TenantManager.get().initTenantFromRequest(request);
        }
        return true;
    }

    @Override
    @Transactional
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    @Transactional
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}