package com.liaozl.utils.exection;

/**
 * @author liaozuliang
 * @date 2017-08-30
 */
public class BusinessExection extends Exception {

    public BusinessExection() {
        super();
    }

    public BusinessExection(String message) {
        super(message);
    }

    public BusinessExection(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessExection(Throwable cause) {
        super(cause);
    }

    public BusinessExection(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BusinessExection(String message, boolean writableStackTrace) {
        super(message, null, true, writableStackTrace);
    }
}
