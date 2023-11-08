package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionType implements BaseExceptionType {
    ALREADY_EXIST_ID(HttpStatus.CONFLICT, "이미 존재하는 ID입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
