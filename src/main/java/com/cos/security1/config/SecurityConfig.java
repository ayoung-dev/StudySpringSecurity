package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Spring Security Filter(SecurityConfig)가 Spring Filter Chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화, preAuthorize & postAuthorize 어노테이션 활성화
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
                            .requestMatchers("/user/**").authenticated()    // 인증만 되면 들어갈 수 있음
                            .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll()
                    )
                    .formLogin(form -> form
                            .loginPage("/loginForm")
                            .loginProcessingUrl("/login")   // /login 주소가 호출이 되면 security가 낚아채서 대신 로그인 진행 -> controller에 /login 만들지 않아도 됨
                            .defaultSuccessUrl("/")
                            .permitAll()
                    );

            return http.build();
        }
}
