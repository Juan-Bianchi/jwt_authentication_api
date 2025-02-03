package org.example.jwt_authentication_api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jwt_authentication_api.dto.ExceptionDto;
import org.example.jwt_authentication_api.exceptions.JwtPersonalizedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;


    public AuthTokenFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull
            HttpServletRequest request,
            @Nonnull
            HttpServletResponse response,
            @Nonnull
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtService.extractUsername(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            jwtService.validateJwtToken(jwt, userDetails);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            if(e instanceof JwtPersonalizedException) {
                ExceptionDto responseBody = new ExceptionDto(e.getMessage());
                response.setStatus(((JwtPersonalizedException) e).getStatusCode());
                response.getWriter().write(convertObjectToJson(responseBody));

                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(convertObjectToJson(new ExceptionDto("Unauthorized")));
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
