package hu.progmasters.fundraiser.exception;

public class InvalidConfirmationCodeException extends RuntimeException {

    private final String accountEmail;

    public InvalidConfirmationCodeException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }
}
