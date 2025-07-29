package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View를 리턴
public class indexController {

    //localhost:8080/
    //localhost:8080
    @GetMapping({"","/"})
    public String index() {
        // mustache 기본 폴더 - src/main/resources/
        // View Resolver 설정 - prefix : templates, suffix : .mustache (의존성 등록시 경로 자동으로 잡힘. 생략 가능)
        return "index"; // scr/main/resources.templates/index.mustache
    }
}
