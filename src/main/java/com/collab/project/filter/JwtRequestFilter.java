package com.collab.project.filter;

import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.collab.project.util.JwtUtils;
import com.collab.project.util.TokenAuthentication;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain)
        throws IOException {
        try {
            if (!isAuthRequired(request)) {
                chain.doFilter(request, response);
                return;
            }
            final String authorizationHeader = request.getHeader("Authorization");

            final String token = authorizationHeader.split(" ")[1].trim();
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(token));

            if (tokenAuth.isAuthenticated()) {
                logger.info("Doing Auth");
                SecurityContextHolder.getContext().setAuthentication(tokenAuth);
                chain.doFilter(request, response);
            } else {
                logger.info("Token expired");
                response.getWriter().write("Unable to Login");
                return;
            }
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            logger.info("Auth Failed");
            response.getWriter().write("Exception whileLogin");
            return;
        }
    }

//        String userName = jwtUtils.extractUsername(token);

//
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            if (jwtUtil.validateToken(jwt, userDetails)) {
//
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        chain.doFilter(request, response);

    private void replyWithValidationFailure(HttpServletResponse response) {
        logger.info("Auth failure");
        response.setContentType("application/json");
    }


    private boolean isAuthRequired(HttpServletRequest request) {
        if (request.getServletPath().contains("/login") || request.getServletPath()
            .contains("/callback") || request.getServletPath().contains("/")) {
            return false;
        }
        return true;
    }
}
