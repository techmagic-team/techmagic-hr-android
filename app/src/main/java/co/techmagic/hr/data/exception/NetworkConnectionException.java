package co.techmagic.hr.data.exception;

/**
 * Created by techmagic on 4/6/17.
 */

public class NetworkConnectionException extends RuntimeException {


    public NetworkConnectionException() {
    }


    public NetworkConnectionException(String message) {
        super(message);
    }


    public NetworkConnectionException(String message, Throwable cause) {
        super(message, cause);
    }


    public NetworkConnectionException(Throwable cause) {
        super(cause);
    }
}
