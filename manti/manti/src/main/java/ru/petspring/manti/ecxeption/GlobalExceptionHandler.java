package ru.petspring.manti.ecxeption;


import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.petspring.manti.ecxeption.error.ErrorDetails;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handlerResourceNotFoundException(ResourceNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDetails(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleGlobalException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDetails handleResourceAlreadyExistsException(ResourceAlreadyExistsException  exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDetails(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValidException(InvalidStatusException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDetails(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
