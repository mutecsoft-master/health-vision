

package com.mutecsoft.healthvision.admin.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
    private final CustomAuthFailureHandler customAuthFailureHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, LocaleResolver localeResolver) throws Exception {
        http
        		.antMatcher("/**")
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(requests -> requests
                        .antMatchers("/admin/static/**", "/templates/**").permitAll()
                        .antMatchers("/admin/**").hasAnyRole("ADMIN", "ANALYST")
                        .antMatchers("/admin/auth/login/**").permitAll()
                        .antMatchers("/admin/user/signUp").permitAll()
                        .antMatchers("/admin/home/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/auth/login") // 로그인 페이지
                        .loginProcessingUrl("/admin/auth/login/proc") // 로그인 처리
                        .defaultSuccessUrl("/admin/main", true) // 로그인 성공 시 이동
                        .failureHandler(customAuthFailureHandler)
                        .permitAll()
                    )
                .logout(logout -> logout
                        .logoutUrl("/admin/auth/logout")
                        .logoutSuccessUrl("/admin/auth/login")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
    
}