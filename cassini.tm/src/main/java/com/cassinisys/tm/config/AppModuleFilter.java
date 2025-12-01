package com.cassinisys.tm.config;

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

/**
 * Created by reddy on 11/9/15.
 */

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
        response.setContentType("application/javascript");

        String path = req.getRequestURI();

        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(req.getHeader("User-Agent"));


        String fileName = null;

        if(path.equalsIgnoreCase("/app/app.root.js")) {
            fileName = "/app/desktop/desktop.root.js";
            if(agent.getDeviceCategory().getCategory() == ReadableDeviceCategory.Category.SMARTPHONE) {
                fileName = "/app/phone/phone.root.js";
            }
        }

        if(fileName != null) {
            InputStream is = request.getServletContext().getResourceAsStream(fileName);
            String js = IOUtils.toString(is);
            response.getWriter().write(js);
            response.getWriter().flush();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
