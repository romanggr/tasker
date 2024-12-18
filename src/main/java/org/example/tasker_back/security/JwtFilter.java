package org.example.tasker_back.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Date;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${auth.app.secret}")
    private String secretKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String excludeUrl = "/api/v1/auth";
        return request.getRequestURI().startsWith(excludeUrl);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                if (claims.getExpiration().before(new Date())) {
                    throw new ServletException("Token is expired");
                }

                request.setAttribute("claims", claims);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization token is missing or invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
