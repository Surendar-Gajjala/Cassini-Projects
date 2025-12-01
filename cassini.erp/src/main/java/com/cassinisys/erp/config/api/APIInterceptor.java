package com.cassinisys.erp.config.api;

import com.cassinisys.erp.api.exceptions.UnauthorizedAccessException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by reddy on 9/2/15.
 */
public class APIInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        if(!uri.startsWith("/api/security/login") &&
                !uri.startsWith("/api/security/session/current") &&
                !uri.startsWith("/api/security/session/isactive")) {
            String apiKey = request.getHeader("CASSINI-API-KEY");
            if(apiKey == null) {
                apiKey = request.getParameter("apiKey");
            }

            if(apiKey == null) {
                throw new UnauthorizedAccessException("Missing API key");
            }

            if(!APIKeyGenerator.get().validateKey(apiKey)) {
                throw new UnauthorizedAccessException("Invalid API key: " + apiKey);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
