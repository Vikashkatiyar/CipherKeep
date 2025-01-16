package com.encription.exception;

import com.encription.CipherKeepApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

      @ExceptionHandler(UserNotFoundException.class)
        ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
          CipherKeepApplication.logger.error("User not found: {}", ex.getMessage(),ex);
          return new ResponseEntity<>(ErrorDetails.builder()
                                     .message(ex.getMessage())
                                      .details(request.getDescription(false))
                  .build(), HttpStatus.NOT_FOUND);
      }


    @ExceptionHandler(EncryptionDecryptionException.class)
    ResponseEntity<ErrorDetails> handleEncryptionDecryptionException(EncryptionDecryptionException ex, WebRequest request){
        CipherKeepApplication.logger.error("Encryption/Decryption error: {}", ex.getMessage(), ex);
          return new ResponseEntity<>(ErrorDetails.builder()
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(SecretNotFoundException.class)
    ResponseEntity<ErrorDetails> handleSecretNotFoundException(SecretNotFoundException ex, WebRequest request){
        CipherKeepApplication.logger.error("Secret not found: {}", ex.getMessage(), ex);
          return new ResponseEntity<>(ErrorDetails.builder()
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request){
        CipherKeepApplication.logger.warn("User already exists: {}", ex.getMessage());
          return new ResponseEntity<>(ErrorDetails.builder()
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    ResponseEntity<ErrorDetails> handleFileNotFoundException(FileNotFoundException ex, WebRequest request){
          CipherKeepApplication.logger.error("File not found: {}", ex.getMessage(), ex);
          return new ResponseEntity<>(ErrorDetails.builder()
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(), HttpStatus.NOT_FOUND);
    }


}
