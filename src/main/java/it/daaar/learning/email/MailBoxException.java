package it.daaar.learning.email;

public class MailBoxException extends RuntimeException {
    public MailBoxException(String message, Exception cause) {
        super(message, cause);
    }
}
