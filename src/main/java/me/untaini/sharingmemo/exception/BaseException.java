package me.untaini.sharingmemo.exception;

import me.untaini.sharingmemo.exception.type.BaseExceptionType;

public abstract class BaseException extends RuntimeException {
    public abstract BaseExceptionType getExceptionType();
}
