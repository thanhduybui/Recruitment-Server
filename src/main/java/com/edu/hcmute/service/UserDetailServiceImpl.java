package com.edu.hcmute.service;

import com.edu.hcmute.config.security.AuthUser;
import com.edu.hcmute.constant.Message;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(Message.USER_NOT_FOUND_BY_EMAIL, email)));

        AuthUser userDetail = AuthUser.create(user);
        return userDetail;
    }
}
