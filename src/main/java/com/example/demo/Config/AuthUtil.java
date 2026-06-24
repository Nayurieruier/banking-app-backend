package com.example.demo.Config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public int getCurrentCustomerId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Integer) authentication.getDetails();
    }
}