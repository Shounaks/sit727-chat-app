package org.shounak.sit727chatapp.config;

import org.shounak.sit727chatapp.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailService userDetailsService;
    private final CustomFailureHandler failureHandler;

    public SecurityConfig(CustomUserDetailService userDetailsService, CustomFailureHandler failureHandler) {
        this.userDetailsService = userDetailsService;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //INSECURE
//        return new Md4PasswordEncoder();
        //BOTTOM ONE IS SECURE!
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    protected AuthenticationManagerBuilder authManagerConfig(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, CustomSuccessHandler successHandler) throws Exception {
        return http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/chat").authenticated()
                                .requestMatchers("/chat/**").permitAll() // Allow WebSocket endpoint (initial handshake)
                                .requestMatchers("/", "/auth", "/login", "/logout", "/register").permitAll()
                                .requestMatchers("/favicon.ico").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .loginPage("/login").permitAll()
                                .loginProcessingUrl("/auth").permitAll()
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                                .defaultSuccessUrl("/home", false)
                )
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
