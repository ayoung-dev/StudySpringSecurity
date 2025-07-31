package com.cos.security1.config.auth;

// security가 /login 주소 요청이 오면 낚아채서 로그인 진행
// 로그인 진행이 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder)
// Object type => Authentication 타입 객체
// Authentication 안에는 User 정보가 있어야 함
// User Object type => UserDetails 타입 객체

// Security Session => Authentication 객체 => UserDetails(PrincipalDetails)

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한 return 하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 1년동안 회원이 로그인 안하면 휴먼계정 전환
        // 현재시간 - 로그인 시간 > 1년 => return false;
        return true;
    }
}
