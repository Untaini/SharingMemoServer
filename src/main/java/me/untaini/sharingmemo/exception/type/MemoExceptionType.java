package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemoExceptionType implements BaseExceptionType {
    ;

    private HttpStatus httpStatus;
    private String message;
}
