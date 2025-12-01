
package com.cassinisys.platform.filters;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.security.EvaluationConstraints;
import com.cassinisys.platform.security.jwt.JwtTokenProvider;
import com.cassinisys.platform.security.userDetails.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        urlPatterns = "/*",
        filterName = "JwtTokenFilter",
        description = "Jwt Filter"
)
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    private JwtUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, CassiniException {
        if (!request.getRequestURI().equals("/api/security/logout") && request.getRequestURI().startsWith("/api/")
                && !request.getRequestURI().equals("/api/app/details") && !request.getRequestURI().equals("/api/security/checkportal")
                && !request.getRequestURI().equals("/api/security/login/validate") && !request.getRequestURI().equals("/api/security/login/resetpwd")
                && !request.getRequestURI().equals("/api/security/login/resetpwd/verify") && !request.getRequestURI().equals("/api/security/login/newpassword")
                && !request.getRequestURI().equals("/api/security/language/de") && !request.getRequestURI().equals("/api/security/language/en")
                && !request.getRequestURI().equals("/api/security/session/current") && !request.getRequestURI().equals("/api/core/currencies")
                && !request.getRequestURI().equals("/api/security/session/isactive") && !request.getRequestURI().equals("/api/common/persons/preferences")
                && !request.getRequestURI().equals("/api/common/persons/getSessionTime") && !request.getRequestURI().equals("/api/common/persons/getPreferenceByContext/SYSTEM")
                && !request.getRequestURI().equals("/api/security/login/resetpwd")) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            tokenProvider = webApplicationContext.getBean(JwtTokenProvider.class);
            userDetailsService = webApplicationContext.getBean(JwtUserDetailsService.class);
            String jwt = tokenProvider.getJwt(request);
            String refreshJwt = tokenProvider.getRefreshJwt(request);
            try {
                if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                    String username = tokenProvider.getUserNameFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    response.addHeader(EvaluationConstraints.JWTTOKEN, jwt);
                    response.addHeader(EvaluationConstraints.REFRESHTOKEN, refreshJwt);
                    response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
                }
            } catch (ExpiredJwtException ex) {
                try {
                    try {
                        tokenProvider.validateJwtToken(refreshJwt);
                        response.addHeader(EvaluationConstraints.JWTTOKEN, refreshJwt);
                        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
                    } catch (ExpiredJwtException ex1) {
                        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    }
                } catch (CassiniException ce) {
                    throw ex;
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
