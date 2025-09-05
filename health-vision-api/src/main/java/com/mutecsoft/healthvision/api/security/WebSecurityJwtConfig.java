

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityJwtConfig {

	//api
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    
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
                        .antMatchers(HttpMethod.GET, "/api/terms/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/notice/**").permitAll()
                        .anyRequest().authenticated())
                ;

        // jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 삽입
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    @Order(3)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
        		.antMatchers("/").permitAll()
        		.antMatchers("/static/**", "/templates/**").permitAll()
        		.antMatchers("/error").permitAll() //error : whitelabel error 페이지 표시
                .antMatchers("/swagger-ui/**").permitAll()
        		.antMatchers("/swagger-resources/**").permitAll()
        		.antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

}