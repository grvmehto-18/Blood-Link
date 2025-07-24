package com.blood.bank.Blood.bank.security.config.handler;

import java.io.IOException;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
         String errorMessage = "Invalid username or password.";
        if (exception instanceof LockedException) {
            errorMessage = "Your account is locked.";
        }
        setDefaultFailureUrl("/login?error=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
