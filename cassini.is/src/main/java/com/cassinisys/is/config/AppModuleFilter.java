package com.cassinisys.is.config;
/**
 * The class is for AppModuleFilter
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        urlPatterns = "/*",
        filterName = "AppModuleFilter",
        description = "Filter to replace app.module.js"
)
public class AppModuleFilter implements Filter {
    /**
     * The init() method is started by the browser when the Java program  is loaded and run by the browser.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param request  the request to pass along the chain.
     * @param response the response to pass along the chain.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        /*HttpServletRequest req = (HttpServletRequest) request;
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
        chain.doFilter(request, response);*/
        HttpServletRequest req = (HttpServletRequest) request;
        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "http://localhost:8100");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        HttpServletResponse resp = (HttpServletResponse) response;
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (req.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * The destroy() method is generally used to perform any final clean-up.
     */
    @Override
    public void destroy() {
    }
}
