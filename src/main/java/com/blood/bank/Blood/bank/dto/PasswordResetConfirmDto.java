package com.blood.bank.Blood.bank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmDto {
    private String token;
    private String newPassword;
    private String confirmPassword;
}