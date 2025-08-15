package com.cos.security1.config;

// 1. 코드 받기(인증)
// 2. 엑세스 토큰(권한)
// 3. 사용자 프로필 정보 가져옴
// 4-1. 정보를 토대로 회원가입 자동으로 진행
// 4-2. 추가 정보 받은 창으로 이동

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Spring Security Filter(SecurityConfig)가 Spring Filter Chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화, preAuthorize & postAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

//    // 해당 메서드의 리턴 오브젝트를 IoC로 등록
//    @Bean
//    public BCryptPasswordEncoder encodePwd() {
//        return new BCryptPasswordEncoder();
//    }

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
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .loginPage("/loginForm")
                            .userInfoEndpoint(userInfo ->
                                    userInfo.userService(principalOauth2UserService)
                            )
                    );

            return http.build();
        }
}
