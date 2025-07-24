package com.blood.bank.Blood.bank.exception;

public class DonorNotFoundException extends RuntimeException {
    public DonorNotFoundException(String message) {
        super(message);
    }
}
