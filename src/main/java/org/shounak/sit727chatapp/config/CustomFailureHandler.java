package org.shounak.sit727chatapp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;


@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {
    Logger logger = Logger.getLogger(CustomFailureHandler.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println(exception.getMessage());
        //Patched Code: ⬇️
//        response.sendRedirect("/login?error=true&errorMessage=" + exception.getMessage());
        //Vulnerable Code: ⬇️
        response.sendRedirect("/login?error=true&errorMessage=" + getExactReason(exception));
    }

    /*This method is vulnerable and should not be used in production!*/
    private String getExactReason(AuthenticationException ex) {
        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
        if ("org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider".equals(stackTraceElement.getClassName())) {
            return "Invalid Username";
        }
        else if("org.springframework.security.authentication.dao.DaoAuthenticationProvider".equals(stackTraceElement.getClassName())) {
            return "Invalid Password";
        }
        return "Something Went Wrong!";
    }
}