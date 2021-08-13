package com.collab.project.filter;

import com.collab.project.util.JwtUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtRequestFilter  {

}

//    @Autowired
//    JwtUtils jwtUtils;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//        HttpServletResponse response, FilterChain chain)
//        throws ServletException, IOException {
//
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String artistName = null;
//        String jwt = null;
//
//        final String token = authorizationHeader.split(" ")[1].trim();
//        try {
//            if (!jwtUtils.isTokenExpired(token)) {
//
//                chain.doFilter(request, response);
//                return;
//            } else {
//                logger.info("Token expired");
//            }
//        } catch (Exception exception) {
//            logger.info("Auth Failed");
//        }
//
////        String userName = jwtUtils.extractUsername(token);
//
////
////
////        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////
////            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
////
////            if (jwtUtil.validateToken(jwt, userDetails)) {
////
////                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
////                    userDetails, null, userDetails.getAuthorities());
////                usernamePasswordAuthenticationToken
////                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
////            }
////        }
////        chain.doFilter(request, response);
//    }
//
//    private void replyWithValidationFailure(HttpServletResponse response) {
//        logger.info("Auth failure");
//        response.setContentType("application/json");
//    }
//
//    private boolean isLoginApi(HttpServletRequest request) {
//        String pathInfo = request.getServletPath();
//        if (!StringUtils.hasText(pathInfo)) {
//            pathInfo = request.getPathInfo();
//        }
//        if (StringUtils.hasText(pathInfo)) {
//            return pathInfo.endsWith("/login");
//        }
//        return false;
//    }
//}
