package hu.progmasters.fundraiser.exception;

public class TransferNotFoundException extends RuntimeException {

    private final String accountEmail;

    public TransferNotFoundException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }
}
