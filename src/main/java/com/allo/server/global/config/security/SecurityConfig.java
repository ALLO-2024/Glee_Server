package com.allo.server.global.config.security;


import com.allo.server.global.config.security.jwt.JwtAuthenticationFilter;
import com.allo.server.global.config.security.jwt.JwtTokenProvider;
import com.allo.server.global.config.security.oauth.CustomOAuth2UserService;
import com.allo.server.global.config.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.allo.server.global.config.security.oauth.OAuth2AuthenticationFailureHandler;
import com.allo.server.global.config.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
//    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()

                )
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                        .defaultSuccessUrl("/oauth2/authorize", true))
//                .oauth2Login()
//                .authorizationEndpoint().baseUri("/oauth2/authorize")
//                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
//                .and()
//                .redirectionEndpoint()
//                .baseUri("/login/oauth2/code/**")
//                .and()
//                .userInfoEndpoint().userService(customOAuth2UserService)
//                .and()
//                .successHandler(oAuth2AuthenticationSuccessHandler)
//                .failureHandler(oAuth2AuthenticationFailureHandler)
//                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
//                .authorizeHttpRequests(
//
//                )
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                .and()
//                .formLogin().disable()
//                .httpBasic().disable()
//
//                .authorizeRequests()
//                .antMatchers ("/api/**", "/login/**", "/oauth2/**").permitAll ()
//                .and()
//
//                .oauth2Login()
//                .authorizationEndpoint().baseUri("/oauth2/authorize")
//                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
//                .and()
//                .redirectionEndpoint()
//                .baseUri("/login/oauth2/code/**")
//                .and()
//                .userInfoEndpoint().userService(customOAuth2UserService)
//                .and()
//                .successHandler(oAuth2AuthenticationSuccessHandler)
//                .failureHandler(oAuth2AuthenticationFailureHandler)
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
    }
}