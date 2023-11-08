package me.untaini.sharingmemo.controller.advice;

import me.untaini.sharingmemo.dto.ExceptionDTO;
import me.untaini.sharingmemo.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionDTO> handleBaseException(BaseException baseException) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .message(baseException.getExceptionType().getMessage())
                .build();

        return ResponseEntity
                .status(baseException.getExceptionType().getHttpStatus())
                .body(exceptionDTO);
    }

}
