package org.example.jwt_authentication_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService, JwtService jwtService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtService, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
            .cors(AbstractHttpConfigurer::disable) // Disable CORS
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/api/auth/**", "/api/test/all").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .anyRequest().authenticated()
            )
            // Disable frameOptions so h2-console can be accessed
            .headers((headers) ->headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        ;

        // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
