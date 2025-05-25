package com.project.musicwebbe.service.permission.impl;

import com.project.musicwebbe.dto.UserInforUserDetails;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.permission.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInforDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserInforUserDetails(user, user.getRoles());
    }
    public UserDetails loadUserByUsercode(String usercode) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUserCode(usercode);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserInforUserDetails(user, user.getRoles());
    }
}
