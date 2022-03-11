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

    public static UserDetailsImpl getUser() {
        return (UserDetailsImpl) (SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal());
    }

}
