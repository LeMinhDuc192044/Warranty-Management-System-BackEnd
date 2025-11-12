package com.warrantyclaim.warrantyclaim_api.exception;

public class WarrantyNotEligibleException extends RuntimeException {

    public WarrantyNotEligibleException(String message) {
        super(message);
    }

    public WarrantyNotEligibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
