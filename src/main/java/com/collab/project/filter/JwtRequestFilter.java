package com.collab.project.filter;


import java.io.IOException;
import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.collab.project.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtRequestFilter  extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain) {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            if (isAuthRequired(request) && authorizationHeader != null) {
                final String jwt = authorizationHeader.split(" ")[1].trim();

                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    String artistId = jwtUtils.getArtistIdFromToken(jwt);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(artistId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authentication
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    replyWithValidationFailure(response);
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }


    }

    private void replyWithValidationFailure(HttpServletResponse response) {
        try {
            logger.info("Auth failure");
            response.setContentType("application/json");
            JSONObject error = new JSONObject();
            error.put("error", "Authentication Failure");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getOutputStream().write(error.toString().getBytes());
        } catch (IOException e) {
            log.error("Exception while response writing", e);
        }
    }

    private boolean isAuthRequired(HttpServletRequest request) {
        if (request.getServletPath().contains("/login")) {
            return false;
        }
        return true;
    }
}
