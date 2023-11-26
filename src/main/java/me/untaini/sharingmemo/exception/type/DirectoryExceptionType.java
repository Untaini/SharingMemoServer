package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DirectoryExceptionType implements BaseExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 디렉토리를 찾을 수 없습니다."),
    OWNER_NOT_MATCH(HttpStatus.BAD_REQUEST, "디렉토리를 소유하지 않았습니다."),
    EXIST_SAME_NAME(HttpStatus.CONFLICT, "같은 이름이 디렉토리에 있습니다."),
    ALREADY_HAS_SAME_NAME(HttpStatus.BAD_REQUEST, "해당 디렉토리는 이미 그 이름을 가지고 있습니다."),
    CANNOT_CHANGE_ROOT_DIRECTORY_NAME(HttpStatus.BAD_REQUEST, "루트 디렉토리의 이름은 바꿀 수 없습니다."),
    CANNOT_DELETE_ROOT_DIRECTORY(HttpStatus.BAD_REQUEST, "루트 디렉토리는 삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
