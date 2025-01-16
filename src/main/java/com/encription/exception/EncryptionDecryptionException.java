package com.encription.exception;

public class EncryptionDecryptionException extends RuntimeException{
    public EncryptionDecryptionException(String message, Throwable cause){
        super(message, cause);
    }
}
