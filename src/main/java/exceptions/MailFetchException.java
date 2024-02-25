package exceptions;

public class MailFetchException extends Exception {
    public MailFetchException(String message) {
        super(message);
    }

    public MailFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}