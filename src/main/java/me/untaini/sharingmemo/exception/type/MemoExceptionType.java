package me.untaini.sharingmemo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemoExceptionType implements BaseExceptionType {

    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메모를 찾을 수 없습니다."),
    OWNER_NOT_MATCH(HttpStatus.BAD_REQUEST, "메모를 소유하지 않았습니다."),
    EXIST_SAME_NAME(HttpStatus.CONFLICT, "같은 이름이 디렉토리에 있습니다."),
    ALREADY_HAS_SAME_NAME(HttpStatus.BAD_REQUEST, "해당 메모는 이미 그 이름을 가지고 있습니다."),
    CANNOT_DELETE_BECAUSE_OF_OPENING(HttpStatus.BAD_REQUEST, "해당 메모는 편집중이기 때문에 삭제할 수 없습니다."),
    EXIST_OPENED_DESCENDENT_MEMO(HttpStatus.BAD_REQUEST, "편집중인 메모가 있기 때문에 디렉토리를 삭제할 수 없습니다."),
    ALREADY_OPEN(HttpStatus.BAD_REQUEST, "해당 메모는 이미 편집중입니다."),
    CANNOT_OPEN_MORE(HttpStatus.BAD_REQUEST, "한 번에 메모 하나만 편집할 수 있습니다."),
    MEMO_NOT_OPEN(HttpStatus.BAD_REQUEST, "편집중인 메모가 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
