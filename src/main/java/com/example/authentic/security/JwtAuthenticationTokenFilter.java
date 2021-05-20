package com.example.authentic.security;

import com.example.authentic.business.UserDetailsServiceImpl;
import com.example.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse
            , FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(httpServletRequest);
            if (jwt != null && jwtAuthenticationProvider.validateJwtToken(jwt)) {

                String username = jwtAuthenticationProvider.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuth = httpServletRequest.getHeader(Constants.AUTH_KEY.HEADER_KEY);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(Constants.AUTH_KEY.PREFIX_KEY)) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
