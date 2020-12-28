package com.example.authentic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (true) {
            // use the credentials
            // and authenticate against the third-party system

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    name, password));
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
