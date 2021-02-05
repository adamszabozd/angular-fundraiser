package hu.progmasters.fundraiser.exception;

public class ConfirmedTransferDeleteException extends RuntimeException {
    private final String accountEmail;

    public ConfirmedTransferDeleteException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }
}
