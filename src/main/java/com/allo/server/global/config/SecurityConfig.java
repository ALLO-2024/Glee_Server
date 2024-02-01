package com.allo.server.global.config;

import com.allo.server.domain.auth.filter.CustomUserAuthFilter;
import com.allo.server.domain.auth.handler.LoginFailureHandler;
import com.allo.server.domain.auth.handler.LoginSuccessHandler;
import com.allo.server.domain.auth.service.UserLoginService;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.jwt.JwtAccessDeniedHandler;
import com.allo.server.jwt.JwtAuthenticationEntryPoint;
import com.allo.server.jwt.filter.JwtAuthenticationProcessingFilter;
import com.allo.server.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ObjectMapper objectMapper;
    private final UserLoginService userLoginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(mvcMatcherBuilder.pattern("/users/login")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/users/login/social")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/users/signUp")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/users/signUp/email")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/login/oauth2/code/kakao")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/login/oauth2/code/naver")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/login/oauth2/code/google")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/reissue-token")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/error")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/favicon.ico")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-resources/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/users/nickname/isDuplicated")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/lectures/test")).permitAll() // 테스트
                                .anyRequest().authenticated())
                .addFilterAfter(customUserAuthFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), CustomUserAuthFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    @Primary
    public AuthenticationManager userAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userLoginService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Collections.singletonList(provider));
    }


    @Bean
    public CustomUserAuthFilter customUserAuthFilter() {
        CustomUserAuthFilter customUserAuthFilter = new CustomUserAuthFilter(objectMapper);
        customUserAuthFilter.setAuthenticationManager(userAuthenticationManager());
        customUserAuthFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUserAuthFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customUserAuthFilter;
    }


    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter = new JwtAuthenticationProcessingFilter(jwtService);
        return jwtAuthenticationProcessingFilter;
    }

}
