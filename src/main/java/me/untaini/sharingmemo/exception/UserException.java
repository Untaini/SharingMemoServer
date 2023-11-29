package me.untaini.sharingmemo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.untaini.sharingmemo.exception.type.BaseExceptionType;

@Data
@Builder
@AllArgsConstructor
public class UserException extends BaseException {

    private BaseExceptionType exceptionType;

}
