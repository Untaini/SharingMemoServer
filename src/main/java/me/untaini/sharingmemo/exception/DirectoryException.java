package me.untaini.sharingmemo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.untaini.sharingmemo.exception.type.BaseExceptionType;

@Data
@AllArgsConstructor
@Builder
public class DirectoryException extends BaseException {

    private BaseExceptionType exceptionType;

}
