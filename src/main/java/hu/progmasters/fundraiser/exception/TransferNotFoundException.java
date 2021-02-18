package hu.progmasters.fundraiser.exception;

public class TransferNotFoundException extends RuntimeException {

    private final String accountEmail;

    public TransferNotFoundException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getCode() {
        return "transfer.not.found";
    }
}
