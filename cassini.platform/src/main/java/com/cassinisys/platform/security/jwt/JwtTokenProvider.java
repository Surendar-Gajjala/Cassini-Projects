package com.cassinisys.platform.security.jwt;

import com.cassinisys.platform.model.core.Login;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private String jwtExpiration;

    @Value("${app.refreshExpiration}")
    private String refreshExpiration;

    public String generateJwtToken(Authentication authentication) {

        Login userPrinciple = (Login)authentication.getPrincipal();

        return Jwts.builder()
		                .setSubject((userPrinciple.getLoginName()))
		                .setIssuedAt(new Date())
		                .setExpiration(new Date((new Date()).getTime() + Integer.parseInt(jwtExpiration)*1000))
		                .signWith(SignatureAlgorithm.HS512, jwtSecret)
		                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {

        Login userPrinciple = (Login)authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrinciple.getLoginName()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (Integer.parseInt(refreshExpiration) + Integer.parseInt(jwtExpiration)) * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
			                .setSigningKey(jwtSecret)
			                .parseClaimsJws(token)
			                .getBody().getSubject();
    }

    public String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }

    public String getRefreshJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("X-Refresh-Token");

        if (authHeader != null) {
            return authHeader;
        }
        return null;
    }
}