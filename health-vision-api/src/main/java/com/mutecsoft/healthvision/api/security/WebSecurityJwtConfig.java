

package com.mutecsoft.healthvision.api.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;

import com.mutecsoft.healthvision.api.service.CustomOAuth2UserService;
import com.mutecsoft.healthvision.common.config.LocaleFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityJwtConfig {

	//api
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    
    //web
    private final CustomAuthFailureHandler customAuthFailureHandler;
    
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    @Order(1)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        		.antMatcher("/api/**") // api 요청만 처리
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // jwt 인증 실패한 경우(401)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeRequests(requests -> requests
                        .antMatchers("/api/auth/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .antMatchers("/api/user/email/**").permitAll()
                        .antMatchers("/api/user/findPw").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/terms/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/notice/**").permitAll()
                        .antMatchers("/api/googleRtdn/**").permitAll()
                        .antMatchers("/api/appleRtdn/**").permitAll()
                        .anyRequest().authenticated())
                ;

        // jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 삽입
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
    @Bean
    @Order(2)
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http, LocaleResolver localeResolver) throws Exception {
        http
            .antMatcher("/web/**") // web 요청만 처리
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .antMatchers("/static/**").permitAll()
                .antMatchers("/web/auth/login/**").permitAll()
                .antMatchers("/web/oauth/**").permitAll() //Apple 로그인
        		.antMatchers("/web/notice/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/web/auth/login") // 로그인 페이지
                .loginProcessingUrl("/web/auth/login/proc") // 로그인 처리
                .defaultSuccessUrl("/web/main", true) // 로그인 성공 시 이동
                .failureHandler(customAuthFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/web/auth/logout")
                .logoutSuccessUrl("/web/auth/login")
                .invalidateHttpSession(true)
            );
        
        http.addFilterBefore(new LocaleFilter(localeResolver), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
    @Bean
    @Order(3)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
        		.antMatchers("/").permitAll()
        		.antMatchers("/static/**", "/templates/**").permitAll()
        		.antMatchers("/oauth2/**").permitAll() //oauth2 : Google 로그인
        		.antMatchers("/error").permitAll() //error : whitelabel error 페이지 표시
                .antMatchers("/swagger-ui/**").permitAll()
        		.antMatchers("/swagger-resources/**").permitAll()
        		.antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(login -> login
                .loginPage("/web/auth/login")
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
            );
        return http.build();
    }

}