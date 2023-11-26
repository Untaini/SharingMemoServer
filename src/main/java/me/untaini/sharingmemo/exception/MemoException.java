package me.untaini.sharingmemo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.untaini.sharingmemo.exception.type.BaseExceptionType;

@Getter
@Builder
@AllArgsConstructor
public class MemoException extends BaseException {

    private BaseExceptionType exceptionType;

}
