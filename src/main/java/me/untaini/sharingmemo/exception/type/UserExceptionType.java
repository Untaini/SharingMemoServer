package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionType implements BaseExceptionType {
    ALREADY_EXIST_ID(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    NOT_EXIST_ID(HttpStatus.BAD_REQUEST, "가입 되지 않은 ID입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인하지 않았습니다."),
    ALREADY_LOGIN(HttpStatus.BAD_REQUEST, "이미 로그인 했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
