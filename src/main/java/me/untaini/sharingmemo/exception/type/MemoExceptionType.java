package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemoExceptionType implements BaseExceptionType {
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
