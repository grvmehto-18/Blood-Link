package com.blood.bank.Blood.bank.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView modelAndView = new ModelAndView();

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView.setViewName("error/404");
                modelAndView.addObject("errorMessage", "The page you are looking for does not exist.");
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                modelAndView.setViewName("error/403");
                modelAndView.addObject("errorMessage", "You don't have permission to access this page.");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                modelAndView.setViewName("error/500");
                modelAndView.addObject("errorMessage", "An unexpected internal server error occurred.");
            } else {
                modelAndView.setViewName("error");
                modelAndView.addObject("errorMessage", "An unexpected error occurred.");
            }
        } else {
            modelAndView.setViewName("error");
            modelAndView.addObject("errorMessage", "An unexpected error occurred.");
        }

        return modelAndView;
    }
}
