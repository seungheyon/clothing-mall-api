package com.example.clothingmallapi.security;

import com.example.clothingmallapi.users.entity.Users;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Users users;
    private final String usersname;
    private final String emailId;
    private final Long Id;

    public UserDetailsImpl(Users users, String usersname, String emailId, Long id) {
        this.users = users;
        this.usersname = usersname;
        this.emailId = emailId;
        this.Id = id;
    }

    public Users getUsers() {
        return users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String authority = "USER";

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.usersname;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getEmailId(){
        return this.emailId;
    }
}