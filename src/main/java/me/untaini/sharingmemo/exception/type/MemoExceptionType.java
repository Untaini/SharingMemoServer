package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemoExceptionType implements BaseExceptionType {

    EXIST_SAME_NAME(HttpStatus.CONFLICT, "같은 이름이 디렉토리에 있습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
