package com.exist.HelpdeskApp.config;

import com.exist.HelpdeskApp.exception.businessexceptions.InvalidCredentialsException;
import com.exist.HelpdeskApp.service.JwtService;
import com.exist.HelpdeskApp.service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private ApplicationContext context;

    @Autowired
    public JwtFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer")) {
                token = authHeader.substring(7); //Bearer: ...
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(AccountDetailsService.class).loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new InvalidCredentialsException("Token is invalid or expired. Please login again...");
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidCredentialsException ex) {
            writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", ex.getMessage(), request.getRequestURI());
        } catch (Exception ex) {
            writeJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request.getRequestURI());
        }
    }

    private void writeJsonError(HttpServletResponse response, int status, String error, String message, String path) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);

        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                Instant.now().toString(),
                status,
                error,
                message,
                path
        );

        response.getWriter().write(json);
    }
}
