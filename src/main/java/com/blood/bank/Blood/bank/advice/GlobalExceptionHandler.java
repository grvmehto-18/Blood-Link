package com.blood.bank.Blood.bank.advice;

import com.blood.bank.Blood.bank.controller.RedirectionAndReturn;
import com.blood.bank.Blood.bank.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(SQLException ex) {
        log.error("SQLException: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("UserAlreadyExistsException: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/409");
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.CONFLICT);
        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("MethodArgumentNotValidException: {}", errorMessage, ex);

        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("errorMessage", "Validation failed: " + errorMessage);
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("errorMessage", "You are not authorized to access this page.");
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage(), ex);

        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            if (cause.getMessage().contains("Duplicate entry")) {
                ModelAndView mav = new ModelAndView("redirect:/donorRegistration");
                mav.addObject("eMsg", "This email or phone number is already registered.");
                return mav;
            }
        }

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public String handleInvalidVerificationCodeException(InvalidVerificationCodeException ex, RedirectAttributes model) {
        log.error("InvalidVerificationCodeException: {}", ex.getMessage(), ex);
        model.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/blood/verify";
    }

    @ExceptionHandler(DonorNotFoundException.class)
    public String handleDonorNotFoundException(DonorNotFoundException ex, RedirectAttributes model) {
        log.error("DonorNotFoundException: {}", ex.getMessage(), ex);
        model.addFlashAttribute("errorMessage", ex.getMessage());
        return RedirectionAndReturn.DONOR_DASHBOARD_RED;
    }
}