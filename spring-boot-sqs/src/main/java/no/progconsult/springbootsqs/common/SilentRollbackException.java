package no.progconsult.springbootsqs.common;

public class SilentRollbackException extends RuntimeException {
    public SilentRollbackException(String message) {
        super(message);
    }

    public SilentRollbackException() {
        super("");
    }
}
