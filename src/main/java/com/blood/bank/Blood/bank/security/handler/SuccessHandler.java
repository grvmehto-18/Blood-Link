package com.blood.bank.Blood.bank.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        boolean isAdmin = authentication.getAuthorities()
                                                        .stream()
                                                        .anyMatch(
                                                            grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")
                                                        );

        if(isAdmin){
            setDefaultTargetUrl("/admin/dashboard");
        }else{
            setDefaultTargetUrl("/donor/dashboard");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
