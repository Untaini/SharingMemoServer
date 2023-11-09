package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionType implements BaseExceptionType {
    ALREADY_EXIST_ID(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    NOT_EXIST_ID(HttpStatus.BAD_REQUEST, "회원가입이 되지 않은 정보입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
