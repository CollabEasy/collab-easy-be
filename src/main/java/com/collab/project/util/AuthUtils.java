package com.collab.project.util;

import com.collab.project.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUtils {

    public static String getArtistId() {
        return ((UserDetailsImpl) (SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal())).getId();
    }

    public UserDetailsImpl getUser() {
        return (UserDetailsImpl) (SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal());
    }

    public String getEmail() {
        return getUser().getEmail();
    }

}
