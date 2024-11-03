package com.inghub.StockOrder.security;

import com.inghub.StockOrder.entity.Employee;
import com.inghub.StockOrder.entity.OauthToken;
import com.inghub.StockOrder.services.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    private LoginService loginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {


            String authHeader = request.getHeader("Authorization");
            String requestURI = request.getRequestURI();

            if (requestURI.equals("/api/auth/login")) {
                response.setStatus(HttpServletResponse.SC_OK);
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("NOT_REQUIRED"));

                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(null, null, authorities);
                // authenticationToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

            else if (authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.substring(7).isBlank()) {
                String accessToken = authHeader.substring(7);

                Employee user = loginService.isTokenValid(accessToken);

                if (user == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                else {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    // authenticationToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
