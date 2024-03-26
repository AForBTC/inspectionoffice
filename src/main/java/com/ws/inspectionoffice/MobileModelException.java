package com.ws.inspectionoffice;

public class MobileModelException extends RuntimeException{
    public MobileModelException() {
        super();
}

    public MobileModelException(String message) {
        super(message);
    }

    public MobileModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public MobileModelException(Throwable cause) {
        super(cause);
    }

    protected MobileModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
