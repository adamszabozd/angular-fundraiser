package hu.progmasters.fundraiser.exception;

public class AlreadyConfirmedTransferException extends RuntimeException {
    private final String accountEmail;

    public AlreadyConfirmedTransferException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }
}
