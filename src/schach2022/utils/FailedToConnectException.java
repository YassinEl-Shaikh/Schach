package schach2022.utils;

public class FailedToConnectException extends RuntimeException {
    public FailedToConnectException(String message) {
        super(message);
    }
}