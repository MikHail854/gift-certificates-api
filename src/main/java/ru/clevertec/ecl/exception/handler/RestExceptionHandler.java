package ru.clevertec.ecl.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.constants.Constants;
import ru.clevertec.ecl.exception.ErrorResponse;
import ru.clevertec.ecl.utils.MessageUtil;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerEntityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_ENTITY_NOT_FOUND_EXCEPTION, "handlerEntityNotFoundException", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_ILLEGAL_ARGUMENT_EXCEPTION, "handlerIllegalArgumentException", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "handlerMethodArgumentNotValidException", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handlerDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_DATA_INTEGRITY_VIOLATION_EXCEPTION, "handlerDataIntegrityViolationException", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, HttpStatus.INTERNAL_SERVER_ERROR, "handlerException", e.getMessage());
    }

    private ResponseEntity<?> createResponseEntity(HttpServletRequest request, HttpStatus status, String code, Object... objects) {
        return ResponseEntity.status(status.value())
                .body(ErrorResponse.builder()
                        .path(request.getRequestURI())
                        .message(Objects.nonNull(code) ? messageUtil.getMessage(code, objects) : String.valueOf(objects[0]))
                        .error(status.getReasonPhrase())
                        .status(status.value())
                        .timestamp(new Date())
                        .build());
    }

    private ResponseEntity<?> createResponseEntity(HttpServletRequest request, int status, String code, Object... objects) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .path(request.getRequestURI())
                        .message(Objects.nonNull(code) ? messageUtil.getMessage(code, objects) : String.valueOf(objects[0]))
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .status(status)
                        .timestamp(new Date())
                        .build());
    }

}
