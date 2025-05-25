package com.project.musicwebbe.util;

import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GetCurrentUser {
    @Autowired
    private IUserService userService;

    public AppUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        String email = authentication.getName();
        return userService.findByEmail(email);
    }
}
