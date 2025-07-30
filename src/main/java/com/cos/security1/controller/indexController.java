package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    //localhost:8080/
    //localhost:8080
    @GetMapping({"","/"})
    public String index() {
        // mustache 기본 폴더 - src/main/resources/
        // View Resolver 설정 - prefix : templates, suffix : .mustache (의존성 등록시 경로 자동으로 잡힘. 생략 가능)
        return "index"; // scr/main/resources.templates/index.mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
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
}
