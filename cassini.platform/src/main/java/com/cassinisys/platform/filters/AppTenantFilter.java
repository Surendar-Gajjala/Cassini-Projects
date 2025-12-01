package com.cassinisys.platform.filters;

import com.cassinisys.platform.config.TenantManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by reddy on 9/24/15.
 */
@WebFilter(
        urlPatterns = "/*",
        filterName = "AppTenantFilter",
        description = "Filter to Tenet"
)
public class AppTenantFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        String tenantId = (String) session.getAttribute("CASSINI_TENANT_IDENTIFIER");
        if(tenantId != null) {
            TenantManager.get().setTenantId(tenantId);
        }
        else {
            TenantManager.get().initTenantFromRequest(req);
        }
        try {
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Override
    public void destroy() {

    }
}