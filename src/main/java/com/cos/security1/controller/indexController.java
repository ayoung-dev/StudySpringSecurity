package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴
public class indexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 방법 2개
    // 1. Authentication PrincipalDetails로 다운캐스트해서 getUser()
    // 2. AuthenticationPrincipal 어노테이션을 통해 PrincipalDetails로 getUser()
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {  //DI(의존성 주입)
        System.out.println("/test/login ==============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication:" + principalDetails.getUser());

        System.out.println("userDetails:" + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    // oauth의 경우
    // 1. OAuth2User로 다운 캐스팅 후 getAttributes()
    // 2. AuthenticationPrincipal 어노테이션을 사용해 OAuth2User의 getAttributes()
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {  //DI(의존성 주입)
        System.out.println("/test/oauth/login ==============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication:" + oAuth2User.getAttributes());

        System.out.println("oauth2User:" + oAuth2User.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    //localhost:8080/
    //localhost:8080
    @GetMapping({"","/"})
    public String index() {
        // mustache 기본 폴더 - src/main/resources/
        // View Resolver 설정 - prefix : templates, suffix : .mustache (의존성 등록시 경로 자동으로 잡힘. 생략 가능)
        return "index"; // scr/main/resources.templates/index.mustache
    }

    // OAuth 로그인 & 일반 로그인 모두 PrincipalDetails 받을 수 있음
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails:" + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // SpringSecurity가 해당 주소를 낚아챔 - SecurityConfig 파일 생성 후 작동 안함
//    @GetMapping("/login")
//    public @ResponseBody String login() {
//        return "login";
//    }
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);  //회원가입 잘되지만 비밀번호 암호화 안됨 => Security로 로그인 불가
        return "redirect:/loginForm";
    }

//    @GetMapping("/joinProc")
//    public @ResponseBody String joinProc() {
//        return "회원가입 완료됨!";
//    }

    @Secured("ROLE_ADMIN")  // 특정 메서드에 간단하게 role 제한
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
