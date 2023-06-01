package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handlerOfValidationException(final ValidationException e) {
        return new ErrorResponse(
            String.format("Ошибка  \"%s\".", e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handlerOfMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ErrorResponse(
            String.format("Ошибка \"%s\".", e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handlerOfConstraintViolationException(final ConstraintViolationException e) {
        return new ErrorResponse(
            String.format("Ошибка \"%s\".", e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handlerOfObjectNotFoundException(final NotFoundException e) {
        return new ErrorResponse(
            String.format("Ошибка \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerExceptions(final Exception e) {
        return new ErrorResponse(
            String.format("Ошибка \"%s\".", e.getMessage())
        );
    }
}