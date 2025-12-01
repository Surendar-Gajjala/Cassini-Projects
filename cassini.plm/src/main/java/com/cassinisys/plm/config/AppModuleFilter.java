package com.cassinisys.plm.config;

import net.sf.uadetector.ReadableDeviceCategory;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@WebFilter(
        urlPatterns = "/*",
        filterName = "AppModuleFilter",
        description = "Filter to replace app.module.js"
)
public class AppModuleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        if (path.equalsIgnoreCase("/app/app.js")) {
            response.setContentType("application/javascript");
            String file = "/app/app.desktop.js";
            UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
            ReadableUserAgent agent = parser.parse(req.getHeader("User-Agent"));
            if (agent.getDeviceCategory().getCategory() == ReadableDeviceCategory.Category.SMARTPHONE) {
                file = "/app/app.phone.js";
            }
            InputStream is = request.getServletContext().getResourceAsStream(file);
            String js = IOUtils.toString(is);
            response.getWriter().write(js);
            response.getWriter().flush();
        }

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
