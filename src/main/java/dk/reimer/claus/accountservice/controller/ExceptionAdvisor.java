package dk.reimer.claus.accountservice.controller;

import dk.reimer.claus.accountservice.AccountServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@ControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AccountServiceException.class})
    public ResponseEntity<Object> exception(AccountServiceException exception) {
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> exception(Exception exception) {
        return new ResponseEntity<>(new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), exception.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public record Error(int code, Date date, String message) {}
}
