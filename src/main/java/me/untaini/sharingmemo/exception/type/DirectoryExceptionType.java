package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DirectoryExceptionType implements BaseExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 디렉토리를 찾을 수 없습니다."),
    OWNER_NOT_MATCH(HttpStatus.BAD_REQUEST, "디렉토리를 소유하지 않았습니다."),
    EXIST_SAME_NAME(HttpStatus.CONFLICT, "같은 이름이 디렉토리에 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
