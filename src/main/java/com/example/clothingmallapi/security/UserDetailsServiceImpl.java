package com.example.clothingmallapi.security;


import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String usersname) throws UsernameNotFoundException {
        Users users = usersRepository.findByName(usersname)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(users, usersname, users.getEmailId(), users.getId());
    }

    public UserDetails loadUserByEmailId(String emailId) throws IllegalArgumentException {
        Users users = usersRepository.findByEmailId(emailId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(users, users.getName(), emailId, users.getId());
    }

    public UserDetails loadUserById(Long Id) throws IllegalArgumentException {
        Users users = usersRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(users, users.getName(), users.getEmailId(), users.getId());
    }

}