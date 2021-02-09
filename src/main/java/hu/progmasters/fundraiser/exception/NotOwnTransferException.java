package hu.progmasters.fundraiser.exception;

public class NotOwnTransferException extends RuntimeException {

    private final String accountEmail;

    public NotOwnTransferException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

}
