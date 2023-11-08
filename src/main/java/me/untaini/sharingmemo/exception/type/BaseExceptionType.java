package me.untaini.sharingmemo.exception.type;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus getHttpStatus();
    String getMessage();

}
