package com.skinversity.backend.Config;

import com.skinversity.backend.Services.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;


    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JWTFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/register", "admin/enroll-admin")
                        .permitAll()
                        .requestMatchers("/product/addProduct", "/product/remove-product/*")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/product/allProducts","/user/change-password","/product/getCategory/*")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                        .requestMatchers("/cart/*")
                        .hasAuthority("ROLE_CUSTOMER"))
//                .oauth2Login(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(15));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public BearerTokenResolver cookieBearerTokenResolver() {
        return request -> {
            Cookie [] cookies = request.getCookies();
            if(cookies != null) {
                return Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("accessToken"))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            }
            return null;
        };
    }
}
