package org.karlbennett.jni.exception;

/**
 * User: karl
 * Date: 19/11/11
 *
 * A runtime IO exception that can be thrown without a try/catch being required.
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException() {
        super();
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IORuntimeException(Throwable cause) {
        super(cause);
    }
}
