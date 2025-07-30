package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Spring Security Filter(SecurityConfig)가 Spring Filter Chain에 등록됨
public class SecurityConfig {

    // 해당 메서드의 리턴 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/user/**").authenticated()
                            .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll()
                    )
                    .formLogin(form -> form
                            .loginPage("/loginForm")
                            .permitAll()
                    );

            return http.build();
        }
}
